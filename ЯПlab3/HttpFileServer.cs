using System;
using System.Collections.Generic;
using System.IO;
using System.Net;
using System.Threading;

namespace lab_3_final
{
    public class HttpFileServer
    {
        private readonly HttpListener _listener;
        private readonly Queue<HttpListenerContext> _requestQueue;
        private readonly object _queueLock = new object();
        private readonly Thread[] _workerThreads;
        private readonly int _workerCount;
        private readonly string _baseDirectory;
        private readonly int _port;
        private volatile bool _isRunning;
        private readonly ManualResetEvent _shutdownEvent;

        public HttpFileServer(int port, string baseDirectory, int workerCount = 5)
        {
            _port = port;
            _baseDirectory = Path.GetFullPath(baseDirectory);
            _workerCount = workerCount;

            _listener = new HttpListener();
            _requestQueue = new Queue<HttpListenerContext>();
            _workerThreads = new Thread[_workerCount];
            _isRunning = false;
            _shutdownEvent = new ManualResetEvent(false);

            _listener.Prefixes.Add($"http://localhost:{_port}/");
            

            Console.WriteLine($"Server configured:");
            Console.WriteLine($"  Port: {_port}");
            Console.WriteLine($"  Directory: {_baseDirectory}");
            Console.WriteLine($"  Worker threads: {_workerCount}");
        }

        public void Start()
        {
            if (_isRunning) return;

            try
            {
                _listener.Start();
                _isRunning = true;
                _shutdownEvent.Reset();

                Console.WriteLine("Starting worker threads...");

                for (int i = 0; i < _workerCount; i++)
                {
                    _workerThreads[i] = new Thread(WorkerThreadProc);
                    _workerThreads[i].Name = $"Worker-{i}";
                    _workerThreads[i].IsBackground = true;
                    _workerThreads[i].Start();
                    Console.WriteLine($"  Started {_workerThreads[i].Name}");
                }

                Thread listenerThread = new Thread(ListenerThreadProc);
                listenerThread.Name = "ListenerThread";
                listenerThread.IsBackground = true;
                listenerThread.Start();

                Console.WriteLine($"Server started successfully!");
                Console.WriteLine($"Access URLs:");
                Console.WriteLine($"  http://localhost:{_port}/");
                Console.WriteLine();
                Console.WriteLine("Press 'Q' to stop server, 'L' to list files");
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Failed to start server: {ex.Message}");
                throw;
            }
        }

        public void Stop()
        {
            if (!_isRunning) return;
            _isRunning = false;
            _shutdownEvent.Set();

            _listener?.Stop();

            foreach (var worker in _workerThreads)
            {
                worker?.Join(TimeSpan.FromSeconds(3));
            }

            Console.WriteLine("Server stopped.");
        }

        private void ListenerThreadProc()
        {
            Console.WriteLine("Listener thread started - waiting for connections...");

            while (_isRunning)
            {
                try
                {
                    var context = _listener.GetContext();
                    lock (_queueLock)
                    {
                        _requestQueue.Enqueue(context);
                    }
                    Console.WriteLine($"New request queued: {context.Request.Url}");
                }
                catch (HttpListenerException)
                {
                    break;
                }
                catch (Exception e)
                {
                    if (_isRunning)
                        Console.WriteLine($"Listener error: {e.Message}");
                }
            }
            Console.WriteLine("Listener thread stopped.");
        }

        private void WorkerThreadProc()
        { 
            Console.WriteLine($"{Thread.CurrentThread.Name} started");

            while (_isRunning || _requestQueue.Count > 0)
            {
                try
                {
                    HttpListenerContext context = null;
                    lock (_queueLock)
                    {
                        if (_requestQueue.Count > 0)
                        {
                            context = _requestQueue.Dequeue();
                        }
                    }

                    if (context != null)
                    {
                        ProcessRequest(context);
                    }
                    else
                    {
                        Thread.Sleep(10);
                    }
                }
                catch (Exception e)
                {
                    Console.WriteLine($"{Thread.CurrentThread.Name} error: {e.Message}");
                }
            }
            Console.WriteLine($"{Thread.CurrentThread.Name} stopped");
        }

        private void ProcessRequest(HttpListenerContext context)
        {
            var request = context.Request;
            var response = context.Response;

            try
            {
                Console.WriteLine($"{Thread.CurrentThread.Name} processing: {request.Url}");

                string requestedPath = Uri.UnescapeDataString(request.Url.AbsolutePath.Trim('/'));

                if (string.IsNullOrEmpty(requestedPath))
                {
                    requestedPath = "index.html";
                }

                string fullPath = Path.GetFullPath(Path.Combine(_baseDirectory, requestedPath));

                if (!fullPath.StartsWith(_baseDirectory, StringComparison.OrdinalIgnoreCase))
                {
                    SendErrorResponse(response, 403, "Forbidden: Path traversal attempt");
                    return;
                }

                if (!File.Exists(fullPath))
                {
                    SendErrorResponse(response, 404, $"File not found: {requestedPath}");
                    return;
                }

                var fileInfo = new FileInfo(fullPath);
                string contentType = GetContentType(fullPath);

                response.ContentType = contentType;
                response.ContentLength64 = fileInfo.Length;
                response.StatusCode = 200;

                using (var fileStream = new FileStream(fullPath, FileMode.Open, FileAccess.Read, FileShare.Read))
                {
                    byte[] buffer = new byte[65536]; 
                    int bytesRead;
    
                    while ((bytesRead = fileStream.Read(buffer, 0, buffer.Length)) > 0)
                    {
                        response.OutputStream.Write(buffer, 0, bytesRead);
                    }
                }

                Console.WriteLine($"{Thread.CurrentThread.Name} sent: {requestedPath} ({fileInfo.Length} bytes)");
            }
            catch (Exception e)
            {
                Console.WriteLine($"{Thread.CurrentThread.Name} request error: {e.Message}");
                SendErrorResponse(response, 500, "Internal server error");
            }
            finally
            {
                response.OutputStream.Close();
            }
        }

        private void SendErrorResponse(HttpListenerResponse response, int statusCode, string message)
        {
            response.StatusCode = statusCode;
            using (var writer = new StreamWriter(response.OutputStream))
            {
                writer.Write($"{statusCode} - {message}");
            }
        }

        private string GetContentType(string filePath)
        {
            string extension = Path.GetExtension(filePath).ToLowerInvariant();

            switch (extension)
            {
                case ".html":
                case ".htm":
                    return "text/html";
                case ".css":
                    return "text/css";
                case ".js":
                    return "application/javascript";
                case ".json":
                    return "application/json";
                case ".png":
                    return "image/png";
                case ".jpg":
                case ".jpeg":
                    return "image/jpeg";
                case ".gif":
                    return "image/gif";
                case ".bmp":
                    return "image/bmp";
                case ".txt":
                    return "text/plain";
                case ".pdf":
                    return "application/pdf";
                case ".zip":
                    return "application/zip";
                case ".xml":
                    return "application/xml";
                case ".csv":
                    return "text/csv";
                default:
                    return "application/octet-stream";
            }
        }

        public void ListFiles()
        {
            Console.WriteLine($"Files in {_baseDirectory}:");
            
            if (!Directory.Exists(_baseDirectory))
            {
                Console.WriteLine("  Directory does not exist!");
                return;
            }

            var files = Directory.GetFiles(_baseDirectory);
            foreach (var file in files)
            {
                var info = new FileInfo(file);
                Console.WriteLine($"  /{Path.GetFileName(file)} ({info.Length} bytes)");
            }

            if (files.Length == 0)
            {
                Console.WriteLine("  No files found!");
            }
        }
    }
}
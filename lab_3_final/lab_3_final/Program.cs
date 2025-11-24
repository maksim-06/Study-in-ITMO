using System;
using System.IO;

namespace lab_3_final
{
    internal class Program
    {
        public static void Main(string[] args)
        {
            Console.WriteLine("=== HTTP File Server ===");

            if (args.Length < 2)
            {
                Console.WriteLine("Usage: HttpFileServer <port> <directory> [workerCount]");
                Console.WriteLine("Default worker count: 5");
                return;
            }

            if (!int.TryParse(args[0], out int port) || port < 1 || port > 65535)
            {
                Console.WriteLine("Error: Invalid port number");
                return;
            }
            
            string directory = args[1];
            int workerCount = 5;
            
            if (args.Length >= 3 && int.TryParse(args[2], out int customWorkers))
            {
                workerCount = customWorkers;
            }

            if (!Directory.Exists(directory))
            {
                Console.WriteLine($"Error: Directory '{directory}' does not exist");
                return;
            }


            var server = new HttpFileServer(port, directory, workerCount);
            try
            {
                server.Start();
                server.ListFiles();
                Console.WriteLine();

                bool running = true;
                while (running)
                {
                    var key = Console.ReadKey(true).Key;

                    switch (key)
                    {
                        case ConsoleKey.Q:
                            Console.WriteLine("Shutting down...");
                            running = false;
                            break;

                        case ConsoleKey.L:
                            server.ListFiles();
                            break;

                        case ConsoleKey.H:
                            ShowHelp();
                            break;
                    }
                }
            }
            finally
            {
                server.Stop();
            }
        }
        
        private static void ShowHelp()
        {
            Console.WriteLine();
            Console.WriteLine("Available commands:");
            Console.WriteLine("  Q - Stop server and exit");
            Console.WriteLine("  L - List available files");
            Console.WriteLine("  H - Show this help");
            Console.WriteLine();
        }
    }

}

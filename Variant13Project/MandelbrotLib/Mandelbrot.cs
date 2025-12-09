using System;

namespace MandelbrotLib
{
    public class Mandelbrot
    {
        public static int[,] Calculate(int width, int height, double zoom, double offsetX, double offsetY, int maxIter)
        {
            int[,] pixels = new int[width, height];
            
            for (int x = 0; x < width; x++)
            {
                for (int y = 0; y < height; y++)
                {
                    double zx = 0, zy = 0;
                    double cx = (x - width / 2.0) / zoom + offsetX;
                    double cy = (y - height / 2.0) / zoom + offsetY;
                    
                    int iteration = 0;
                    while (zx * zx + zy * zy < 4 && iteration < maxIter)
                    {
                        double tmp = zx * zx - zy * zy + cx;
                        zy = 2.0 * zx * zy + cy;
                        zx = tmp;
                        iteration++;
                    }
                    
                    pixels[x, y] = iteration;
                }
            }
            
            return pixels;
        }
    }
}
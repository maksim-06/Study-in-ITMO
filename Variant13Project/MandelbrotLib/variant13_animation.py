import os
import sys
import clr
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation


DLL_PATH = r"C:/Users/nekru/Variant13Project/MandelbrotLib/bin/Release/net8.0/MandelbrotLib.dll"

if not os.path.exists(DLL_PATH):
    raise SystemExit(f"DLL не найдена: {DLL_PATH}")

sys.path.append(os.path.dirname(DLL_PATH))
clr.AddReference(DLL_PATH)
from MandelbrotLib import Mandelbrot


WIDTH, HEIGHT = 600, 450       
MAX_ITER = 300                 
FPS = 10                       
TOTAL_FRAMES = 80              

center_x, center_y = -0.745, 0.186

initial_zoom = 150.0
zoom_factor_per_frame = 1.26   

fig, ax = plt.subplots(figsize=(10, 7.5))
ax.axis('off')
im = ax.imshow(
    np.zeros((HEIGHT, WIDTH)),
    cmap='magma',
    origin='lower',
    vmin=0,
    vmax=MAX_ITER
)


def animate(frame):
    current_zoom = initial_zoom * (zoom_factor_per_frame ** frame)
    pixels = Mandelbrot.Calculate(WIDTH, HEIGHT, current_zoom, center_x, center_y, MAX_ITER)

    arr = np.empty((WIDTH, HEIGHT), dtype=np.int32)
    for x in range(WIDTH):
        for y in range(HEIGHT):
            arr[x, y] = pixels[x, y]
    
    im.set_array(arr.T)
    print(f"🎬 Кадр {frame + 1:02d}/{TOTAL_FRAMES} | zoom = {current_zoom:.2e}")
    return [im]


print("🚀 Начинаю генерацию анимации...")
anim = FuncAnimation(
    fig, animate,
    frames=TOTAL_FRAMES,
    interval=1000 // FPS,
    blit=True,
    repeat=True  
)

plt.show()
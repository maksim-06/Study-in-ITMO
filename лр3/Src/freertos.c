#include "FreeRTOS.h"
#include "task.h"
#include "main.h"
#include "cmsis_os.h"
#include "usart.h"
#include <string.h>
#include <stdlib.h>
#include "kb.h"
#include "stm32f4xx_hal.h"
#include "oled.h"

/* Импортируем мьютекс из main.c */
extern osMutexId OLED_I2C_MutexHandle;
extern I2C_HandleTypeDef hi2c1;

typedef struct { int x; int y; } Point;

#define FIELD_WIDTH  32
#define FIELD_HEIGHT 16
#define SNAKE_MAX_LEN 40

Point snake[SNAKE_MAX_LEN];
int snake_len = 3;
Point apple = {15, 8};
uint32_t current_dir = 6;
uint8_t game_over = 0;

osThreadId Task_LED1Handle;
osThreadId Task_UARTHandle;
osThreadId Task_LED2Handle;
osMessageQId myQueue01Handle;

void StartTaskLED1(void const * argument);
void StartTaskUART(void const * argument);
void StartTasLED2(void const * argument);

void configureTimerForRunTimeStats(void) {}
unsigned long getRunTimeCounterValue(void) { return HAL_GetTick(); }

static StaticTask_t xIdleTaskTCBBuffer;
static StackType_t xIdleStack[configMINIMAL_STACK_SIZE];
  
void vApplicationGetIdleTaskMemory( StaticTask_t **ppxIdleTaskTCBBuffer, StackType_t **ppxIdleTaskStackBuffer, uint32_t *pulIdleTaskStackSize ) {
  *ppxIdleTaskTCBBuffer = &xIdleTaskTCBBuffer;
  *ppxIdleTaskStackBuffer = &xIdleStack[0];
  *pulIdleTaskStackSize = configMINIMAL_STACK_SIZE;
}                   

void MX_FREERTOS_Init(void) {
    osMessageQDef(myQueue01, 16, uint32_t);
    myQueue01Handle = osMessageCreate(osMessageQ(myQueue01), NULL);

    osThreadDef(Task_LED1, StartTaskLED1, osPriorityAboveNormal, 0, 128);
    Task_LED1Handle = osThreadCreate(osThread(Task_LED1), NULL);

    osThreadDef(Task_UART, StartTaskUART, osPriorityLow, 0, 256);
    Task_UARTHandle = osThreadCreate(osThread(Task_UART), NULL);

    osThreadDef(Task_LED2, StartTasLED2, osPriorityNormal, 0, 256);
    Task_LED2Handle = osThreadCreate(osThread(Task_LED2), NULL);
}

char scanKeypad(void) {
  static uint32_t lastScan = 0;
  if (xTaskGetTickCount() - lastScan < 50) return '\0';
  lastScan = xTaskGetTickCount();

  uint8_t Row[4] = {ROW4, ROW3, ROW2, ROW1};
  const char keymap[4][3] = {
    {'*', '0', '#'},
    {'7', '8', '9'},
    {'4', '5', '6'},
    {'1', '2', '3'}
  };

  for (int i = 0; i < 4; i++) {
    uint8_t Key = Check_Row(Row[i]);
    if (Key != 0) {
      char detectedKey = '\0';
      if (Key == 0x04)      detectedKey = keymap[i][0];
      else if (Key == 0x02) detectedKey = keymap[i][1];
      else if (Key == 0x01) detectedKey = keymap[i][2];

      if (detectedKey != '\0') return detectedKey;
    }
  }
  return '\0';
}

uint8_t get_snake_direction(void) {
    char key = scanKeypad();
    if (key == '2') return 2;
    if (key == '4') return 4;
    if (key == '6') return 6;
    if (key == '8') return 8;
    if (key == '5') return 5;
    return 0;
}

/* ==================== 1. ЗАДАЧА КЛАВИАТУРЫ ==================== */
void StartTaskLED1(void const * argument) {
  uint8_t dir = 0;
  for(;;) {
      if (osMutexWait(OLED_I2C_MutexHandle, 20) == osOK) {
          dir = get_snake_direction();
          osMutexRelease(OLED_I2C_MutexHandle);
      }

	  if (dir == 2 || dir == 4 || dir == 6 || dir == 8) {
		  if (!((dir == 2 && current_dir == 8) || (dir == 8 && current_dir == 2) ||
				(dir == 4 && current_dir == 6) || (dir == 6 && current_dir == 4))) {
			  osMessagePut(myQueue01Handle, (uint32_t)dir, 0);
		  }
	  }
	  if (dir == 5 && game_over) {
		  game_over = 0;
		  snake_len = 3;
		  current_dir = 6;
		  for(int i = 0; i < snake_len; i++) {
			  snake[i].x = 10 - i;
			  snake[i].y = 8;
		  }
	  }
	  osDelay(40);
  }
}

/* ==================== 2. ЗАДАЧА ДИСПЛЕЯ ==================== */
void StartTaskUART(void const * argument) {
  osMutexWait(OLED_I2C_MutexHandle, osWaitForever);
  oled_Init();
  uint8_t fix_mode[] = {0x20, 0x00};
  HAL_I2C_Mem_Write(&hi2c1, OLED_I2C_ADDR, 0x00, 1, fix_mode, 2, 10);
  osMutexRelease(OLED_I2C_MutexHandle);

  for(;;) {
		oled_Fill(Black);
		if (game_over) {
			// Отрисовка экрана окончания игры
			oled_SetCursor(25, 8);
			oled_WriteString("GAME OVER!", Font_11x18, White);

			int score = snake_len - 3;
			char score_str[10] = "Score:  ";
			score_str[7] = (score / 10) + '0';
			score_str[8] = (score % 10) + '0';

			oled_SetCursor(40, 30);
			oled_WriteString(score_str, Font_7x10, White);

			// Подсказка о перезапуске
			oled_SetCursor(12, 48);
			oled_WriteString("Press 5 to Restart", Font_7x10, White);
		} else {
			oled_DrawSquare(apple.x * 4, apple.x * 4 + 3, apple.y * 4, apple.y * 4 + 3, White);
			for (int i = 0; i < snake_len; i++) {
				oled_DrawSquare(snake[i].x * 4, snake[i].x * 4 + 3, snake[i].y * 4, snake[i].y * 4 + 3, White);
			}
		}

        if (osMutexWait(OLED_I2C_MutexHandle, 50) == osOK) {
            oled_UpdateScreen();
            osMutexRelease(OLED_I2C_MutexHandle);
        }
		osDelay(50);
  }
}

/* ==================== 3. ЗАДАЧА ЛОГИКИ ИГРЫ ==================== */
void StartTasLED2(void const * argument) {
  for(int i = 0; i < snake_len; i++) { snake[i].x = 10 - i; snake[i].y = 8; }
  osEvent event;


  uint32_t game_speed = 213;

  for(;;) {
	  if (!game_over) {
		  event = osMessageGet(myQueue01Handle, 5);
		  if (event.status == osEventMessage) {
			  current_dir = event.value.v;
		  }

		  Point last_tail_pos = snake[snake_len - 1];

		  for (int i = snake_len - 1; i > 0; i--) { snake[i] = snake[i - 1]; }

		  if (current_dir == 2) snake[0].y--;
		  else if (current_dir == 8) snake[0].y++;
		  else if (current_dir == 4) snake[0].x--;
		  else if (current_dir == 6) snake[0].x++;

		  if (snake[0].x < 0 || snake[0].x >= FIELD_WIDTH || snake[0].y < 0 || snake[0].y >= FIELD_HEIGHT) {
			  game_over = 1;
		  }
		  for (int i = 1; i < snake_len; i++) {
			  if (snake[0].x == snake[i].x && snake[0].y == snake[i].y) game_over = 1;
		  }

		  if (snake[0].x == apple.x && snake[0].y == apple.y) {
			  if (snake_len < SNAKE_MAX_LEN) {
				  snake[snake_len] = last_tail_pos;
				  snake_len++;
			  }

			  /* МАТЕМАТИКА ДИНАМИЧЕСКОГО РАЗГОНА:
			     С каждым новым яблоком базовый шаг (213 мс) уменьшается на 7 миллисекунд. */
			  int current_score = snake_len - 3;
			  int calculated_speed = 213 - (current_score * 7);


			  if (calculated_speed < 63) {
				  calculated_speed = 63;
			  }

			  /* ЗАЩИТА ШИНЫ ОТ ЧЕТНЫХ ЧИСЕЛ:
				 Если в ходе разгона получилось четное число (например 180, 150),
				 принудительно делаем его нечетным (+1), уводя фазу от коллизий с I2C */
			  if (calculated_speed % 2 == 0) {
				  calculated_speed += 1;
			  }

			  game_speed = calculated_speed;

			  // На основе тиков процессора, что защищает систему от зависания!
			  uint32_t ticks = xTaskGetTickCount();

			  int valid_space = 0;
			  while (!valid_space) {
				  apple.x = (ticks + snake_len) % FIELD_WIDTH;
				  apple.y = (ticks * 3 + snake_len) % FIELD_HEIGHT;

				  // Защита от выхода за границы на всякий случай
				  if (apple.x < 0) apple.x = 0;
				  if (apple.y < 0) apple.y = 0;

				  valid_space = 1;
				  for (int i = 0; i < snake_len; i++) {
					  if (apple.x == snake[i].x && apple.y == snake[i].y) {
						  valid_space = 0;
						  ticks += 7; // Сдвигаем шаг, если попали в тело
						  break;
					  }
				  }
			  }
		  }
	  } else {
		  game_speed = 213;
	  }

	  osDelay(game_speed);
  }
}


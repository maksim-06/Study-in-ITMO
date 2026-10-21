/* USER CODE BEGIN Header */
/**
  ******************************************************************************
  * @file           : main.c
  * @brief          : Main program body (Timer with Buzzer on PE9)
  ******************************************************************************
  */
/* USER CODE END Header */

/* Includes ------------------------------------------------------------------*/
#include "main.h"
#include "i2c.h"
#include "usart.h"
#include "gpio.h"

/* Private includes ----------------------------------------------------------*/
/* USER CODE BEGIN Includes */
#include "kb.h"
#include "sdk_uart.h"
#include "pca9538.h"
#include "oled.h"
#include "fonts.h"
#include <stdio.h> // Для sprintf
/* USER CODE END Includes */

/* Private typedef -----------------------------------------------------------*/
/* USER CODE BEGIN PTD */

/* USER CODE END PTD */

/* Private define ------------------------------------------------------------*/
/* USER CODE BEGIN PD */

/* USER CODE END PD */

/* Private macro -------------------------------------------------------------*/
/* USER CODE BEGIN PM */

/* USER CODE END PM */

/* Private variables ---------------------------------------------------------*/

/* USER CODE BEGIN PV */
uint16_t timerValue = 0;       // Текущее значение таймера в секундах
uint8_t timerStarted = 0;      // Флаг работы: 1 - запущен, 0 - пауза/ввод
uint32_t lastDecrementTick = 0;// Время прошлого уменьшения секунды
/* USER CODE END PV */

/* Private function prototypes -----------------------------------------------*/
void SystemClock_Config(void);
/* USER CODE BEGIN PFP */
char scanKeypad(void);
void updateDisplay(void);
void initBuzzerGPIO(void);
void playAlarmSound(void);
/* USER CODE END PFP */

/* Private user code ---------------------------------------------------------*/
/* USER CODE BEGIN 0 */

/* USER CODE END 0 */

/**
  * @brief  The application entry point.
  * @retval int
  */
int main(void)
{
  /* MCU Configuration--------------------------------------------------------*/
  HAL_Init();

  /* Configure the system clock */
  SystemClock_Config();

  /* Initialize all configured peripherals */
  MX_GPIO_Init();
  MX_I2C1_Init();
  MX_USART6_UART_Init();

  /* USER CODE BEGIN 2 */
  oled_Init();
  initBuzzerGPIO(); // Настраиваем PE9 на выход для управления пищалкой

  // Первоначальная очистка и вывод на OLED
  oled_Fill(Black);
  updateDisplay();

  UART_Transmit((uint8_t*)"Timer Project Ready\n");
  /* USER CODE END 2 */

  /* Infinite loop */
  /* USER CODE BEGIN WHILE */
  while (1)
  {
    /* USER CODE END WHILE */

    /* USER CODE BEGIN 3 */
    char key = scanKeypad(); // Опрашиваем клавиатуру стенда

    // Кнопка '*' — сброс таймера и выключение звука в любой момент
    if (key == '*') {
      timerStarted = 0;
      timerValue = 0;
      HAL_GPIO_WritePin(GPIOE, GPIO_PIN_9, GPIO_PIN_RESET); // Глушим звук
      updateDisplay();
      UART_Transmit((uint8_t*)"Reset pressed\n");
    }

    // РЕЖИМ НАСТРОЙКИ (Ввод секунд)
    if (timerStarted == 0) {
      if (key >= '0' && key <= '9') {
        if (timerValue < 1000) { // Ограничение на 4 знака (макс 9999)
          timerValue = (timerValue * 10) + (key - '0');
          updateDisplay();
        }
      }
      // Кнопка '#' — Запуск обратного отсчета
      else if (key == '#') {
        if (timerValue > 0) {
          timerStarted = 1;
          lastDecrementTick = HAL_GetTick(); // Фиксируем стартовое время
          UART_Transmit((uint8_t*)"Timer Started\n");
          updateDisplay();
        }
      }
    }
    // РЕЖИМ ОБРАТНОГО ОТСЧЕТА
    else {
      if (timerValue > 0) {
        // Проверяем, прошла ли ровно 1 секунда (1000 мс)
        if (HAL_GetTick() - lastDecrementTick >= 1000) {
          lastDecrementTick += 1000;
          timerValue--;
          updateDisplay();
        }
      }

      // ВРЕМЯ ИСТЕКЛО: Подаем звуковой сигнал
      if (timerValue == 0) {
        playAlarmSound(); // Генерируем прерывистый программный писк
      }
    }

    HAL_Delay(5); // Минимальная пауза для стабильной работы бесконечного цикла
  }
  /* USER CODE END 3 */
}

/**
  * @brief System Clock Configuration
  * @retval None
  */
void SystemClock_Config(void)
{
  RCC_OscInitTypeDef RCC_OscInitStruct = {0};
  RCC_ClkInitTypeDef RCC_ClkInitStruct = {0};

  __HAL_RCC_PWR_CLK_ENABLE();
  __HAL_PWR_VOLTAGESCALING_CONFIG(PWR_REGULATOR_VOLTAGE_SCALE1);

  RCC_OscInitStruct.OscillatorType = RCC_OSCILLATORTYPE_HSE;
  RCC_OscInitStruct.HSEState = RCC_HSE_ON;
  RCC_OscInitStruct.PLL.PLLState = RCC_PLL_ON;
  RCC_OscInitStruct.PLL.PLLSource = RCC_PLLSOURCE_HSE;
  RCC_OscInitStruct.PLL.PLLM = 25;
  RCC_OscInitStruct.PLL.PLLN = 336;
  RCC_OscInitStruct.PLL.PLLP = RCC_PLLP_DIV2;
  RCC_OscInitStruct.PLL.PLLQ = 4;
  if (HAL_RCC_OscConfig(&RCC_OscInitStruct) != HAL_OK)
  {
    Error_Handler();
  }

  RCC_ClkInitStruct.ClockType = RCC_CLOCKTYPE_HCLK|RCC_CLOCKTYPE_SYSCLK

                              |RCC_CLOCKTYPE_PCLK1|RCC_CLOCKTYPE_PCLK2;
  RCC_ClkInitStruct.SYSCLKSource = RCC_SYSCLKSOURCE_PLLCLK;
  RCC_ClkInitStruct.AHBCLKDivider = RCC_SYSCLK_DIV1;
  RCC_ClkInitStruct.APB1CLKDivider = RCC_HCLK_DIV4;
  RCC_ClkInitStruct.APB2CLKDivider = RCC_HCLK_DIV2;

  if (HAL_RCC_ClockConfig(&RCC_ClkInitStruct, FLASH_LATENCY_5) != HAL_OK)
  {
    Error_Handler();
  }
}

/* USER CODE BEGIN 4 */
void Error_Handler(void)
{
  __disable_irq();
  while (1)
  {
      // Если что-то сломалось, контроллер зависнет здесь
  }
}


/**
  * @brief  Настройка пина PE9 (Buzzer) на выход напрямую через регистры
  */
void initBuzzerGPIO(void) {
  // Включаем тактирование порта GPIOE
  __HAL_RCC_GPIOE_CLK_ENABLE();

  // Настраиваем PE9 как обычный цифровой выход (Output)
  GPIOE->MODER = (GPIOE->MODER & ~(3U << (9 * 2))) | (1U << (9 * 2));
  GPIOE->OTYPER &= ~(1 << 9);
  GPIOE->OSPEEDR |= (3U << (9 * 2));

  // Изначально выключаем звук подачей низкого уровня
  HAL_GPIO_WritePin(GPIOE, GPIO_PIN_9, GPIO_PIN_RESET);
}

/**
  * @brief  Функция сканирования клавиатуры ИТМО-стенда (на основе ИТМО-драйвера)
  * @retval Символ нажатой кнопки ('0'-'9', '#', '*') или '\0'
  */
char scanKeypad(void) {
  static uint32_t lastScan = 0;
  static char lastKeyStr = '\0';

  // Опрашиваем раз в 80 мс, чтобы отсечь дребезг контактов
  if (HAL_GetTick() - lastScan < 80) return '\0';
  lastScan = HAL_GetTick();

  // Оригинальная раскладка ИТМО стенда: от ROW4 к ROW1 (снизу вверх)
  uint8_t Row[4] = {ROW4, ROW3, ROW2, ROW1};
  const char keymap[4][3] = {
    {'*', '0', '#'}, // ROW4
    {'7', '8', '9'}, // ROW3
    {'4', '5', '6'}, // ROW2
    {'1', '2', '3'}  // ROW1
  };

  for (int i = 0; i < 4; i++) {
    uint8_t Key = Check_Row(Row[i]); // Родная ИТМО-функция чтения байта расширителя
    if (Key != 0) {
      char detectedKey = '\0';
      if (Key == 0x04)      detectedKey = keymap[i][0]; // Левый столбец (C1)
      else if (Key == 0x02) detectedKey = keymap[i][1]; // Центральный столбец (C2)
      else if (Key == 0x01) detectedKey = keymap[i][2]; // Правый столбец (C3)

      // Защита от зажимания кнопки пальцем
      if (detectedKey != '\0' && detectedKey != lastKeyStr) {
        lastKeyStr = detectedKey;
        return detectedKey;
      }
      return '\0';
    }
  }
  lastKeyStr = '\0'; // Клавиша полностью отпущена
  return '\0';
}

/**
  * @brief  Обновление и форматирование информации на OLED экране
  */
void updateDisplay(void) {
  char str[32];
  oled_Fill(Black); // Полностью очищаем видеобуфер дисплея

  // Верхняя статусная строка
  oled_SetCursor(5, 5);
  if (timerStarted) {
    oled_WriteString("COUNTDOWN...", Font_7x10, White);
  } else if (timerValue == 0 && lastDecrementTick != 0) {
    oled_SetCursor(5, 5);
    oled_WriteString("!!! TIME IS UP !!!", Font_7x10, White);
  } else {
    oled_WriteString("SET SECONDS:", Font_7x10, White);
  }

  // Центральный вывод крупного таймера
  sprintf(str, "%04d sec", timerValue);
  oled_SetCursor(35, 26); // Новые координаты для среднего шрифта
  oled_WriteString(str, Font_11x18, White); // Заменили размер шрифта на 11x18

  oled_SetCursor(5, 48); // <---
  oled_WriteString("# - START", Font_7x10, White); // <---
  oled_SetCursor(75, 48); // <---
  oled_WriteString("* - RESET", Font_7x10, White); // <---

  oled_UpdateScreen(); // Физически перерисовываем видеопамять в контроллер дисплея
}

/**
  * @brief  Генерация прерывистого звукового сигнала программным ШИМ-методом
  */
void playAlarmSound(void) {
  static uint32_t lastToggle = 0;
  static uint8_t soundState = 0;

  // Каждые 250 мс переключаем пищалку (250 мс пищит, 250 мс молчит)
  if (HAL_GetTick() - lastToggle >= 250) {
    lastToggle = HAL_GetTick();
    soundState = !soundState;
  }

  if (soundState) {
    // Быстро дергаем пин вверх и вниз, формируя частоту ~1 кГц
    HAL_GPIO_WritePin(GPIOE, GPIO_PIN_9, GPIO_PIN_SET);
    for(volatile int i=0; i<400; i++);
    HAL_GPIO_WritePin(GPIOE, GPIO_PIN_9, GPIO_PIN_RESET);
    for(volatile int i=0; i<400; i++);
  } else {
    HAL_GPIO_WritePin(GPIOE, GPIO_PIN_9, GPIO_PIN_RESET); // Пауза в писке
  }
}
/* USER CODE END 4 */

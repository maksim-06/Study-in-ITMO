/* Includes ------------------------------------------------------------------*/
#include "main.h"
#include "cmsis_os.h"
#include "usart.h"
#include "gpio.h"
#include "i2c.h"
#include "kb.h"

/* Глобальный дескриптор мьютекса для защиты шины I2C */
osMutexId OLED_I2C_MutexHandle;

void SystemClock_Config(void);
void MX_FREERTOS_Init(void);
void Manual_OLED_HardwareReset(void);

/**
  * @brief  The application entry point.
  * @retval int
  */
int main(void)
{
  /* Reset of all peripherals, Initializes the Flash interface and the Systick. */
  HAL_Init();

  /* Configure the system clock */
  SystemClock_Config();

  /* Initialize all configured peripherals */
  MX_GPIO_Init();
  MX_USART1_UART_Init();
  MX_I2C1_Init();

  /* USER CODE BEGIN 2 */
  __HAL_RCC_GPIOC_CLK_ENABLE();
  __HAL_RCC_GPIOD_CLK_ENABLE();

  GPIO_InitTypeDef GPIO_InitStruct = {0};
  GPIO_InitStruct.Mode = GPIO_MODE_OUTPUT_PP;
  GPIO_InitStruct.Pull = GPIO_NOPULL;
  GPIO_InitStruct.Speed = GPIO_SPEED_FREQ_LOW;

  // Настройка стабильного питания периферии несущей платы
  GPIO_InitStruct.Pin = GPIO_PIN_12;
  HAL_GPIO_Init(GPIOC, &GPIO_InitStruct);
  HAL_GPIO_WritePin(GPIOC, GPIO_PIN_12, GPIO_PIN_SET);

  GPIO_InitStruct.Pin = GPIO_PIN_12;
  HAL_GPIO_Init(GPIOD, &GPIO_InitStruct);
  HAL_GPIO_WritePin(GPIOD, GPIO_PIN_12, GPIO_PIN_SET);

  // Настраиваем пины ИТМО-стенда PB8 и PB9
  __HAL_RCC_GPIOB_CLK_ENABLE();
  __HAL_RCC_I2C1_CLK_ENABLE();

  GPIO_InitStruct.Pin = GPIO_PIN_8 | GPIO_PIN_9;
  GPIO_InitStruct.Mode = GPIO_MODE_AF_OD;
  GPIO_InitStruct.Pull = GPIO_PULLUP;
  GPIO_InitStruct.Speed = GPIO_SPEED_FREQ_VERY_HIGH;
  GPIO_InitStruct.Alternate = GPIO_AF4_I2C1;
  HAL_GPIO_Init(GPIOB, &GPIO_InitStruct);

  HAL_Delay(200);

  // Создаем мьютекс защиты шины I2C
  osMutexDef(OLED_I2C_Mutex);
  OLED_I2C_MutexHandle = osMutexCreate(osMutex(OLED_I2C_Mutex));

  // Выполняем жесткий сброс дисплея
  Manual_OLED_HardwareReset();
  /* USER CODE END 2 */

  MX_FREERTOS_Init(); 
  osKernelStart();
 
  while (1) {}
}

/**
  * @brief System Clock Configuration
  */
void SystemClock_Config(void)
{
  RCC_OscInitTypeDef RCC_OscInitStruct = {0};
  RCC_ClkInitTypeDef RCC_ClkInitStruct = {0};

  __HAL_RCC_PWR_CLK_ENABLE();
  __HAL_PWR_VOLTAGESCALING_CONFIG(PWR_REGULATOR_VOLTAGE_SCALE1);

  RCC_OscInitStruct.OscillatorType = RCC_OSCILLATORTYPE_HSI;
  RCC_OscInitStruct.HSIState = RCC_HSI_ON;
  RCC_OscInitStruct.HSICalibrationValue = RCC_HSICALIBRATION_DEFAULT;
  RCC_OscInitStruct.PLL.PLLState = RCC_PLL_NONE;
  if (HAL_RCC_OscConfig(&RCC_OscInitStruct) != HAL_OK)
  {
    Error_Handler();
  }

  RCC_ClkInitStruct.ClockType = RCC_CLOCKTYPE_HCLK|RCC_CLOCKTYPE_SYSCLK|RCC_CLOCKTYPE_PCLK1|RCC_CLOCKTYPE_PCLK2;
  RCC_ClkInitStruct.SYSCLKSource = RCC_SYSCLKSOURCE_HSI;
  RCC_ClkInitStruct.AHBCLKDivider = RCC_SYSCLK_DIV1;
  RCC_ClkInitStruct.APB1CLKDivider = RCC_HCLK_DIV1;
  RCC_ClkInitStruct.APB2CLKDivider = RCC_HCLK_DIV1;

  if (HAL_RCC_ClockConfig(&RCC_ClkInitStruct, FLASH_LATENCY_0) != HAL_OK)
  {
    Error_Handler();
  }
}

/**
  * @brief  Системный обработчик ошибок
  */
void Error_Handler(void)
{
  while (1)
  {
      // Зависаем при критической ошибке периферии
  }
}

void HAL_TIM_PeriodElapsedCallback(TIM_HandleTypeDef *htim)
{
  if (htim->Instance == TIM1) {
    HAL_IncTick();
  }
}

#ifdef  USE_FULL_ASSERT
void assert_failed(uint8_t *file, uint32_t line) {}
#endif

/* USER CODE BEGIN 4 */
/**
  * @brief Вспомогательная функция жесткого сброса OLED (реализация)
  */
void Manual_OLED_HardwareReset(void) {
    uint8_t i2c_buf[2];
    uint16_t dev_addr = 0xE0 << 1; // 8-битный адрес расширителя сброса для функций HAL

    // 1. Конфигурируем P0 на выход (пишем 0xFE в CONFIG, регистр 0x03)
    i2c_buf[0] = 0x03;
    i2c_buf[1] = 0xFE;
    HAL_I2C_Master_Transmit(&hi2c1, dev_addr, i2c_buf, 2, 100);

    // 2. Устанавливаем P0 в "1" (Регистр OUTPUT_PORT 0x01)
    i2c_buf[0] = 0x01;
    i2c_buf[1] = 0x01;
    HAL_I2C_Master_Transmit(&hi2c1, dev_addr, i2c_buf, 2, 100);
    HAL_Delay(5);

    // 3. Формируем импульс низкого уровня "0" на P0 длительностью не менее 1 мкс
    i2c_buf[0] = 0x01;
    i2c_buf[1] = 0x00;
    HAL_I2C_Master_Transmit(&hi2c1, dev_addr, i2c_buf, 2, 100);
    HAL_Delay(2);

    // 4. Возвращаем в "1"
    i2c_buf[0] = 0x01;
    i2c_buf[1] = 0x01;
    HAL_I2C_Master_Transmit(&hi2c1, dev_addr, i2c_buf, 2, 100);
    HAL_Delay(5);
}
/* USER CODE END 4 */

💎 АЛГОРИТМ: СТЕКОВЫЙ КАЛЬКУЛЯТОР RPN (STACK\_BASED\_CALCULATOR) — ARCH: M68K



В этом модуле представлена низкоуровневая реализация стекового калькулятора, работающего с выражениями в Обратной польской нотации (Reverse Polish Notation), разработанная под CISC-архитектуру m68k.



📌 ТЕХНИЧЕСКОЕ ЗАДАНИЕ И ОГРАНИЧЕНИЯ

👉 Суть задачи: Считать из порта 0x80 строку символов (длиной до 64 байт / 0x40), содержащую числа и знаки операций (+, -, \*, /), разделенные пробелами. Вычислить итоговое значение математического выражения с использованием логики стека и вернуть результат в порт 0x84. Конец строки определяется символом '\\n'.

👉 Обработка ошибок:

&#x20;  - Деление на ноль или некорректное выражение (нехватка операндов, лишние числа в конце): алгоритм возвращает -1.

&#x20;  - Выход за границы 32-битного знакового машинного слова (переполнение при расчете или парсинге числа): алгоритм возвращает 0xCCCCCCCC.



🛠 ОСОБЕННОСТИ РЕАЛИЗАЦИИ НА M68K

🔥 Использование специфики CISC: В коде активно задействованы развитые режимы адресации процессора m68k (косвенная адресация с преддекрементом и постинкрементом регистров-указателей) для эффективного парсинга текстовых токенов и организации программного стека операндов.

🔥 Структура процедур: Программа спроектирована на основе вложенных подпрограмм. Выделены изолированные процедуры для обработки каждого математического оператора, валидации разрядов при конвертации ASCII-строки в целочисленный формат (int) и проверки финального состояния стека.



🤖 АВТОТЕСТИРОВАНИЕ

🔗 Ссылка на отчет автотестирования: https://wrench.edu.swampbuds.me/report/7dd850fc-1dd2-4271-9b51-406fd3d9a37b



Решение:

.data

input\_port:   .word 0x80

output\_port:  .word 0x84



&#x20;   .text

&#x20;   .org 0x88



\_start:

&#x20;   movea.l 0x700, A7

&#x20;   movea.l input\_port, A0

&#x20;   movea.l (A0), A2          ; A2 = 0x80

&#x20;   movea.l output\_port, A0

&#x20;   movea.l (A0), A4          ; A4 = 0x84

&#x20;   movea.l 0x4FF, A1

&#x20;   clr.l D7

&#x20;   jsr calc\_main\_\_fetch

&#x20;   jmp exit\_\_label



calc\_main\_\_fetch:

&#x20;   move.b (A2), D0



calc\_main\_\_route:

&#x20;   cmp.b 32, D0 ; ' '

&#x20;   beq calc\_main\_\_fetch

&#x20;   cmp.b 10, D0 ; '\\n'

&#x20;   beq calc\_main\_\_end

&#x20;   cmp.b 43, D0 ; '+'

&#x20;   beq calc\_main\_\_op

&#x20;   cmp.b 45, D0 ; '-'

&#x20;   beq calc\_main\_\_op

&#x20;   cmp.b 42, D0 ; '\*'

&#x20;   beq calc\_main\_\_op

&#x20;   cmp.b 47, D0 ; '/'

&#x20;   beq calc\_main\_\_op

&#x20;   jsr parse\_number\_\_proc

&#x20;   jmp calc\_main\_\_route



calc\_main\_\_op:

&#x20;   jsr operator\_\_proc

&#x20;   jmp calc\_main\_\_fetch



calc\_main\_\_end:

&#x20;   cmp.l 1, D7

&#x20;   bne calc\_main\_\_invalid

&#x20;   move.l (A1)+, D4

&#x20;   move.l D4, (A4)

&#x20;   jmp calc\_main\_\_exit



calc\_main\_\_invalid:

&#x20;   jsr write\_error\_\_proc



calc\_main\_\_exit:

&#x20;   rts



parse\_number\_\_proc:

&#x20;   sub.b 48, D0

&#x20;   clr.l D1

&#x20;   move.b D0, D1

&#x20;   move.l D1, D5

&#x20;   move.l 1, D4



parse\_number\_\_loop:

&#x20;   cmp.l 64, D4

&#x20;   bge parse\_number\_\_overflow



&#x20;   move.b (A2), D0

&#x20;   cmp.b 48, D0

&#x20;   blt parse\_number\_\_done

&#x20;   cmp.b 57, D0

&#x20;   bgt parse\_number\_\_skip\_rest



&#x20;   sub.b 48, D0

&#x20;   clr.l D1

&#x20;   move.b D0, D1

&#x20;   move.l D5, D2

&#x20;   move.l 10, D3

&#x20;   mul.l D3, D2

&#x20;   bvs parse\_number\_\_overflow

&#x20;   add.l D1, D2

&#x20;   bvs parse\_number\_\_overflow

&#x20;   move.l D2, D5

&#x20;   add.l 1, D4

&#x20;   jmp parse\_number\_\_loop



parse\_number\_\_done:

&#x20;   move.l D5, -(A1)

&#x20;   add.l 1, D7

&#x20;   rts



parse\_number\_\_skip\_rest:

&#x20;   add.l 1, D4



parse\_number\_\_skip\_loop:

&#x20;   cmp.l 64, D4

&#x20;   bge parse\_number\_\_overflow

&#x20;   move.b (A2), D0

&#x20;   cmp.b 48, D0

&#x20;   blt parse\_number\_\_skip\_done

&#x20;   add.l 1, D4

&#x20;   jmp parse\_number\_\_skip\_loop



parse\_number\_\_skip\_done:

&#x20;   move.l 0xFFFFFFFF, (A4)

&#x20;   movea.l 0x700, A7

&#x20;   jmp exit\_\_label



parse\_number\_\_overflow:

&#x20;   move.l 0xCCCCCCCC, (A4)

&#x20;   movea.l 0x700, A7

&#x20;   jmp exit\_\_label



operator\_\_proc:

&#x20;   cmp.l 2, D7

&#x20;   blt operator\_\_error



&#x20;   move.b D0, D6

&#x20;   move.l (A1)+, D1

&#x20;   move.l (A1)+, D0

&#x20;   sub.l 2, D7



&#x20;   cmp.b 43, D6

&#x20;   beq operator\_\_add

&#x20;   cmp.b 45, D6

&#x20;   beq operator\_\_sub

&#x20;   cmp.b 42, D6

&#x20;   beq operator\_\_mul

&#x20;   cmp.b 47, D6

&#x20;   beq operator\_\_div

&#x20;   jmp operator\_\_error



operator\_\_add:

&#x20;   add.l D1, D0

&#x20;   jmp operator\_\_finish



operator\_\_sub:

&#x20;   sub.l D1, D0

&#x20;   jmp operator\_\_finish



operator\_\_mul:

&#x20;   mul.l D1, D0

&#x20;   jmp operator\_\_finish



operator\_\_div:

&#x20;   cmp.l 0, D1

&#x20;   beq operator\_\_error

&#x20;   div.l D1, D0



operator\_\_finish:

&#x20;   bvs operator\_\_overflow

&#x20;   move.l D0, -(A1)

&#x20;   add.l 1, D7

&#x20;   rts



operator\_\_error:

&#x20;   jsr drain\_to\_newline\_\_loop

&#x20;   jsr write\_error\_\_proc

&#x20;   movea.l 0x700, A7

&#x20;   jmp exit\_\_label



operator\_\_overflow:

&#x20;   jsr drain\_to\_newline\_\_loop

&#x20;   jsr write\_overflow\_\_proc

&#x20;   movea.l 0x700, A7

&#x20;   jmp exit\_\_label





drain\_to\_newline\_\_loop:

&#x20;   move.b (A2), D0

&#x20;   cmp.b 10, D0

&#x20;   beq drain\_to\_newline\_\_done

&#x20;   cmp.b 0, D0

&#x20;   beq drain\_to\_newline\_\_done

&#x20;   jmp drain\_to\_newline\_\_loop



drain\_to\_newline\_\_done:

&#x20;   rts



write\_error\_\_proc:

&#x20;   move.l 0xFFFFFFFF, (A4)

&#x20;   rts



write\_overflow\_\_proc:

&#x20;   move.l 0xCCCCCCCC, (A4)

&#x20;   rts



exit\_\_label:

&#x20;   halt


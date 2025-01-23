import time
import main
import dop1
import dop2

start_time = time.perf_counter()
for i in range(1000):
    main.task()
end_time = time.perf_counter()
execution_time = end_time - start_time
print(f"Время выполнения main: {round(execution_time, 10)} секунд")



start_time = time.perf_counter()
for i in range(1000):
    dop1.task1()
end_time = time.perf_counter()
execution_time = end_time - start_time
print(f"Время выполнения dop1: {round(execution_time, 10)} секунд")


start_time = time.perf_counter()
for i in range(1000):
    dop2.task2()
end_time = time.perf_counter()
execution_time = end_time - start_time
print(f"Время выполнения dop2: {round(execution_time, 10)} секунд")


# start_time = time.perf_counter()
# for i in range(1000):
#     additional_task_3.add_3()
# end_time = time.perf_counter()
# execution_time = end_time - start_time
# print(f"Время выполнения additional_task_3: {round(execution_time, 10)} секунд")
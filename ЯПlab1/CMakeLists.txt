cmake_minimum_required(VERSION 4.0)
project(lab1 C)

set(CMAKE_C_STANDARD 11)

add_executable(lab1 main.c
        bst.c
        bst.h
        contact.h
        contact.c
        ui.h
        ui.c)

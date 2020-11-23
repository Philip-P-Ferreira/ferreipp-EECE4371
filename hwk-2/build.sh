#!/bin/bash
# A simple script to build this project
# Generated files are placed into a bin folder

clang-format src/*.java -style="Google" -i
mkdir -p bin
javac src/*.java -d bin
#!/bin/bash

rm -rf bin
find src -name "*.java" -print0 | xargs -0 javac -d bin
java -cp bin game.Main

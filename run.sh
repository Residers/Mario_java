#!/bin/bash
cd "$(dirname "$0")"

# Создаём папку для скомпилированных классов
mkdir -p out
rm -rf out/*

# Компилируем все Java-файлы
javac -d out -sourcepath src/main/java \
  src/main/java/ru/resider/mario/*.java

# Запускаем
java -cp "out:src/main/resources" ru.resider.mario.Main

@echo off
echo %1

java -jar scripts\ProcessVisualiser.jar %1 > graph1.txt
call graph-easy graph1.txt --as=svg --output=%1.svg
del graph1.txt

%1.svg
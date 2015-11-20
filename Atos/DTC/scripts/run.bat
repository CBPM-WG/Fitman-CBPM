@echo off
echo %1

java -jar scripts\InputProcessVisualiser-0.0.1-SNAPSHOT-jar-with-dependencies.jar %1 > graph-tmp.txt
call dot graph-tmp.txt -o%1.svg -Tsvg
::del graph-tmp.txt

%1.svg
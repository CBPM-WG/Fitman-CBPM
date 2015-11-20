#!/bin/bash
java -jar ../scripts/InputProcessVisualiser-0.0.1-SNAPSHOT-jar-with-dependencies.jar "$1" > graph-tmp.txt
#graph-easy graph1.txt --as=svg --output="$1.svg"
dot graph-tmp.txt -o$1.svg -Tsvg
rm graph-tmp.txt
eog "$1.svg"

#!/bin/bash
java -jar scripts/ProcessVisualiser.jar "$1" > graph1.txt
graph-easy graph1.txt --as=svg --output="$1.svg"
rm graph1.txt
firefox "$1.svg"

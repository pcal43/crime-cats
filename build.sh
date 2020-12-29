#!/bin/sh
rm -rf build/
mkdir -p build/classes
cd src
find . -name *.java -exec javac -d ../build/classes {} \;
cd ..
jar --create --file build/crimecats.jar --main-class net.pcal.crimecats.CrimeCats -C ./build/classes .


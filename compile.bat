@echo off
javac src\*.java
del classes\*
move src\*.class classes\
pause
javac src/*
rm -f classes/* || echo "no old classes found to delete"
mv src/*.class classes/

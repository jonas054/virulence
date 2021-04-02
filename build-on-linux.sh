# /bin/sh
set -e -x
JAVA_HOME=`ls -d /usr/lib/jvm/java-1.8* | tail -1`
JARS=lib/lwjgl.jar:lib/slick.jar

mkdir -p Virulence/classes
${JAVA_HOME}/bin/javac -cp ${JARS} -d Virulence/classes src/*.java
cp -r lib run-on-linux.sh Virulence
zip Virulence.zip `find Virulence -type f`

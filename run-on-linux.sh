JAVA_HOME=`ls -d /usr/lib/jvm/java-1.8* | tail -1`
JARS=lib/lwjgl.jar:lib/slick.jar

${JAVA_HOME}/bin/javac -cp ${JARS} src/*.java
${JAVA_HOME}/bin/java -cp src:${JARS} -Djava.library.path=lib Main

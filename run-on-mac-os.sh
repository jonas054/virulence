JAVA_HOME=`ls -d /Library/Java/JavaVirtualMachines/jdk1.8.*/Contents/Home | tail -1`
JARS=lib/lwjgl.jar:lib/slick.jar

${JAVA_HOME}/bin/javac -cp ${JARS} src/*.java
${JAVA_HOME}/bin/java -cp src:${JARS} -Djava.library.path=lib Main

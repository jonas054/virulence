JAVA_HOME=`ls -d /usr/lib/jvm/java-1.8* | tail -1`
JARS=lib/lwjgl.jar:lib/slick.jar

${JAVA_HOME}/bin/java -cp Virulence/classes:${JARS} -Djava.library.path=Virulence/lib Main

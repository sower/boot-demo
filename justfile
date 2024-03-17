mvn versions:set '-DnewVersion=1.0.0-SNAPSHOT'
mvn clean package -Dmaven.test.skip=true
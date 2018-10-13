# stuha-server

Server side part of Stuha.

To build for prod, use: 
```
mvn clean install -Pprod
```

Run resulting JAR as
```
java -Dserver.port=$PORT_NUMBER -jar $PATH_TO_JAR --spring.config.location=file:$CONFIG_DIR_PATH
```
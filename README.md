# Cron Parser

Cron Parser is a java program for parsing cron expression .

## Installation

Requirements:
- Java 21
- Maven

Run maven command and run it via fat-jar (see Usage)
```bash
mvn clean install
```

## Usage
Run `cron-parser-jar-with-dependencies.jar`. 
Pass arguments via `"` as single line.
  
Example:
```java
java -jar cron-parser-jar-with-dependencies.jar "*/15 0 1,15 * 1-5 /usr/bin/find"
```
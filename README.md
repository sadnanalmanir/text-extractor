# text-extractor: Extract text from any document
This project uses [Apache Tika](https://tika.apache.org) library to detect the format of a document, parses, and 
extract text. The [AutoDetectParser](https://tika.apache.org/2.7.0/api/org/apache/tika/parser/AutoDetectParser.html) 
class is used.

## JSAP
- Java Simple Argument Parser (JSAP) is used to parse the commands
- Accepts three arguments
- `--extract -i InputDirectory -o OutputDirectory`
- `--extract` refers to boolean value to initiate extraction of text
- `-i InputDirectory` refers to input directory containing the input files
- `-i OutputDirectory` refers to output directory for saving extracted text files

## How to run
### Using the `Main` method
- in Windows, set arguments as `--extract -i H:\html-corpus -o H:\extracted-text`
- in Ubuntu, set arguments as `--extract -i /home/user/html-corpus -o /home/user/extracted-text`
### Using the exec-maven-plugin

> Note-1: Works if the IDEs use older maven versions, successfully tested with apache maven 3.8.1

> Note-2: Check **mvn -version**. Latest versions don't work with the plugin. `mvn exec:java` command shown below works for 3.8.1 but does not work for 3.8.6 or later.

> The following exception occurs at Runtime: 

```
java.lang.ExceptionInInitializerError
    at org.apache.pdfbox.filter.FilterFactory.<init> (FilterFactory.java:42)
    ...
    at java.lang.Thread.run (Thread.java:833)
```
```
Caused by: java.lang.RuntimeException: XPathFactory#newInstance() failed to create an XPathFactory for the default object model: http://java.sun.com/jaxp/xpath/dom with the XPathFactoryConfigurationException: javax.xml.xpath.XPathFactoryConfigurationException: No XPathFctory implementation found for the object model: http://java.sun.com/jaxp/xpath/dom
    at javax.xml.xpath.XPathFactory.newInstance (Unknown Source)
```




1. Set the argument values directly inside the plugin inside `<configuration>`

```xml

<plugin>
    <groupId>org.codehaus.mojo</groupId>
    <artifactId>exec-maven-plugin</artifactId>
    <version>3.1.0</version>
    <configuration>
        <mainClass>uk.ac.rothamsted.ide.text.extractor.Main</mainClass>
        <arguments>
            <argument>--extract</argument>
            <argument>-i H:\html-corpus</argument>
            <argument>-o H:\text-from-html-corpus</argument>
        </arguments>
    </configuration>
</plugin>
```
or 

2. Use `mvn exec:java` from terminal 

For Windows
```shell
mvn exec:java -D'exec.mainClass'='uk.ac.rothamsted.ide.text.extractor.Main' -D'exec.args'="--extract -i H:\html-corpus -o H:\extracted-text"
```

For Linux
```shell
mvn exec:java -Dexec.mainClass=uk.ac.rothamsted.ide.text.extractor.Main -Dexec.args="--extract -i /home/user/html-corpus -o /home/user/extracted-text"
```
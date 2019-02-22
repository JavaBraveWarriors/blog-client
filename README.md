# blog-client
This application is a rest-api service client for creating and managing a blog.

## Necessary tools
* [Maven 3][2]
* [Java 8 or latest version][4]

## Getting started
Clone this repo to your local machine using:
```
git clone https://github.com/JavaBraveWarriors/blog-client.git
```

To run the application on the embedded Jetty server, execute the following commands:
```
mvn clean install jetty:run 
```
>Note: please configure address of the rest-api server in [application.properties][1] settings.

[1]: src/main/resources/application.properties
[2]: https://maven.apache.org/install.html
[4]: https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html

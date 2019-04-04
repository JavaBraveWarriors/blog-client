# blog-client
This application is a [rest-api][3] service client for creating and managing a blog.

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

## Production deployment
* In the *settings.xml* file ($M2_HOME/conf/settings.xml) write the settings for access to the server. Example:
```
 <server>
    <id>apache-tomcat</id>
    <username>tomcat-manager</username>
    <password>password</password>
  </server>
```
> *Node*: server identifier in the settings.xml file must match the identifier in pom.xml in the root of the plugin project "tomcat7-maven-plugin".
* When the settings are completed, run the following commands:
```
mvn clean install tomcat7:deploy -Dserver.deploy.port=${YOUR_CUSTOM_PORT}

```
* If you want to redeploy the project:
```
mvn clean install tomcat7:redeploy -Dserver.deploy.port=${YOUR_CUSTOM_PORT}
```
> *Node*: ${YOUR_CUSTOM_PORT} is the port on which tomcat is running.

### Links
* How setup deploy a real environment [Docker Containers Setup][5]

[1]: src/main/resources/application.properties
[2]: https://maven.apache.org/install.html
[3]: https://github.com/JavaBraveWarriors/BlogRest
[4]: https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
[5]: https://github.com/JavaBraveWarriors/BlogRest/blob/refactoringApi/docs/DockerContainersSetup.md
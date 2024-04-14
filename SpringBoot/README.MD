
# Camel Route Using Red Hat build of Apache Camel 3 for Spring Boot 2

The following components are used in this example:

| Component | Version |
|---|---|
| Java Development Kit | JDK 11 |
| Apache Camel | 3.20.x |
| Spring Boot | 2.7.x |
| Maven | 3.9.6 |

## Setup

### Install Components

Refer to the Red Hat build of Apache Camel 3.20 for Spring Boot 2 Supported Platforms documentation to select JVM. Once the desired JDK has been selected and installed, install [Maven](https://maven.apache.org/download.cgi?.). Red Hat provides [Visual Studio Code](https://code.visualstudio.com/) (VSCode) extensions that assist with efficiently authoring and building Camel projects:

* [Language Support for Apache Camel by Red Hat](https://camel-tooling.github.io/camel-lsp-client-vscode/)
* [Debug Adapter for Apache Camel by Red Hat](https://camel-tooling.github.io/camel-dap-client-vscode/)

### Adding repositories to Maven Settings
Adding the Red Hat repositories is required to ensure dependencies and plugins are downloaded from the Red Hat maven repository first and then fall back to Maven central.

[Repositories](https://maven.apache.org/guides/introduction/introduction-to-repositories.html) and PluginRepositories are specified in [Profiles](https://maven.apache.org/guides/introduction/introduction-to-profiles.html). These profiles can be activated based on various triggers - in this case the profile will be activated by default.

Per User profiles are specified in the user's maven settings file. This file is usually located in the user's home directory under the ***.m2*** directory with the name ***settings.xml***.

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 https://maven.apache.org/xsd/settings-1.0.0.xsd">
  <!-- Profiles Section -->
  <profiles>
    <profile>
      <!-- Activation Section for Profile specifying activeByDefault = true -->
      <activation>
        <activeByDefault>true</activeByDefault>
      </activation>
      <!-- Repository List in Descending Order of Priority -->
      <repositories>
        <!-- Red Hat General Availability Repository -->
        <repository>
          <id>redhat.ga</id>
          <name>Red Hat GA</name>
          <url>https://maven.repository.redhat.com/ga/</url>
          <releases>
            <enabled>true</enabled>
          </releases>
        </repository>
        <!-- Maven Central Repository -->
        <repository>
          <id>central</id>
          <name>Maven Central</name>
          <url>https://repo1.maven.org/maven2/</url>
          <releases>
            <enabled>true</enabled>
          </releases>
          <snapshots>
            <enabled>true</enabled>
            <updatePolicy>always</updatePolicy>
          </snapshots>
        </repository>
        <!-- Red Hat Early Access Repository -->
        <repository>
          <id>redhat.ea</id>
          <name>Red Hat EA</name>
          <url>https://maven.repository.redhat.com/earlyaccess/all/</url>
          <releases>
            <enabled>true</enabled>
          </releases>
        </repository>
      </repositories>
      <!-- PluginRepository List in Descending Order of Priority -->
      <pluginRepositories>
        <!-- Red Hat General Availability Repository -->
        <pluginRepository>
          <id>redhat.ga</id>
          <name>Red Hat GA</name>
          <url>https://maven.repository.redhat.com/ga/</url>
          <releases>
            <enabled>true</enabled>
          </releases>
        </pluginRepository>
        <!-- Maven Central Repository -->
        <pluginRepository>
          <id>central</id>
          <name>Maven Central</name>
          <url>https://repo1.maven.org/maven2/</url>
          <releases>
            <enabled>true</enabled>
          </releases>
          <snapshots>
            <enabled>true</enabled>
            <updatePolicy>always</updatePolicy>
          </snapshots>
        </pluginRepository>
        <!-- Red Hat Early Access Repository -->
        <pluginRepository>
          <id>redhat.ea</id>
          <name>Red Hat EA</name>
          <url>https://maven.repository.redhat.com/earlyaccess/all/</url>
          <releases>
            <enabled>true</enabled>
          </releases>
        </pluginRepository>
      </pluginRepositories>
    </profile>
  </profiles>
</settings>
```

**Ensure these settings have been specified before proceeding to use the maven archetype to generate a Camel for Spring Boot application otherwise the archetype will not be found.**

### Generating a Camel for Spring Boot application using Maven

```bash
mvn archetype:generate -DarchetypeGroupId=org.apache.camel.archetypes -DarchetypeArtifactId=camel-archetype-spring-boot -DarchetypeVersion=3.20.1.redhat-00064 -DgroupId=com.example -DartifactId=camel-timer-http-base64-kafka -Dversion=1.0-SNAPSHOT -DinteractiveMode=false
```
* Specify a relevant [GAV](https://maven.apache.org/repositories/artifacts.html) by entering the ***groupId***, ***artifactId*** and ***version*** for the application

This will create a new Camel for Spring Boot application in a sub-directory named after the ***artifactId*** specified. It includes some boilerplate code along with the maven pom file and directory structure.

## Camel Application

This application will configure a Camel Route that will:

1. Use a [Timer](https://access.redhat.com/documentation/en-us/red_hat_build_of_apache_camel_for_spring_boot/3.20/html/camel_spring_boot_reference/csb-camel-timer-component-starter) component configured as a consumer that will trigger every 10 seconds
1. Use an [HTTP](https://access.redhat.com/documentation/en-us/red_hat_build_of_apache_camel_for_spring_boot/3.20/html/camel_spring_boot_reference/csb-camel-http-component-starter) component to make a call to an HTTPS endpoint
1. Use a [Data Format](https://access.redhat.com/documentation/en-us/red_hat_build_of_apache_camel_for_spring_boot/3.20/html/camel_spring_boot_reference/csb-camel-dataformat-component-starter) component to marshal the response payload to Base64
1. Use a [Kafka](https://access.redhat.com/documentation/en-us/red_hat_build_of_apache_camel_for_spring_boot/3.20/html/camel_spring_boot_reference/csb-camel-kafka-component-starter) component configured as a producer to send the Base64 encoded payload to a Kafka topic

## References

* [Maven Settings Reference](https://maven.apache.org/ref/3.9.6/maven-settings/settings.html)
* [Maven Settings - Repositories](https://maven.apache.org/repositories/artifacts.html)
* [Red Hat build of Apache Camel for Spring Boot - Camel Spring Boot Reference](https://access.redhat.com/documentation/en-us/red_hat_build_of_apache_camel_for_spring_boot/3.20/html/camel_spring_boot_reference/index)
* [Spring Boot - Developer Tools](https://docs.spring.io/spring-boot/docs/2.7.12/reference/html/using.html#using.devtools)
* [Install Confluent Platform Using Docker](https://docs.confluent.io/platform/current/installation/docker/installation.html)
* [Packaging Red Hat build of OpenJDK 11 applications in containers](https://access.redhat.com/documentation/en-us/red_hat_build_of_openjdk/11/html/packaging_red_hat_build_of_openjdk_11_applications_in_containers/index)
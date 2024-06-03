## RestCountries 
### Overview

The RestCountries Application is a Spring Boot application designed to manage and provide information about countries. 
The application exposes REST endpoints to fetch country details based on specific criteria.


```Features

   * Retrieve countries sorted by population density.
   * Get the Asian country with the most borders in a different region.
```


### Prerequisites

    Java 17
    Maven or Gradle
    An IDE (e.g., IntelliJ IDEA, Eclipse) is recommended for development.

### Project Structure

```plaintext

assessment/
├── .git/
├── .gradle/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── restcontries/
│   │   │           └── app/
│   │   │               ├── configuration/
│   │   │               │   └── AppProperties.java
│   │   │               ├── controller/
│   │   │               │   └── CountryController.java
│   │   │               ├── model/
│   │   │               │   └── Country.java
│   │   │               ├── restclient/
│   │   │               │   └── RestTemplateUtil.java
│   │   │               ├── service/
│   │   │               │   └── CountryService.java
│   │   │               └── AssessmentApplication.java
│   │   ├── resources/
│   │   │   └── application.properties
│   ├── test/
│       ├── java/
│       │   └── com/
│       │       └── restcontries/
│       │           └── app/
│       │               ├── AssessmentApplicationTests.java
│       │               └── service/
├── build.gradle
└── pom.xml

```

### Getting Started


### Build and Run the Application

#### Using Gradle

```bash
./gradlew clean build

./gradlew bootRun
```

### Access the Application

Once the application is running, you can access the following endpoints:

    Retrieve countries by population density:

```vbnet

GET /api/countries-by-density

```

Get the Asian country with the most borders:

```vbnet
    GET /api/asian-country-with-most-borders
```



### Configuration

The application can be configured using the application.properties file located in the src/main/resources directory. Add any necessary configuration properties there.
Running Tests

To run the tests, use the following commands based on your build tool.

Using Gradle

```bash
./gradlew test
```



For any questions or feedback, please contact [akp.ani@yahoo.com].# restcountries-assessment
# restcountries-assessment
# restcountries-assessment

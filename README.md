# TestRail API Java Client
--------------------------

A Java client library for [TestRail API](http://docs.gurock.com/testrail-api2/start).

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.codepine.api/testrail-api-java-client/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.codepine.api/testrail-api-java-client)

Actual version: 2.0.3 

## Quick Start
--------------

### Maven Dependency
```xml
<dependency>
    <groupId>com.codepine.api</groupId>
    <artifactId>testrail-api-java-client</artifactId>
    <version>${stable.version.shown.above}</version>
</dependency>
```

### Example Usage
```java
// create a TestRail instance
TestRail testRail = TestRail.builder("https://some.testrail.net/", "username", "password").applicationName("playground").build();

// create a new project
Project project = testRail.projects().add(new Project().setName("Playground Project")).execute();

// add a new test suite
Suite suite = testRail.suites().add(project.getId(), new Suite().setName("Functional Tests")).execute();

// add a new section
Section section = testRail.sections().add(project.getId(), new Section().setSuiteId(suite.getId()).setName("Boundary Cases")).execute();

// add a test case
List<CaseField> customCaseFields = testRail.caseFields().list().execute();
Case testCase = testRail.cases().add(section.getId(), new Case().setTitle("Be able to play in playground"), customCaseFields).execute();

// add a new test run
Run run = testRail.runs().add(project.getId(), new Run().setSuiteId(suite.getId()).setName("Weekly Regression")).execute();

// add test result
List<ResultField> customResultFields = testRail.resultFields().list().execute();
testRail.results().addForCase(run.getId(), testCase.getId(), new Result().setStatusId(1), customResultFields).execute();

// close the run
testRail.runs().close(run.getId()).execute();

// complete the project - supports partial updates
testRail.projects().update(project.setCompleted(true)).execute();
```

## Supported TestRail Version
-----------------------------
![TestRail v4.1](https://img.shields.io/badge/TestRail-v4.1-blue.svg)
[![TestRail v4.1](https://img.shields.io/badge/TestRail%20API-v2-orange.svg)](http://docs.gurock.com/testrail-api2/start)

[Old API (aka Mini API)](http://docs.gurock.com/testrail-api/start) is not supported. Please note that you may not be able to use some API features supported by this library depending on the TestRail version you use. Similarly, since this is not an official library, API updates in future versions of TestRail may not be supported immediately with the release of new version or may need an incompatible major version change.

## Notables
------------

### Thin Client Library
Except the initial configration (refer to [example](#example-usage)), this client library does not maintain any state from your TestRail service. You can maintain/cache state on your end if you like.

### Custom Case And Result Fields
TestRail supports adding custom case and result fields. The request interfaces in ```TestRail.Cases``` and ```TestRail.Results``` requires a list of these fields in order to allow this library to map them to the correct Java types. Here's an example where we want to to know the separated test steps of a particular test case:
```java
// fetch list of custom case field configured in TestRail
List<CaseField> customCaseFields = testRail.caseFields().list().execute();

// get test case
Case testCase = testRail.cases().get(1, customCaseFields).execute();

// assuming separated_steps is a custom TestRail Steps type case field
List<Field.Step> customSteps = testCase.getCustomField("separated_steps");

// work with typed customSteps
......
```
Find the map of supported TestRail field types to Java types in the javadoc of ```Field.Type``` enum.
As mentioned [above](#thin-client-library), since this is a thin library, it does not store the list of fields. You can cache them on your end if you like.

## License
----------
This project is licensed under [MIT license](http://opensource.org/licenses/MIT).

# TemporalPeriods4j
![Java CI with Gradle](https://github.com/MikeSafonov/TemporalPeriods4J/workflows/Java%20CI%20with%20Gradle/badge.svg?branch=master)
[![Conventional Commits](https://img.shields.io/badge/Conventional%20Commits-1.0.0-yellow.svg)](https://conventionalcommits.org)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=MikeSafonov_TemporalPeriods4J&metric=alert_status)](https://sonarcloud.io/dashboard?id=MikeSafonov_TemporalPeriods4J)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=MikeSafonov_TemporalPeriods4J&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=MikeSafonov_TemporalPeriods4J)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=MikeSafonov_TemporalPeriods4J&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=MikeSafonov_TemporalPeriods4J)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=MikeSafonov_TemporalPeriods4J&metric=security_rating)](https://sonarcloud.io/dashboard?id=MikeSafonov_TemporalPeriods4J)

[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=MikeSafonov_TemporalPeriods4J&metric=bugs)](https://sonarcloud.io/dashboard?id=MikeSafonov_TemporalPeriods4J)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=MikeSafonov_TemporalPeriods4J&metric=code_smells)](https://sonarcloud.io/dashboard?id=MikeSafonov_TemporalPeriods4J)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=MikeSafonov_TemporalPeriods4J&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=MikeSafonov_TemporalPeriods4J)

[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=MikeSafonov_TemporalPeriods4J&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=MikeSafonov_TemporalPeriods4J)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=MikeSafonov_TemporalPeriods4J&metric=ncloc)](https://sonarcloud.io/dashboard?id=MikeSafonov_TemporalPeriods4J)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=MikeSafonov_TemporalPeriods4J&metric=sqale_index)](https://sonarcloud.io/dashboard?id=MikeSafonov_TemporalPeriods4J)

`TemporalPeriods4j` is a library providing temporal periods to extend `java.time` API .

## Features

### `Date`, `DateTime` and `YearMonth` temporal periods

`TemporalPeriods4j` provides three temporal periods: `DatePeriod`, `DateTimePeriod` and `YearMonthPeriod`.

![class diagram](http://www.plantuml.com/plantuml/proxy?src=https://raw.github.com/MikeSafonov/TemporalPeriods4j/master/diagrams/class.txt)

#### DatePeriod

`DatePeriod` is a temporal period between two `java.time.LocalDate`.

You may use one of the following to create `DatePeriod`:

- constructor `new DatePeriod(LocalDate from, LocalDate to)`;
- static constructors `of` : 
    - `DatePeriod.of(LocalDate from, LocalDate to)`; 
    - `DatePeriod.of(int yearFrom, int monthFrom, int dayFrom, int yearTo, int monthTo, int dayTo)`.
- static constructors `from`:
    - `DatePeriod.from(LocalDateTime from, LocalDateTime to)`;
    - `DatePeriod.from(DateTimePeriod dateTimePeriod)`.


#### DateTimePeriod

`DateTimePeriod` is a temporal period between two `java.time.LocalDateTime`.

You may use one of the following to create `DateTimePeriod`:

- constructor `new DateTimePeriod(LocalDateTime from, LocalDateTime to)`;
- static constructor `of` `DateTimePeriod.of(LocalDateTime from, LocalDateTime to)`
- static constructors `from`:
    - `DateTimePeriod.from(DatePeriod period, LocalTime time)`;
    - `DateTimePeriod.from(DatePeriod period)`.

#### YearMonthPeriod

`YearMonthPeriod` is a temporal period between two `java.time.YearMonth`.

You may use one of the following to create `YearMonthPeriod`:

- constructor `new YearMonthPeriod(YearMonth from, YearMonth to)`;
- static constructors `of` : 
    - `YearMonthPeriod.of(YearMonth from, YearMonth to)`; 
    - `YearMonthPeriod.of(int yearFrom, int monthFrom, int yearTo, int monthTo)`.
- static constructors `from`:
    - `YearMonthPeriod.from(LocalDate from, LocalDate to)`;
    - `YearMonthPeriod.from(LocalDateTime from, LocalDateTime to)`;
    - `YearMonthPeriod.from(DatePeriod period)`;
    - `YearMonthPeriod.from(DateTimePeriod period)`.

### Combining two temporal periods

`combineWith` method creates **new period** by combining current and specified periods.

Example:

    DatePeriod one = DatePeriod.of(2020, 1, 1, 2020, 1, 20);
    DatePeriod two = DatePeriod.of(2020, 1, 10, 2020, 1, 25);
    DatePeriod combined = one.combineWith(two);

![combineWith](http://www.plantuml.com/plantuml/proxy?src=https://raw.github.com/MikeSafonov/TemporalPeriods4j/master/diagrams/combine.txt)

### Intersection between two temporal periods

`intersectionWith` method creates **new period** by finding intersection between 
current and specified periods.

Example:

    DatePeriod one = DatePeriod.of(2020, 1, 1, 2020, 1, 20);
    DatePeriod two = DatePeriod.of(2020, 1, 10, 2020, 1, 25);
    Optional<DatePeriod> intersection = one.intersectionWith(two);

![intersection](http://www.plantuml.com/plantuml/proxy?src=https://raw.github.com/MikeSafonov/TemporalPeriods4j/master/diagrams/intersection.txt)


### Splitting period by temporal point


`split` method creates **array of periods** by splitting current period by 
specified temporal point.

Example:

    DatePeriod period = DatePeriod.of(2020, 1, 1, 2020, 1, 31);
    DatePeriod[] spl = period.split(LocalDate.from(2020, 1, 10));

![split](http://www.plantuml.com/plantuml/proxy?src=https://raw.github.com/MikeSafonov/TemporalPeriods4j/master/diagrams/split.txt)


## Usage

Maven:
    
    <dependency>
      <groupId>com.github.mikesafonov</groupId>
      <artifactId>TemporalPeriods4j</artifactId>
      <version>0.0.1</version>
    </dependency>
    
Gradle:

    dependencies {
        implementation 'com.github.mikesafonov:TemporalPeriods4j:0.0.1'
    }

## Build

### Build from source

You can build application using following command:

    ./gradlew clean build -x signArchives
    
#### Requirements:

JDK >= 1.8

### Unit tests

You can run unit tests using following command:

    ./gradlew test
    
### Mutation tests

You can run mutation tests using following command:

    ./grdlew pitest

You will be able to find pitest report in `build/reports/pitest/` folder.

## Contributing

Feel free to contribute. 
New feature proposals and bug fixes should be submitted as GitHub pull requests. 
Fork the repository on GitHub, prepare your change on your forked copy, and submit a pull request.

**IMPORTANT!**
>Before contributing please read about [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0-beta.2/) / [Conventional Commits RU](https://www.conventionalcommits.org/ru/v1.0.0-beta.2/)

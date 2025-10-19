# jalali-calendar

This project provides a JalaliCalendar class that extends the Java Calendar class.
You can convert jalali to gregorian calendar and vice-versa with the JalaliUtil class too.

## Usage

For start, you should add the following dependency to your pom. 
also you can find the latest-version in the Maven Central repository.

```
<dependency>
  <groupId>com.tosan.tools</groupId>
  <artifactId>jalali-calendar</artifactId>
  <version>latest-version</version>
</dependency>
```

### Prerequisites

This Library requires java version 8 or above.

### Leap Year Algorithm
The library uses the Khayyam table algorithm with hardcoded exceptions.
The algorithm calculates a 128-year cycle offset and checks against a predefined set of leap year positions.

Exception years (1403, 1436, 1469, 1404, 1437, 1470) are handled via HashSet lookups.

## Contributing

Any contribution is greatly appreciated.

If you have a suggestion that would make this project better, please fork the repo and create a pull request.
You can also simply open an issue with the tag "enhancement".

## License

The source files in this repository are available under the [Apache License Version 2.0](./LICENSE.txt).

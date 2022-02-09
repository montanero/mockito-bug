
This project shows a problem with mockito running under java 17 with default settings: The mocking is not done correctly.

What's worse: Mockito produces incorrect mocks **silently**.

# Observation

* When using a Java 11 JVM, the code will run fine
* When using a Java 17 JVM with default jvm settings, the code will crash with a 
`NullPointerException` 
* If the tests are run with Java 17 with option `--add-opens=java.base/java.io=ALL-UNNAMED` java opts,
everything works again.

The original object has member `OutputStream#enableOverride` set to `true`.
The spy'd object has that member set to `false`, which will lead to a different course of execution within `OutputStream`, leading to the NPE.


# Expectations

mockito should either 
* produce correct mocks

OR
* fail with Exception if correct mocks cannot be produced with the current jvm settings  

# Reproduce

Find code at https://github.com/montanero/mockito-bug.git

## Run with java 11 (works)
```
$ java -version
openjdk version "11.0.13" 2021-10-19
...

$ mvn clean install
...
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
..
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```
## Run with java 17 (fails w/o warning from mockito)

```
$ java -version
openjdk version "17.0.1" 2021-10-19
...

$ mvn clean install
Running mockitobug.TestMockOutputStream
Tests run: 1, Failures: 0, Errors: 1, Skipped: 0, Time elapsed: 0.449 sec <<< FAILURE!
runSpy(mockitobug.TestMockOutputStream)  Time elapsed: 0.41 sec  <<< ERROR!
java.lang.NullPointerException: Cannot invoke "java.io.ObjectOutputStream$BlockDataOutputStream.setBlockDataMode(boolean)" because "this.bou
t" is null
at java.base/java.io.ObjectOutputStream.writeObject0(ObjectOutputStream.java:1121)
at java.base/java.io.ObjectOutputStream.writeObject(ObjectOutputStream.java:354)
at mockitobug.TestMockOutputStream.runSpy(TestMockOutputStream.java:16)
...

Results :

Tests in error:
runSpy(mockitobug.TestMockOutputStream): Cannot invoke "java.io.ObjectOutputStream$BlockDataOutputStream.setBlockDataMode(boolean)" becaus
e "this.bout" is null

Tests run: 1, Failures: 0, Errors: 1, Skipped: 0

[INFO] ------------------------------------------------------------------------
[INFO] BUILD FAILURE
[INFO] ------------------------------------------------------------------------
```
## Run with java 17 --add-opens (works)

```
$ java -version
openjdk version "17.0.1" 2021-10-19
...

$ mvn clean install -Popen
...
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
...
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

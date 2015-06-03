# The Sample Application of JAX-WS with Spring Framework

This project provide two sample application.

* Using Spring Framework + JAX-WS(Apache CXF)
* Using Spring Framework + JAX-WS(JDK HTTP Server)

## The main framework and library

This Sample applications use the TERASOLUNA Server Framework for Java (5.x).

* Spring Framework 4.1.4.RELEASE
* Bean Validation (Hibernate Validator 5.1.3.Final)
* etc ...

## The Sample Application using Apache CXF

### Additional library

* JAX-WS (Apache CXF 3.1.0)


### Application Structure overview

![alt text](./images/cxf-overview.png "Application Structure overview using Apace CXF")


> **Todo:**
> 
> Details are written later ...


### Run and test an application

Run an application.

```console
cd soap-spring-with-cxf
mvn clean tomcat7:run
```

Test an application.

```console
cd soap-spring-with-cxf
mvn clean test
```


## The Sample Application using JDK HTTP Server

### Additional library

* None

### Application Structure overview

![alt text](./images/jdk-overview.png "Application Structure overview using JDK HTTP Server")


> **Todo:**
> 
> Details are written later ...


### Run and test an application

Run an application.

```console
cd soap-spring-with-jdk
mvn clean tomcat7:run
```

Test an application.

```console
cd soap-spring-with-jdk
mvn clean test
```

> **Warning:**
> 
> Currently, a validationErrorAtMethodArgument test is failing.
> It's under investigation.

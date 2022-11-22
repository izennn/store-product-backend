# Store Backend

This project uses Quarkus, the Supersonic Subatomic Java Framework.
    
The architecture is written following the hexagonal/ports & adapters design pattern.

## Build & Run application

You can build the application & play with this application at `http://localhost:8080`.    
First navigate to root directory and then:

```shell script
./gradlew quarkusDev
```

## Testing the application

The application can be tested using:

```shell script
./gradlew quarkusIntTest
```

easiest way is to run this from the Gradle plugin

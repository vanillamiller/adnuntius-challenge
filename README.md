# Medication Server
## Getting Started
---
This is a gradle project and as such you will need to download from here https://gradle.org/ if you have not already

```console
user@machine:~$ gradle -v
```

Then you can build and run the project running with the following
```console
user@machine:~$ gradle wrapper --gradle-version <your gradle version>
user@machine:~$ gradle build
user@machine:~$ ./gradlew appRun
```

To interact with the endpoint:

This API accepts 2 formats:

- semicolon delimited String format:

    - ```json  
        { "medicationStrings": "186FASc73541_M_105818673cda541_S_0061186FASc73541_M_1058;18673cda541_S_0061;" }
        ```

- array of strings

    - ```json
        { "medicationStrings": [ "186FASc73541_M_1058" , "18673cda541_S_0061" ] }
        ```
```console
user@machine:~$ curl http://localhost:8080/medication-server/

> {"message" : "there are no medications currently stored"}

user@machine:~$ curl -d '{"medicationStrings":"186FASd73541_m_1058;186FASc73541_M_1058;18673cda541_S_0061;"}' -X POST http://localhost:8080/medication-server/

> {"medicationsSuccessfullyUploaded":[{"id":"186FASd73541","bottleSize":"M","dosageCount":1058},{"id":"186FASc73541","bottleSize":"M","dosageCount":1058},{"id":"18673cda541","bottleSize":"S","dosageCount":61}]}

user@machine:~$ curl localhost:8080/medication-server/

> {"numberMedicationPerSize":{"S":61,"M":2116},"totalMedicationsStored":3,"numberMedicationPerMedication":{"186FASc73541":1,"186FASd73541":1,"18673cda541":1},"totalDosagesPerMedication":{"186FASc73541":1058,"186FASd73541":1058,"18673cda541":61}}

user@machine:~$ curl -d '{ medicationStrings: [ "186FASc73541_M_1058" , "18673cda541_S_0061" ] }' -X POST http://localhost:8080/medication-server/

> {"medicationsSuccessfullyUploaded":[{"id":"186FASc73541","bottleSize":"M","dosageCount":1058},{"id":"18673cda541","bottleSize":"S","dosageCount":61}]}

user@machine:~$ curl localhost:8080/medication-server/

> {"numberMedicationPerSize":{"S":122,"M":3174},"totalMedicationsStored":5,"numberMedicationPerMedication":{"186FASc73541":2,"186FASd73541":1,"18673cda541":2},"totalDosagesPerMedication":{"186FASc73541":2116,"186FASd73541":1058,"18673cda541":122}}
```

## Testing 
several tests were written under src\test\java\com\adnuntius\challenge\MedicationServletTest.java using Mockito
The results for these tests can be found in build\reports\tests\test\index.html
They test:
- GET with empty repo
- POST with format1
- POST with format2
These tests are verified before build is complete

Alternatively they can be tested with:

```console
user@machine:~$ gradle test
```

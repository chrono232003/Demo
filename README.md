# Alpian

## Overview
This repository contains 3 coding exercises that all live under the **src.main** folder in the repository. There are unit tests for each of these exercises that live in the respective **src.test** folder.

### Requirements
The exercises are written in Java and the cloned project will need the following to run:

- Java JDK installed
- A Java IDE such as Intellij or Eclipse
- Maven to pull in dependencies

### Structure
**Exercise #1**
 - Name: Collatz Recursion
 - Class Path: com.recursion.Recursion
 - Unit Test Class Path: com.recursion.RecursionTest
 
**Exercise #2**
  - Name: Dot Product
  - Class Path: com.dotproduct.DotProduct
  - Unit Test Class Path: com.dotproduct.DotProductTest
  
**Exercise #3**
- Name: Customer Mapping Service
- Class Package: com.customermappingservice
- Main API Class Path: com.customermappingservice.Application
- Test Package: com.customermappingservice

### Testing
To run the unit tests:

1. Clone the project locally
2. run "mvn clean install" from the terminal on the root folder of the project.
3. In the IDE:
   - Mark the src/main folder as the sources folder.
   - Mark the  src/test folder as the test sources folder (So that JUnit will recognize the classes as test classes)
4. navigate to the respective test folders for the exercises (Shown below in Structure), and run the unit tests.  
 - For both Collatz Recursion and Dot Product, there is one test suite under the respective packages.
 - For  the Customer Mapping Service, there are 2 tests under the package:
    
    - AppUtilsTest which tests some of the utility methods such as validating a date etc.
    - AppTest which is an end to end api test. NOTE: When one of these tests is ran, the API server will be automatically started and stopped.
      These tests will need to be ran one at a time.
      
- Tests can also be ran through Postman or similar utility by starting the server.
It can be started by running com.customermappingservice.Application.main() in an IDE or cmd prompt. the server is start on port 4568 and the base url should be http://localhost:4568/.

- /getcustomer
    - params:
        - customerId (Int)
- /postcustomer
    - params:
        - customerId (Int)
        - createdAt (String - yyyy-mm-dd)
        


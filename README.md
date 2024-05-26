# Quick description
Project is a simple elevator system application which allow to do specific operations on elevators.

# Running the project

To be able to run this project two thing are needed:

- Maven - project was compiled ana packaged with Maven 3.9.5 - so I can tell this version will work 100%
- Java 21 installed on your system or included with your IDE tool

If you have downloaded this project go to directory when pom.xml is located and hit:
**mvn clean package spring-boot:run**

Note: Even if you open project in IDE always do package because project includes mapstruct for mapping and openapi codegen for REST API so resources will be generated in maven generate stage (before compiling)

## Configuration (application.yaml)

There are 3 options you should know that could be configured in the project:

- "building.min-floor" - minimum floor that elevator can reach in the building
- "building.max-floor" - maximum floor that elevator can reach in the building
- "elevators.number-of-elevators-limit" - number of elevators that can work in the system

## Ordering elevator alghoritm
Alghoritm when someone want to order elevator (/elevators/order POST) can be presented in steps:

1. If there is any free elevator (not moving or ordered) on the same floor that order is created, then first free elevator from db will be taken (if 2 persons will order elevator on the same floor only one will open)
2. If 1 point is not applicable then first free elevator is ordered an which is closest to the order floor
3. If 2 point is not applicable then order elevator that could stop on the order floor and which is going to direction specified by pressing the button
4. If 3 point is not applicable order any elevator that will stop on the order floor
5. If 4 point is not applicable order random elevator

Of course with this alghoritm some elevators will be more used than another (it is huge drawback of this)

## Moving alghoritm
Elevator moves to destination point. Even if during its way some additional stop points were add. This can be presented on the example:
Lets assume there is only 1 elevator for this example.
1. Person1 order free elevator on 2 floor. Elevator arrived at 2 floor.
2. Person1 wants to go to floor 8.
3. Person2 order elevator on floor 4 when it is currently on 3 floor.
4. Elevator stops on floor 4.
5. Person2 select floor 7 as destination.
6. Elevator stops at floor 7.
7. Person 3 enter on floor 7 and select floor 5.
8. Elevator stops at floor 8.
9. Person 4 enter the elevator and select floor 10.
10. Elevator is changing direction and stops at floor 5.
11.  Elevator is changing direction and stops at floor 10.

## Known limitations

- Application handle many elevators but don't take into account that elevators could be located in different places in the building. If we would have such case the repository layer should be extended in ex. buckets for elevators
- Application works on in-memory database which is not safe. If there is problem with system - then system should call elevator for its status (it is not implemented). On the other hand - there could be a problem with elevator. Then additional POST endpoint would be a good option (it would be that red button that you can press to call service when you stuck in the elevator)
- Of course REST API for elevator system is not a great solution - but it is example just how it could work. Using that was fast for me for development because i am familiar with it.
- All elevators are moving one step (one floor) but in real world example there should be some call/event from elevator that it changed its position
- Exception handling in this application is poor and should be extended. Also returning ResponseStatusException exceptions in repository layer is also not a good approach - I know about that. This is just an example with presentation of some knowledge of Spring Boot and Java.
- There are much more... I know that I had more in my mind when I wrote this app but what can I say :D I didn't note them.


## REST API

- /elevators POST - add elevator to the system
- /elevators/{elevatorId} DELETE - deletes elevator from the system
- /elevators/status GET - get statuses of all elevators
- /elevators/order POST - order elevator (it is like pressing one of two buttons: "up" or "down" and ordering elevator)
- /elevators/{elevatorId}/status PUT - updates status of the elevator (it is like pressing the button at the elevator if you want to go ex. 7th floor)
- /elevators/{elevatorId}/move POST - moves elevator one step (little bit like a simulation step)

## About the tests
Application is covered with unit tests and few end-to-end test for controllers (if so small tests can be named so).

## Try it yourself
You can start application and try it yourself - there is OpenApi endpoint created when you start appliaction:
http://localhost:|your-port|/swagger-ui/index.html - it can be useful. There is also file in repository called elevator-system.yml on which this ui is based.
You can also prepared calls based in: src/test/java/elevator/system/domain/controller/simulation. Just create your own tests in SimulationTest.java and use calls from ElevatorsApiRestTestInvoker.java in it :) 


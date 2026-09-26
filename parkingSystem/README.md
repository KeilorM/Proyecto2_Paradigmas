# Parking Coto — Private Parking Lot Management System

Project 2 · EIF400 Programming Paradigms

**Opening this in NetBeans:** this is a standard Maven project
(it has a pom.xml inside the parkingSystem folder). Use **File > Open Project**
and select the parkingSystem folder directly.
NetBeans detects the `pom.xml` automatically, so no "New Project" 
wizard is needed.

From the command line, `mvn compile`, `mvn exec:java` and
`mvn package` are also available once Maven can reach the internet
to download its plugins the first time.

## Requirements

* JDK 17 or higher installed.
* Use `javac -version` and `javadoc -version` to verify the installation.

## Architecture: Clean Architecture

The project is organized in layers, following the Clean Architecture
dependency rule: **dependencies only point inward**. Outer layers know
about inner layers; inner layers never know about outer ones.

parkingSystem/src/main/java/parking/

```
domain/                     <- Innermost layer. Zero outward dependencies.
  model/                    Entities and value objects:
                            Vehicle, Car, Motorcycle, CargoVehicle,
                            Rate, HourlyRate, RateWithDailyCap,
                            ParkingSpace, ParkingTicket, Payment,
                            enums, and TimeUtil.

  exception/                Business-rule exceptions:
                            BusinessException and its subclasses.

  repository/               Repository PORTS (interfaces only):
                            VehicleRepository, ParkingSpaceRepository,
                            ParkingTicketRepository, PaymentRepository.

application/                <- Depends only on domain.
  usecase/                  One class per system operation:
                            RegisterVehicleUseCase,
                            CheckInVehicleUseCase,
                            CheckOutVehicleUseCase,
                            RegisterPaymentUseCase,
                            ValidateSpaceAssignmentUseCase,
                            ListAvailableSpacesUseCase,
                            ListVehiclesInsideUseCase,
                            ListActiveTicketsUseCase,
                            GetOccupancyByTypeUseCase,
                            GetTotalRevenueUseCase,
                            RegisterParkingSpaceUseCase.

infrastructure/             <- Depends on domain.
  repository/               In-memory adapters that implement
                            the domain repository interfaces:
                            InMemoryVehicleRepository,
                            InMemoryParkingSpaceRepository,
                            InMemoryParkingTicketRepository,
                            InMemoryPaymentRepository.

app/                        <- Outermost layer: composition and delivery.
  ParkingSystem.java        Wires infrastructure adapters into
                            the application use cases.

  Main.java                 Starts the interactive console application.

  ConsoleMenu.java          Handles console input, menus, and output.

  Demo.java                 Runs a predefined full-flow demonstration.

  ParkingTests.java         Runs the 15 mandatory test cases.
```

Why this matters for the grading rubric: the domain layer
(`Vehicle`, `Rate`, `ParkingTicket`, etc.) can be tested, reused, or
implemented through another delivery mechanism without depending on
the infrastructure or presentation layers.

Only `ParkingSystem` is responsible for wiring the concrete
in-memory repositories into the application use cases.

## How to compile

From the `parkingSystem` folder, where the `src` folder and `pom.xml` are located:

`mvn compile`

A manual compilation option is also available:

`mkdir -p bin`

`javac -d bin $(find src -name "*.java")`

## How to run

### Interactive console application

`mvn exec:java`

Or, after manual compilation:

`java -cp bin parking.app.Main`

This starts the interactive console menu for registering vehicles,
registering parking spaces, checking vehicles in and out, registering
payments, and consulting parking information.

### Full-flow demonstration

`java -cp bin parking.app.Demo`

This runs a predefined demonstration of the main system flow:

`register → check in → check out → pay → revenue`

### Mandatory tests

`java -cp bin parking.app.ParkingTests`

This executes the 15 mandatory test cases from the assignment and
prints each result with `[OK]` or `[FAIL]`, followed by a summary table
containing the input, expected result, and actual result.

## How to generate the JavaDoc

Every public class and public method is documented with JavaDoc,
including `@param`, `@return`, and `@throws` where relevant.

Each package also contains a `package-info.java` file explaining
the role of that layer in the architecture.

`mkdir -p docs`

`javadoc -d docs -sourcepath src -subpackages parking`

Then open `docs/index.html` in a browser.

A pre-generated copy is already included in this delivery under
`docs/`.

## Key design decisions

### 1. No type-based rate calculation

The system does not use an `if` or `switch` based on the concrete
vehicle type to calculate rates.

Each `Vehicle` subclass (`Car`, `Motorcycle`, `CargoVehicle`)
implements the required behavior through `getRate()` and
`getCompatibleSpaceType()`.

The rest of the system works with the `Vehicle` abstraction and
calls these methods polymorphically.

### 2. Rate calculation with Strategy + Decorator

`Rate` is an interface that defines the rate calculation behavior.

`HourlyRate` implements the basic hourly charge, while
`RateWithDailyCap` decorates another `Rate` and applies the maximum
amount per 24-hour period once the required stay threshold is reached.

This directly addresses the daily-cap requirement. The rule is isolated
in a single class, allowing it to evolve without distributing
conditions throughout `Vehicle`, `ParkingTicket`, or the use cases.

### 3. Clean Architecture and Dependency Inversion

The domain defines what persistence it needs through repository
interfaces such as `VehicleRepository`, `ParkingSpaceRepository`,
`ParkingTicketRepository`, and `PaymentRepository`.

The infrastructure layer provides the concrete implementations.

The current implementation uses in-memory `ArrayList` collections,
but the storage mechanism could be replaced without modifying the
domain or application layers.

### 4. One use case per operation

Instead of one large class coordinating every operation, each business
operation is represented by its own use-case class with a focused
responsibility and an `execute(...)` method.

`ParkingSystem` acts as the composition root and wires the use cases
with their required repository implementations.

### 5. Custom business exceptions

Business-rule violations are represented by custom exceptions in
`parking.domain.exception`.

Examples include:

* `SpaceOccupiedException`
* `SpaceOutOfServiceException`
* `IncompatibleSpaceException`
* `VehicleWithActiveTicketException`
* `TicketNotActiveException`
* `TicketStillActiveException`

These exceptions derive from the common `BusinessException` root,
keeping business-rule failures explicit and organized.

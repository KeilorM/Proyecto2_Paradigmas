# Parking Coto — Private Parking Lot Management System

Project 2 · EIF400 Programming Paradigms

> **Opening this in NetBeans:** this is a standard Maven project
> (it has a `pom.xml` at the root). Just use **File > Open Project**
> and select this folder directly — NetBeans detects the `pom.xml`
> automatically, no "New Project" wizard needed. From the command line,
> `mvn compile`, `mvn exec:java` (runs `parking.app.Main` by default)
> and `mvn package` also work once Maven can reach the internet to
> download its plugins the first time.

## Requirements
- JDK 17 or higher installed (`javac -version`, `javadoc -version` to confirm).

## Architecture: Clean Architecture

The project is organized in layers, following the Clean Architecture
dependency rule: **dependencies only point inward**. Outer layers know
about inner layers; inner layers never know about outer ones.

```
src/parking/
  domain/                     <- Innermost layer. Zero outward dependencies.
    model/                    Entities and value objects (Vehicle, Car,
                               Motorcycle, CargoVehicle, Rate, HourlyRate,
                               RateWithDailyCap, ParkingSpace,
                               ParkingTicket, Payment, enums, TimeUtil)
    exception/                Business-rule exceptions (BusinessException
                               and its subclasses)
    repository/                Repository PORTS (interfaces only):
                               VehicleRepository, ParkingSpaceRepository,
                               ParkingTicketRepository, PaymentRepository

  application/                 <- Depends only on domain.
    usecase/                  One class per operation the system can
                               perform (RegisterVehicleUseCase,
                               CheckInVehicleUseCase, CheckOutVehicleUseCase,
                               RegisterPaymentUseCase,
                               ValidateSpaceAssignmentUseCase,
                               ListAvailableSpacesUseCase,
                               ListVehiclesInsideUseCase,
                               ListActiveTicketsUseCase,
                               GetOccupancyByTypeUseCase,
                               GetTotalRevenueUseCase,
                               RegisterParkingSpaceUseCase)

  infrastructure/              <- Depends on domain (implements its ports).
    repository/                In-memory ADAPTERS that implement the
                               domain repository interfaces
                               (InMemoryVehicleRepository,
                               InMemoryParkingSpaceRepository,
                               InMemoryParkingTicketRepository,
                               InMemoryPaymentRepository)

  app/                          <- Outermost layer: composition root + delivery.
    ParkingSystem.java          Wires infrastructure adapters into the use
                               cases (the only place that knows both
                               "infrastructure" and "application" exist)
    Main.java                  Console demo, built on top of ParkingSystem
    ParkingTests.java           15 mandatory test cases, built on top of
                               ParkingSystem
```

Why this matters for the grading rubric: the domain layer (`Vehicle`,
`Rate`, `ParkingTicket`, etc.) can be unit-tested, reused, or even
reimplemented in another delivery mechanism (a REST API, a GUI) without
changing a single line, because nothing in `domain` or `application`
imports anything from `infrastructure` or `app`. Only `ParkingSystem`
(the composition root) is allowed to know about the concrete in-memory
repositories.

## How to compile

From the project root (where this `src` folder lives):

```bash
mkdir -p bin
javac -d bin $(find src -name "*.java")
```

## How to run

Full-flow demo (register → check in → check out → pay → revenue):
```bash
java -cp bin parking.app.Main
```

Mandatory tests (15 cases from section 12 of the assignment):
```bash
java -cp bin parking.app.ParkingTests
```

This prints each test with `[OK]`/`[FAIL]` and, at the end, a table
ready to paste into the report (input / expected result / actual
result).

## How to generate the JavaDoc

Every public class and public method is documented with JavaDoc,
including `@param`, `@return` and `@throws` where relevant, plus a
`package-info.java` per package explaining that layer's role in the
architecture.

```bash
mkdir -p docs
javadoc -d docs -sourcepath src -subpackages parking
```

Then open `docs/index.html` in a browser. A pre-generated copy is
already included in this delivery under `docs/`.

## Key design decisions (for the report and the oral defense)

1. **No if/switch based on vehicle type.** Each `Vehicle` subclass
   (`Car`, `Motorcycle`, `CargoVehicle`) implements two abstract
   methods: `getRate()` and `getCompatibleSpaceType()`. Nothing in the
   system ever asks "what type of vehicle is this?"; it simply calls
   those methods polymorphically.

2. **Rate calculation with Strategy + Decorator.** `Rate` is an
   interface (strategy). `HourlyRate` implements the simple hourly
   charge. `RateWithDailyCap` **decorates** any `Rate` and applies the
   24-hour period cap once the stay reaches the threshold. This directly
   answers requirement 9 of the assignment: the daily cap rule lives in
   **a single, isolated class**, and it can evolve without touching
   `Vehicle`, `ParkingTicket`, or any use case.

3. **Clean Architecture / Dependency Inversion.** The domain defines
   *what* persistence it needs (`VehicleRepository`,
   `ParkingSpaceRepository`, etc., as interfaces) but never *how*; the
   infrastructure layer supplies the *how* (in-memory `ArrayList`-backed
   implementations here, but any other storage could be swapped in
   without touching `domain` or `application`).

4. **One use case per operation.** Instead of one large "God class"
   coordinating everything, each business operation (`CheckInVehicleUseCase`,
   `RegisterPaymentUseCase`, ...) is its own small, single-responsibility
   class with one public `execute(...)` method. `ParkingSystem` is the
   only class that wires them all together (composition root pattern).

5. **Custom business exceptions**, in `parking.domain.exception`, one
   per rule (`SpaceOccupiedException`, `SpaceOutOfServiceException`,
   `IncompatibleSpaceException`, `VehicleWithActiveTicketException`,
   `TicketNotActiveException`, `TicketStillActiveException`, etc.), all
   derived from a common root, `BusinessException`.

## Still pending to complete the deliverable
- UML diagram (already generated separately as a draw.io file; may need
  a light update to reflect the new package structure — ask if you want
  it regenerated).
- PDF report (6–10 pages) with the requested sections.
- Individual conclusions from each team member.

/**
 * Application layer: use cases (Clean Architecture's "application
 * business rules").
 * <p>
 * Each class here represents exactly one operation the system can
 * perform (check a vehicle in, register a payment, list available
 * spaces, and so on) and exposes it through a single {@code execute}
 * method. Use cases depend only on the domain layer - its entities and
 * its repository ports - never on a concrete storage mechanism or on
 * any presentation detail. This keeps them trivially testable and
 * reusable from any delivery mechanism (a console app, a REST
 * controller, a test suite) without modification.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
package parking.application.usecase;

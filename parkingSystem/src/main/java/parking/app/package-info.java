/**
 * Composition root and delivery mechanism: the outermost layer in
 * Clean Architecture terms.
 * <p>
 * {@link parking.app.ParkingSystem} wires the concrete
 * {@code infrastructure} adapters into the {@code application} use
 * cases. {@link parking.app.Main} is a minimal console "driver" that
 * exercises the wired system, and {@link parking.app.ParkingTests} is
 * the automated test harness for the assignment's 15 mandatory cases.
 * Nothing in the inner layers depends on this package.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
package parking.app;

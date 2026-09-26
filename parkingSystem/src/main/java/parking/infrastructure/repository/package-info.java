/**
 * Infrastructure layer: concrete adapters for the repository ports
 * declared in {@code parking.domain.repository}.
 * <p>
 * Everything in this package is replaceable. It currently offers a
 * simple in-memory implementation suited for a single run of the
 * console demo and the automated tests; a future JDBC-, file-, or
 * network-backed implementation could be dropped in here without the
 * domain or application layers noticing, since both only ever reference
 * the repository interfaces, never these concrete classes directly.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
package parking.infrastructure.repository;

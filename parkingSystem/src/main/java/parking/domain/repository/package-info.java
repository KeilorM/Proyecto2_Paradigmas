/**
 * Repository ports (interfaces) for every persistable domain entity.
 * <p>
 * These interfaces are owned by the domain layer, but are implemented
 * by the {@code parking.infrastructure.repository} package. This is the
 * Dependency Inversion Principle at work: the domain defines what
 * persistence it needs, and outer layers provide it, so the dependency
 * arrow always points inward, toward the domain, never outward from it.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
package parking.domain.repository;

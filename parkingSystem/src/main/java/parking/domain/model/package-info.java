/**
 * Domain entities and value objects: the innermost layer of the
 * application, following Clean Architecture's dependency rule.
 * <p>
 * Everything in this package is plain Java with no dependency on any
 * other layer, framework, or storage mechanism. It expresses only the
 * business concepts (vehicles, rates, spaces, tickets, payments) and the
 * rules that are intrinsic to them (e.g. how a rate is computed, how a
 * space validates compatibility). Persistence, orchestration across
 * multiple entities, and any notion of "use case" belong to the
 * {@code parking.application} and {@code parking.infrastructure}
 * packages instead.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
package parking.domain.model;

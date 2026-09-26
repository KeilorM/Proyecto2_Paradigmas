/**
 * Business-rule exceptions raised by the domain layer.
 * <p>
 * All of them extend {@link parking.domain.exception.BusinessException},
 * forming a shallow hierarchy that lets callers catch broadly
 * ({@code BusinessException}) or narrowly (a specific subclass) as
 * needed.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
package parking.domain.exception;

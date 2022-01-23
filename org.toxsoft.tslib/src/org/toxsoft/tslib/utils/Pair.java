package org.toxsoft.tslib.utils;

/**
 * The pair - two nullable objects, the left and the right.
 *
 * @author vs
 * @param left &lt;L&gt; - the left object, may be <code>null</code>
 * @param right &lt;R&gt; - the right object, may be <code>null</code>
 * @param <L> - left object type
 * @param <R> - right object type
 */
@SuppressWarnings ( "hiding" )
public record Pair<L, R> (L left, R right ) {

  // nop

}

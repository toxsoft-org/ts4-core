package org.toxsoft.core.tslib.bricks.lexan;

/**
 * Identifies the formula substring.
 * <p>
 * Sequence of substringms makes original formula string.
 *
 * @author hazard157
 * @param formula String - the initial formula string
 * @param subs String - the substring
 * @param start int - index of the first char of the <code>subs</code> in <code>formula</code>
 * @param end int - index after the last char of the <code>subs</code> in <code>formula</code>
 */
public record FormulaSubstring ( String formula, String subs, int start, int end ) {

  // nop

}

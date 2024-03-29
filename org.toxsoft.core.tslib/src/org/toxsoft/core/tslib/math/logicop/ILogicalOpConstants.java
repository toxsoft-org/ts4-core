package org.toxsoft.core.tslib.math.logicop;

/**
 * Logical operation constants.
 *
 * @author hazard157
 */
public interface ILogicalOpConstants {

  /**
   * Upper-case keyword of logical operation AND.
   */
  String KW_LOGICAL_AND = "AND"; //$NON-NLS-1$

  /**
   * Upper-case keyword of logical operation OR.
   */
  String KW_LOGICAL_OR = "OR"; //$NON-NLS-1$

  /**
   * Upper-case keyword of logical operation XOR.
   */
  String KW_LOGICAL_XOR = "XOR"; //$NON-NLS-1$

  /**
   * Upper-case keyword of logical operation NOT (inversion).
   */
  String KW_LOGICAL_NOT = "NOT"; //$NON-NLS-1$

  /**
   * Single ASCII <code>char</code> representation of logical operation AND.
   */
  char CH_LOGICAL_AND = '&';

  /**
   * Single ASCII <code>char</code> representation of logical operation XOR.
   */
  char CH_LOGICAL_OR = '|';

  /**
   * Single ASCII <code>char</code> representation of logical operation OR.
   */
  char CH_LOGICAL_XOR = '^';

  /**
   * Single ASCII <code>char</code> representation of logical operation NOT (inversion).
   */
  char CH_LOGICAL_NOT = '!';

  /**
   * String made of logical operation single chars.
   */
  String STR_LOGICAL_OP_CHARS = "&|^!"; //$NON-NLS-1$

}

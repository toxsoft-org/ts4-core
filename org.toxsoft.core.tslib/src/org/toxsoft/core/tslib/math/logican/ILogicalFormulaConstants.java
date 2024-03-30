package org.toxsoft.core.tslib.math.logican;

import org.toxsoft.core.tslib.math.logicop.*;

/**
 * Contains definitions of the tokens recognized by the logical formula.
 *
 * @author hazard157
 */
public interface ILogicalFormulaConstants {

  /**
   * Token ID: unary logical NOT operation, inversion.
   */
  String TKID_LOGICAL_NOT = "tk.logical_not"; //$NON-NLS-1$

  /**
   * Token ID: binary logical AND operation {@link ELogicalOp#AND}.
   */
  String TKID_LOGICAL_AND = "tk.logical_and"; //$NON-NLS-1$

  /**
   * Token ID: binary logical OR operation {@link ELogicalOp#OR}.
   */
  String TKID_LOGICAL_OR = "tk.logical_or"; //$NON-NLS-1$

  /**
   * Token ID: binary logical XOR operation {@link ELogicalOp#XOR}.
   */
  String TKID_LOGICAL_XOR = "tk.logical_xor"; //$NON-NLS-1$

  /**
   * Token ID: boolean constant <code>true</code>.<br>
   */
  String TKID_LOGICAL_TRUE = "tk.logical_true"; //$NON-NLS-1$

  /**
   * Token ID: boolean constant <code>false</code>.<br>
   */
  String TKID_LOGICAL_FALSE = "tk.logical_false"; //$NON-NLS-1$

  /**
   * Symbols, recognized as a token in the logical formula.
   */
  String RECOGNIZED_CHARS = ILogicalOpConstants.STR_LOGICAL_OP_CHARS;

  /**
   * Upper-case keyword for boolean constant <code>true</code>.
   * <p>
   * Case-insesitive recognition creates the token of kind {@link #TKID_LOGICAL_TRUE}.
   */
  String KW_TRUE = "TRUE"; //$NON-NLS-1$

  /**
   * Upper-case keyword for boolean constant <code>false</code>.
   * <p>
   * Case-insesitive recognition creates the token of kind {@link #TKID_LOGICAL_FALSE}.
   */
  String KW_FALSE = "FALSE"; //$NON-NLS-1$

}

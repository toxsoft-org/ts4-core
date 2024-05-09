package org.toxsoft.core.tslib.math.lexan;

import org.toxsoft.core.tslib.math.lexan.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * An editable extension of {@link IFormulaTokens}.
 * <p>
 * Warning: editing the instance destroys the correspondence between {@link #tokens()} and {@link #subStrings()}. The
 * {@link #subStrings()} is NOT updated when tokens are edited.
 *
 * @author hazard157
 */
public interface IFormulaTokensEdit
    extends IFormulaTokens {

  /**
   * Replaces the token in {@link #tokens()} with the specified token.
   *
   * @param aIndex int - index of token to replace
   * @param aToken {@link ILexanToken} - the replacement token
   * @return {@link ILexanToken} - always returns argument <code>aToken</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>@
   * @throws TsIllegalArgumentRtException index out of range
   */
  ILexanToken replaceToken( int aIndex, ILexanToken aToken );

  /**
   * Replaces the token in {@link #tokens()} with the specified token.
   *
   * @param aIndex int - index of token to replace
   * @param aErrorMessage String - the error message of the replacement token {@link TkError}
   * @return {@link ILexanToken} - the error token replaced the original one
   * @throws TsNullArgumentRtException any argument = <code>null</code>@
   * @throws TsIllegalArgumentRtException index out of range
   */
  ILexanToken setErrorToken( int aIndex, String aErrorMessage );

}

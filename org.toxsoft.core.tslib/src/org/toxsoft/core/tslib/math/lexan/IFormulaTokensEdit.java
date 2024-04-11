package org.toxsoft.core.tslib.math.lexan;

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

  ILexanToken replaceToken( int aIndex, ILexanToken aToken );

  ILexanToken setErrorToken( int aIndex, String aErrorMessage );

  ILexanToken replaceTokens( int aIndex, int aCount, ILexanToken aToken );

}

package org.toxsoft.core.tslib.math.lexan;

/**
 * An editable extension of {@link IFormulaTokens}.
 *
 * @author hazard157
 */
public interface IFormulaTokensEdit
    extends IFormulaTokens {

  ILexanToken replaceToken( int aIndex, ILexanToken aToken );

  ILexanToken setErrorToken( int aIndex, String aErrorMessage );

  ILexanToken replaceTokens( int aIndex, int aCount, ILexanToken aToken );

}

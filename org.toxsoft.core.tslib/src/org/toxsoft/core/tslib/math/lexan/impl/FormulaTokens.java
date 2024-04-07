package org.toxsoft.core.tslib.math.lexan.impl;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.math.lexan.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IFormulaTokensEdit} implementation.
 *
 * @author hazard157
 */
public class FormulaTokens
    implements IFormulaTokensEdit {

  private final String                 forumlaString;
  private final IListEdit<ILexanToken> tokens;
  private final IStringListEdit        subStrings;

  /**
   * Constructor.
   * <p>
   * The argument must a valid parsed values.
   *
   * @param aFormulaString String - the formula string
   * @param aTokens {@link IList}&lt;{@link ILexanToken}&gt; - tokens making the formula
   * @param aSubStrings {@link IStringListEdit} - the substrings corresponding to the tokens
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException not a valid arguments
   */
  public FormulaTokens( String aFormulaString, IList<ILexanToken> aTokens, IStringList aSubStrings ) {
    TsNullArgumentRtException.checkNulls( aFormulaString, aTokens, aSubStrings );
    TsIllegalArgumentRtException.checkTrue( aTokens.isEmpty() );
    TsIllegalArgumentRtException.checkTrue( aSubStrings.size() != aTokens.size() );
    StringBuilder sb = new StringBuilder();
    for( String s : aSubStrings ) {
      sb.append( s );
    }
    TsIllegalArgumentRtException.checkTrue( !sb.toString().equals( aFormulaString ) );
    forumlaString = aFormulaString;
    tokens = new ElemArrayList<>( aTokens );
    subStrings = new StringArrayList( aSubStrings );
  }

  // ------------------------------------------------------------------------------------
  // IFormulaTokens
  //

  @Override
  public String formulaString() {
    return forumlaString;
  }

  @Override
  public IList<ILexanToken> tokens() {
    return tokens;
  }

  @Override
  public IStringList subStrings() {
    return subStrings;
  }

  // ------------------------------------------------------------------------------------
  // IFormulaTokensEdit
  //

  @Override
  public ILexanToken replaceToken( int aIndex, ILexanToken aToken ) {
    TsErrorUtils.checkCollIndex( tokens.size() - 1, aIndex );
    TsNullArgumentRtException.checkNull( aToken );
    tokens.set( aIndex, aToken );
    return aToken;
  }

  @Override
  public ILexanToken setErrorToken( int aIndex, String aErrorMessage ) {
    TsErrorUtils.checkCollIndex( tokens.size() - 1, aIndex );
    TsNullArgumentRtException.checkNull( aErrorMessage );
    return replaceToken( aIndex, new TkError( aErrorMessage ) );
  }

  @Override
  public ILexanToken replaceTokens( int aIndex, int aCount, ILexanToken aToken ) {
    // TODO реализовать FormulaTokens.replaceTokens()
    throw new TsUnderDevelopmentRtException( "FormulaTokens.replaceTokens()" );
  }

}

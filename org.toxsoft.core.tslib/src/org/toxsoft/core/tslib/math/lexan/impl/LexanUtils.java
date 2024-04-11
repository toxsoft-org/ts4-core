package org.toxsoft.core.tslib.math.lexan.impl;

import static org.toxsoft.core.tslib.math.lexan.ILexanConstants.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.math.lexan.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Logical formula utilities.
 *
 * @author hazard157
 */
public class LexanUtils {

  /**
   * Returns the index of the error token first symbol in the {@link IFormulaTokens#formulaString()}.
   * <p>
   * For an empty formula returns -1.
   *
   * @param aFt {@link IFormulaTokens} - the lexical analysis result
   * @return int - error starting position in formula or -1 on no error
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static int getErrorCharPos( IFormulaTokens aFt ) {
    TsNullArgumentRtException.checkNull( aFt );
    int errTokenIndex = aFt.firstErrorIndex();
    if( errTokenIndex >= 0 ) {
      return getCharPos( aFt, errTokenIndex );
    }
    return -1;
  }

  /**
   * Returns the index of the token first symbol in the {@link IFormulaTokens#formulaString()}.
   * <p>
   * For an empty formula returns -1.
   *
   * @param aFt {@link IFormulaTokens} - the lexical analysis result
   * @param aTokenIndex int - token index in
   * @return int - error starting position in formula or -1 on no error
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException index of of range
   */
  public static int getCharPos( IFormulaTokens aFt, int aTokenIndex ) {
    TsNullArgumentRtException.checkNull( aFt );
    IList<ILexanToken> ll = aFt.tokens();
    if( ll.isEmpty() ) {
      return -1;
    }
    TsErrorUtils.checkCollIndex( ll.size() - 1, aTokenIndex );
    int charIndex = 0;
    for( int i = 0; i < aTokenIndex; i++ ) {
      String ss = aFt.subStrings().get( i );
      charIndex += ss.length();
    }
    return charIndex;
  }

  /**
   * Returns unique keyword name of the tokens.
   * <p>
   * Keyword is returned by the token {@link ILexanToken#str()} of kind {@link ILexanConstants#TKID_KEYWORD}.
   *
   * @param aTokens {@link IList}&lt;{@link ILexanToken}&gt; - parsed tokens
   * @return {@link IStringList} - unique keywords
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static IStringList listKeywords1( IList<ILexanToken> aTokens ) {
    TsNullArgumentRtException.checkNull( aTokens );
    if( aTokens.isEmpty() ) {
      return IStringList.EMPTY;
    }
    IStringListEdit ll = new StringArrayList();
    for( ILexanToken t : aTokens ) {
      if( t.kindId().equals( TKID_KEYWORD ) ) {
        String kw = t.str();
        if( !ll.hasElem( kw ) ) {
          ll.add( kw );
        }
      }
    }
    return ll;
  }

  /**
   * Makes formula string from tokens list.
   * <p>
   * For an empty list returns the empty string. If last token is error, returns the error message.
   * <p>
   * Checks that only the last token is finisher {@link ILexanToken#isFinisher()} = <code>true</code>.
   *
   * @param aTokens {@link IList}&lt;{@link ILexanToken}&gt; - the tokens
   * @return String - formula string
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException tokens list is illegal
   */
  public static String makeFormulaString( IList<ILexanToken> aTokens ) {
    TsNullArgumentRtException.checkNull( aTokens );
    if( aTokens.isEmpty() ) {
      return EMPTY_STRING;
    }
    TsIllegalArgumentRtException.checkFalse( aTokens.last().isFinisher() );
    for( ILexanToken t : aTokens ) {
      if( t != aTokens.last() ) {
        TsIllegalArgumentRtException.checkTrue( t.isFinisher() );
      }
    }
    if( aTokens.last().kindId().equals( TKID_ERROR ) ) {
      return aTokens.last().str();
    }
    StringBuilder sb = new StringBuilder();
    for( ILexanToken t : aTokens ) {
      if( t == aTokens.last() ) {
        break;
      }
      sb.append( t.str() );
    }
    return sb.toString();
  }

  /**
   * No subclasses.
   */
  private LexanUtils() {
    // nop
  }

}

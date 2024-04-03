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
   * Returns the index of the error token first symbol in the {@link ILexicalAnalyzer#getFormulaString()}.
   * <p>
   * For an empty formula returns -1.
   *
   * @param aLexan {@link ILexicalAnalyzer} - the analyzer
   * @return int - error starting position in formula or -1 on no error
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static int getErrorCharPos( ILexicalAnalyzer aLexan ) {
    TsNullArgumentRtException.checkNull( aLexan );
    IList<ILexanToken> ll = aLexan.getTokens();
    if( ll.isEmpty() ) {
      return -1;
    }
    for( int i = 0; i < ll.size(); i++ ) {
      ILexanToken tk = ll.get( i );
      if( tk.kindId().equals( TKID_ERROR ) ) {
        return getCharPos( aLexan, i );
      }
    }
    return -1;
  }

  /**
   * Returns the index of the token first symbol in the {@link ILexicalAnalyzer#getFormulaString()}.
   * <p>
   * For an empty formula returns -1.
   *
   * @param aLexan {@link ILexicalAnalyzer} - the analyzer
   * @param aTokenIndex int - token index in
   * @return int - error starting position in formula or -1 on no error
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException index of of range
   */
  public static int getCharPos( ILexicalAnalyzer aLexan, int aTokenIndex ) {
    TsNullArgumentRtException.checkNull( aLexan );
    IList<ILexanToken> ll = aLexan.getTokens();
    if( ll.isEmpty() ) {
      return -1;
    }
    TsErrorUtils.checkCollIndex( ll.size() - 1, aTokenIndex );
    int charIndex = 0;
    for( int i = 0; i < aTokenIndex; i++ ) {
      String ss = aLexan.getSubStrings().get( i );
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
  public static IStringList listKeywords( IList<ILexanToken> aTokens ) {
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
   * Returns the substrings of formula string corresponding to the tokens.
   * <p>
   * This is a helper method. For example, to highlight tokens in GUI formula editor.
   * <p>
   * Concatenated substrings makes the formula string.
   *
   * @param aFormulaString String - the formula string
   * @param aTokens {@link IList}&lt;{@link ILexanToken}&gt; - parsed tokens
   * @return {@link IStringList} - substrings making the formula string
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  // public static final IStringList makeTokenSubstrings( String aFormulaString, IList<ILexanToken> aTokens ) {
  // TsNullArgumentRtException.checkNulls( aFormulaString, aTokens );
  // if( aTokens.isEmpty() ) {
  // return IStringList.EMPTY;
  // }
  // IStringListEdit ll = new StringArrayList( aTokens.size() );
  // int startIndex = 0;
  // int endIndex = 0;
  // // iterate all but last token
  // for( int i = 0; i < aTokens.size() - 1; i++ ) {
  // ILexanToken tkCurr = aTokens.get( i );
  // ILexanToken tkNext = aTokens.get( i + 1 );
  // startIndex = tkCurr.startIndex();
  // endIndex = tkNext.startIndex();
  // String s = aFormulaString.substring( startIndex, endIndex );
  // ll.add( s );
  // }
  // // process last token, always make it up to the end of formula string
  // ll.add( aFormulaString.substring( endIndex ) );
  // return ll;
  // }

  /**
   * Makes formula string from tokens list.
   * <p>
   * For an empty list returns the empty string. If last token is error, returns the error message.
   * <p>
   * Check that only the last token is finisher {@link ILexanToken#isFinisher()} = <code>true</code>.
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

  // /**
  // * Returns new tokens list with some tokens replaced by new tokens.
  // *
  // * @param aTokens {@link IList}&lt;{@link ILexanToken}&gt; - initial list of tokens
  // * @param aNew {@link IStringMap}&lt;{@link ILexanToken}&gt; - replacement map "index" - "new token"
  // * @return {@link IList}&lt;{@link ILexanToken}&gt; - resulting list of tokens
  // * @throws TsNullArgumentRtException any argument = <code>null</code>
  // */
  // public static IListEdit<ILexanToken> replaceTokens( IList<ILexanToken> aTokens, IIntMap<ILexanToken> aNew ) {
  // TsNullArgumentRtException.checkNulls( aTokens, aNew );
  // IListEdit<ILexanToken> ll = new ElemArrayList<>( aTokens );
  // int delta = 0;
  // for( int i = 0; i < ll.size(); i++ ) {
  // ILexanToken tk = aNew.findByKey( i );
  // if( tk != null ) {
  // ll.set( i, tk );
  // }
  // }
  //
  // // TODO LexanUtils.replaceTokens()
  //
  // return ll;
  // }

  /**
   * No subclasses.
   */
  private LexanUtils() {
    // nop
  }

}

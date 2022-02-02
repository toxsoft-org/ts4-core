package org.toxsoft.core.tslib.bricks.strio.chario.impl;

import java.io.IOException;

import org.toxsoft.core.tslib.utils.TsLibUtils;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Character input stream from java {@link String}.
 *
 * @author hazard157
 */
public class CharInputStreamString
    extends AbstractCharInputStream {

  private String sourceString;
  private int    counter = 0;
  private int    bound   = -1;

  /**
   * Creates reader from empty string.
   * <p>
   * Input must be set by on of the <code>setSource()</code> methods.
   */
  public CharInputStreamString() {
    sourceString = TsLibUtils.EMPTY_STRING;
  }

  /**
   * Creates reader from the specified empty string.
   *
   * @param aString String - the input string
   * @throws TsNullArgumentRtException aString = <code>null</code>
   */
  public CharInputStreamString( String aString ) {
    setSource( aString );
  }

  /**
   * Создает именованный читатель с указанной строки.
   *
   * @param aInputName String - input name returned by
   *          {@link org.toxsoft.core.tslib.bricks.strio.chario.ICharInputStream.IPosition#inputName()}
   * @param aString String - the input string
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public CharInputStreamString( String aInputName, String aString ) {
    super( aInputName );
    setSource( aString );
  }

  /**
   * Создает читатель с указанного места до конца строки.
   *
   * @param aString String - строка, источник символов
   * @param aStartIndex int - индекс первого считываемого символа
   * @throws TsNullArgumentRtException aString = null
   * @throws TsIllegalArgumentRtException индекс указывают вне строки
   */
  public CharInputStreamString( String aString, int aStartIndex ) {
    setSource( aString, aStartIndex );
  }

  // ------------------------------------------------------------------------------------
  // Internal methods
  //

  private void init( String aString, int aStartIndex, int aEndIndex ) {
    sourceString = aString;
    int strLen = aString.length();
    TsIllegalArgumentRtException.checkTrue( aStartIndex < 0 );
    TsIllegalArgumentRtException.checkTrue( aEndIndex < 0 );
    TsIllegalArgumentRtException.checkTrue( aStartIndex > strLen && strLen != 0 );
    TsIllegalArgumentRtException.checkTrue( aEndIndex > strLen );
    TsIllegalArgumentRtException.checkTrue( aStartIndex > aEndIndex );
    counter = aStartIndex;
    bound = aEndIndex;
  }

  // ------------------------------------------------------------------------------------
  // Additional API
  //

  /**
   * Changes the input string.
   *
   * @param aString String - the input string
   * @param aStartIndex int - the begin index, inclusive
   * @param aEndIndex int - the end index, exclusive
   * @throws TsNullArgumentRtException aString = <code>null</code>
   * @throws TsIllegalArgumentRtException any index is out of range or aStartIndex > aEndIndex
   */
  public void setSource( String aString, int aStartIndex, int aEndIndex ) {
    TsNullArgumentRtException.checkNull( aString );
    init( aString, aStartIndex, aEndIndex );
  }

  /**
   * Changes the input string.
   *
   * @param aString String - the input string
   * @param aStartIndex int - the begin index, inclusive
   * @throws TsNullArgumentRtException aString = <code>null</code>
   * @throws TsIllegalArgumentRtException any index is out of range
   */
  public void setSource( String aString, int aStartIndex ) {
    TsNullArgumentRtException.checkNull( aString );
    init( aString, aStartIndex, aString.length() );
  }

  /**
   * Changes the input string.
   *
   * @param aString String - the input string
   * @throws TsNullArgumentRtException aString = <code>null</code>
   */
  public void setSource( String aString ) {
    TsNullArgumentRtException.checkNull( aString );
    init( aString, 0, aString.length() );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  @Override
  public int doReadChar()
      throws IOException {
    if( counter < bound ) {
      return sourceString.charAt( counter++ );
    }
    return -1;
  }

}

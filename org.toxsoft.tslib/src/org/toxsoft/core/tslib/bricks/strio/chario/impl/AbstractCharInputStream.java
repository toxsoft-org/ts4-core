package org.toxsoft.core.tslib.bricks.strio.chario.impl;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import java.io.IOException;

import org.toxsoft.core.tslib.bricks.strio.chario.ICharInputEofCallback;
import org.toxsoft.core.tslib.bricks.strio.chario.ICharInputStream;
import org.toxsoft.core.tslib.utils.TsLibUtils;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Base class for all {@link ICharInputStream} implementations.
 * <p>
 * Contains EOF processing basic functionality.
 *
 * @author hazard157
 */
public abstract class AbstractCharInputStream
    implements ICharInputStream {

  class Position
      implements IPosition {

    @Override
    public String inputName() {
      return inputName;
    }

    @Override
    public int lineNo() {
      return lineNo;
    }

    @Override
    public int charNo() {
      return charNo;
    }

  }

  final String inputName;
  int          lineNo = 0;
  int          charNo = 0;

  private boolean               wasNewLine  = false;
  private final IPosition       pos         = new Position();
  private ICharInputEofCallback eofCallback = ICharInputEofCallback.NONE;

  /**
   * Constructor.
   *
   * @param aInputName String - input name returned by
   *          {@link org.toxsoft.core.tslib.bricks.strio.chario.ICharInputStream.IPosition#inputName()}
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  protected AbstractCharInputStream( String aInputName ) {
    TsNullArgumentRtException.checkNull( aInputName );
    inputName = aInputName;
  }

  /**
   * Constructor with end-of-file callback.
   *
   * @param aInputName String - input name returned by
   *          {@link org.toxsoft.core.tslib.bricks.strio.chario.ICharInputStream.IPosition#inputName()}
   * @param aEofCallback ICharInputEofCallback - end-of-file callback
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  protected AbstractCharInputStream( String aInputName, ICharInputEofCallback aEofCallback ) {
    this( aInputName );
    eofCallback = TsNullArgumentRtException.checkNull( aEofCallback );
  }

  protected AbstractCharInputStream() {
    this( TsLibUtils.EMPTY_STRING );
  }

  // ------------------------------------------------------------------------------------
  // ICharInputStream
  //

  @Override
  public ICharInputEofCallback eofCallback() {
    return eofCallback;
  }

  @Override
  public void setEofCallback( ICharInputEofCallback aEofCallback ) {
    eofCallback = TsNullArgumentRtException.checkNull( aEofCallback );
  }

  @Override
  final public int readChar()
      throws IOException {
    int c = doReadChar();
    if( c == -1 ) {
      return eofCallback.onEof();
    }
    if( wasNewLine ) { // считан первый символ на новой строке
      charNo = 0;
      ++lineNo;
      wasNewLine = false;
    }
    ++charNo;
    if( c == CHAR_EOL ) { // обнаружено окончание строки
      wasNewLine = true;
    }
    return c;
  }

  @Override
  public IPosition currentPosition() {
    return pos;
  }

  // ------------------------------------------------------------------------------------
  // Methods for implementation
  //

  /**
   * Descendant must read next character from input stream.
   * <p>
   * This method on EOD must just return -1. EOF callbakc will e called by {@link #readChar()}.
   *
   * @return int - character from inpuit stream or -1 on EOF
   * @throws IOException any input/output error occured
   */
  abstract protected int doReadChar()
      throws IOException;

}

package org.toxsoft.core.tslib.bricks.strio.impl;

import static org.toxsoft.core.tslib.bricks.strio.impl.ITsResources.*;

import java.io.IOException;

import org.toxsoft.core.tslib.bricks.strio.StrioRtException;
import org.toxsoft.core.tslib.bricks.strio.chario.ICharInputStream;
import org.toxsoft.core.tslib.utils.errors.TsIoRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link ICharInputStream} wrapper with ability to "return back" read characters.
 * <p>
 *
 * @author hazard157
 */
class CharSource {

  /**
   * The size of the return-back buffer.
   */
  private static int PUTBACK_BUF_SIZE = 128;

  /**
   * Input stream.
   */
  private ICharInputStream charIn;

  // buffer and pointers for storing read/returned characters
  private final int[] charBuf;              // circular buffer of characters read
  private int         head         = 0;     // buffer head, where the characters are written
  private boolean     bufIsFull    = false; // buffer is filled with characters read
  private int         putbackCount = 0;
  private int         currPos      = 0;     // position of last character in stream

  /**
   * Constructor.
   *
   * @param aCharInputStream {@link ICharInputStream} - input stream
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  CharSource( ICharInputStream aCharInputStream ) {
    charIn = TsNullArgumentRtException.checkNull( aCharInputStream );
    charBuf = new int[PUTBACK_BUF_SIZE];
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns input stream.
   *
   * @return {@link ICharInputStream} - input stream
   */
  ICharInputStream inputStream() {
    return charIn;
  }

  /**
   * Changes the input stream.
   *
   * @param aCharInputStream {@link ICharInputStream} - the new input stream
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setInputStream( ICharInputStream aCharInputStream ) {
    charIn = TsNullArgumentRtException.checkNull( aCharInputStream );
    head = 0;
    bufIsFull = false;
    putbackCount = 0;
    currPos = 0;
  }

  /**
   * Returns next character from the input stream.
   * <p>
   * If there were charactes marked as returned back with method {@link #putCharBack()}, then those characters will be
   * returned before input stream fetching continues.
   *
   * @return int - next character from input stream
   * @throws TsIoRtException {@link IOException} occured while fetching input stream
   */
  int nextChar() {
    // если есть возвращенные символы, то сначала выдадим их
    if( putbackCount > 0 ) {
      int pos = head - putbackCount;
      if( pos < 0 ) {
        pos += charBuf.length;
      }
      --putbackCount;
      ++currPos;
      return charBuf[pos];
    }
    try {
      int ch = charIn.readChar();
      charBuf[head++] = ch;
      if( head == charBuf.length ) {
        head = 0;
        bufIsFull = true;
      }
      ++currPos;
      return ch;
    }
    catch( IOException e ) {
      throw new TsIoRtException( e );
    }
  }

  // TRANSLATE

  /**
   * Помечает только что прочитанный символ к повторному чтению.
   * <p>
   * Последовательные вызовы данного метода "отодвигают" чтение назад по уже проичтанным символам. Подряд можно пометить
   * количество символов, не превышающий размер буфера, заданный в конструкторе.
   *
   * @throws StrioRtException попытка вернуть назад слишком много символов
   * @throws StrioRtException попытка вернуть назад символы, которые не были прочитаны
   */
  void putCharBack() {
    // кол-во считанных символов в буфере (после первого круга - charBuf.length)
    int readCharsInBufCount = charBuf.length;
    if( !bufIsFull ) {
      readCharsInBufCount = head;
    }
    // проверим, что не все считанные символы уже помечены как возвращенные
    if( putbackCount >= readCharsInBufCount ) {
      if( bufIsFull ) {
        throw new StrioRtException( FMT_ERR_PUTING_BACK_TOO_MANY_CHARS, Integer.valueOf( readCharsInBufCount ) );
      }
      throw new StrioRtException( MSG_ERR_PUTING_BACK_UNREAD_CHAR );
    }
    ++putbackCount;
    --currPos;
  }

  /**
   * Возвращает текущую позицию символа, который будет считан.
   * <p>
   * Отсчет ведется с момента создания этого объекта. Смена входного потока методом
   * {@link #setInputStream(ICharInputStream)} не меняет значение счетчика.
   *
   * @return int - текущее положение символа в потоке (начинается с 0)
   */
  int currentPosition() {
    return currPos;
  }
}

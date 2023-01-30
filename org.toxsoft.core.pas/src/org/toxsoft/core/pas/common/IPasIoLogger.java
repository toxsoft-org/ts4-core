package org.toxsoft.core.pas.common;

/**
 * Журнал.
 *
 * @author mvk
 */
public interface IPasIoLogger {

  /**
   * Сообщение для журнала о прочтении символа из потока.
   *
   * @param aChar char прочитанный символ потока
   */
  void readChar( char aChar );

}

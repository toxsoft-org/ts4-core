package org.toxsoft.core.tslib.utils.logs.impl;

import org.toxsoft.core.tslib.coll.primtypes.IStringList;
import org.toxsoft.core.tslib.coll.primtypes.IStringListEdit;
import org.toxsoft.core.tslib.coll.primtypes.impl.StringLinkedBundleList;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

// TRANSLATE

/**
 * Реализация логера, запоминающая лог в памяти.
 * <p>
 * Имеет ограничение по используемой памяти. При превышении памяти "забывает" наиболее старые сообщения.
 * <p>
 * Строки (содержание) логера доступны как список строк
 *
 * @author hazard157
 */
public class InMemoryLogger
    extends AbstractLogger {

  /**
   * Максимальное значение используемой памяти в килобайтах.
   */
  public static final int MAX_MEM_KB = 64 * 1024; // 64 Мбайт

  /**
   * Максимальное значение используемой памяти в килобайтах.
   */
  public static final int MIN_MEM_KB = 64; // 64 Кбайт

  /**
   * Значение используемой памяти по умолчанию.
   */
  public static final int DEFAULT_MEM_KB = 512; // 512 Кбайт

  private final int             memKb;
  private final IStringListEdit messages = new StringLinkedBundleList();

  /**
   * Создает логер с органиченим используемой памяти по умолчанию.
   */
  public InMemoryLogger() {
    this( DEFAULT_MEM_KB, new LogMessageFormatter() );
  }

  /**
   * Создает логер с органиченим используемой памяти по умолчанию.
   * <p>
   * Если аргумент выходит за допустимые пределы, он насильно вгоняется в эти пределы.
   *
   * @param aMemKb int - объем используемой памяти в Кибибайтах
   * @param aFormatter {@link LogMessageFormatter} - log messages formatter
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public InMemoryLogger( int aMemKb, LogMessageFormatter aFormatter ) {
    super( aFormatter );
    if( aMemKb < MIN_MEM_KB ) {
      memKb = MIN_MEM_KB;
    }
    else {
      if( aMemKb > MAX_MEM_KB ) {
        memKb = MAX_MEM_KB;
      }
      else {
        memKb = MIN_MEM_KB;
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  private void checkAndFreeMemory() {
    long used = 0;
    for( int i = 0, n = messages.size(); i < n; i++ ) {
      used += 4 * messages.get( i ).length(); // каждый символ - 4 байта
      used += 16; // в 32-биной реализациии минимум столько добавляется на каждую ссылку на String
    }
    int usedKb = (int)(used / 1024L);
    if( usedKb > memKb ) {
      int delta = (usedKb - memKb) * 1024;
      while( delta > 0 && !messages.isEmpty() ) {
        messages.removeByIndex( 0 );
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса AbstractLogger
  //

  @Override
  protected void printLine( String aLine ) {
    messages.add( aLine );
    checkAndFreeMemory();
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает сообщения лога.
   * <p>
   * Первый элемент (с индексом 0) - самое старое сообщение.
   *
   * @return {@link IStringList} - сообщения лога
   */
  public IStringList messages() {
    return messages;
  }

  /**
   * Очищает все сообщения лога.
   */
  public void clear() {
    messages.clear();
    System.gc();
  }

}

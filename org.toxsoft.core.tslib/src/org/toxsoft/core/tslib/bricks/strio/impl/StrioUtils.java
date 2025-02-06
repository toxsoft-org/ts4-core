package org.toxsoft.core.tslib.bricks.strio.impl;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;
import static org.toxsoft.core.tslib.bricks.strio.impl.ITsResources.*;

import java.io.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.chario.*;
import org.toxsoft.core.tslib.bricks.strio.chario.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Helper methods for token reader/writer.
 *
 * @author hazard157
 */
public class StrioUtils {

  /**
   * Returns <code>true</code> if character is ASCII letter a-Z or A-Z.
   *
   * @param aCh char - character to be checked
   * @return <b>true</b> - argument is ASCII letter;<br>
   *         <b>false</b> - something else.
   */
  public static boolean isAsciiChar( char aCh ) {
    if( aCh >= 'a' && aCh <= 'z' ) {
      return true;
    }
    if( aCh >= 'A' && aCh <= 'Z' ) {
      return true;
    }
    return false;
  }

  /**
   * Returns <code>true</code> if character is ASCII digit (0-9).
   *
   * @param aCh char - character to be checked
   * @return <b>true</b> - argument is ASCII digit;<br>
   *         <b>false</b> - something else.
   */
  public static boolean isAsciiDigit( char aCh ) {
    return aCh >= '0' && aCh <= '9';
  }

  /**
   * Returns <code>true</code> if character allowed hexadecimal reprezentation 0-9, A-F, a-f.
   *
   * @param aCh char - character to be checked
   * @return <b>true</b> - argument is hexadecimal digit;<br>
   *         <b>false</b> - something else.
   */
  public static boolean isHexChar( char aCh ) {
    if( aCh >= '0' && aCh <= '9' ) {
      return true;
    }
    if( aCh >= 'A' && aCh <= 'F' ) {
      return true;
    }
    if( aCh >= 'a' && aCh <= 'f' ) {
      return true;
    }
    return false;
  }

  /**
   * Determines if argument is the end-of-line marker.
   *
   * @param aCh char - character to be checked
   * @return <b>true</b> - this is the end-of-line marker;<br>
   *         <b>false</b> - any other char.
   */
  public static boolean isEol( char aCh ) {
    return (aCh == CHAR_EOL) || (aCh == '\r');
  }

  // ------------------------------------------------------------------------------------
  // Conversions
  //

  // TODO TRANSLATE

  /**
   * Возвращает числовое значение 16-тиричного символа.
   *
   * @param aHexChar char - символ, один из диапазонов 0-9, A-F, a-f
   * @return int - целое число в диапазоне 0-15
   * @throws StrioRtException символ не из допустимого диапазона
   */
  public static final int hexChar2Int( char aHexChar ) {
    if( aHexChar >= '0' && aHexChar <= '9' ) {
      return aHexChar - '0';
    }
    if( aHexChar >= 'A' && aHexChar <= 'F' ) {
      return aHexChar - 'A' + 10;
    }
    if( aHexChar >= 'a' && aHexChar <= 'f' ) {
      return aHexChar - 'a' + 10;
    }
    throw new StrioRtException( FMT_ERR_HEX_CHAR_EXPECTED, Character.valueOf( aHexChar ) );
  }

  /**
   * Возвращает 16-теричное представление числа в диапазоне 0..15 (0x0, 0xF).
   *
   * @param aValue int - число в диапазоне 0..15 (0x0, 0xF).
   * @return char - 16-тиричный символ в диапазоне 0-9 или A-Z
   * @throws TsIllegalArgumentRtException - агрумент выходит за пределы 0..15
   */
  public static final char int2HexChar( int aValue ) {
    if( aValue < 0 || aValue > 15 ) {
      throw new TsIllegalArgumentRtException();
    }
    if( aValue < 10 ) {
      return (char)(aValue + '0');
    }
    return (char)(aValue + 'A');
  }

  // ------------------------------------------------------------------------------------
  // Keyword header I/O
  //

  /**
   * Записывает заголовок "Keyword = ".
   * <p>
   * Если аргумент aKeyword пустая строка, то метод ничего не делает.
   *
   * @param aSw {@link IStrioWriter} - писатель текстового представления
   * @param aKeyword String - ключевое слово, ИД-путь или пустая строка
   * @param aIndented boolean - признак удобочитаемогой записи текста
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException aKeywrod не ИД-путь
   */
  public static void writeKeywordHeader( IStrioWriter aSw, String aKeyword, boolean aIndented ) {
    TsNullArgumentRtException.checkNulls( aSw, aKeyword );
    if( !aKeyword.isEmpty() ) {
      StridUtils.checkValidIdPath( aKeyword );
      aSw.writeAsIs( aKeyword );
      if( aIndented ) {
        aSw.writeChars( CHAR_SPACE, CHAR_EQUAL, CHAR_SPACE );
      }
      else {
        aSw.writeChars( CHAR_EQUAL );
      }
    }
  }

  /**
   * Записывает заголовок "Keyword = ".
   * <p>
   * Равнозначен вызову {@link #writeKeywordHeader(IStrioWriter, String, boolean) writeKeywordHeader(aSw, aKeyword,
   * <b>true</b>)}.
   *
   * @param aSw {@link IStrioWriter} - писатель текстового представления
   * @param aKeyword String - ключевое слово, ИД-путь или пустая строка
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException aKeywrod не ИД-путь
   */
  public static void writeKeywordHeader( IStrioWriter aSw, String aKeyword ) {
    writeKeywordHeader( aSw, aKeyword, true );
  }

  /**
   * Считывает заголовок, записанный методом {@link #writeKeywordHeader(IStrioWriter, String)}.
   * <p>
   * Если аргумент aKeyword пустая строка, то метод ничего не делает.
   *
   * @param aSr {@link IStrioReader} - читатедль из текстового представления
   * @param aKeyword String - ключевое слово, ИД-путь или пустая строка
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException aKeywrod не ИД-путь
   * @throws TsIoRtException при чтении возникло исключение {@link IOException}
   * @throws StrioRtException вход не совпадает со ожидаемым заголовком
   */
  public static void ensureKeywordHeader( IStrioReader aSr, String aKeyword ) {
    TsNullArgumentRtException.checkNulls( aSr, aKeyword );
    if( !aKeyword.isEmpty() ) {
      StridUtils.checkValidIdPath( aKeyword );
      aSr.ensureString( aKeyword );
      aSr.ensureChar( CHAR_EQUAL );
    }
  }

  // ------------------------------------------------------------------------------------
  // Array I/O
  //

  /**
   * Writes an <code>int</code> array.
   * <p>
   * Array is written in form "<code>[N1,N2,...Nn]</code>".
   *
   * @param aSw {@link IStrioWriter} - output stream
   * @param aArray int[] - array to write
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static void writeIntArray( IStrioWriter aSw, int[] aArray ) {
    TsNullArgumentRtException.checkNulls( aSw, aArray );
    aSw.writeChar( CHAR_ARRAY_BEGIN );
    for( int i = 0; i < aArray.length; i++ ) {
      aSw.writeInt( aArray[i] );
      if( i < aArray.length - 1 ) {
        aSw.writeSeparatorChar();
      }
    }
    aSw.writeChar( CHAR_ARRAY_END );
  }

  /**
   * Reads <code>int</code> array written by {@link #writeIntArray(IStrioWriter, int[])}.
   *
   * @param aSr {@link IStrioReader} - input stream
   * @return int[] - read array
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static int[] readIntArray( IStrioReader aSr ) {
    TsNullArgumentRtException.checkNull( aSr );
    if( aSr.readArrayBegin() ) {
      IntArrayList ll = new IntArrayList();
      do {
        ll.add( aSr.readInt() );
      } while( aSr.readArrayNext() );
      return ll.getInternalArray();
    }
    return TsLibUtils.EMPTY_ARRAY_OF_INTS;
  }

  // ------------------------------------------------------------------------------------
  // Collection i/o
  //

  /**
   * Записывает коллекцию хранимых элементов в текстовое представление.
   * <p>
   * Запись коллекции происходит в виде:
   *
   * <pre>
   * KEYWORD=[
   *   Элемент1,
   *   ...
   *   ЭлементN-1,
   *   ЭлементN
   * ]
   * </pre>
   *
   * Где:
   * <ul>
   * <li><b>KEYWORD</b> - ключевое слово, аргумент aKeyword этого метода;</li>
   * <li><b>=</b> - символ {@link IStrioHardConstants#CHAR_EQUAL};</li>
   * <li><b>[</b> и <b>]</b> - символы начала/окончания массива {@link IStrioHardConstants#CHAR_ARRAY_BEGIN} и
   * {@link IStrioHardConstants#CHAR_ARRAY_END};</li>
   * <li><b>ЭлементN</b> - элементы коллекции, записанные методом
   * {@link IEntityKeeper#write(IStrioWriter, Object)};</li>
   * <li><b>,</b> - зпаятая {@link IStrioHardConstants#CHAR_ITEM_SEPARATOR} разделаяет элементы между собой. Обратите
   * внимание, что после последнего элемента запятая <b>не</b> ставиться.</li>
   * </ul>
   * <p>
   * Если aKeyword пустая строка, то метод равнозначен
   * {@link IEntityKeeper#writeColl(IStrioWriter, ITsCollection, boolean)} с аргументом Indented = <code>true</code>.
   *
   * @param <E> - тип хранимых сущностей
   * @param aSw {@link IStrioWriter} - писатель к текстовое прдставление
   * @param aKeyword String - ключевое слово (ИД-путь), предваряющее коллекцию или пустая строка
   * @param aColl {@link ITsCollection} - записывааемая коллекция
   * @param aKeeper {@link IEntityKeeper} - хранитель элемсентов коллекции
   * @param aIndented boolean - признак расположения каждого элемента на отдельной строке
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException aKeyword не ИД-путь
   * @throws TsIoRtException ошибка записи в выходной поток
   */
  public static <E> void writeCollection( IStrioWriter aSw, String aKeyword, ITsCollection<E> aColl,
      IEntityKeeper<E> aKeeper, boolean aIndented ) {
    TsNullArgumentRtException.checkNulls( aSw, aColl, aKeeper, aKeyword );
    writeKeywordHeader( aSw, aKeyword, aIndented );
    aKeeper.writeColl( aSw, aColl, aIndented );
  }

  /**
   * Записывает коллекцию хранимых элементов в текстовое представление.
   * <p>
   * Аналогично вызову {@link #writeCollection(IStrioWriter, String, ITsCollection, IEntityKeeper, boolean)} с
   * аргументом aIndenter = <code>true</code>.
   *
   * @param <E> - тип хранимых сущностей
   * @param aDw {@link IStrioWriter} - писатель к текстовое прдставление
   * @param aKeyword String - ключевое слово (ИД-путь), предваряющее коллекцию или пустая строка
   * @param aColl {@link ITsCollection} - записывааемая коллекция
   * @param aKeeper {@link IEntityKeeper} - хранитель элемсентов коллекции
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException aKeyword не ИД-путь
   * @throws TsIoRtException ошибка записи в выходной поток
   */
  public static <E> void writeCollection( IStrioWriter aDw, String aKeyword, ITsCollection<E> aColl,
      IEntityKeeper<E> aKeeper ) {
    writeCollection( aDw, aKeyword, aColl, aKeeper, true );
  }

  /**
   * Считывает коллекцию сохраняемых элементов из текстового представления.
   * <p>
   * Коллекция должна быть ранее записанна методом
   * {@link #writeCollection(IStrioWriter, String, ITsCollection, IEntityKeeper)}.
   * <p>
   * Возвращаемое значение можно безопасно приводить к {@link IListEdit}.
   *
   * @param <E> - тип хранимых сущностей
   * @param aSr {@link IStrioReader} - читатедль из текстового представления
   * @param aKeyword String - ключевое слово (ИД-путь), предваряющее коллекцию или пустая строка
   * @param aKeeper {@link IEntityKeeper} - хранитель элементов коллекции
   * @return {@link IListEdit} - считанная коллекция
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException aKeyword не ИД-путь
   * @throws StrioRtException синтаксическая ошибка текстового представления
   * @throws TsIoRtException ошибка чтения из входного потока
   */
  public static <E> IListEdit<E> readCollection( IStrioReader aSr, String aKeyword, IEntityKeeper<E> aKeeper ) {
    TsNullArgumentRtException.checkNulls( aSr, aKeeper, aKeyword );
    if( !aKeyword.isEmpty() ) {
      StridUtils.checkValidIdPath( aKeyword );
    }
    ensureKeywordHeader( aSr, aKeyword );
    IListEdit<E> coll = new ElemLinkedBundleList<>();
    aKeeper.readColl( aSr, coll );
    return coll;
  }

  /**
   * Записывает карту "ключ" - "сущность" в текстовое представление.
   * <p>
   * Запись проиходит в виде <code>KEYWORD = { KEY1=ENTITY, KEY2=ENTITY, ... KEYm=ENTITY }</code>, где KEYm - записанный
   * с помощью <code>aKeyKeeper</code> ключ в карте, а ENTITY - записанная с помощью <code>aValueKeeper</code> сущность.
   * Если aKeyword пустая строка, то часть записы "KEYWORD = " отсуствет.
   *
   * @param <K> - тип ключей в карте
   * @param <E> - тип хранимых элементов, значений в карте
   * @param aSw {@link IStrioWriter} - писатель к текстовое прдставление
   * @param aKeyword String - ключевое слово (ИД-путь), предваряющее коллекцию или пустая строка
   * @param aMap {@link IStringMap} - записываемая карта "ключ" - "сущность"
   * @param aKeyKeeper {@link IEntityKeeper} - хранитель ключей карты
   * @param aValueKeeper {@link IEntityKeeper} - хранитель элемсентов карты
   * @param aIndent boolean - признак переноса строк<br>
   *          <b>true</b> - каждая пана KEYm=ENTITY записывается на отдельной строке;<br>
   *          <b>false</b> - вся карта записывается в одну строку, без переноса.
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException aKeyword не ИД-путь
   * @throws TsIoRtException ошибка записи в выходной поток
   */
  public static <K, E> void writeMap( IStrioWriter aSw, String aKeyword, IMap<K, E> aMap, IEntityKeeper<K> aKeyKeeper,
      IEntityKeeper<E> aValueKeeper, boolean aIndent ) {
    TsNullArgumentRtException.checkNulls( aSw, aMap, aKeyKeeper, aValueKeeper );
    // запись "KEYWORD = "
    writeKeywordHeader( aSw, aKeyword );
    aSw.writeChar( CHAR_SET_BEGIN );
    // запись пустой карты
    if( aMap.isEmpty() ) {
      aSw.writeChar( CHAR_SET_END );
      return;
    }
    if( aIndent ) {
      aSw.incNewLine();
    }
    for( int i = 0, n = aMap.size(); i < n; i++ ) {
      K key = aMap.keys().get( i );
      E e = aMap.getByKey( key );
      if( !aIndent ) {
        aSw.writeSpace();
      }
      aKeyKeeper.write( aSw, key );
      aSw.writeChars( CHAR_EQUAL );
      aValueKeeper.write( aSw, e );
      if( i != n - 1 ) {
        aSw.writeChar( CHAR_ITEM_SEPARATOR );
        if( aIndent ) {
          aSw.writeEol();
        }
      }
    }
    if( aIndent ) {
      aSw.decNewLine();
    }
    else {
      aSw.writeSpace();
    }
    aSw.writeChar( CHAR_SET_END );
  }

  /**
   * Считывает карту "ключ" - "сущность" из текстового представления, записанную методом
   * {@link #writeMap(IStrioWriter, String, IMap, IEntityKeeper, IEntityKeeper, boolean)}.
   * <p>
   * Возвращаемое значение можно безопасно приводить к {@link IMap}.
   *
   * @param <K> - тип ключей в карте
   * @param <E> - тип хранимых элементов, значений в карте
   * @param aSr {@link IStrioReader} - читатель из текстового прдставления
   * @param aKeyword String - ключевое слово (ИД-путь), предваряющее коллекцию или пустая строка
   * @param aKeyKeeper {@link IEntityKeeper} - хранитель ключей карты
   * @param aValueKeeper {@link IEntityKeeper} - хранитель элемсентов карты
   * @return {@link IStringMapEdit} - считанная карта
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException aKeyword не ИД-путь
   * @throws StrioRtException синтаксическая ошибка текстового представления
   * @throws TsIoRtException ошибка чтения из входного потока
   */
  public static <K, E> IMapEdit<K, E> readMap( IStrioReader aSr, String aKeyword, IEntityKeeper<K> aKeyKeeper,
      IEntityKeeper<E> aValueKeeper ) {
    TsNullArgumentRtException.checkNulls( aSr, aKeyKeeper, aValueKeeper );
    ensureKeywordHeader( aSr, aKeyword );
    IMapEdit<K, E> map = new ElemMap<>();
    if( aSr.readSetBegin() ) {
      do {
        K key = aKeyKeeper.read( aSr );
        aSr.ensureChar( CHAR_EQUAL );
        E e = aValueKeeper.read( aSr );
        map.put( key, e );
      } while( aSr.readSetNext() );
    }
    return map;
  }

  /**
   * Считывает карту "ключ" - "сущность" из текстового представления, записанную методом
   * {@link #writeMap(IStrioWriter, String, IMap, IEntityKeeper, IEntityKeeper, boolean)}.
   * <p>
   * Возвращаемое значение можно безопасно приводить к {@link IMap}.
   *
   * @param <K> - тип ключей в карте
   * @param <E> - тип хранимых элементов, значений в карте
   * @param aSr {@link IStrioReader} - читатель из текстового прдставления
   * @param aKeyword String - ключевое слово (ИД-путь), предваряющее коллекцию или пустая строка
   * @param aKeyKeeper {@link IEntityKeeper} - хранитель ключей карты
   * @param aValueKeeper {@link IEntityKeeper} - хранитель элемсентов карты
   * @param aMap {@link IStringMapEdit} - карта, куда будут считаны элементы
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException aKeyword не ИД-путь
   * @throws StrioRtException синтаксическая ошибка текстового представления
   * @throws TsIoRtException ошибка чтения из входного потока
   */
  public static <K, E> void readMap( IStrioReader aSr, String aKeyword, IEntityKeeper<K> aKeyKeeper,
      IEntityKeeper<E> aValueKeeper, IMapEdit<K, E> aMap ) {
    TsNullArgumentRtException.checkNulls( aSr, aKeyKeeper, aValueKeeper, aMap );
    ensureKeywordHeader( aSr, aKeyword );
    if( aSr.readSetBegin() ) {
      do {
        K key = aKeyKeeper.read( aSr );
        aSr.ensureChar( CHAR_EQUAL );
        E e = aValueKeeper.read( aSr );
        aMap.put( key, e );
      } while( aSr.readSetNext() );
    }
  }

  /**
   * Записывает карту "строка" - "сущность" в текстовое представление.
   * <p>
   * Запись проиходит в виде <code>KEYWORD = { "key1"=ENTITY, "key2"=ENTITY, ... "keyM"=ENTITY }</code>, где keyN - ключ
   * в карте, а ENTITY - записанная с помощью <code>aKeeper</code> сущность. Если aKeyword пустая строка, то часть
   * записы "KEYWORD = " отсуствет.
   *
   * @param <E> - тип хранимых сущностей
   * @param aSw {@link IStrioWriter} - писатель к текстовое прдставление
   * @param aKeyword String - ключевое слово (ИД-путь), предваряющее коллекцию или пустая строка
   * @param aMap {@link IStringMap} - записываемая карта "строка" - "сущность"
   * @param aKeeper {@link IEntityKeeper} - хранитель элемсентов коллекции
   * @param aIndent boolean - признак переноса строк<br>
   *          <b>true</b> - каждая пана "keyN"=ENTITY записывается на отдельной строке;<br>
   *          <b>false</b> - вся карта записывается в одну строку, без переноса.
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException aKeyword не ИД-путь
   * @throws TsIoRtException ошибка чтения из входного потока
   */
  public static <E> void writeStringMap( IStrioWriter aSw, String aKeyword, IStringMap<E> aMap,
      IEntityKeeper<E> aKeeper, boolean aIndent ) {
    TsNullArgumentRtException.checkNulls( aSw, aMap, aKeeper );
    // запись "KEYWORD = "
    writeKeywordHeader( aSw, aKeyword );
    // запись пустой карты
    if( aMap.isEmpty() ) {
      aSw.writeChars( CHAR_SET_BEGIN, CHAR_SET_END );
      return;
    }
    aSw.writeChar( CHAR_SET_BEGIN );
    if( aIndent ) {
      aSw.incNewLine();
    }
    for( int i = 0, n = aMap.size(); i < n; i++ ) {
      String key = aMap.keys().get( i );
      E e = aMap.getByKey( key );
      if( !aIndent ) {
        aSw.writeSpace();
      }
      if( StridUtils.isValidIdPath( key ) ) {
        aSw.writeAsIs( key );
      }
      else {
        aSw.writeQuotedString( key );
      }
      aSw.writeChar( CHAR_EQUAL );
      aKeeper.write( aSw, e );
      if( i != n - 1 ) {
        aSw.writeChar( CHAR_ITEM_SEPARATOR );
        if( aIndent ) {
          aSw.writeEol();
        }
      }
    }
    if( aIndent ) {
      aSw.decNewLine();
    }
    else {
      aSw.writeSpace();
    }
    aSw.writeChar( CHAR_SET_END );
  }

  /**
   * Считывает карту "строка" - "сущность" из текстового представления, записанную методом
   * {@link #writeStringMap(IStrioWriter, String, IStringMap, IEntityKeeper, boolean)}.
   * <p>
   * Возвращаемое значение можно безопасно приводить к {@link IStringMapEdit}.
   *
   * @param <E> - тип хранимых сущностей
   * @param aSr {@link IStrioReader} - читатель из текстового прдставления
   * @param aKeyword String - ключевое слово (ИД-путь), предваряющее коллекцию или пустая строка
   * @param aKeeper {@link IEntityKeeper} - хранитель элемсентов коллекции
   * @param aMap {@link IStringMapEdit} - карта, куда будут считаны элементы
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException aKeyword не ИД-путь
   * @throws StrioRtException синтаксическая ошибка текстового представления
   * @throws TsIoRtException ошибка чтения из входного потока
   */
  public static <E> void readStringMap( IStrioReader aSr, String aKeyword, IEntityKeeper<E> aKeeper,
      IStringMapEdit<E> aMap ) {
    TsNullArgumentRtException.checkNulls( aSr, aKeeper, aMap );
    ensureKeywordHeader( aSr, aKeyword );
    if( aSr.readSetBegin() ) {
      do {
        String key;
        if( aSr.peekChar( EStrioSkipMode.SKIP_COMMENTS ) == CHAR_QUOTE ) {
          key = aSr.readQuotedString();
        }
        else {
          key = aSr.readIdPath();
        }
        aSr.ensureChar( CHAR_EQUAL );
        E e = aKeeper.read( aSr );
        aMap.put( key, e );
      } while( aSr.readSetNext() );
    }
  }

  /**
   * Считывает карту "строка" - "сущность" из текстового представления, записанную методом
   * {@link #writeStringMap(IStrioWriter, String, IStringMap, IEntityKeeper, boolean)}.
   * <p>
   * Возвращаемое значение можно безопасно приводить к {@link IStringMapEdit}.
   *
   * @param <E> - тип хранимых сущностей
   * @param aSr {@link IStrioReader} - читатель из текстового прдставления
   * @param aKeyword String - ключевое слово (ИД-путь), предваряющее коллекцию или пустая строка
   * @param aKeeper {@link IEntityKeeper} - хранитель элемсентов коллекции
   * @return {@link IStringMapEdit} - считанная карта
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException aKeyword не ИД-путь
   * @throws StrioRtException синтаксическая ошибка текстового представления
   */
  public static <E> IStringMapEdit<E> readStringMap( IStrioReader aSr, String aKeyword, IEntityKeeper<E> aKeeper ) {
    IStringMapEdit<E> map = new StringMap<>();
    readStringMap( aSr, aKeyword, aKeeper, map );
    return map;
  }

  /**
   * Writes the map "integer" - "entity" to the output stream..
   * <p>
   * Output form is <code>KEYWORD = { key1=ENTITY, key=ENTITY, ... keyM=ENTITY }</code>, where keyN - is a key in a map,
   * ENTITY - an entity written using thw <code>aKeeper</code>. If <code>aKeyword</code> is an empty string, then part
   * "KEYWORD = " is not written.
   *
   * @param <E> - type of the entities in the map
   * @param aSw {@link IStrioWriter} - output stream
   * @param aKeyword String - keyword (an IDpath or an empty string)
   * @param aMap {@link IIntMap} - the map "integer" - "entity" to write
   * @param aKeeper {@link IEntityKeeper} - entities keeper
   * @param aIndent boolean - the indentation flag<br>
   *          <b>true</b> - each pair keyN=ENTITY will be written indented on a new line;<br>
   *          <b>false</b> - whole map will be written in on line.
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException aKeyword is not an IDpath and not an empty string
   * @throws TsIoRtException stream I/O error
   */
  public static <E> void writeIntMap( IStrioWriter aSw, String aKeyword, IIntMap<E> aMap, IEntityKeeper<E> aKeeper,
      boolean aIndent ) {
    TsNullArgumentRtException.checkNulls( aSw, aMap, aKeeper );
    // "KEYWORD = "
    writeKeywordHeader( aSw, aKeyword );
    // an empty map
    if( aMap.isEmpty() ) {
      aSw.writeChars( CHAR_SET_BEGIN, CHAR_SET_END );
      return;
    }
    aSw.writeChar( CHAR_SET_BEGIN );
    if( aIndent ) {
      aSw.incNewLine();
    }
    for( int i = 0, n = aMap.size(); i < n; i++ ) {
      int key = aMap.keys().getValue( i );
      E e = aMap.getByKey( key );
      if( !aIndent ) {
        aSw.writeSpace();
      }
      aSw.writeInt( key );
      aSw.writeChar( CHAR_EQUAL );
      aKeeper.write( aSw, e );
      if( i != n - 1 ) {
        aSw.writeChar( CHAR_ITEM_SEPARATOR );
        if( aIndent ) {
          aSw.writeEol();
        }
      }
    }
    if( aIndent ) {
      aSw.decNewLine();
    }
    else {
      aSw.writeSpace();
    }
    aSw.writeChar( CHAR_SET_END );
  }

  /**
   * Reads the map "integer" - "entity" previously written by
   * {@link #writeIntMap(IStrioWriter, String, IIntMap, IEntityKeeper, boolean)}.
   * <p>
   * Returned value may safely cast to {@link IIntMapEdit}.
   *
   * @param <E> - type of the entities in the map
   * @param aSr {@link IStrioReader} - input stream
   * @param aKeyword String - keyword (an IDpath or an empty string)
   * @param aKeeper {@link IEntityKeeper} - entities keeper
   * @return {@link IIntMap} - read map
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException aKeyword is not an IDpath and not an empty string
   * @throws StrioRtException syntax violation in input stream
   * @throws TsIoRtException stream I/O error
   */
  public static <E> IIntMap<E> readIntMap( IStrioReader aSr, String aKeyword, IEntityKeeper<E> aKeeper ) {
    TsNullArgumentRtException.checkNulls( aSr, aKeeper );
    ensureKeywordHeader( aSr, aKeyword );
    IIntMapEdit<E> map = new IntMap<>();
    if( aSr.readSetBegin() ) {
      do {
        int key = aSr.readInt();
        aSr.ensureChar( CHAR_EQUAL );
        E e = aKeeper.read( aSr );
        map.put( key, e );
      } while( aSr.readSetNext() );
    }
    return map;
  }

  /**
   * Writes stridables list to the STRIO output stream.
   *
   * @param <E> - type of items to write
   * @param aSw {@link IStrioWriter} - the output stream
   * @param aList {@link IStridablesList}&lt;E&gt; - collection to write
   * @param aKeeper {@link IEntityKeeper}&lt;E&gt; - items keeper
   * @param aIndent boolean - flags that each items will be written on new line
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static <E extends IStridable> void writeStridablesList( IStrioWriter aSw, IStridablesList<E> aList,
      IEntityKeeper<E> aKeeper, boolean aIndent ) {
    TsNullArgumentRtException.checkNulls( aSw, aKeeper );
    aKeeper.writeColl( aSw, aList, aIndent );
  }

  /**
   * Reads stridables list from STRIO input stream.
   *
   * @param <E> - type of items to read
   * @param aSr {@link IStrioReader} - input stream
   * @param aKeeper {@link IEntityKeeper}&lt;E&gt; - items keeper
   * @return {@link IStridablesListEdit}&lt;E&gt; - read list
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static <E extends IStridable> IStridablesListEdit<E> readStridablesList( IStrioReader aSr,
      IEntityKeeper<E> aKeeper ) {
    TsNullArgumentRtException.checkNulls( aSr, aKeeper );
    IStridablesListEdit<E> ll = new StridablesList<>();
    if( aSr.readArrayBegin() ) {
      do {
        E item = aKeeper.read( aSr );
        ll.add( item );
      } while( aSr.readArrayNext() );
    }
    return ll;
  }

  /**
   * Reads stridables list.
   *
   * @param <E> - type of items to read
   * @param aSr {@link IStrioReader} - input stream
   * @param aList {@link IStridablesListEdit}&lt;E&gt; - collection to red items into
   * @param aKeeper {@link IEntityKeeper}&lt;E&gt; - items keeper
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static <E extends IStridable> void readStridablesList( IStrioReader aSr, IStridablesListEdit<E> aList,
      IEntityKeeper<E> aKeeper ) {
    TsNullArgumentRtException.checkNulls( aSr, aList, aKeeper );
    if( aSr.readArrayBegin() ) {
      do {
        E item = aKeeper.read( aSr );
        aList.add( item );
      } while( aSr.readArrayNext() );
    }
  }

  // ------------------------------------------------------------------------------------
  // Misc
  //

  /**
   * Reads input starting from opening bracket to the closing bracket.
   * <p>
   * Opening and closing brackets will be included in the resulting string.
   * <p>
   * First read character must be {@link IStrioHardConstants#CHAR_SET_BEGIN} or
   * {@link IStrioHardConstants#CHAR_ARRAY_BEGIN}.
   *
   * @param aSr {@link IStrioReader} - input reader
   * @return String - read content (without comments)
   * @throws TsNullArgumentRtException argument = null
   */
  public static String readInterbaceContent( IStrioReader aSr ) {
    TsNullArgumentRtException.checkNull( aSr );
    char ch = aSr.peekChar( EStrioSkipMode.SKIP_COMMENTS );
    char chStart = ch;
    char chEnd = switch( ch ) {
      case CHAR_ARRAY_BEGIN -> CHAR_ARRAY_END;
      case CHAR_SET_BEGIN -> CHAR_SET_END;
      default -> throw new StrioRtException( FMT_ERR_LEFT_BRACKET_EXPECTED, Character.valueOf( ch ) );
    };
    aSr.nextChar(); // skipping left (first) bracket already read
    StringBuilder sb = new StringBuilder();
    sb.append( ch );
    int bracketsLevel = 1;
    do {
      ch = aSr.nextChar( EStrioSkipMode.SKIP_NONE );
      sb.append( ch );
      // check for interbrace content end
      if( ch == chEnd ) {
        --bracketsLevel;
        if( bracketsLevel == 0 ) {
          return sb.toString();
        }
        continue;
      }
      // starting bracket again?
      if( ch == chStart ) {
        ++bracketsLevel;
        continue;
      }
      // bypass comment
      if( ch == CHAR_LINE_COMMENT_SHELL ) {
        while( ch != CHAR_EOL ) {
          ch = aSr.nextChar( EStrioSkipMode.SKIP_NONE );
          StrioRtException.checkTrue( ch == CHAR_EOF, MSG_ERR_UNEXPECTED_EOF );
          sb.append( ch );
        }
      }
      // reading quoted string "as is", with escaped characters
      if( ch == CHAR_QUOTE ) {
        boolean prevWasEscape = false; // flag^ previous char was escape char
        while( true ) {
          ch = aSr.nextChar( EStrioSkipMode.SKIP_NONE );
          StrioRtException.checkTrue( ch == CHAR_EOF, MSG_ERR_UNEXPECTED_EOF );
          sb.append( ch );
          // bypass comment

          // GOGA 20203-01-07 --- do NOT process in the quoted string
          // if( ch == CHAR_LINE_COMMENT_SHELL ) {
          // while( ch != CHAR_EOL ) {
          // ch = aSr.nextChar( EStrioSkipMode.SKIP_NONE );
          // StrioRtException.checkTrue( ch == CHAR_EOF, MSG_ERR_UNEXPECTED_EOF );
          // sb.append( ch );
          // }
          // continue;
          // }
          // ---

          // process quote char (either escaped one or end of quoted string)
          if( ch == CHAR_QUOTE ) {
            if( !prevWasEscape ) {
              break;
            }
          }
          if( ch == CHAR_ESCAPE ) {
            prevWasEscape = !prevWasEscape;
          }
          else {
            prevWasEscape = false;
          }
        }
      }
      // other chars will be simply copied to resulting string
    } while( ch != CHAR_EOF );
    throw new StrioRtException( MSG_ERR_UNEXPECTED_EOF );
  }

  /**
   * Checks argument is valid KTOR interbrace content.
   *
   * @param aInterbraceContent String - interbrace content
   * @return {@link ValidationResult} - the check result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ValidationResult validateInterbraceContent( String aInterbraceContent ) {
    IStrioReader sr = new StrioReader( new CharInputStreamString( aInterbraceContent ) );
    char ch = sr.peekChar( EStrioSkipMode.SKIP_COMMENTS );
    char chStart = ch;
    char chEnd;
    switch( ch ) {
      case CHAR_ARRAY_BEGIN:
        chEnd = CHAR_ARRAY_END;
        break;
      case CHAR_SET_BEGIN:
        chEnd = CHAR_SET_END;
        break;
      default:
        return ValidationResult.error( FMT_ERR_LEFT_BRACKET_EXPECTED, Character.valueOf( ch ) );
    }
    sr.nextChar(); // skipping left (first) bracket already read
    int bracketsLevel = 1;
    do {
      ch = sr.nextChar( EStrioSkipMode.SKIP_NONE );
      // check for interbrace content end
      if( ch == chEnd ) {
        --bracketsLevel;
        if( bracketsLevel == 0 ) {
          return ValidationResult.SUCCESS;
        }
        continue;
      }
      // starting bracket again?
      if( ch == chStart ) {
        ++bracketsLevel;
        continue;
      }
      // bypass comment
      if( ch == CHAR_LINE_COMMENT_SHELL ) {
        while( ch != CHAR_EOL ) {
          ch = sr.nextChar( EStrioSkipMode.SKIP_NONE );
          if( ch == CHAR_EOF ) {
            return ValidationResult.error( MSG_ERR_UNEXPECTED_EOF );
          }
        }
      }
      // reading quoted string "as is", with escaped characters
      if( ch == CHAR_QUOTE ) {
        boolean prevWasEscape = false; // flag^ previous char was escape char
        while( true ) {
          ch = sr.nextChar( EStrioSkipMode.SKIP_NONE );
          if( ch == CHAR_EOF ) {
            return ValidationResult.error( MSG_ERR_UNEXPECTED_EOF );
          }
          // bypass comment
          if( ch == CHAR_LINE_COMMENT_SHELL ) {
            while( ch != CHAR_EOL ) {
              ch = sr.nextChar( EStrioSkipMode.SKIP_NONE );
              if( ch == CHAR_EOF ) {
                return ValidationResult.error( MSG_ERR_UNEXPECTED_EOF );
              }
            }
            continue;
          }
          // process quote char (either escaped one or end of quoted string)
          if( ch == CHAR_QUOTE ) {
            if( !prevWasEscape ) {
              break;
            }
          }
          if( ch == CHAR_ESCAPE ) {
            prevWasEscape = !prevWasEscape;
          }
          else {
            prevWasEscape = false;
          }
        }
      }
      // other chars will be simply copied to resulting string
    } while( ch != CHAR_EOF );
    return ValidationResult.error( MSG_ERR_UNEXPECTED_EOF );
  }

  /**
   * Checks argument is valid KTOR interbrace content and throws an exception if not.
   *
   * @param aInterbraceContent String - interbrace content
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException failed {@link #validateInterbraceContent(String)}
   */
  public static void checkValidInterbraceContent( String aInterbraceContent ) {
    TsValidationFailedRtException.checkWarn( validateInterbraceContent( aInterbraceContent ) );
  }

  /**
   * Reads the section until the end of the input stream.
   * <p>
   * This method simply calls {@link #readSections(IStrioReader, char) readSections(sr, CHAR_EOF)}.
   * <p>
   * TODO describe what is the section
   *
   * @param aSr {@link IStrioReader} - input stream reader
   * @return {@link IStringMap}&lt;String&gt; - the map "section id" - "section content as-is"
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws StrioRtException input stream format violation
   */
  public static IStringMap<String> readSections( IStrioReader aSr ) {
    return readSections( aSr, CHAR_EOF );
  }

  /**
   * Reads the section until the specified character of the input stream.
   * <p>
   * Input stream for the specified char is checked only after any section was read.
   *
   * @param aSr {@link IStrioReader} - input stream reader
   * @param aTerminalChar char - character to stop sections reading after any section end
   * @return {@link IStringMap}&lt;String&gt; - the map "section id" - "section content as-is"
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIoRtException I/O exception
   * @throws StrioRtException input stream format violation
   */
  public static IStringMap<String> readSections( IStrioReader aSr, char aTerminalChar ) {
    TsNullArgumentRtException.checkNull( aSr );
    IStringMapEdit<String> sects = new StringMap<>();
    while( aSr.peekChar( EStrioSkipMode.SKIP_COMMENTS ) != aTerminalChar ) {
      String sectId = aSr.readIdPath();
      aSr.ensureChar( CHAR_EQUAL );
      String content = readInterbaceContent( aSr );
      if( sects.hasKey( sectId ) ) {
        throw new StrioRtException( FMT_ERR_DUPLICATE_SECTION, sectId );
      }
      sects.put( sectId, content );
    }
    return sects;
  }

  /**
   * Writes sections to the output stream.
   *
   * @param aSw {@link IStrioWriter} - output stream writer
   * @param aSects {@link IStringMap}&lt;String&gt; - the map "section id" - "section content as-is"
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIoRtException I/O exception
   */
  public static void writeSections( IStrioWriter aSw, IStringMap<String> aSects ) {
    TsNullArgumentRtException.checkNulls( aSw, aSects );
    for( String sectId : aSects.keys() ) {
      String content = aSects.getByKey( sectId ).trim();
      int lastChar = content.charAt( content.length() - 1 );
      switch( content.charAt( 0 ) ) {
        case CHAR_SET_BEGIN:
          if( lastChar != CHAR_SET_END ) {
            throw new TsIllegalArgumentRtException( FMT_ERR_INV_SECT_CONTENT1, sectId );
          }
          break;
        case CHAR_ARRAY_BEGIN:
          if( lastChar != CHAR_ARRAY_END ) {
            throw new TsIllegalArgumentRtException( FMT_ERR_INV_SECT_CONTENT2, sectId );
          }
          break;
        default:
          throw new TsIllegalArgumentRtException( MSG_ERR_INV_SECT_CONTENT3 );
      }
    }
    for( String sectId : aSects.keys() ) {
      aSw.writeAsIs( sectId );
      aSw.writeSpace();
      aSw.writeChar( CHAR_EQUAL );
      aSw.writeSpace();
      aSw.writeAsIs( aSects.getByKey( sectId ) );
      aSw.writeEol();
    }
  }

  // ------------------------------------------------------------------------------------
  // Read/write
  //

  /**
   * Writes keepable entity content to the file.
   *
   * @param <T> - entity class
   * @param aFile {@link File} - the file
   * @param aEntity &lt;T&gt; - the keepable entity
   * @return &lt;T&gt; - the argument entity
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException error writing to file
   */
  public static <T extends IKeepableEntity> T writeKeepableEntity( File aFile, T aEntity ) {
    TsNullArgumentRtException.checkNulls( aEntity );
    try( ICharOutputStreamCloseable chOut = new CharOutputStreamFile( aFile ) ) {
      IStrioWriter sw = new StrioWriter( chOut );
      aEntity.write( sw );
    }
    return aEntity;
  }

  /**
   * Reads keepable entity content from the file.
   *
   * @param <T> - entity class
   * @param aFile {@link File} - the file
   * @param aEntity &lt;T&gt; - the keepable entity
   * @return &lt;T&gt; - the argument entity
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException error reading from file
   */
  public static <T extends IKeepableEntity> T readKeepableEntity( File aFile, T aEntity ) {
    TsNullArgumentRtException.checkNulls( aEntity );
    try( ICharInputStreamCloseable chIn = new CharInputStreamFile( aFile ) ) {
      IStrioReader sr = new StrioReader( chIn );
      aEntity.read( sr );
    }
    return aEntity;
  }

  /**
   * Prohibition of descendants creation.
   */
  private StrioUtils() {
    // nop
  }

}

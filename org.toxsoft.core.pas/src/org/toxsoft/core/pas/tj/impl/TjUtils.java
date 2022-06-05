package org.toxsoft.core.pas.tj.impl;

import java.io.Reader;
import java.io.Writer;

import org.toxsoft.core.pas.tj.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.chario.ICharInputStream;
import org.toxsoft.core.tslib.bricks.strio.chario.ICharOutputStream;
import org.toxsoft.core.tslib.bricks.strio.chario.impl.*;
import org.toxsoft.core.tslib.bricks.strio.impl.StrioReader;
import org.toxsoft.core.tslib.bricks.strio.impl.StrioWriter;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Точка входа в подистсему работы со структурами TsJson данных.
 *
 * @author hazard157
 */
public class TjUtils {

  /**
   * Экземпляр-синглтон JSON-консатнты <code>null</code>.
   */
  public static final ITjValue NULL = TjValueNull.INSTANCE;

  /**
   * Экземпляр-синглтон JSON-консатнты <code>true</code>.
   */
  public static final ITjValue TRUE = TjValueTrue.INSTANCE;

  /**
   * Экземпляр-синглтон JSON-консатнты <code>false</code>.
   */
  public static final ITjValue FALSE = TjValueFalse.INSTANCE;

  // ------------------------------------------------------------------------------------
  // Чтение/запись в JSON представление
  //

  /**
   * Преобразует объект в текстовую строку.
   *
   * @param aObject {@link ITjObject} - сохраняемый объект
   * @return String - JSON текстовое представление
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public static String obj2str( ITjObject aObject ) {
    StringBuilder sb = new StringBuilder();
    saveObject( sb, aObject );
    return sb.toString();
  }

  /**
   * Создает объект из JSON текстового представления.
   *
   * @param aJsonString String - JSON текстовое представление
   * @return {@link ITjObject} - загруженный объект
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws StrioRtException неверный входной формат
   */
  public static ITjObject str2obj( String aJsonString ) {
    ICharInputStream chIn = new CharInputStreamString( aJsonString );
    IStrioReader sr = new StrioReader( chIn );
    return loadObject( sr );
  }

  // 2021-06-10 mvk
  /**
   * Загружает объект из JSON из потока.
   *
   * @param aReader {@link Reader} - читатель потока
   * @return {@link ITjObject} - загруженный объект
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws StrioRtException неверный входной формат
   */
  @SuppressWarnings( "resource" )
  public static ITjObject loadObject( Reader aReader ) {
    TsNullArgumentRtException.checkNull( aReader );
    ICharInputStream chIn = new CharInputStreamReader( aReader );
    IStrioReader sr = new StrioReader( chIn );
    return loadObject( sr );
  }

  // 2021-06-10 mvk
  /**
   * Сохраняет объект в поток.
   * <p>
   * Этим методом удобно сохранять например в {@link StringBuilder}.
   *
   * @param aWriter {@link Writer} - пистель в поток
   * @param aObject {@link ITjObject} - сохраняемый объект
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public static void saveObject( Writer aWriter, ITjObject aObject ) {
    TsNullArgumentRtException.checkNulls( aWriter, aObject );
    CharOutputStreamWriter chOut = new CharOutputStreamWriter( aWriter );
    IStrioWriter sw = new StrioWriter( chOut );
    saveObject( sw, aObject );
  }

  /**
   * Сохраняет объект в текстовый поток.
   * <p>
   * Этим методом удобно сохранять например в {@link StringBuilder}.
   *
   * @param aOut {@link Appendable} - пистель в текстовы поток
   * @param aObject {@link ITjObject} - сохраняемый объект
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public static void saveObject( Appendable aOut, ITjObject aObject ) {
    ICharOutputStream chOut = new CharOutputStreamAppendable( aOut );
    IStrioWriter sw = new StrioWriter( chOut );
    saveObject( sw, aObject );
  }

  /**
   * Сохраняет объект в текстовый поток.
   *
   * @param aSw {@link IStrioWriter} - пистель в текстовы поток
   * @param aObject {@link ITjObject} - сохраняемый объект
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public static void saveObject( IStrioWriter aSw, ITjObject aObject ) {
    if( aSw == null || aObject == null ) {
      throw new TsNullArgumentRtException();
    }
    TsJsonObjectStorage.STORAGE.save( aSw, aObject );
  }

  /**
   * Загружает объект из JSON текстового представления.
   *
   * @param aSr {@link IStrioReader} - читатель текстового представления
   * @return {@link ITjObject} - загруженный объект
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws StrioRtException неверный входной формат
   */
  public static ITjObject loadObject( IStrioReader aSr ) {
    TsNullArgumentRtException.checkNull( aSr );
    return TsJsonObjectStorage.STORAGE.load( aSr );
  }

  // ------------------------------------------------------------------------------------
  // Статические конструкторы
  //

  /**
   * Создает TsJson объект.
   *
   * @return {@link ITjObject} - созданный объект
   */
  public static ITjObject createTjObject() {
    return new TjObject();
  }

  /**
   * Создает значение-объект вида {@link ETjKind#OBJECT}.
   *
   * @param aObject {@link ITjObject} - объект
   * @return {@link ITjValue} - созданное значение
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public static ITjValue createObject( ITjObject aObject ) {
    if( aObject == null ) {
      throw new TsNullArgumentRtException();
    }
    return new TjValueObject( aObject );
  }

  /**
   * Создает пустое значение-объект вида {@link ETjKind#OBJECT}.
   *
   * @return {@link ITjValue} - созданное значение
   */
  public static ITjValue createObject() {
    return new TjValueObject( createTjObject() );
  }

  /**
   * Создает строковое значение вида {@link ETjKind#STRING}.
   *
   * @param aString String - строковое значение
   * @return {@link ITjValue} - созданное значение
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public static ITjValue createString( String aString ) {
    if( aString == null ) {
      throw new TsNullArgumentRtException();
    }
    return new TjValueString( aString );
  }

  /**
   * Создает числовое значение вида {@link ETjKind#NUMBER}.
   *
   * @param aNumber {@link Number} - числовое значение
   * @return {@link ITjValue} - созданное значение
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public static ITjValue createNumber( Number aNumber ) {
    if( aNumber == null ) {
      throw new TsNullArgumentRtException();
    }
    return new TjValueNumber( aNumber );
  }

  /**
   * Создает целочисленное значение вида {@link ETjKind#NUMBER}.
   *
   * @param aNumber int - целочисленное значение
   * @return {@link ITjValue} - созданное значение
   */
  public static ITjValue createNumber( int aNumber ) {
    return new TjValueNumber( Integer.valueOf( aNumber ) );
  }

  /**
   * Создает целочисленное значение вида {@link ETjKind#NUMBER}.
   *
   * @param aNumber long - целочисленное значение
   * @return {@link ITjValue} - созданное значение
   */
  public static ITjValue createNumber( long aNumber ) {
    return new TjValueNumber( Long.valueOf( aNumber ) );
  }

  /**
   * Создает вещественное значение вида {@link ETjKind#NUMBER}.
   *
   * @param aNumber double - вещественное значение
   * @return {@link ITjValue} - созданное значение
   */
  public static ITjValue createNumber( double aNumber ) {
    return new TjValueNumber( Double.valueOf( aNumber ) );
  }

  /**
   * Создает значение-массив вида {@link ETjKind#ARRAY}.
   *
   * @param aArray {@link IList}&lt;{@link ITjValue}&gt; - элементы массива
   * @return {@link ITjValue} - созданное значение
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public static ITjValue createArray( IList<ITjValue> aArray ) {
    return new TjValueArray( aArray );
  }

  /**
   * Создает пустое значение-массив вида {@link ETjKind#ARRAY}.
   *
   * @return {@link ITjValue} - созданное значение
   */
  public static ITjValue createArray() {
    return new TjValueArray( IList.EMPTY );
  }

  /**
   * Запрет на создание экземпляров.
   */
  private TjUtils() {
    // nop
  }

}

package org.toxsoft.core.tslib.utils.files;

import static org.toxsoft.core.tslib.utils.files.ITsResources.*;

import java.io.File;

import org.toxsoft.core.tslib.bricks.strid.IStridable;
import org.toxsoft.core.tslib.utils.errors.TsItemNotFoundRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

// TODO TRANSLATE

/**
 * File system object kind.
 * <p>
 * Considers only regular files and directories. Other
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
public enum EFsObjKind
    implements IStridable {

  /**
   * Файл(ы).
   */
  FILE( "File", STR_FOK_FILE_D, STR_FOK_FILE, true, false ),

  /**
   * Папка(и).
   */
  DIR( "Dir", STR_FOK_DIR_D, STR_FOK_DIR, false, true ),

  /**
   * Всё (файлы и папки).
   */
  BOTH( "Both", STR_FOK_BOTH_D, STR_FOK_BOTH, true, true ),

  ;

  private final String  id;
  private final String  description;
  private final String  name;
  private final boolean file;
  private final boolean dir;

  /**
   * Создать константу с заданием всех инвариантов.
   *
   * @param aId String - идентифицирующее название константы
   * @param aDescr String - отображаемое описание константы
   * @param aName String - краткое название константы
   * @param aIsFile boolean - признак файла
   * @param aIsDir boolean - признак папки
   */
  EFsObjKind( String aId, String aDescr, String aName, boolean aIsFile, boolean aIsDir ) {
    id = aId;
    description = aDescr;
    name = aName;
    file = aIsFile;
    dir = aIsDir;
  }

  // --------------------------------------------------------------------------
  // IStridable
  //

  @Override
  public String id() {
    return id;
  }

  @Override
  public String description() {
    return description;
  }

  @Override
  public String nmName() {
    return name;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает признак файла.
   *
   * @return boolean - признак файла
   */
  public boolean isFile() {
    return file;
  }

  /**
   * Возвращает признак папки.
   *
   * @return boolean - признак папки
   */
  public boolean isDir() {
    return dir;
  }

  /**
   * Проверяет, соответствует ли файловый объект этому типу.
   *
   * @param aFile {@link File} - файловый тип
   * @return boolean - признак соответствия
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   */
  public boolean isAccepted( File aFile ) {
    TsNullArgumentRtException.checkNull( aFile );
    if( aFile.isFile() && isFile() ) {
      return true;
    }
    if( aFile.isDirectory() && isDir() ) {
      return true;
    }
    return false;
  }

  // ----------------------------------------------------------------------------------
  // Методы проверки
  //

  /**
   * Определяет, существует ли константа перечисления с заданным идентификатором.
   *
   * @param aId String - идентификатор искомой константы
   * @return boolean - признак существования константы <br>
   *         <b>true</b> - константа с заданным идентификатором существует;<br>
   *         <b>false</b> - неет константы с таким идентификатором.
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static boolean isItemById( String aId ) {
    return findByIdOrNull( aId ) != null;
  }

  /**
   * Определяет, существует ли константа перечисления с заданным описанием.
   *
   * @param aDescription String - описание искомой константы
   * @return boolean - признак существования константы <br>
   *         <b>true</b> - константа с заданным описанием существует;<br>
   *         <b>false</b> - неет константы с таким описанием.
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static boolean isItemByDescription( String aDescription ) {
    return findByDescriptionOrNull( aDescription ) != null;
  }

  /**
   * Определяет, существует ли константа перечисления с заданным именем.
   *
   * @param aName String - имя (название) искомой константы
   * @return boolean - признак существования константы <br>
   *         <b>true</b> - константа с заданным именем существует;<br>
   *         <b>false</b> - неет константы с таким именем.
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static boolean isItemByName( String aName ) {
    return findByNameOrNull( aName ) != null;
  }

  // ----------------------------------------------------------------------------------
  // Методы поиска
  //

  /**
   * Возвращает константу по идентификатору или null.
   *
   * @param aId String - идентификатор искомой константы
   * @return EFsObjKind - найденная константа, или null если нет константы с таимк идентификатором
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static EFsObjKind findByIdOrNull( String aId ) {
    TsNullArgumentRtException.checkNull( aId );
    for( EFsObjKind item : values() ) {
      if( item.id.equals( aId ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Возвращает константу по идентификатору или выбрасывает исключение.
   *
   * @param aId String - идентификатор искомой константы
   * @return EFsObjKind - найденная константа
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким идентификатором
   */
  public static EFsObjKind findById( String aId ) {
    return TsItemNotFoundRtException.checkNull( findByIdOrNull( aId ) );
  }

  /**
   * Возвращает константу по описанию или null.
   *
   * @param aDescription String - описание искомой константы
   * @return EFsObjKind - найденная константа, или null если нет константы с таким описанием
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static EFsObjKind findByDescriptionOrNull( String aDescription ) {
    TsNullArgumentRtException.checkNull( aDescription );
    for( EFsObjKind item : values() ) {
      if( item.description.equals( aDescription ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Возвращает константу по описанию или выбрасывает исключение.
   *
   * @param aDescription String - описание искомой константы
   * @return EFsObjKind - найденная константа
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким описанием
   */
  public static EFsObjKind findByDescription( String aDescription ) {
    return TsItemNotFoundRtException.checkNull( findByDescriptionOrNull( aDescription ) );
  }

  /**
   * Возвращает константу по имени или null.
   *
   * @param aName String - имя искомой константы
   * @return EFsObjKind - найденная константа, или null если нет константы с таким именем
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static EFsObjKind findByNameOrNull( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EFsObjKind item : values() ) {
      if( item.name.equals( aName ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Возвращает константу по имени или выбрасывает исключение.
   *
   * @param aName String - имя искомой константы
   * @return EFsObjKind - найденная константа
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким именем
   */
  public static EFsObjKind findByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByNameOrNull( aName ) );
  }

}

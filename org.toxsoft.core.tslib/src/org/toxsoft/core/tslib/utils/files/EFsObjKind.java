package org.toxsoft.core.tslib.utils.files;

import static org.toxsoft.core.tslib.utils.files.ITsResources.*;

import java.io.*;

import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

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
   * Regular files, when {@link File#isFile()} == <code>true</code>.
   */
  FILE( "File", STR_FOK_FILE_D, STR_FOK_FILE, true, false, TsFileFilter.FF_FILES_HIDDEN ),

  /**
   * Directories, when {@link File#isDirectory()} == <code>true</code>.
   */
  DIR( "Dir", STR_FOK_DIR_D, STR_FOK_DIR, false, true, TsFileFilter.FF_DIRS_HIDDEN ),

  /**
   * Regular files and directoires.
   */
  BOTH( "Both", STR_FOK_BOTH_D, STR_FOK_BOTH, true, true, TsFileFilter.FF_ALL_HIDDEN ),

  ;

  private final String       id;
  private final String       description;
  private final String       name;
  private final boolean      file;
  private final boolean      dir;
  private final TsFileFilter fileFilter;

  private static IStridablesListEdit<EFsObjKind> list = null;

  EFsObjKind( String aId, String aDescr, String aName, boolean aIsFile, boolean aIsDir, TsFileFilter aFileFilter ) {
    id = aId;
    description = aDescr;
    name = aName;
    file = aIsFile;
    dir = aIsDir;
    fileFilter = aFileFilter;
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
   * Returns if constant includes files.
   *
   * @return boolean - <code>true</code> regular files are included
   */
  public boolean isFile() {
    return file;
  }

  /**
   * Returns if constant includes directories.
   *
   * @return boolean - <code>true</code> directories are included
   */
  public boolean isDir() {
    return dir;
  }

  /**
   * Checks if existing file system object is accepted by this constant.
   * <p>
   * Returns <code>false</code> if argument does not exists or is not neither file
   * {@link File#isFile()}=<code>true</code> nor directory {@link File#isDirectory()} = <code>false</code>.
   *
   * @param aFile {@link File} - the file system object
   * @return boolean - <code>true</code> if object is accepted, <code>false</code> - rejected
   * @throws TsNullArgumentRtException any argument = <code>null</code>
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

  /**
   * Returns {@link TsFileFilter} which accepts objects according to {@link #isAccepted(File)}.
   *
   * @return {@link TsFileFilter} - the file filter
   */
  public TsFileFilter fileFilter() {
    return fileFilter;
  }

  // ----------------------------------------------------------------------------------
  // Stridable enum common API
  //

  /**
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link EFsObjKind} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<EFsObjKind> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link EFsObjKind} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static EFsObjKind getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link EFsObjKind} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static EFsObjKind findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EFsObjKind item : values() ) {
      if( item.nmName().equals( aName ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Returns the constant by the name.
   *
   * @param aName String - the name
   * @return {@link EFsObjKind} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static EFsObjKind getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

  // // ----------------------------------------------------------------------------------
  // // Методы проверки
  // //
  //
  // /**
  // * Определяет, существует ли константа перечисления с заданным идентификатором.
  // *
  // * @param aId String - идентификатор искомой константы
  // * @return boolean - признак существования константы <br>
  // * <b>true</b> - константа с заданным идентификатором существует;<br>
  // * <b>false</b> - неет константы с таким идентификатором.
  // * @throws TsNullArgumentRtException аргумент = null
  // */
  // public static boolean isItemById( String aId ) {
  // return findByIdOrNull( aId ) != null;
  // }
  //
  // /**
  // * Определяет, существует ли константа перечисления с заданным описанием.
  // *
  // * @param aDescription String - описание искомой константы
  // * @return boolean - признак существования константы <br>
  // * <b>true</b> - константа с заданным описанием существует;<br>
  // * <b>false</b> - неет константы с таким описанием.
  // * @throws TsNullArgumentRtException аргумент = null
  // */
  // public static boolean isItemByDescription( String aDescription ) {
  // return findByDescriptionOrNull( aDescription ) != null;
  // }
  //
  // /**
  // * Определяет, существует ли константа перечисления с заданным именем.
  // *
  // * @param aName String - имя (название) искомой константы
  // * @return boolean - признак существования константы <br>
  // * <b>true</b> - константа с заданным именем существует;<br>
  // * <b>false</b> - неет константы с таким именем.
  // * @throws TsNullArgumentRtException аргумент = null
  // */
  // public static boolean isItemByName( String aName ) {
  // return findByNameOrNull( aName ) != null;
  // }
  //
  // // ----------------------------------------------------------------------------------
  // // Методы поиска
  // //
  //
  // /**
  // * Возвращает константу по идентификатору или null.
  // *
  // * @param aId String - идентификатор искомой константы
  // * @return EFsObjKind - найденная константа, или null если нет константы с таимк идентификатором
  // * @throws TsNullArgumentRtException аргумент = null
  // */
  // public static EFsObjKind findByIdOrNull( String aId ) {
  // TsNullArgumentRtException.checkNull( aId );
  // for( EFsObjKind item : values() ) {
  // if( item.id.equals( aId ) ) {
  // return item;
  // }
  // }
  // return null;
  // }
  //
  // /**
  // * Возвращает константу по идентификатору или выбрасывает исключение.
  // *
  // * @param aId String - идентификатор искомой константы
  // * @return EFsObjKind - найденная константа
  // * @throws TsNullArgumentRtException аргумент = null
  // * @throws TsItemNotFoundRtException нет константы с таким идентификатором
  // */
  // public static EFsObjKind findById( String aId ) {
  // return TsItemNotFoundRtException.checkNull( findByIdOrNull( aId ) );
  // }
  //
  // /**
  // * Возвращает константу по описанию или null.
  // *
  // * @param aDescription String - описание искомой константы
  // * @return EFsObjKind - найденная константа, или null если нет константы с таким описанием
  // * @throws TsNullArgumentRtException аргумент = null
  // */
  // public static EFsObjKind findByDescriptionOrNull( String aDescription ) {
  // TsNullArgumentRtException.checkNull( aDescription );
  // for( EFsObjKind item : values() ) {
  // if( item.description.equals( aDescription ) ) {
  // return item;
  // }
  // }
  // return null;
  // }
  //
  // /**
  // * Возвращает константу по описанию или выбрасывает исключение.
  // *
  // * @param aDescription String - описание искомой константы
  // * @return EFsObjKind - найденная константа
  // * @throws TsNullArgumentRtException аргумент = null
  // * @throws TsItemNotFoundRtException нет константы с таким описанием
  // */
  // public static EFsObjKind findByDescription( String aDescription ) {
  // return TsItemNotFoundRtException.checkNull( findByDescriptionOrNull( aDescription ) );
  // }
  //
  // /**
  // * Возвращает константу по имени или null.
  // *
  // * @param aName String - имя искомой константы
  // * @return EFsObjKind - найденная константа, или null если нет константы с таким именем
  // * @throws TsNullArgumentRtException аргумент = null
  // */
  // public static EFsObjKind findByNameOrNull( String aName ) {
  // TsNullArgumentRtException.checkNull( aName );
  // for( EFsObjKind item : values() ) {
  // if( item.name.equals( aName ) ) {
  // return item;
  // }
  // }
  // return null;
  // }
  //
  // /**
  // * Возвращает константу по имени или выбрасывает исключение.
  // *
  // * @param aName String - имя искомой константы
  // * @return EFsObjKind - найденная константа
  // * @throws TsNullArgumentRtException аргумент = null
  // * @throws TsItemNotFoundRtException нет константы с таким именем
  // */
  // public static EFsObjKind findByName( String aName ) {
  // return TsItemNotFoundRtException.checkNull( findByNameOrNull( aName ) );
  // }

}

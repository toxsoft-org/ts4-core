package org.toxsoft.core.txtproj.lib.storage;

import java.io.File;

import org.toxsoft.core.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.core.tslib.bricks.strid.impl.StridUtils;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.basis.ITsCollection;
import org.toxsoft.core.tslib.coll.primtypes.IStringMap;
import org.toxsoft.core.tslib.coll.primtypes.IStringMapEdit;
import org.toxsoft.core.tslib.coll.primtypes.impl.StringMap;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.TsFileFilter;
import org.toxsoft.core.tslib.utils.files.TsFileUtils;

/**
 * Реализация хранилища в виде набора фалов в директории.
 * <p>
 * Каждый раздел хранится в файле с именем, совпадающим с идентификатором раздела и расширением
 * {@link #getSectionFileExtension()}.
 *
 * @author hazard157
 */
public class KeepablesStorageInDir
    implements IKeepablesStorage {

  /**
   * Расширение по умолчанию файлов разделов.
   * <p>
   * KSS = Keepable Storage Section.
   */
  public static final String DEFAULT_SECTION_FILE_EXTENSION = "kss"; //$NON-NLS-1$

  private final File         dir;
  private final String       fileExt;
  private final TsFileFilter fileFilter;

  /**
   * Создает хранилище.
   *
   * @param aDir {@link File} - директория хранения
   * @param aSectionFileExtension String - расширение файлов разделов (без точки)
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException расширение - пустая строка
   */
  public KeepablesStorageInDir( File aDir, String aSectionFileExtension ) {
    dir = TsNullArgumentRtException.checkNull( aDir );
    fileExt = TsErrorUtils.checkNonBlank( aSectionFileExtension );
    fileFilter = TsFileFilter.ofFileExt( fileExt );
  }

  // ------------------------------------------------------------------------------------
  // Внутренние методы
  //

  /**
   * Возвращает список файлов, потенциально содержащих разделы.
   * <p>
   * Список файлов формируется по расширению, и имени, которая доллжна быть ИД-путем.
   *
   * @return IStringMap&lt;File&gt; - карта "имя файла без расширения (ИД раздела)" - "файл"
   */
  private IStringMap<File> listProbableSectionFiles() {
    File[] ff = dir.listFiles( fileFilter );
    IStringMapEdit<File> result = new StringMap<>();
    for( File f : ff ) {
      String bareFileName = TsFileUtils.extractBareFileName( f.getName() );
      if( StridUtils.isValidIdPath( bareFileName ) ) {
        result.put( bareFileName, f );
      }
    }
    return result;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ITsClearableCollection
  //

  @Override
  public void clear() {
    for( File f : listProbableSectionFiles() ) {
      f.delete();
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IKeepablesStorage
  //

  @Override
  public boolean hasSection( String aId ) {
    return listProbableSectionFiles().hasKey( aId );
  }

  @Override
  public <T> T readItem( String aId, IEntityKeeper<T> aKeeper, T aDefault ) {
    File f = listProbableSectionFiles().findByKey( aId );
    if( f == null ) {
      return aDefault;
    }
    return aKeeper.read( f );
  }

  @Override
  public <T> void writeItem( String aId, T aItem, IEntityKeeper<T> aKeeper ) {
    TsNullArgumentRtException.checkNulls( aItem, aKeeper );
    StridUtils.checkValidIdPath( aId );
    File f = new File( dir, aId + File.separator + fileExt );
    aKeeper.write( f, aItem );
  }

  @Override
  public <T> IList<T> readColl( String aId, IEntityKeeper<T> aKeeper ) {
    File f = listProbableSectionFiles().findByKey( aId );
    if( f == null ) {
      return IList.EMPTY;
    }
    return aKeeper.readColl( f );
  }

  @Override
  public <T> void writeColl( String aId, ITsCollection<T> aColl, IEntityKeeper<T> aKeeper ) {
    TsNullArgumentRtException.checkNulls( aColl, aKeeper );
    StridUtils.checkValidIdPath( aId );
    File f = new File( dir, aId + File.separator + fileExt );
    aKeeper.writeColl( f, aColl, true );
  }

  @Override
  public void removeSection( String aId ) {
    File f = listProbableSectionFiles().findByKey( aId );
    if( f != null ) {
      f.delete();
    }
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает директорию хранения файлов разделов.
   *
   * @return {@link File} - директория хранения файлов
   */
  public File getDirectory() {
    return dir;
  }

  /**
   * Возвращает расширение файлов разделов.
   *
   * @return String - расширение файлов разделов (без точки)
   */
  public String getSectionFileExtension() {
    return fileExt;
  }

}

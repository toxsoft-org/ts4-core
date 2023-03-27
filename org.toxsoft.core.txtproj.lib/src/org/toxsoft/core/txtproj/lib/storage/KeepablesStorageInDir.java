package org.toxsoft.core.txtproj.lib.storage;

import java.io.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;

/**
 * {@link IKeepablesStorage} implemented as the files in the single directory.
 * <p>
 * Each section is stored in the file named as section ID and added extension {@link #getSectionFileExtension()}.
 *
 * @author hazard157
 */
public class KeepablesStorageInDir
    implements IKeepablesStorage {

  /**
   * Default section file name extension.
   * <p>
   * KSS = Keepable Storage Section.
   */
  public static final String DEFAULT_SECTION_FILE_EXTENSION = "kss"; //$NON-NLS-1$

  private final File         dir;
  private final String       fileExt;
  private final TsFileFilter fileFilter;

  /**
   * Constructor.
   *
   * @param aDir {@link File} - the storage directory
   * @param aSectionFileExtension String - section file names extension
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException extension is an empty string
   */
  public KeepablesStorageInDir( File aDir, String aSectionFileExtension ) {
    dir = TsNullArgumentRtException.checkNull( aDir );
    fileExt = TsErrorUtils.checkNonBlank( aSectionFileExtension );
    fileFilter = TsFileFilter.ofFileExt( fileExt );
  }

  /**
   * Constructor with default extension.
   *
   * @param aDir {@link File} - the storage directory
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public KeepablesStorageInDir( File aDir ) {
    this( aDir, DEFAULT_SECTION_FILE_EXTENSION );
  }

  // ------------------------------------------------------------------------------------
  // ITsClearableCollection
  //

  @Override
  public void clear() {
    for( File f : listProbableSectionFiles() ) {
      f.delete();
    }
  }

  // ------------------------------------------------------------------------------------
  // IKeepablesStorage
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
    File f = makeSectionFile( aId );
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
    File f = makeSectionFile( aId );
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
   * Returns the directory containing sections files.
   *
   * @return {@link File} - the storage directory
   */
  public File getDirectory() {
    return dir;
  }

  /**
   * Returns the section content file name extension.
   *
   * @return String - section file names extension
   */
  public String getSectionFileExtension() {
    return fileExt;
  }

  /**
   * Returns the names of the file probably containing the sections data.
   * <p>
   * File names are chosen by the specified extension and names as an IDpath.
   *
   * @return IStringMap&lt;File&gt; - the map "bare file name (= section ID)" - " the section file"
   */
  public IStringMap<File> listProbableSectionFiles() {
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

  /**
   * Creates the file name for the specified section.
   *
   * @param aSectionId String - the section ID
   * @return {@link File} - name of the file
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument is not an IDpath
   */
  public File makeSectionFile( String aSectionId ) {
    StridUtils.checkValidIdPath( aSectionId );
    return new File( dir, aSectionId + '.' + fileExt );
  }

}

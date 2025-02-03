package org.toxsoft.core.txtproj.lib.storage;

import java.io.*;
import java.util.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.chario.*;
import org.toxsoft.core.tslib.bricks.strio.chario.impl.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;
import org.toxsoft.core.txtproj.lib.tdfile.*;

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
  // Iterable
  //

  @Override
  public Iterator<TdfSection> iterator() {
    return new Iterator<>() {

      Iterator<File> fileIterator = listProbableSectionFiles().iterator();

      @Override
      public TdfSection next() {
        File f = fileIterator.next();
        try( ICharInputStreamCloseable chIn = new CharInputStreamFile( f ) ) {
          IStrioReader sr = new StrioReader( chIn );
          String content = sr.readUntilChar( IStrioHardConstants.CHAR_EOF );
          return new TdfSection( content, content );
        }
      }

      @Override
      public boolean hasNext() {
        return fileIterator.hasNext();
      }
    };
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
    try( ICharInputStreamCloseable chIn = new CharInputStreamFile( f ) ) {
      IStrioReader sr = new StrioReader( chIn );
      return aKeeper.readEnclosed( sr );
    }
  }

  @Override
  public <T> void writeItem( String aId, T aItem, IEntityKeeper<T> aKeeper ) {
    TsNullArgumentRtException.checkNulls( aItem, aKeeper );
    File f = makeSectionFile( aId );
    try( ICharOutputStreamCloseable chOut = new CharOutputStreamFile( f ) ) {
      IStrioWriter sw = new StrioWriter( chOut );
      aKeeper.writeEnclosed( sw, aItem );
    }
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
  public <T> void writeColl( String aId, ITsCollection<T> aColl, IEntityKeeper<T> aKeeper, boolean aIndented ) {
    TsNullArgumentRtException.checkNulls( aColl, aKeeper );
    File f = makeSectionFile( aId );
    aKeeper.writeColl( f, aColl, aIndented );
  }

  @Override
  public <T> IStringMap<T> readStridMap( String aId, IEntityKeeper<T> aKeeper ) {
    File f = listProbableSectionFiles().findByKey( aId );
    if( f == null ) {
      return IStringMap.EMPTY;
    }
    return aKeeper.readStridMap( f );
  }

  @Override
  public <T> IStringMap<T> readStringMap( String aId, IEntityKeeper<T> aKeeper ) {
    File f = listProbableSectionFiles().findByKey( aId );
    if( f == null ) {
      return IStringMap.EMPTY;
    }
    return aKeeper.readStringMap( f );
  }

  @Override
  public <T> void writeStridMap( String aId, IStringMap<T> aMap, IEntityKeeper<T> aKeeper, boolean aIndented ) {
    TsNullArgumentRtException.checkNulls( aMap, aKeeper );
    File f = makeSectionFile( aId );
    aKeeper.writeStridMap( f, aMap, aIndented );
  }

  @Override
  public <T> void writeStringMap( String aId, IStringMap<T> aMap, IEntityKeeper<T> aKeeper, boolean aIndented ) {
    TsNullArgumentRtException.checkNulls( aMap, aKeeper );
    File f = makeSectionFile( aId );
    aKeeper.writeStringMap( f, aMap, aIndented );
  }

  @Override
  public void writeSection( TdfSection aSection ) {
    TsNullArgumentRtException.checkNull( aSection );
    File f = makeSectionFile( aSection.keyword() );
    try( ICharOutputStreamCloseable chOut = new CharOutputStreamFile( f ) ) {
      IStrioWriter sw = new StrioWriter( chOut );
      sw.writeAsIs( aSection.getContent() );
    }
  }

  @Override
  public void removeSection( String aId ) {
    File f = listProbableSectionFiles().findByKey( aId );
    if( f != null ) {
      f.delete();
    }
  }

  @Override
  public void copyFrom( IKeepablesStorageRo aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    for( TdfSection s : aSource ) {
      writeSection( s );
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

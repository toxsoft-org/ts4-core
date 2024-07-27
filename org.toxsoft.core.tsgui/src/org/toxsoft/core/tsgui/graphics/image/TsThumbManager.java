package org.toxsoft.core.tsgui.graphics.image;

import static org.toxsoft.core.tsgui.graphics.image.TsImageManagementUtils.*;

import java.io.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.graphics.image.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * {@link ITsThumbManager} implementation.
 *
 * @author hazard157
 */
public class TsThumbManager
    implements ITsThumbManager {

  private static final int MIN_MAX_THUMBS_IN_MEMORY = 16;
  private static final int DEF_MAX_THUMBS_IN_MEMORY = 64 * 1024;
  private static final int MAX_MAX_THUMBS_IN_MEMORY = 64 * 1024;

  // caches
  /**
   * The map "thumb size" - "map of loaded images by image file".
   */
  private final IMapEdit<EThumbSize, IMapEdit<File, TsImage>> thumbsMapMap = new ElemMap<>();

  // caching parameters
  private int maxThumbsInMemory = DEF_MAX_THUMBS_IN_MEMORY;

  private File thumbsRoot = new File( DEFAULT_ROOT_PATH );
  // private final IEclipseContext appContext;
  private final Display display;

  /**
   * Constructor.
   *
   * @param aDisplay Display - the display to use as images creation device
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TsThumbManager( Display aDisplay ) {
    TsNullArgumentRtException.checkNull( aDisplay );
    display = aDisplay;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private File makeThumbFileName( File aImageFile, EThumbSize aThumbSize ) {
    String thumbFileName = formatImageThumbFileName( aImageFile, aThumbSize );
    String absDirString = aImageFile.getParentFile().getAbsolutePath();
    File absDirFile = new File( thumbsRoot, TsFileUtils.removeStartingSeparator( absDirString ) );
    return new File( absDirFile, thumbFileName );
  }

  private IMapEdit<File, TsImage> getThumbsMap( EThumbSize aThumbSize ) {
    IMapEdit<File, TsImage> map = thumbsMapMap.findByKey( aThumbSize );
    if( map == null ) {
      map = new ElemMap<>();
      thumbsMapMap.put( aThumbSize, map );
    }
    return map;
  }

  private void deleteThumbFiles( File aFileOrDir ) {
    String absString = aFileOrDir.getAbsolutePath();
    // TODO check if line below works for Windows file system with drive letters
    File absPath = new File( thumbsRoot, TsFileUtils.removeStartingSeparator( absString ) );
    if( !absPath.exists() ) {
      return;
    }
    // #absPath is a directory - remove everything
    if( absPath.isDirectory() ) {
      TsFileUtils.deleteDirectory( absPath, ILongOpProgressCallback.NONE );
      return;
    }
    // #absPath denotes to the file, remove it's thumb files of all sizes
    for( EThumbSize thumbSize : EThumbSize.values() ) {
      File f = makeThumbFileName( aFileOrDir, thumbSize );
      if( f.exists() ) {
        f.delete();
      }
    }
  }

  private void removeDirFromCache( File aImageFile ) {
    // TODO TsThumbManager.removeDirFromCache()
  }

  private void removeFileFromCache( File aImageFile ) {
    // TODO TsThumbManager.removeFileFromCache()
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  @Override
  public File getThumbsRoot() {
    return thumbsRoot;
  }

  @Override
  public int getMaxThumbsInMemory() {
    return maxThumbsInMemory;
  }

  @Override
  public void setup( File aThumbsRootDir, int aMaxThumbsInMemory ) {
    TsNullArgumentRtException.checkNull( aThumbsRootDir );
    thumbsRoot = aThumbsRootDir;
    // setup maxThumbsInMemory
    int mtm = aMaxThumbsInMemory;
    if( mtm < MIN_MAX_THUMBS_IN_MEMORY ) {
      mtm = MIN_MAX_THUMBS_IN_MEMORY;
    }
    if( mtm > MAX_MAX_THUMBS_IN_MEMORY ) {
      mtm = MAX_MAX_THUMBS_IN_MEMORY;
    }
    if( maxThumbsInMemory > mtm ) {
      for( EThumbSize ths : EThumbSize.values() ) {
        IMapEdit<File, TsImage> thumbsMap = getThumbsMap( ths );
        while( thumbsMap.size() > mtm ) {
          TsImage mi = thumbsMap.removeByKey( thumbsMap.keys().get( 0 ) );
          mi.dispose();
        }
      }
      System.gc();
    }
    maxThumbsInMemory = mtm;
  }

  @Override
  public TsImage findThumb( File aImageFile, EThumbSize aThumbSize ) {
    TsNullArgumentRtException.checkNulls( aImageFile, aThumbSize );
    IMapEdit<File, TsImage> thumbsMap = getThumbsMap( aThumbSize );
    TsImage mi = thumbsMap.findByKey( aImageFile );
    if( mi != null ) {
      return mi;
    }
    if( !TsFileUtils.isFileReadable( aImageFile ) ) {
      return null;
    }
    File thumbFile = makeThumbFileName( aImageFile, aThumbSize );
    if( !thumbFile.exists() || aImageFile.lastModified() >= thumbFile.lastModified() ) {
      try {
        createThumbFile( aImageFile, thumbFile, aThumbSize );
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex );
        return null;
      }
    }
    try {
      mi = TsImageUtils.loadTsImage( thumbFile, display );
      thumbsMap.put( aImageFile, mi );
      if( thumbsMap.size() > maxThumbsInMemory ) {
        thumbsMap.removeByKey( thumbsMap.keys().get( 0 ) ).dispose();
      }
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
    }
    return mi;
  }

  @Override
  public File getThumbFile( File aImageFile, EThumbSize aThumbSize ) {
    TsNullArgumentRtException.checkNulls( aImageFile, aThumbSize );
    return makeThumbFileName( aImageFile, aThumbSize );
  }

  @Override
  public boolean ensureThumb( File aImageFile, EThumbSize aThumbSize, boolean aForceCreate ) {
    TsNullArgumentRtException.checkNulls( aImageFile, aThumbSize );
    if( !TsFileUtils.isFileReadable( aImageFile ) ) {
      return false;
    }
    File thumbFile = makeThumbFileName( aImageFile, aThumbSize );
    // no need to (re)create thumbnail file
    if( thumbFile.exists() && !aForceCreate && thumbFile.lastModified() > aImageFile.lastModified() ) {
      return true;
    }
    try {
      createThumbFile( aImageFile, thumbFile, aThumbSize );
      return true;
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
      return false;
    }
  }

  @Override
  public boolean isThumbCached( File aImageFile, EThumbSize aThumbSize ) {
    TsNullArgumentRtException.checkNulls( aImageFile, aThumbSize );
    IMapEdit<File, TsImage> thumbsMap = getThumbsMap( aThumbSize );
    return thumbsMap.hasKey( aImageFile );
  }

  @Override
  public boolean isThumbFile( File aImageFile, EThumbSize aThumbSize ) {
    TsNullArgumentRtException.checkNulls( aImageFile, aThumbSize );
    if( !TsFileUtils.isFileReadable( aImageFile ) ) {
      return false;
    }
    File thumbFile = makeThumbFileName( aImageFile, aThumbSize );
    return thumbFile.exists();
  }

  @Override
  public void refreshCache( File aFileOrDir ) {
    TsNullArgumentRtException.checkNull( aFileOrDir );
    // delete all related thumb files
    deleteThumbFiles( aFileOrDir );
    // remove images from cache
    if( TsImageManagementUtils.isDir( aFileOrDir ) ) {
      removeDirFromCache( aFileOrDir );
    }
    else {
      removeFileFromCache( aFileOrDir );
    }
  }

  @Override
  public void clearCache() {
    for( EThumbSize ths : EThumbSize.values() ) {
      IMapEdit<File, TsImage> thumbsMap = getThumbsMap( ths );
      while( !thumbsMap.isEmpty() ) {
        TsImage mi = thumbsMap.removeByKey( thumbsMap.keys().get( 0 ) );
        mi.dispose();
      }
    }
    System.gc();
  }

}

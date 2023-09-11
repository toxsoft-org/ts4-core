package org.toxsoft.core.tsgui.graphics.image.impl;

import static org.toxsoft.core.tsgui.graphics.image.impl.TsImageManagerUtils.*;

import java.io.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * {@link ITsImageManager} implementation.
 *
 * @author hazard157
 */
public class TsImageManager
    implements ITsImageManager {

  private static final String DEFAULT_ROOT_PATH        = "/home/zcache"; //$NON-NLS-1$
  private static final int    MIN_MAX_IMAGES_IN_MEMORY = 8;
  private static final int    DEF_MAX_IMAGES_IN_MEMORY = 256;
  private static final int    MAX_MAX_IMAGES_IN_MEMORY = 512;
  private static final int    MIN_MAX_THUMBS_IN_MEMORY = 16;
  private static final int    DEF_MAX_THUMBS_IN_MEMORY = 64 * 1024;
  private static final int    MAX_MAX_THUMBS_IN_MEMORY = 64 * 1024;

  // caches
  private final IMapEdit<File, TsImage>                       imagesMap    = new ElemMap<>();
  private final IMapEdit<EThumbSize, IMapEdit<File, TsImage>> thumbsMapMap = new ElemMap<>();

  // caching parameters
  private int maxImagesInMemory = DEF_MAX_IMAGES_IN_MEMORY;
  private int maxThumbsInMemory = DEF_MAX_THUMBS_IN_MEMORY;

  private File                  thumbsRoot = new File( DEFAULT_ROOT_PATH );
  private final IEclipseContext appContext;
  private final Display         display;

  /**
   * Constructor.
   *
   * @param aAppContext {@link IEclipseContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TsImageManager( IEclipseContext aAppContext ) {
    TsNullArgumentRtException.checkNull( aAppContext );
    appContext = aAppContext;
    display = appContext.get( Display.class );
    TsInternalErrorRtException.checkNull( display );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private File makeThumbFileName( File aImageFile, EThumbSize aThumbSize ) {
    String thumbFileName = formatImageThumbFileName( aImageFile, aThumbSize );
    String absDirString = aImageFile.getParentFile().getAbsolutePath();
    File absDirFile = new File( thumbsRoot, TsFileUtils.removeStartingSeparator( absDirString ) );
    // absDirFile.mkdirs();
    return new File( absDirFile, thumbFileName );
  }

  private void deleteThumbFiles( File aFileOrDir ) {
    String absString = aFileOrDir.getAbsolutePath();
    File absFile = new File( thumbsRoot, TsFileUtils.removeStartingSeparator( absString ) );
    if( !absFile.exists() ) {
      return;
    }
    if( absFile.isDirectory() ) {
      TsFileUtils.deleteDirectory( absFile, IFileOperationProgressCallback.NULL );
      return;
    }
    for( EThumbSize thumbSize : EThumbSize.values() ) {
      File f = makeThumbFileName( aFileOrDir, thumbSize );
      if( f.exists() ) {
        f.delete();
      }
    }
  }

  private IMapEdit<File, TsImage> getThumbsMap( EThumbSize aThumbSize ) {
    IMapEdit<File, TsImage> map = thumbsMapMap.findByKey( aThumbSize );
    if( map == null ) {
      map = new ElemMap<>();
      thumbsMapMap.put( aThumbSize, map );
    }
    return map;
  }

  private void internalRefreshFile( File aFile ) {
    imagesMap.removeByKey( aFile );
    for( EThumbSize ths : EThumbSize.values() ) {
      getThumbsMap( ths ).removeByKey( aFile );
      File thumbFile = makeThumbFileName( aFile, ths );
      if( thumbFile.exists() ) {
        thumbFile.delete();
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  @Override
  public void setup( int aMaxImagesInMemory ) {
    // setup maxImagesInMemory
    int mim = aMaxImagesInMemory;
    if( mim < MIN_MAX_IMAGES_IN_MEMORY ) {
      mim = MIN_MAX_IMAGES_IN_MEMORY;
    }
    if( mim > MAX_MAX_IMAGES_IN_MEMORY ) {
      mim = MAX_MAX_IMAGES_IN_MEMORY;
    }
    if( maxImagesInMemory > mim ) {
      while( imagesMap.size() > mim ) {
        TsImage mi = imagesMap.removeByKey( imagesMap.keys().get( 0 ) );
        mi.dispose();
      }
    }
    maxImagesInMemory = mim;
  }

  @Override
  public void refreshCache( File aFileOrDir ) {
    TsNullArgumentRtException.checkNull( aFileOrDir );
    if( !aFileOrDir.exists() ) {
      deleteThumbFiles( aFileOrDir );
    }
    if( aFileOrDir.isFile() ) {
      internalRefreshFile( aFileOrDir );
    }
    else {
      if( aFileOrDir.isDirectory() ) {
        File[] files = aFileOrDir.listFiles( IMediaFileConstants.FF_IMAGES );
        for( File f : files ) {
          internalRefreshFile( f );
        }
      }
    }
  }

  @Override
  public boolean isCached( File aImageFile ) {
    return imagesMap.hasKey( aImageFile );
  }

  @Override
  public TsImage findImage( File aImageFile ) {
    TsImage mi = imagesMap.findByKey( aImageFile );
    if( mi != null ) {
      return mi;
    }
    try {
      mi = TsImageUtils.loadTsImage( aImageFile, display );
      imagesMap.put( aImageFile, mi );
      if( imagesMap.size() > maxImagesInMemory ) {
        imagesMap.removeByKey( imagesMap.keys().get( 0 ) ).dispose();
      }
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().warning( ex.getMessage() );
    }
    return mi;
  }

  @Override
  public TsImage getImage( File aImageFile ) {
    TsImage mi = findImage( aImageFile );
    TsItemNotFoundRtException.checkNull( mi );
    return mi;
  }

  @Override
  public void clearCache() {
    while( !imagesMap.isEmpty() ) {
      TsImage mi = imagesMap.removeByKey( imagesMap.keys().get( 0 ) );
      mi.dispose();
    }
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

package org.toxsoft.core.tsgui.graphics.image.impl;

import static org.toxsoft.core.tsgui.graphics.image.impl.TsImageManagerUtils.*;

import java.io.File;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.swt.widgets.Display;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.utils.IMediaFileConstants;
import org.toxsoft.core.tslib.coll.IMapEdit;
import org.toxsoft.core.tslib.coll.impl.ElemMap;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.IFileOperationProgressCallback;
import org.toxsoft.core.tslib.utils.files.TsFileUtils;
import org.toxsoft.core.tslib.utils.logs.impl.LoggerUtils;

/**
 * Реализация {@link ITsImageManager}.
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

  // кеши
  private final IMapEdit<File, TsImage>                       imagesMap    = new ElemMap<>();
  private final IMapEdit<EThumbSize, IMapEdit<File, TsImage>> thumbsMapMap = new ElemMap<>();

  // параметры кеширования
  private int maxImagesInMemory = DEF_MAX_IMAGES_IN_MEMORY;
  private int maxThumbsInMemory = DEF_MAX_THUMBS_IN_MEMORY;

  private File                  thumbsRoot = new File( DEFAULT_ROOT_PATH );
  private final IEclipseContext appContext;
  private final Display         display;

  /**
   * Конструктор.
   * <p>
   * В контексте ожидает ссылку на {@link Display}.
   *
   * @param aAppContext {@link IEclipseContext} - контекст приложения
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public TsImageManager( IEclipseContext aAppContext ) {
    TsNullArgumentRtException.checkNull( aAppContext );
    appContext = aAppContext;
    display = appContext.get( Display.class );
    TsInternalErrorRtException.checkNull( display );
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
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

  // private void internalRefreshOnRemove( File aFsObj ) {
  // IListEdit<File> toRemove = new ElemLinkedBundleList<>();
  // String fsObjPath = TsFileUtils.removeEndingSeparator( aFsObj.getAbsolutePath() );
  // for( File f : imagesMap.keys() ) {
  // String fPath = TsFileUtils.removeEndingSeparator( f.getAbsolutePath() );
  // if( fPath.startsWith( fsObjPath ) ) {
  // toRemove.add( f );
  // }
  // }
  // for( File f : toRemove ) {
  // imagesMap.remove( f );
  // for( EThumbSize thumbSize : EThumbSize.values() ) {
  // File thumbFile = makeThumbFileName( f, thumbSize );
  // thumbFile.delete();
  // }
  // }
  // }

  // ------------------------------------------------------------------------------------
  // API
  //

  @Override
  public File getThumbsRoot() {
    return thumbsRoot;
  }

  @Override
  public void setThumbsRoot( File aDir ) {
    TsFileUtils.checkDirReadable( aDir );
    if( !thumbsRoot.equals( aDir ) ) {
      thumbsRoot = aDir;
      clearCache();
    }
  }

  @Override
  public void setup( int aMaxImagesInMemory, int aMaxThumbsInMemory ) {
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
        File[] files = aFileOrDir.listFiles( IMediaFileConstants.IMAGE_FILES_FILTER );
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
    // не нужно (пере)создавать файл значка
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

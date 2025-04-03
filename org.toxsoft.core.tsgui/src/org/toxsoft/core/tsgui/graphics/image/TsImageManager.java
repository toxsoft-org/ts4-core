package org.toxsoft.core.tsgui.graphics.image;

import java.io.*;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.image.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.math.*;
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

  private static final int MIN_MAX_IMAGES_IN_MEMORY = 16;
  private static final int DEF_MAX_IMAGES_IN_MEMORY = 1024;
  private static final int MAX_MAX_IMAGES_IN_MEMORY = 16 * 1024;

  private static final IntRange unknownImageSizeRange      = new IntRange( 16, 256 );
  private static final Color    UNKNOWN_IMAGE_BACK_COLOR   = new Color( ETsColor.WHITE.rgba() );
  private static final Color    UNKNOWN_IMAGE_SQUARE_COLOR = new Color( ETsColor.RED.rgba() );

  // caches
  private final IMapEdit<File, TsImage>              filesMap = new ElemMap<>();
  private final IMapEdit<TsImageDescriptor, TsImage> descrMap = new ElemMap<>();

  // caching parameters
  private int maxImagesInMemory = DEF_MAX_IMAGES_IN_MEMORY;

  private final ITsGuiContext tsContext;
  private final Display       display;

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TsImageManager( ITsGuiContext aContext ) {
    tsContext = TsNullArgumentRtException.checkNull( aContext );
    display = tsContext.get( Display.class );
    TsInternalErrorRtException.checkNull( display );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  @Override
  public boolean isCached( TsImageDescriptor aDescriptor ) {
    return descrMap.hasKey( aDescriptor );
  }

  @Override
  public TsImage getImage( TsImageDescriptor aDescriptor ) {
    TsImage mi = descrMap.findByKey( aDescriptor );
    if( mi != null ) {
      return mi;
    }
    ITsImageSourceKind sourceKind = TsImageDescriptor.getImageSourceKindsMap().getByKey( aDescriptor.kindId() );
    mi = sourceKind.createImage( aDescriptor, tsContext );
    descrMap.put( aDescriptor, mi );
    if( descrMap.size() > maxImagesInMemory ) {
      descrMap.removeByKey( descrMap.keys().get( 0 ) ).dispose();
    }
    return mi;
  }

  @Override
  public void setup( int aMaxImagesInMemory ) {
    int mim = aMaxImagesInMemory;
    if( mim < MIN_MAX_IMAGES_IN_MEMORY ) {
      mim = MIN_MAX_IMAGES_IN_MEMORY;
    }
    if( mim > MAX_MAX_IMAGES_IN_MEMORY ) {
      mim = MAX_MAX_IMAGES_IN_MEMORY;
    }
    if( maxImagesInMemory > mim ) {
      while( filesMap.size() > mim ) {
        TsImage mi = filesMap.removeByKey( filesMap.keys().get( 0 ) );
        mi.dispose();
      }
    }
    maxImagesInMemory = mim;
  }

  @Override
  public void refreshCache( File aFileOrDir ) {
    TsNullArgumentRtException.checkNull( aFileOrDir );
    if( TsImageManagementUtils.isDir( aFileOrDir ) ) {
      IListEdit<File> llToRemove = new ElemArrayList<>();
      for( File f : filesMap.keys() ) {
        if( TsFileUtils.isChild( aFileOrDir, f ) ) {
          llToRemove.add( f );
        }
      }
      for( File f : llToRemove ) {
        filesMap.removeByKey( f );
      }
    }
    else {
      filesMap.removeByKey( aFileOrDir );
    }
  }

  @Override
  public File saveToFile( TsImage aImage, boolean aLoseless, String aFilePath ) {
    TsNullArgumentRtException.checkNull( aImage );
    TsErrorUtils.checkNonBlank( aFilePath );
    ESaveImageFileFormat sf = ESaveImageFileFormat.determineFormat( aImage, aLoseless );
    String ext = TsFileUtils.extractExtension( aFilePath );
    File outFile;
    if( ext.equalsIgnoreCase( sf.getFileExtension() ) ) {
      outFile = new File( aFilePath );
    }
    else {
      outFile = new File( aFilePath + TsFileUtils.CHAR_EXT_SEPARATOR + sf.getFileExtension() );
    }
    outFile.getParentFile().mkdirs();
    TsFileUtils.checkFileAppendable( outFile );
    sf.save( outFile, aImage );
    return outFile;
  }

  @Override
  public boolean isCached( File aImageFile ) {
    return filesMap.hasKey( aImageFile );
  }

  @Override
  public TsImage findImage( File aImageFile ) {
    TsImage mi = filesMap.findByKey( aImageFile );
    if( mi != null ) {
      return mi;
    }
    try {
      mi = TsImageUtils.loadTsImage( aImageFile, display );
      filesMap.put( aImageFile, mi );
      if( filesMap.size() > maxImagesInMemory ) {
        filesMap.removeByKey( filesMap.keys().get( 0 ) ).dispose();
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
    while( !filesMap.isEmpty() ) {
      TsImage mi = filesMap.removeByKey( filesMap.keys().get( 0 ) );
      mi.dispose();
    }
    System.gc();
  }

  @Override
  public TsImage createUnknownImage( int aImageSize ) {
    int size = unknownImageSizeRange.inRange( aImageSize );
    Image image = new Image( display, size, size );
    GC gc = new GC( image );
    gc.setBackground( UNKNOWN_IMAGE_BACK_COLOR );
    gc.fillRectangle( 0, 0, size, size );
    gc.setBackground( UNKNOWN_IMAGE_SQUARE_COLOR );
    int margin = size / 4;
    int square_size = size / 2;
    gc.fillRectangle( margin, margin, square_size, square_size );
    gc.dispose();
    return TsImage.create( image );
  }

}

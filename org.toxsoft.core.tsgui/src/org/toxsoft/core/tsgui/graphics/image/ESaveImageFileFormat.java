package org.toxsoft.core.tsgui.graphics.image;

import java.io.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.image.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;

/**
 * Internal implementation of {@link TsImage} saving to the disk file.
 *
 * @author hazard157
 */
enum ESaveImageFileFormat {

  STILL_JPG( "jpg" ) { //$NON-NLS-1$

    @Override
    protected void save( File aFile, TsImage aImage ) {
      saveStillImage( aFile, aImage, SWT.IMAGE_JPEG );
    }

  },

  STILL_PNG( "png" ) { //$NON-NLS-1$

    @Override
    protected void save( File aFile, TsImage aImage ) {
      saveStillImage( aFile, aImage, SWT.IMAGE_PNG );
    }

  },

  ANIMATED_GIF( "gif" ) { //$NON-NLS-1$

    @Override
    protected void save( File aFile, TsImage aImage ) {
      saveAnimatedImage( aFile, aImage );
    }

  };

  private final String ext;

  ESaveImageFileFormat( String aExt ) {
    ext = aExt.toLowerCase();
  }

  public String getFileExtension() {
    return ext;
  }

  public static ESaveImageFileFormat determineFormat( TsImage aImage, boolean aLoseless ) {
    if( aImage != null && !aImage.isDisposed() ) {
      if( aImage.isAnimated() ) {
        return ANIMATED_GIF;
      }
      if( aLoseless ) {
        return STILL_PNG;
      }
      return STILL_JPG;
    }
    return null;
  }

  public static File findExistingFileByBarePath( String aFilePath ) {
    for( ESaveImageFileFormat sf : values() ) {
      File f = new File( aFilePath + TsFileUtils.CHAR_EXT_SEPARATOR + sf.ext );
      if( f.exists() ) {
        return f;
      }
    }
    return null;
  }

  protected abstract void save( File aFile, TsImage aImage );

  private static void saveStillImage( File aFile, TsImage aImage, int aSwtFormat ) {
    ImageLoader saver = new ImageLoader();
    saver.data = new ImageData[] { aImage.image().getImageData() };
    try {
      saver.save( aFile.getAbsolutePath(), aSwtFormat );
    }
    catch( Exception ex ) {
      throw new TsIoRtException( ex );
    }
  }

  private static void saveAnimatedImage( File aFile, TsImage aImage ) {
    // TODO реализовать ESaveImageFileFormat.saveAnimatedImage()
    throw new TsUnderDevelopmentRtException( "ESaveImageFileFormat.saveAnimatedImage()" );
  }

}

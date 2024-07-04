package org.toxsoft.core.tsgui.graphics.image;

import java.io.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.image.TsImageManagementUtils.*;
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
    saveSwtImage( aFile, aImage.image(), aSwtFormat );
  }

  private static void saveAnimatedImage( File aFile, TsImage aImage ) {
    TempFilesSequence tfs = new TempFilesSequence( aImage.count(), STILL_PNG.ext );
    try {
      // create frame files sequence in the temporary directory
      for( int i = 0; i < aImage.count(); i++ ) {
        Image image = aImage.frames().get( i );
        File f = tfs.seqFiles.get( i );
        saveSwtImage( f, image, SWT.IMAGE_PNG );
      }
      // call GraphicsMagick to create
      // Example: gm convert -delay 20 *.jpg outfile.gif
      // TODO process non-even animation when aImage.isEvenAnimation() = false
      int gmDelay = (int)(aImage.delay() / 10L);
      if( gmDelay <= 0 ) {
        gmDelay = 1;
      }
      String srcFilesMask = TsFileUtils.ensureEndingSeparator( tfs.dir.getAbsolutePath() ) + "*." + STILL_PNG.ext; //$NON-NLS-1$
      TsImageManagementUtils.runGmWait( "convert", //$NON-NLS-1$
          "-delay", Integer.toString( gmDelay ), //$NON-NLS-1$
          srcFilesMask, //
          aFile.getAbsolutePath() //
      );
    }
    finally {
      // clean up temporary files and directory
      tfs.close();
    }
  }

  private static void saveSwtImage( File aFile, Image aImage, int aSwtFormat ) {
    ImageLoader saver = new ImageLoader();
    saver.data = new ImageData[] { aImage.getImageData() };
    try {
      saver.save( aFile.getAbsolutePath(), aSwtFormat );
    }
    catch( Exception ex ) {
      throw new TsIoRtException( ex );
    }
  }

}

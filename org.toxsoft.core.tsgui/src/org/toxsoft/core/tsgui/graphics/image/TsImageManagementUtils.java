package org.toxsoft.core.tsgui.graphics.image;

import java.io.*;

import org.toxsoft.core.tsgui.graphics.image.impl.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;

/**
 * Helper methods for image, thumbs and icons managers implementation.
 *
 * @author hazard157
 */
class TsImageManagementUtils {

  /**
   * TODO need rewrite for the following reasons: <br>
   * 1. Correctly determine #DEFAULT_ROOT_PATH for Linux/Windows/MacOS <br>
   * 2. Check #DEFAULT_ROOT_PATH exists and is writable and log warning if not <br>
   * 3. Log warning if GraphicksMagic application is not installed <br>
   */

  /**
   * GraphicsMagick executable file name.
   */
  private static final String GM_PROGRAM_NAME = "gm"; //$NON-NLS-1$

  static final String         DEFAULT_ROOT_PATH      = "/home/zcache";                  //$NON-NLS-1$
  private static final String TSIMGSRCKIND_ROOT_OATH = DEFAULT_ROOT_PATH + "/ts_imgsk"; //$NON-NLS-1$
  private static final String TMP_WORKS_ROOT_OATH    = DEFAULT_ROOT_PATH + "/tmp";      //$NON-NLS-1$

  static class TempFilesSequence
      implements ICloseable {

    final File        dir;
    final IList<File> seqFiles;

    @SuppressWarnings( "boxing" )
    public TempFilesSequence( int aCount, String aExt ) {
      TsIllegalArgumentRtException.checkTrue( aCount <= 0 );
      // ensure empty #dir exists
      File d;
      do {
        String dirName = "seq_" + System.nanoTime(); //$NON-NLS-1$
        d = new File( TMP_WORKS_ROOT_OATH, dirName );
      } while( d.exists() );
      d.mkdirs();
      dir = d;
      // create sequental file names
      IListEdit<File> ll = new ElemArrayList<>( aCount );
      for( int i = 0; i < aCount; i++ ) {
        String fileName = String.format( "f%06d", i ); //$NON-NLS-1$
        ll.add( new File( dir, fileName + TsFileUtils.CHAR_EXT_SEPARATOR + aExt ) );
      }
      seqFiles = ll;
    }

    @Override
    public void close() {
      if( TsFileUtils.isDirWriteable( dir ) ) {
        TsFileUtils.deleteDirectory( dir, IFileOperationProgressCallback.NULL );
      }
    }

  }

  /**
   * Creates unique file name in the {@link #TSIMGSRCKIND_ROOT_OATH} directory.
   *
   * @return {@link File} - absolute path (without extension) of the file in the temporary images directory
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  static String makeTsImgDescrTempFileAbsulutePath( ITsImageSourceKind aKind, TsImageDescriptor aImgDescr ) {
    return TSIMGSRCKIND_ROOT_OATH + '/' + aKind.id() + '/' + aImgDescr.uniqueName();
  }

  // ------------------------------------------------------------------------------------
  // Working with the thumbnails
  //

  /**
   * Создает имя скрытого файла значка для указанного файла изображения.
   *
   * @param aFile {@link File} - файл изображения
   * @param aThumbSize {@link EThumbSize} - размер значка
   * @return String - имя файла значка с расширением (но без имени директория)
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  @SuppressWarnings( "nls" )
  public static String formatImageThumbFileName( File aFile, EThumbSize aThumbSize ) {
    TsNullArgumentRtException.checkNulls( aFile, aThumbSize );
    StringBuilder sb = new StringBuilder();
    sb.append( "thumb-" );
    sb.append( aThumbSize.id() );
    sb.append( "-" );
    sb.append( aFile.getName() );
    if( IMediaFileConstants.hasAnimatedExtension( aFile.getName() ) ) {
      sb.append( ".gif" );
    }
    else {
      sb.append( ".png" );
    }
    return sb.toString();
  }

  /**
   * Создает скрытый файл значка в директории файла изображения.
   * <p>
   * Файлу дается название {@link #formatImageThumbFileName(File, EThumbSize)}.
   *
   * @param aFile {@link File} - файл изображения
   * @param aThumbSize {@link EThumbSize} - размер значка
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIoRtException ошибка выполнения программы конвертации "gm"
   */
  public static void createThumbFileInSameDir( File aFile, EThumbSize aThumbSize ) {
    TsNullArgumentRtException.checkNulls( aFile, aThumbSize );
    File thumbFile = new File( aFile.getParentFile(), formatImageThumbFileName( aFile, aThumbSize ) );
    createThumbFile( aFile, thumbFile, aThumbSize );
  }

  /**
   * Создает файл значка указанного размера.
   *
   * @param aSrc {@link File} - исходное изображение
   * @param aDest {@link File} - имя файла назначения (с раширением)
   * @param aThumbSize {@link EThumbSize} - размер созданного файла
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  @SuppressWarnings( "nls" )
  public static void createThumbFile( File aSrc, File aDest, EThumbSize aThumbSize ) {
    TsNullArgumentRtException.checkNulls( aDest, aThumbSize );
    TsFileUtils.isFileReadable( aSrc );
    File dir = aDest.getParentFile();
    if( !dir.exists() ) {
      dir.mkdirs();
    }
    TsFileUtils.isFileAppendable( aDest );
    String srcAbs = aSrc.getAbsolutePath();
    String destAbs = aDest.getAbsolutePath();
    Integer dim = Integer.valueOf( aThumbSize.size() );
    // String dimsStr = String.format( "%dx%d>", dim, dim ); // "WWWxHHH>", ">" чтобы не увеличивать меленькие
    // изображения
    String dimsStr = String.format( "%dx%d", dim, dim ); // "WWWxHHH>", ">" чтобы не увеличивать меленькие изображения
    if( IMediaFileConstants.hasAnimatedExtension( aDest.getName() ) ) {

    }
    // делаем без coalesce
    runGmWait( "convert", //
        "-gravity", "center", //
        "-resize", dimsStr, //
        "-background", "none", //
        "-extent", dimsStr, //
        srcAbs, destAbs );
  }

  /**
   * Де-оптимизирует анимированное изображение для корректного отображения средствами {@link TsImage}.
   *
   * @param aImageFile {@link File} - файл изображения
   * @return boolean - признак, что программа преобразования была запущена
   * @throws TsNullArgumentRtException аргумент = null
   */
  @SuppressWarnings( "nls" )
  public static final boolean coalesceAnimatedImage( File aImageFile ) {
    if( !TsFileUtils.isFileWriteable( aImageFile ) ) {
      return false;
    }
    if( !IMediaFileConstants.hasAnimatedExtension( aImageFile.getName() ) ) {
      return false;
    }
    String absPath = aImageFile.getAbsolutePath();
    TsMiscUtils.runProgram( GM_PROGRAM_NAME, "convert", "-coalesce", absPath, absPath );
    return true;
  }

  public static void runGmWait( String... aArgs ) {
    TsMiscUtils.runAndWait( 60, GM_PROGRAM_NAME, aArgs );
  }

  static boolean isDir( File aFileOrDir ) {
    if( aFileOrDir.exists() ) {
      return aFileOrDir.isDirectory();
    }
    String fodName = aFileOrDir.getName();
    if( fodName.isBlank() ) {
      return false;
    }
    if( fodName.charAt( fodName.length() - 1 ) == File.separatorChar ) {
      return true;
    }
    if( IMediaFileConstants.hasImageExtension( fodName ) ) {
      return false;
    }
    return true;
  }

  /**
   * No subclasses.
   */
  private TsImageManagementUtils() {
    // nop
  }

}

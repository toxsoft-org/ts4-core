package org.toxsoft.core.tsgui.graphics.image.impl;

import java.io.File;

import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.utils.IMediaFileConstants;
import org.toxsoft.core.tslib.utils.TsMiscUtils;
import org.toxsoft.core.tslib.utils.errors.TsIoRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.core.tslib.utils.files.TsFileUtils;

/**
 * Вспомогательные методы для {@link ITsImageManager}.
 * <p>
 * Методы не "достойны" войти в API {@link ITsImageManager}, но могут быть полезны для некторых применений.
 *
 * @author hazard157
 */
public class TsImageManagerUtils {

  /**
   * Название исполняемой программы GraphicsMagick.
   */
  private static final String GM_PROGRAM_NAME = "gm"; //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Преобразование изображений Image
  //

  // GOGA этот код не работает в RAP
  // /**
  // * Изменяет размер изображения и возвращает новое, масштабированное изображение.
  // * <p>
  // * Этот метод потенциально изменяет соотношение сторон изображения, для рисования без изменения соотношения сторон
  // * используйте {@link #resizeImageWithFit(Image, TsPoint)}.
  // *
  // * @param aOriginal {@link Image} - исходное изображение
  // * @param aNewSize {@link TsPoint} - новый размер
  // * @return {@link Image} - масштабированное изображение
  // * @throws TsNullArgumentRtException любой аргумент = null
  // * @throws TsIllegalArgumentRtException новый размер слишком мал или отрицательный
  // */
  // public static final Image resizeImage( Image aOriginal, TsPoint aNewSize ) {
  // TsNullArgumentRtException.checkNulls( aOriginal, aNewSize );
  // TsIllegalArgumentRtException.checkTrue( aNewSize.x() < 1 || aNewSize.y() < 1 );
  // Image img = new Image( aOriginal.getDevice(), new Rectangle( 0, 0, aNewSize.x(), aNewSize.y() ) );
  // int origW = aOriginal.getImageData().width;
  // int origH = aOriginal.getImageData().height;
  // GC gc = new GC( img );
  // try {
  // gc.drawImage( aOriginal, 0, 0, origW, origH, 0, 0, aNewSize.x(), aNewSize.y() );
  // }
  // finally {
  // gc.dispose();
  // }
  // return img;
  // }
  //
  // /**
  // * Изменяет размер изображения и возвращает новое, масштабированное изображение.
  // * <p>
  // * Этот метод сохраняет соотношение сторон изображения, вписывая новое изображение по ширине или высоте по центру
  // * области размером aNewSize.
  // *
  // * @param aOriginal {@link Image} - исходное изображение
  // * @param aNewSize {@link TsPoint} - новый размер
  // * @return {@link Image} - масштабированное изображение
  // * @throws TsNullArgumentRtException любой аргумент = null
  // * @throws TsIllegalArgumentRtException новый размер слишком мал или отрицательный
  // */
  // public static final Image resizeImageWithFit( Image aOriginal, TsPoint aNewSize ) {
  // TsNullArgumentRtException.checkNulls( aOriginal, aNewSize );
  // TsIllegalArgumentRtException.checkTrue( aNewSize.x() < 1 || aNewSize.y() < 1 );
  // Rectangle imgBounds = new Rectangle( 0, 0, aNewSize.x(), aNewSize.y() );
  // Image img = new Image( aOriginal.getDevice(), imgBounds );
  // int origW = aOriginal.getImageData().width;
  // int origH = aOriginal.getImageData().height;
  // TsPoint fitedSize =
  // TsImageUtils.calcSize( origW, origH, aNewSize.x(), aNewSize.y(), TsImageUtils.ZOOM_FIT_BEST, true );
  // GC gc = new GC( img );
  // try {
  // int destX = (aNewSize.x() - fitedSize.x()) / 2;
  // int destY = (aNewSize.y() - fitedSize.y()) / 2;
  // gc.drawImage( aOriginal, 0, 0, origW, origH, destX, destY, fitedSize.x(), fitedSize.y() );
  // }
  // finally {
  // gc.dispose();
  // }
  // return img;
  // }

  // ------------------------------------------------------------------------------------
  // Работа со значками (уменьшенные копии изображения) и их файлами
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
      // FIXME // для анимированных если нужно, сделаем -coalasce, потом по месту - изменение размера
      // runProgram( 30, GM_PROGRAM_NAME, "convert", "-coalesce", srcAbs, destAbs );
      // runProgram( 30, GM_PROGRAM_NAME, "convert", "-gravity", "center", "-resize", dimsStr, "-extent", dimsStr,
      // "-background", "#transparent", destAbs, destAbs );

      // делаем без coalesce
      TsMiscUtils.runAndWait( 60, GM_PROGRAM_NAME, "convert", //
          "-gravity", "center", //
          "-resize", dimsStr, //
          "-background", "none", //
          "-extent", dimsStr, //
          srcAbs, destAbs );
    }
    else {
      TsMiscUtils.runAndWait( 60, GM_PROGRAM_NAME, "convert", //
          "-gravity", "center", //
          "-resize", dimsStr, //
          "-background", "none", //
          "-extent", dimsStr, //
          srcAbs, destAbs );
    }
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

  /**
   * Запрет на создание экземпляров.
   */
  private TsImageManagerUtils() {
    // nop
  }

}

package org.toxsoft.core.tsgui.graphics.image;

import java.io.*;

import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.image.FileFormat;
import org.toxsoft.core.tslib.utils.errors.TsIoRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.core.tslib.utils.files.TsFileUtils;

/**
 * Helper methos to work with {@link TsImage}.
 *
 * @author hazard157
 */
@SuppressWarnings( "restriction" )
public class TsImageUtils {

  // ------------------------------------------------------------------------------------
  // Load images from file
  //

  /**
   * Загружает набор кадров для анимированного изображения из файла.
   * <p>
   * Метод может загрузить как анимированные (например, GIF), так и одиночные изображения (например, JPG). В случае
   * загрузки одиночого изображения, метод {@link TsImage#frames()} возвращает массив из одного элемента - загруженного
   * изображения.
   *
   * @param aImageFile File
   * @param aDevice {@link Device} - устройство, для отображения на котором и создается изображение {@link Image}
   * @return {@link TsImage} - загруженное кадры анимированного или обычного изображения
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIoRtException ошибка доступа к файлу
   * @throws SWTException неизвестный или нарушенный формат файла изображения
   */
  public static TsImage loadTsImage( File aImageFile, Device aDevice ) {
    TsFileUtils.checkFileReadable( aImageFile );
    TsNullArgumentRtException.checkNull( aDevice );
    try( InputStream is = new FileInputStream( aImageFile ) ) {
      return loadTsImage( is, aDevice );
    }
    catch( IOException e ) {
      throw new TsIoRtException( e );
    }
  }

  /**
   * Загружает изображение из указанного потока.
   *
   * @param aInputStream {@link InputStream} - входной поток
   * @param aDevice {@link Device} - графическое устройство, в привязке с которым создается изображение
   * @return {@link TsImage} - загруженное изображение
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public static TsImage loadTsImage( InputStream aInputStream, Device aDevice ) {
    TsNullArgumentRtException.checkNulls( aInputStream, aDevice );
    // 2021-07-26 GOGA --- в SWT версии 4.15 изменили способ загрузки. GIF-анимация стала грузится неправильно
    // временно, пока не исправят ошибку, сделаем загрузку старым способом - через FileFormat
    // ImageLoader imageLoader = new ImageLoader();
    // imageLoader.load( aInputStream );
    // ImageData[] darr = imageLoader.data;
    ImageData[] darr = FileFormat.load( aInputStream, new ImageLoader() );
    // ---
    int count = darr.length;
    Image[] images = new Image[count];
    long[] frameDelays = new long[count];
    for( int i = 0; i < count; ++i ) {
      ImageData nextFrameData = darr[i];
      images[i] = new Image( aDevice, nextFrameData );
      frameDelays[i] = nextFrameData.delayTime * 10; // delayTime в единицах 1/100 секунды
    }
    TsImage mi = new TsImage( images, frameDelays, 0 );
    return mi;
  }

  private TsImageUtils() {
    // nop
  }

}

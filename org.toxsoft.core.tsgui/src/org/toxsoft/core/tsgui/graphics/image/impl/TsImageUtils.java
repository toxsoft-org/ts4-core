package org.toxsoft.core.tsgui.graphics.image.impl;

import java.io.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.image.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;

/**
 * Helper methods to work with {@link TsImage}.
 *
 * @author hazard157
 */
@SuppressWarnings( "restriction" )
public class TsImageUtils {

  /**
   * Default inter-frame delay used when specified delay is 0.
   */
  private static final long DEFAUL_FRAME_DELAY = 1000L / 25; // 40 msec = 25 FPS

  // ------------------------------------------------------------------------------------
  // Load images from file
  //

  // TODO TRANSLATE

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
    TsIllegalArgumentRtException.checkTrue( aDevice.isDisposed() );
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
    TsIllegalArgumentRtException.checkTrue( aDevice.isDisposed() );
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

  /**
   * Returns the index of the frame to be displayed at the specified time after cycle start.
   * <p>
   * Method has sense for animated images, for still images always returns 0.
   * <p>
   * Animated image is a set of {@link TsImage#frames()} each to be displayed during {@link TsImage#delays()}
   * milliseconds. After last frame display the cycle starts from the first frame. This method determines which frame to
   * display when <code>aMisllisecs</code> passed after cycle start.
   *
   * @param aImage {@link TsImage} - the image
   * @param aMisllisecs long - number of milliseconds after cycle start (maybe negative)
   * @return int - the displayed frame index in {@link TsImage#frames()} list
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static int findIndexByTime( TsImage aImage, long aMisllisecs ) {
    TsNullArgumentRtException.checkNull( aImage );
    if( aImage.isSingleFrame() ) {
      return 0;
    }
    /**
     * Note: it is legal for animated image to have delays of 0 msec. However, for such frames delay will be replaced by
     * the value #DEFAUL_FRAME_DELAY.
     */
    long cycleDur = Math.max( aImage.cycleDuration(), 1 );
    for( int i = 0; i < aImage.count(); i++ ) {
      long frameDelay = aImage.delays().getValue( i );
      if( frameDelay == 0L ) {
        cycleDur += DEFAUL_FRAME_DELAY;
      }
    }
    // msecs since cycle start
    long passedMsecs = aMisllisecs % cycleDur;
    if( passedMsecs < 0 ) {
      passedMsecs += cycleDur;
    }
    // find frame for #passedMsecs
    long nextFrameStartMsec = 0;
    for( int i = 0; i < aImage.count(); i++ ) {
      long frameDelay = aImage.delays().getValue( i );
      if( frameDelay == 0L ) {
        frameDelay = DEFAUL_FRAME_DELAY;
      }
      nextFrameStartMsec += frameDelay;
      if( passedMsecs < nextFrameStartMsec ) {
        return i;
      }
    }
    throw new TsInternalErrorRtException();
  }

  private TsImageUtils() {
    // nop
  }

}

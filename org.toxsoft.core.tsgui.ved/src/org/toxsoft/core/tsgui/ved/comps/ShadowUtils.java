package org.toxsoft.core.tsgui.ved.comps;

import java.util.*;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * Утилитные методы для генерации "тени" от разлиных объектов.
 * <p>
 *
 * @author vs
 */
public class ShadowUtils {

  static Color colorBlack = new Color( 0, 0, 0 );

  static Color colorServ = new Color( 100, 100, 100 );

  /**
   * Возвращает размер окна для фильтра "скользящее среднее-СС".
   *
   * @param aSigma - длина отбрасываемой тени
   * @param aCount - количество раз последовательного примнения фильтра "СС"
   * @return int - размер скользящего окна для фильтра "СС"
   */
  public static int movingAverageFrameLength( double aSigma, int aCount ) {
    int frameLength = (int)Math.sqrt( (12. * aSigma * aSigma) / aCount + 1 );
    return frameLength;
  }

  /**
   * Создает и возвращает графический контекст для формирования тени.
   * <p>
   *
   * @param aImage {@link Image}
   * @param aFrameLength int - длина скользящего буфера
   * @param aAlpha int - значение прозрачности [0-255]
   * @param aXfactor double - коэффициент масштабирования по оси X
   * @param aYFactor double - коэффициент масштабирования по оси Y
   * @return {@link GC} - графический контекст для формирования тени
   */
  public static GC createGcForShadow( Image aImage, int aFrameLength, int aAlpha, double aXfactor, double aYFactor ) {
    GC gc = new GC( aImage );

    gc.setAlpha( aAlpha );
    gc.setBackground( colorServ );

    Transform tr = new Transform( aImage.getDevice() );
    tr.translate( 2 * aFrameLength, 2 * aFrameLength );
    tr.scale( (float)aXfactor, (float)aYFactor );
    gc.setTransform( tr );
    tr.dispose();
    return gc;
  }

  /**
   * Преобразует изображение в тень, удаляя цвет и задавая для всех не нулевых пикселей, переданное значение
   * прозрачности.
   *
   * @param aImage {@link Image}
   * @param aFrameLength int - длина скользящего буфера
   * @param aAlpha int - значение прозрачности [0-255]
   * @param aCount int - количество применений фильтра "скользящее среднее"
   * @return {@link Image} - изображение тени
   */
  public static Image image2shadow( Image aImage, int aFrameLength, int aAlpha, int aCount ) {
    ImageData imd = aImage.getImageData();
    imd.alpha = -1;
    for( int i = 0; i < imd.width; i++ ) {
      for( int j = 0; j < imd.height; j++ ) {
        if( imd.getPixel( i, j ) == 0 ) {
          imd.setAlpha( i, j, 0 );
        }
        else {
          imd.setAlpha( i, j, aAlpha );
        }
        imd.setPixel( i, j, 0 );
      }
    }

    filterByMovingAverage( aFrameLength, aCount, imd );

    Image image = new Image( aImage.getDevice(), imd );
    return image;
  }

  /**
   * Создает и возвращает изображение тени для переданного пути.
   *
   * @param aPath {@link Path} - путь
   * @param aShadowLength int - длина тени
   * @param aAlpha int - значение прозрачности [0-255]
   * @param aXfactor double - коэффициент масштабирования по оси X
   * @param aYFactor double - коэффициент масштабирования по оси Y
   * @param aCount - количество раз последовательного примнения фильтра "СС"
   * @return {@link Image} - изображение тени для переданного пути
   */
  public static Image createShadowImage( Path aPath, int aShadowLength, int aAlpha, double aXfactor, double aYFactor,
      int aCount ) {
    Display display = Display.getDefault();
    int fl = movingAverageFrameLength( aShadowLength, 3 ); // длина скользящего буфера

    float[] bounds = new float[8];
    aPath.getBounds( bounds );
    int width = (int)Math.floor( bounds[2] + 4 * fl );
    int height = (int)Math.floor( bounds[3] + 4 * fl );
    if( height <= 0 ) {
      height = 1;
    }
    Image image = createTransparentImage( display, width, height, 255 );

    GC gc = createGcForShadow( image, fl, aAlpha, aXfactor, aYFactor );
    gc.setBackground( new Color( 100, 100, 100 ) );
    gc.fillPath( aPath );

    Image newImage = image2shadow( image, fl, aAlpha, aCount );

    gc.dispose();
    image.dispose();

    return newImage;
  }

  /**
   * Последовательно применяет фильтр - скользящее среднее, указанное количество раз, модифицируя, переданную информацию
   * об изображении.
   *
   * @param aFrameLength int - длина скользящего буфера
   * @param aCount int - количество проходов
   * @param aImd {@link ImageData} - данные изображения
   */
  public static void filterByMovingAverage( int aFrameLength, int aCount, ImageData aImd ) {

    int fl = aFrameLength;

    byte[] oldAlpha;

    for( int n = 0; n < aCount; n++ ) {
      // horizontal pass
      oldAlpha = Arrays.copyOf( aImd.alphaData, aImd.alphaData.length );
      for( int j = 0; j < aImd.height; j++ ) {
        int sum = 0;
        for( int k = 0; k < fl; k++ ) {
          sum += oldAlpha[k + j * aImd.width] & 0xFF;
        }
        for( int i = 0; i < aImd.width - fl - 1; i++ ) {
          aImd.setAlpha( i, j, (int)(sum / (double)fl) );
          sum -= oldAlpha[i + j * aImd.width] & 0xFF;
          sum += oldAlpha[i + fl + j * aImd.width] & 0xFF;
        }
      }

      // vertical pass
      oldAlpha = Arrays.copyOf( aImd.alphaData, aImd.alphaData.length );
      for( int i = 0; i < aImd.width; i++ ) {
        int sum = 0;
        for( int k = 0; k < fl; k++ ) {
          sum += oldAlpha[i + k * aImd.width] & 0xFF;
        }
        for( int j = 0; j < aImd.height - fl - 1; j++ ) {
          aImd.setAlpha( i, j, (int)(sum / (double)fl) );
          sum -= oldAlpha[i + j * aImd.width] & 0xFF;
          sum += oldAlpha[i + (fl + j) * aImd.width] & 0xFF;
        }
      }
    }
  }

  /**
   * Создает "пустое" изображение с прозрачным фоном.
   *
   * @param aDisplay {@link Display} - дисплей
   * @param aWidth int - ширина изображения в пикселях
   * @param aHeight int - высота изображения в пикселях
   * @param aAlpha int - коэффициент прозрачности (255 - полностью непрозрачный фон)
   * @return Image - изображение
   */
  public static Image createTransparentImage( Display aDisplay, int aWidth, int aHeight, int aAlpha ) {
    // allocate an image data
    ImageData imData = new ImageData( aWidth, aHeight, 24, new PaletteData( 0xff0000, 0x00ff00, 0x0000ff ) );
    imData.transparentPixel = -1;
    imData.setAlpha( 0, 0, 0 ); // just to force alpha array allocation with the right size
    Arrays.fill( imData.alphaData, (byte)0 ); // set whole image as transparent
    imData.alpha = aAlpha;

    Arrays.fill( imData.data, (byte)0 );

    return new Image( aDisplay, imData );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  /**
   * Запрет на создание экземпляров.
   */
  private ShadowUtils() {
    // nop
  }
}

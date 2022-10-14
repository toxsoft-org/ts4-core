package org.toxsoft.core.tsgui.chart.legaсy;

import java.awt.image.*;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Набор методов для обработки изображении.
 * <p>
 * Данный набор включает в себя методы конвертации изображения из "SWT" в "AWT" представление и обратно.
 *
 * @author vs
 * @author goga
 */
public class TsImageUtils {

  // ------------------------------------------------------------------------------------
  // Поддержка масштабирования
  //

  /**
   * Масштаб: показывать картинку родного размера, равен 100 (от 100%).
   */
  public static final int ZOOM_ORIGINAL   = 100;
  /**
   * Масштаб: уместить картину в ширину и высоту.
   */
  public static final int ZOOM_FIT_BEST   = -1;
  /**
   * Масштаб: уместить картину в ширину контроли.
   */
  public static final int ZOOM_FIT_WIDTH  = -2;
  /**
   * Масштаб: уместить картинку в высоту контроли.
   */
  public static final int ZOOM_FIT_HEIGHT = -3;
  /**
   * Масштаб: максимально допустмое значение масштаба.
   * <p>
   * Минимально допустимое значение по определению равено 1 (соответствет уменьшению в 100 раз).
   */
  public static final int MAX_ZOOM        = 10000; // 10000% = увеличение в 100 раз

  /**
   * Определяет, является ли аргумент допустимым коэффициентом масштабирования.
   * <p>
   * Допустимым ялвется коэффициент от 1-го до {@link #MAX_ZOOM}, или одна из констант {@link #ZOOM_FIT_BEST},
   * {@link #ZOOM_FIT_HEIGHT}, {@link #ZOOM_FIT_WIDTH} (напомним, что по определению {@link #ZOOM_ORIGINAL} = 100).
   *
   * @param aZoom int - коэффициент масштабирования
   * @return boolean - признак допустимого коэффициента масштабирования
   */
  public static boolean isValidZoom( int aZoom ) {
    if( aZoom >= 1 && aZoom <= MAX_ZOOM ) {
      return true;
    }
    switch( aZoom ) {
      case ZOOM_FIT_BEST:
      case ZOOM_FIT_HEIGHT:
      case ZOOM_FIT_WIDTH:
        return true;
      default:
        return false;
    }
  }

  /**
   * Вычисляет размер изображения с учетом масштабирования в области отображения.
   * <p>
   * Если любой размер изображения или области отображения равен 0, то возвращает константу {@link ITsPoint#ZERO}.
   *
   * @param aImgW int - ширина исходного изображения
   * @param aImgH int - высота исходного изображения
   * @param aViewW int - ширина области отображения
   * @param aViewH int - высота области отображения
   * @param aZoomFactor int - степень масштабирования (проценты или одна из констант адаптивного масштабирования
   *          {@link #ZOOM_FIT_BEST}, {@link #ZOOM_FIT_HEIGHT} или {@link #ZOOM_FIT_WIDTH})
   * @param aExpandToFit boolean - признак увеличения изображения в режимах адаптивного масштабирования <br>
   *          <b>true</b> - если изображения меньше области отображения, то увеличить до одного из размеров области
   *          отображения;<br>
   *          <b>false</b> - изображения меньше области отображения отображаются в родном размере.
   * @return {@link ITsPoint} - размеры отмасштабированного изображения
   * @throws TsIllegalArgumentRtException любой размер - отрицательный
   * @throws TsIllegalArgumentRtException aZoomFactor имеет недопустимое значение
   */
  public static ITsPoint calcSize( int aImgW, int aImgH, int aViewW, int aViewH, int aZoomFactor,
      boolean aExpandToFit ) {
    if( aImgW < 0 || aImgH < 0 || aViewW < 0 || aViewH < 0 || !isValidZoom( aZoomFactor ) ) {
      throw new TsIllegalArgumentRtException();
    }
    if( aImgW == 0 || aImgH == 0 || aViewW == 0 || aViewH == 0 ) {
      return ITsPoint.ZERO;
    }
    double imgAspect = ((double)aImgW) / ((double)aImgH);
    double viewAspect = ((double)aViewW) / ((double)aViewH);
    int w, h;
    switch( aZoomFactor ) {
      case TsImageUtils.ZOOM_FIT_BEST:
        if( imgAspect > viewAspect ) { // вместим в ширину
          w = aViewW;
          h = (int)(w / imgAspect);
        }
        else { // вместим в высоту
          h = aViewH;
          w = (int)(h * imgAspect);
        }
        break;
      case TsImageUtils.ZOOM_FIT_HEIGHT:
        h = aViewH;
        w = (int)(h * imgAspect);
        break;
      case TsImageUtils.ZOOM_FIT_WIDTH:
        w = aViewW;
        h = (int)(w / imgAspect);
        break;
      case TsImageUtils.ZOOM_ORIGINAL:
        w = aImgW;
        h = aImgH;
        break;
      default:
        w = aImgW * aZoomFactor / 100;
        h = aImgH * aZoomFactor / 100;
        if( w == 0 ) {
          w = 1;
        }
        if( h == 0 ) {
          h = 1;
        }
        break;
    }
    if( aZoomFactor < 0 && !aExpandToFit ) { // режим адаптивного масштабирования и НЕ надо увеличивать
      if( w > aImgW || h > aImgH ) {
        w = aImgW;
        h = aImgH;
      }
    }
    return new TsPoint( w, h );
  }

  /**
   * Изменяет размер изображения на новый размер.
   * <p>
   * Этот метод потенциально изменяет соотношение сторон изображения, для рисования без изменения соотношения сторон
   * используйте {@link #resizeImageWithFit(Image, ITsPoint)}.
   *
   * @param aOriginal {@link Image} - исходное изображение
   * @param aNewSize {@link ITsPoint} - новый размер
   * @return {@link Image} - масштабированное изображение
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException новый размер слишком мал или отрицательный
   */
  // FIXME это не работает в RAP
  // public static final Image resizeImage( Image aOriginal, ITsPoint aNewSize ) {
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

  /**
   * Изменяет размер изображения на новый размер.
   * <p>
   * Этот метод сохраняет соотношение сторон изображения, вписывая новое изображение по ширине или высоте по центру
   * области размером aNewSize.
   *
   * @param aOriginal {@link Image} - исходное изображение
   * @param aNewSize {@link ITsPoint} - новый размер
   * @return {@link Image} - масштабированное изображение
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException новый размер слишком мал или отрицательный
   */
  // FIXME это не работает в RAP
  // public static final Image resizeImageWithFit( Image aOriginal, ITsPoint aNewSize ) {
  // TsNullArgumentRtException.checkNulls( aOriginal, aNewSize );
  // TsIllegalArgumentRtException.checkTrue( aNewSize.x() < 1 || aNewSize.y() < 1 );
  // Rectangle imgBounds = new Rectangle( 0, 0, aNewSize.x(), aNewSize.y() );
  // Image img = new Image( aOriginal.getDevice(), imgBounds );
  // int origW = aOriginal.getImageData().width;
  // int origH = aOriginal.getImageData().height;
  // ITsPoint fitedSize = calcSize( origW, origH, aNewSize.x(), aNewSize.y(), ZOOM_FIT_BEST, true );
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
  // Методы преобразования изображений
  //

  /**
   * Преобразовывает SWT изображение в формат AWT.
   * <p>
   * Изображения в SWT и AWT представляются различными структурами данных. При работе в SWT при необхоимости
   * использования сторонних библиотек, например JasperReports, возникает необходимость обмениваться изображениями в
   * этих "форматах" AWT. Данная функция предназначена для преобразования изображения из "SWT" преставления в "AWT"
   * представление. Преобразование не меняет параметров изображения: цвета, размеры, прозрачность. В отличие от
   * стандартных snippets данная функция учитывает все варианты представления палитры изображения.
   *
   * @param aData - представление изображения в SWT
   * @return bufferedImage - представление изображения AWT
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static BufferedImage convertToAWT( ImageData aData ) {
    TsNullArgumentRtException.checkNull( aData );
    ColorModel colorModel = null;
    PaletteData palette = aData.palette;
    if( palette.isDirect ) {
      return createImageFromDirectPalette( aData );
    }

    RGB[] rgbs = palette.getRGBs();
    byte[] red = new byte[rgbs.length];
    byte[] green = new byte[rgbs.length];
    byte[] blue = new byte[rgbs.length];
    for( int i = 0; i < rgbs.length; i++ ) {
      RGB rgb = rgbs[i];
      red[i] = (byte)rgb.red;
      green[i] = (byte)rgb.green;
      blue[i] = (byte)rgb.blue;
    }
    if( aData.transparentPixel != -1 ) {
      colorModel = new IndexColorModel( aData.depth, rgbs.length, red, green, blue, aData.transparentPixel );
    }
    else {
      colorModel = new IndexColorModel( aData.depth, rgbs.length, red, green, blue );
    }
    BufferedImage bufferedImage = new BufferedImage( colorModel,
        colorModel.createCompatibleWritableRaster( aData.width, aData.height ), false, null );
    WritableRaster raster = bufferedImage.getRaster();
    int[] pixelArray = new int[1];
    for( int y = 0; y < aData.height; y++ ) {
      for( int x = 0; x < aData.width; x++ ) {
        int pixel = aData.getPixel( x, y );
        pixelArray[0] = pixel;
        raster.setPixel( x, y, pixelArray );
      }
    }
    return bufferedImage;
  }

  /**
   * Объединяет два изображения, точнее, накладывает одно изображение на другое.
   * <p>
   * При объединении результирующее изображение считается фоном, на котором по указанным координатам рисуется исходное
   * изображение. При объединении порождается новое изображения - аргументы остаются неизменными. Изображение наносится
   * на фон с учетом прозрачности.
   *
   * @param aX int - Х координата относительно левой границы фонового изображения
   * @param aY int - Y координата относительно верхней границы фонового изображения
   * @param aMixinImage {@link ImageData} - изображение, которое будет нанесено на фоновое изображение
   * @param aBaseImage {@link ImageData} - фоновое изображение, на котором будет нарисовано другое изображение
   * @return Image - результирующее изображение
   */
  public static Image mergeImages( int aX, int aY, final ImageData aMixinImage, final ImageData aBaseImage ) {
    int redMixin;
    int greenMixin;
    int blueMixin;
    int redBase;
    int greenBase;
    int blueBase;
    double mixinAlpha, baseAlpha;
    int resultPixel = 0;
    int basePixel;
    int mixinPixel;
    int baseRedShift = Math.abs( aBaseImage.palette.redShift );
    int baseGreenShift = Math.abs( aBaseImage.palette.greenShift );
    int baseBlueShift = Math.abs( aBaseImage.palette.blueShift );

    int mixinRedShift = Math.abs( aMixinImage.palette.redShift );
    int mixinBlueShift = Math.abs( aMixinImage.palette.blueShift );
    int mixinGreenShift = Math.abs( aMixinImage.palette.greenShift );

    for( int i = 0; i < aMixinImage.width; i++ ) {
      for( int j = 0; j < aMixinImage.height; j++ ) {
        mixinAlpha = aMixinImage.getAlpha( i, j ) / 255.;
        baseAlpha = aBaseImage.getAlpha( i, j ) / 255.;

        mixinPixel = aMixinImage.getPixel( i, j );
        basePixel = aBaseImage.getPixel( i + aX, j + aY );

        redBase = ((basePixel >> baseRedShift) & 0xff);
        redMixin = ((mixinPixel >> mixinRedShift) & 0xff);

        greenBase = ((basePixel >> baseGreenShift) & 0xff);
        greenMixin = ((mixinPixel >> mixinGreenShift) & 0xff);

        blueBase = ((basePixel >> baseBlueShift) & 0xff);
        blueMixin = ((mixinPixel >> mixinBlueShift) & 0xff);

        resultPixel = //
            ((int)(redBase * (1 - mixinAlpha) + redMixin * mixinAlpha) << baseRedShift) | //
                ((int)(greenBase * (1 - mixinAlpha) + greenMixin * mixinAlpha) << baseGreenShift) | //
                ((int)(blueBase * (1 - mixinAlpha) + blueMixin * mixinAlpha) << baseBlueShift);

        aBaseImage.setPixel( i + aX, j + aY, resultPixel );
        aBaseImage.setAlpha( i + aX, j + aY, 255 - (int)((1 - baseAlpha) * (1 - mixinAlpha) * 255) );
      }
    }
    return new Image( Display.getDefault(), aBaseImage ); // rap 1.4
  }

  /**
   * Изображения в SWT и AWT представляются различными структурами данных.<br>
   * При работе в SWT при необхоимости использования сторонних библиотек, например JasperReports, возникает
   * необходимость обмениваться изображениями в этих "форматах" AWT.<br>
   * Данная функция предназначена для преобразования изображения из "AWT" преставления в "SWT" представление.
   * Преобразование не меняет параметров изображения: цвета, размеры, прозрачность <br>
   * <br>
   * В отличие от стандартных snippets данная функция учитывает все варианты представления палитры изображения.
   *
   * @param aBufferedImage - представление изображения AWT
   * @return ImageData - представление изображения в SWT
   * @throws TsNullArgumentRtException - aBufferedImage - null
   */
  public static ImageData convertToSWT( BufferedImage aBufferedImage ) {
    if( aBufferedImage.getColorModel() instanceof DirectColorModel ) {
      DirectColorModel colorModel = (DirectColorModel)aBufferedImage.getColorModel();
      PaletteData palette =
          new PaletteData( colorModel.getRedMask(), colorModel.getGreenMask(), colorModel.getBlueMask() );
      ImageData data =
          new ImageData( aBufferedImage.getWidth(), aBufferedImage.getHeight(), colorModel.getPixelSize(), palette );
      WritableRaster raster = aBufferedImage.getRaster();
      int[] pixelArray = new int[4];
      for( int y = 0; y < data.height; y++ ) {
        for( int x = 0; x < data.width; x++ ) {
          raster.getPixel( x, y, pixelArray );
          int pixel = palette.getPixel( new RGB( pixelArray[0], pixelArray[1], pixelArray[2] ) );
          data.setPixel( x, y, pixel );
          if( colorModel.hasAlpha() ) {
            data.setAlpha( x, y, pixelArray[3] );
          }
        }
      }
      return data;
    }
    else
      if( aBufferedImage.getColorModel() instanceof IndexColorModel ) {
        IndexColorModel colorModel = (IndexColorModel)aBufferedImage.getColorModel();
        int size = colorModel.getMapSize();
        byte[] reds = new byte[size];
        byte[] greens = new byte[size];
        byte[] blues = new byte[size];
        colorModel.getReds( reds );
        colorModel.getGreens( greens );
        colorModel.getBlues( blues );
        RGB[] rgbs = new RGB[size];
        for( int i = 0; i < rgbs.length; i++ ) {
          rgbs[i] = new RGB( reds[i] & 0xFF, greens[i] & 0xFF, blues[i] & 0xFF );
        }
        PaletteData palette = new PaletteData( rgbs );
        ImageData data =
            new ImageData( aBufferedImage.getWidth(), aBufferedImage.getHeight(), colorModel.getPixelSize(), palette );
        data.transparentPixel = colorModel.getTransparentPixel();
        WritableRaster raster = aBufferedImage.getRaster();
        int[] pixelArray = new int[1];
        for( int y = 0; y < data.height; y++ ) {
          for( int x = 0; x < data.width; x++ ) {
            raster.getPixel( x, y, pixelArray );
            data.setPixel( x, y, pixelArray[0] );
          }
        }
        return data;
      }
    return null;
  }

  /**
   * Служебный метод, используемый для преобразования изображения из "SWT" в "AWT" представления для случая, когда
   * преобразумое изображение имеет "непосредственную" палитру.
   *
   * @param aData ImageData - "SWT" представление изображения
   * @return BufferedImage - изображение в формате "AWT"
   */
  private static BufferedImage createImageFromDirectPalette( ImageData aData ) {
    ColorModel colorModel = null;
    PaletteData palette = aData.palette;
    int alphaMask = 0xff000000;
    colorModel =
        // new DirectColorModel( aData.depth + 8, palette.redMask, palette.greenMask, palette.blueMask, alphaMask );
        new DirectColorModel( aData.depth, palette.redMask, palette.greenMask, palette.blueMask, alphaMask );
    WritableRaster raster = colorModel.createCompatibleWritableRaster( aData.width, aData.height );
    BufferedImage bufferedImage = new BufferedImage( colorModel, raster, false, null );

    int[] pixelArray = new int[4];
    for( int y = 0; y < aData.height; y++ ) {
      for( int x = 0; x < aData.width; x++ ) {
        int pixel = aData.getPixel( x, y );
        RGB rgb = palette.getRGB( pixel );
        pixelArray[0] = rgb.red;
        pixelArray[1] = rgb.green;
        pixelArray[2] = rgb.blue;
        pixelArray[3] = getAlphaData( aData, x, y ); // aData.getAlpha( x, y );
        raster.setPixels( x, y, 1, 1, pixelArray );
      }
    }
    return bufferedImage;
  }

  /**
   * Сводит различные варианты получения значения прозрачности для конкретного пикселя к одному методу.<br>
   * В стандарнтых snippets'ах эти варианты не учтены.
   */
  @SuppressWarnings( "javadoc" )
  private static int getAlphaData( ImageData aImageData, int aX, int aY ) {
    if( aImageData.transparentPixel != -1 ) {
      return aImageData.transparentPixel;
    }
    return aImageData.getAlpha( aX, aY );
  }

  /**
   * Нельзя создавать экземпляры класса.
   */
  private TsImageUtils() {
    // nop
  }

}

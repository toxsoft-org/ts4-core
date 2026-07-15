package org.toxsoft.core.tsgui.ved.comps.AI;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class ShadowPathSWT {

  // Радиус размытия тени
  private static final int BLUR_RADIUS = 12;
  // Смещение тени
  private static final int SHADOW_OFFSET_X = 8;
  private static final int SHADOW_OFFSET_Y = 8;
  // Цвет тени (R, G, B)
  private static final int SHADOW_R = 0;
  private static final int SHADOW_G = 0;
  private static final int SHADOW_B = 0;
  // Прозрачность тени (0-255)
  private static final int SHADOW_ALPHA = 180;

  public static void main( String[] args ) {
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setText( "SWT Path Shadow with Blur" );
    shell.setSize( 500, 400 );

    shell.addPaintListener( e -> paintScene( e.gc, shell ) );

    shell.open();
    while( !shell.isDisposed() ) {
      if( !display.readAndDispatch() ) {
        display.sleep();
      }
    }
    display.dispose();
  }

  private static void paintScene( GC gc, Shell shell ) {
    Rectangle bounds = shell.getClientArea();
    Display display = shell.getDisplay();

    // 1. Рисуем фигуру на отдельном изображении (маска тени)
    Image shadowMask = new Image( display, bounds.width, bounds.height );
    GC maskGC = new GC( shadowMask );
    maskGC.setBackground( display.getSystemColor( SWT.COLOR_WHITE ) );
    maskGC.fillRectangle( bounds );

    // Создаём Path — звезда
    Path path = createStarPath( display, 200, 160, 100, 45 );

    maskGC.setBackground( display.getSystemColor( SWT.COLOR_BLACK ) );
    maskGC.setAntialias( SWT.ON );
    maskGC.fillPath( path );
    maskGC.dispose();

    // 2. Применяем Gaussian Blur к маске → получаем тень
    ImageData maskData = shadowMask.getImageData();
    shadowMask.dispose();

    int[] alpha = extractLuminanceChannel( maskData );
    int[] blurred = gaussianBlur( alpha, bounds.width, bounds.height, BLUR_RADIUS );

    // 3. Создаём итоговое изображение тени (RGBA)
    Image shadowImage = buildShadowImage( display, bounds.width, bounds.height, blurred );

    // 4. Рисуем фон
    gc.setBackground( display.getSystemColor( SWT.COLOR_WIDGET_BACKGROUND ) );
    gc.fillRectangle( bounds );

    // 5. Рисуем тень со смещением
    gc.setAlpha( 255 );
    gc.drawImage( shadowImage, SHADOW_OFFSET_X, SHADOW_OFFSET_Y );
    shadowImage.dispose();

    // 6. Рисуем саму фигуру поверх тени
    gc.setAntialias( SWT.ON );
    Color fillColor = new Color( display, 70, 130, 180 );
    Color borderColor = new Color( display, 30, 80, 130 );
    gc.setBackground( fillColor );
    gc.setForeground( borderColor );
    gc.setLineWidth( 2 );
    gc.fillPath( path );
    gc.drawPath( path );

    path.dispose();
    fillColor.dispose();
    borderColor.dispose();
  }

  /**
   * Создаёт Path в форме звезды.
   */
  private static Path createStarPath( Display display, float cx, float cy, float outerR, float innerR ) {
    Path path = new Path( display );
    int points = 5;
    for( int i = 0; i < points * 2; i++ ) {
      double angle = Math.PI / points * i - Math.PI / 2;
      float r = (i % 2 == 0) ? outerR : innerR;
      float x = cx + (float)(r * Math.cos( angle ));
      float y = cy + (float)(r * Math.sin( angle ));
      if( i == 0 ) {
        path.moveTo( x, y );
      }
      else {
        path.lineTo( x, y );
      }
    }
    path.close();
    return path;
  }

  /**
   * Извлекает инвертированную яркость как альфа-канал маски. Чёрные пиксели (фигура) → 255, белые (фон) → 0.
   */
  private static int[] extractLuminanceChannel( ImageData data ) {
    int w = data.width;
    int h = data.height;
    int[] alpha = new int[w * h];
    PaletteData palette = data.palette;
    for( int y = 0; y < h; y++ ) {
      for( int x = 0; x < w; x++ ) {
        int pixel = data.getPixel( x, y );
        RGB rgb = palette.getRGB( pixel );
        int lum = (rgb.red + rgb.green + rgb.blue) / 3;
        // Инвертируем: чёрный пиксель = полная тень
        alpha[y * w + x] = 255 - lum;
      }
    }
    return alpha;
  }

  /**
   * Применяет двухпроходный Gaussian Blur к одноканальному массиву.
   */
  private static int[] gaussianBlur( int[] src, int w, int h, int radius ) {
    float[] kernel = makeGaussianKernel( radius );
    int kLen = kernel.length;
    int[] tmp = new int[w * h];
    int[] dst = new int[w * h];

    // Горизонтальный проход
    for( int y = 0; y < h; y++ ) {
      for( int x = 0; x < w; x++ ) {
        float sum = 0;
        for( int k = 0; k < kLen; k++ ) {
          int sx = x + k - radius;
          if( sx < 0 ) {
            sx = 0;
          }
          if( sx >= w ) {
            sx = w - 1;
          }
          sum += src[y * w + sx] * kernel[k];
        }
        tmp[y * w + x] = Math.round( sum );
      }
    }

    // Вертикальный проход
    for( int y = 0; y < h; y++ ) {
      for( int x = 0; x < w; x++ ) {
        float sum = 0;
        for( int k = 0; k < kLen; k++ ) {
          int sy = y + k - radius;
          if( sy < 0 ) {
            sy = 0;
          }
          if( sy >= h ) {
            sy = h - 1;
          }
          sum += tmp[sy * w + x] * kernel[k];
        }
        dst[y * w + x] = Math.round( sum );
      }
    }
    return dst;
  }

  /**
   * Строит нормализованное ядро Гаусса.
   */
  private static float[] makeGaussianKernel( int radius ) {
    int size = radius * 2 + 1;
    float[] kernel = new float[size];
    float sigma = radius / 3.0f;
    float sum = 0;
    for( int i = 0; i < size; i++ ) {
      int x = i - radius;
      kernel[i] = (float)Math.exp( -(x * x) / (2 * sigma * sigma) );
      sum += kernel[i];
    }
    for( int i = 0; i < size; i++ ) {
      kernel[i] /= sum;
    }
    return kernel;
  }

  /**
   * Строит изображение тени из размытого альфа-канала. Использует ImageData с альфа-каналом (RGBA через palette +
   * alphaData).
   */
  private static Image buildShadowImage( Display display, int w, int h, int[] blurred ) {
    ImageData data = new ImageData( w, h, 24, new PaletteData( 0xFF0000, 0x00FF00, 0x0000FF ) );
    byte[] alphaData = new byte[w * h];

    for( int i = 0; i < w * h; i++ ) {
      // Цвет тени
      int r = SHADOW_R;
      int g = SHADOW_G;
      int b = SHADOW_B;
      data.setPixel( i % w, i / w, data.palette.getPixel( new RGB( r, g, b ) ) );
      // Прозрачность = размытая маска * коэффициент
      int a = (int)(blurred[i] * (SHADOW_ALPHA / 255.0f));
      alphaData[i] = (byte)Math.min( 255, a );
    }
    data.alphaData = alphaData;
    return new Image( display, data );
  }
}

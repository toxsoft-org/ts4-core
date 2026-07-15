package org.toxsoft.core.tsgui.ved.comps.AI;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * Утилитный класс — генерирует прозрачное (ARGB) изображение цветной тени для произвольного SWT Path.
 */
public final class ShadowRenderer {

  private ShadowRenderer() {
  }

  /**
   * Создаёт прозрачное изображение цветной тени.
   *
   * @param display текущий Display
   * @param path контур фигуры/текста
   * @param width ширина буфера в пикселях
   * @param height высота буфера в пикселях
   * @param shadowRGB цвет тени
   * @param blurRadius радиус размытия (0 = чёткая маска)
   * @return Image с альфа-каналом; вызывающий код обязан вызвать dispose()
   */
  public static Image createColoredShadow( Display display, Path path, int width, int height, RGB shadowRGB,
      int blurRadius ) {

    if( width <= 0 || height <= 0 ) {
      ImageData empty = new ImageData( 1, 1, 32, new PaletteData( 0xFF0000, 0x00FF00, 0x0000FF ) );
      empty.alphaData = new byte[1];
      return new Image( display, empty );
    }

    // ── 1. Рисуем контур белым на чёрном во временный Image ──────────────
    Image tempImg = new Image( display, width, height );
    GC gc = new GC( tempImg );
    gc.setBackground( display.getSystemColor( SWT.COLOR_BLACK ) );
    gc.fillRectangle( 0, 0, width, height );
    gc.setBackground( display.getSystemColor( SWT.COLOR_WHITE ) );
    gc.setAntialias( SWT.ON );
    gc.fillPath( path );
    gc.dispose();

    ImageData tempData = tempImg.getImageData();
    tempImg.dispose();

    // ── 2. Переносим яркость в массив alphaData ───────────────────────────
    byte[] alpha = new byte[width * height];
    for( int y = 0; y < height; y++ ) {
      for( int x = 0; x < width; x++ ) {
        int pixel = tempData.getPixel( x, y );
        // Изображение ч/б: берём красный канал как яркость
        int r = (pixel & tempData.palette.redMask);
        int redShift = tempData.palette.redShift;
        int brightness = (redShift < 0) ? (r >>> (-redShift)) : (r << redShift);
        alpha[y * width + x] = (byte)(brightness & 0xFF);
      }
    }

    // ── 3. Трёхпроходный box-blur (≈ гауссово размытие) ──────────────────
    if( blurRadius > 0 ) {
      alpha = boxBlurAlpha( alpha, width, height, blurRadius );
    }

    // ── 4. Строим итоговый ARGB ImageData ─────────────────────────────────
    // ВАЖНО: setPixel() обнуляет alphaData затронутых пикселей (баг SWT),
    // поэтому заполняем data[] напрямую.
    // Байтовое смещение каждого канала вычисляем из маски — платформонезависимо.
    ImageData result = new ImageData( width, height, 32, new PaletteData( 0xFF0000, 0x00FF00, 0x0000FF ) );
    result.alphaData = alpha;

    int bpp = result.depth / 8; // 4 байта на пиксель
    int rOff = byteOffset( result.palette.redMask, result.palette.redShift, bpp );
    int gOff = byteOffset( result.palette.greenMask, result.palette.greenShift, bpp );
    int bOff = byteOffset( result.palette.blueMask, result.palette.blueShift, bpp );
    byte rv = (byte)shadowRGB.red;
    byte gv = (byte)shadowRGB.green;
    byte bv = (byte)shadowRGB.blue;

    int bpl = result.bytesPerLine;
    for( int y = 0; y < height; y++ ) {
      for( int x = 0; x < width; x++ ) {
        int base = y * bpl + x * bpp;
        result.data[base + rOff] = rv;
        result.data[base + gOff] = gv;
        result.data[base + bOff] = bv;
      }
    }

    return new Image( display, result );
  }

  /**
   * Возвращает байтовое смещение канала внутри пикселя по его маске и сдвигу. shift — это PaletteData.redShift и т.д.:
   * отрицательный = сдвиг вправо. Для маски 0xFF0000, shift=-16: бит 23..16 → байт 1 (big-endian).
   */
  private static int byteOffset( int mask, int shift, int bpp ) {
    // Находим позицию старшего значимого бита маски
    int msb = 31 - Integer.numberOfLeadingZeros( mask );
    // Байт: (bpp-1) - msb/8 для big-endian packed int
    return (bpp - 1) - (msb / 8);
  }

  // ── Внутренние утилиты ────────────────────────────────────────────────────

  /**
   * Трёхпроходный box-blur по альфа-массиву. 3 прохода box-blur аппроксимируют гауссово ядро.
   */
  private static byte[] boxBlurAlpha( byte[] src, int w, int h, int r ) {
    byte[] tmp = new byte[src.length];
    byte[] dst = src.clone();

    for( int pass = 0; pass < 3; pass++ ) {
      // Горизонтальный проход
      for( int y = 0; y < h; y++ ) {
        int sum = 0;
        for( int x = 0; x < w; x++ ) {
          int xAdd = Math.min( x + r, w - 1 );
          int xSub = Math.max( x - r - 1, -1 );
          sum += (dst[y * w + xAdd] & 0xFF);
          if( xSub >= 0 ) {
            sum -= (dst[y * w + xSub] & 0xFF);
          }
          int count = xAdd - Math.max( 0, x - r ) + 1;
          tmp[y * w + x] = (byte)(sum / count);
        }
      }
      // Вертикальный проход
      for( int x = 0; x < w; x++ ) {
        int sum = 0;
        for( int y = 0; y < h; y++ ) {
          int yAdd = Math.min( y + r, h - 1 );
          int ySub = Math.max( y - r - 1, -1 );
          sum += (tmp[yAdd * w + x] & 0xFF);
          if( ySub >= 0 ) {
            sum -= (tmp[ySub * w + x] & 0xFF);
          }
          int count = yAdd - Math.max( 0, y - r ) + 1;
          dst[y * w + x] = (byte)(sum / count);
        }
      }
    }
    return dst;
  }
}

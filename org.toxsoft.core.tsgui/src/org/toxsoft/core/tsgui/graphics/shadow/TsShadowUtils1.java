package org.toxsoft.core.tsgui.graphics.shadow;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.graphics.path.*;

/**
 * Набор вспомогательных мтодов для рисования тени.
 *
 * @author vs
 */
public class TsShadowUtils1 {

  public static ImageData buildDropShadowData( Path aPath, TsShadowInfo aShadowInfo, Display aDisplay ) {
    Path path = null;

    try {
      float[] bounds = new float[4];
      aPath.getBounds( bounds );

      PathData pd = PathDataUtils.shift( aPath.getPathData(), -bounds[0] + 2 * aShadowInfo.blur() + 15,
          -bounds[1] + 2 * aShadowInfo.blur() + 15 );
      // pd = aPath.getPathData();

      path = new Path( aDisplay, pd );

      int W = Math.round( bounds[2] );
      int H = Math.round( bounds[3] );

      // ── 1. Рисуем силуэт тени во временный Image ─────────────────────
      // Добавляем отступ = blurRadius чтобы blur не обрезался по краям
      int pad = aShadowInfo.blur() * 2;
      int bW = W + pad * 2 + aShadowInfo.blur();
      int bH = H + pad * 2 + aShadowInfo.blur();

      Image silhouette = new Image( aDisplay, bW, bH );
      GC sgc = new GC( silhouette );
      sgc.setAntialias( SWT.ON );
      sgc.setTextAntialias( SWT.ON );

      // Фон буфера — чёрный (0,0,0), силуэт — белый (255,255,255)
      sgc.setBackground( aDisplay.getSystemColor( SWT.COLOR_BLACK ) );
      sgc.fillRectangle( 0, 0, bW, bH );

      sgc.setBackground( aDisplay.getSystemColor( SWT.COLOR_WHITE ) );
      sgc.fillPath( path );
      sgc.dispose();

      // ── 2. Гауссово размытие силуэта ─────────────────────
      float[] kernel = buildGaussianKernel( aShadowInfo.blur() );
      ImageData src = silhouette.getImageData();
      silhouette.dispose();
      ImageData blurred = gaussianBlur( src, kernel );

      // ── 3. Собираем финальное изображение ────────────────

      // Накладываем размытую тень попиксельно с заданной непрозрачностью
      // Используем ImageData напрямую — рисуем только тёмные пиксели
      PaletteData palette = new PaletteData( 0xFF0000, 0x00FF00, 0x0000FF );
      ImageData shadow = new ImageData( W, H, 24, palette );

      int shadowAlpha = aShadowInfo.rgba().alpha;

      // Фон тени = цвет фона (240, 240, 245)
      for( int py = 0; py < H; py++ ) {
        for( int px = 0; px < W; px++ ) {
          int bx = px + pad, by = py + pad;
          if( bx < 0 || bx >= bW || by < 0 || by >= bH ) {
            continue;
          }

          int pixel = blurred.getPixel( bx, by );
          RGB rgb = blurred.palette.getRGB( pixel );
          int grayVal = rgb.red; // силуэт был ч/б

          // Смешиваем цвет тени (0,0,0) с фоном (240,240,245)
          // по значению серого с учётом shadowAlpha
          float t = grayVal / 255f * (shadowAlpha / 255f);
          int r = (int)(240 * (1 - t));
          int g = (int)(240 * (1 - t));
          int b = (int)(245 * (1 - t));

          shadow.setPixel( px, py, palette.getPixel( new RGB( Math.min( 255, Math.max( 0, r ) ),
              Math.min( 255, Math.max( 0, g ) ), Math.min( 255, Math.max( 0, b ) ) ) ) );
        }
      }
      return shadow;
    }
    finally {
      if( path != null ) {
        path.dispose();
      }
    }
  }

  /**
   * Строит ядро Гауссиана, заданного радиуса.
   *
   * @param aRadius int - радиус размытия
   * @return float[] - массив значений ядра
   */
  public static float[] buildGaussianKernel( int aRadius ) {
    int size = aRadius * 2 + 1;
    float[] kernel = new float[size];
    float sigma = aRadius / 3.0f;
    float sum = 0;

    for( int i = 0; i < size; i++ ) {
      int x = i - aRadius;
      float v = (float)Math.exp( -(x * x) / (2 * sigma * sigma) );
      kernel[i] = v;
      sum += v;
    }
    // Нормализация
    for( int i = 0; i < size; i++ ) {
      kernel[i] /= sum;
    }

    return kernel;
  }

  /**
   * Возвращает новые данные изображения, размытые в соответствии с ядром Гаусса.
   *
   * @param aSrc - данные исходного иображения
   * @return ImageData - данные нового изображения
   */
  static ImageData gaussianBlur( ImageData aSrc, float[] aKernel ) {
    int W = aSrc.width, H = aSrc.height;
    // float[] kernel = buildGaussianKernel( aRadius );
    int kSize = aKernel.length;
    int half = kSize / 2;

    // Промежуточный буфер (float для точности)
    float[] tmp = new float[W * H];
    float[] out = new float[W * H];

    // Читаем исходные пиксели в float[]
    float[] srcF = new float[W * H];
    for( int y = 0; y < H; y++ ) {
      for( int x = 0; x < W; x++ ) {
        int pixel = aSrc.getPixel( x, y );
        RGB rgb = aSrc.palette.getRGB( pixel );
        srcF[y * W + x] = rgb.red; // ч/б — берём любой канал
      }
    }

    // Горизонтальный проход
    for( int y = 0; y < H; y++ ) {
      for( int x = 0; x < W; x++ ) {
        float sum = 0;
        for( int k = 0; k < kSize; k++ ) {
          int sx = Math.min( W - 1, Math.max( 0, x + k - half ) );
          sum += srcF[y * W + sx] * aKernel[k];
        }
        tmp[y * W + x] = sum;
      }
    }

    // Вертикальный проход
    for( int y = 0; y < H; y++ ) {
      for( int x = 0; x < W; x++ ) {
        float sum = 0;
        for( int k = 0; k < kSize; k++ ) {
          int sy = Math.min( H - 1, Math.max( 0, y + k - half ) );
          sum += tmp[sy * W + x] * aKernel[k];
        }
        out[y * W + x] = sum;
      }
    }

    // Записываем результат в новый ImageData
    PaletteData palette = new PaletteData( 0xFF0000, 0x00FF00, 0x0000FF );
    ImageData result = new ImageData( W, H, 24, palette );

    for( int y = 0; y < H; y++ ) {
      for( int x = 0; x < W; x++ ) {
        int v = Math.min( 255, Math.max( 0, (int)out[y * W + x] ) );
        result.setPixel( x, y, palette.getPixel( new RGB( v, v, v ) ) );
      }
    }
    return result;
  }

  /**
   * Constructor. Instantions restrictions
   */
  private TsShadowUtils1() {
    // nop
  }
}

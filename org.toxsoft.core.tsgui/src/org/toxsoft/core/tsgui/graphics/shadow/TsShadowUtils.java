package org.toxsoft.core.tsgui.graphics.shadow;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * Набор вспомогательных методов для рисования тени.
 *
 * @author vs, AI
 */
public class TsShadowUtils {

  /**
   * Строит ShadowCache для произвольного Path: 1. Получает bounding box Path через Path.getBounds(). 2. Создаёт
   * временный off-screen Image размером (bbox + 2*pad). 3. Рисует Path в этот Image через GC.fillPath() белым цветом на
   * чёрном фоне. Это единственный корректный способ растеризовать произвольный Path с кривыми Безье, дугами и т.д. 4.
   * Читает яркость пикселей → получает маску 0.0–1.0. 5. Применяет гауссово размытие к маске. 6. Собирает итоговый
   * Image: цвет = shadowColor, alpha = blur * shadowAlpha.
   *
   * @param aPath {@link Path} - контур
   * @param aShadowInfo {@link TsShadowInfo} - параметры тени
   * @param aDevice {@link Device} - устройство
   * @param aEmulation <b>true</b> - если вместо честного "Гаусса" применить эмуляцию
   * @return {@link ImageData} - данные изображения
   */
  public static ImageData buildDropShadowData( Path aPath, TsShadowInfo aShadowInfo, Device aDevice,
      boolean aEmulation ) {
    if( aEmulation ) {
      return buildDropShadowDataEmul( aPath, aShadowInfo, aDevice );
    }
    // ── 1. Bounding box Path ─────────────────────────────────────────────
    float[] pb = new float[4];
    aPath.getBounds( pb );

    int bx = (int)Math.floor( pb[0] );
    int by = (int)Math.floor( pb[1] );
    int bw = (int)Math.ceil( pb[2] ) + 1; // +1 на случай дробной части
    int bh = (int)Math.ceil( pb[3] ) + 1;

    int pad = aShadowInfo.blur();
    int imgW = bw + 2 * pad;
    int imgH = bh + 2 * pad;

    // Смещение: мировые координаты → локальные координаты Image
    // localX = worldX - bx + pad
    int localOffX = -bx + pad;
    int localOffY = -by + pad;

    // ── 2. Off-screen Image для растеризации маски ───────────────────────
    // Используем RGB (24-bit): белый = 1, чёрный = 0.
    // GC рисует Path в локальных координатах Image (со сдвигом).
    ImageData maskImgData = new ImageData( imgW, imgH, 24, new PaletteData( 0xFF0000, 0x00FF00, 0x0000FF ) );
    // Заполняем чёрным (значение по умолчанию = 0)

    Image maskImage = new Image( aDevice, maskImgData );
    GC maskGC = new GC( maskImage );
    maskGC.setAdvanced( true );
    maskGC.setAntialias( SWT.ON );

    // Чёрный фон
    maskGC.setBackground( aDevice.getSystemColor( SWT.COLOR_BLACK ) );
    maskGC.fillRectangle( 0, 0, imgW, imgH );

    // Строим Path в локальных координатах (сдвинутый)
    // Path localPath = buildDemoPath( device, canvasW, canvasH );
    // Применяем Transform: translate(-bx+pad, -by+pad)
    Transform t = new Transform( aDevice );
    t.translate( localOffX, localOffY );
    maskGC.setTransform( t );
    t.dispose();

    // Рисуем белым
    maskGC.setBackground( aDevice.getSystemColor( SWT.COLOR_WHITE ) );
    maskGC.fillPath( aPath );
    // localPath.dispose();

    maskGC.setTransform( null );
    maskGC.dispose();

    // ── 3. Читаем маску из Image ──────────────────────────────────────────
    // Получаем ImageData и читаем яркость каждого пикселя (R-канал достаточен).
    ImageData md = maskImage.getImageData();
    maskImage.dispose();

    float[] mask = new float[imgW * imgH];
    for( int y = 0; y < imgH; y++ ) {
      for( int x = 0; x < imgW; x++ ) {
        int pixel = md.getPixel( x, y );
        // PaletteData(0xFF0000,0x00FF00,0x0000FF): красный в старшем байте
        int r = (pixel >> 16) & 0xFF;
        mask[y * imgW + x] = r / 255f;
      }
    }

    // ── 4. Гауссово размытие ─────────────────────────────────────────────
    float[] kernel = buildGaussianKernel( aShadowInfo.blur() );
    float[] blurred = gaussianBlur( mask, imgW, imgH, kernel );

    // ── 5. Сборка итогового ImageData (32-bit ARGB) ──────────────────────
    ImageData imgData = new ImageData( imgW, imgH, 32, new PaletteData( 0xFF0000, 0x00FF00, 0x0000FF ) );

    RGB shadowColor = aShadowInfo.rgba().rgb;
    int shadowAlpha = aShadowInfo.rgba().alpha;

    int rgbPixel = (shadowColor.red << 16) | (shadowColor.green << 8) | shadowColor.blue;
    byte[] alphaRow = new byte[imgW];

    for( int y = 0; y < imgH; y++ ) {
      for( int x = 0; x < imgW; x++ ) {
        float v = blurred[y * imgW + x];
        // int a = Math.round( v * shadowAlpha * 255 );
        int a = Math.round( v * shadowAlpha );
        if( a < 0 ) {
          a = 0;
        }
        else
          if( a > 255 ) {
            a = 255;
          }

        imgData.setPixel( x, y, rgbPixel );
        alphaRow[x] = (byte)a;
      }
      imgData.setAlphas( 0, y, imgW, alphaRow, 0 );
    }
    return imgData;
  }

  public static ImageData buildDropShadowDataEmul( Path aPath, TsShadowInfo aShadowInfo, Device aDevice ) {

    // ── 1. Bounding box ──────────────────────────────────────────────────
    float[] pb = new float[4];
    aPath.getBounds( pb );

    int bx = (int)Math.floor( pb[0] );
    int by = (int)Math.floor( pb[1] );
    int bw = (int)Math.ceil( pb[2] ) + 1;
    int bh = (int)Math.ceil( pb[3] ) + 1;

    int pad = aShadowInfo.blur();
    int imgW = bw + 2 * pad;
    int imgH = bh + 2 * pad;

    int localOffX = -bx + pad;
    int localOffY = -by + pad;

    // ── 2. Растеризация Path в 8-bit grayscale off-screen Image ──────────
    //
    // 8-bit палитра: 256 оттенков серого.
    // Читать маску из неё значительно быстрее, чем из 24-bit через getPixel(),
    // потому что ImageData.data хранит уже готовые байты индексов (0–255).
    //
    PaletteData grayPalette = buildGrayPalette();
    ImageData maskData = new ImageData( imgW, imgH, 8, grayPalette );
    // По умолчанию все байты = 0 (чёрный)

    Image maskImage = new Image( aDevice, maskData );
    GC maskGC = new GC( maskImage );
    maskGC.setAdvanced( true );
    maskGC.setAntialias( SWT.ON );

    maskGC.setBackground( aDevice.getSystemColor( SWT.COLOR_BLACK ) );
    maskGC.fillRectangle( 0, 0, imgW, imgH );

    // Path localPath = buildDemoPath( device, canvasW, canvasH );
    Transform t = new Transform( aDevice );
    t.translate( localOffX, localOffY );
    maskGC.setTransform( t );
    t.dispose();

    maskGC.setBackground( aDevice.getSystemColor( SWT.COLOR_WHITE ) );
    maskGC.fillPath( aPath );
    // localPath.dispose();

    maskGC.setTransform( null );
    maskGC.dispose();

    // ── 3. Читаем маску как сырые байты ──────────────────────────────────
    //
    // ImageData.data — это byte[] сырых пикселей. Для 8-bit каждый пиксель
    // занимает ровно один байт. Чтение через data[i] в ~10 раз быстрее
    // поштучного getPixel(x, y), который делает много дополнительной работы.
    //
    ImageData md = maskImage.getImageData();
    maskImage.dispose();

    // Переносим в int[] (0–255) для целочисленной арифметики
    int[] mask = new int[imgW * imgH];
    byte[] raw = md.data;
    // bytesPerLine может быть больше imgW из-за выравнивания строк
    int bpl = md.bytesPerLine;
    for( int y = 0; y < imgH; y++ ) {
      int srcBase = y * bpl;
      int dstBase = y * imgW;
      for( int x = 0; x < imgW; x++ ) {
        mask[dstBase + x] = raw[srcBase + x] & 0xFF; // знаковый byte → беззнаковый int
      }
    }

    // ── 4. Box blur × 3 прохода ──────────────────────────────────────────
    //
    // Три прохода box blur с одинаковым радиусом аппроксимируют гаусс
    // со среднеквадратичным отклонением σ ≈ radius * sqrt(1/3).
    // Сложность каждого прохода: O(W·H) — скользящая сумма,
    // не зависит от radius (в отличие от свёртки O(W·H·r)).
    //
    int boxR = Math.max( 1, aShadowInfo.blur() / 2 ); // радиус каждого из трёх box blur
    int[] blurred = mask;
    for( int pass = 0; pass < 3; pass++ ) {
      blurred = boxBlur( blurred, imgW, imgH, boxR );
    }

    // ── 5. Сборка итогового 32-bit ARGB ImageData ────────────────────────
    ImageData imgData = new ImageData( imgW, imgH, 32, new PaletteData( 0xFF0000, 0x00FF00, 0x0000FF ) );

    // Цвет тени — упакован один раз
    RGB shadowColor = aShadowInfo.rgba().rgb;
    int rgbPixel = (shadowColor.red << 16) | (shadowColor.green << 8) | shadowColor.blue;
    // Предвычисляем цвет для всех строк (одно значение)
    int[] rgbRow = new int[imgW];
    java.util.Arrays.fill( rgbRow, rgbPixel );

    byte[] alphaRow = new byte[imgW];

    int shadowAlpha = aShadowInfo.rgba().alpha;
    for( int y = 0; y < imgH; y++ ) {
      int base = y * imgW;
      for( int x = 0; x < imgW; x++ ) {
        // Целочисленная операция: v * shadowAlpha / 255
        // Всё в int, без float
        int a = blurred[base + x] * shadowAlpha / 255;
        alphaRow[x] = (byte)(a > 255 ? 255 : a);
      }
      imgData.setPixels( 0, y, imgW, rgbRow, 0 );
      imgData.setAlphas( 0, y, imgW, alphaRow, 0 );
    }

    return imgData;
    // Image shadowImage = new Image( device, imgData );
    // return new ShadowCache( shadowImage, bx - pad, by - pad );
  }

  // public static ImageData buildInnerShadowData( Path aPath, TsShadowInfo aShadowInfo, Device device ) {
  //
  // // ── 1. BBox фигуры ───────────────────────────────────────────────────
  // float[] pb = new float[4];
  // aPath.getBounds( pb );
  //
  // int bx = (int)Math.floor( pb[0] );
  // int by = (int)Math.floor( pb[1] );
  // int bw = (int)Math.ceil( pb[2] ) + 1;
  // int bh = (int)Math.ceil( pb[3] ) + 1;
  //
  // float k = (Math.max( bw, bh ) + aShadowInfo.blur() * 2.f) / Math.max( bw, bh );
  // k = 1.f;
  // PathData pd = PathDataUtils.scale( aPath.getPathData(), bx + bw / 2.f, by + bh / 2.f, k, k );
  //
  // Path tempPath = new Path( device, pd );
  // tempPath.getBounds( pb );
  //
  // bx = (int)Math.floor( pb[0] );
  // by = (int)Math.floor( pb[1] );
  // bw = (int)Math.ceil( pb[2] ) + 1;
  // bh = (int)Math.ceil( pb[3] ) + 1;
  //
  // int imgW = bw;
  // int imgH = bh;
  //
  // int localOffX = -bx;
  // int localOffY = -by;
  //
  // // ── 2. Растеризуем фигуру: белая на чёрном → shapeMask ───────────────
  // float[] shapeMask = rasterizePath( device, tempPath, imgW, imgH, localOffX, localOffY, false );
  // tempPath.dispose();
  //
  // // ── 3. Строим invertedMask со сдвигом на (+offsetX, +offsetY) ────────
  // // Сдвигаем фигуру В СТОРОНУ тени, а не против —
  // // тогда тёмные зоны собираются у нужного края и дают тень ВНУТРИ контура.
  // int shadowOffsetX = aShadowInfo.xOffset();
  // int shadowOffsetY = aShadowInfo.yOffset();
  // shadowOffsetX = 0;
  // shadowOffsetY = 0;
  // float[] invertedMask = new float[imgW * imgH];
  // for( int y = 0; y < imgH; y++ ) {
  // for( int x = 0; x < imgW; x++ ) {
  // int sx = x + shadowOffsetX; // ← плюс (было минус)
  // int sy = y + shadowOffsetY; // ← плюс (было минус)
  // float shapeVal;
  // if( sx < 0 || sx >= imgW || sy < 0 || sy >= imgH ) {
  // shapeVal = 0f;
  // }
  // else {
  // shapeVal = shapeMask[sy * imgW + sx];
  // }
  // invertedMask[y * imgW + x] = 1.0f - shapeVal;
  // }
  // }
  //
  // // ── 4. Эмуляция гауссова размытия тремя проходами box blur ──────────
  // float[] blurred = boxBlurApprox( invertedMask, imgW, imgH, aShadowInfo.blur() );
  //
  // RGB shadowColor = aShadowInfo.rgba().rgb;
  //
  // // ── 5. Сборка ARGB Image ─────────────────────────────────────────────
  // ImageData imgData = new ImageData( imgW, imgH, 32, new PaletteData( 0xFF0000, 0x00FF00, 0x0000FF ) );
  // int rgbPixel = (shadowColor.red << 16) | (shadowColor.green << 8) | shadowColor.blue;
  // byte[] alphaRow = new byte[imgW];
  //
  // float shadowAlpha = (float)(aShadowInfo.rgba().alpha / 100.);
  // for( int y = 0; y < imgH; y++ ) {
  // for( int x = 0; x < imgW; x++ ) {
  // float v = blurred[y * imgW + x] * shapeMask[y * imgW + x];
  // int a = Math.round( v * shadowAlpha * 255 );
  // a = Math.max( 0, Math.min( 255, a ) );
  // imgData.setPixel( x, y, rgbPixel );
  // alphaRow[x] = (byte)a;
  // }
  // imgData.setAlphas( 0, y, imgW, alphaRow, 0 );
  // }
  // return imgData;
  // // Image shadowImage = new Image( device, imgData );
  // // return new ShadowCache( shadowImage, bx, by );
  // }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  // ════════════════════════════════════════════════════════════════════════
  // Гауссово ядро и разделимое размытие (без изменений)
  // ════════════════════════════════════════════════════════════════════════

  private static float[] buildGaussianKernel( int radius ) {
    int size = 2 * radius + 1;
    float[] k = new float[size];
    double sigma = Math.max( radius / 3.0, 0.5 );
    double twoSigSq = 2 * sigma * sigma;
    double sum = 0;
    for( int i = 0; i < size; i++ ) {
      int d = i - radius;
      k[i] = (float)Math.exp( -(d * d) / twoSigSq );
      sum += k[i];
    }
    for( int i = 0; i < size; i++ ) {
      k[i] /= (float)sum;
    }
    return k;
  }

  private static float[] gaussianBlur( float[] src, int W, int H, float[] kernel ) {
    int r = kernel.length / 2;
    float[] tmp = new float[W * H];
    float[] dst = new float[W * H];

    // Горизонтальный проход
    for( int y = 0; y < H; y++ ) {
      int rowBase = y * W;
      for( int x = 0; x < W; x++ ) {
        float acc = 0;
        for( int k = -r; k <= r; k++ ) {
          int sx = x + k;
          if( sx < 0 ) {
            sx = 0;
          }
          else
            if( sx >= W ) {
              sx = W - 1;
            }
          acc += src[rowBase + sx] * kernel[k + r];
        }
        tmp[rowBase + x] = acc;
      }
    }

    // Вертикальный проход
    for( int y = 0; y < H; y++ ) {
      for( int x = 0; x < W; x++ ) {
        float acc = 0;
        for( int k = -r; k <= r; k++ ) {
          int sy = y + k;
          if( sy < 0 ) {
            sy = 0;
          }
          else
            if( sy >= H ) {
              sy = H - 1;
            }
          acc += tmp[sy * W + x] * kernel[k + r];
        }
        dst[y * W + x] = acc;
      }
    }
    return dst;
  }

  // ════════════════════════════════════════════════════════════════════════
  // Эмуляция гауссова размытия тремя проходами box blur
  // ════════════════════════════════════════════════════════════════════════

  /**
   * Три прохода box blur хорошо аппроксимируют гауссиан. Радиус box blur вычисляется по формуле: sigma = max(radius /
   * 3.0, 0.5) boxR = max(1, round(sigma * sqrt(3))) Сложность O(W * H) на проход — скользящая сумма, не зависит от
   * радиуса.
   */
  private static float[] boxBlurApprox( float[] src, int W, int H, int radius ) {
    double sigma = Math.max( radius / 3.0, 0.5 );
    int boxR = Math.max( 1, (int)Math.round( sigma * Math.sqrt( 3 ) ) );

    float[] a = src.clone();
    float[] b = new float[W * H];

    for( int pass = 0; pass < 3; pass++ ) {
      boxBlurH( a, b, W, H, boxR );
      boxBlurV( b, a, W, H, boxR );
    }
    return a;
  }

  /** Горизонтальный проход box blur со скользящей суммой. */
  private static void boxBlurH( float[] src, float[] dst, int W, int H, int r ) {
    float scale = 1.0f / (2 * r + 1);
    for( int y = 0; y < H; y++ ) {
      int base = y * W;
      // Инициализируем сумму для первого пикселя строки
      float sum = src[base] * (r + 1);
      for( int x = 1; x <= r; x++ ) {
        sum += src[base + Math.min( x, W - 1 )];
      }
      for( int x = 0; x < W; x++ ) {
        sum += src[base + Math.min( x + r, W - 1 )];
        sum -= src[base + Math.max( x - r - 1, 0 )];
        dst[base + x] = sum * scale;
      }
    }
  }

  /** Вертикальный проход box blur со скользящей суммой. */
  private static void boxBlurV( float[] src, float[] dst, int W, int H, int r ) {
    float scale = 1.0f / (2 * r + 1);
    for( int x = 0; x < W; x++ ) {
      // Инициализируем сумму для первого пикселя столбца
      float sum = src[x] * (r + 1);
      for( int y = 1; y <= r; y++ ) {
        sum += src[Math.min( y, H - 1 ) * W + x];
      }
      for( int y = 0; y < H; y++ ) {
        sum += src[Math.min( y + r, H - 1 ) * W + x];
        sum -= src[Math.max( y - r - 1, 0 ) * W + x];
        dst[y * W + x] = sum * scale;
      }
    }
  }

  public static ImageData buildInnerShadowData( Path aPath, TsShadowInfo aShadowInfo, Device device ) {
    // ── 1. BBox фигуры ───────────────────────────────────────────────────
    float[] pb = new float[4];
    aPath.getBounds( pb );
    // path.dispose();

    int bx = (int)Math.floor( pb[0] );
    int by = (int)Math.floor( pb[1] );
    int bw = (int)Math.ceil( pb[2] ) + 1;
    int bh = (int)Math.ceil( pb[3] ) + 1;

    // Паддинг = радиус + макс. смещение: гарантирует пустое пространство
    // вокруг фигуры со всех сторон, иначе у прямоугольника тень не видна
    // у тех краёв, где сдвиг не выходит за пределы bbox.
    int pad = aShadowInfo.blur() + Math.max( Math.abs( aShadowInfo.xOffset() ), Math.abs( aShadowInfo.yOffset() ) );

    int imgW = bw + 2 * pad;
    int imgH = bh + 2 * pad;

    int localOffX = -bx + pad;
    int localOffY = -by + pad;

    // ── 2. Растеризуем фигуру: белая на чёрном → shapeMask ───────────────
    float[] shapeMask = rasterizePath( aPath, device, imgW, imgH, localOffX, localOffY, false );

    int shadowOffsetX = aShadowInfo.xOffset();
    int shadowOffsetY = aShadowInfo.yOffset();

    // ── 3. Строим invertedMask со сдвигом на (+offsetX, +offsetY) ────────
    // Сдвигаем фигуру В СТОРОНУ тени, а не против —
    // тогда тёмные зоны собираются у нужного края и дают тень ВНУТРИ контура.
    float[] invertedMask = new float[imgW * imgH];
    for( int y = 0; y < imgH; y++ ) {
      for( int x = 0; x < imgW; x++ ) {
        int sx = x + shadowOffsetX; // ← плюс (было минус)
        int sy = y + shadowOffsetY; // ← плюс (было минус)
        float shapeVal;
        if( sx < 0 || sx >= imgW || sy < 0 || sy >= imgH ) {
          shapeVal = 0f;
        }
        else {
          shapeVal = shapeMask[sy * imgW + sx];
        }
        invertedMask[y * imgW + x] = 1.0f - shapeVal;
      }
    }

    // ── 4. Гауссово размытие ─────────────────────────────────────────────
    float[] kernel = buildGaussianKernel( aShadowInfo.blur() );
    float[] blurred = gaussianBlur( invertedMask, imgW, imgH, kernel );

    RGB shadowColor = aShadowInfo.rgba().rgb;
    float shadowAlpha = aShadowInfo.rgba().alpha / 255.f;

    // ── 5. Сборка ARGB Image ─────────────────────────────────────────────
    ImageData imgData = new ImageData( imgW, imgH, 32, new PaletteData( 0xFF0000, 0x00FF00, 0x0000FF ) );
    int rgbPixel = (shadowColor.red << 16) | (shadowColor.green << 8) | shadowColor.blue;
    byte[] alphaRow = new byte[imgW];

    for( int y = 0; y < imgH; y++ ) {
      for( int x = 0; x < imgW; x++ ) {
        float v = blurred[y * imgW + x] * shapeMask[y * imgW + x];
        int a = Math.round( v * shadowAlpha * 255 );
        a = Math.max( 0, Math.min( 255, a ) );
        imgData.setPixel( x, y, rgbPixel );
        alphaRow[x] = (byte)a;
      }
      imgData.setAlphas( 0, y, imgW, alphaRow, 0 );
    }
    return imgData;
    // Image shadowImage = new Image( device, imgData );
    // return new ShadowCache( shadowImage, bx, by );
  }

  /**
   * Растеризует Path в off-screen Image и возвращает маску яркости [0.0, 1.0].
   *
   * @param invertColors если true — чёрная фигура на белом; если false — белая на чёрном
   */
  private static float[] rasterizePath( Path aPath, Device device, int imgW, int imgH, int localOffX, int localOffY,
      boolean invertColors ) {
    ImageData maskImgData = new ImageData( imgW, imgH, 24, new PaletteData( 0xFF0000, 0x00FF00, 0x0000FF ) );
    Image maskImage = new Image( device, maskImgData );
    GC maskGC = new GC( maskImage );
    maskGC.setAdvanced( true );
    maskGC.setAntialias( SWT.ON );

    maskGC.setBackground( device.getSystemColor( invertColors ? SWT.COLOR_WHITE : SWT.COLOR_BLACK ) );
    maskGC.fillRectangle( 0, 0, imgW, imgH );

    Path localPath = new Path( device, aPath.getPathData() );
    Transform t = new Transform( device );
    t.translate( localOffX, localOffY );
    maskGC.setTransform( t );
    t.dispose();

    maskGC.setBackground( device.getSystemColor( invertColors ? SWT.COLOR_BLACK : SWT.COLOR_WHITE ) );
    maskGC.fillPath( localPath );
    localPath.dispose();

    maskGC.setTransform( null );
    maskGC.dispose();

    ImageData md = maskImage.getImageData();
    maskImage.dispose();

    float[] mask = new float[imgW * imgH];
    for( int y = 0; y < imgH; y++ ) {
      for( int x = 0; x < imgW; x++ ) {
        int pixel = md.getPixel( x, y );
        int r = (pixel >> 16) & 0xFF;
        mask[y * imgW + x] = r / 255f;
      }
    }
    return mask;
  }

  // ════════════════════════════════════════════════════════════════════════
  // Box blur — скользящая сумма, O(W·H) независимо от радиуса
  // ════════════════════════════════════════════════════════════════════════

  /**
   * Разделимый box blur: горизонтальный проход → вертикальный. Каждый проход использует скользящую сумму: при сдвиге
   * окна на 1 пиксель добавляем новый край и убираем старый. Граничные пиксели — clamp (крайнее значение повторяется).
   */
  private static int[] boxBlur( int[] src, int W, int H, int r ) {
    int[] tmp = new int[W * H];
    int[] dst = new int[W * H];
    int diam = 2 * r + 1;

    // Горизонтальный проход
    for( int y = 0; y < H; y++ ) {
      int rowBase = y * W;

      // Начальная скользящая сумма для x=0: окно [-r..r] с clamp
      int sum = 0;
      for( int k = -r; k <= r; k++ ) {
        int sx = k < 0 ? 0 : (k >= W ? W - 1 : k);
        sum += src[rowBase + sx];
      }
      tmp[rowBase] = sum / diam;

      // Сдвигаем окно вправо
      for( int x = 1; x < W; x++ ) {
        // Убираем левый край (x-1-r), добавляем правый (x+r)
        int removeX = x - 1 - r;
        if( removeX < 0 ) {
          removeX = 0;
        }
        int addX = x + r;
        if( addX >= W ) {
          addX = W - 1;
        }
        sum += src[rowBase + addX] - src[rowBase + removeX];
        tmp[rowBase + x] = sum / diam;
      }
    }

    // Вертикальный проход
    for( int x = 0; x < W; x++ ) {
      // Начальная сумма для y=0
      int sum = 0;
      for( int k = -r; k <= r; k++ ) {
        int sy = k < 0 ? 0 : (k >= H ? H - 1 : k);
        sum += tmp[sy * W + x];
      }
      dst[x] = sum / diam;

      for( int y = 1; y < H; y++ ) {
        int removeY = y - 1 - r;
        if( removeY < 0 ) {
          removeY = 0;
        }
        int addY = y + r;
        if( addY >= H ) {
          addY = H - 1;
        }
        sum += tmp[addY * W + x] - tmp[removeY * W + x];
        dst[y * W + x] = sum / diam;
      }
    }

    return dst;
  }

  // ════════════════════════════════════════════════════════════════════════
  // Вспомогательное
  // ════════════════════════════════════════════════════════════════════════

  /** 256-цветная палитра оттенков серого для 8-bit маски. */
  private static PaletteData buildGrayPalette() {
    RGB[] colors = new RGB[256];
    for( int i = 0; i < 256; i++ ) {
      colors[i] = new RGB( i, i, i );
    }
    return new PaletteData( colors );
  }

  /**
   * Constructor. Instantions restrictions
   */
  private TsShadowUtils() {
    // nop
  }
}

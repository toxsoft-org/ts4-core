package org.toxsoft.core.tsgui.ved.comps.AI;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * SWT — прозрачная цветная гауссова тень от произвольного Path. Оптимизации по сравнению с предыдущей версией: 1. Box
 * blur (3 прохода) вместо гауссовой свёртки. Сложность O(W·H) независимо от радиуса вместо O(W·H·(2r+1)). Три прохода
 * box blur аппроксимируют гаусс с погрешностью <1%. 2. Целочисленная арифметика (int[], 0–255) вместо float[]. 3. Маска
 * читается как сырые байты 8-bit grayscale ImageData вместо поштучного getPixel() из 24-bit RGB.
 */
public class GaussianShadowDemo {

  // ── Параметры тени ───────────────────────────────────────────────────────
  private int shadowOffsetX = 12;
  private int shadowOffsetY = 14;
  private int shadowAlpha   = 185;                   // 0–255 (≈72%)
  private int shadowRadius  = 18;
  private RGB shadowColor   = new RGB( 20, 50, 180 );

  // ── SWT-виджеты ──────────────────────────────────────────────────────────
  private Shell  shell;
  private Canvas canvas;

  private ShadowCache shadowCache;

  // ════════════════════════════════════════════════════════════════════════
  public static void main( String[] args ) {
    new GaussianShadowDemo().run();
  }

  public void run() {
    Display display = new Display();
    shell = new Shell( display, SWT.SHELL_TRIM );
    shell.setText( "Гауссова тень от Path — оптимизированная версия" );
    shell.setLayout( new GridLayout( 1, false ) );

    buildControls();
    buildCanvas();

    shell.setSize( 560, 640 );
    shell.open();

    while( !shell.isDisposed() ) {
      if( !display.readAndDispatch() ) {
        display.sleep();
      }
    }
    disposeShadowCache();
    display.dispose();
  }

  // ════════════════════════════════════════════════════════════════════════
  // Контейнер кешированной тени
  // ════════════════════════════════════════════════════════════════════════

  private static class ShadowCache {

    final Image image;
    final int   originX;
    final int   originY;

    ShadowCache( Image image, int originX, int originY ) {
      this.image = image;
      this.originX = originX;
      this.originY = originY;
    }
  }

  private void disposeShadowCache() {
    if( shadowCache != null ) {
      shadowCache.image.dispose();
      shadowCache = null;
    }
  }

  // ════════════════════════════════════════════════════════════════════════
  // UI
  // ════════════════════════════════════════════════════════════════════════

  private void buildControls() {
    Composite panel = new Composite( shell, SWT.NONE );
    panel.setLayout( new GridLayout( 6, false ) );
    panel.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );

    new Label( panel, SWT.NONE ).setText( "Радиус:" );
    Slider sR = slider( panel, 1, 60, shadowRadius );
    Label lR = new Label( panel, SWT.NONE );
    lR.setText( shadowRadius + "px" );
    sR.addSelectionListener( SelectionListener.widgetSelectedAdapter( e -> {
      shadowRadius = sR.getSelection();
      lR.setText( shadowRadius + "px" );
      disposeShadowCache();
      canvas.redraw();
    } ) );

    new Label( panel, SWT.NONE ).setText( "Alpha:" );
    Slider sA = slider( panel, 0, 100, shadowAlpha * 100 / 255 );
    Label lA = new Label( panel, SWT.NONE );
    lA.setText( shadowAlpha * 100 / 255 + "%" );
    sA.addSelectionListener( SelectionListener.widgetSelectedAdapter( e -> {
      shadowAlpha = sA.getSelection() * 255 / 100;
      lA.setText( sA.getSelection() + "%" );
      disposeShadowCache();
      canvas.redraw();
    } ) );

    new Label( panel, SWT.NONE ).setText( "Offset X:" );
    Slider sOX = slider( panel, -50, 50, shadowOffsetX );
    Label lOX = new Label( panel, SWT.NONE );
    lOX.setText( shadowOffsetX + "px" );
    sOX.addSelectionListener( SelectionListener.widgetSelectedAdapter( e -> {
      shadowOffsetX = sOX.getSelection();
      lOX.setText( shadowOffsetX + "px" );
      canvas.redraw();
    } ) );

    new Label( panel, SWT.NONE ).setText( "Offset Y:" );
    Slider sOY = slider( panel, -50, 50, shadowOffsetY );
    Label lOY = new Label( panel, SWT.NONE );
    lOY.setText( shadowOffsetY + "px" );
    sOY.addSelectionListener( SelectionListener.widgetSelectedAdapter( e -> {
      shadowOffsetY = sOY.getSelection();
      lOY.setText( shadowOffsetY + "px" );
      canvas.redraw();
    } ) );

    new Label( panel, SWT.NONE ).setText( "Цвет:" );
    Button btn = new Button( panel, SWT.PUSH );
    btn.setText( "Выбрать..." );
    new Label( panel, SWT.NONE );
    btn.addSelectionListener( SelectionListener.widgetSelectedAdapter( e -> {
      ColorDialog dlg = new ColorDialog( shell );
      dlg.setRGB( shadowColor );
      RGB chosen = dlg.open();
      if( chosen != null ) {
        shadowColor = chosen;
        disposeShadowCache();
        canvas.redraw();
      }
    } ) );
  }

  private static Slider slider( Composite parent, int min, int max, int val ) {
    Slider s = new Slider( parent, SWT.HORIZONTAL );
    s.setMinimum( min );
    s.setMaximum( max + 1 );
    s.setThumb( 1 );
    s.setSelection( val );
    GridData gd = new GridData( SWT.FILL, SWT.CENTER, true, false );
    gd.minimumWidth = 80;
    s.setLayoutData( gd );
    return s;
  }

  private void buildCanvas() {
    canvas = new Canvas( shell, SWT.NO_BACKGROUND | SWT.DOUBLE_BUFFERED );
    canvas.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
    canvas.addPaintListener( this::onPaint );
    canvas.addControlListener( ControlListener.controlResizedAdapter( e -> {
      disposeShadowCache();
      canvas.redraw();
    } ) );
  }

  // ════════════════════════════════════════════════════════════════════════
  // Рендеринг
  // ════════════════════════════════════════════════════════════════════════

  private void onPaint( PaintEvent e ) {
    GC gc = e.gc;
    Rectangle bounds = canvas.getBounds();
    int W = bounds.width, H = bounds.height;

    drawCheckerBackground( gc, W, H );

    long t0 = System.currentTimeMillis();
    if( shadowCache == null ) {
      shadowCache = buildShadowCache( gc.getDevice(), W, H );
    }
    long dt = System.currentTimeMillis() - t0;

    gc.setAdvanced( true );
    gc.setAlpha( 255 );
    gc.drawImage( shadowCache.image, shadowCache.originX + shadowOffsetX, shadowCache.originY + shadowOffsetY );

    Path path = buildDemoPath( gc.getDevice(), W, H );
    gc.setAntialias( SWT.ON );
    gc.setLineWidth( 2 );
    Color fill = new Color( gc.getDevice(), 220, 232, 255 );
    Color stroke = new Color( gc.getDevice(), 60, 90, 200 );
    gc.setBackground( fill );
    gc.setForeground( stroke );
    gc.fillPath( path );
    gc.drawPath( path );
    fill.dispose();
    stroke.dispose();
    path.dispose();

    Rectangle imgBounds = shadowCache.image.getBounds();
    gc.setForeground( gc.getDevice().getSystemColor( SWT.COLOR_DARK_GRAY ) );
    gc.drawString( String.format( "Shadow Image: %d×%d px  radius=%d  build=%d ms", imgBounds.width, imgBounds.height,
        shadowRadius, dt ), 8, H - 20, true );
  }

  private void drawCheckerBackground( GC gc, int W, int H ) {
    final int CELL = 16;
    Color c1 = new Color( gc.getDevice(), 200, 200, 200 );
    Color c2 = new Color( gc.getDevice(), 240, 240, 240 );
    for( int y = 0; y < H; y += CELL ) {
      for( int x = 0; x < W; x += CELL ) {
        gc.setBackground( ((x / CELL + y / CELL) % 2 == 0) ? c1 : c2 );
        gc.fillRectangle( x, y, CELL, CELL );
      }
    }
    c1.dispose();
    c2.dispose();
  }

  // ════════════════════════════════════════════════════════════════════════
  // Демонстрационный Path с кривыми Безье
  // ════════════════════════════════════════════════════════════════════════

  private Path buildDemoPath( Device device, int W, int H ) {
    double sx = (W - 80) / 320.0;
    double sy = (H - 80) / 220.0;
    int ox = 40, oy = 40;

    Path p = new Path( device );
    p.moveTo( tx( 160, sx, ox ), ty( 20, sy, oy ) );
    p.cubicTo( tx( 230, sx, ox ), ty( 0, sy, oy ), tx( 310, sx, ox ), ty( 30, sy, oy ), tx( 320, sx, ox ),
        ty( 115, sy, oy ) );
    p.quadTo( tx( 330, sx, ox ), ty( 190, sy, oy ), tx( 245, sx, ox ), ty( 210, sy, oy ) );
    p.cubicTo( tx( 200, sx, ox ), ty( 230, sy, oy ), tx( 130, sx, ox ), ty( 220, sy, oy ), tx( 115, sx, ox ),
        ty( 168, sy, oy ) );
    p.quadTo( tx( 70, sx, ox ), ty( 140, sy, oy ), tx( 90, sx, ox ), ty( 105, sy, oy ) );
    p.cubicTo( tx( 80, sx, ox ), ty( 50, sy, oy ), tx( 110, sx, ox ), ty( 10, sy, oy ), tx( 160, sx, ox ),
        ty( 20, sy, oy ) );
    p.close();
    return p;
  }

  private static int tx( double v, double sx, int ox ) {
    return (int)(v * sx) + ox;
  }

  private static int ty( double v, double sy, int oy ) {
    return (int)(v * sy) + oy;
  }

  // ════════════════════════════════════════════════════════════════════════
  // Построение Image тени
  // ════════════════════════════════════════════════════════════════════════

  private ShadowCache buildShadowCache( Device device, int canvasW, int canvasH ) {

    // ── 1. Bounding box ──────────────────────────────────────────────────
    Path path = buildDemoPath( device, canvasW, canvasH );
    float[] pb = new float[4];
    path.getBounds( pb );
    path.dispose();

    int bx = (int)Math.floor( pb[0] );
    int by = (int)Math.floor( pb[1] );
    int bw = (int)Math.ceil( pb[2] ) + 1;
    int bh = (int)Math.ceil( pb[3] ) + 1;

    int pad = shadowRadius;
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

    Image maskImage = new Image( device, maskData );
    GC maskGC = new GC( maskImage );
    maskGC.setAdvanced( true );
    maskGC.setAntialias( SWT.ON );

    maskGC.setBackground( device.getSystemColor( SWT.COLOR_BLACK ) );
    maskGC.fillRectangle( 0, 0, imgW, imgH );

    Path localPath = buildDemoPath( device, canvasW, canvasH );
    Transform t = new Transform( device );
    t.translate( localOffX, localOffY );
    maskGC.setTransform( t );
    t.dispose();

    maskGC.setBackground( device.getSystemColor( SWT.COLOR_WHITE ) );
    maskGC.fillPath( localPath );
    localPath.dispose();

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
    int boxR = Math.max( 1, shadowRadius / 2 ); // радиус каждого из трёх box blur
    int[] blurred = mask;
    for( int pass = 0; pass < 3; pass++ ) {
      blurred = boxBlur( blurred, imgW, imgH, boxR );
    }

    // ── 5. Сборка итогового 32-bit ARGB ImageData ────────────────────────
    ImageData imgData = new ImageData( imgW, imgH, 32, new PaletteData( 0xFF0000, 0x00FF00, 0x0000FF ) );

    // Цвет тени — упакован один раз
    int rgbPixel = (shadowColor.red << 16) | (shadowColor.green << 8) | shadowColor.blue;
    // Предвычисляем цвет для всех строк (одно значение)
    int[] rgbRow = new int[imgW];
    java.util.Arrays.fill( rgbRow, rgbPixel );

    byte[] alphaRow = new byte[imgW];

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

    Image shadowImage = new Image( device, imgData );
    return new ShadowCache( shadowImage, bx - pad, by - pad );
  }

  // ════════════════════════════════════════════════════════════════════════
  // Box blur — скользящая сумма, O(W·H) независимо от радиуса
  // ════════════════════════════════════════════════════════════════════════

  /**
   * Разделимый box blur: горизонтальный проход → вертикальный. Каждый проход использует скользящую сумму: при сдвиге
   * окна на 1 пиксель добавляем новый край и убираем старый. Граничные пиксели — clamp (крайнее значение повторяется).
   */
  private int[] boxBlur( int[] src, int W, int H, int r ) {
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
}

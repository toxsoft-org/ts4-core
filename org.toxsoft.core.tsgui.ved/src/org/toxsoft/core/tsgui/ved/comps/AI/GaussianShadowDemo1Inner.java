package org.toxsoft.core.tsgui.ved.comps.AI;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * SWT — внутренняя гауссова тень (inner shadow) от произвольного Path.
 * <p>
 * Алгоритм:
 * <ol>
 * <li>Растеризуем фигуру в off-screen Image (белая на чёрном) → shapeMask[].</li>
 * <li>Строим invertedMask[] = 1 - shapeMask, но сдвинутую на (+offsetX, +offsetY) — это имитирует направление источника
 * света.</li>
 * <li>Размываем invertedMask гауссовым фильтром.</li>
 * <li>Итоговая alpha[i] = blurred[i] * shapeMask[i] * shadowAlpha — тень видна строго внутри фигуры.</li>
 * <li>Рисуем: сначала заливку фигуры, затем поверх — Image тени с клиппингом по Path.</li>
 * </ol>
 */
public class GaussianShadowDemo1Inner {

  // ── Параметры тени ───────────────────────────────────────────────────────
  private int   shadowOffsetX = 12;
  private int   shadowOffsetY = 14;
  private float shadowAlpha   = 0.72f;
  private int   shadowRadius  = 18;
  private RGB   shadowColor   = new RGB( 20, 50, 180 );

  // ── SWT-виджеты ──────────────────────────────────────────────────────────
  private Shell       shell;
  private Canvas      canvas;
  private ShadowCache shadowCache;

  public static void main( String[] args ) {
    new GaussianShadowDemo1Inner().run();
  }

  public void run() {
    Display display = new Display();
    shell = new Shell( display, SWT.SHELL_TRIM );
    shell.setText( "Внутренняя гауссова тень от Path" );
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
  // Кеш тени
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
    Slider sA = slider( panel, 0, 100, (int)(shadowAlpha * 100) );
    Label lA = new Label( panel, SWT.NONE );
    lA.setText( (int)(shadowAlpha * 100) + "%" );
    sA.addSelectionListener( SelectionListener.widgetSelectedAdapter( e -> {
      shadowAlpha = sA.getSelection() / 100f;
      lA.setText( (int)(shadowAlpha * 100) + "%" );
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
      disposeShadowCache();
      canvas.redraw();
    } ) );

    new Label( panel, SWT.NONE ).setText( "Offset Y:" );
    Slider sOY = slider( panel, -50, 50, shadowOffsetY );
    Label lOY = new Label( panel, SWT.NONE );
    lOY.setText( shadowOffsetY + "px" );
    sOY.addSelectionListener( SelectionListener.widgetSelectedAdapter( e -> {
      shadowOffsetY = sOY.getSelection();
      lOY.setText( shadowOffsetY + "px" );
      disposeShadowCache();
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

    if( shadowCache == null ) {
      shadowCache = buildShadowCache( gc.getDevice(), W, H );
    }

    // 1. Рисуем заливку фигуры
    Path path = buildDemoPath( gc.getDevice(), W, H );
    gc.setAdvanced( true );
    gc.setAntialias( SWT.ON );
    gc.setLineWidth( 2 );
    Color fill = new Color( gc.getDevice(), 220, 232, 255 );
    Color stroke = new Color( gc.getDevice(), 60, 90, 200 );
    gc.setBackground( fill );
    gc.setForeground( stroke );
    // gc.fillPath( path );
    gc.drawPath( path );
    fill.dispose();
    stroke.dispose();
    path.dispose();

    // 2. Рисуем inner shadow поверх заливки с клиппингом по контуру фигуры
    // Path clipPath = buildDemoPath( gc.getDevice(), W, H );
    // gc.setClipping( clipPath );
    // gc.setAdvanced( true );
    // gc.setAlpha( 255 );
    gc.drawImage( shadowCache.image, shadowCache.originX, shadowCache.originY );
    // gc.setClipping( (Path)null );
    // clipPath.dispose();

    Rectangle imgBounds = shadowCache.image.getBounds();
    gc.setForeground( gc.getDevice().getSystemColor( SWT.COLOR_DARK_GRAY ) );
    gc.drawString( String.format( "Shadow Image: %d×%d px  (radius=%d, offset=%d,%d)", imgBounds.width,
        imgBounds.height, shadowRadius, shadowOffsetX, shadowOffsetY ), 8, H - 20, true );
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
  // Path
  // ════════════════════════════════════════════════════════════════════════

  private Path buildDemoPath( Device device, int W, int H ) {
    double sx = (W - 80) / 320.0;
    double sy = (H - 80) / 220.0;
    int ox = 40, oy = 40;
    Path p = new Path( device );
    p.addRectangle( 120, 80, W - 160, H - 160 );
    // p.moveTo( tx( 160, sx, ox ), ty( 20, sy, oy ) );
    // p.cubicTo( tx( 230, sx, ox ), ty( 0, sy, oy ), tx( 310, sx, ox ), ty( 30, sy, oy ), tx( 320, sx, ox ),
    // ty( 115, sy, oy ) );
    // p.quadTo( tx( 330, sx, ox ), ty( 190, sy, oy ), tx( 245, sx, ox ), ty( 210, sy, oy ) );
    // p.cubicTo( tx( 200, sx, ox ), ty( 230, sy, oy ), tx( 130, sx, ox ), ty( 220, sy, oy ), tx( 115, sx, ox ),
    // ty( 168, sy, oy ) );
    // p.quadTo( tx( 70, sx, ox ), ty( 140, sy, oy ), tx( 90, sx, ox ), ty( 105, sy, oy ) );
    // p.cubicTo( tx( 80, sx, ox ), ty( 50, sy, oy ), tx( 110, sx, ox ), ty( 10, sy, oy ), tx( 160, sx, ox ),
    // ty( 20, sy, oy ) );
    // p.close();
    return p;
  }

  private static int tx( double v, double sx, int ox ) {
    return (int)(v * sx) + ox;
  }

  private static int ty( double v, double sy, int oy ) {
    return (int)(v * sy) + oy;
  }

  // ════════════════════════════════════════════════════════════════════════
  // Построение inner shadow
  // ════════════════════════════════════════════════════════════════════════

  private ShadowCache buildShadowCache( Device device, int canvasW, int canvasH ) {
    // ── 1. BBox фигуры ───────────────────────────────────────────────────
    Path path = buildDemoPath( device, canvasW, canvasH );
    float[] pb = new float[4];
    path.getBounds( pb );
    path.dispose();

    int bx = (int)Math.floor( pb[0] );
    int by = (int)Math.floor( pb[1] );
    int bw = (int)Math.ceil( pb[2] ) + 1;
    int bh = (int)Math.ceil( pb[3] ) + 1;

    // Паддинг = радиус + макс. смещение: гарантирует пустое пространство
    // вокруг фигуры со всех сторон, иначе у прямоугольника тень не видна
    // у тех краёв, где сдвиг не выходит за пределы bbox.
    int pad = shadowRadius + Math.max( Math.abs( shadowOffsetX ), Math.abs( shadowOffsetY ) );

    int imgW = bw + 2 * pad;
    int imgH = bh + 2 * pad;

    int localOffX = -bx + pad;
    int localOffY = -by + pad;

    // ── 2. Растеризуем фигуру: белая на чёрном → shapeMask ───────────────
    float[] shapeMask = rasterizePath( device, canvasW, canvasH, imgW, imgH, localOffX, localOffY, false );

    // ── 3. Строим invertedMask со сдвигом на (+offsetX, +offsetY) ────────
    // Сдвигаем фигуру В СТОРОНУ тени, а не против —
    // тогда тёмные зоны собираются у нужного края и дают тень ВНУТРИ контура.
    float[] invertedMask = new float[imgW * imgH];
    for( int y = 0; y < imgH; y++ ) {
      for( int x = 0; x < imgW; x++ ) {
        int sx = x - shadowOffsetX; // ← плюс (было минус)
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
    float[] kernel = buildGaussianKernel( shadowRadius );
    float[] blurred = gaussianBlur( invertedMask, imgW, imgH, kernel );

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

    Image shadowImage = new Image( device, imgData );
    return new ShadowCache( shadowImage, bx - pad, by - pad );
  }

  /**
   * Растеризует Path в off-screen Image и возвращает маску яркости [0.0, 1.0].
   *
   * @param invertColors если true — чёрная фигура на белом; если false — белая на чёрном
   */
  private float[] rasterizePath( Device device, int canvasW, int canvasH, int imgW, int imgH, int localOffX,
      int localOffY, boolean invertColors ) {
    ImageData maskImgData = new ImageData( imgW, imgH, 24, new PaletteData( 0xFF0000, 0x00FF00, 0x0000FF ) );
    Image maskImage = new Image( device, maskImgData );
    GC maskGC = new GC( maskImage );
    maskGC.setAdvanced( true );
    maskGC.setAntialias( SWT.ON );

    maskGC.setBackground( device.getSystemColor( invertColors ? SWT.COLOR_WHITE : SWT.COLOR_BLACK ) );
    maskGC.fillRectangle( 0, 0, imgW, imgH );

    Path localPath = buildDemoPath( device, canvasW, canvasH );
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
  // Гауссово ядро и размытие
  // ════════════════════════════════════════════════════════════════════════

  private float[] buildGaussianKernel( int radius ) {
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

  private float[] gaussianBlur( float[] src, int W, int H, float[] kernel ) {
    int r = kernel.length / 2;
    float[] tmp = new float[W * H];
    float[] dst = new float[W * H];

    for( int y = 0; y < H; y++ ) {
      int rowBase = y * W;
      for( int x = 0; x < W; x++ ) {
        float acc = 0;
        for( int k = -r; k <= r; k++ ) {
          int sx = Math.max( 0, Math.min( W - 1, x + k ) );
          acc += src[rowBase + sx] * kernel[k + r];
        }
        tmp[rowBase + x] = acc;
      }
    }

    for( int y = 0; y < H; y++ ) {
      for( int x = 0; x < W; x++ ) {
        float acc = 0;
        for( int k = -r; k <= r; k++ ) {
          int sy = Math.max( 0, Math.min( H - 1, y + k ) );
          acc += tmp[sy * W + x] * kernel[k + r];
        }
        dst[y * W + x] = acc;
      }
    }
    return dst;
  }
}

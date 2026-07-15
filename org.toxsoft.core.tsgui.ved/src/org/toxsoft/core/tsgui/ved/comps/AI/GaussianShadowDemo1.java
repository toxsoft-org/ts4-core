package org.toxsoft.core.tsgui.ved.comps.AI;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * SWT — прозрачная цветная гауссова тень от произвольного Path (кривые Безье, дуги и т.д.). Ключевые свойства: • Маска
 * тени строится через GC.fillPath() в off-screen Image — поддерживается любой Path. • ImageData тени имеет МИНИМАЛЬНЫЙ
 * размер: ширина = bbox.width + 2*radius высота = bbox.height + 2*radius • Все пиксели вне тени имеют alpha=0
 * (полностью прозрачны). • Координаты отрисовки тени: drawX = bbox.x - radius + shadowOffsetX drawY = bbox.y - radius +
 * shadowOffsetY Зависимости: только SWT (org.eclipse.swt).
 */
public class GaussianShadowDemo1 {

  // ── Параметры тени ───────────────────────────────────────────────────────
  private int   shadowOffsetX = 12;
  private int   shadowOffsetY = 14;
  private float shadowAlpha   = 0.72f;                 // 0.0–1.0
  private int   shadowRadius  = 18;
  private RGB   shadowColor   = new RGB( 20, 50, 180 );

  // ── SWT-виджеты ──────────────────────────────────────────────────────────
  private Shell  shell;
  private Canvas canvas;

  /**
   * Кешированные данные тени. Пересчитываются только при изменении Path или shadowRadius/shadowColor/shadowAlpha.
   */
  private ShadowCache shadowCache;

  // ════════════════════════════════════════════════════════════════════════
  public static void main( String[] args ) {
    new GaussianShadowDemo1().run();
  }

  public void run() {
    Display display = new Display();
    shell = new Shell( display, SWT.SHELL_TRIM );
    shell.setText( "Гауссова тень от Path — минимальный прозрачный Image" );
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
    /** Левый верхний угол Image тени в координатах canvas (без учёта offset). */
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
  // UI — панель управления
  // ════════════════════════════════════════════════════════════════════════

  private void buildControls() {
    Composite panel = new Composite( shell, SWT.NONE );
    panel.setLayout( new GridLayout( 6, false ) );
    panel.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );

    // Радиус
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

    // Alpha
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

    // Offset X
    new Label( panel, SWT.NONE ).setText( "Offset X:" );
    Slider sOX = slider( panel, -50, 50, shadowOffsetX );
    Label lOX = new Label( panel, SWT.NONE );
    lOX.setText( shadowOffsetX + "px" );
    sOX.addSelectionListener( SelectionListener.widgetSelectedAdapter( e -> {
      shadowOffsetX = sOX.getSelection();
      lOX.setText( shadowOffsetX + "px" );
      canvas.redraw(); // Image не пересчитываем — только позиция меняется
    } ) );

    // Offset Y
    new Label( panel, SWT.NONE ).setText( "Offset Y:" );
    Slider sOY = slider( panel, -50, 50, shadowOffsetY );
    Label lOY = new Label( panel, SWT.NONE );
    lOY.setText( shadowOffsetY + "px" );
    sOY.addSelectionListener( SelectionListener.widgetSelectedAdapter( e -> {
      shadowOffsetY = sOY.getSelection();
      lOY.setText( shadowOffsetY + "px" );
      canvas.redraw();
    } ) );

    // Цвет
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
    s.setMaximum( max + 1 ); // +1 для thumb
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

    // ── Шахматный фон — наглядно доказывает прозрачность ─────────────────
    drawCheckerBackground( gc, W, H );

    // ── Строим/кешируем тень ─────────────────────────────────────────────
    if( shadowCache == null ) {
      shadowCache = buildShadowCache( gc.getDevice(), W, H );
    }

    // ── Рисуем тень ──────────────────────────────────────────────────────
    gc.setAdvanced( true );
    gc.setAlpha( 255 ); // alpha управляется через ImageData
    gc.drawImage( shadowCache.image, shadowCache.originX + shadowOffsetX, shadowCache.originY + shadowOffsetY );

    // ── Рисуем фигуру поверх тени ─────────────────────────────────────────
    Path path = buildDemoPath( gc.getDevice(), W, H );
    gc.setAdvanced( true );
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

    // ── Отладочная подпись ────────────────────────────────────────────────
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
  // Демонстрационный Path: скруглённая фигура с кривыми Безье
  // ════════════════════════════════════════════════════════════════════════

  /**
   * Строит произвольный демонстрационный Path в координатах canvas. Заменяет SHAPE_TEMPLATE из полигон-версии — теперь
   * используются cubicTo/quadTo, что невозможно растеризовать ray casting-ом. Метод вызывается как при отрисовке
   * фигуры, так и при построении маски.
   */
  private Path buildDemoPath( Device device, int W, int H ) {
    // Масштабируем под canvas, оставляя поля
    double sx = (W - 80) / 320.0;
    double sy = (H - 80) / 220.0;
    int ox = 40, oy = 40; // смещение начала координат шаблона

    // Вспомогательная лямбда-подобная функция через локальные переменные
    // для перевода координат шаблона → canvas
    // tx(v) = (int)(v * sx) + ox; ty(v) = (int)(v * sy) + oy

    Path p = new Path( device );

    // Начало — верхняя точка
    p.moveTo( tx( 160, sx, ox ), ty( 20, sy, oy ) );

    // Правая верхняя дуга (cubicBezier)
    p.cubicTo( tx( 230, sx, ox ), ty( 0, sy, oy ), // cp1
        tx( 310, sx, ox ), ty( 30, sy, oy ), // cp2
        tx( 320, sx, ox ), ty( 115, sy, oy ) // end
    );

    // Правая нижняя часть (квадратичная кривая)
    p.quadTo( tx( 330, sx, ox ), ty( 190, sy, oy ), // cp
        tx( 245, sx, ox ), ty( 210, sy, oy ) // end
    );

    // Нижняя часть — снова кубик
    p.cubicTo( tx( 200, sx, ox ), ty( 230, sy, oy ), // cp1
        tx( 130, sx, ox ), ty( 220, sy, oy ), // cp2
        tx( 115, sx, ox ), ty( 168, sy, oy ) // end
    );

    // Левая нижняя — квадратичная
    p.quadTo( tx( 70, sx, ox ), ty( 140, sy, oy ), // cp
        tx( 90, sx, ox ), ty( 105, sy, oy ) // end
    );

    // Левая верхняя — кубик обратно к началу
    p.cubicTo( tx( 80, sx, ox ), ty( 50, sy, oy ), // cp1
        tx( 110, sx, ox ), ty( 10, sy, oy ), // cp2
        tx( 160, sx, ox ), ty( 20, sy, oy ) // end (замыкаем)
    );

    p.close();
    return p;
  }

  /** Масштабирование X-координаты шаблона → canvas. */
  private static int tx( double v, double sx, int ox ) {
    return (int)(v * sx) + ox;
  }

  /** Масштабирование Y-координаты шаблона → canvas. */
  private static int ty( double v, double sy, int oy ) {
    return (int)(v * sy) + oy;
  }

  // ════════════════════════════════════════════════════════════════════════
  // Построение минимального прозрачного Image тени
  // ════════════════════════════════════════════════════════════════════════

  /**
   * Строит ShadowCache для произвольного Path: 1. Получает bounding box Path через Path.getBounds(). 2. Создаёт
   * временный off-screen Image размером (bbox + 2*pad). 3. Рисует Path в этот Image через GC.fillPath() белым цветом на
   * чёрном фоне. Это единственный корректный способ растеризовать произвольный Path с кривыми Безье, дугами и т.д. 4.
   * Читает яркость пикселей → получает маску 0.0–1.0. 5. Применяет гауссово размытие к маске. 6. Собирает итоговый
   * Image: цвет = shadowColor, alpha = blur * shadowAlpha.
   */
  private ShadowCache buildShadowCache( Device device, int canvasW, int canvasH ) {
    // ── 1. Bounding box Path ─────────────────────────────────────────────
    // Path.getBounds() возвращает float[]{x, y, w, h}
    Path path = buildDemoPath( device, canvasW, canvasH );
    float[] pb = new float[4];
    path.getBounds( pb );
    path.dispose();

    int bx = (int)Math.floor( pb[0] );
    int by = (int)Math.floor( pb[1] );
    int bw = (int)Math.ceil( pb[2] ) + 1; // +1 на случай дробной части
    int bh = (int)Math.ceil( pb[3] ) + 1;

    int pad = shadowRadius;
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

    Image maskImage = new Image( device, maskImgData );
    GC maskGC = new GC( maskImage );
    maskGC.setAdvanced( true );
    maskGC.setAntialias( SWT.ON );

    // Чёрный фон
    maskGC.setBackground( device.getSystemColor( SWT.COLOR_BLACK ) );
    maskGC.fillRectangle( 0, 0, imgW, imgH );

    // Строим Path в локальных координатах (сдвинутый)
    Path localPath = buildDemoPath( device, canvasW, canvasH );
    // Применяем Transform: translate(-bx+pad, -by+pad)
    Transform t = new Transform( device );
    t.translate( localOffX, localOffY );
    maskGC.setTransform( t );
    t.dispose();

    // Рисуем белым
    maskGC.setBackground( device.getSystemColor( SWT.COLOR_WHITE ) );
    maskGC.fillPath( localPath );
    localPath.dispose();

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
    float[] kernel = buildGaussianKernel( shadowRadius );
    float[] blurred = gaussianBlur( mask, imgW, imgH, kernel );

    // ── 5. Сборка итогового ImageData (32-bit ARGB) ──────────────────────
    ImageData imgData = new ImageData( imgW, imgH, 32, new PaletteData( 0xFF0000, 0x00FF00, 0x0000FF ) );

    int rgbPixel = (shadowColor.red << 16) | (shadowColor.green << 8) | shadowColor.blue;
    byte[] alphaRow = new byte[imgW];

    for( int y = 0; y < imgH; y++ ) {
      for( int x = 0; x < imgW; x++ ) {
        float v = blurred[y * imgW + x];
        int a = Math.round( v * shadowAlpha * 255 );
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

    Image shadowImage = new Image( device, imgData );

    // ── 6. Позиция Image на canvas (без учёта shadowOffset) ──────────────
    int originX = bx - pad;
    int originY = by - pad;

    return new ShadowCache( shadowImage, originX, originY );
  }

  // ════════════════════════════════════════════════════════════════════════
  // Гауссово ядро и разделимое размытие (без изменений)
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
}

package org.toxsoft.core.tsgui.ved.comps.AI;

import java.io.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * FlatSteelRingSWT Два режима отрисовки плоского стального кольца: А) КОНИЧЕСКИЙ ГРАДИЕНТ — реализован попиксельно
 * через atan2. Угол φ каждого пикселя переводится в позицию на палитре стали. Добавляются: мягкое кольцевое затемнение
 * краёв + узкий highlight. Б) ТЕКСТУРА — загружаем произвольный PNG/JPEG файл (путь задаётся кнопкой «Открыть…»).
 * Текстура проецируется на кольцо через маскирование ImageData (alpha = 0 вне кольца). Поддерживается поворот и масштаб
 * текстуры.
 */
public class FlatSteelCircleSWT {

  // ── Геометрия ────────────────────────────────────────────────────
  private static int outerRadius = 140;

  // ── Конический градиент ──────────────────────────────────────────
  private static int rotationDeg    = 0;  // поворот градиента
  private static int highlightWidth = 28; // ширина яркой полосы (°)
  private static int darkWidth      = 22; // ширина тёмной полосы (°)
  private static int brightness     = 50; // общая яркость 0–100
  private static int contrast       = 60; // контраст 0–100

  // ── Текстура ────────────────────────────────────────────────────
  private static String    texturePath = null;
  private static ImageData textureData = null;
  private static int       texRotation = 0;   // поворот текстуры (°)
  private static int       texScale    = 100; // масштаб 50–300 %

  // ── Режим ───────────────────────────────────────────────────────
  private static boolean useTexture = false;

  // ── Кэш ─────────────────────────────────────────────────────────
  private static ImageData cachedData = null;
  private static boolean   dirty      = true;

  public static void main( String[] args ) {
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setText( "Flat Steel Disc — SWT" );
    shell.setLayout( new GridLayout( 1, false ) );

    // ── Холст ──────────────────────────────────────────────────
    Canvas canvas = new Canvas( shell, SWT.DOUBLE_BUFFERED | SWT.NO_BACKGROUND );
    GridData cgd = new GridData( SWT.FILL, SWT.FILL, true, true );
    cgd.widthHint = 580;
    cgd.heightHint = 460;
    canvas.setLayoutData( cgd );
    canvas.addPaintListener( e -> paintScene( e.gc, canvas ) );

    // ── Переключатель режима ────────────────────────────────────
    Group modeGroup = new Group( shell, SWT.NONE );
    modeGroup.setText( "Режим" );
    modeGroup.setLayout( new RowLayout( SWT.HORIZONTAL ) );
    modeGroup.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );

    Button btnConic = new Button( modeGroup, SWT.RADIO );
    btnConic.setText( "Конический градиент" );
    btnConic.setSelection( true );

    Button btnTex = new Button( modeGroup, SWT.RADIO );
    btnTex.setText( "Текстура" );

    // Кнопка загрузки файла текстуры
    Button openBtn = new Button( modeGroup, SWT.PUSH );
    openBtn.setText( "Открыть текстуру…" );
    Label texLabel = new Label( modeGroup, SWT.NONE );
    texLabel.setText( "(файл не выбран)" );

    openBtn.addListener( SWT.Selection, e -> {
      FileDialog fd = new FileDialog( shell, SWT.OPEN );
      fd.setText( "Выберите текстуру" );
      fd.setFilterExtensions( new String[] { "*.png;*.jpg;*.jpeg;*.bmp", "*.*" } );
      fd.setFilterNames( new String[] { "Изображения", "Все файлы" } );
      String path = fd.open();
      if( path != null ) {
        try {
          textureData = new ImageData( path );
          texturePath = path;
          texLabel.setText( new File( path ).getName() );
          modeGroup.layout();
          useTexture = true;
          btnConic.setSelection( false );
          btnTex.setSelection( true );
          dirty = true;
          canvas.redraw();
        }
        catch( Exception ex ) {
          MessageBox mb = new MessageBox( shell, SWT.ICON_ERROR );
          mb.setMessage( "Не удалось загрузить изображение:\n" + ex.getMessage() );
          mb.open();
        }
      }
    } );

    btnConic.addListener( SWT.Selection, ev -> {
      if( ((Button)ev.widget).getSelection() ) {
        useTexture = false;
        dirty = true;
        canvas.redraw();
      }
    } );
    btnTex.addListener( SWT.Selection, ev -> {
      if( ((Button)ev.widget).getSelection() ) {
        useTexture = true;
        dirty = true;
        canvas.redraw();
      }
    } );

    // ── Параметры конического градиента ────────────────────────
    Group conicGroup = new Group( shell, SWT.NONE );
    conicGroup.setText( "Конический градиент" );
    conicGroup.setLayout( new GridLayout( 6, false ) );
    conicGroup.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );

    addSlider( conicGroup, canvas, "Радиус", 40, 200, outerRadius, v -> {
      outerRadius = v;
      dirty = true;
    } );
    addSlider( conicGroup, canvas, "Поворот", 0, 359, rotationDeg, v -> {
      rotationDeg = v;
      dirty = true;
    } );
    addSlider( conicGroup, canvas, "Блик (°)", 2, 90, highlightWidth, v -> {
      highlightWidth = v;
      dirty = true;
    } );
    addSlider( conicGroup, canvas, "Тень (°)", 2, 90, darkWidth, v -> {
      darkWidth = v;
      dirty = true;
    } );
    addSlider( conicGroup, canvas, "Яркость", 0, 100, brightness, v -> {
      brightness = v;
      dirty = true;
    } );
    addSlider( conicGroup, canvas, "Контраст", 0, 100, contrast, v -> {
      contrast = v;
      dirty = true;
    } );

    // ── Параметры текстуры ──────────────────────────────────────
    Group texGroup = new Group( shell, SWT.NONE );
    texGroup.setText( "Текстура" );
    texGroup.setLayout( new GridLayout( 4, false ) );
    texGroup.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );

    addSlider( texGroup, canvas, "Поворот тек-ры", 0, 359, texRotation, v -> {
      texRotation = v;
      dirty = true;
    } );
    addSlider( texGroup, canvas, "Масштаб %", 30, 300, texScale, v -> {
      texScale = v;
      dirty = true;
    } );

    shell.pack();
    shell.open();
    while( !shell.isDisposed() ) {
      if( !display.readAndDispatch() ) {
        display.sleep();
      }
    }
    display.dispose();
  }

  // ════════════════════════════════════════════════════════════════
  // Отрисовка
  // ════════════════════════════════════════════════════════════════
  private static void paintScene( GC gc, Canvas canvas ) {
    Display display = canvas.getDisplay();
    Rectangle b = canvas.getClientArea();

    Color bg = new Color( display, 18, 18, 22 );
    gc.setBackground( bg );
    gc.fillRectangle( b );
    bg.dispose();

    int cx = b.width / 2;
    int cy = b.height / 2;
    int size = outerRadius * 2 + 4;

    if( dirty ) {
      cachedData = useTexture && textureData != null ? generateTextureRing( size, size, outerRadius )
          : generateConicRing( size, size, outerRadius );
      dirty = false;
    }

    Image img = new Image( display, cachedData );
    gc.drawImage( img, cx - outerRadius - 2, cy - outerRadius - 2 );
    img.dispose();

    // Подпись
    gc.setForeground( display.getSystemColor( SWT.COLOR_DARK_GRAY ) );
    Font font = new Font( display, new FontData( "Tahoma", 10, SWT.NORMAL ) );
    gc.setFont( font );
    String mode = useTexture ? "Текстура: " + (texturePath != null ? new File( texturePath ).getName() : "—")
        : "Конический градиент";
    Point ts = gc.textExtent( mode );
    gc.drawText( mode, cx - ts.x / 2, cy + outerRadius + 14, true );
    font.dispose();
  }

  // ════════════════════════════════════════════════════════════════
  // А) КОНИЧЕСКИЙ ГРАДИЕНТ
  // ════════════════════════════════════════════════════════════════
  private static ImageData generateConicRing( int w, int h, int outerR ) {
    ImageData data = newImageData( w, h );
    float cx = w / 2f, cy = h / 2f;
    double rotRad = Math.toRadians( rotationDeg );

    // Нормированные параметры
    float brightF = 0.4f + brightness / 100f * 1.0f; // 0.4..1.4
    float contrastF = 0.5f + contrast / 100f * 2.5f; // 0.5..3.0

    // Угловые пороги (в радианах)
    float hiHalf = (float)Math.toRadians( highlightWidth / 2.0 );
    float darkHalf = (float)Math.toRadians( darkWidth / 2.0 );

    for( int y = 0; y < h; y++ ) {
      for( int x = 0; x < w; x++ ) {
        float dx = x - cx, dy = y - cy;
        float dist = (float)Math.sqrt( dx * dx + dy * dy );
        if( dist > outerR ) {
          continue;
        }

        // Угол пикселя относительно поворота градиента
        double phi = Math.atan2( dy, dx ) - rotRad;
        // Нормируем к [0, 2π)
        phi = ((phi % (2 * Math.PI)) + 2 * Math.PI) % (2 * Math.PI);
        // Переводим в [-π, π] для симметрии
        if( phi > Math.PI ) {
          phi -= 2 * Math.PI;
        }
        float a = (float)phi;

        // ── Базовая стальная палитра по углу φ ─────────────
        // Сталь имеет несколько переходов по окружности:
        // главный блик (~0°), средний тон, тень (~180°)

        float steelVal = steelConicValue( a, hiHalf, darkHalf );

        // Применяем яркость и контраст
        steelVal = (steelVal - 0.5f) * contrastF + 0.5f;
        steelVal = steelVal * brightF;
        steelVal = Math.max( 0f, Math.min( 1f, steelVal ) );

        // ── Тонкое затемнение по краю круга (rim) ───────────
        float rim = 1f - (float)Math.pow( dist / outerR, 6f ) * 0.20f;
        steelVal *= rim;

        // ── Цвет стали: почти нейтральный, чуть синеватый ───
        int r = clamp( steelVal * 195 );
        int g = clamp( steelVal * 200 );
        int b = clamp( steelVal * 215 );

        setPixel( data, x, y, r, g, b );
      }
    }
    return data;
  }

  /**
   * Функция формирования значения конического градиента стали. Возвращает [0..1]: 1 = яркий блик, 0 = глубокая тень.
   * Логика (по аналогии с реальной полированной сталью): – Главный блик (a ≈ 0) – Плавный переход к средней яркости –
   * Тёмная полоса (a ≈ ±π) — затенённая сторона – Вторичный блик (~2/3 оборота) — отражение окружающей среды
   */
  private static float steelConicValue( float a, float hiHalf, float darkHalf ) {

    // Главный блик — Гаусс вокруг a=0
    float hiGauss = (float)Math.exp( -(a * a) / (2f * hiHalf * hiHalf) );

    // Тёмная полоса — Гаусс вокруг a = ±π
    float aMirror = Math.abs( Math.abs( a ) - (float)Math.PI );
    float darkGauss = (float)Math.exp( -(aMirror * aMirror) / (2f * darkHalf * darkHalf) );

    // Вторичный слабый блик (~120° от главного)
    float a2 = a - (float)(2 * Math.PI / 3);
    float hi2Gauss = 0.35f * (float)Math.exp( -(a2 * a2) / (2f * hiHalf * hiHalf * 2f) );

    // Плавная «синусоидальная» база имитирует мягкие переходы металла
    float base = 0.55f + 0.18f * (float)Math.cos( a ) + 0.07f * (float)Math.cos( 2 * a );

    return base + 0.45f * hiGauss + 0.20f * hi2Gauss - 0.35f * darkGauss;
  }

  // ════════════════════════════════════════════════════════════════
  // Б) ТЕКСТУРА
  // ════════════════════════════════════════════════════════════════
  private static ImageData generateTextureRing( int w, int h, int outerR ) {

    ImageData data = newImageData( w, h );
    float cx = w / 2f, cy = h / 2f;

    int tw = textureData.width;
    int th = textureData.height;

    double rotRad = Math.toRadians( texRotation );
    float scaleF = texScale / 100f;
    float cosR = (float)Math.cos( rotRad );
    float sinR = (float)Math.sin( rotRad );

    for( int y = 0; y < h; y++ ) {
      for( int x = 0; x < w; x++ ) {
        float dx = x - cx, dy = y - cy;
        float dist = (float)Math.sqrt( dx * dx + dy * dy );
        if( dist > outerR ) {
          continue;
        }

        // Трансформируем координаты в пространство текстуры
        float tx_f = dx * cosR + dy * sinR;
        float ty_f = -dx * sinR + dy * cosR;

        // Масштабирование: растягиваем так, чтобы диаметр = текстура
        float nx = (tx_f / (outerR * scaleF) + 1f) * 0.5f; // 0..1
        float ny = (ty_f / (outerR * scaleF) + 1f) * 0.5f;

        // Тайлинг (wrap)
        int txPx = Math.floorMod( (int)(nx * tw), tw );
        int tyPx = Math.floorMod( (int)(ny * th), th );

        // Билинейная интерполяция
        int[] rgb = sampleBilinear( textureData, nx * tw, ny * th );

        // Тонкое затемнение по краю круга
        float rim = 1f - (float)Math.pow( dist / outerR, 6f ) * 0.20f;

        setPixel( data, x, y, clamp( rgb[0] * rim ), clamp( rgb[1] * rim ), clamp( rgb[2] * rim ) );
      }
    }
    return data;
  }

  /** Билинейная выборка из ImageData (с тайлингом). */
  private static int[] sampleBilinear( ImageData img, float fx, float fy ) {
    int w = img.width, h = img.height;
    int x0 = Math.floorMod( (int)fx, w );
    int x1 = Math.floorMod( (int)fx + 1, w );
    int y0 = Math.floorMod( (int)fy, h );
    int y1 = Math.floorMod( (int)fy + 1, h );
    float u = fx - (float)Math.floor( fx );
    float v = fy - (float)Math.floor( fy );

    int[] c00 = getRGB( img, x0, y0 );
    int[] c10 = getRGB( img, x1, y0 );
    int[] c01 = getRGB( img, x0, y1 );
    int[] c11 = getRGB( img, x1, y1 );

    int[] out = new int[3];
    for( int i = 0; i < 3; i++ ) {
      out[i] = (int)(c00[i] * (1 - u) * (1 - v) + c10[i] * u * (1 - v) + c01[i] * (1 - u) * v + c11[i] * u * v);
    }
    return out;
  }

  private static int[] getRGB( ImageData img, int x, int y ) {
    int pixel = img.getPixel( x, y );
    RGB rgb = img.palette.getRGB( pixel );
    return new int[] { rgb.red, rgb.green, rgb.blue };
  }

  // ════════════════════════════════════════════════════════════════
  // Утилиты
  // ════════════════════════════════════════════════════════════════
  private static ImageData newImageData( int w, int h ) {
    ImageData d = new ImageData( w, h, 32, new PaletteData( 0x00FF0000, 0x0000FF00, 0x000000FF ) );
    d.alphaData = new byte[w * h];
    return d;
  }

  private static void setPixel( ImageData d, int x, int y, int r, int g, int b ) {
    d.setPixel( x, y, (r << 16) | (g << 8) | b );
    d.alphaData[y * d.width + x] = (byte)255;
  }

  private static int clamp( float v ) {
    return Math.max( 0, Math.min( 255, (int)v ) );
  }

  // ════════════════════════════════════════════════════════════════
  // UI slider
  // ════════════════════════════════════════════════════════════════
  @FunctionalInterface
  interface IntConsumer {

    void accept( int v );
  }

  private static void addSlider( Composite parent, Canvas canvas, String label, int min, int max, int initial,
      IntConsumer onChanged ) {
    Composite col = new Composite( parent, SWT.NONE );
    col.setLayout( new GridLayout( 1, false ) );

    Label lbl = new Label( col, SWT.CENTER );
    lbl.setLayoutData( new GridData( SWT.CENTER, SWT.CENTER, true, false ) );
    lbl.setText( label + ": " + initial );

    Slider slider = new Slider( col, SWT.HORIZONTAL );
    GridData sd = new GridData( SWT.FILL, SWT.CENTER, true, false );
    sd.widthHint = 130;
    slider.setLayoutData( sd );
    slider.setMinimum( min );
    slider.setMaximum( max + slider.getThumb() );
    slider.setSelection( initial );

    slider.addListener( SWT.Selection, e -> {
      int val = slider.getSelection();
      lbl.setText( label + ": " + val );
      onChanged.accept( val );
      canvas.redraw();
    } );
  }
}

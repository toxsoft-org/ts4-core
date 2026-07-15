package org.toxsoft.core.tsgui.ved.comps.AI;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * BrushedSteelRingSWT — кольцо из полированной стали с радиальными анизотропными рисками (brushed radial / токарная
 * обработка). Алгоритм: 1. Для каждого пикселя внутри кольца вычисляем угол φ = atan2(dy, dx). 2. Вдоль этого угла
 * усредняем несколько смещённых значений шума — это имитирует размытие вдоль направления полировки (анизотропное). 3.
 * Накладываем физическое освещение (Phong): диффуз + specular + rim. 4. Добавляем тонкие кольцевые полосы
 * (концентрические риски) для глубины.
 */
public class RadialBrushedSteelRingSWT {

  // ── Параметры кольца ──────────────────────────────────────────────
  private static int outerRadius = 130;
  private static int ringThick   = 52; // outerRadius - innerRadius

  // ── Освещение ─────────────────────────────────────────────────────
  private static int lightAngleDeg = 130; // угол источника (°)
  private static int shininess     = 72;  // блеск 0–100

  // ── Параметры рисок ───────────────────────────────────────────────
  private static int   scratches    = 180;   // кол-во угловых рисок
  private static int   blurSamples  = 24;    // сколько отсчётов усреднять
  private static float scratchDepth = 0.55f; // глубина риски 0..1

  public static void main( String[] args ) {
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setText( "Brushed Steel Ring — SWT" );
    shell.setLayout( new GridLayout( 1, false ) );

    // ── Холст ─────────────────────────────────────────────────────
    Canvas canvas = new Canvas( shell, SWT.DOUBLE_BUFFERED | SWT.NO_BACKGROUND );
    GridData cgd = new GridData( SWT.FILL, SWT.FILL, true, true );
    cgd.widthHint = 560;
    cgd.heightHint = 440;
    canvas.setLayoutData( cgd );
    canvas.addPaintListener( e -> paintScene( e.gc, canvas ) );

    // ── Панель управления ─────────────────────────────────────────
    Group g = new Group( shell, SWT.NONE );
    g.setText( "Параметры" );
    g.setLayout( new GridLayout( 6, false ) );
    g.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );

    addSlider( g, canvas, "Внешний R", 40, 200, outerRadius, v -> outerRadius = v );
    addSlider( g, canvas, "Толщина", 4, 90, ringThick, v -> ringThick = v );
    addSlider( g, canvas, "Угол света", 0, 359, lightAngleDeg, v -> lightAngleDeg = v );
    addSlider( g, canvas, "Блеск", 0, 100, shininess, v -> shininess = v );
    addSlider( g, canvas, "Рисок", 20, 400, scratches, v -> scratches = v );
    addSlider( g, canvas, "Размытие", 4, 64, blurSamples, v -> blurSamples = v );

    shell.pack();
    shell.open();
    while( !shell.isDisposed() ) {
      if( !display.readAndDispatch() ) {
        display.sleep();
      }
    }
    display.dispose();
  }

  // ══════════════════════════════════════════════════════════════════
  // Отрисовка
  // ══════════════════════════════════════════════════════════════════
  private static void paintScene( GC gc, Canvas canvas ) {
    Display display = canvas.getDisplay();
    Rectangle b = canvas.getClientArea();

    // Тёмный фон
    Color bg = new Color( display, 22, 22, 26 );
    gc.setBackground( bg );
    gc.fillRectangle( b );
    bg.dispose();

    int cx = b.width / 2;
    int cy = b.height / 2;
    int innerR = Math.max( 4, outerRadius - ringThick );

    int size = outerRadius * 2 + 6;
    int ox = cx - outerRadius - 3;
    int oy = cy - outerRadius - 3;

    ImageData ringData = generateRing( size, size, outerRadius, innerR );
    Image ringImg = new Image( display, ringData );
    gc.drawImage( ringImg, ox, oy );
    ringImg.dispose();

    // Подпись
    Font font = new Font( display, new FontData( "Tahoma", 11, SWT.NORMAL ) );
    gc.setFont( font );
    gc.setForeground( display.getSystemColor( SWT.COLOR_GRAY ) );
    String info = "Brushed Steel  |  R=" + outerRadius + "  thick=" + ringThick + "  light=" + lightAngleDeg + "°";
    Point ts = gc.textExtent( info );
    gc.drawText( info, cx - ts.x / 2, cy + outerRadius + 16, true );
    font.dispose();
  }

  // ══════════════════════════════════════════════════════════════════
  // Генерация пикселей кольца
  // ══════════════════════════════════════════════════════════════════
  private static ImageData generateRing( int w, int h, int outerR, int innerR ) {
    ImageData data = new ImageData( w, h, 32, new PaletteData( 0x00FF0000, 0x0000FF00, 0x000000FF ) );
    data.alphaData = new byte[w * h];

    float cx = w / 2f;
    float cy = h / 2f;

    // Направление источника света (вектор в плоскости + z-компонента)
    double lightRad = Math.toRadians( lightAngleDeg );
    float lx = (float)Math.cos( lightRad );
    float ly = (float)-Math.sin( lightRad );
    float lz = 0.70f;
    float lLen = (float)Math.sqrt( lx * lx + ly * ly + lz * lz );
    lx /= lLen;
    ly /= lLen;
    lz /= lLen;

    // Шаг угла для одной риски
    float angStep = (float)(2.0 * Math.PI / scratches);
    // Угловой диапазон для анизотропного размытия
    float blurHalf = angStep * blurSamples * 0.5f;

    for( int y = 0; y < h; y++ ) {
      for( int x = 0; x < w; x++ ) {
        float dx = x - cx;
        float dy = y - cy;
        float dist = (float)Math.sqrt( dx * dx + dy * dy );

        if( dist < innerR || dist > outerR ) {
          continue;
        }

        // Нормализованное положение по толщине: 0 = inner, 1 = outer
        float t = (dist - innerR) / (outerR - innerR);

        // ── Нормаль на поверхности (тор, упрощённо: цилиндрическая грань) ──
        // Радиальная нормаль + небольшое z (имитация выпуклости к середине)
        float normX = dx / dist;
        float normY = dy / dist;
        float normZ = 0.5f - Math.abs( t - 0.5f ); // выпуклость
        float nLen = (float)Math.sqrt( normX * normX + normY * normY + normZ * normZ );
        normX /= nLen;
        normY /= nLen;
        normZ /= nLen;

        // ── Анизотропное размытие вдоль угла φ ───────────────────────────
        float phi = (float)Math.atan2( dy, dx ); // угол пикселя

        // Усредняем шум по нескольким углам вокруг φ (имитация риски)
        float brushSum = 0;
        for( int s = 0; s < blurSamples; s++ ) {
          float sAngle = phi - blurHalf + angStep * s;
          brushSum += radialNoise( sAngle, dist, outerR );
        }
        float brushVal = brushSum / blurSamples; // 0..1

        // ── Концентрические кольцевые риски (слабые) ─────────────────────
        float ring = (float)(0.5f + 0.5f * Math.sin( dist * 0.9f ));
        ring = ring * 0.18f; // слабее, чтобы не доминировали

        // ── Итоговая яркость поверхности ─────────────────────────────────
        float surface = brushVal * scratchDepth + (1f - scratchDepth) * 0.7f;
        surface = surface * 0.7f + ring;

        // ── Освещение (Phong) ─────────────────────────────────────────────
        float diff = Math.max( 0f, normX * lx + normY * ly + normZ * lz );

        // Half-vector для specular
        float hx = lx, hy = ly, hz = lz + 1f;
        float hLen2 = (float)Math.sqrt( hx * hx + hy * hy + hz * hz );
        hx /= hLen2;
        hy /= hLen2;
        hz /= hLen2;
        float NdotH = Math.max( 0f, normX * hx + normY * hy + normZ * hz );
        float spec = (float)Math.pow( NdotH, 6 + shininess * 3 );

        // Затемнение краёв (rim)
        float rim = 1f - (float)Math.pow( Math.abs( t - 0.5f ) * 2f, 1.6f ) * 0.55f;

        float ambient = 0.18f;
        float diffuse = ambient + (1f - ambient) * diff * rim;

        // ── Цвет стали: холодный серый с лёгким синеватым отливом ─────────
        float shine01 = shininess / 100f;
        float baseR = surface * 170 * diffuse + 255 * spec * shine01;
        float baseG = surface * 175 * diffuse + 255 * spec * shine01;
        float baseB = surface * 185 * diffuse + 255 * spec * shine01;

        // Добавляем яркое зеркало (узкая полоса highlight вдоль дуги)
        float highlight = computeHighlight( phi, dist, outerR, lightRad );
        baseR += highlight * 80 * shine01;
        baseG += highlight * 85 * shine01;
        baseB += highlight * 95 * shine01;

        data.setPixel( x, y, (clamp( baseR ) << 16) | (clamp( baseG ) << 8) | clamp( baseB ) );
        data.alphaData[y * w + x] = (byte)255;
      }
    }
    return data;
  }

  // ══════════════════════════════════════════════════════════════════
  // Радиальный шум — значение вдоль луча под углом angle
  // Имитирует микрорельеф шлифованной поверхности.
  // ══════════════════════════════════════════════════════════════════
  private static float radialNoise( float angle, float dist, float outerR ) {
    // Несколько гармоник по угловой частоте
    float v = 0.5f + 0.22f * (float)Math.sin( angle * 137f + dist * 0.12f )
        + 0.15f * (float)Math.sin( angle * 271f - dist * 0.07f ) + 0.08f * (float)Math.sin( angle * 53f + dist * 0.21f )
        + 0.05f * (float)Math.sin( angle * 389f - dist * 0.05f );
    return Math.max( 0f, Math.min( 1f, v ) );
  }

  // ══════════════════════════════════════════════════════════════════
  // Зеркальная полоса вдоль дуги (highlight): максимум там, где
  // нормаль кольца смотрит точно на источник.
  // ══════════════════════════════════════════════════════════════════
  private static float computeHighlight( float phi, float dist, float outerR, double lightRad ) {
    // Угол, при котором радиальная нормаль смотрит на свет
    float diffAng = phi - (float)lightRad;
    // Нормируем к [-π, π]
    while( diffAng > Math.PI ) {
      diffAng -= (float)(2 * Math.PI);
    }
    while( diffAng < -Math.PI ) {
      diffAng += (float)(2 * Math.PI);
    }
    // Широкий плавный highlight
    float h = (float)Math.exp( -diffAng * diffAng * 3.5f );
    // Слегка усиливаем ближе к середине толщины
    float t = dist / outerR;
    h *= (float)(0.6f + 0.4f * Math.exp( -Math.pow( (t - 0.75f) * 5f, 2 ) ));
    return h;
  }

  // ══════════════════════════════════════════════════════════════════
  // UI helpers
  // ══════════════════════════════════════════════════════════════════
  private static int clamp( float v ) {
    return Math.max( 0, Math.min( 255, (int)v ) );
  }

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
    sd.widthHint = 120;
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

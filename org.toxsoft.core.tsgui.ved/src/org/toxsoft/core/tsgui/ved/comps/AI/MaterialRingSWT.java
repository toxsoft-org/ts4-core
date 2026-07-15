package org.toxsoft.core.tsgui.ved.comps.AI;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * MaterialRingSWT — имитация материалов кольца средствами SWT. Материалы: 1. Сталь — анизотропный градиент +
 * горизонтальные полосы блеска 2. Золото — многослойный тёплый градиент + specular-блик 3. Пластик — насыщенный цвет +
 * большой диффузный блик 4. Мрамор — процедурные прожилки (турбулентность) + глянец 5. Дерево — процедурные годичные
 * кольца + волокна 6. Медь — радужный оксидный градиент + патина 7. Обсидиан — тёмное стекло + резкий белый рефлекс
 */
public class MaterialRingSWT {

  // ── Параметры кольца ──────────────────────────────────────
  private static int outerRadius = 120;
  private static int innerRadius = 72; // толщина кольца = outer - inner

  // ── Параметры освещения ───────────────────────────────────
  private static int lightAngleDeg = 135; // угол источника света (°)
  private static int shininess     = 80;  // интенсивность блика (0–100)

  // ── Текущий материал ──────────────────────────────────────
  private static int currentMaterial = 0;

  private static final String[] MATERIAL_NAMES =
      { "Сталь", "Золото", "Пластик", "Мрамор", "Дерево", "Медь", "Обсидиан" };

  // ── Seed для процедурных текстур ──────────────────────────
  private static long noiseSeed = 42L;

  public static void main( String[] args ) {
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setText( "Материалы кольца — SWT" );
    shell.setLayout( new GridLayout( 1, false ) );

    // ── Холст ─────────────────────────────────────────────
    Canvas canvas = new Canvas( shell, SWT.DOUBLE_BUFFERED | SWT.NO_BACKGROUND );
    GridData cgd = new GridData( SWT.FILL, SWT.FILL, true, true );
    cgd.widthHint = 560;
    cgd.heightHint = 420;
    canvas.setLayoutData( cgd );
    canvas.addPaintListener( e -> paintRing( e.gc, canvas ) );

    // ── Выбор материала ───────────────────────────────────
    Group matGroup = new Group( shell, SWT.NONE );
    matGroup.setText( "Материал" );
    matGroup.setLayout( new RowLayout( SWT.HORIZONTAL ) );
    matGroup.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );

    for( int i = 0; i < MATERIAL_NAMES.length; i++ ) {
      final int idx = i;
      Button btn = new Button( matGroup, SWT.RADIO );
      btn.setText( MATERIAL_NAMES[i] );
      btn.setSelection( i == 0 );
      btn.addListener( SWT.Selection, e -> {
        if( ((Button)e.widget).getSelection() ) {
          currentMaterial = idx;
          noiseSeed = new Random().nextLong();
          canvas.redraw();
        }
      } );
    }

    // ── Параметры ─────────────────────────────────────────
    Group paramGroup = new Group( shell, SWT.NONE );
    paramGroup.setText( "Параметры" );
    paramGroup.setLayout( new GridLayout( 6, false ) );
    paramGroup.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );

    addSlider( paramGroup, canvas, "Внешний R", 40, 180, outerRadius, v -> {
      outerRadius = v;
      if( innerRadius >= outerRadius - 10 ) {
        innerRadius = outerRadius - 10;
      }
    } );
    addSlider( paramGroup, canvas, "Толщина", 4, 80, outerRadius - innerRadius, v -> innerRadius = outerRadius - v );
    addSlider( paramGroup, canvas, "Угол света", 0, 359, lightAngleDeg, v -> lightAngleDeg = v );
    addSlider( paramGroup, canvas, "Блеск", 0, 100, shininess, v -> shininess = v );

    Button regenBtn = new Button( paramGroup, SWT.PUSH );
    regenBtn.setText( "Новая текстура" );
    regenBtn.addListener( SWT.Selection, e -> {
      noiseSeed = new Random().nextLong();
      canvas.redraw();
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

  // ══════════════════════════════════════════════════════════
  // Главный метод отрисовки
  // ══════════════════════════════════════════════════════════
  private static void paintRing( GC gc, Canvas canvas ) {
    Display display = canvas.getDisplay();
    Rectangle b = canvas.getClientArea();

    // Фон
    Color bg = new Color( display, 30, 30, 35 );
    gc.setBackground( bg );
    gc.fillRectangle( b );
    bg.dispose();

    int cx = b.width / 2;
    int cy = b.height / 2;

    // Генерируем текстуру в ImageData
    int size = outerRadius * 2 + 4;
    int ox = cx - outerRadius - 2;
    int oy = cy - outerRadius - 2;

    ImageData ringData = generateMaterialTexture( size, size, outerRadius, innerRadius, currentMaterial, lightAngleDeg,
        shininess, noiseSeed );

    Image ringImg = new Image( display, ringData );
    gc.drawImage( ringImg, ox, oy );
    ringImg.dispose();

    // Подпись материала
    gc.setForeground( display.getSystemColor( SWT.COLOR_WHITE ) );
    gc.setFont( new Font( display, new FontData( "Tahoma", 13, SWT.BOLD ) ) );
    String name = MATERIAL_NAMES[currentMaterial];
    Point ts = gc.textExtent( name );
    gc.drawText( name, cx - ts.x / 2, cy + outerRadius + 14, true );
  }

  // ══════════════════════════════════════════════════════════
  // Генерация текстуры материала
  // ══════════════════════════════════════════════════════════
  private static ImageData generateMaterialTexture( int w, int h, int outerR, int innerR, int material, int lightDeg,
      int shine, long seed ) {

    ImageData data = new ImageData( w, h, 32, new PaletteData( 0x00FF0000, 0x0000FF00, 0x000000FF ) );
    // Прозрачный фон
    for( int i = 0; i < w * h; i++ ) {
      data.setPixel( i % w, i / w, 0 );
    }
    data.alphaData = new byte[w * h];

    float cx = w / 2f;
    float cy = h / 2f;
    double lightRad = Math.toRadians( lightDeg );
    float lx = (float)Math.cos( lightRad );
    float ly = (float)-Math.sin( lightRad );

    for( int y = 0; y < h; y++ ) {
      for( int x = 0; x < w; x++ ) {
        float dx = x - cx;
        float dy = y - cy;
        float dist = (float)Math.sqrt( dx * dx + dy * dy );

        // Пиксель только внутри кольца
        if( dist < innerR || dist > outerR ) {
          continue;
        }

        // Нормализованные координаты [-1..1] по поверхности кольца
        float nx = dx / outerR;
        float ny = dy / outerR;

        // Нормаль на торической поверхности (упрощённо — цилиндр)
        float t = (dist - innerR) / (outerR - innerR); // 0=inner..1=outer
        // Нормаль направлена наружу по радиусу + чуть вверх (выпуклость)
        float normX = dx / dist;
        float normY = dy / dist;
        float normZ = 0.6f - Math.abs( t - 0.5f ) * 1.2f;
        float nLen = (float)Math.sqrt( normX * normX + normY * normY + normZ * normZ );
        normX /= nLen;
        normY /= nLen;
        normZ /= nLen;

        // Диффузное освещение
        float diff = Math.max( 0, normX * lx + normY * ly + normZ * 0.6f );

        // Specular (Phong)
        float halfX = (lx + 0) / 2f;
        float halfY = (ly + 0) / 2f;
        float halfZ = (0.6f + 1f) / 2f;
        float hLen = (float)Math.sqrt( halfX * halfX + halfY * halfY + halfZ * halfZ );
        float spec = (float)Math.pow( Math.max( 0, normX * halfX / hLen + normY * halfY / hLen + normZ * halfZ / hLen ),
            4 + shine * 2 );

        int[] rgb = computeMaterialColor( material, x, y, w, h, cx, cy, dist, outerR, innerR, t, nx, ny, diff, spec,
            shine / 100f, seed );

        int r = clamp( rgb[0] ), g = clamp( rgb[1] ), b = clamp( rgb[2] );
        int pixel = (r << 16) | (g << 8) | b;
        data.setPixel( x, y, pixel );
        data.alphaData[y * w + x] = (byte)255;
      }
    }
    return data;
  }

  // ══════════════════════════════════════════════════════════
  // Цвет пикселя по материалу
  // ══════════════════════════════════════════════════════════
  private static int[] computeMaterialColor( int mat, int px, int py, int w, int h, float cx, float cy, float dist,
      float outerR, float innerR, float t, float nx, float ny, float diff, float spec, float shine, long seed ) {

    return switch( mat ) {
      case 0 -> steel( px, py, dist, outerR, t, diff, spec, shine );
      case 1 -> gold( px, py, dist, outerR, t, diff, spec, shine );
      case 2 -> plastic( px, py, dist, outerR, t, diff, spec, shine );
      case 3 -> marble( px, py, cx, cy, dist, outerR, t, diff, spec, shine, seed );
      case 4 -> wood( px, py, cx, cy, dist, outerR, innerR, t, diff, spec, shine, seed );
      case 5 -> copper( px, py, dist, outerR, t, diff, spec, shine, seed );
      case 6 -> obsidian( px, py, dist, outerR, t, diff, spec, shine );
      default -> new int[] { 128, 128, 128 };
    };
  }

  // ─── СТАЛЬ ────────────────────────────────────────────────
  private static int[] steel( int px, int py, float dist, float outerR, float t, float diff, float spec, float shine ) {

    // Базовый серый с анизотропными горизонтальными полосами
    float stripe = (float)(0.5f + 0.3f * Math.sin( py * 0.35f ) + 0.15f * Math.sin( py * 1.1f + 0.7f ));
    float base = 100 + 80 * stripe;

    // Радиальное затемнение по краям (объём)
    float rim = 1f - (float)Math.pow( Math.abs( t - 0.5f ) * 2f, 1.5f ) * 0.5f;

    float r = base * diff * rim + 240 * spec * shine;
    float g = base * diff * rim + 240 * spec * shine;
    float b = (base - 5) * diff * rim + 255 * spec * shine;
    return new int[] { (int)r, (int)g, (int)b };
  }

  // ─── ЗОЛОТО ───────────────────────────────────────────────
  private static int[] gold( int px, int py, float dist, float outerR, float t, float diff, float spec, float shine ) {

    // Тёплый жёлто-оранжевый градиент
    float wave = (float)(0.5f + 0.4f * Math.sin( py * 0.28f + 1.0f ) + 0.1f * Math.sin( py * 0.9f ));
    float rim = 1f - (float)Math.pow( Math.abs( t - 0.5f ) * 2f, 2f ) * 0.4f;
    float amb = 0.25f;
    float d = amb + (1f - amb) * diff * rim;

    float r = (200 + 40 * wave) * d + 255 * spec * shine;
    float g = (160 + 30 * wave) * d + 220 * spec * shine;
    float b = (40 + 20 * wave) * d + 80 * spec * shine;
    return new int[] { (int)r, (int)g, (int)b };
  }

  // ─── ПЛАСТИК ──────────────────────────────────────────────
  private static int[] plastic( int px, int py, float dist, float outerR, float t, float diff, float spec,
      float shine ) {

    // Насыщенный синий пластик
    float rim = 1f - (float)Math.pow( Math.abs( t - 0.5f ) * 2f, 2f ) * 0.3f;
    float amb = 0.2f;
    float d = amb + (1f - amb) * diff * rim;

    float r = 30 * d + 255 * spec * shine;
    float g = 100 * d + 255 * spec * shine;
    float b = 220 * d + 255 * spec * shine;
    return new int[] { (int)r, (int)g, (int)b };
  }

  // ─── МРАМОР ───────────────────────────────────────────────
  private static int[] marble( int px, int py, float cx, float cy, float dist, float outerR, float t, float diff,
      float spec, float shine, long seed ) {

    // Базовый цвет + прожилки через турбулентность
    float turb = turbulence( px * 0.04f, py * 0.04f, 4, seed );
    float vein = (float)(0.5f + 0.5f * Math.sin( (px * 0.02f + py * 0.015f) * Math.PI + turb * 6.0f ));

    float rim = 1f - (float)Math.pow( Math.abs( t - 0.5f ) * 2f, 1.8f ) * 0.4f;
    float amb = 0.3f;
    float d = amb + (1f - amb) * diff * rim;

    // Белый мрамор с серыми прожилками
    float base = 200 + 40 * vein;
    float veinC = 60 + 80 * (1 - vein);
    float marble = base * 0.7f + veinC * 0.3f;

    float r = marble * d + 255 * spec * shine;
    float g = marble * d + 250 * spec * shine;
    float b = marble * d + 255 * spec * shine;
    return new int[] { (int)r, (int)g, (int)b };
  }

  // ─── ДЕРЕВО ───────────────────────────────────────────────
  private static int[] wood( int px, int py, float cx, float cy, float dist, float outerR, float innerR, float t,
      float diff, float spec, float shine, long seed ) {

    // Годичные кольца + волокна
    float dx = px - cx, dy = py - cy;
    float angle = (float)Math.atan2( dy, dx );
    float distN = dist / outerR;

    float grain = turbulence( px * 0.06f, py * 0.06f, 3, seed ) * 0.3f;
    float rings = (float)(0.5f + 0.5f * Math.sin( distN * 18f + angle * 0.4f + grain * 5f ));

    float rim = 1f - (float)Math.pow( Math.abs( t - 0.5f ) * 2f, 1.5f ) * 0.35f;
    float amb = 0.25f;
    float d = amb + (1f - amb) * diff * rim;

    // Тёплые коричневые тона
    float dark = rings;
    float r = (140 + 60 * dark) * d + 220 * spec * shine;
    float g = (85 + 40 * dark) * d + 180 * spec * shine;
    float b = (30 + 20 * dark) * d + 100 * spec * shine;
    return new int[] { (int)r, (int)g, (int)b };
  }

  // ─── МЕДЬ ─────────────────────────────────────────────────
  private static int[] copper( int px, int py, float dist, float outerR, float t, float diff, float spec, float shine,
      long seed ) {

    // Медь с патиной (зелёные пятна)
    float patina = noise2d( px * 0.07f, py * 0.07f, seed );
    float wave = (float)(0.5f + 0.4f * Math.sin( py * 0.25f + patina * 3f ));

    float rim = 1f - (float)Math.pow( Math.abs( t - 0.5f ) * 2f, 1.8f ) * 0.4f;
    float amb = 0.2f;
    float d = amb + (1f - amb) * diff * rim;

    // Переход от медного к зелёной патине
    float copper = wave;
    float pat = Math.max( 0, patina - 0.3f ) * 1.4f;

    float r = ((180 + 40 * copper) * (1 - pat * 0.6f)) * d + 255 * spec * shine;
    float g = ((90 + 30 * copper) * (1 - pat * 0.2f) + 120 * pat) * d + 220 * spec * shine;
    float b = ((40 + 20 * copper) + 80 * pat) * d + 180 * spec * shine;
    return new int[] { (int)r, (int)g, (int)b };
  }

  // ─── ОБСИДИАН ─────────────────────────────────────────────
  private static int[] obsidian( int px, int py, float dist, float outerR, float t, float diff, float spec,
      float shine ) {

    // Тёмное вулканическое стекло — почти чёрное + резкий рефлекс
    float rim = 1f - (float)Math.pow( Math.abs( t - 0.5f ) * 2f, 2f ) * 0.5f;
    float amb = 0.05f;
    float d = amb + (1f - amb) * diff * rim * 0.4f;

    // Фиолетовый оттенок в отражении
    float r = 20 * d + 200 * spec * shine;
    float g = 15 * d + 180 * spec * shine;
    float b = 25 * d + 230 * spec * shine;
    return new int[] { (int)r, (int)g, (int)b };
  }

  // ══════════════════════════════════════════════════════════
  // Процедурный шум и турбулентность
  // ══════════════════════════════════════════════════════════

  /** Плавный решётчатый шум Перлина (упрощённый). */
  private static float noise2d( float x, float y, long seed ) {
    int ix = (int)Math.floor( x );
    int iy = (int)Math.floor( y );
    float fx = x - ix;
    float fy = y - iy;
    float ux = fx * fx * (3 - 2 * fx);
    float uy = fy * fy * (3 - 2 * fy);

    float v00 = grad( ix, iy, fx, fy, seed );
    float v10 = grad( ix + 1, iy, fx - 1, fy, seed );
    float v01 = grad( ix, iy + 1, fx, fy - 1, seed );
    float v11 = grad( ix + 1, iy + 1, fx - 1, fy - 1, seed );

    float nx0 = v00 + ux * (v10 - v00);
    float nx1 = v01 + ux * (v11 - v01);
    return 0.5f + 0.5f * (nx0 + uy * (nx1 - nx0));
  }

  private static float grad( int ix, int iy, float dx, float dy, long seed ) {
    long h = seed ^ (ix * 1619L) ^ (iy * 31337L);
    h = h * 6364136223846793005L + 1442695040888963407L;
    h ^= h >>> 33;
    float gx = ((h & 0xFF) / 127.5f) - 1f;
    float gy = (((h >> 8) & 0xFF) / 127.5f) - 1f;
    return dx * gx + dy * gy;
  }

  /** Турбулентность = сумма нескольких октав шума. */
  private static float turbulence( float x, float y, int octaves, long seed ) {
    float val = 0, amp = 1, freq = 1, max = 0;
    for( int o = 0; o < octaves; o++ ) {
      val += Math.abs( noise2d( x * freq, y * freq, seed + o * 997L ) * 2 - 1 ) * amp;
      max += amp;
      amp *= 0.5f;
      freq *= 2f;
    }
    return val / max;
  }

  // ══════════════════════════════════════════════════════════
  // Утилиты
  // ══════════════════════════════════════════════════════════
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

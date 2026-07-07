package org.toxsoft.core.tsgui.ved.comps.AI;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * SwtPathScaler — масштабирование SWT Path без Transform с независимыми коэффициентами по X и Y. Алгоритм: 1.
 * path.getPathData() → points[], types[] 2. Каждую точку масштабируем относительно anchor: px' = anchorX + (px -
 * anchorX) * scaleX py' = anchorY + (py - anchorY) * scaleY 3. Собираем новый Path
 */
public class SwtPathScaler {

  /**
   * Возвращает новый масштабированный Path. Вызывающий код обязан вызвать dispose() на возвращённом Path.
   *
   * @param display SWT Display
   * @param src исходный Path (не изменяется)
   * @param anchorX X точки опоры (относительно неё масштабируем)
   * @param anchorY Y точки опоры
   * @param scaleX коэффициент масштаба по X (1.0 = без изменений)
   * @param scaleY коэффициент масштаба по Y (1.0 = без изменений)
   * @return новый масштабированный Path
   */
  public static Path scale( Display display, Path src, float anchorX, float anchorY, float scaleX, float scaleY ) {
    PathData pd = src.getPathData();
    float[] pts = pd.points;
    byte[] types = pd.types;

    // Масштабируем все точки сразу — быстрее чем внутри switch
    float[] scaled = scalePoints( pts, anchorX, anchorY, scaleX, scaleY );

    Path dst = new Path( display );
    int pi = 0;

    for( byte type : types ) {
      switch( type ) {
        case SWT.PATH_MOVE_TO:
          dst.moveTo( scaled[pi], scaled[pi + 1] );
          pi += 2;
          break;

        case SWT.PATH_LINE_TO:
          dst.lineTo( scaled[pi], scaled[pi + 1] );
          pi += 2;
          break;

        case SWT.PATH_QUAD_TO:
          dst.quadTo( scaled[pi], scaled[pi + 1], scaled[pi + 2], scaled[pi + 3] );
          pi += 4;
          break;

        case SWT.PATH_CUBIC_TO:
          dst.cubicTo( scaled[pi], scaled[pi + 1], scaled[pi + 2], scaled[pi + 3], scaled[pi + 4], scaled[pi + 5] );
          pi += 6;
          break;

        case SWT.PATH_CLOSE:
          dst.close();
          // pi не меняется — CLOSE не имеет координат
          break;
      }
    }
    return dst;
  }

  /**
   * Удобный метод: масштаб относительно центра ограничивающего прямоугольника самого Path (getBounds()).
   */
  public static Path scaleFromCenter( Display display, Path src, float scaleX, float scaleY ) {
    float[] b = new float[4];
    src.getBounds( b ); // x, y, w, h
    float cx = b[0] + b[2] / 2f;
    float cy = b[1] + b[3] / 2f;
    return scale( display, src, cx, cy, scaleX, scaleY );
  }

  // ────────────────────────────────────────────────────────────────
  // Внутренний метод: масштабирование массива точек
  // ────────────────────────────────────────────────────────────────
  private static float[] scalePoints( float[] pts, float anchorX, float anchorY, float scaleX, float scaleY ) {
    float[] out = new float[pts.length];
    for( int i = 0; i < pts.length; i += 2 ) {
      out[i] = anchorX + (pts[i] - anchorX) * scaleX;
      out[i + 1] = anchorY + (pts[i + 1] - anchorY) * scaleY;
    }
    return out;
  }

  // ════════════════════════════════════════════════════════════════
  // Комбинированный метод: поворот + масштаб за один проход
  // (чтобы не создавать промежуточный Path)
  // ════════════════════════════════════════════════════════════════

  /**
   * Поворот на angleDeg градусов + масштаб (scaleX, scaleY) вокруг одной точки (cx, cy) — всё за один проход по точкам.
   * Порядок применения: сначала поворот, потом масштаб.
   */
  public static Path rotateAndScale( Display display, Path src, float cx, float cy, float angleDeg, float scaleX,
      float scaleY ) {
    double rad = Math.toRadians( angleDeg );
    double cosA = Math.cos( rad );
    double sinA = Math.sin( rad );

    PathData pd = src.getPathData();
    float[] pts = pd.points;
    // byte[] types = pd.types;

    float[] xformed = new float[pts.length];
    for( int i = 0; i < pts.length; i += 2 ) {
      double dx = pts[i] - cx;
      double dy = pts[i + 1] - cy;
      // Поворот
      double rx = dx * cosA - dy * sinA;
      double ry = dx * sinA + dy * cosA;
      // Масштаб
      xformed[i] = (float)(cx + rx * scaleX);
      xformed[i + 1] = (float)(cy + ry * scaleY);
    }

    // Path dst = new Path( display );
    for( int i = 0; i < pts.length; i += 2 ) {
      pd.points[i] = xformed[i];
      pd.points[i + 1] = xformed[i + 1];
    }

    Path dst = new Path( display, pd );

    // int pi = 0;
    // for( byte type : types ) {
    // switch( type ) {
    // case SWT.PATH_MOVE_TO:
    // dst.moveTo( xformed[pi], xformed[pi + 1] );
    // pi += 2;
    // break;
    // case SWT.PATH_LINE_TO:
    // dst.lineTo( xformed[pi], xformed[pi + 1] );
    // pi += 2;
    // break;
    // case SWT.PATH_QUAD_TO:
    // dst.quadTo( xformed[pi], xformed[pi + 1], xformed[pi + 2], xformed[pi + 3] );
    // pi += 4;
    // break;
    // case SWT.PATH_CUBIC_TO:
    // dst.cubicTo( xformed[pi], xformed[pi + 1], xformed[pi + 2], xformed[pi + 3], xformed[pi + 4],
    // xformed[pi + 5] );
    // pi += 6;
    // break;
    // case SWT.PATH_CLOSE:
    // dst.close();
    // break;
    // }
    // }
    return dst;
  }

  // ════════════════════════════════════════════════════════════════
  // Демонстрация
  // ════════════════════════════════════════════════════════════════
  public static void main( String[] args ) {
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setText( "SwtPathScaler — Demo" );
    shell.setLayout( new GridLayout( 1, false ) );
    shell.setSize( 600, 520 );

    Canvas canvas = new Canvas( shell, SWT.DOUBLE_BUFFERED | SWT.NO_BACKGROUND );
    canvas.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );

    // Параметры масштаба
    float[] scaleX = { 1.0f };
    float[] scaleY = { 1.0f };

    canvas.addPaintListener( e -> {
      Rectangle b = canvas.getClientArea();
      e.gc.setBackground( display.getSystemColor( SWT.COLOR_WHITE ) );
      e.gc.fillRectangle( b );
      e.gc.setAntialias( SWT.ON );

      float cx = b.width / 2f;
      float cy = b.height / 2f;

      // Исходная фигура — кольцо (два концентрических круга)
      Path original = makeRing( display, cx, cy, 90, 40 );

      // Масштабированный Path относительно центра
      Path scaled = scale( display, original, cx, cy, scaleX[0], scaleY[0] );

      // Оригинал — серый пунктир
      e.gc.setForeground( display.getSystemColor( SWT.COLOR_GRAY ) );
      e.gc.setLineStyle( SWT.LINE_DASH );
      e.gc.setLineWidth( 1 );
      e.gc.drawPath( original );

      // Масштабированный — синий
      Color fill = new Color( display, 66, 133, 244 );
      Color stroke = new Color( display, 25, 75, 160 );
      e.gc.setLineStyle( SWT.LINE_SOLID );
      e.gc.setLineWidth( 2 );
      e.gc.setBackground( fill );
      e.gc.setForeground( stroke );
      e.gc.setAlpha( 180 );
      e.gc.fillPath( scaled );
      e.gc.setAlpha( 255 );
      e.gc.drawPath( scaled );
      fill.dispose();
      stroke.dispose();

      // Крестик — точка опоры
      e.gc.setForeground( display.getSystemColor( SWT.COLOR_RED ) );
      e.gc.setLineWidth( 1 );
      e.gc.drawLine( (int)cx - 8, (int)cy, (int)cx + 8, (int)cy );
      e.gc.drawLine( (int)cx, (int)cy - 8, (int)cx, (int)cy + 8 );

      // Подпись
      e.gc.setForeground( display.getSystemColor( SWT.COLOR_DARK_GRAY ) );
      e.gc.drawText( String.format( "scaleX=%.2f  scaleY=%.2f", scaleX[0], scaleY[0] ), 10, 10, true );

      original.dispose();
      scaled.dispose();
    } );

    // ── Панель управления ─────────────────────────────────────
    Composite panel = new Composite( shell, SWT.NONE );
    panel.setLayout( new GridLayout( 4, false ) );
    panel.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );

    addSlider( panel, canvas, "Scale X", 10, 300, 100, v -> scaleX[0] = v / 100f );
    addSlider( panel, canvas, "Scale Y", 10, 300, 100, v -> scaleY[0] = v / 100f );

    shell.open();
    while( !shell.isDisposed() ) {
      if( !display.readAndDispatch() ) {
        display.sleep();
      }
    }
    display.dispose();
  }

  /** Кольцо из двух окружностей через addArc. */
  private static Path makeRing( Display display, float cx, float cy, float outerR, float innerR ) {
    Path p = new Path( display );
    p.addArc( cx - outerR, cy - outerR, outerR * 2, outerR * 2, 0, 360 );
    p.addArc( cx - innerR, cy - innerR, innerR * 2, innerR * 2, 0, 360 );
    return p;
  }

  @FunctionalInterface
  interface IntConsumer {

    void accept( int v );
  }

  private static void addSlider( Composite parent, Canvas canvas, String label, int min, int max, int initial,
      IntConsumer onChange ) {
    Composite col = new Composite( parent, SWT.NONE );
    col.setLayout( new GridLayout( 1, false ) );

    Label lbl = new Label( col, SWT.CENTER );
    lbl.setLayoutData( new GridData( SWT.CENTER, SWT.CENTER, true, false ) );
    lbl.setText( label + ": " + initial + "%" );

    Slider slider = new Slider( col, SWT.HORIZONTAL );
    GridData sd = new GridData( SWT.FILL, SWT.CENTER, true, false );
    sd.widthHint = 200;
    slider.setLayoutData( sd );
    slider.setMinimum( min );
    slider.setMaximum( max + slider.getThumb() );
    slider.setSelection( initial );
    slider.addListener( SWT.Selection, e -> {
      int val = slider.getSelection();
      lbl.setText( label + ": " + val + "%" );
      onChange.accept( val );
      canvas.redraw();
    } );
  }
}

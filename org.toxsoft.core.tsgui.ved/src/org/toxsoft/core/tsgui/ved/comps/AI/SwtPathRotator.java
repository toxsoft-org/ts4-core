package org.toxsoft.core.tsgui.ved.comps.AI;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * SwtPathRotator — поворот SWT Path вокруг заданной точки без использования Transform. Алгоритм: 1. path.getPathData()
 * → массивы points[] и types[] 2. Применяем формулу поворота к каждой паре (x, y) 3. Собираем новый Path из повёрнутых
 * координат Типы сегментов SWT и количество точек: SEG_MOVETO (0) — 1 точка (2 координаты) SEG_LINETO (1) — 1 точка (2
 * координаты) SEG_QUADTO (2) — 2 точки (4 координаты) SEG_CUBICTO (3) — 3 точки (6 координат) SEG_CLOSE (4) — 0 точек
 * (0 координат)
 */
public class SwtPathRotator {

  // /**
  // * Возвращает новый Path, повёрнутый вокруг точки (cx, cy) на угол angleDeg (в градусах, + = по часовой стрелке в
  // * SWT). Вызывающий код обязан вызвать dispose() на возвращённом Path.
  // *
  // * @param display SWT Display
  // * @param src исходный Path (не изменяется)
  // * @param cx X центра вращения
  // * @param cy Y центра вращения
  // * @param angleDeg угол поворота в градусах
  // * @return новый повёрнутый Path
  // */
  // public static Path rotate( Display display, Path src, float cx, float cy, float angleDeg ) {
  // double rad = Math.toRadians( angleDeg );
  // double cosA = Math.cos( rad );
  // double sinA = Math.sin( rad );
  //
  // PathData pd = src.getPathData();
  // float[] pts = pd.points;
  // byte[] types = pd.types;
  //
  // float[] xformed = new float[pts.length];
  // for( int i = 0; i < pts.length; i += 2 ) {
  // float[] rotatedPoint = rotatePoint( pts[i], pts[i + 1], cx, cy, cosA, sinA );
  // xformed[i] = rotatedPoint[0];
  // xformed[i + 1] = rotatedPoint[1];
  // }
  //
  // for( int i = 0; i < pts.length; i += 2 ) {
  // pd.points[i] = xformed[i];
  // pd.points[i + 1] = xformed[i + 1];
  // }
  //
  // Path dst = new Path( display, pd );
  //
  // // Path dst = new Path( display );
  // // int pi = 0; // индекс в массиве points
  // //
  // // for( byte type : types ) {
  // // switch( type ) {
  // //
  // // case SWT.PATH_MOVE_TO: {
  // // float[] p = rotatePoint( pts, pi, cx, cy, cosA, sinA );
  // // dst.moveTo( p[0], p[1] );
  // // pi += 2;
  // // break;
  // // }
  // // case SWT.PATH_LINE_TO: {
  // // float[] p = rotatePoint( pts, pi, cx, cy, cosA, sinA );
  // // dst.lineTo( p[0], p[1] );
  // // pi += 2;
  // // break;
  // // }
  // // case SWT.PATH_QUAD_TO: {
  // // // Контрольная точка + конечная точка
  // // float[] cp = rotatePoint( pts, pi, cx, cy, cosA, sinA );
  // // float[] ep = rotatePoint( pts, pi + 2, cx, cy, cosA, sinA );
  // // // SWT не имеет quadTo → конвертируем в cubicTo
  // // // (или используем путь через две точки)
  // // // Получаем текущую позицию пера через bounds — невозможно напрямую,
  // // // поэтому просто делаем lineTo к конечной точке как fallback.
  // // // Для точной конвертации quadTo→cubicTo нужно знать curX/curY —
  // // // см. примечание ниже.
  // // dst.quadTo( cp[0], cp[1], ep[0], ep[1] );
  // // pi += 4;
  // // break;
  // // }
  // // case SWT.PATH_CUBIC_TO: {
  // // // cp1, cp2, конечная точка
  // // float[] cp1 = rotatePoint( pts, pi, cx, cy, cosA, sinA );
  // // float[] cp2 = rotatePoint( pts, pi + 2, cx, cy, cosA, sinA );
  // // float[] ep = rotatePoint( pts, pi + 4, cx, cy, cosA, sinA );
  // // dst.cubicTo( cp1[0], cp1[1], cp2[0], cp2[1], ep[0], ep[1] );
  // // pi += 6;
  // // break;
  // // }
  // // case SWT.PATH_CLOSE: {
  // // dst.close();
  // // // pi не меняется — CLOSE не имеет координат
  // // break;
  // // }
  // // }
  // // }
  // return dst;
  // }

  public static Path rotate( Display display, Path src, float cx, float cy, float angleDeg ) {
    double rad = Math.toRadians( angleDeg );
    double cosA = Math.cos( rad );
    double sinA = Math.sin( rad );

    PathData pd = src.getPathData();
    float[] pts = pd.points;

    float[] xformed = new float[pts.length];
    for( int i = 0; i < pts.length; i += 2 ) {
      double dx = pts[i] - cx;
      double dy = pts[i + 1] - cy;
      xformed[i] = (float)(cx + dx * cosA - dy * sinA);
      xformed[i + 1] = (float)(cy + dx * sinA + dy * cosA);
    }
    pd.points = xformed;
    return new Path( display, pd );
  }

  /**
   * Поворачивает точку (pts[pi], pts[pi+1]) вокруг (cx, cy).
   */
  private static float[] rotatePoint( float[] pts, int pi, float cx, float cy, double cosA, double sinA ) {
    double dx = pts[pi] - cx;
    double dy = pts[pi + 1] - cy;
    return new float[] { (float)(cx + dx * cosA - dy * sinA), (float)(cy + dx * sinA + dy * cosA) };
  }

  /**
   * Поворачивает точку (pts[pi], pts[pi+1]) вокруг (cx, cy).
   */
  private static float[] rotatePoint( float x, float y, float cx, float cy, double cosA, double sinA ) {
    double dx = x - cx;
    double dy = y - cy;
    return new float[] { (float)(cx + dx * cosA - dy * sinA), (float)(cy + dx * sinA + dy * cosA) };
  }

  // ════════════════════════════════════════════════════════════════
  // Демонстрация
  // ════════════════════════════════════════════════════════════════
  public static void main( String[] args ) {
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setText( "SwtPathRotator — Demo" );
    shell.setSize( 520, 460 );

    // Угол поворота — управляется слайдером
    int[] angleDeg = { 0 };

    shell.setLayout( new org.eclipse.swt.layout.GridLayout( 1, false ) );

    Canvas canvas = new Canvas( shell, SWT.DOUBLE_BUFFERED | SWT.NO_BACKGROUND );
    org.eclipse.swt.layout.GridData gd = new org.eclipse.swt.layout.GridData( SWT.FILL, SWT.FILL, true, true );
    canvas.setLayoutData( gd );

    canvas.addPaintListener( e -> {
      Rectangle b = canvas.getClientArea();
      e.gc.setBackground( display.getSystemColor( SWT.COLOR_WHITE ) );
      e.gc.fillRectangle( b );
      e.gc.setAntialias( SWT.ON );

      float cx = b.width / 2f;
      float cy = b.height / 2f;

      // Исходная фигура — стрелка, чтобы поворот был хорошо виден
      Path original = makeArrow( display, cx, cy, 100 );

      // Повёрнутый Path
      Path rotated = rotate( display, original, cx, cy, angleDeg[0] );

      // Рисуем оригинал (полупрозрачный серый)
      e.gc.setAlpha( 60 );
      e.gc.setBackground( display.getSystemColor( SWT.COLOR_GRAY ) );
      e.gc.fillPath( original );
      e.gc.setAlpha( 255 );

      // Рисуем повёрнутый (синий)
      Color fill = new Color( display, 66, 133, 244 );
      Color stroke = new Color( display, 30, 80, 160 );
      e.gc.setBackground( fill );
      e.gc.setForeground( stroke );
      e.gc.setLineWidth( 2 );
      e.gc.fillPath( rotated );
      e.gc.drawPath( rotated );
      fill.dispose();
      stroke.dispose();

      // Крестик — центр вращения
      e.gc.setForeground( display.getSystemColor( SWT.COLOR_RED ) );
      e.gc.setLineWidth( 1 );
      int cs = 8;
      e.gc.drawLine( (int)cx - cs, (int)cy, (int)cx + cs, (int)cy );
      e.gc.drawLine( (int)cx, (int)cy - cs, (int)cx, (int)cy + cs );

      original.dispose();
      rotated.dispose();
    } );

    // Слайдер угла
    org.eclipse.swt.widgets.Composite bottom = new org.eclipse.swt.widgets.Composite( shell, SWT.NONE );
    bottom.setLayout( new org.eclipse.swt.layout.GridLayout( 2, false ) );
    bottom.setLayoutData( new org.eclipse.swt.layout.GridData( SWT.FILL, SWT.CENTER, true, false ) );

    Label lbl = new Label( bottom, SWT.NONE );
    lbl.setText( "Угол: 0°" );
    lbl.setLayoutData( new org.eclipse.swt.layout.GridData( 80, SWT.DEFAULT ) );

    Slider slider = new Slider( bottom, SWT.HORIZONTAL );
    slider.setLayoutData( new org.eclipse.swt.layout.GridData( SWT.FILL, SWT.CENTER, true, false ) );
    slider.setMinimum( 0 );
    slider.setMaximum( 360 + slider.getThumb() );
    slider.setSelection( 0 );
    slider.addListener( SWT.Selection, ev -> {
      angleDeg[0] = slider.getSelection();
      lbl.setText( "Угол: " + angleDeg[0] + "°" );
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

  /** Стрелка вправо с центром в (cx, cy) и размером size. */
  private static Path makeArrow( Display display, float cx, float cy, float size ) {
    Path p = new Path( display );
    float s = size;
    // Тело стрелки
    p.moveTo( cx - s, cy - s * 0.25f );
    p.lineTo( cx + s * 0.3f, cy - s * 0.25f );
    p.lineTo( cx + s * 0.3f, cy - s * 0.55f );
    p.lineTo( cx + s, cy );
    p.lineTo( cx + s * 0.3f, cy + s * 0.55f );
    p.lineTo( cx + s * 0.3f, cy + s * 0.25f );
    p.lineTo( cx - s, cy + s * 0.25f );
    p.close();
    return p;
  }
}

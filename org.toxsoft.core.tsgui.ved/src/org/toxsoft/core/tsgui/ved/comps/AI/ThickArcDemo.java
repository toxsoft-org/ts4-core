package org.toxsoft.core.tsgui.ved.comps.AI;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * Демо-приложение для рисования толстой дуги через SWT Path. Зависимость (Maven) — выберите свою платформу: Linux:
 * org.eclipse.platform : org.eclipse.swt.gtk.linux.x86_64 Windows: org.eclipse.platform :
 * org.eclipse.swt.win32.win32.x86_64 macOS: org.eclipse.platform : org.eclipse.swt.cocoa.macosx.x86_64 Версия: 3.125.0
 * (или новее) Компиляция и запуск: javac -cp swt.jar ThickArcDemo.java java -cp .:swt.jar ThickArcDemo (Linux/macOS)
 * java -cp ".;swt.jar" ThickArcDemo (Windows)
 */
public class ThickArcDemo {

  public static void main( String[] args ) {
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setText( "Thick Arc Demo" );
    shell.setSize( 700, 600 );
    shell.setLayout( new GridLayout( 1, false ) );

    Canvas canvas = new Canvas( shell, SWT.DOUBLE_BUFFERED );
    canvas.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );

    Composite controls = new Composite( shell, SWT.NONE );
    controls.setLayout( new RowLayout( SWT.HORIZONTAL ) );
    controls.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );

    new Label( controls, SWT.NONE ).setText( "Радиус:" );
    Spinner spinRadius = new Spinner( controls, SWT.BORDER );
    spinRadius.setMinimum( 20 );
    spinRadius.setMaximum( 250 );
    spinRadius.setSelection( 120 );

    new Label( controls, SWT.NONE ).setText( "  Толщина:" );
    Spinner spinThick = new Spinner( controls, SWT.BORDER );
    spinThick.setMinimum( 2 );
    spinThick.setMaximum( 100 );
    spinThick.setSelection( 30 );

    new Label( controls, SWT.NONE ).setText( "  Нач.угол:" );
    Spinner spinStart = new Spinner( controls, SWT.BORDER );
    spinStart.setMinimum( -360 );
    spinStart.setMaximum( 360 );
    spinStart.setSelection( 30 );

    new Label( controls, SWT.NONE ).setText( "  Угол дуги:" );
    Spinner spinSweep = new Spinner( controls, SWT.BORDER );
    spinSweep.setMinimum( -360 );
    spinSweep.setMaximum( 360 );
    spinSweep.setSelection( 270 );

    new Label( controls, SWT.NONE ).setText( "  Концы:" );
    Button btnFlat = new Button( controls, SWT.RADIO );
    btnFlat.setText( "Плоские" );
    btnFlat.setSelection( true );
    Button btnRound = new Button( controls, SWT.RADIO );
    btnRound.setText( "Круглые" );

    canvas.addPaintListener( e -> {
      GC gc = e.gc;
      gc.setAntialias( SWT.ON );

      Rectangle bounds = canvas.getClientArea();
      int cx = bounds.width / 2;
      int cy = bounds.height / 2;

      gc.setBackground( display.getSystemColor( SWT.COLOR_WHITE ) );
      gc.fillRectangle( bounds );

      // Вспомогательные окружности
      gc.setForeground( display.getSystemColor( SWT.COLOR_GRAY ) );
      gc.setLineStyle( SWT.LINE_DOT );
      int r = spinRadius.getSelection();
      int t = spinThick.getSelection();
      gc.drawOval( cx - r, cy - r, r * 2, r * 2 );
      gc.drawOval( cx - r - t / 2, cy - r - t / 2, (r + t / 2) * 2, (r + t / 2) * 2 );
      gc.drawOval( cx - r + t / 2, cy - r + t / 2, (r - t / 2) * 2, (r - t / 2) * 2 );
      gc.setLineStyle( SWT.LINE_SOLID );

      gc.setBackground( new Color( display, 41, 128, 185 ) );
      drawThickArc( gc, cx, cy, spinRadius.getSelection(), spinStart.getSelection(), spinSweep.getSelection(),
          spinThick.getSelection(), btnRound.getSelection() );

      gc.setForeground( display.getSystemColor( SWT.COLOR_DARK_GRAY ) );
      gc.drawString( String.format( "R=%d  thick=%d  start=%d°  sweep=%d°  %s", spinRadius.getSelection(),
          spinThick.getSelection(), spinStart.getSelection(), spinSweep.getSelection(),
          btnRound.getSelection() ? "round caps" : "flat caps" ), 10, 10, true );
    } );

    SelectionListener redraw = SelectionListener.widgetSelectedAdapter( e -> canvas.redraw() );
    spinRadius.addSelectionListener( redraw );
    spinThick.addSelectionListener( redraw );
    spinStart.addSelectionListener( redraw );
    spinSweep.addSelectionListener( redraw );
    btnFlat.addSelectionListener( redraw );
    btnRound.addSelectionListener( redraw );

    shell.open();
    while( !shell.isDisposed() ) {
      if( !display.readAndDispatch() ) {
        display.sleep();
      }
    }
    display.dispose();
  }

  // =========================================================
  // Рисование толстой дуги
  // =========================================================

  /**
   * Рисует залитую дугу (кольцевой сегмент) через SWT Path. Система углов совпадает с SWT/математической: 0° = вправо,
   * положительный sweepDeg = против часовой стрелки.
   *
   * @param gc графический контекст
   * @param cx центр X
   * @param cy центр Y
   * @param radius радиус до середины дуги
   * @param startDeg начальный угол в градусах
   * @param sweepDeg угол дуги в градусах (+ против часовой, - по часовой)
   * @param thickness толщина дуги
   * @param roundCaps true = закруглённые концы, false = плоские
   */
  public static void drawThickArc( GC gc, int cx, int cy, int radius, float startDeg, float sweepDeg, int thickness,
      boolean roundCaps ) {
    Display display = Display.getCurrent();
    Path path = new Path( display );
    try {
      float half = thickness / 2.0f;
      float outer = radius + half;
      float inner = Math.max( 0.5f, radius - half );

      float[] ox1 = pointOn( cx, cy, outer, startDeg );
      float[] ix1 = pointOn( cx, cy, inner, startDeg );
      float[] ox2 = pointOn( cx, cy, outer, startDeg + sweepDeg );
      float[] ix2 = pointOn( cx, cy, inner, startDeg + sweepDeg );

      if( roundCaps ) {
        path.moveTo( ox1[0], ox1[1] );
        // Внешняя дуга
        path.addArc( cx - outer, cy - outer, outer * 2, outer * 2, startDeg, sweepDeg );
        // Полукруг на конечном торце: от ox2 к ix2
        addCapSemicircle( path, ox2[0], ox2[1], ix2[0], ix2[1], half, sweepDeg );
        // Внутренняя дуга обратно
        path.addArc( cx - inner, cy - inner, inner * 2, inner * 2, startDeg + sweepDeg, -sweepDeg );
        // Полукруг на начальном торце: от ix1 к ox1
        addCapSemicircle( path, ix1[0], ix1[1], ox1[0], ox1[1], half, sweepDeg );
      }
      else {
        path.moveTo( ox1[0], ox1[1] );
        path.addArc( cx - outer, cy - outer, outer * 2, outer * 2, startDeg, sweepDeg );
        path.lineTo( ix2[0], ix2[1] );
        path.addArc( cx - inner, cy - inner, inner * 2, inner * 2, startDeg + sweepDeg, -sweepDeg );
        path.close();
      }

      gc.fillPath( path );
    }
    finally {
      path.dispose();
    }
  }

  /**
   * Точка на окружности радиуса R при математическом угле angleDeg. Экранная Y-ось смотрит вниз, поэтому y = cy -
   * R*sin(a).
   */
  private static float[] pointOn( float cx, float cy, float R, float angleDeg ) {
    double a = Math.toRadians( angleDeg );
    return new float[] { cx + (float)(R * Math.cos( a )), cy - (float)(R * Math.sin( a )) };
  }

  /**
   * Добавляет полукруг-торец между точками A=(fromX,fromY) и B=(toX,toY). A и B — концы диаметра полукруга (внешняя и
   * внутренняя точки торца). Полукруг выступает НАРУЖУ от кольца. Выбор направления обхода: Вектор A→B смотрит внутрь
   * (от внешней к внутренней точке). sweepDeg > 0 (CCW-контур) → полукруг идёт по часовой: arcAngle = -180 sweepDeg < 0
   * (CW-контур) → полукруг идёт против часовой: arcAngle = +180 Угол вектора A→B вычисляется в математической системе
   * (Y вверх): atan2 принимает экранный dy с инвертированным знаком.
   */
  private static void addCapSemicircle( Path path, float fromX, float fromY, float toX, float toY, float radius,
      float sweepDeg ) {
    float mx = (fromX + toX) / 2f;
    float my = (fromY + toY) / 2f;

    // Угол вектора A→B в математической системе координат (инвертируем Y)
    float phiMath = (float)Math.toDegrees( Math.atan2( -(toY - fromY), toX - fromX ) );

    float startAngle = phiMath + 180f;
    float arcAngle = sweepDeg > 0 ? 180f : -180f;

    path.addArc( mx - radius, my - radius, radius * 2, radius * 2, startAngle, arcAngle );
  }
}

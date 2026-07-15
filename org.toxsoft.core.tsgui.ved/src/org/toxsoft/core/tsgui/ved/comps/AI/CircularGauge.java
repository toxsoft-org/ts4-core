package org.toxsoft.core.tsgui.ved.comps.AI;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * Круговая шкала (спидометр) на SWT. Параметры шкалы: - Диапазон значений : 0..360 - Дуга : 270°, начало снизу-слева
 * (225°), конец снизу-справа (135°) - Большие засечки : каждые 30 единиц шкалы - Подписи : базовая линия
 * перпендикулярна засечке (повёрнуты по радиусу) В SWT нет встроенного drawArc с произвольным углом поворота текста,
 * поэтому для поворота подписей используется Transform.
 */
public class CircularGauge {

  // ── Параметры шкалы ──────────────────────────────────────────────────────
  private static final double VALUE_MIN = 0;
  private static final double VALUE_MAX = 360;
  private static final double START_DEG = 225; // угол начала дуги (от оси X по часовой)
  private static final double ARC_DEG   = 270; // протяжённость дуги
  private static final double TICK_STEP = 30;  // шаг больших засечек (единицы шкалы)

  // ── Геометрия (в долях от радиуса) ───────────────────────────────────────
  private static final double TICK_INNER = 0.82; // внутренний конец засечки
  private static final double TICK_OUTER = 0.95; // внешний конец засечки (на дуге)
  private static final double LABEL_R    = 1.13; // радиус центра подписи

  public static void main( String[] args ) {
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setText( "Круговая шкала" );
    shell.setLayout( new FillLayout() );
    shell.setSize( 420, 420 );

    Canvas canvas = new Canvas( shell, SWT.DOUBLE_BUFFERED );
    canvas.addPaintListener( new GaugePainter() );

    shell.open();
    while( !shell.isDisposed() ) {
      if( !display.readAndDispatch() ) {
        display.sleep();
      }
    }
    display.dispose();
  }

  // ─────────────────────────────────────────────────────────────────────────
  static class GaugePainter
      implements PaintListener {

    @Override
    public void paintControl( PaintEvent e ) {
      GC gc = e.gc;
      Rectangle bounds = ((Canvas)e.widget).getBounds();

      int w = bounds.width;
      int h = bounds.height;

      // Центр и радиус
      int cx = w / 2;
      int cy = h / 2;
      int radius = (int)(Math.min( w, h ) * 0.36);

      gc.setAntialias( SWT.ON );
      gc.setTextAntialias( SWT.ON );

      drawBackground( gc, w, h );
      drawArc( gc, cx, cy, radius );
      drawTicksAndLabels( gc, cx, cy, radius );
      drawCenter( gc, cx, cy );
    }

    // ── Фон ──────────────────────────────────────────────────────────────
    private void drawBackground( GC gc, int w, int h ) {
      Display d = Display.getCurrent();
      Color bg = d.getSystemColor( SWT.COLOR_WHITE );
      gc.setBackground( bg );
      gc.fillRectangle( 0, 0, w, h );
    }

    // ── Дуга шкалы ───────────────────────────────────────────────────────
    private void drawArc( GC gc, int cx, int cy, int radius ) {
      Display d = Display.getCurrent();
      Color trackColor = new Color( d, 180, 178, 169 ); // #B4B2A9

      gc.setForeground( trackColor );
      gc.setLineWidth( 3 );

      // SWT drawArc: углы в градусах, отсчёт от 3 часов (0°) против часовой стрелки.
      // Нам нужна дуга от 225° до 225°+270°=495° ПО часовой →
      // в SWT: startAngle = -(225) = -225 → нормализуем: 360-225=135
      // sweepAngle = -270 (по часовой = отрицательный sweep в SWT)
      int swtStart = (int)(-START_DEG); // SWT считает против часовой
      int swtSweep = (int)(-ARC_DEG); // отрицательный = по часовой

      int d2 = radius * 2;
      gc.drawArc( cx - radius, cy - radius, d2, d2, swtStart, swtSweep );

      trackColor.dispose();
    }

    // ── Засечки и подписи ────────────────────────────────────────────────
    private void drawTicksAndLabels( GC gc, int cx, int cy, int radius ) {
      Display d = Display.getCurrent();
      Color tickColor = new Color( d, 95, 94, 90 ); // #5F5E5A
      Color labelColor = new Color( d, 44, 44, 42 ); // #2C2C2A

      Font font = new Font( d, "sans-serif", 9, SWT.NORMAL );
      gc.setFont( font );
      gc.setForeground( tickColor );
      gc.setLineWidth( 2 );

      for( double v = VALUE_MIN; v <= VALUE_MAX; v += TICK_STEP ) {

        double angleDeg = valueToAngle( v );
        double angleRad = Math.toRadians( angleDeg );

        double cosA = Math.cos( angleRad );
        double sinA = Math.sin( angleRad );

        // Конечные точки засечки
        int x1 = (int)Math.round( cx + cosA * radius * TICK_INNER );
        int y1 = (int)Math.round( cy + sinA * radius * TICK_INNER );
        int x2 = (int)Math.round( cx + cosA * radius * TICK_OUTER );
        int y2 = (int)Math.round( cy + sinA * radius * TICK_OUTER );

        gc.setForeground( tickColor );
        gc.drawLine( x1, y1, x2, y2 );

        // Центр подписи
        int lx = (int)Math.round( cx + cosA * radius * LABEL_R );
        int ly = (int)Math.round( cy + sinA * radius * LABEL_R );

        // Угол поворота текста: базовая линия ⊥ радиусу
        float rotDeg = labelRotation( angleDeg );

        drawRotatedLabel( gc, labelColor, String.valueOf( (int)v ), lx, ly, rotDeg );
      }

      font.dispose();
      tickColor.dispose();
      labelColor.dispose();
    }

    /**
     * Рисует текст повёрнутым вокруг точки (lx, ly). SWT Transform применяется к GC — сохраняем и восстанавливаем.
     */
    private void drawRotatedLabel( GC gc, Color color, String text, int lx, int ly, float rotDeg ) {
      Display d = Display.getCurrent();

      // Размер текста для центрирования
      Point extent = gc.textExtent( text );
      int tw = extent.x;
      int th = extent.y;

      Transform tr = new Transform( d );
      // Переносим начало координат в точку подписи, поворачиваем, рисуем со смещением −tw/2, −th/2
      tr.translate( lx, ly );
      tr.rotate( rotDeg );
      gc.setTransform( tr );

      gc.setForeground( color );
      // drawText без фона (SWT.DRAW_TRANSPARENT)
      gc.drawText( text, -tw / 2, -th / 2, true );

      // Восстанавливаем трансформацию
      gc.setTransform( null );
      tr.dispose();
    }

    // ── Центральная точка ────────────────────────────────────────────────
    private void drawCenter( GC gc, int cx, int cy ) {
      Display d = Display.getCurrent();
      Color c = new Color( d, 83, 74, 183 ); // #534AB7
      gc.setBackground( c );
      gc.fillOval( cx - 5, cy - 5, 10, 10 );
      c.dispose();
    }

    // ── Вспомогательные функции ──────────────────────────────────────────

    /**
     * Переводит значение шкалы в угол (в градусах, СВГ-система координат, ось Y вниз, отсчёт по часовой стрелке от оси
     * X).
     */
    private double valueToAngle( double value ) {
      double t = (value - VALUE_MIN) / (VALUE_MAX - VALUE_MIN);
      return START_DEG + t * ARC_DEG;
    }

    /**
     * Угол поворота текста (в градусах) для SWT Transform.rotate(). Базовая линия подписи перпендикулярна радиусу.
     * Корректируется, чтобы текст не был перевёрнутым.
     */
    private float labelRotation( double angleDeg ) {
      double rot = angleDeg + 90;
      // Нормализуем
      rot = ((rot + 180) % 360) - 180;
      if( rot > 90 ) {
        rot -= 180;
      }
      if( rot < -90 ) {
        rot += 180;
      }
      return (float)rot;
    }
  }
}

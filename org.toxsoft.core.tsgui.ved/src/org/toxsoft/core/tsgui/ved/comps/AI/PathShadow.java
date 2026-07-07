package org.toxsoft.core.tsgui.ved.comps.AI;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class PathShadow {

  // ── Параметры тени ───────────────────────────────────────────
  static final int   SHADOW_LAYERS = 40;   // слоёв размытия
  static final float SHADOW_SPREAD = 0.8f; // растяжение тени на слой (px)
  static final int   SHADOW_ALPHA  = 12;   // прозрачность каждого слоя
  static final float SHADOW_DIST   = 60f;  // длина тени (смещение)

  // ── Текст ────────────────────────────────────────────────────
  static final String TEXT      = "Hello";
  static final int    FONT_SIZE = 120;

  public static void main( String[] args ) {
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setText( "Path Shadow — SWT" );
    shell.setLayout( new GridLayout( 1, false ) );
    shell.setSize( 700, 500 );

    // Слайдер угла
    Composite controls = new Composite( shell, SWT.NONE );
    controls.setLayout( new RowLayout( SWT.HORIZONTAL ) );
    controls.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );

    new Label( controls, SWT.NONE ).setText( "Угол света:" );
    Slider slider = new Slider( controls, SWT.HORIZONTAL );
    slider.setMinimum( 0 );
    slider.setMaximum( 369 ); // 369 = 360 + thumb size
    slider.setThumb( 9 );
    slider.setSelection( 45 );
    slider.setLayoutData( new RowData( 300, SWT.DEFAULT ) );

    Label angleLabel = new Label( controls, SWT.NONE );
    angleLabel.setText( "45°" );
    angleLabel.setLayoutData( new RowData( 40, SWT.DEFAULT ) );

    // Canvas
    Canvas canvas = new Canvas( shell, SWT.DOUBLE_BUFFERED );
    canvas.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );

    // Держим угол в массиве чтобы использовать в лямбде
    double[] lightAngle = { Math.toRadians( 45 ) };

    slider.addListener( SWT.Selection, e -> {
      int deg = slider.getSelection();
      angleLabel.setText( deg + "°" );
      lightAngle[0] = Math.toRadians( deg );
      canvas.redraw();
    } );

    canvas.addPaintListener( e -> {
      GC gc = e.gc;
      gc.setAntialias( SWT.ON );
      gc.setTextAntialias( SWT.ON );

      Rectangle area = canvas.getClientArea();
      int W = area.width, H = area.height;

      // ── Фон ─────────────────────────────────────────────
      gc.setBackground( display.getSystemColor( SWT.COLOR_WHITE ) );
      gc.fillRectangle( area );

      // ── Позиция текста (по центру) ───────────────────────
      FontData fd = display.getSystemFont().getFontData()[0];
      fd.setHeight( FONT_SIZE );
      fd.setStyle( SWT.BOLD );
      Font font = new Font( display, fd );
      gc.setFont( font );

      Point textSize = gc.textExtent( TEXT );
      int tx = (W - textSize.x) / 2;
      int ty = (H - textSize.y) / 2;

      // ── Строим Path из текста ────────────────────────────
      Path textPath = new Path( display );
      textPath.addString( TEXT, tx, ty, font );

      // ── Направление тени (от объекта ПРОТИВ света) ──────
      // Угол = направление НА источник, тень падает в обратную сторону
      double shadowDx = -Math.cos( lightAngle[0] );
      double shadowDy = -Math.sin( lightAngle[0] );

      // ── Рисуем тень слоями (мягкий градиент) ────────────
      // Каждый слой: смещённый и расширенный Path, полупрозрачный серый
      gc.setAdvanced( true );
      gc.setAlpha( SHADOW_ALPHA );
      gc.setBackground( new Color( display, 0, 0, 0 ) );

      for( int layer = SHADOW_LAYERS; layer >= 1; layer-- ) {
        float t = (float)layer / SHADOW_LAYERS;

        // Смещение тени — нарастает от 0 до SHADOW_DIST
        float offX = (float)(shadowDx * SHADOW_DIST * t);
        float offY = (float)(shadowDy * SHADOW_DIST * t);

        // Расширение контура — имитирует размытие
        float spread = SHADOW_SPREAD * (SHADOW_LAYERS - layer + 1);

        // Строим смещённый Path вручную через трансформ
        Transform tr = new Transform( display );
        tr.translate( offX, offY );
        gc.setTransform( tr );

        // Заливаем оригинальный Path со смещением через трансформ
        gc.fillPath( textPath );

        // Дополнительный contour expand: рисуем линию по Path
        // с нарастающей толщиной для имитации размытого края
        gc.setLineWidth( (int)(spread * 2) );
        gc.drawPath( textPath );

        tr.dispose();
      }

      // Сбрасываем трансформ и прозрачность
      gc.setTransform( null );
      gc.setAlpha( 255 );

      // ── Рисуем сам объект поверх тени ────────────────────
      gc.setBackground( new Color( display, 30, 100, 200 ) );
      gc.fillPath( textPath );

      gc.setForeground( new Color( display, 10, 60, 150 ) );
      gc.setLineWidth( 1 );
      gc.drawPath( textPath );

      // ── Рисуем стрелку направления света ─────────────────
      gc.setAlpha( 200 );
      gc.setForeground( new Color( display, 255, 160, 0 ) );
      gc.setLineWidth( 2 );

      int arrowX = 50, arrowY = 50, arrowLen = 40;
      int arrowEndX = (int)(arrowX + Math.cos( lightAngle[0] ) * arrowLen);
      int arrowEndY = (int)(arrowY + Math.sin( lightAngle[0] ) * arrowLen);
      gc.drawLine( arrowX, arrowY, arrowEndX, arrowEndY );
      gc.fillOval( arrowEndX - 5, arrowEndY - 5, 10, 10 );

      gc.setForeground( display.getSystemColor( SWT.COLOR_DARK_GRAY ) );
      gc.setLineWidth( 1 );
      gc.setAlpha( 255 );
      gc.drawText( "Свет", arrowX - 10, arrowY - 20, true );

      // ── Освобождаем ресурсы ──────────────────────────────
      textPath.dispose();
      font.dispose();
    } );

    shell.open();
    while( !shell.isDisposed() ) {
      if( !display.readAndDispatch() ) {
        display.sleep();
      }
    }
    display.dispose();
  }
}

package org.toxsoft.core.tsgui.ved.comps.AI;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * SWT-приложение: интерактивная демонстрация цветной тени для текстового контура. Параметры настраиваются через
 * элементы управления в окне: – текст – цвет тени (RGB-слайдеры) – радиус размытия – смещение тени (X/Y) – размер
 * шрифта
 */
public class ShadowDemo {

  // ── Состояние UI ──────────────────────────────────────────────────────────
  private String text       = "Hello";
  private int    fontSize   = 72;
  private int    blurRadius = 8;
  private int    offsetX    = 6;
  private int    offsetY    = 8;
  private int    shadowR    = 80;
  private int    shadowG    = 0;
  private int    shadowB    = 200;

  // Кешированные ресурсы (пересоздаются при изменении параметров)
  private Image shadowImage;
  private Font  font;

  // ── Canvas-область ────────────────────────────────────────────────────────
  private Canvas  canvas;
  private Display display;

  // ─────────────────────────────────────────────────────────────────────────

  public static void main( String[] args ) {
    new ShadowDemo().run();
  }

  public void run() {
    display = new Display();
    Shell shell = new Shell( display );
    shell.setText( "SWT Coloured Shadow Demo" );
    shell.setLayout( new GridLayout( 1, false ) );
    shell.setMinimumSize( 680, 580 );

    buildCanvas( shell );
    buildControls( shell );

    shell.pack();
    shell.setSize( 700, 620 );
    shell.open();

    refreshResources();

    while( !shell.isDisposed() ) {
      if( !display.readAndDispatch() ) {
        display.sleep();
      }
    }

    disposeCached();
    display.dispose();
  }

  // ── Холст ─────────────────────────────────────────────────────────────────

  private void buildCanvas( Composite parent ) {
    canvas = new Canvas( parent, SWT.DOUBLE_BUFFERED | SWT.BORDER );
    GridData gd = new GridData( SWT.FILL, SWT.FILL, true, true );
    gd.heightHint = 200;
    canvas.setLayoutData( gd );

    canvas.setBackground( display.getSystemColor( SWT.COLOR_WHITE ) );

    canvas.addPaintListener( e -> {
      if( shadowImage == null || font == null ) {
        return;
      }

      Rectangle ca = canvas.getClientArea();

      // Вычисляем размер текста для центрирования
      e.gc.setFont( font );
      Point textSize = e.gc.textExtent( text );

      int textX = (ca.width - textSize.x) / 2;
      int textY = (ca.height - textSize.y) / 2;

      // Рисуем тень со смещением (тень позади текста)
      e.gc.drawImage( shadowImage, textX + offsetX - blurRadius, textY + offsetY - blurRadius );

      // Рисуем текст поверх
      e.gc.setFont( font );
      e.gc.setForeground( display.getSystemColor( SWT.COLOR_BLACK ) );
      e.gc.setAntialias( SWT.ON );
      e.gc.drawText( text, textX, textY, true );
    } );
  }

  // ── Панель управления ─────────────────────────────────────────────────────

  private void buildControls( Composite parent ) {
    Group grp = new Group( parent, SWT.NONE );
    grp.setText( "Параметры" );
    grp.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false ) );
    grp.setLayout( new GridLayout( 4, false ) );

    // Текст
    addLabel( grp, "Текст:" );
    Text textField = new Text( grp, SWT.BORDER );
    textField.setText( text );
    textField.setLayoutData( span( 3 ) );
    textField.addModifyListener( e -> {
      text = textField.getText();
      refreshResources();
    } );

    // Размер шрифта
    addLabel( grp, "Размер шрифта:" );
    Spinner spFont = new Spinner( grp, SWT.BORDER );
    spFont.setMinimum( 12 );
    spFont.setMaximum( 200 );
    spFont.setSelection( fontSize );
    spFont.setLayoutData( span( 3 ) );
    spFont.addSelectionListener( sel( () -> {
      fontSize = spFont.getSelection();
      refreshResources();
    } ) );

    // Радиус blur
    addLabel( grp, "Blur radius:" );
    Slider slBlur = slider( grp, 0, 40, blurRadius );
    Label lbBlur = new Label( grp, SWT.NONE );
    lbBlur.setText( blurRadius + " px" );
    new Label( grp, SWT.NONE ); // заглушка
    slBlur.addSelectionListener( sel( () -> {
      blurRadius = slBlur.getSelection();
      lbBlur.setText( blurRadius + " px" );
      refreshResources();
    } ) );

    // Смещение X
    addLabel( grp, "Смещение X:" );
    Slider slX = slider( grp, -50, 50, offsetX );
    Label lbX = new Label( grp, SWT.NONE );
    lbX.setText( offsetX + " px" );
    new Label( grp, SWT.NONE );
    slX.addSelectionListener( sel( () -> {
      offsetX = slX.getSelection();
      lbX.setText( offsetX + " px" );
      canvas.redraw();
    } ) );

    // Смещение Y
    addLabel( grp, "Смещение Y:" );
    Slider slY = slider( grp, -50, 50, offsetY );
    Label lbY = new Label( grp, SWT.NONE );
    lbY.setText( offsetY + " px" );
    new Label( grp, SWT.NONE );
    slY.addSelectionListener( sel( () -> {
      offsetY = slY.getSelection();
      lbY.setText( offsetY + " px" );
      canvas.redraw();
    } ) );

    // Цвет R
    addLabel( grp, "Цвет R:" );
    Slider slR = slider( grp, 0, 255, shadowR );
    Label lbR = new Label( grp, SWT.NONE );
    lbR.setText( shadowR + "" );
    new Label( grp, SWT.NONE );
    slR.addSelectionListener( sel( () -> {
      shadowR = slR.getSelection();
      lbR.setText( shadowR + "" );
      refreshResources();
    } ) );

    // Цвет G
    addLabel( grp, "Цвет G:" );
    Slider slG = slider( grp, 0, 255, shadowG );
    Label lbG = new Label( grp, SWT.NONE );
    lbG.setText( shadowG + "" );
    new Label( grp, SWT.NONE );
    slG.addSelectionListener( sel( () -> {
      shadowG = slG.getSelection();
      lbG.setText( shadowG + "" );
      refreshResources();
    } ) );

    // Цвет B
    addLabel( grp, "Цвет B:" );
    Slider slB = slider( grp, 0, 255, shadowB );
    Label lbB = new Label( grp, SWT.NONE );
    lbB.setText( shadowB + "" );
    new Label( grp, SWT.NONE );
    slB.addSelectionListener( sel( () -> {
      shadowB = slB.getSelection();
      lbB.setText( shadowB + "" );
      refreshResources();
    } ) );
  }

  // ── Пересоздание ресурсов ─────────────────────────────────────────────────

  private void refreshResources() {
    disposeCached();

    font = new Font( display, "Arial", fontSize, SWT.BOLD );

    // Вычисляем размер контура текста
    GC measureGC = new GC( canvas );
    measureGC.setFont( font );
    measureGC.setAntialias( SWT.ON );
    Point size = measureGC.textExtent( text );
    measureGC.dispose();

    int padding = blurRadius * 2 + 4;
    int imgW = size.x + padding * 2;
    int imgH = size.y + padding * 2;

    // Строим Path из текста
    Path path = new Path( display );
    path.addString( text, padding, padding, font );

    shadowImage = ShadowRenderer.createColoredShadow( display, path, imgW, imgH, new RGB( shadowR, shadowG, shadowB ),
        blurRadius );

    path.dispose();
    canvas.redraw();
  }

  private void disposeCached() {
    if( shadowImage != null ) {
      shadowImage.dispose();
      shadowImage = null;
    }
    if( font != null ) {
      font.dispose();
      font = null;
    }
  }

  // ── Вспомогательные методы ────────────────────────────────────────────────

  private static void addLabel( Composite p, String text ) {
    Label l = new Label( p, SWT.NONE );
    l.setText( text );
  }

  private static Slider slider( Composite p, int min, int max, int sel ) {
    Slider s = new Slider( p, SWT.HORIZONTAL );
    s.setMinimum( min );
    s.setMaximum( max + 10 );
    s.setThumb( 10 );
    s.setPageIncrement( 5 );
    s.setIncrement( 1 );
    s.setSelection( sel );
    GridData gd = new GridData( SWT.FILL, SWT.CENTER, true, false );
    gd.minimumWidth = 160;
    s.setLayoutData( gd );
    return s;
  }

  private static GridData span( int cols ) {
    GridData gd = new GridData( SWT.FILL, SWT.CENTER, true, false );
    gd.horizontalSpan = cols;
    return gd;
  }

  private static SelectionListener sel( Runnable r ) {
    return new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        r.run();
      }
    };
  }
}

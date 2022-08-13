package org.toxsoft.core.tsgui.valed.controls.graphics;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tslib.bricks.events.change.*;

public class RgbaSelector
    extends Canvas
    implements IGenericChangeEventCapable {

  static class ColorBackground
      extends Canvas {

    int width  = 8;
    int height = 8;

    private final Color lightColor;
    private final Color darkColor;

    Color color = null;
    int   alpha = 255;

    Color colorBlack;
    Color colorWhite;

    ColorBackground( Composite aParent, Color aLightColor, Color aDarkColor, ITsColorManager aColorManager ) {
      super( aParent, SWT.NO_BACKGROUND | SWT.DOUBLE_BUFFERED );

      lightColor = aLightColor;
      darkColor = aDarkColor;

      colorBlack = aColorManager.getColor( ETsColor.BLACK );
      colorWhite = aColorManager.getColor( ETsColor.WHITE );

      addPaintListener( aEvent -> {
        Point p = getSize();
        int rows = p.y / 8 + 1;
        int columns = p.x / 8 + 1;
        for( int i = 0; i < columns; i++ ) {
          for( int j = 0; j < rows; j++ ) {
            if( (i + j) % 2 == 1 ) {
              aEvent.gc.setBackground( darkColor );
            }
            else {
              aEvent.gc.setBackground( lightColor );
            }
            aEvent.gc.fillRectangle( i * 8, j * 8, width, height );
          }
        }
        if( color != null ) {
          aEvent.gc.setAlpha( alpha );
          aEvent.gc.setBackground( color );
          aEvent.gc.fillRectangle( 0, 0, p.x, p.y );
        }
        aEvent.gc.setForeground( colorBlack );
        aEvent.gc.drawRectangle( 0, 0, p.x - 1, p.y - 1 );
        aEvent.gc.setForeground( colorWhite );
        aEvent.gc.drawRectangle( 1, 1, p.x - 3, p.y - 3 );
      } );

    }

    void setColor( Color aColor, int aAlpha ) {
      color = aColor;
      alpha = aAlpha;
    }

  }

  Spinner redSpin;
  Spinner greenSpin;
  Spinner blueSpin;
  Spinner alphaSpin;

  Slider redSlide;
  Slider greenSlide;
  Slider blueSlide;
  Slider alphaSlide;

  private final ITsColorManager cm;

  private final ColorBackground cb;

  private GenericChangeEventer changeEventer;

  public RgbaSelector( Composite aParent, int aStyle, IEclipseContext aAppContext ) {
    super( aParent, aStyle );

    changeEventer = new GenericChangeEventer( this );

    setLayout( new FillLayout() );

    Group g = new Group( this, SWT.NONE );
    g.setText( "Компоненты цвета" );

    GridLayout gd = new GridLayout( 4, false );

    g.setLayout( gd );

    redSlide = new Slider( g, SWT.HORIZONTAL );
    redSlide.setValues( 0, 0, 256, 1, 1, 10 );
    redSlide.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        redSpin.setSelection( redSlide.getSelection() );
        onColorChanged();
      }
    } );

    redSpin = createRow( g, "red" );
    redSpin.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        onColorChanged();
        redSlide.setSelection( redSpin.getSelection() );
      }
    } );

    cm = aAppContext.get( ITsColorManager.class );
    cb = new ColorBackground( g, cm.getColor( ETsColor.GRAY ), cm.getColor( ETsColor.DARK_GRAY ), cm );
    cb.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true, 1, 4 ) );

    greenSlide = new Slider( g, SWT.HORIZONTAL );
    greenSlide.setValues( 0, 0, 256, 1, 1, 10 );
    greenSlide.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        greenSpin.setSelection( greenSlide.getSelection() );
        onColorChanged();
      }
    } );
    greenSpin = createRow( g, "green" );
    greenSpin.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        onColorChanged();
        greenSlide.setSelection( greenSpin.getSelection() );
      }
    } );

    blueSlide = new Slider( g, SWT.HORIZONTAL );
    blueSlide.setValues( 0, 0, 256, 1, 1, 10 );
    blueSlide.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        blueSpin.setSelection( blueSlide.getSelection() );
        onColorChanged();
      }
    } );
    blueSpin = createRow( g, "blue" );
    blueSpin.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        onColorChanged();
        blueSlide.setSelection( blueSpin.getSelection() );
      }
    } );

    alphaSlide = new Slider( g, SWT.HORIZONTAL );
    alphaSlide.setValues( 0, 0, 256, 1, 1, 10 );
    alphaSlide.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        alphaSpin.setSelection( alphaSlide.getSelection() );
        onColorChanged();
      }
    } );
    alphaSpin = createRow( g, "alpha" );
    alphaSpin.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        onColorChanged();
        alphaSlide.setSelection( alphaSpin.getSelection() );
      }
    } );

    alphaSpin.setSelection( 255 );
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return changeEventer;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Задает значение цвета с прозрачностью.
   *
   * @param aRgba RGBA - значение цвета с прозрачностью
   */
  public void setRgba( RGBA aRgba ) {
    if( aRgba != null ) {
      redSpin.setSelection( aRgba.rgb.red );
      greenSpin.setSelection( aRgba.rgb.green );
      blueSpin.setSelection( aRgba.rgb.blue );
      alphaSpin.setSelection( aRgba.alpha );
      redSlide.setSelection( aRgba.rgb.red );
      greenSlide.setSelection( aRgba.rgb.green );
      blueSlide.setSelection( aRgba.rgb.blue );
      alphaSlide.setSelection( aRgba.alpha );
    }
    onColorChanged();
  }

  /**
   * Возвращает значение цвета с прозрачностью
   *
   * @return RGBA - значение цвета с прозрачностью
   */
  public RGBA rgba() {
    int r = redSpin.getSelection();
    int g = greenSpin.getSelection();
    int b = blueSpin.getSelection();
    int a = alphaSpin.getSelection();
    return new RGBA( r, g, b, a );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private Spinner createRow( Composite aParent, String aText ) {
    CLabel l;
    Spinner spin;

    l = new CLabel( aParent, SWT.NONE );
    l.setText( aText );
    spin = new Spinner( aParent, SWT.BORDER );
    spin.setMinimum( 0 );
    spin.setMaximum( 255 );

    return spin;
  }

  private void onColorChanged() {

    int r = redSpin.getSelection();
    int g = greenSpin.getSelection();
    int b = blueSpin.getSelection();
    int a = alphaSpin.getSelection();

    Color c = cm.getColor( r, g, b );
    cb.setColor( c, a );
    cb.redraw();

    changeEventer.fireChangeEvent();
  }

}

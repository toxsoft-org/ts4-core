package org.toxsoft.tsgui.widgets.mpv.impl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * Small pane with two buttons "up" and "down" like {@link Spinner} right buttons.
 * <p>
 * You need to add mouse listener to use buttons.
 *
 * @author hazard157
 */
public class VertTwoButtonsPane
    extends Canvas {

  /**
   * Положение точки относительно этого контроля для метода {@link VertTwoButtonsPane#whereIsPoint(int, int)}.
   *
   * @author goga
   */
  public enum EPointLocation {
    /**
     * Точка вне контроля.
     */
    OUTSIDE,
    /**
     * Точка в области "вверх".
     */
    UP,
    /**
     * Точка в области "вниз".
     */
    DOWN
  }

  private final PaintListener paintListener = new PaintListener() {

    @Override
    public void paintControl( PaintEvent aEvent ) {
      doPaint( aEvent );
    }
  };

  /**
   * Параметры рисования стрелок на верхней и нижной кнопке.
   */
  int   arrowX    = 8;  // x-координата середины стрелок
  int   arrow1Y   = 8;  // y-координата середины верхней стрелки "вверх"
  int   arrow2Y   = 24; // y-координата середины нижней стрелки "вниз"
  int   arrowSize = 4;  // размер стрелки (ширина_стрелки=2*arrowSize, высота_стрелки=arrowSize)
  Color arrowColor;     // цвет стрелки

  /**
   * Конструктор.
   *
   * @param aParent {@link Composite} - родительская компонента
   */
  public VertTwoButtonsPane( Composite aParent ) {
    super( aParent, SWT.NO_FOCUS | SWT.BORDER );
    arrowColor = new Color( getDisplay(), 0x4d, 0x4d, 0x4d );
    this.addPaintListener( paintListener );
    this.addControlListener( new ControlListener() {

      @Override
      public void controlResized( ControlEvent aEvent ) {
        Point size = getSize();
        arrowX = size.x / 2 - 1;
        arrowSize = size.x / 4;
        arrow1Y = size.y * 1 / 4 + arrowSize * 2 / 3;
        arrow2Y = size.y * 3 / 4 - arrowSize * 2 / 3 - 1;
      }

      @Override
      public void controlMoved( ControlEvent aEvent ) {
        // nop
      }
    } );
  }

  @Override
  public void dispose() {
    if( arrowColor != null ) {
      arrowColor.dispose();
      arrowColor = null;
    }
    super.dispose();
  }

  void drawUpArrow( GC aGc, int aX, int aY, int aSize ) {
    aGc.drawLine( aX - aSize, aY, aX, aY - aSize );
    aGc.drawLine( aX, aY - aSize, aX + aSize, aY );
  }

  void drawDownArrow( GC aGc, int aX, int aY, int aSize ) {
    aGc.drawLine( aX - aSize, aY, aX, aY + aSize );
    aGc.drawLine( aX, aY + aSize, aX + aSize, aY );
  }

  void doPaint( PaintEvent aEvent ) {
    aEvent.gc.setForeground( arrowColor );
    aEvent.gc.setLineWidth( 1 );
    aEvent.gc.setLineCap( SWT.CAP_FLAT );
    drawUpArrow( aEvent.gc, arrowX, arrow1Y, arrowSize );
    drawDownArrow( aEvent.gc, arrowX, arrow2Y, arrowSize );
  }

  @Override
  public Point computeSize( int wHint, int hHint, boolean changed ) {
    int w = 20;
    int h = 16;
    if( hHint != SWT.DEFAULT ) {
      h = hHint;
      w = h * 2 / 3;
    }
    return new Point( w, h );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Определяет, где находится точка.
   *
   * @param aX int - x-координата относительно этого контроля
   * @param aY int - y-координата относительно этого
   * @return {@link EPointLocation} - положение точки на контроле:
   */
  public EPointLocation whereIsPoint( int aX, int aY ) {
    Point size = this.getSize();
    if( aX < 0 || aX > size.x || aY < 0 || aY > size.y ) {
      return EPointLocation.OUTSIDE;
    }
    if( aY <= size.x / 2 ) {
      return EPointLocation.UP;
    }
    return EPointLocation.DOWN;
  }

}

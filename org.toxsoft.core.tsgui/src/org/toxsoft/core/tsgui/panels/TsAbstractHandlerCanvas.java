package org.toxsoft.core.tsgui.panels;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;

/**
 * Helpful extension of {@link Canvas} for painting.
 *
 * @author hazard157
 */
public abstract class TsAbstractHandlerCanvas
    extends TsAbstractCanvas {

  private final ControlListener resizeListener = new ControlAdapter() {

    @Override
    public void controlResized( ControlEvent aEvent ) {
      doControlResized( aEvent );
    }
  };

  private final MouseListener mouseListener = new MouseAdapter() {

    @Override
    public void mouseDown( MouseEvent aEvent ) {
      doMouseDown( aEvent );
    }

    @Override
    public void mouseDoubleClick( MouseEvent aEvent ) {
      doMouseDoubleClick( aEvent );
    }

  };

  /**
   * Constructor.
   *
   * @param aParent {@link Composite} - parent composite
   * @param aStyle int - {@link SWT} style of canvas
   * @param aContext {@link ITsGuiContext} - the context
   */
  public TsAbstractHandlerCanvas( Composite aParent, int aStyle, ITsGuiContext aContext ) {
    super( aParent, aStyle, aContext );
    this.addListener( SWT.KeyDown, this::doKeyPressed );
    this.addControlListener( resizeListener );
    this.addMouseListener( mouseListener );
  }

  /**
   * Constructor with no style bits.
   *
   * @param aParent {@link Composite} - parent composite
   * @param aContext {@link ITsGuiContext} - the context
   */
  public TsAbstractHandlerCanvas( Composite aParent, ITsGuiContext aContext ) {
    this( aParent, SWT.NONE, aContext );
  }

  // ------------------------------------------------------------------------------------
  // To implements
  //

  /**
   * Subclass may handle mouse left button press event.
   *
   * @param aEvent {@link MouseEvent} - the event
   */
  protected void doMouseDown( MouseEvent aEvent ) {
    // nop
  }

  /**
   * Subclass may handle mouse left button double click event.
   *
   * @param aEvent {@link MouseEvent} - the event
   */
  protected void doMouseDoubleClick( MouseEvent aEvent ) {
    // nop
  }

  /**
   * Subclass may handle keyboard event on canvas.
   *
   * @param aEvent {@link Event} - the keybard event
   */
  protected void doKeyPressed( Event aEvent ) {
    // nop
  }

  /**
   * Subclass may handle the control resize.
   *
   * @param aEvent {@link ControlEvent} - the conrol resize event
   */
  protected void doControlResized( ControlEvent aEvent ) {
    // nop
  }

}

package org.toxsoft.core.tsgui.panels;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Helpful extension of {@link Canvas} for painting.
 *
 * @author hazard157
 */
public abstract class TsAbstractCanvas
    extends Canvas
    implements ITsGuiContextable {

  private final ITsGuiContext tsContext;

  /**
   * Constructor.
   *
   * @param aParent {@link Composite} - parent composite
   * @param aStyle int - {@link SWT} style of canvas
   * @param aContext {@link ITsGuiContext} - the context
   */
  public TsAbstractCanvas( Composite aParent, int aStyle, ITsGuiContext aContext ) {
    super( aParent, aStyle );
    tsContext = TsNullArgumentRtException.checkNull( aContext );
    this.addPaintListener( aE -> paint( aE.gc, new TsRectangle( aE.x, aE.y, aE.width, aE.height ) ) );
  }

  /**
   * Constructor with no style bits.
   *
   * @param aParent {@link Composite} - parent composite
   * @param aContext {@link ITsGuiContext} - the context
   */
  public TsAbstractCanvas( Composite aParent, ITsGuiContext aContext ) {
    this( aParent, SWT.NONE, aContext );
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // To implements
  //

  /**
   * Subclass must draw on canvas.
   *
   * @param aGc {@link GC} - the graphics context
   * @param aPaintBounds {@link ITsRectangle} - rectangle region that need to be painted
   */
  public abstract void paint( GC aGc, ITsRectangle aPaintBounds );

  /**
   * Subclass may perform clean-up including release of the resources.
   * <p>
   * Does nothing in base class.
   */
  protected void doDispose() {
    // nop
  }

}

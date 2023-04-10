package org.toxsoft.core.tsgui.panels;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Base class for composites implementing {@link ITsGuiContextable}.
 *
 * @author hazard157
 */
public class TsPanel
    extends TsComposite
    implements ITsGuiContextable {

  private final ITsGuiContext tsContext;

  /**
   * Constructor.
   * <p>
   * Constructor stores reference to the context, does not creates copy.
   *
   * @param aParent {@link Composite} - parent component
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TsPanel( Composite aParent, ITsGuiContext aContext ) {
    this( aParent, aContext, SWT.NONE );
  }

  /**
   * Constructor.
   * <p>
   * Constructor stores reference to the context, does not creates copy.
   *
   * @param aParent {@link Composite} - parent component
   * @param aContext {@link ITsGuiContext} - the context
   * @param aStyle int - SWT style of composite to be created
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TsPanel( Composite aParent, ITsGuiContext aContext, int aStyle ) {
    super( aParent, aStyle );
    tsContext = TsNullArgumentRtException.checkNull( aContext );
    addDisposeListener( aE -> doDispose() );
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContext
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * Subclass may perform clean-up including release of the resources.
   * <p>
   * Does nothing in base class.
   */
  protected void doDispose() {
    // nop
  }

}

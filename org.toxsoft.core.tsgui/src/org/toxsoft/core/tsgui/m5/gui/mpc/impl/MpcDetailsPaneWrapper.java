package org.toxsoft.core.tsgui.m5.gui.mpc.impl;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IMpcDetailsPane} implementation that wraps over {@link IM5EntityPanel}.
 *
 * @author hazard157
 * @param <T> - displayed M5-modeled entity type
 */
public class MpcDetailsPaneWrapper<T>
    extends MpcAbstractPane<T, Control>
    implements IMpcDetailsPane<T> {

  private final IM5EntityPanel<T> panel;

  /**
   * Constructor.
   *
   * @param aOwner {@link MultiPaneComponent} - the owner component
   * @param aPanel {@link IM5EntityPanel} - panel to be wrapped
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException <code>aPanel</code> has already initialized {@link ILazyControl#getControl()}
   */
  public MpcDetailsPaneWrapper( MultiPaneComponent<T> aOwner, IM5EntityPanel<T> aPanel ) {
    super( aOwner );
    TsNullArgumentRtException.checkNull( aPanel );
    TsIllegalArgumentRtException.checkTrue( aPanel.getControl() != null );
    panel = aPanel;
  }

  // ------------------------------------------------------------------------------------
  // MpcAbstractPane
  //

  @Override
  protected Control doCreateControl( Composite aParent ) {
    return panel.createControl( aParent );
  }

  // ------------------------------------------------------------------------------------
  // IMpcDetailsPane
  //

  @Override
  public void setValues( ITsNode aSelectedNode, IM5Bunch<T> aValues ) {
    panel.setValues( aValues );
  }

}

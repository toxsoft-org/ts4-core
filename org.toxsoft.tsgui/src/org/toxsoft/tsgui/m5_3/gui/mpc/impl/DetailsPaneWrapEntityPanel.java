package org.toxsoft.tsgui.m5_3.gui.mpc.impl;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.toxsoft.tsgui.bricks.tstree.ITsNode;
import org.toxsoft.tsgui.m5_3.IM5Bunch;
import org.toxsoft.tsgui.m5_3.gui.panels.IM5EntityPanel;
import org.toxsoft.tsgui.panels.lazy.ILazyControl;
import org.toxsoft.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link IMpcDetailsPane} implementation that wraps over {@link IM5EntityPanel}.
 *
 * @author hazard157
 * @param <T> - displayed M5-modelled entity type
 */
public class DetailsPaneWrapEntityPanel<T>
    extends AbstractMpcPane<T, Control>
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
  public DetailsPaneWrapEntityPanel( MultiPaneComponent<T> aOwner, IM5EntityPanel<T> aPanel ) {
    super( aOwner );
    TsNullArgumentRtException.checkNull( aPanel );
    TsIllegalArgumentRtException.checkTrue( aPanel.getControl() != null );
    panel = aPanel;
  }

  // ------------------------------------------------------------------------------------
  // AbstractMpcPane
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

package org.toxsoft.tsgui.m5_1.impl.gui.mpc.impl;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.toxsoft.tsgui.bricks.tstree.ITsNode;
import org.toxsoft.tsgui.m5_1.api.IM5Bunch;
import org.toxsoft.tsgui.m5_1.gui.IM5EntityPanel;
import org.toxsoft.tsgui.m5_1.impl.gui.mpc.IMpsDetailsPane;
import org.toxsoft.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Реализация {@link IMpsDetailsPane} по умолчанию на основе панели {@link IM5EntityPanel}.
 *
 * @author hazard157
 * @param <T> - displayed M5-modelled entity type
 */
public class DefaultDetailsPane<T>
    extends AbstractPane<T, Control>
    implements IMpsDetailsPane<T> {

  private final IM5EntityPanel<T> panel;

  /**
   * Конструктор.
   *
   * @param aOwner {@link BasicMultiPaneComponent} - компонента, создающая эту панель
   * @param aPanel {@link IM5EntityPanel} - оборачиваемая панель
   * @throws TsNullArgumentRtException aOwner = <code>null</code>
   */
  public DefaultDetailsPane( BasicMultiPaneComponent<T> aOwner, IM5EntityPanel<T> aPanel ) {
    super( aOwner );
    TsNullArgumentRtException.checkNull( aPanel );
    TsIllegalArgumentRtException.checkTrue( aPanel.getControl() != null );
    panel = aPanel;
  }

  @Override
  protected Control doCreateControl( Composite aParent ) {
    return panel.createControl( aParent );
  }

  @Override
  public void setValues( ITsNode aSelectedNode, IM5Bunch<T> aValues ) {
    panel.setValues( aValues );
  }

}

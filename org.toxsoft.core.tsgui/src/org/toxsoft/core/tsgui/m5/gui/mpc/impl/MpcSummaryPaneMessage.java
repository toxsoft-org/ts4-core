package org.toxsoft.core.tsgui.m5.gui.mpc.impl;

import static org.toxsoft.core.tsgui.m5.gui.mpc.impl.ITsResources.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponent;
import org.toxsoft.core.tsgui.utils.layout.BorderLayout;
import org.toxsoft.core.tsgui.widgets.TsComposite;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link IMpcSummaryPane} implementation displaying string provided by the specified {@link IMessageProvider}.
 *
 * @author hazard157
 * @param <T> - displayed M5-modelled entity type
 */
public class MpcSummaryPaneMessage<T>
    extends MpcAbstractPane<T, Control>
    implements IMpcSummaryPane<T> {

  /**
   * Summary pane message provider.
   *
   * @author hazard157
   * @param <T> - displayed M5-modelled entity type
   */
  public interface IMessageProvider<T> {

    /**
     * Implementation must create single-line message text.
     *
     * @param aOwner {@link IMultiPaneComponent} - owner MPC
     * @param aSelectedNode {@link ITsNode} - selected node or <code>null</code>
     * @param aSelEntity &lt;T&gt; - selected entity or <code>null</code>
     * @param aAllItems {@link IList}&lt;T&gt; - all items in tree (include hidden by the filter)
     * @param aFilteredItems {@link IList}&lt;T&gt; - displayed items in tree
     * @return String - single line summary message
     */
    String makeMessage( IMultiPaneComponent<T> aOwner, ITsNode aSelectedNode, T aSelEntity, IList<T> aAllItems,
        IList<T> aFilteredItems );

  }

  /**
   * Default message provider displayes total numer of items and optionally filtered items numner.
   */
  @SuppressWarnings( "rawtypes" )
  public static final IMessageProvider DEFAULT_MESSAGE_RPOVIDER =
      ( aOwner, aSelectedNode, aSelEntity, aAllItems, aFilteredItems ) -> {
        String msg;
        if( aAllItems.size() != aFilteredItems.size() ) {
          msg = String.format( FMT_MSG_ITEMS_FILTERED_COUNT, Integer.valueOf( aAllItems.size() ),
              Integer.valueOf( aFilteredItems.size() ) );
        }
        else {
          msg = String.format( FMT_MSG_ITEMS_COUNT, Integer.valueOf( aAllItems.size() ) );
        }
        return msg;
      };

  private IMessageProvider<T> messageProvider = DEFAULT_MESSAGE_RPOVIDER;

  private Label label = null;

  /**
   * Constructor.
   *
   * @param aOwner {@link MultiPaneComponent} - the owner component
   * @param aMessageProvider {@link IMessageProvider} - summary message provider
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public MpcSummaryPaneMessage( MultiPaneComponent<T> aOwner, IMessageProvider<T> aMessageProvider ) {
    super( aOwner );
    TsNullArgumentRtException.checkNull( aMessageProvider );
    messageProvider = aMessageProvider;
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов базового класса
  //

  @Override
  protected TsComposite doCreateControl( Composite aParent ) {
    TsComposite c = new TsComposite( aParent, SWT.BORDER );
    c.setLayout( new BorderLayout() );
    label = new Label( c, SWT.LEFT );
    label.setLayoutData( BorderLayout.CENTER );
    return c;
  }

  // ------------------------------------------------------------------------------------
  // IMpcSummaryPane
  //

  @Override
  public void updateSummary( ITsNode aSelectedNode, T aSelEntity, IList<T> aAllItems, IList<T> aFilteredItems ) {
    String msg = messageProvider.makeMessage( owner(), aSelectedNode, aSelEntity, aAllItems, aFilteredItems );
    label.setText( msg );
  }

  // ------------------------------------------------------------------------------------
  // Class API
  //

  /**
   * Returns the summary message provider.
   *
   * @return {@link IMessageProvider} - summary message provider never is <code>null</code>
   */
  public IMessageProvider<T> getMessageProvider() {
    return messageProvider;
  }

  /**
   * Sets the summary message provider.
   *
   * @param aMessageProvider {@link IMessageProvider} - summary message provider
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void setMessageProvider( IMessageProvider<T> aMessageProvider ) {
    TsNullArgumentRtException.checkNull( aMessageProvider );
    messageProvider = aMessageProvider;
  }

}

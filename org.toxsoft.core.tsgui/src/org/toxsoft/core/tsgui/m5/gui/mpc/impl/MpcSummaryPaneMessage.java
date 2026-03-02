package org.toxsoft.core.tsgui.m5.gui.mpc.impl;

import static org.toxsoft.core.tsgui.m5.gui.mpc.impl.ITsResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.*;
import org.toxsoft.core.tsgui.utils.checkcoll.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IMpcSummaryPane} implementation displaying string provided by the specified {@link IMessageProvider}.
 *
 * @author hazard157
 * @param <T> - displayed M5-modeled entity type
 */
public class MpcSummaryPaneMessage<T>
    extends MpcAbstractPane<T, Control>
    implements IMpcSummaryPane<T> {

  /**
   * Summary pane message provider.
   *
   * @author hazard157
   * @param <T> - displayed M5-modeled entity type
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

  private IMessageProvider<T> messageProvider = this::makeMessage;

  private Label label = null;

  /**
   * Constructor.
   *
   * @param aOwner {@link MultiPaneComponent} - the owner component
   * @param aMessageProvider {@link IMessageProvider} - summary message provider or <code>null</code> for default
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public MpcSummaryPaneMessage( MultiPaneComponent<T> aOwner, IMessageProvider<T> aMessageProvider ) {
    super( aOwner );
    messageProvider = aMessageProvider != null ? aMessageProvider : this::makeMessage;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private String makeMessage( IMultiPaneComponent<T> aOwner, @SuppressWarnings( "unused" ) ITsNode aSelectedNode,
      @SuppressWarnings( "unused" ) T aSelEntity, IList<T> aAllItems, IList<T> aFilteredItems ) {
    String msg;
    if( aAllItems.size() != aFilteredItems.size() ) {
      msg = String.format( FMT_MSG_ITEMS_FILTERED_COUNT, Integer.valueOf( aAllItems.size() ),
          Integer.valueOf( aFilteredItems.size() ) );
    }
    else {
      msg = String.format( FMT_MSG_ITEMS_COUNT, Integer.valueOf( aAllItems.size() ) );
    }

    // TODO ---
    ITsCheckSupport<?> checkSupport = aOwner.tree().checks();
    if( checkSupport.isChecksSupported() ) {
      int checkedNumber = aOwner.tree().checks().listCheckedItems( true ).size();
      msg = String.format( FMT_MSG_ITEMS_CHECKED_COUNT, msg, Integer.valueOf( checkedNumber ) );
    }
    // ---

    return msg;
  }

  // ------------------------------------------------------------------------------------
  // MpcAbstractPane
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

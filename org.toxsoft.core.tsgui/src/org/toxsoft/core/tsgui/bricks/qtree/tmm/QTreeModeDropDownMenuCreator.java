package org.toxsoft.core.tsgui.bricks.qtree.tmm;

import java.util.Objects;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.toxsoft.core.singlesrc.rcp.TsSinglesourcingUtils;
import org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContextable;
import org.toxsoft.core.tsgui.graphics.icons.EIconSize;
import org.toxsoft.core.tsgui.utils.swt.AbstractMenuCreator;
import org.toxsoft.core.tsgui.utils.swt.SelectionListenerAdapter;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Drop down menu creator for menu button of action {@link ITsStdActionDefs#ACDEF_VIEW_AS_TREE_MENU}.
 *
 * @author hazard157
 * @param <T> - displayed M5-modelled entity type
 */
public class QTreeModeDropDownMenuCreator<T>
    extends AbstractMenuCreator
    implements ITsGuiContextable {

  private final ITsGuiContext       tsContext;
  private final IQTreeModeManager<T> qTreeModeManager;

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aTreeModeManager {@link IQTreeModeManager} - the tree mode manager
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public QTreeModeDropDownMenuCreator( ITsGuiContext aContext, IQTreeModeManager<T> aTreeModeManager ) {
    TsNullArgumentRtException.checkNulls( aContext, aTreeModeManager );
    tsContext = aContext;
    qTreeModeManager = aTreeModeManager;
  }

  // ------------------------------------------------------------------------------------
  // AbstractMenuCreator
  //

  @Override
  protected boolean fillMenu( Menu aMenu ) {
    for( QTreeModeInfo<T> modeInfo : qTreeModeManager().treeModeInfoes() ) {
      MenuItem mItem = new MenuItem( aMenu, SWT.RADIO );
      mItem.setText( modeInfo.nmName() );
      TsSinglesourcingUtils.MenuItem_setToolTipText( mItem, modeInfo.description() );
      if( modeInfo.iconId() != null ) {
        mItem.setImage( iconManager().loadStdIcon( modeInfo.iconId(), EIconSize.IS_16X16 ) );
      }
      mItem.addSelectionListener( new SelectionListenerAdapter() {

        @Override
        public void widgetSelected( SelectionEvent aEvent ) {
          qTreeModeManager().setCurrentMode( modeInfo.id() );
        }
      } );
      boolean isThisMode = Objects.equals( qTreeModeManager().currModeId(), modeInfo.id() );
      mItem.setSelection( isThisMode );
    }
    return true;
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // Class API
  //

  /**
   * Returns the tree mode manager that was specified in constructor.
   *
   * @return {@link IQTreeModeManager} - the tree mode manager
   */
  public IQTreeModeManager<T> qTreeModeManager() {
    return qTreeModeManager;
  }

}
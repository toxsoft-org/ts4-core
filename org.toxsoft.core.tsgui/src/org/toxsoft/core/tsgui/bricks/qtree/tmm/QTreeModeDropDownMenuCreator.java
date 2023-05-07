package org.toxsoft.core.tsgui.bricks.qtree.tmm;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.singlesrc.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Drop down menu creator for menu button of action {@link ITsStdActionDefs#ACDEF_VIEW_AS_TREE_MENU}.
 *
 * @author hazard157
 * @param <T> - displayed M5-modelled entity type
 */
public class QTreeModeDropDownMenuCreator<T>
    extends AbstractMenuCreator
    implements ITsGuiContextable {

  private final ITsGuiContext        tsContext;
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

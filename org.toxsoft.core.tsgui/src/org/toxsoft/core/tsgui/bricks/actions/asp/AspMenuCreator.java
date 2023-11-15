package org.toxsoft.core.tsgui.bricks.actions.asp;

import org.eclipse.jface.action.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.actions.asp.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;

/**
 * Создатель меню из поставщика "действий" {@link ITsActionSetProvider}.
 * <p>
 *
 * @author vs
 */
public class AspMenuCreator
    extends AbstractMenuCreator
    implements ITsGuiContextable {

  class AspListener
      implements IGenericChangeListener {

    @Override
    public void onGenericChangeEvent( Object aSource ) {
      for( String id : menuItems.keys() ) {
        MenuItem mi = menuItems.getByKey( id );
        mi.setEnabled( actionsProvider.isActionEnabled( id ) );
        mi.setSelection( actionsProvider.isActionChecked( id ) );
      }
    }

  }

  class MiSelectionListener
      extends SelectionAdapter {

    @Override
    public void widgetSelected( SelectionEvent aEvent ) {
      MenuItem mi = (MenuItem)aEvent.getSource();
      ITsActionDef aDef = (ITsActionDef)mi.getData();
      actionsProvider.handleAction( aDef.id() );
    }
  }

  private final ITsActionSetProvider actionsProvider;

  ITsGuiContext tsContext;

  private final MiSelectionListener selectionListener = new MiSelectionListener();

  private final IStringMapEdit<MenuItem> menuItems = new StringMap<>();

  private final AspListener aspLitener = new AspListener();

  /**
   * Конструктор.
   *
   * @param aActionsProvider {@link ITsActionSetProvider} - поставщик "действий"
   * @param aTsContext {@link ITsGuiContext} - соответствующий контекст
   */
  public AspMenuCreator( ITsActionSetProvider aActionsProvider, ITsGuiContext aTsContext ) {
    actionsProvider = aActionsProvider;
    actionsProvider.actionsStateEventer().addListener( aspLitener );
    tsContext = aTsContext;
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // IMenuCreator
  //

  @Override
  public void dispose() {
    // TODO ??? what to do ???
  }

  @Override
  protected boolean fillMenu( Menu aMenu ) {
    boolean result = false;
    menuItems.clear();
    for( ITsActionDef actionDef : actionsProvider.listAllActionDefs() ) {
      MenuItem mi = createMenuItem( aMenu, actionDef );
      mi.addSelectionListener( selectionListener );
      menuItems.put( actionDef.id(), mi );
      result = true;
    }
    return result;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  MenuItem createMenuItem( Menu aMenu, ITsActionDef aActionDef ) {
    if( aActionDef.isSeparator() ) {
      return new MenuItem( aMenu, SWT.SEPARATOR );
    }
    int actionFlag = SWT.PUSH;
    int actionStyle = aActionDef.actionStyle();
    if( actionStyle == IAction.AS_UNSPECIFIED ) {
      actionFlag = 0;
    }
    if( actionStyle == IAction.AS_CHECK_BOX ) {
      actionFlag = SWT.CHECK;
    }
    if( actionStyle == IAction.AS_RADIO_BUTTON ) {
      actionFlag = SWT.RADIO;
    }
    if( actionStyle == IAction.AS_DROP_DOWN_MENU ) {
      actionFlag = SWT.DROP_DOWN;
    }
    MenuItem mi = new MenuItem( aMenu, actionFlag );
    mi.setData( aActionDef );
    mi.setText( aActionDef.nmName() );
    mi.setToolTipText( aActionDef.description() );
    String iconId = aActionDef.iconId();
    if( iconId != null ) {
      mi.setImage( iconManager().loadStdIcon( iconId, EIconSize.IS_16X16 ) );
    }
    mi.setSelection( actionsProvider.isActionChecked( aActionDef.id() ) );
    mi.setEnabled( actionsProvider.isActionEnabled( aActionDef.id() ) );
    return mi;
  }

}

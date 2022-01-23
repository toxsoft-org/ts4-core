package org.toxsoft.tsgui.m5_3.gui.mpc.impl;

import java.util.Objects;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.toxsoft.singlesrc.rcp.TsSinglesourcingUtils;
import org.toxsoft.tsgui.bricks.actions.ITsStdActionDefs;
import org.toxsoft.tsgui.graphics.icons.EIconSize;
import org.toxsoft.tsgui.graphics.icons.ITsIconManager;
import org.toxsoft.tsgui.m5_3.gui.mpc.ITreeModeManager;
import org.toxsoft.tsgui.utils.swt.AbstractMenuCreator;
import org.toxsoft.tsgui.utils.swt.SelectionListenerAdapter;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

// TODO TRANSLATE

/**
 * Создатель выпадающего меню выбора режима дерева к действию {@link ITsStdActionDefs#ACDEF_VIEW_AS_TREE_MENU}.
 *
 * @author hazard157
 * @param <T> - displayed M5-modelled entity type
 */
public class TreeModeDropDownMenuCreator<T>
    extends AbstractMenuCreator {

  private final IEclipseContext     winContext;
  private final ITreeModeManager<T> treeModeManager;

  /**
   * Конструктор.
   *
   * @param aWinContext {@link IEclipseContext} - контекст приложения уровня окна
   * @param aTreeModeManager {@link ITreeModeManager} - менеджер режимов дерева от соответствующего просмотрщика
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   */
  public TreeModeDropDownMenuCreator( IEclipseContext aWinContext, ITreeModeManager<T> aTreeModeManager ) {
    TsNullArgumentRtException.checkNulls( aWinContext, aTreeModeManager );
    winContext = aWinContext;
    treeModeManager = aTreeModeManager;
  }

  @Override
  protected boolean fillMenu( Menu aMenu ) {
    ITsIconManager iconManager = appContext().get( ITsIconManager.class );
    for( TreeModeInfo<T> modeInfo : treeModeManager().treeModeInfoes() ) {
      MenuItem mItem = new MenuItem( aMenu, SWT.RADIO );
      mItem.setText( modeInfo.nmName() );
      TsSinglesourcingUtils.MenuItem_setToolTipText( mItem, modeInfo.description() );
      if( modeInfo.iconId() != null ) {
        mItem.setImage( iconManager.loadStdIcon( modeInfo.iconId(), EIconSize.IS_16X16 ) );
      }
      mItem.addSelectionListener( new SelectionListenerAdapter() {

        @Override
        public void widgetSelected( SelectionEvent aEvent ) {
          treeModeManager().setCurrentMode( modeInfo.id() );
        }
      } );
      boolean isThisMode = Objects.equals( treeModeManager().getCurrentModeId(), modeInfo.id() );
      mItem.setSelection( isThisMode );
    }
    return true;
  }

  /**
   * Возвращает менеджер режимов дерева.
   *
   * @return {@link ITreeModeManager} - менеджер режимов дерева от соответствующего просмотрщика
   */
  public ITreeModeManager<T> treeModeManager() {
    return treeModeManager;
  }

  /**
   * Возвращает контекст приложения.
   *
   * @return {@link IEclipseContext} - контекст приложения
   */
  public IEclipseContext appContext() {
    return winContext;
  }

}

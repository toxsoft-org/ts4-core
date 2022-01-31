package org.toxsoft.core.tsgui.bricks.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.graphics.icons.EIconSize;
import org.toxsoft.core.tsgui.graphics.icons.ITsIconManager;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link ITsActionDef} base action.
 *
 * @author hazard157
 */
public class TsAction
    extends Action {

  private final ITsActionDef  def;
  private final ITsGuiContext ctx;

  /**
   * Создает действие с указанием размера значка.
   * <p>
   * Если размер значка указан null, для загрузки значка используется {@link ITsIconManager#loadFreeIcon(String)}.
   * <p>
   * Долен вызываться только из основного GUI-потока выполнения.
   *
   * @param aDef {@link ITsActionDef} - описание действия
   * @param aIconSize {@link EIconSize} - размер значка или null для нестандартных значков
   * @param aContext {@link ITsGuiContext} - контекст
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public TsAction( ITsActionDef aDef, EIconSize aIconSize, ITsGuiContext aContext ) {
    super( TsNullArgumentRtException.checkNull( aDef ).nmName(), aDef.actionStyle() );
    TsNullArgumentRtException.checkNulls( aIconSize, aContext );
    def = aDef;
    ctx = aContext;
    setToolTipText( def.description() );
    ITsIconManager iconManager = ctx.get( ITsIconManager.class );
    if( def.iconId() != null ) {
      ImageDescriptor iconDescr = iconManager.loadStdDescriptor( def.iconId(), aIconSize );
      setImageDescriptor( iconDescr );
    }
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the action definition.
   *
   * @return {@link ITsActionDef} - the action definition
   */
  public ITsActionDef def() {
    return def;
  }

}

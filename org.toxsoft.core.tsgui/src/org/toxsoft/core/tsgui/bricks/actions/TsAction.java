package org.toxsoft.core.tsgui.bricks.actions;

import org.eclipse.jface.action.*;
import org.eclipse.jface.resource.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tslib.utils.errors.*;

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
   * Constructor.
   * <p>
   * If icon size is <code>null</code>, then {@link ITsIconManager#loadFreeIcon(String)} will be used to load an icon,
   * so {@link ITsActionDef#iconId()} will be considered as symbolic name of an icon.
   *
   * @param aDef {@link ITsActionDef} - action definition
   * @param aIconSize {@link EIconSize} - the icon size or <code>null</code> for icon symbolic name
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
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

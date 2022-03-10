package org.toxsoft.core.tsgui.bricks.actions;

import static org.toxsoft.core.tsgui.bricks.actions.ITsActionConstants.*;

import org.eclipse.jface.action.*;
import org.eclipse.jface.resource.*;
import org.eclipse.jface.util.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITsActionDef} base action.
 *
 * @author hazard157
 */
public class TsAction
    extends Action {

  class PropertyChangeListener
      implements IPropertyChangeListener {

    @Override
    public void propertyChange( PropertyChangeEvent aEvent ) {
      if( aEvent.getProperty().equals( CHECKED ) ) {
        if( isChecked() ) {
          if( checkedText != null ) {
            setText( checkedText );
          }
          if( checkedTooltip != null ) {
            setToolTipText( checkedTooltip );
          }
          if( checkedIconDescr != null ) {
            setImageDescriptor( checkedIconDescr );
          }
        }
        else {
          setText( def.nmName() );
          setToolTipText( def.description() );
          setImageDescriptor( iconDescr );
        }
      }
    }

  }

  private final ITsActionDef  def;
  private final ITsGuiContext ctx;
  final ImageDescriptor       iconDescr;
  final ImageDescriptor       checkedIconDescr;
  final String                checkedText;
  final String                checkedTooltip;

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
    // icon
    if( def.iconId() != null ) {
      iconDescr = iconManager.loadStdDescriptor( def.iconId(), aIconSize );
      setImageDescriptor( iconDescr );
    }
    else {
      iconDescr = null;
    }
    // cheched icon
    IAtomicValue av = def.params().getValue( OPID_CHECKED_ICON_ID, IAtomicValue.NULL );
    if( av.isAssigned() ) {
      checkedIconDescr = iconManager.loadStdDescriptor( av.asString(), aIconSize );
    }
    else {
      checkedIconDescr = null;
    }
    // cheched text
    av = def.params().getValue( OPID_CHECKED_TEXT, IAtomicValue.NULL );
    if( av.isAssigned() ) {
      checkedText = av.asString();
    }
    else {
      checkedText = null;
    }
    // cheched tooltip
    av = def.params().getValue( OPID_CHECKED_TOOLTIP, IAtomicValue.NULL );
    if( av.isAssigned() ) {
      checkedTooltip = av.asString();
    }
    else {
      checkedTooltip = null;
    }
    if( def.actionStyle() == IAction.AS_CHECK_BOX ) {
      addPropertyChangeListener( new PropertyChangeListener() );
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

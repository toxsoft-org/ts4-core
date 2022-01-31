package org.toxsoft.core.tsgui.bricks.actions;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.jface.action.IAction;
import org.toxsoft.core.tslib.av.opset.IOptionSet;
import org.toxsoft.core.tslib.av.opset.impl.OptionSetUtils;
import org.toxsoft.core.tslib.bricks.strid.impl.StridableParameterized;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Parttly editable implementation of {@link ITsActionDef}.
 *
 * @author hazard157
 */
public class TsActionDef
    extends StridableParameterized
    implements ITsActionDef {

  private final int actionStyle;

  /**
   * Constructor.
   *
   * @param aId String - action ID (Idpath)
   * @param aActionStyle int - action stle, only one of the {@link IAction}<b>.AS_XXX</b> constant
   * @param aParams {@link IOptionSet} - {@link #params()} initial values
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   * @throws TsIllegalArgumentRtException aActionStyle has invalid value
   */
  public TsActionDef( String aId, int aActionStyle, IOptionSet aParams ) {
    super( aId, aParams );
    actionStyle = switch( aActionStyle ) {
      case IAction.AS_CHECK_BOX, IAction.AS_DROP_DOWN_MENU, IAction.AS_PUSH_BUTTON, IAction.AS_RADIO_BUTTON, IAction.AS_UNSPECIFIED -> aActionStyle;
      default -> throw new TsIllegalArgumentRtException();
    };
  }

  /**
   * Конструктор.
   *
   * @param aId String - идентификатор (ИД-путь) типа
   * @param aActionStyle int - стиль действия
   * @param aIdsAndValues Object[] - id / value pairs as for {@link OptionSetUtils#createOpSet(Object...)}
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   * @throws TsIllegalArgumentRtException идентификатор не ИД-путь
   * @throws TsIllegalArgumentRtException aActionStyle имеет недопустимое значение
   */
  public TsActionDef( String aId, int aActionStyle, Object... aIdsAndValues ) {
    this( aId, aActionStyle, OptionSetUtils.createOpSet( aIdsAndValues ) );
  }

  @SuppressWarnings( "javadoc" )
  public static TsActionDef ofPush1( String aId, Object... aIdsAndValues ) {
    return new TsActionDef( aId, IAction.AS_PUSH_BUTTON, aIdsAndValues );
  }

  @SuppressWarnings( "javadoc" )
  public static TsActionDef ofPush2( String aId, String aName, String aDescription, String aIconId,
      Object... aIdsAndValues ) {
    TsNullArgumentRtException.checkNulls( aName, aDescription );
    TsActionDef adef = ofPush1( aId, aIdsAndValues );
    adef.setNameAndDescription( aName, aDescription );
    if( aIconId != null && !aIconId.isBlank() ) {
      adef.params().setStr( TSID_ICON_ID, aIconId );
    }
    return adef;
  }

  @SuppressWarnings( "javadoc" )
  public static TsActionDef ofCheck1( String aId, Object... aIdsAndValues ) {
    return new TsActionDef( aId, IAction.AS_CHECK_BOX, aIdsAndValues );
  }

  @SuppressWarnings( "javadoc" )
  public static TsActionDef ofCheck2( String aId, String aName, String aDescription, String aIconId,
      Object... aIdsAndValues ) {
    TsNullArgumentRtException.checkNulls( aName, aDescription );
    TsActionDef adef = ofCheck1( aId, aIdsAndValues );
    adef.setNameAndDescription( aName, aDescription );
    if( aIconId != null && !aIconId.isBlank() ) {
      adef.params().setStr( TSID_ICON_ID, aIconId );
    }
    return adef;
  }

  @SuppressWarnings( "javadoc" )
  public static TsActionDef ofMenu1( String aId, Object... aIdsAndValues ) {
    return new TsActionDef( aId, IAction.AS_DROP_DOWN_MENU, aIdsAndValues );
  }

  @SuppressWarnings( "javadoc" )
  public static TsActionDef ofMenu2( String aId, String aName, String aDescription, String aIconId,
      Object... aIdsAndValues ) {
    TsNullArgumentRtException.checkNulls( aName, aDescription );
    TsActionDef adef = ofMenu1( aId, aIdsAndValues );
    adef.setNameAndDescription( aName, aDescription );
    if( aIconId != null && !aIconId.isBlank() ) {
      adef.params().setStr( TSID_ICON_ID, aIconId );
    }
    return adef;
  }

  @SuppressWarnings( "javadoc" )
  public static TsActionDef ofRadio1( String aId, Object... aIdsAndValues ) {
    return new TsActionDef( aId, IAction.AS_RADIO_BUTTON, aIdsAndValues );
  }

  @SuppressWarnings( "javadoc" )
  public static TsActionDef ofUnspec1( String aId, Object... aIdsAndValues ) {
    return new TsActionDef( aId, IAction.AS_UNSPECIFIED, aIdsAndValues );
  }

  // ------------------------------------------------------------------------------------
  // ITsActionDef
  //

  @Override
  public int actionStyle() {
    return actionStyle;
  }

  @Override
  public String iconId() {
    return params().getStr( TSID_ICON_ID, null );
  }

}

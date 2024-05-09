package org.toxsoft.core.tsgui.bricks.actions;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.jface.action.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Partly editable implementation of {@link ITsActionDef}.
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
   * @param aId String - action ID (an IDpath)
   * @param aActionStyle int - action style, only one of the {@link IAction}<b>.AS_XXX</b> constant
   * @param aParams {@link IOptionSet} - {@link #params()} values
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   * @throws TsIllegalArgumentRtException aActionStyle has invalid value
   */
  public TsActionDef( String aId, int aActionStyle, IOptionSet aParams ) {
    super( aId, aParams );
    actionStyle = switch( aActionStyle ) {
      case //
          IAction.AS_CHECK_BOX, //
          IAction.AS_DROP_DOWN_MENU, //
          IAction.AS_PUSH_BUTTON, //
          IAction.AS_RADIO_BUTTON, //
          IAction.AS_UNSPECIFIED -> aActionStyle;
      default -> throw new TsIllegalArgumentRtException();
    };
  }

  /**
   * Constructor.
   *
   * @param aId String - action ID (an IDpath)
   * @param aActionStyle int - action style, only one of the {@link IAction}<b>.AS_XXX</b> constant
   * @param aIdsAndValues Object[] - {@link #params()} values as for {@link OptionSetUtils#createOpSet(Object...)}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   * @throws TsIllegalArgumentRtException aActionStyle has invalid value
   */
  public TsActionDef( String aId, int aActionStyle, Object... aIdsAndValues ) {
    this( aId, aActionStyle, OptionSetUtils.createOpSet( aIdsAndValues ) );
  }

  /**
   * Copy constructor.
   *
   * @param aSource {@link ITsActionDef} - the source
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TsActionDef( ITsActionDef aSource ) {
    super( TsNullArgumentRtException.checkNull( aSource ).id(), aSource.params() );
    actionStyle = aSource.actionStyle();
  }

  /**
   * Create an action definition of style {@link IAction#AS_PUSH_BUTTON}.
   *
   * @param aId String - action ID (an IDpath)
   * @param aIdsAndValues Object[] - {@link #params()} values as for {@link OptionSetUtils#createOpSet(Object...)}
   * @return {@link TsActionDef} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public static TsActionDef ofPush1( String aId, Object... aIdsAndValues ) {
    return new TsActionDef( aId, IAction.AS_PUSH_BUTTON, aIdsAndValues );
  }

  /**
   * Create an action definition of style {@link IAction#AS_PUSH_BUTTON}.
   *
   * @param aId String - action ID (an IDpath)
   * @param aName String - action name {@link ITsActionDef#nmName()}
   * @param aDescription String - action description {@link ITsActionDef#description()}
   * @param aIconId String - icon ID {@link ITsActionDef#iconId()}, may be <code>null</code>
   * @param aIdsAndValues Object[] - {@link #params()} values as for {@link OptionSetUtils#createOpSet(Object...)}
   * @return {@link TsActionDef} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
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

  /**
   * Create an action definition of style {@link IAction#AS_CHECK_BOX}.
   *
   * @param aId String - action ID (an IDpath)
   * @param aIdsAndValues Object[] - {@link #params()} values as for {@link OptionSetUtils#createOpSet(Object...)}
   * @return {@link TsActionDef} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public static TsActionDef ofCheck1( String aId, Object... aIdsAndValues ) {
    return new TsActionDef( aId, IAction.AS_CHECK_BOX, aIdsAndValues );
  }

  /**
   * Create an action definition of style {@link IAction#AS_CHECK_BOX}.
   *
   * @param aId String - action ID (an IDpath)
   * @param aName String - action name {@link ITsActionDef#nmName()}
   * @param aDescription String - action description {@link ITsActionDef#description()}
   * @param aIconId String - icon ID {@link ITsActionDef#iconId()}, may be <code>null</code>
   * @param aIdsAndValues Object[] - {@link #params()} values as for {@link OptionSetUtils#createOpSet(Object...)}
   * @return {@link TsActionDef} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
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

  /**
   * Create an action definition of style {@link IAction#AS_DROP_DOWN_MENU}.
   *
   * @param aId String - action ID (an IDpath)
   * @param aIdsAndValues Object[] - {@link #params()} values as for {@link OptionSetUtils#createOpSet(Object...)}
   * @return {@link TsActionDef} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public static TsActionDef ofMenu1( String aId, Object... aIdsAndValues ) {
    return new TsActionDef( aId, IAction.AS_DROP_DOWN_MENU, aIdsAndValues );
  }

  /**
   * Create an action definition of style {@link IAction#AS_DROP_DOWN_MENU}.
   *
   * @param aId String - action ID (an IDpath)
   * @param aName String - action name {@link ITsActionDef#nmName()}
   * @param aDescription String - action description {@link ITsActionDef#description()}
   * @param aIconId String - icon ID {@link ITsActionDef#iconId()}, may be <code>null</code>
   * @param aIdsAndValues Object[] - {@link #params()} values as for {@link OptionSetUtils#createOpSet(Object...)}
   * @return {@link TsActionDef} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
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

  /**
   * Create an action definition of style {@link IAction#AS_RADIO_BUTTON}.
   *
   * @param aId String - action ID (an IDpath)
   * @param aIdsAndValues Object[] - {@link #params()} values as for {@link OptionSetUtils#createOpSet(Object...)}
   * @return {@link TsActionDef} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public static TsActionDef ofRadio1( String aId, Object... aIdsAndValues ) {
    return new TsActionDef( aId, IAction.AS_RADIO_BUTTON, aIdsAndValues );
  }

  /**
   * Create an action definition of style {@link IAction#AS_RADIO_BUTTON}.
   *
   * @param aId String - action ID (an IDpath)
   * @param aName String - action name {@link ITsActionDef#nmName()}
   * @param aDescription String - action description {@link ITsActionDef#description()}
   * @param aIconId String - icon ID {@link ITsActionDef#iconId()}, may be <code>null</code>
   * @param aIdsAndValues Object[] - {@link #params()} values as for {@link OptionSetUtils#createOpSet(Object...)}
   * @return {@link TsActionDef} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public static TsActionDef ofRadio2( String aId, String aName, String aDescription, String aIconId,
      Object... aIdsAndValues ) {
    TsNullArgumentRtException.checkNulls( aName, aDescription );
    TsActionDef adef = ofRadio1( aId, aIdsAndValues );
    adef.setNameAndDescription( aName, aDescription );
    if( aIconId != null && !aIconId.isBlank() ) {
      adef.params().setStr( TSID_ICON_ID, aIconId );
    }
    return adef;
  }

  /**
   * Create an action definition of style {@link IAction#AS_UNSPECIFIED}.
   *
   * @param aId String - action ID (an IDpath)
   * @param aIdsAndValues Object[] - {@link #params()} values as for {@link OptionSetUtils#createOpSet(Object...)}
   * @return {@link TsActionDef} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public static TsActionDef ofUnspec1( String aId, Object... aIdsAndValues ) {
    return new TsActionDef( aId, IAction.AS_UNSPECIFIED, aIdsAndValues );
  }

  /**
   * Creates action definition based on template.
   * <p>
   * Created action definition will have same style as the given template. Option values given in
   * <code>aIdsAndValues</code> will override {@link #params()} option taken from the template.
   *
   * @param aTemplate {@link ITsActionDef} - the template action definition
   * @param aIdsAndValues String[] - {@link #params()} values to override
   * @return {@link TsActionDef} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public static TsActionDef ofTemplate( ITsActionDef aTemplate, Object... aIdsAndValues ) {
    TsActionDef acDef = new TsActionDef( aTemplate.id(), aTemplate.actionStyle(), aTemplate.params() );
    acDef.params().addAll( OptionSetUtils.createOpSet( aIdsAndValues ) );
    return acDef;
  }

  /**
   * Creates action definition with new ID based on template.
   * <p>
   * Created action definition will have same style as the given template. Option values given in
   * <code>aIdsAndValues</code> will override {@link #params()} option taken from the template.
   *
   * @param aId String - action ID (an IDpath)
   * @param aTemplate {@link ITsActionDef} - the template action definition
   * @param aIdsAndValues String[] - {@link #params()} values to override
   * @return {@link TsActionDef} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public static TsActionDef ofTemplate( String aId, ITsActionDef aTemplate, Object... aIdsAndValues ) {
    TsActionDef acDef = new TsActionDef( aId, aTemplate.actionStyle(), aIdsAndValues );
    acDef.params().addAll( OptionSetUtils.createOpSet( aIdsAndValues ) );
    return acDef;
  }

  // ------------------------------------------------------------------------------------
  // ITsActionDef
  //

  @Override
  public int actionStyle() {
    return actionStyle;
  }

  @Override
  public boolean isSeparator() {
    return id().equals( ACTID_SEPARATOR );
  }

  // ------------------------------------------------------------------------------------
  // IIconIdable
  //

  @Override
  public String iconId() {
    return params().getStr( TSID_ICON_ID, null );
  }

}

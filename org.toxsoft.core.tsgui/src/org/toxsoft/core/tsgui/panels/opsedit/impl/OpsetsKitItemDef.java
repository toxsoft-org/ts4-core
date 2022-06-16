package org.toxsoft.core.tsgui.panels.opsedit.impl;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.panels.opsedit.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IOpsetsKitItemDef} implementation.
 *
 * @author hazard157
 */
public class OpsetsKitItemDef
    implements IOpsetsKitItemDef, IParameterizedEdit {

  private final String                        id;
  private final IOptionSetEdit                params     = new OptionSet();
  private final IStridablesListEdit<IDataDef> optionDefs = new StridablesList<>();

  /**
   * Constructor.
   *
   * @param aId String - kit item ID
   * @param aOpDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - list of option definitions
   * @param aParams {@link IOptionSetEdit} - values of {@link #params}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public OpsetsKitItemDef( String aId, IStridablesList<IDataDef> aOpDefs, IOptionSet aParams ) {
    id = StridUtils.checkValidIdPath( aId );
    optionDefs.addAll( aOpDefs );
    params.addAll( aParams );
  }

  /**
   * Creates an empty definition to be edited later.
   *
   * @param aId String - kit item ID
   * @param aName String - kit item name
   * @param aDescription String - kit item description
   * @param aIconId String - kit item icon ID may be <code>null</code>
   * @return {@link OpsetsKitItemDef} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public static OpsetsKitItemDef create( String aId, String aName, String aDescription, String aIconId ) {
    TsNullArgumentRtException.checkNulls( aId, aName, aDescription );
    OpsetsKitItemDef def = new OpsetsKitItemDef( aId, IStridablesList.EMPTY, IOptionSet.NULL );
    DDEF_NAME.setValue( def.params, avStr( aName ) );
    DDEF_DESCRIPTION.setValue( def.params, avStr( aDescription ) );
    if( aIconId != null && !aIconId.isBlank() ) {
      DDEF_ICON_ID.setValue( def.params, avStr( aIconId ) );
    }
    return def;
  }

  // ------------------------------------------------------------------------------------
  // IStridableParameterized
  //

  @Override
  public String id() {
    return id;
  }

  @Override
  public String nmName() {
    return DDEF_NAME.getValue( params ).asString();
  }

  @Override
  public String description() {
    return DDEF_DESCRIPTION.getValue( params ).asString();
  }

  @Override
  public IOptionSetEdit params() {
    return params;
  }

  // ------------------------------------------------------------------------------------
  // IOpsetsKitItemDef
  //

  @Override
  public String iconId() {
    return params.getStr( TSID_ICON_ID, null );
  }

  @Override
  public IStridablesListEdit<IDataDef> optionDefs() {
    return optionDefs;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return id() + '[' + optionDefs.size() + ']';
  }

}

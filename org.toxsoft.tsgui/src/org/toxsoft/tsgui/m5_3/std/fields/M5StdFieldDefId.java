package org.toxsoft.tsgui.m5_3.std.fields;

import static org.toxsoft.tsgui.m5_3.IM5Constants.*;
import static org.toxsoft.tsgui.m5_3.std.fields.ITsResources.*;
import static org.toxsoft.tslib.av.impl.AvUtils.*;
import static org.toxsoft.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.tsgui.m5_3.model.impl.M5AttributeFieldDef;
import org.toxsoft.tslib.av.IAtomicValue;
import org.toxsoft.tslib.bricks.strid.IStridable;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Attribute {@link IStridable#id()}.
 *
 * @author hazard157
 * @param <T> - modelled {@link IStridable} entity type
 */
public class M5StdFieldDefId<T extends IStridable>
    extends M5AttributeFieldDef<T> {

  /**
   * Constructor.
   */
  public M5StdFieldDefId() {
    this( STR_N_ID, STR_D_ID );
  }

  /**
   * Constructor.
   *
   * @param aName String - field name
   * @param aDescription String - field description
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public M5StdFieldDefId( String aName, String aDescription ) {
    super( FID_ID, DDEF_IDPATH );
    setNameAndDescription( aName, aDescription );
    setFlags( M5FF_COLUMN | M5FF_INVARIANT );
  }

  // ------------------------------------------------------------------------------------
  // M5AttributeFieldDef
  //

  @Override
  protected IAtomicValue doGetFieldValue( T aEntity ) {
    return avStr( aEntity.id() );
  }

}

package org.toxsoft.core.tsgui.m5.std.fields;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.m5.std.fields.ITsResources.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.m5.model.impl.M5AttributeFieldDef;
import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.bricks.strid.IStridable;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Attribute {@link IStridable#id()}.
 *
 * @author hazard157
 * @param <T> - modelled {@link IStridable} entity type
 */
public class M5StdFieldDefName<T extends IStridable>
    extends M5AttributeFieldDef<T> {

  /**
   * Constructor.
   */
  public M5StdFieldDefName() {
    this( STR_N_NAME, STR_D_NAME );
  }

  /**
   * Constructor.
   *
   * @param aName String - field name
   * @param aDescription String - field description
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public M5StdFieldDefName( String aName, String aDescription ) {
    super( FID_NAME, DDEF_NAME );
    setNameAndDescription( aName, aDescription );
    setFlags( M5FF_COLUMN );
  }

  // ------------------------------------------------------------------------------------
  // M5AttributeFieldDef
  //

  @Override
  protected IAtomicValue doGetFieldValue( T aEntity ) {
    return avStr( aEntity.nmName() );
  }

}

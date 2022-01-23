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
public class M5StdFieldDefDescription<T extends IStridable>
    extends M5AttributeFieldDef<T> {

  /**
   * Constructor.
   */
  public M5StdFieldDefDescription() {
    this( STR_N_DESCRIPTION, STR_D_DESCRIPTION );
  }

  /**
   * Constructor.
   *
   * @param aName String - field name
   * @param aDescription String - field description
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public M5StdFieldDefDescription( String aName, String aDescription ) {
    super( FID_DESCRIPTION, DDEF_DESCRIPTION );
    setNameAndDescription( aName, aDescription );
    setFlags( M5FF_DETAIL );
  }

  // ------------------------------------------------------------------------------------
  // M5AttributeFieldDef
  //

  @Override
  protected IAtomicValue doGetFieldValue( T aEntity ) {
    return avStr( aEntity.description() );
  }

}

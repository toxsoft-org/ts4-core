package org.toxsoft.core.tsgui.m5.std.fields;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.m5.std.fields.ITsResources.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Attribute {@link IStridable#id()}.
 *
 * @author hazard157
 * @param <T> - modeled {@link IStridable} entity type
 */
public class M5StdFieldDefId<T extends IStridable>
    extends M5AttributeFieldDef<T> {

  /**
   * Constructor.
   */
  public M5StdFieldDefId() {
    super( FID_ID, DDEF_IDPATH );
    setNameAndDescription( STR_N_ID, STR_D_ID );
    setFlags( M5FF_COLUMN | M5FF_INVARIANT );
  }

  /**
   * Constructor.
   *
   * @param aIdsAndValues Object[] - identifier / value pairs for {@link #params()}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException number of elements in array is uneven
   * @throws ClassCastException argument types convention is violated
   */
  public M5StdFieldDefId( Object... aIdsAndValues ) {
    super( FID_ID, DDEF_IDPATH, aIdsAndValues );
    addFlags( M5FF_INVARIANT );
  }

  // ------------------------------------------------------------------------------------
  // M5AttributeFieldDef
  //

  @Override
  protected IAtomicValue doGetFieldValue( T aEntity ) {
    return avStr( aEntity.id() );
  }

}

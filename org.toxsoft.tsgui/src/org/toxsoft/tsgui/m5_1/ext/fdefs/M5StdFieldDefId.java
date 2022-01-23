package org.toxsoft.tsgui.m5_1.ext.fdefs;

import static org.toxsoft.tsgui.m5_1.IM5Constants.*;
import static org.toxsoft.tsgui.m5_1.ext.fdefs.ITsResources.*;
import static org.toxsoft.tslib.av.impl.AvUtils.*;
import static org.toxsoft.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.tsgui.m5.model.impl.M5AttributeFieldDef;
import org.toxsoft.tslib.av.IAtomicValue;
import org.toxsoft.tslib.bricks.strid.IStridable;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Attribute {@link IStridable#id()}.
 *
 * @author hazard157
 * @param <T> - modelled entity type
 */
public class M5StdFieldDefId<T extends IStridable>
    extends M5AttributeFieldDef<T> {

  /**
   * Конструктор.
   */
  public M5StdFieldDefId() {
    this( STR_N_ID, STR_D_ID );
  }

  /**
   * Конструктор.
   *
   * @param aName String - имя
   * @param aDescription String - описание
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public M5StdFieldDefId( String aName, String aDescription ) {
    super( FID_ID, DDEF_IDPATH );
    setNameAndDescription( aName, aDescription );
    setHints( M5FF_COLUMN | M5FF_INVARIANT );
  }

  @Override
  protected IAtomicValue doGetFieldValue( T aEntity ) {
    return avStr( aEntity.id() );
  }

}

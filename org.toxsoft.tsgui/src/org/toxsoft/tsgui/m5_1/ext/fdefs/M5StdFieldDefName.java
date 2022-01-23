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
 * Стандартное описание атрибута {@link IStridable#nmName()}.
 *
 * @author goga
 * @param <T> - тип моделированного объекта
 */
public class M5StdFieldDefName<T extends IStridable>
    extends M5AttributeFieldDef<T> {

  /**
   * Конструктор.
   */
  public M5StdFieldDefName() {
    this( STR_N_NAME, STR_D_NAME );
  }

  /**
   * Конструктор.
   *
   * @param aName String - имя
   * @param aDescription String - описание
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public M5StdFieldDefName( String aName, String aDescription ) {
    super( FID_NAME, DDEF_NAME );
    setNameAndDescription( aName, aDescription );
    setHints( M5FF_COLUMN );
  }

  @Override
  protected IAtomicValue doGetFieldValue( T aEntity ) {
    return avStr( aEntity.nmName() );
  }

}

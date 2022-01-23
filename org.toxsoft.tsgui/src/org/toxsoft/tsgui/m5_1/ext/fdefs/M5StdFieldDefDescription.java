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
 * Стандартное описание атрибута {@link IStridable#description()}.
 *
 * @author goga
 * @param <T> - тип моделированного объекта
 */
public class M5StdFieldDefDescription<T extends IStridable>
    extends M5AttributeFieldDef<T> {

  /**
   * Конструктор.
   */
  public M5StdFieldDefDescription() {
    this( STR_N_DESCRIPTION, STR_D_DESCRIPTION );
  }

  /**
   * Конструктор.
   *
   * @param aName String - имя
   * @param aDescription String - описание
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public M5StdFieldDefDescription( String aName, String aDescription ) {
    super( FID_DESCRIPTION, DDEF_DESCRIPTION );
    setNameAndDescription( aName, aDescription );
    setHints( M5FF_DETAIL );
  }

  @Override
  protected IAtomicValue doGetFieldValue( T aEntity ) {
    return avStr( aEntity.description() );
  }

}

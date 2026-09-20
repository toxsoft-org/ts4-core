package org.toxsoft.core.tsgui.bricks.tin.tti;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tslib.av.*;

/**
 * {@link ITinTypeInfo} implementation for Java <code>enum</code> packed as {@link EAtomicType#VALOBJ}.
 *
 * @author vs
 */
@SuppressWarnings( "rawtypes" )
public class TtiAvEnum
    extends AbstractTinTypeInfo<Enum> {

  /**
   * The type information singleton.
   */
  public static final TtiAvEnum INSTANCE = new TtiAvEnum();

  private TtiAvEnum() {
    super( ETinTypeKind.ATOMIC, DT_AV_ENUM, Enum.class );
  }

  @Override
  protected ITinValue doGetNullTinValue() {
    return TinValue.ofAtomic( dataType().defaultValue() );
  }

  @Override
  protected ITinValue doGetTinValue( Enum aEntity ) {
    return TinValue.ofAtomic( avValobj( aEntity ) );
  }

  @Override
  public ITinValue defaultValue() {
    return TinValue.ofAtomic( avValobj( dataType().defaultValue() ) );
  }
}

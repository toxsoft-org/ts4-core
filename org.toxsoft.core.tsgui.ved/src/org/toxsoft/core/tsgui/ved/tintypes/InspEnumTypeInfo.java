package org.toxsoft.core.tsgui.ved.tintypes;

import static org.toxsoft.core.tsgui.ved.tintypes.IVieselOptionTypeConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;

/**
 * Информация о поле инспектора для {@link Enum}.
 *
 * @author vs
 */
public class InspEnumTypeInfo
    extends AbstractTinTypeInfo<Enum> {

  /**
   * The type information singleton.
   */
  public static InspEnumTypeInfo INSP_TYPE_INFO = new InspEnumTypeInfo();

  private InspEnumTypeInfo() {
    super( ETinTypeKind.ATOMIC, DT_ENUM, Enum.class );
  }

  @Override
  protected ITinValue doGetNullTinValue() {
    return null;
  }

  @Override
  protected ITinValue doGetTinValue( Enum aEntity ) {
    return TinValue.ofAtomic( avValobj( aEntity ) );
  }

}

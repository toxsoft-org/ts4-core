package org.toxsoft.core.tsgui.ved.tintypes;

import static org.toxsoft.core.tsgui.ved.tintypes.IVieselOptionTypeConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.graphics.image.*;

/**
 * Информация о поле инспектора для {@link TsImageDescriptor}.
 *
 * @author vs
 */
public class InspImageDescriptorTypeInfo
    extends AbstractTinTypeInfo<TsImageDescriptor> {

  /**
   * The type information singleton.
   */
  public static InspImageDescriptorTypeInfo INSTANCE = new InspImageDescriptorTypeInfo();

  InspImageDescriptorTypeInfo() {
    super( ETinTypeKind.ATOMIC, DT_IMAGE_DESCRIPTOR, TsImageDescriptor.class );

  }

  // ------------------------------------------------------------------------------------
  // AbstractTinTypeInfo
  //

  @Override
  protected ITinValue doGetNullTinValue() {
    return doGetTinValue( TsImageDescriptor.NONE );
  }

  @Override
  protected ITinValue doGetTinValue( TsImageDescriptor aEntity ) {
    return TinValue.ofAtomic( avValobj( aEntity ) );
  }

}

package org.toxsoft.core.tsgui.ved.tintypes;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.ved.api.items.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;

/**
 * Группа {@link ITinTypeInfo} для {@link IVedVisel}.
 *
 * @author vs
 */
public class InspViselTypeInfo
    extends AbstractTinTypeInfo<IVedVisel> {

  // ------------------------------------------------------------------------------------
  // AbstractTinTypeInfo
  //

  /**
   * Конструктор.
   *
   * @param aFieldInfoes IStridablesList&lt;ITinFieldInfo> - список полей
   */
  public InspViselTypeInfo( IStridablesList<ITinFieldInfo> aFieldInfoes ) {
    super( ETinTypeKind.GROUP, null, IVedVisel.class );
    for( ITinFieldInfo fieldInfo : aFieldInfoes ) {
      fieldInfos().add( fieldInfo );
    }
  }

  @Override
  protected ITinValue doGetNullTinValue() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected ITinValue doGetTinValue( IVedVisel aEntity ) {
    // TODO Auto-generated method stub
    return null;
  }

}

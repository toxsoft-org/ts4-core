package org.toxsoft.core.pas.tj.impl;

import org.toxsoft.core.pas.tj.ITjObject;
import org.toxsoft.core.pas.tj.ITjValue;
import org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants;
import org.toxsoft.core.tslib.coll.primtypes.IStringMapEdit;
import org.toxsoft.core.tslib.coll.primtypes.impl.StringMap;
import org.toxsoft.core.tslib.utils.TsLibUtils;

/**
 * Реализация {@link ITjObject}.
 *
 * @author hazard157
 */
class TjObject
    implements ITjObject {

  private final IStringMapEdit<ITjValue> fields = new StringMap<>();

  /**
   * Конструктор.
   */
  public TjObject() {
    // nop
  }

  @Override
  public IStringMapEdit<ITjValue> fields() {
    return fields;
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса Object
  //

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append( IStrioHardConstants.CHAR_ARRAY_BEGIN );
    int size = fields.keys().size();
    int index = 0;
    for( String key : fields.keys() ) {
      sb.append( key );
      sb.append( '=' );
      sb.append( fields.getByKey( key ) );
      if( index + 1 < size ) {
        sb.append( ',' );
      }
      index++;
    }
    sb.append( IStrioHardConstants.CHAR_ARRAY_END );
    return sb.toString();
  }

  @Override
  public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    if( aThat instanceof TjObject that ) {
      return fields.equals( that.fields );
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + fields.hashCode();
    return result;
  }

}

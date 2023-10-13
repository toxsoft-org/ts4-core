package org.toxsoft.core.tsgui.bricks.tin.tti;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Default implementation of {@link ITsVisualsProvider} for {@link ITinTypeInfo}.
 * <p>
 * Formats atomic value using {@link AvUtils#printAv(String, IAtomicValue)} where the format string is the constraint
 * {@link IAvMetaConstants#TSID_FORMAT_STRING} found in field or type info. Non-atomic {@link ETinTypeKind#GROUP} kind
 * value is formatted something like pairs "{fieldID1=value,fieldID2=value,...,fieldIdN=value}".
 * <p>
 * Used as default value visualizer both for TIN fields ant TIN types, depending on used constructor.
 *
 * @author hazard157
 */
public class DefaultValueVisualizer
    implements ITsVisualsProvider<ITinValue> {

  private final ITinFieldInfo fieldInfo;
  private final ITinTypeInfo  typeInfo;

  /**
   * Constructor when creating instance for the field.
   *
   * @param aFieldInfo {@link ITinFieldInfo} - the field info
   */
  public DefaultValueVisualizer( ITinFieldInfo aFieldInfo ) {
    TsNullArgumentRtException.checkNull( aFieldInfo );
    fieldInfo = aFieldInfo;
    typeInfo = fieldInfo.typeInfo();
  }

  /**
   * Constructor when creating instance for the root type.
   *
   * @param aTypeInfo {@link ITinTypeInfo} - the type info
   */
  public DefaultValueVisualizer( ITinTypeInfo aTypeInfo ) {
    TsNullArgumentRtException.checkNull( aTypeInfo );
    fieldInfo = null;
    typeInfo = aTypeInfo;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private IAtomicValue getConstraint( String aOpId, IAtomicValue aDefaultValue ) {
    IAtomicValue constraintValue = null;
    if( fieldInfo != null ) {
      constraintValue = fieldInfo.params().findByKey( aOpId );
    }
    if( constraintValue == null && typeInfo.kind().hasAtomic() ) {
      constraintValue = typeInfo.dataType().params().findByKey( aOpId );
    }
    if( constraintValue == null ) {
      constraintValue = aDefaultValue;
    }
    return constraintValue;
  }

  // ------------------------------------------------------------------------------------
  // ITsVisualsProvider
  //

  @Override
  public String getName( ITinValue aItem ) {
    if( aItem == ITinValue.NULL ) {
      return TsLibUtils.EMPTY_STRING;
    }
    IAtomicValue avFmtStr = getConstraint( TSID_FORMAT_STRING, null );
    String fmtStr = avFmtStr != null ? avFmtStr.asString() : null;
    switch( aItem.kind() ) {
      case ATOMIC:
      case FULL: {
        return AvUtils.printAv( fmtStr, aItem.atomicValue() );
      }
      case GROUP: {
        StringBuilder sb = new StringBuilder();
        sb.append( '{' );
        for( String fieldId : aItem.childValues().keys() ) {
          sb.append( fieldId );
          sb.append( '=' );
          sb.append( aItem.childValues().getByKey( fmtStr ).toString() );
        }
        sb.append( '}' );
        return sb.toString();
      }
      default:
        throw new TsNotAllEnumsUsedRtException( aItem.kind().id() );
    }
  }

  @Override
  public String getDescription( ITinValue aItem ) {
    return getConstraint( TSID_DESCRIPTION, AV_STR_EMPTY ).asString();
  }

}

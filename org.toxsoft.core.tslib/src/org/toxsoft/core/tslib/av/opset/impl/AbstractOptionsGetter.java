package org.toxsoft.core.tslib.av.opset.impl;

import static org.toxsoft.core.tslib.av.opset.impl.ITsResources.*;

import org.toxsoft.core.tslib.av.EAtomicType;
import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.av.errors.AvTypeCastRtException;
import org.toxsoft.core.tslib.av.metainfo.IDataDef;
import org.toxsoft.core.tslib.av.opset.IOpsGetter;
import org.toxsoft.core.tslib.bricks.strid.IStridable;
import org.toxsoft.core.tslib.utils.errors.TsItemNotFoundRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Abstract implementation of {@link IOpsGetter}.
 *
 * @author hazard157
 */
public abstract class AbstractOptionsGetter
    implements IOpsGetter {

  /**
   * Constructor.
   */
  public AbstractOptionsGetter() {
    // nop
  }

  /**
   * Implementation must find value by the specified identifier.
   *
   * @param aId String - specified identifier, never is <code>null</code>, may be non-IDpath, even an empty string
   * @return {@link IAtomicValue} - found value or <code>null</code> if none found
   */
  protected abstract IAtomicValue doInternalFind( String aId );

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  final protected IAtomicValue internalFind( String aId ) {
    if( aId == null ) {
      throw new TsNullArgumentRtException();
    }
    return doInternalFind( aId );
  }

  final protected IAtomicValue internalGet( String aId ) {
    IAtomicValue av = internalFind( aId );
    if( av == null ) {
      throw new TsItemNotFoundRtException();
    }
    return av;
  }

  final protected IAtomicValue internalFindAs( String aId, EAtomicType aAtomicType ) {
    if( aAtomicType == null ) {
      throw new TsNullArgumentRtException();
    }
    IAtomicValue av = internalFind( aId );
    if( av != null ) {
      if( av != IAtomicValue.NULL ) {
        AvTypeCastRtException.checkFalse( av.atomicType() == aAtomicType, FMT_ERR_CANT_CAST_OPSET_VALUE, aId,
            av.atomicType().id(), aAtomicType.id() );
      }
    }
    return av;
  }

  final protected IAtomicValue internalGetAs( String aId, EAtomicType aAtomicType ) {
    if( aAtomicType == null ) {
      throw new TsNullArgumentRtException();
    }
    IAtomicValue av = internalGet( aId );
    if( av != IAtomicValue.NULL ) {
      AvTypeCastRtException.checkFalse( av.atomicType() == aAtomicType, FMT_ERR_CANT_CAST_OPSET_VALUE, aId,
          av.atomicType().id(), aAtomicType.id() );
    }
    return av;
  }

  final protected IAtomicValue internalFindAs( IStridable aOpId, EAtomicType aAtomicType ) {
    if( aOpId == null ) {
      throw new TsNullArgumentRtException();
    }
    return internalFindAs( aOpId.id(), aAtomicType );
  }

  final protected IAtomicValue internalGetAs( IStridable aOpId, EAtomicType aAtomicType ) {
    if( aOpId == null ) {
      throw new TsNullArgumentRtException();
    }
    return internalGetAs( aOpId.id(), aAtomicType );
  }

  // ------------------------------------------------------------------------------------
  // IOptionsGetter
  //

  @Override
  public boolean hasValue( String aId ) {
    return internalFind( aId ) != null;
  }

  @Override
  public boolean hasValue( IDataDef aOpId ) {
    return findValue( aOpId ) != null;
  }

  @Override
  public boolean isNull( String aId ) {
    IAtomicValue v = findValue( aId );
    if( v == null || v == IAtomicValue.NULL ) {
      return true;
    }
    return false;
  }

  @Override
  public boolean isNull( IDataDef aOpId ) {
    IAtomicValue v = findValue( aOpId );
    if( v == null || v == IAtomicValue.NULL ) {
      return true;
    }
    return false;
  }

  @Override
  public IAtomicValue findValue( String aId ) {
    return internalFind( aId );
  }

  @Override
  public IAtomicValue findValue( IDataDef aOpId ) {
    TsNullArgumentRtException.checkNull( aOpId );
    return internalFind( aOpId.id() );
  }

  @Override
  public IAtomicValue getValue( String aId ) {
    return internalGet( aId );
  }

  @Override
  public IAtomicValue getValue( String aId, IAtomicValue aDefaultValue ) {
    IAtomicValue value = internalFind( aId );
    if( value == null ) {
      value = aDefaultValue;
    }
    return value;
  }

  @Override
  public IAtomicValue getValue( IDataDef aOpId ) {
    IAtomicValue av = internalFindAs( aOpId, aOpId.atomicType() );
    if( av == null ) {
      throw new TsItemNotFoundRtException();
    }
    return av;
  }

  @Override
  public boolean getBool( String aId ) {
    return internalGetAs( aId, EAtomicType.BOOLEAN ).asBool();
  }

  @Override
  public boolean getBool( String aId, boolean aDefaultValue ) {
    IAtomicValue av = internalFindAs( aId, EAtomicType.BOOLEAN );
    if( av == null ) {
      return aDefaultValue;
    }
    return av.asBool();
  }

  @Override
  public boolean getBool( IDataDef aOpId ) {
    IAtomicValue av = internalFindAs( aOpId, EAtomicType.BOOLEAN );
    if( av == null ) {
      return aOpId.defaultValue().asBool();
    }
    return av.asBool();
  }

  @Override
  public int getInt( String aId ) {
    return internalGetAs( aId, EAtomicType.INTEGER ).asInt();
  }

  @Override
  public int getInt( String aId, int aDefaultValue ) {
    IAtomicValue av = internalFindAs( aId, EAtomicType.INTEGER );
    if( av == null ) {
      return aDefaultValue;
    }
    return av.asInt();
  }

  @Override
  public int getInt( IDataDef aOpId ) {
    IAtomicValue av = internalFindAs( aOpId, EAtomicType.INTEGER );
    if( av == null ) {
      return aOpId.defaultValue().asInt();
    }
    return av.asInt();
  }

  @Override
  public long getLong( String aId ) {
    return internalGetAs( aId, EAtomicType.INTEGER ).asLong();
  }

  @Override
  public long getLong( String aId, long aDefaultValue ) {
    IAtomicValue av = internalFindAs( aId, EAtomicType.INTEGER );
    if( av == null ) {
      return aDefaultValue;
    }
    return av.asLong();
  }

  @Override
  public long getLong( IDataDef aOpId ) {
    IAtomicValue av = internalFindAs( aOpId, EAtomicType.INTEGER );
    if( av == null ) {
      return aOpId.defaultValue().asLong();
    }
    return av.asLong();
  }

  @Override
  public float getFloat( String aId ) {
    return internalGetAs( aId, EAtomicType.FLOATING ).asFloat();
  }

  @Override
  public float getFloat( String aId, float aDefaultValue ) {
    IAtomicValue av = internalFindAs( aId, EAtomicType.FLOATING );
    if( av == null ) {
      return aDefaultValue;
    }
    return av.asFloat();
  }

  @Override
  public float getFloat( IDataDef aOpId ) {
    IAtomicValue av = internalFindAs( aOpId, EAtomicType.INTEGER );
    if( av == null ) {
      return aOpId.defaultValue().asFloat();
    }
    return av.asFloat();
  }

  @Override
  public double getDouble( String aId ) {
    return internalGetAs( aId, EAtomicType.FLOATING ).asDouble();
  }

  @Override
  public double getDouble( String aId, double aDefaultValue ) {
    IAtomicValue av = internalFindAs( aId, EAtomicType.FLOATING );
    if( av == null ) {
      return aDefaultValue;
    }
    return av.asDouble();
  }

  @Override
  public double getDouble( IDataDef aOpId ) {
    IAtomicValue av = internalFindAs( aOpId, EAtomicType.INTEGER );
    if( av == null ) {
      return aOpId.defaultValue().asDouble();
    }
    return av.asDouble();
  }

  @Override
  public long getTime( String aId ) {
    return internalGetAs( aId, EAtomicType.TIMESTAMP ).asLong();
  }

  @Override
  public long getTime( String aId, long aDefaultValue ) {
    IAtomicValue av = internalFindAs( aId, EAtomicType.TIMESTAMP );
    if( av == null ) {
      return aDefaultValue;
    }
    return av.asLong();
  }

  @Override
  public long getTime( IDataDef aOpId ) {
    IAtomicValue av = internalFindAs( aOpId, EAtomicType.INTEGER );
    if( av == null ) {
      return aOpId.defaultValue().asLong();
    }
    return av.asLong();
  }

  @Override
  public String getStr( String aId ) {
    return internalGetAs( aId, EAtomicType.STRING ).asString();
  }

  @Override
  public String getStr( String aId, String aDefaultValue ) {
    IAtomicValue av = internalFindAs( aId, EAtomicType.STRING );
    if( av == null ) {
      return aDefaultValue;
    }
    return av.asString();
  }

  @Override
  public String getStr( IDataDef aOpId ) {
    IAtomicValue av = internalFindAs( aOpId, EAtomicType.STRING );
    if( av == null ) {
      return aOpId.defaultValue().asString();
    }
    return av.asString();
  }

  @Override
  public <T> T getValobj( String aId ) {
    return internalGetAs( aId, EAtomicType.VALOBJ ).asValobj();
  }

  @Override
  public <T> T getValobj( String aId, T aDefaultValue ) {
    IAtomicValue av = internalFindAs( aId, EAtomicType.VALOBJ );
    if( av == null ) {
      return aDefaultValue;
    }
    return av.asValobj();
  }

  @Override
  public <T> T getValobj( IDataDef aOpId ) {
    IAtomicValue av = internalFindAs( aOpId, EAtomicType.INTEGER );
    if( av == null ) {
      return aOpId.defaultValue().asValobj();
    }
    return av.asValobj();
  }

}

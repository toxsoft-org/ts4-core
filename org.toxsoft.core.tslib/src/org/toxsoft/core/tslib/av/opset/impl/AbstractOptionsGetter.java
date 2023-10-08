package org.toxsoft.core.tslib.av.opset.impl;

import static org.toxsoft.core.tslib.av.opset.impl.ITsResources.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.errors.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.utils.errors.*;

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
  // implementation
  //

  /**
   * Wraps over {@link #doInternalFind(String)} just adding <code>null</code> argument check.
   *
   * @param aId String - the option ID
   * @return {@link IAtomicValue} - result of {@link #doInternalFind(String)}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  final protected IAtomicValue internalFind( String aId ) {
    if( aId == null ) {
      throw new TsNullArgumentRtException();
    }
    return doInternalFind( aId );
  }

  /**
   * Wraps over {@link #internalFind(String)} just adding <code>null</code> return value check.
   *
   * @param aId String - the option ID
   * @return {@link IAtomicValue} - result of {@link #doInternalFind(String)}, never is null
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException if {@link #internalFind(String)} returns <code>null</code>
   */
  final protected IAtomicValue internalGet( String aId ) {
    IAtomicValue av = internalFind( aId );
    if( av == null ) {
      throw new TsItemNotFoundRtException();
    }
    return av;
  }

  /**
   * Wraps over {@link #internalFind(String)} adding returned value atomic type check.
   * <p>
   * If returned value is not <code>null</code> and is not {@link IAtomicValue#NULL NULL}, then checks if returned value
   * type is of expected type <code>aAtomicType</code>.
   *
   * @param aId String - the option ID
   * @param aAtomicType {@link EAtomicType} - the expected type
   * @return {@link IAtomicValue} - found value of expected type or <code>null</code> or {@link IAtomicValue#NULL NULL}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws AvTypeCastRtException returned meaningful value is not of expected type
   */
  final protected IAtomicValue internalFindAs( String aId, EAtomicType aAtomicType ) {
    if( aAtomicType == null ) {
      throw new TsNullArgumentRtException();
    }
    IAtomicValue av = internalFind( aId );
    if( av != null && av != IAtomicValue.NULL ) {
      if( av.atomicType() != aAtomicType ) {
        throw new AvTypeCastRtException( FMT_ERR_CANT_CAST_OPSET_VALUE, aId, av.atomicType().id(), aAtomicType.id() );
      }
    }
    return av;
  }

  /**
   * Wraps over {@link #internalGet(String)} adding returned value atomic type check.
   * <p>
   * If returned value is not {@link IAtomicValue#NULL NULL}, then checks if returned value type is of expected type
   * <code>aAtomicType</code>.
   *
   * @param aId String - the option ID
   * @param aAtomicType {@link EAtomicType} - the expected type
   * @return {@link IAtomicValue} - found value of expected type or <code>null</code> or {@link IAtomicValue#NULL NULL}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException if {@link #internalFind(String)} returns <code>null</code>
   * @throws AvTypeCastRtException returned meaningful value is not of expected type
   */
  final protected IAtomicValue internalGetAs( String aId, EAtomicType aAtomicType ) {
    if( aAtomicType == null ) {
      throw new TsNullArgumentRtException();
    }
    IAtomicValue av = internalGet( aId );
    if( av != IAtomicValue.NULL ) {
      if( av.atomicType() != aAtomicType ) {
        throw new AvTypeCastRtException( FMT_ERR_CANT_CAST_OPSET_VALUE, aId, av.atomicType().id(), aAtomicType.id() );
      }
    }
    return av;
  }

  /**
   * Wraps over {@link #internalFindAs(String, EAtomicType)} adding argument check for <code>null</code>.
   * <p>
   * If returned value is not <code>null</code> and is not {@link IAtomicValue#NULL NULL}, then checks if returned value
   * type is of expected type <code>aAtomicType</code>.
   *
   * @param aOpId {@link IStridable} - the option ID
   * @param aAtomicType {@link EAtomicType} - the expected type
   * @return {@link IAtomicValue} - found value of expected type or <code>null</code> or {@link IAtomicValue#NULL NULL}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws AvTypeCastRtException returned meaningful value is not of expected type
   */
  final protected IAtomicValue internalFindAs( IStridable aOpId, EAtomicType aAtomicType ) {
    if( aOpId == null ) {
      throw new TsNullArgumentRtException();
    }
    return internalFindAs( aOpId.id(), aAtomicType );
  }

  /**
   * Wraps over {@link #internalGetAs(IStridable, EAtomicType)} adding argument check for <code>null</code>.
   * <p>
   * If returned value is not {@link IAtomicValue#NULL NULL}, then checks if returned value type is of expected type
   * <code>aAtomicType</code>.
   *
   * @param aOpId {@link IStridable} - the option ID
   * @param aAtomicType {@link EAtomicType} - the expected type
   * @return {@link IAtomicValue} - found value of expected type or {@link IAtomicValue#NULL NULL}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException if {@link #internalFind(String)} returns <code>null</code>
   * @throws AvTypeCastRtException returned meaningful value is not of expected type
   */
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

  // ------------------------------------------------------------------------------------
  // getValue

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
    if( av != null && av != IAtomicValue.NULL ) {
      return av;
    }
    if( av == null && aOpId.isMandatory() ) {
      throw new TsItemNotFoundRtException( FMT_ERR_NO_MANDATORY_OP, aOpId.id(), aOpId.nmName() );
    }
    return aOpId.defaultValue();
  }

  // ------------------------------------------------------------------------------------
  // getBool

  @Override
  public boolean getBool( String aId ) {
    return internalGetAs( aId, EAtomicType.BOOLEAN ).asBool();
  }

  @Override
  public boolean getBool( String aId, boolean aDefaultValue ) {
    IAtomicValue av = internalFindAs( aId, EAtomicType.BOOLEAN );
    // the same check as #isNull(aId)
    if( av == null || av == IAtomicValue.NULL ) {
      return aDefaultValue;
    }
    return av.asBool();
  }

  @Override
  public boolean getBool( IDataDef aOpId ) {
    IAtomicValue av = internalFindAs( aOpId, EAtomicType.BOOLEAN );
    if( av != null && av != IAtomicValue.NULL ) {
      return av.asBool();
    }
    if( av == null && aOpId.isMandatory() ) {
      throw new TsItemNotFoundRtException( FMT_ERR_NO_MANDATORY_OP, aOpId.id(), aOpId.nmName() );
    }
    return aOpId.defaultValue().asBool();
  }

  // ------------------------------------------------------------------------------------
  // getInt

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
    if( av != null && av != IAtomicValue.NULL ) {
      return av.asInt();
    }
    if( av == null && aOpId.isMandatory() ) {
      throw new TsItemNotFoundRtException( FMT_ERR_NO_MANDATORY_OP, aOpId.id(), aOpId.nmName() );
    }
    return aOpId.defaultValue().asInt();
  }

  // ------------------------------------------------------------------------------------
  // getLong

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
    if( av != null && av != IAtomicValue.NULL ) {
      return av.asLong();
    }
    if( av == null && aOpId.isMandatory() ) {
      throw new TsItemNotFoundRtException( FMT_ERR_NO_MANDATORY_OP, aOpId.id(), aOpId.nmName() );
    }
    return aOpId.defaultValue().asLong();
  }

  // ------------------------------------------------------------------------------------
  // getFloat

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
    IAtomicValue av = internalFindAs( aOpId, EAtomicType.FLOATING );
    if( av != null && av != IAtomicValue.NULL ) {
      return av.asFloat();
    }
    if( av == null && aOpId.isMandatory() ) {
      throw new TsItemNotFoundRtException( FMT_ERR_NO_MANDATORY_OP, aOpId.id(), aOpId.nmName() );
    }
    return aOpId.defaultValue().asFloat();
  }

  // ------------------------------------------------------------------------------------
  // getDouble

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
    IAtomicValue av = internalFindAs( aOpId, EAtomicType.FLOATING );
    if( av != null && av != IAtomicValue.NULL ) {
      return av.asDouble();
    }
    if( av == null && aOpId.isMandatory() ) {
      throw new TsItemNotFoundRtException( FMT_ERR_NO_MANDATORY_OP, aOpId.id(), aOpId.nmName() );
    }
    return aOpId.defaultValue().asDouble();
  }

  // ------------------------------------------------------------------------------------
  // getTime

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
    if( av != null && av != IAtomicValue.NULL ) {
      return av.asLong();
    }
    if( av == null && aOpId.isMandatory() ) {
      throw new TsItemNotFoundRtException( FMT_ERR_NO_MANDATORY_OP, aOpId.id(), aOpId.nmName() );
    }
    return aOpId.defaultValue().asLong();
  }

  // ------------------------------------------------------------------------------------
  // getStr

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
    if( av != null && av != IAtomicValue.NULL ) {
      return av.asString();
    }
    if( av == null && aOpId.isMandatory() ) {
      throw new TsItemNotFoundRtException( FMT_ERR_NO_MANDATORY_OP, aOpId.id(), aOpId.nmName() );
    }
    return aOpId.defaultValue().asString();
  }

  // ------------------------------------------------------------------------------------
  // getValobj

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
    IAtomicValue av = internalFindAs( aOpId, EAtomicType.VALOBJ );
    if( av != null && av != IAtomicValue.NULL ) {
      return av.asValobj();
    }
    if( av == null && aOpId.isMandatory() ) {
      throw new TsItemNotFoundRtException( FMT_ERR_NO_MANDATORY_OP, aOpId.id(), aOpId.nmName() );
    }
    return aOpId.defaultValue().asValobj();
  }

}

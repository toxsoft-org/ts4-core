package org.toxsoft.core.tslib.av.opset.impl;

import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Implementation of both {@link IOpsGetter} and {@link IOpsSetter}
 *
 * @author hazard157
 */
public abstract class AbstractOptionsSetter
    extends AbstractOptionsGetter
    implements IOpsSetter {

  /**
   * Constructor.
   */
  public AbstractOptionsSetter() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // IOpsSetter
  //

  @Override
  public void setValue( String aId, IAtomicValue aValue ) {
    internalSet( aId, aValue );
  }

  @Override
  public void setValue( IDataDef aOpId, IAtomicValue aValue ) {
    internalSet( aOpId, aValue );
  }

  @Override
  public void setValueIfNull( String aId, IAtomicValue aValue ) {
    StridUtils.checkValidIdPath( aId );
    TsNullArgumentRtException.checkNull( aValue );
    IAtomicValue oldVal = findValue( aId );
    if( oldVal == null || oldVal == IAtomicValue.NULL ) {
      setValue( aId, aValue );
    }
  }

  // ------------------------------------------------------------------------------------
  // boolean

  @Override
  public void setBool( String aId, boolean aValue ) {
    if( internalFindAs( aId, BOOLEAN ) == null ) {
      StridUtils.checkValidIdPath( aId );
    }
    internalSet( aId, avBool( aValue ) );
  }

  @Override
  public void setBool( IDataDef aOpId, boolean aValue ) {
    internalFindAs( aOpId, BOOLEAN );
    internalSet( aOpId, avBool( aValue ) );
  }

  // ------------------------------------------------------------------------------------
  // int

  @Override
  public void setInt( String aId, int aValue ) {
    if( internalFindAs( aId, INTEGER ) == null ) {
      StridUtils.checkValidIdPath( aId );
    }
    internalSet( aId, avInt( aValue ) );
  }

  @Override
  public void setInt( IDataDef aOpId, int aValue ) {
    internalFindAs( aOpId, INTEGER );
    internalSet( aOpId, avInt( aValue ) );
  }

  // ------------------------------------------------------------------------------------
  // long
  //

  @Override
  public void setLong( String aId, long aValue ) {
    if( internalFindAs( aId, INTEGER ) == null ) {
      StridUtils.checkValidIdPath( aId );
    }
    internalSet( aId, avInt( aValue ) );
  }

  @Override
  public void setLong( IDataDef aOpId, long aValue ) {
    internalFindAs( aOpId, INTEGER );
    internalSet( aOpId, avInt( aValue ) );
  }

  // ------------------------------------------------------------------------------------
  // float

  @Override
  public void setFloat( String aId, float aValue ) {
    if( internalFindAs( aId, FLOATING ) == null ) {
      StridUtils.checkValidIdPath( aId );
    }
    internalSet( aId, avFloat( aValue ) );
  }

  @Override
  public void setFloat( IDataDef aOpId, float aValue ) {
    internalFindAs( aOpId, FLOATING );
    internalSet( aOpId, avFloat( aValue ) );
  }

  // ------------------------------------------------------------------------------------
  // double

  @Override
  public void setDouble( String aId, double aValue ) {
    if( internalFindAs( aId, FLOATING ) == null ) {
      StridUtils.checkValidIdPath( aId );
    }
    internalSet( aId, avFloat( aValue ) );
  }

  @Override
  public void setDouble( IDataDef aOpId, double aValue ) {
    internalFindAs( aOpId, FLOATING );
    internalSet( aOpId, avFloat( aValue ) );
  }

  // ------------------------------------------------------------------------------------
  // time

  @Override
  public void setTime( String aId, long aValue ) {
    if( internalFindAs( aId, TIMESTAMP ) == null ) {
      StridUtils.checkValidIdPath( aId );
    }
    internalSet( aId, avTimestamp( aValue ) );
  }

  @Override
  public void setTime( IDataDef aOpId, long aValue ) {
    internalFindAs( aOpId, TIMESTAMP );
    internalSet( aOpId, avTimestamp( aValue ) );
  }

  // ------------------------------------------------------------------------------------
  // String

  @Override
  public void setStr( String aId, String aValue ) {
    if( internalFindAs( aId, STRING ) == null ) {
      StridUtils.checkValidIdPath( aId );
    }
    internalSet( aId, avStr( aValue ) );
  }

  @Override
  public void setStr( IDataDef aOpId, String aValue ) {
    internalFindAs( aOpId, STRING );
    internalSet( aOpId, avStr( aValue ) );
  }

  // ------------------------------------------------------------------------------------
  // valobj

  @Override
  public void setValobj( String aId, Object aValue ) {
    if( internalFindAs( aId, VALOBJ ) == null ) {
      StridUtils.checkValidIdPath( aId );
    }
    internalSet( aId, avValobj( aValue ) );
  }

  @Override
  public void setValobj( IDataDef aOpId, Object aValue ) {
    internalFindAs( aOpId, VALOBJ );
    internalSet( aOpId, avValobj( aValue ) );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  final protected void internalSet( String aId, IAtomicValue aValue ) {
    StridUtils.checkValidIdPath( aId );
    if( aValue == null ) {
      throw new TsNullArgumentRtException();
    }
    IAtomicValue oldValue = internalFind( aId );
    doBeforeSet( aId, oldValue, aValue );
    doInternalSet( aId, aValue );
    doAfterSet( aId, oldValue, aValue );
  }

  final protected void internalSet( IDataDef aOpId, IAtomicValue aValue ) {
    if( aOpId == null || aValue == null ) {
      throw new TsNullArgumentRtException();
    }
    String id = StridUtils.checkValidIdPath( aOpId.id() );
    IAtomicValue oldValue = internalFind( id );
    doBeforeSet( id, oldValue, aValue );
    doInternalSet( id, aValue );
    doAfterSet( id, oldValue, aValue );
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * Subclass may perform additional action just before value optionis set.
   * <p>
   * Does nothing in base class there, is not no need to call superclass method when overriding.
   *
   * @param aId String - option ID
   * @param aOldValue {@link IAtomicValue} - old value or <code>null</code> if there was no option with such ID
   * @param aNewValue {@link IAtomicValue} - new value, never is <code>null</code>
   */
  protected void doBeforeSet( String aId, IAtomicValue aOldValue, IAtomicValue aNewValue ) {
    // nop
  }

  /**
   * Subclass may perform additional action immediately after option value is set.
   * <p>
   * Does nothing in base class there, is not no need to call superclass method when overriding.
   *
   * @param aId String - option ID
   * @param aOldValue {@link IAtomicValue} - old value or <code>null</code> if there was no option with such ID
   * @param aNewValue {@link IAtomicValue} - new value, never is <code>null</code>
   */
  protected void doAfterSet( String aId, IAtomicValue aOldValue, IAtomicValue aNewValue ) {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  /**
   * Implementation must write value with specified identifier.
   * <p>
   * Value for existing identifier must be overwritten.
   *
   * @param aId String - always is an valid IDpath
   * @param aValue {@link IAtomicValue} - value, never is <code>null</code>
   */
  protected abstract void doInternalSet( String aId, IAtomicValue aValue );

}

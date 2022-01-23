package org.toxsoft.tslib.av.opset.impl;

import static org.toxsoft.tslib.coll.helpers.ECrudOp.*;

import java.util.Iterator;
import java.util.Objects;

import org.toxsoft.tslib.av.IAtomicValue;
import org.toxsoft.tslib.av.metainfo.IDataDef;
import org.toxsoft.tslib.av.opset.*;
import org.toxsoft.tslib.coll.IList;
import org.toxsoft.tslib.coll.IMap;
import org.toxsoft.tslib.coll.notifier.impl.AbstractNotifierMap;
import org.toxsoft.tslib.coll.primtypes.IStringList;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Notification and validation wrapper over {@link IOptionSetEdit}.
 *
 * @author hazard157
 */
public class NotifierOptionSetEditWrapper
    extends AbstractNotifierMap<String, IAtomicValue>
    implements INotifierOptionSetEdit {

  // FIXME check canXxx before editing option set

  private static final long serialVersionUID = 157157L;

  private final IOptionSetEdit source;

  /**
   * Constructor.
   *
   * @param aSource {@link IOptionSetEdit} - the set to be wrapped
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public NotifierOptionSetEditWrapper( IOptionSetEdit aSource ) {
    source = TsNullArgumentRtException.checkNull( aSource );
  }

  // ------------------------------------------------------------------------------------
  // ITsNotifierCollection
  //

  @Override
  public void fireItemByIndexChangeEvent( int aIndex ) {
    fireChangedEvent( EDIT, source.keys().get( aIndex ) );
  }

  @Override
  public void fireItemByRefChangeEvent( Object aItem ) {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // INotifierMap
  //

  @Override
  public void fireItemByKeyChangeEvent( String aKey ) {
    TsNullArgumentRtException.checkNull( aKey );
    String key = aKey;
    if( keys().hasElem( key ) ) {
      fireChangedEvent( EDIT, key );
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsCollection
  //

  @Override
  public boolean hasElem( IAtomicValue aElem ) {
    TsNullArgumentRtException.checkNull( aElem );
    return source.hasElem( aElem );
  }

  @Override
  public IAtomicValue[] toArray( IAtomicValue[] aSrcArray ) {
    return source.toArray( aSrcArray );
  }

  @Override
  public Object[] toArray() {
    return source.toArray();
  }

  @Override
  public Iterator<IAtomicValue> iterator() {
    return source.iterator();
  }

  @Override
  public int size() {
    return source.size();
  }

  // ------------------------------------------------------------------------------------
  // IMap
  //

  @Override
  public boolean hasKey( String aKey ) {
    return source.hasKey( aKey );
  }

  @Override
  public IAtomicValue findByKey( String aKey ) {
    return source.findByKey( aKey );
  }

  @Override
  public IStringList keys() {
    return source.keys();
  }

  @Override
  public IList<IAtomicValue> values() {
    return source.values();
  }

  // ------------------------------------------------------------------------------------
  // IOptionSet
  //

  @Override
  public boolean hasValue( String aId ) {
    return source.hasValue( aId );
  }

  @Override
  public boolean hasValue( IDataDef aOpId ) {
    return source.hasValue( aOpId );
  }

  @Override
  public boolean isNull( String aId ) {
    return source.isNull( aId );
  }

  @Override
  public boolean isNull( IDataDef aOpId ) {
    return source.isNull( aOpId );
  }

  @Override
  public IAtomicValue findValue( String aId ) {
    return source.findValue( aId );
  }

  @Override
  public IAtomicValue findValue( IDataDef aOpId ) {
    return source.findValue( aOpId );
  }

  @Override
  public IAtomicValue getValue( String aId ) {
    return source.getValue( aId );
  }

  @Override
  public IAtomicValue getValue( String aId, IAtomicValue aDefaultValue ) {
    return source.getValue( aId, aDefaultValue );
  }

  @Override
  public IAtomicValue getValue( IDataDef aOpId ) {
    return source.getValue( aOpId );
  }

  @Override
  public boolean getBool( String aId ) {
    return source.getBool( aId );
  }

  @Override
  public boolean getBool( String aId, boolean aDefaultValue ) {
    return source.getBool( aId, aDefaultValue );
  }

  @Override
  public boolean getBool( IDataDef aOpId ) {
    return source.getBool( aOpId );
  }

  @Override
  public int getInt( String aId ) {
    return source.getInt( aId );
  }

  @Override
  public int getInt( String aId, int aDefaultValue ) {
    return source.getInt( aId, aDefaultValue );
  }

  @Override
  public int getInt( IDataDef aOpId ) {
    return source.getInt( aOpId );
  }

  @Override
  public long getLong( String aId ) {
    return source.getLong( aId );
  }

  @Override
  public long getLong( String aId, long aDefaultValue ) {
    return source.getLong( aId, aDefaultValue );
  }

  @Override
  public long getLong( IDataDef aOpId ) {
    return source.getLong( aOpId );
  }

  @Override
  public float getFloat( String aId ) {
    return source.getFloat( aId );
  }

  @Override
  public float getFloat( String aId, float aDefaultValue ) {
    return source.getFloat( aId, aDefaultValue );
  }

  @Override
  public float getFloat( IDataDef aOpId ) {
    return source.getFloat( aOpId );
  }

  @Override
  public double getDouble( String aId ) {
    return source.getDouble( aId );
  }

  @Override
  public double getDouble( String aId, double aDefaultValue ) {
    return source.getDouble( aId, aDefaultValue );
  }

  @Override
  public double getDouble( IDataDef aOpId ) {
    return source.getDouble( aOpId );
  }

  @Override
  public long getTime( String aId ) {
    return source.getTime( aId );
  }

  @Override
  public long getTime( String aId, long aDefaultValue ) {
    return source.getTime( aId, aDefaultValue );
  }

  @Override
  public long getTime( IDataDef aOpId ) {
    return source.getTime( aOpId );
  }

  @Override
  public String getStr( String aId ) {
    return source.getStr( aId );
  }

  @Override
  public String getStr( String aId, String aDefaultValue ) {
    return source.getStr( aId, aDefaultValue );
  }

  @Override
  public String getStr( IDataDef aOpId ) {
    return source.getStr( aOpId );
  }

  @Override
  public <T> T getValobj( String aId ) {
    return source.getValobj( aId );
  }

  @Override
  public <T> T getValobj( String aId, T aDefaultValue ) {
    return source.getValobj( aId, aDefaultValue );
  }

  @Override
  public <T> T getValobj( IDataDef aOpId ) {
    return source.getValobj( aOpId );
  }

  // ------------------------------------------------------------------------------------
  // IStringMapEdit
  //

  @Override
  public IAtomicValue put( String aKey, IAtomicValue aElem ) {
    TsNullArgumentRtException.checkNulls( aElem );
    IAtomicValue oldValue = source.findValue( aKey );
    if( !Objects.equals( oldValue, aElem ) ) {
      source.setValue( aKey, aElem );
      fireChangedEvent( oldValue != null ? EDIT : CREATE, aKey );
    }
    return oldValue;
  }

  @Override
  public IAtomicValue removeByKey( String aKey ) {
    IAtomicValue value = source.removeByKey( aKey );
    if( value != null ) {
      fireChangedEvent( REMOVE, aKey );
    }
    return value;
  }

  // ------------------------------------------------------------------------------------
  // IOptionSetEdit
  //

  @Override
  public void setValue( String aId, IAtomicValue aValue ) {
    TsNullArgumentRtException.checkNulls( aValue );
    IAtomicValue oldValue = source.findValue( aId );
    if( !Objects.equals( oldValue, aValue ) ) {
      source.setValue( aId, aValue );
      fireChangedEvent( oldValue != null ? EDIT : CREATE, aId );
    }
  }

  @Override
  public void setValue( IDataDef aOpId, IAtomicValue aValue ) {
    TsNullArgumentRtException.checkNull( aOpId );
    setValue( aOpId.id(), aValue );
  }

  @Override
  public void setValueIfNull( String aId, IAtomicValue aValue ) {
    IAtomicValue oldValue = source.findValue( aId );
    source.setValueIfNull( aId, aValue );
    if( !Objects.equals( oldValue, source.findValue( aId ) ) ) {
      source.setValue( aId, aValue );
      fireChangedEvent( oldValue != null ? EDIT : CREATE, aId );
    }
  }

  @Override
  public void setBool( String aId, boolean aValue ) {
    IAtomicValue oldValue = source.findValue( aId );
    if( oldValue == null || oldValue.asBool() != aValue ) {
      source.setBool( aId, aValue );
      fireChangedEvent( oldValue != null ? EDIT : CREATE, aId );
    }
  }

  @Override
  public void setBool( IDataDef aOpId, boolean aValue ) {
    TsNullArgumentRtException.checkNull( aOpId );
    setBool( aOpId.id(), aValue );
  }

  @Override
  public void setInt( String aId, int aValue ) {
    IAtomicValue oldValue = source.findValue( aId );
    if( oldValue == null || oldValue.asInt() != aValue ) {
      source.setInt( aId, aValue );
      fireChangedEvent( oldValue != null ? EDIT : CREATE, aId );
    }
  }

  @Override
  public void setInt( IDataDef aOpId, int aValue ) {
    TsNullArgumentRtException.checkNull( aOpId );
    setInt( aOpId.id(), aValue );
  }

  @Override
  public void setLong( String aId, long aValue ) {
    IAtomicValue oldValue = source.findValue( aId );
    if( oldValue == null || oldValue.asLong() != aValue ) {
      source.setLong( aId, aValue );
      fireChangedEvent( oldValue != null ? EDIT : CREATE, aId );
    }
  }

  @Override
  public void setLong( IDataDef aOpId, long aValue ) {
    TsNullArgumentRtException.checkNull( aOpId );
    setLong( aOpId.id(), aValue );
  }

  @Override
  public void setFloat( String aId, float aValue ) {
    IAtomicValue oldValue = source.findValue( aId );
    if( oldValue == null || Float.compare( oldValue.asFloat(), aValue ) != 0 ) {
      source.setFloat( aId, aValue );
      fireChangedEvent( oldValue != null ? EDIT : CREATE, aId );
    }
  }

  @Override
  public void setFloat( IDataDef aOpId, float aValue ) {
    TsNullArgumentRtException.checkNull( aOpId );
    setFloat( aOpId.id(), aValue );
  }

  @Override
  public void setDouble( String aId, double aValue ) {
    IAtomicValue oldValue = source.findValue( aId );
    if( oldValue == null || Double.compare( oldValue.asDouble(), aValue ) != 0 ) {
      source.setDouble( aId, aValue );
      fireChangedEvent( oldValue != null ? EDIT : CREATE, aId );
    }
  }

  @Override
  public void setDouble( IDataDef aOpId, double aValue ) {
    TsNullArgumentRtException.checkNull( aOpId );
    setDouble( aOpId.id(), aValue );
  }

  @Override
  public void setTime( String aId, long aValue ) {
    IAtomicValue oldValue = source.findValue( aId );
    if( oldValue == null || oldValue.asLong() != aValue ) {
      source.setTime( aId, aValue );
      fireChangedEvent( oldValue != null ? EDIT : CREATE, aId );
    }
  }

  @Override
  public void setTime( IDataDef aOpId, long aValue ) {
    TsNullArgumentRtException.checkNull( aOpId );
    setTime( aOpId.id(), aValue );
  }

  @Override
  public void setStr( String aId, String aValue ) {
    IAtomicValue oldValue = source.findValue( aId );
    if( oldValue == null || !oldValue.asString().equals( aValue ) ) {
      source.setStr( aId, aValue );
      fireChangedEvent( oldValue != null ? EDIT : CREATE, aId );
    }
  }

  @Override
  public void setStr( IDataDef aOpId, String aValue ) {
    TsNullArgumentRtException.checkNull( aOpId );
    setStr( aOpId.id(), aValue );
  }

  @Override
  public void setValobj( String aId, Object aValue ) {
    IAtomicValue oldValue = source.findValue( aId );
    if( oldValue == null || oldValue.asValobj() != aValue ) {
      source.setValobj( aId, aValue );
      fireChangedEvent( oldValue != null ? EDIT : CREATE, aId );
    }
  }

  @Override
  public void setValobj( IDataDef aOpId, Object aValue ) {
    TsNullArgumentRtException.checkNull( aOpId );
    setValobj( aOpId.id(), aValue );
  }

  @Override
  public IAtomicValue remove( String aId ) {
    return removeByKey( aId );
  }

  @Override
  public IAtomicValue remove( IDataDef aOpId ) {
    TsNullArgumentRtException.checkNull( aOpId );
    return removeByKey( aOpId.id() );
  }

  @Override
  public void addAll( IOptionSet aOps ) {
    addAll( (IMap<String, ? extends IAtomicValue>)aOps );
  }

  @Override
  public void addAll( IMap<String, ? extends IAtomicValue> aOps ) {
    TsNullArgumentRtException.checkNull( aOps );
    int changesCount = 0;
    String changedKey = null;
    for( String key : aOps.keys() ) {
      IAtomicValue newValue = aOps.getByKey( key );
      IAtomicValue oldValue = this.findByKey( key );
      if( oldValue == null || !Objects.equals( oldValue, newValue ) ) {
        ++changesCount;
        if( changedKey == null ) {
          changedKey = key;
        }
        source.setValue( key, newValue );
      }
    }
    switch( changesCount ) {
      case 0: // no changes
        // nop
        break;
      case 1: // single value changed
        fireChangedEvent( EDIT, changedKey );
        break;
      default: // may values changed
        fireBatchChangeEvent();
    }
  }

  @Override
  public boolean extendSet( IOptionSet aOps ) {
    return extendSet( (IMap<String, ? extends IAtomicValue>)aOps );
  }

  @Override
  public boolean extendSet( IMap<String, ? extends IAtomicValue> aOps ) {
    TsNullArgumentRtException.checkNull( aOps );
    if( !aOps.isEmpty() ) {
      if( source.extendSet( aOps ) ) {
        fireChangedEvent( LIST, null );
        return true;
      }
    }
    return false;
  }

  @Override
  public void setAll( IOptionSet aOps ) {
    setAll( (IMap<String, ? extends IAtomicValue>)aOps );
  }

  @Override
  public void setAll( IMap<String, ? extends IAtomicValue> aOps ) {
    TsNullArgumentRtException.checkNull( aOps );
    if( !this.equals( aOps ) ) {
      source.setAll( aOps );
      fireChangedEvent( LIST, null );
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsClearable
  //

  @Override
  public void clear() {
    if( source.keys().isEmpty() ) {
      return;
    }
    source.clear();
    fireChangedEvent( LIST, null );
  }

}

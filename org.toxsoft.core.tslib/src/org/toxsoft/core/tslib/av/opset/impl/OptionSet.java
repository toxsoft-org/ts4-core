package org.toxsoft.core.tslib.av.opset.impl;

import java.util.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Implementation of {@link IOptionSetEdit}.
 *
 * @author hazard157
 */
public class OptionSet
    extends AbstractOptionsSetter
    implements IOptionSetEdit {

  protected final IStringMapEdit<IAtomicValue> map = new StringMap<>();

  /**
   * Constructor.
   */
  public OptionSet() {
    // nop
  }

  /**
   * Copy constructor.
   *
   * @param aSource {@link IOptionSet} - source set
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public OptionSet( IOptionSet aSource ) {
    addAll( aSource );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  @Override
  protected void doInternalSet( String aId, IAtomicValue aValue ) {
    map.put( aId, aValue );
  }

  @Override
  protected IAtomicValue doInternalFind( String aId ) {
    return map.findByKey( aId );
  }

  // ------------------------------------------------------------------------------------
  // ITsCountableCollection
  //

  @Override
  public int size() {
    return map.size();
  }

  // ------------------------------------------------------------------------------------
  // Iterable
  //

  @Override
  public Iterator<IAtomicValue> iterator() {
    return map.values().iterator();
  }

  // ------------------------------------------------------------------------------------
  // ITsCollection
  //

  @Override
  public boolean hasElem( IAtomicValue aElem ) {
    return map.values().hasElem( aElem );
  }

  @Override
  public IAtomicValue[] toArray( IAtomicValue[] aSrcArray ) {
    return map.values().toArray( aSrcArray );
  }

  @Override
  public Object[] toArray() {
    return map.values().toArray();
  }

  // ------------------------------------------------------------------------------------
  // IMap
  //

  @Override
  public boolean hasKey( String aKey ) {
    return map.hasKey( aKey );
  }

  @Override
  public IAtomicValue findByKey( String aKey ) {
    return map.findByKey( aKey );
  }

  @Override
  public IList<IAtomicValue> values() {
    return map.values();
  }

  // ------------------------------------------------------------------------------------
  // IStringMap
  //

  @Override
  public IStringList keys() {
    return map.keys();
  }

  // ------------------------------------------------------------------------------------
  // IStringMapEdit
  //

  @Override
  public IAtomicValue put( String aKey, IAtomicValue aElem ) {
    IAtomicValue val = findByKey( aKey );
    setValue( aKey, aElem );
    return val;
  }

  @Override
  public IAtomicValue removeByKey( String aKey ) {
    return map.removeByKey( aKey );
  }

  // ------------------------------------------------------------------------------------
  // ITsClearableCollection
  //

  @Override
  public void clear() {
    map.clear();
  }

  // ------------------------------------------------------------------------------------
  // IOptionSetEdit
  //

  @Override
  public IAtomicValue remove( String aId ) {
    return map.removeByKey( aId );
  }

  @Override
  public IAtomicValue remove( IDataDef aOpId ) {
    TsNullArgumentRtException.checkNull( aOpId );
    return map.removeByKey( aOpId.id() );
  }

  @Override
  public void addAll( IOptionSet aOps ) {
    addAll( (IMap<String, IAtomicValue>)aOps );
  }

  @Override
  public void addAll( IMap<String, ? extends IAtomicValue> aOps ) {
    TsNullArgumentRtException.checkNull( aOps );
    if( aOps != this && aOps != map ) {
      map.putAll( aOps );
    }
  }

  @Override
  public boolean extendSet( IOptionSet aOps ) {
    return extendSet( (IMap<String, IAtomicValue>)aOps );
  }

  @Override
  public boolean extendSet( IMap<String, ? extends IAtomicValue> aOps ) {
    TsNullArgumentRtException.checkNull( aOps );
    boolean wasChanged = false;
    if( aOps != this && aOps != map ) {
      for( String key : aOps.keys() ) {
        if( !map.hasKey( key ) ) {
          map.put( key, aOps.getByKey( key ) );
          wasChanged = true;
        }
      }
    }
    return wasChanged;
  }

  @Override
  public boolean refreshSet( IOptionSet aOps ) {
    return refreshSet( (IMap<String, IAtomicValue>)aOps );
  }

  @Override
  public boolean refreshSet( IMap<String, ? extends IAtomicValue> aOps ) {
    TsNullArgumentRtException.checkNull( aOps );
    boolean wasChanged = false;
    if( aOps != this && aOps != map ) {
      for( String key : aOps.keys() ) {
        if( map.hasKey( key ) ) {
          map.put( key, aOps.getByKey( key ) );
          wasChanged = true;
        }
      }
    }
    return wasChanged;
  }

  @Override
  public void setAll( IOptionSet aOps ) {
    map.setAll( aOps );
  }

  @Override
  public void setAll( IMap<String, ? extends IAtomicValue> aOps ) {
    map.setAll( aOps );
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return TsCollectionsUtils.countableCollectionToString( this );
  }

  @Override
  public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    if( aThat instanceof IStringMap ) {
      return map.equals( aThat );
    }
    return false;
  }

  @Override
  public int hashCode() {
    return map.hashCode();
  }

}

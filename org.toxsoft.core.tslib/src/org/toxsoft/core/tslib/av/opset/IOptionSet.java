package org.toxsoft.core.tslib.av.opset;

import java.io.*;
import java.util.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The set of the identified (named) values.
 * <p>
 * Names are IDpaths. Values generally are {@link IAtomicValue} and may be accesses as diferent types using
 * {@link IOpsGetter} interface.
 *
 * @author hazard157
 */
public interface IOptionSet
    extends IOpsGetter, IStringMap<IAtomicValue> {

  /**
   * Singleton of always empty uneditable (immutable) set.
   * <p>
   * All methods except getXxx() with defaults and findXxx() throws an {@link TsNullObjectErrorRtException}.
   */
  IOptionSetEdit NULL = new InternalNullOptionSetEdit();

}

/**
 * Always empty uneditable (immutable) set.
 *
 * @author hazard157ga
 */
class InternalNullOptionSetEdit
    implements IOptionSetEdit, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * Метод корректно восстанавливает сериализированный {@link IOptionSet#EMPTY}.
   *
   * @return Object объект {@link IOptionSet#EMPTY}
   * @throws ObjectStreamException это обявление, оно тут не выбрасывается
   */
  @SuppressWarnings( { "static-method" } )
  private Object readResolve()
      throws ObjectStreamException {
    return IOptionSet.NULL;
  }

  // ------------------------------------------------------------------------------------
  // ITsCountableCollection
  //

  @Override
  public boolean isEmpty() {
    return true;
  }

  @Override
  public int size() {
    return 0;
  }

  // ------------------------------------------------------------------------------------
  // IOptionSet
  //

  @Override
  public boolean hasValue( String aId ) {
    return false;
  }

  @Override
  public boolean hasValue( IDataDef aOpId ) {
    return false;
  }

  @Override
  public boolean isNull( String aId ) {
    return true;
  }

  @Override
  public boolean isNull( IDataDef aOpId ) {
    return true;
  }

  @Override
  public IAtomicValue findValue( String aId ) {
    StridUtils.checkValidIdPath( aId );
    return null;
  }

  @Override
  public IAtomicValue findValue( IDataDef aOpId ) {
    TsNullArgumentRtException.checkNull( aOpId );
    return null;
  }

  @Override
  public IAtomicValue getValue( String aId ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public IAtomicValue getValue( String aId, IAtomicValue aDefaultValue ) {
    StridUtils.checkValidIdPath( aId );
    return aDefaultValue;
  }

  @Override
  public IAtomicValue getValue( IDataDef aOpId ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public boolean getBool( String aId ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public boolean getBool( String aId, boolean aDefaultValue ) {
    StridUtils.checkValidIdPath( aId );
    return aDefaultValue;
  }

  @Override
  public boolean getBool( IDataDef aOpId ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public int getInt( String aId ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public int getInt( String aId, int aDefaultValue ) {
    StridUtils.checkValidIdPath( aId );
    return aDefaultValue;
  }

  @Override
  public int getInt( IDataDef aOpId ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public long getLong( String aId ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public long getLong( String aId, long aDefaultValue ) {
    StridUtils.checkValidIdPath( aId );
    return aDefaultValue;
  }

  @Override
  public long getLong( IDataDef aOpId ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public float getFloat( String aId ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public float getFloat( String aId, float aDefaultValue ) {
    StridUtils.checkValidIdPath( aId );
    return aDefaultValue;
  }

  @Override
  public float getFloat( IDataDef aOpId ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public double getDouble( String aId ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public double getDouble( String aId, double aDefaultValue ) {
    StridUtils.checkValidIdPath( aId );
    return aDefaultValue;
  }

  @Override
  public double getDouble( IDataDef aOpId ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public long getTime( String aId ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public long getTime( String aId, long aDefaultValue ) {
    StridUtils.checkValidIdPath( aId );
    return aDefaultValue;
  }

  @Override
  public long getTime( IDataDef aOpId ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public String getStr( String aId ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public String getStr( String aId, String aDefaultValue ) {
    StridUtils.checkValidIdPath( aId );
    return aDefaultValue;
  }

  @Override
  public String getStr( IDataDef aOpId ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public <T> T getValobj( String aId ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public <T> T getValobj( String aId, T aDefaultValue ) {
    StridUtils.checkValidIdPath( aId );
    return aDefaultValue;
  }

  @Override
  public <T> T getValobj( IDataDef aOpId ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public IStringList keys() {
    return IStringList.EMPTY;
  }

  @Override
  public boolean hasKey( String aKey ) {
    return false;
  }

  @Override
  public IAtomicValue findByKey( String aKey ) {
    return null;
  }

  @Override
  public IList<IAtomicValue> values() {
    return IList.EMPTY;
  }

  @Override
  public boolean hasElem( IAtomicValue aElem ) {
    return false;
  }

  @Override
  public IAtomicValue[] toArray( IAtomicValue[] aSrcArray ) {
    return TsLibUtils.EMPTY_AV_ARRAY;
  }

  @Override
  public Object[] toArray() {
    return TsLibUtils.EMPTY_AV_ARRAY;
  }

  @Override
  public Iterator<IAtomicValue> iterator() {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void setValue( String aId, IAtomicValue aValue ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void setValue( IDataDef aOpId, IAtomicValue aValue ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void setBool( String aId, boolean aValue ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void setBool( IDataDef aOpId, boolean aValue ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void setInt( String aId, int aValue ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void setInt( IDataDef aOpId, int aValue ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void setLong( String aId, long aValue ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void setLong( IDataDef aOpId, long aValue ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void setFloat( String aId, float aValue ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void setFloat( IDataDef aOpId, float aValue ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void setDouble( String aId, double aValue ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void setDouble( IDataDef aOpId, double aValue ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void setTime( String aId, long aValue ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void setTime( IDataDef aOpId, long aValue ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void setStr( String aId, String aValue ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void setStr( IDataDef aOpId, String aValue ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void setValobj( String aId, Object aValue ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void setValobj( IDataDef aOpId, Object aValue ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void clear() {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public IAtomicValue remove( String aId ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public IAtomicValue removeByKey( String aKey ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public IAtomicValue remove( IDataDef aOpId ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public IAtomicValue put( String aKey, IAtomicValue aElem ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void addAll( IOptionSet aOps ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void addAll( IMap<String, ? extends IAtomicValue> aOps ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public boolean extendSet( IOptionSet aOps ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public boolean extendSet( IMap<String, ? extends IAtomicValue> aOps ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void setAll( IOptionSet aOps ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void setAll( IMap<String, ? extends IAtomicValue> aOps ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void setValueIfNull( String aId, IAtomicValue aValue ) {
    throw new TsNullObjectErrorRtException();
  }

}

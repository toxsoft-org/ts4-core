package org.toxsoft.core.tslib.av.props;

import static org.toxsoft.core.tslib.av.props.ITsResources.*;

import java.util.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.errors.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IPropertiesSet} implementation.
 *
 * @author hazard157
 * @param <S> - event source type, the entity characterized by the properties
 */
public class PropertiesSet<S>
    extends AbstractOptionsSetter
    implements IPropertiesSet<S> {

  /**
   * {@link IPropertiesSet#propsEventer()} implementation.
   *
   * @author hazard157
   */
  class Eventer
      extends AbstractTsEventer<IPropertyChangeListener<S>> {

    private IStringMapEdit<IAtomicValue> oldValMap = new StringMap<>();
    private IStringMapEdit<IAtomicValue> newValMap = new StringMap<>();

    @Override
    protected boolean doIsPendingEvents() {
      return !newValMap.isEmpty();
    }

    @Override
    protected void doFirePendingEvents() {
      reallyFirePropsEvent( oldValMap, newValMap );
    }

    @Override
    protected void doClearPendingEvents() {
      oldValMap.clear();
      newValMap.clear();
    }

    @Override
    protected void doStartEventsAccrual() {
      oldValMap.setAll( valuesMap );
    }

    @SuppressWarnings( { "rawtypes", "unchecked" } )
    private void reallyFirePropsEvent( IStringMap<IAtomicValue> aOldValues, IStringMap<IAtomicValue> aNewValues ) {
      IOptionSetEdit oldOps = new OptionSet();
      IOptionSetEdit newOps = new OptionSet();
      oldOps.addAll( aOldValues );
      newOps.addAll( aNewValues );
      for( IPropertyChangeListener l : listeners() ) {
        l.onPropsChanged( source, oldOps, newOps );
      }
    }

    void firePropsChanged( IStringMap<IAtomicValue> aOldValues, IStringMap<IAtomicValue> aNewValues ) {
      if( isFiringPaused() ) {
        // old values were already saved in doStartEventsAccrual()
        newValMap.putAll( aNewValues );
        return;
      }
      reallyFirePropsEvent( aOldValues, aNewValues );
    }

  }

  private final S       source;
  private final Eventer eventer = new Eventer();

  private final IStringMapEdit<IAtomicValue>  valuesMap = new StringMap<>();
  private final IStridablesListEdit<IDataDef> propDefs  = new StridablesList<>();

  private IPropertyChangeInterceptor<S> interceptor = null;

  /**
   * Constructor.
   *
   * @param aSource &lt;S&gt; - the event source
   * @param aPropsDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - properties definitions
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public PropertiesSet( S aSource, IStridablesList<IDataDef> aPropsDefs ) {
    TsNullArgumentRtException.checkNulls( aSource, aPropsDefs );
    source = aSource;
    propDefs.setAll( aPropsDefs );
    for( IDataDef dd : propDefs ) {
      valuesMap.put( dd.id(), dd.defaultValue() );
    }
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void checkValuesMap( IStringMap<IAtomicValue> aValues ) {
    for( String pid : aValues.keys() ) {
      // check property ID is known
      IDataDef pdef = propDefs.findByKey( pid );
      if( pdef == null ) {
        throw new TsItemNotFoundRtException( FMT_ERR_NO_SUCH_PROP, pid );
      }
      // check property value is compatible with definition
      IAtomicValue av = aValues.getByKey( pid );
      if( !AvTypeCastRtException.canAssign( pdef.atomicType(), av.atomicType() ) ) {
        throw new AvTypeCastRtException( FMT_ERR_INV_PROP_TYPE, pdef.id(), pdef.atomicType().id(),
            av.atomicType().id() );
      }
    }
  }

  /**
   * The single method to really change properties values.
   *
   * @param aNewValues {@link IStringMap}&lt;{@link IAtomicValue}&gt; - the new values, must not be <code>null</code>
   */
  private void internalSetProps( IStringMap<IAtomicValue> aNewValues ) {
    checkValuesMap( aNewValues );
    // intercept changes
    IOptionSetEdit opsToSet = new OptionSet();
    opsToSet.addAll( aNewValues );
    if( interceptor != null ) {
      IOptionSetEdit newOps = new OptionSet();
      newOps.addAll( aNewValues );
      interceptor.interceptPropsChange( source, newOps, opsToSet );
      checkValuesMap( opsToSet );
    }
    if( opsToSet.isEmpty() ) {
      return;
    }
    // create map of really values that really changed
    IOptionSetEdit reallyNewValues = new OptionSet();
    for( String pid : opsToSet.keys() ) {
      IAtomicValue avNew = opsToSet.getValue( pid );
      IAtomicValue avOld = valuesMap.getByKey( pid );
      if( !avNew.equals( avOld ) ) {
        reallyNewValues.put( pid, avNew );
      }
    }
    if( reallyNewValues.isEmpty() ) {
      return;
    }
    valuesMap.putAll( reallyNewValues );
    eventer.firePropsChanged( new StringMap<>( valuesMap ), reallyNewValues );
  }

  // ------------------------------------------------------------------------------------
  // AbstractOptionsGetter
  //

  @Override
  protected IAtomicValue doInternalFind( String aId ) {
    return valuesMap.findByKey( aId );
  }

  // ------------------------------------------------------------------------------------
  // AbstractOptionsSetter
  //

  @Override
  protected boolean doBeforeSet( String aId, IAtomicValue aOldValue, IAtomicValue aNewValue ) {
    // check property ID is known
    IDataDef pdef = propDefs.findByKey( aId );
    if( pdef == null ) {
      throw new TsItemNotFoundRtException( FMT_ERR_NO_SUCH_PROP, aId );
    }
    // if value does not really changes simply ignore
    if( aNewValue.equals( valuesMap.getByKey( aId ) ) ) {
      return true;
    }
    // check property value is compatible with definition
    if( !AvTypeCastRtException.canAssign( pdef.atomicType(), aNewValue.atomicType() ) ) {
      throw new AvTypeCastRtException( FMT_ERR_INV_PROP_TYPE, pdef.id(), pdef.atomicType().id(),
          aNewValue.atomicType().id() );
    }
    // intercept
    if( interceptor != null ) {
      IOptionSetEdit newVals = new OptionSet();
      IOptionSetEdit valsToSet = new OptionSet();
      newVals.put( aId, aNewValue );
      valsToSet.put( aId, aNewValue );
      interceptor.interceptPropsChange( source, newVals, valsToSet );
      if( valsToSet.isEmpty() ) {
        return true; // no changes
      }
    }
    return false;
  }

  @Override
  protected void doInternalSet( String aId, IAtomicValue aValue ) {
    valuesMap.put( aId, aValue );
  }

  @Override
  protected void doAfterSet( String aId, IAtomicValue aOldValue, IAtomicValue aNewValue ) {
    IOptionSetEdit newVals = new OptionSet();
    newVals.put( aId, aNewValue );
    eventer.firePropsChanged( new StringMap<>( valuesMap ), newVals );
  }

  // ------------------------------------------------------------------------------------
  // IStringMap
  //

  @Override
  public IStringList keys() {
    return valuesMap.keys();
  }

  @Override
  public boolean hasKey( String aKey ) {
    return valuesMap.hasKey( aKey );
  }

  @Override
  public IAtomicValue findByKey( String aKey ) {
    return valuesMap.findByKey( aKey );
  }

  @Override
  public IList<IAtomicValue> values() {
    return valuesMap.values();
  }

  @Override
  public boolean hasElem( IAtomicValue aElem ) {
    return valuesMap.hasElem( aElem );
  }

  @Override
  public IAtomicValue[] toArray( IAtomicValue[] aSrcArray ) {
    return valuesMap.toArray( aSrcArray );
  }

  @Override
  public Object[] toArray() {
    return valuesMap.toArray();
  }

  @Override
  public Iterator<IAtomicValue> iterator() {
    return valuesMap.iterator();
  }

  @Override
  public int size() {
    return valuesMap.size();
  }

  // ------------------------------------------------------------------------------------
  // IPropertiesSetRo
  //

  @Override
  public IStridablesList<IDataDef> propDefs() {
    return propDefs;
  }

  @Override
  public ITsEventer<IPropertyChangeListener<S>> propsEventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // IPropertiesSet
  //

  @Override
  public void setProps( IStringMap<IAtomicValue> aNewValues ) {
    TsNullArgumentRtException.checkNull( aNewValues );
    internalSetProps( aNewValues );
  }

  @Override
  public void resetToDefaults() {
    IStringMapEdit<IAtomicValue> mm = new StringMap<>();
    for( IDataDef dd : propDefs ) {
      mm.put( dd.id(), dd.defaultValue() );
    }
    setProps( mm );
  }

  @Override
  public void setInterceptor( IPropertyChangeInterceptor<S> aInterceptor ) {
    interceptor = aInterceptor;
  }

}

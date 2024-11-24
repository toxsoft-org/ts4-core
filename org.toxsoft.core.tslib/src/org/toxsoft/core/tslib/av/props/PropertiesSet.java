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
 * <p>
 * Note on implementation: any property change (either single change via <code>setXxx()</code> or batch changes via
 * <code>setProps()</code>) goes through the {@link #internalSetProps(IStringMap)}. This approach ensures correct work
 * of the interceptor. As interceptor may <b>add</b> value to the argument <code>aValuesToSet</code> of the method
 * {@link IPropertyChangeInterceptor#interceptPropsChange(Object, IOptionSet, IOptionSetEdit)}, it is necessary to
 * process even the single property change as the batch update.
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
    protected void doStartEventsAccural() {
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
    doAfterPropValuesSet( reallyNewValues );
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

  /**
   * Base class methods {@link #doBeforeSet(String, IAtomicValue, IAtomicValue)} is not used here.
   */
  @Override
  final protected boolean doBeforeSet( String aId, IAtomicValue aOldValue, IAtomicValue aNewValue ) {
    return false;
  }

  /**
   * This method redirects actual changes to the method {@link #internalSetProps(IStringMap)}.
   */
  @Override
  protected void doInternalSet( String aId, IAtomicValue aValue ) {
    internalSetProps( new SingleElemStringMap<>( aId, aValue ) );
  }

  /**
   * Base class methods {@link #doAfterSet(String, IAtomicValue, IAtomicValue)} is not used here.
   */
  @Override
  protected void doAfterSet( String aId, IAtomicValue aOldValue, IAtomicValue aNewValue ) {
    // nop
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

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * Subclass may perform additional actions after properties are updated but before change event fired.
   * <p>
   * The main and probably the only purpose of this method is to update internal caches based on changed values.
   * <p>
   * <b>WARNING:</b> properties may be read but it is prohibited to <b>set</b> properties values as it may cause errors
   * and infinite loops.
   * <p>
   * Note: old values are are lost at this time and can <b>not</b> be accessed.
   *
   * @param aChangedValues {@link IOptionSet} - really changed values
   */
  protected void doAfterPropValuesSet( IOptionSet aChangedValues ) {
    // nop
  }

}

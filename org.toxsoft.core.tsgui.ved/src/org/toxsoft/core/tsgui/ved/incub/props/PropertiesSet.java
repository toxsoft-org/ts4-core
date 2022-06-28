package org.toxsoft.core.tsgui.ved.incub.props;

import static org.toxsoft.core.tsgui.ved.incub.props.ITsResources.*;

import java.io.*;
import java.util.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.errors.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IPropertiesSet} implementation.
 *
 * @author hazard157
 */
public class PropertiesSet
    extends OptionSet
    implements IPropertiesSet, Serializable {

  private static final long serialVersionUID = 3102088694406134027L;

  /**
   * {@link IPropertiesSetRo#propsEventer()} implementation.
   *
   * @author hazard157
   */
  class Eventer
      extends AbstractTsEventer<IPropertyChangeListener> {

    private IOptionSetEdit oldValues = new OptionSet();
    private IOptionSetEdit newValues = new OptionSet();
    private String         propId    = null;
    private IAtomicValue   oldValue  = null;
    private IAtomicValue   newValue  = null;

    private boolean wasSingle = false;
    private boolean wasMulti  = false;

    @Override
    protected boolean doIsPendingEvents() {
      return wasMulti || wasSingle;
    }

    @Override
    protected void doFirePendingEvents() {
      // single event will be fire anyway, for multi changes with null arguments
      reallyFireSingleEvent( propId, oldValue, newValue );
      if( wasMulti ) {
        reallyFireSeveralPropsEvent( oldValues, newValues );
      }
    }

    @Override
    protected void doClearPendingEvents() {
      wasSingle = false;
      wasMulti = false;
      oldValues.clear();
      newValues.clear();
      propId = null;
      oldValue = null;
      newValue = null;
    }

    @Override
    protected void doStartEventsAccrual() {
      oldValues.setAll( PropertiesSet.this );
    }

    private void reallyFireSingleEvent( String aPropId, IAtomicValue aOld, IAtomicValue aNew ) {
      for( IPropertyChangeListener l : listeners() ) {
        l.onPropertyChanged( PropertiesSet.this, aPropId, aOld, aNew );
      }
    }

    private void reallyFireSeveralPropsEvent( IOptionSet aOldValues, IOptionSet aNewValues ) {
      for( IPropertyChangeListener l : listeners() ) {
        l.onSeveralPropsChanged( PropertiesSet.this, aOldValues, aNewValues );
      }
    }

    void fireSinglePropChange( String aPropId, IAtomicValue aOld, IAtomicValue aNew ) {
      if( isFiringPaused() ) {
        if( !wasMulti ) {
          propId = aPropId;
          oldValue = aOld;
          newValue = aNew;
        }
        else {
          propId = null;
          oldValue = null;
          newValue = null;
        }
        wasMulti = true;
        return;
      }
      reallyFireSingleEvent( aPropId, aOld, aNew );
    }

    void fireSeveralPropsChanged( IOptionSet aOldValues, IOptionSet aNewValues ) {
      if( isFiringPaused() ) {
        // old values were already saved in doStartEventsAccrual()
        newValues.addAll( aNewValues );
        wasMulti = true;
        return;
      }
      reallyFireSeveralPropsEvent( aOldValues, aNewValues );
    }

  }

  private final Eventer eventer = new Eventer();

  private final IStridablesListEdit<IDataDef> propDefs = new StridablesList<>();

  private final IStringMapEdit<IAtomicValue> valuesMap = new StringMap<>();

  /**
   * Flag to temporary disable single change event in {@link #doAfterSet(String, IAtomicValue, IAtomicValue)}.
   * <p>
   * Used on batch changes methods like {@link #setProps(IStringMap)} because they generate several properties change
   * event.
   */
  private boolean disableSingleChangeEvent = false;

  /**
   * Constructor.
   *
   * @param aPropsDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - properties definitions
   */
  public PropertiesSet( IStridablesList<IDataDef> aPropsDefs ) {
    propDefs.setAll( aPropsDefs );
    // initialize default values
    for( IDataDef dd : propDefs ) {
      valuesMap.put( dd.id(), dd.defaultValue() );
    }
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private static void checkPropertyValueIsCompatibleToDefinition( IDataDef aPropDef, IAtomicValue aNewValue ) {
    if( AvTypeCastRtException.canAssign( aPropDef.atomicType(), aNewValue.atomicType() ) ) {
      throw new AvTypeCastRtException( FMT_ERR_INV_PROP_TYPE, aPropDef.id(), aPropDef.atomicType().id(),
          aNewValue.atomicType().id() );
    }
  }

  // ------------------------------------------------------------------------------------
  // AbstractOptionsSetter
  //

  /**
   * Checks if property exists and value is of compatible type.
   */
  @Override
  protected void doBeforeSet( String aId, IAtomicValue aOldValue, IAtomicValue aNewValue ) {
    IDataDef pdef = propDefs.findByKey( aId );
    if( pdef == null ) {
      throw new TsItemNotFoundRtException( FMT_ERR_NO_SUCH_PROP, aId );
    }
    checkPropertyValueIsCompatibleToDefinition( pdef, aNewValue );
  }

  /**
   * Fire change event if value was really changed.
   */
  @Override
  protected void doAfterSet( String aId, IAtomicValue aOldValue, IAtomicValue aNewValue ) {
    if( !disableSingleChangeEvent && !Objects.equals( aOldValue, aNewValue ) ) {
      eventer.fireSinglePropChange( aId, aOldValue, aNewValue );
    }
  }

  @Override
  protected IAtomicValue doInternalFind( String aId ) {
    return valuesMap.findByKey( aId );
  }

  @Override
  protected void doInternalSet( String aId, IAtomicValue aValue ) {
    valuesMap.put( aId, aValue );
  }

  // ------------------------------------------------------------------------------------
  // IPropertiesSetRo
  //

  @Override
  public IStringList ids() {
    return valuesMap.keys();
  }

  @Override
  public ITsEventer<IPropertyChangeListener> propsEventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // IPropertiesSet
  //

  @Override
  public void setProps( IStringMap<IAtomicValue> aNewValues ) {
    disableSingleChangeEvent = true;
    try {
      IOptionSetEdit newValues = new OptionSet(); // onlye changed new values
      // check arguments
      for( IDataDef pdef : propDefs ) {
        IAtomicValue newValue = aNewValues.findByKey( pdef.id() );
        if( newValue != null ) {
          checkPropertyValueIsCompatibleToDefinition( pdef, newValue );
          if( !Objects.equals( newValue, findValue( pdef.id() ) ) ) {
            newValues.setValue( pdef, newValue );
          }
        }
      }
      IOptionSetEdit oldValues = new OptionSet( this );
      // really set values
      valuesMap.putAll( newValues );
      // fire events
      eventer.fireSinglePropChange( null, null, null );
      eventer.fireSeveralPropsChanged( oldValues, newValues );
    }
    finally {
      disableSingleChangeEvent = false;
    }

  }

}

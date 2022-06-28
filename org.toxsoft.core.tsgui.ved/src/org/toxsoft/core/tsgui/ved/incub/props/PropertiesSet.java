package org.toxsoft.core.tsgui.ved.incub.props;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

/**
 * {@link IPropertiesSet} implementation.
 *
 * @author hazard157
 */
public class PropertiesSet
    extends NotifierOptionSetEditWrapper
    implements IPropertiesSet {

  private static final long serialVersionUID = -1737629296534577202L;

  /**
   * {@link IPropertiesSetRo#propsEventer()} implementation.
   *
   * @author hazard157
   */
  class Eventer
      extends AbstractTsEventer<IPropertyChangeListener> {

    private IOptionSetEdit oldValues = new OptionSet();

    @Override
    protected boolean doIsPendingEvents() {
      // TODO Auto-generated method stub
      return false;
    }

    @Override
    protected void doFirePendingEvents() {
      // TODO Auto-generated method stub
    }

    @Override
    protected void doClearPendingEvents() {
      // TODO Auto-generated method stub
    }

    @Override
    protected void doStartEventsAccrual() {
      oldValues.setAll( PropertiesSet.this );
    }

    void fireSinglePropChange( String aPropId, IAtomicValue aOld, IAtomicValue aNew ) {
      // TODO PropertiesSet.Eventer.fireSinglePropChange()
    }

    void fireSeveralPropsChanged( IOptionSet aOldValues, IOptionSet aNewValues ) {
      // TODO PropertiesSet.Eventer.fireSeveralPropsChanged()
    }

  }

  private final Eventer eventer = new Eventer();

  private IStridablesListEdit<IDataDef> propDefs = new StridablesList<>();

  /**
   * Constructor.
   *
   * @param aPropsDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - properties definitions
   */
  public PropertiesSet( IStridablesList<IDataDef> aPropsDefs ) {
    super( new OptionSet() );
    propDefs.setAll( aPropsDefs );
    // initialize default values
    for( IDataDef dd : propDefs ) {
      setValue( dd.id(), dd.defaultValue() );
    }
  }

  @Override
  public ITsEventer<IPropertyChangeListener> propsEventer() {
    return eventer;
  }

  @Override
  public void setProps( IStringMap<IAtomicValue> aNewValues ) {

    // TODO Auto-generated method stub

  }

}

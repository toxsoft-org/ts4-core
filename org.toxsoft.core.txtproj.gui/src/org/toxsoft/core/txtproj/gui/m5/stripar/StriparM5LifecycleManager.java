package org.toxsoft.core.txtproj.gui.m5.stripar;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.stripar.*;

/**
 * STRIPAR entities lifecycle manager.
 * <p>
 * May be used directly or subclassed.
 *
 * @author hazard157
 * @param <E> - modeled STRIPAR type
 */
public class StriparM5LifecycleManager<E extends IStridable & IParameterized>
    extends M5LifecycleManager<E, IStriparManagerApi<E>> {

  /**
   * Constructor.
   *
   * @param aModel {@link IM5Model}&lt;E&gt; - the STIPAR entity M5-model
   * @param aMaster {@link IStriparManager}&lt;E&gt; - the STIPAR entities manager
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public StriparM5LifecycleManager( IM5Model<E> aModel, IStriparManagerApi<E> aMaster ) {
    super( aModel, true, true, true, true, aMaster );
    TsNullArgumentRtException.checkNull( aMaster );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private IOptionSet makeParams( IM5Bunch<E> aValues ) {
    IOptionSetEdit params = new OptionSet();
    doFillParams( aValues, params );
    return params;
  }

  // ------------------------------------------------------------------------------------
  // M5LifecycleManager
  //

  @Override
  protected ValidationResult doBeforeCreate( IM5Bunch<E> aValues ) {
    String id = aValues.getAsAv( FID_ID ).asString();
    IOptionSet params = makeParams( aValues );
    return master().svs().validator().canCreateItem( id, params );
  }

  @Override
  protected E doCreate( IM5Bunch<E> aValues ) {
    String id = aValues.getAsAv( FID_ID ).asString();
    IOptionSet params = makeParams( aValues );
    return master().createItem( id, params );
  }

  @Override
  protected ValidationResult doBeforeEdit( IM5Bunch<E> aValues ) {
    String id = aValues.getAsAv( FID_ID ).asString();
    IOptionSet params = makeParams( aValues );
    return master().svs().validator().canEditItem( aValues.originalEntity().id(), id, params );
  }

  @Override
  protected E doEdit( IM5Bunch<E> aValues ) {
    String id = aValues.getAsAv( FID_ID ).asString();
    IOptionSet params = makeParams( aValues );
    return master().editItem( aValues.originalEntity().id(), id, params );
  }

  @Override
  protected ValidationResult doBeforeRemove( E aEntity ) {
    return master().svs().validator().canRemoveItem( aEntity.id() );
  }

  @Override
  protected void doRemove( E aEntity ) {
    master().removeItem( aEntity.id() );
  }

  @Override
  protected IList<E> doListEntities() {
    return master().items();
  }

  @Override
  protected IListReorderer<E> doGetItemsReorderer() {
    return master().reorderer();
  }

  // ------------------------------------------------------------------------------------
  // To implement/override
  //

  /**
   * FIXME Subclass must fill STRIPAR paremeters from the values bunch.
   *
   * @param aValues {@link IM5Bunch}&lt;E&gt; - bunch of values
   * @param aParams {@link IOptionSetEdit} - parameters to be filler
   */
  protected void doFillParams( IM5Bunch<E> aValues, IOptionSetEdit aParams ) {
    for( IDataDef dd : master().listParamDefs() ) {
      IAtomicValue av = aValues.getAsAv( dd.id() );
      aParams.setValue( dd, av );
    }
  }

}

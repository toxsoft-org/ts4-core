package org.toxsoft.core.txtproj.gui.m5.stripar;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;

import org.toxsoft.core.tsgui.m5.IM5Bunch;
import org.toxsoft.core.tsgui.m5.IM5Model;
import org.toxsoft.core.tsgui.m5.model.impl.M5LifecycleManager;
import org.toxsoft.core.tslib.av.opset.IOptionSet;
import org.toxsoft.core.tslib.av.opset.IOptionSetEdit;
import org.toxsoft.core.tslib.av.opset.impl.OptionSet;
import org.toxsoft.core.tslib.av.utils.IParameterized;
import org.toxsoft.core.tslib.bricks.strid.IStridable;
import org.toxsoft.core.tslib.bricks.validator.ValidationResult;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.core.txtproj.lib.stripar.IStriparManager;

/**
 * Base class for STRIPAR entities lifecycle manager.
 *
 * @author hazard157
 * @param <E> - STRIPAR type
 */
public abstract class StriparM5AbstractLifecycleManager<E extends IStridable & IParameterized>
    extends M5LifecycleManager<E, IStriparManager<E>> {

  /**
   * Constructor.
   *
   * @param aModel {@link IM5Model}&lt;E&gt; - the STIPAR entity M5-model
   * @param aMaster {@link IStriparManager}&lt;E&gt; - the STIPAR entities manager
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public StriparM5AbstractLifecycleManager( IM5Model<E> aModel, IStriparManager<E> aMaster ) {
    super( aModel, true, true, true, true, aMaster );
    TsNullArgumentRtException.checkNull( aMaster );
  }

  // ------------------------------------------------------------------------------------
  // M5LifecycleManager
  //

  private IOptionSet makeOps( IM5Bunch<E> aValues ) {
    IOptionSetEdit ops = new OptionSet();
    doFillParams( aValues, ops );
    return ops;
  }

  @Override
  protected ValidationResult doBeforeCreate( IM5Bunch<E> aValues ) {
    String id = aValues.getAsAv( FID_ID ).asString();
    IOptionSet ops = makeOps( aValues );
    return master().svs().validator().canCreateItem( id, ops );
  }

  @Override
  protected E doCreate( IM5Bunch<E> aValues ) {
    String id = aValues.getAsAv( FID_ID ).asString();
    IOptionSet ops = makeOps( aValues );
    return master().createItem( id, ops );
  }

  @Override
  protected ValidationResult doBeforeEdit( IM5Bunch<E> aValues ) {
    String id = aValues.getAsAv( FID_ID ).asString();
    IOptionSet ops = makeOps( aValues );
    return master().svs().validator().canEditItem( aValues.originalEntity().id(), id, ops );
  }

  @Override
  protected E doEdit( IM5Bunch<E> aValues ) {
    String id = aValues.getAsAv( FID_ID ).asString();
    IOptionSet ops = makeOps( aValues );
    return master().editItem( aValues.originalEntity().id(), id, ops );
  }

  @Override
  protected ValidationResult doBeforeRemove( E aEntity ) {
    return ValidationResult.SUCCESS;
  }

  @Override
  protected void doRemove( E aEntity ) {
    master().removeItem( aEntity.id() );
  }

  @Override
  protected IList<E> doListEntities() {
    return master().items();
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  /**
   * Subclass must fill STRIPAR paremeters from the values bunch.
   *
   * @param aValues {@link IM5Bunch}&lt;E&gt; - bunch of values
   * @param aParams {@link IOptionSetEdit} - parameters to be filler
   */
  protected abstract void doFillParams( IM5Bunch<E> aValues, IOptionSetEdit aParams );

}

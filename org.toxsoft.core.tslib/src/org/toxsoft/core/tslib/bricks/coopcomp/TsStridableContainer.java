package org.toxsoft.core.tslib.bricks.coopcomp;

import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITsStridableContainer} implementation.
 *
 * @author mvk
 */
public class TsStridableContainer
    extends AbstractTsStridableComponent
    implements ITsStridableContainer {

  private final IStridablesListEdit<AbstractTsStridableComponent> componentsStorage = new StridablesList<>();

  private final TsCooperativeContainer container;

  /**
   * Constructor.
   *
   * @param aId String - entity ID (an IDpath)
   * @param aName String - short name
   * @param aDescription String - description
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  @SuppressWarnings( "unchecked" )
  public TsStridableContainer( String aId, String aName, String aDescription ) {
    super( new Stridable( aId, aName, aDescription ) );
    IListEdit<AbstractTsCooperativeComponent> storage =
        (IListEdit<AbstractTsCooperativeComponent>)(Object)componentsStorage;
    container = new TsCooperativeContainer( id(), nmName(), aDescription, storage );
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsCooperativeComponent
  //

  @Override
  protected ValidationResult doInit( ITsContextRo aEnviron ) {
    TsNullArgumentRtException.checkNull( aEnviron );
    ValidationResult result = container.doInit( aEnviron );
    return result;
  }

  @Override
  protected void doStart() {
    container.doStart();
  }

  @Override
  protected void doDoJob() {
    container.doDoJob();
  }

  @Override
  protected boolean doQueryStop() {
    boolean result = container.doQueryStop();
    return result;
  }

  @Override
  protected boolean doStopping() {
    boolean result = container.doStopping();
    return result;
  }

  @Override
  protected void doDestroy() {
    container.doDestroy();
  }

  // ------------------------------------------------------------------------------------
  // ITsStridableContainer
  //

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  @Override
  public IList<ITsStridableComponent> componentList() {
    return (IStridablesList)componentsStorage;
  }

  @Override
  public ValidationResult addComponent( AbstractTsStridableComponent aComponent ) {
    TsNullArgumentRtException.checkNull( aComponent );
    ValidationResult result = container.addComponent( aComponent );
    return result;
  }

  @Override
  public void removeComponent( String aComponentId ) {
    TsNullArgumentRtException.checkNull( aComponentId );
    AbstractTsStridableComponent component = componentsStorage.findByKey( aComponentId );
    if( component != null ) {
      container.removeComponent( component );
    }
  }

  @Override
  void internalSetToFinalState() {
    // the container as a component is always ready for reuse
    compState = ETsCoopCompState.INITIALIZED;
  }
}

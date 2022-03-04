package org.toxsoft.core.txtproj.lib.stripar;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.core.txtproj.lib.impl.*;

/**
 * Реализация {@link IStriparManager}.
 *
 * @author hazard157
 * @param <E> - конкретный тип {@link IStridable} и {@link IParameterized} сущности
 */
public class StriparManager<E extends IStridable & IParameterized>
    extends AbstractProjDataUnit
    implements IStriparManager<E> {

  /**
   * Класс для реализации {@link IStriparManager#genericChangeEventer()}.
   *
   * @author hazard157
   */
  class Eventer
      extends AbstractTsEventer<IStriparManagerListener> {

    private boolean isPending = false;

    @Override
    protected void doClearPendingEvents() {
      isPending = false;
    }

    @Override
    protected void doFirePendingEvents() {
      isPending = false;
      fireChangeEvent( ECrudOp.LIST, null );
    }

    @Override
    protected boolean doIsPendingEvents() {
      return isPending;
    }

    void fireChangeEvent( ECrudOp aOp, String aItemId ) {
      if( isFiringPaused() ) {
        isPending = true;
        return;
      }
      for( IStriparManagerListener l : listeners() ) {
        try {
          l.onChanged( StriparManager.this, aOp, aItemId );
        }
        catch( Exception ex ) {
          LoggerUtils.errorLogger().error( ex );
        }
      }
      genericChangeEventer.fireChangeEvent();
    }

  }

  /**
   * Класс для реализации {@link IStriparManager#svs()}.
   *
   * @author hazard157
   */
  static class ValidationSupport
      extends AbstractTsValidationSupport<IStriparManagerValidator>
      implements IStriparManagerValidator {

    ValidationSupport() {
      // nop
    }

    // ------------------------------------------------------------------------------------
    // Методы класса AbstractServiceValidationSupport
    //

    @Override
    public IStriparManagerValidator validator() {
      return this;
    }

    // ------------------------------------------------------------------------------------
    // Реализация интерфейса ICmdrivenValidator
    //

    @Override
    public ValidationResult canCreateItem( String aId, IOptionSet aInfo ) {
      // TODO Auto-generated method stub
      return ValidationResult.SUCCESS;
    }

    @Override
    public ValidationResult canEditItem( String aOldId, String aId, IOptionSet aInfo ) {
      // TODO Auto-generated method stub
      return ValidationResult.SUCCESS;
    }

    @Override
    public ValidationResult canRemoveItem( String aId ) {
      // TODO Auto-generated method stub
      return ValidationResult.SUCCESS;
    }
  }

  private final Eventer           eventer           = new Eventer();
  private final ValidationSupport validationSupport = new ValidationSupport();

  private final IStriparCreator<E>          creator;
  private final IStridablesListBasicEdit<E> items = new SortedStridablesList<>();

  /**
   * Конструктор.
   *
   * @param aCreator {@link IStriparCreator} - создатель экземпляров
   */
  public StriparManager( IStriparCreator<E> aCreator ) {
    creator = TsNullArgumentRtException.checkNull( aCreator );
  }

  // ------------------------------------------------------------------------------------
  // Реализация AbstractProjDataUnit
  //

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  @Override
  protected void doWrite( IStrioWriter aDw ) {
    StridableParameterized.KEEPER.writeColl( aDw, (ITsCollection)items, true );
  }

  @Override
  protected void doRead( IStrioReader aDr ) {
    IList<StridableParameterized> ll = StridableParameterized.KEEPER.readColl( aDr );
    eventer.pauseFiring();
    items.clear();
    try {
      for( StridableParameterized sp : ll ) {
        E elem = creator.create( sp.id(), sp.params() );
        items.add( elem );
      }
    }
    finally {
      eventer.resumeFiring( false );
      eventer.fireChangeEvent( ECrudOp.LIST, null );
    }
  }

  @Override
  protected void doClear() {
    if( !items.isEmpty() ) {
      items.clear();
      eventer.fireChangeEvent( ECrudOp.LIST, null );
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IStriparManager
  //

  @Override
  public IStridablesList<E> items() {
    return items;
  }

  @Override
  public E createItem( String aId, IOptionSet aInfo ) {
    TsValidationFailedRtException.checkError( validationSupport.canCreateItem( aId, aInfo ) );
    E elem = creator.create( aId, aInfo );
    items.add( elem );
    eventer.fireChangeEvent( ECrudOp.CREATE, aId );
    return elem;
  }

  @Override
  public E editItem( String aOldId, String aId, IOptionSet aInfo ) {
    TsValidationFailedRtException.checkError( validationSupport.canEditItem( aOldId, aId, aInfo ) );
    E elem = items.getByKey( aOldId );
    if( aOldId.equals( aId ) && elem instanceof IParameterizedEdit ) {
      ((IParameterizedEdit)elem).params().addAll( aInfo );
    }
    else {
      IOptionSetEdit params = new OptionSet();
      params.addAll( params );
      elem = creator.create( aId, params );
    }
    items.put( elem );
    eventer.fireChangeEvent( ECrudOp.EDIT, aId );
    return elem;
  }

  @Override
  public void removeItem( String aId ) {
    TsValidationFailedRtException.checkError( validationSupport.canRemoveItem( aId ) );
    items.removeById( aId );
    eventer.fireChangeEvent( ECrudOp.REMOVE, aId );
  }

  @Override
  public ITsEventer<IStriparManagerListener> eventer() {
    return eventer;
  }

  @Override
  public ITsValidationSupport<IStriparManagerValidator> svs() {
    return validationSupport;
  }

}

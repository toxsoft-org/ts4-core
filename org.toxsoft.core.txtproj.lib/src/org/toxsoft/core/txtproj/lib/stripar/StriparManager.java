package org.toxsoft.core.txtproj.lib.stripar;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.txtproj.lib.stripar.ITsResources.*;

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
 * {@link IStriparManager} implementation.
 *
 * @author hazard157
 * @param <E> - stridable parameterized entity type
 */
public class StriparManager<E extends IStridable & IParameterized>
    extends AbstractProjDataUnit
    implements IStriparManager<E> {

  /**
   * {@link IStriparManager#genericChangeEventer()} implementation.
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
      genericChangeEventer().fireChangeEvent();
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
    // AbstractServiceValidationSupport
    //

    @Override
    public IStriparManagerValidator validator() {
      return this;
    }

    // ------------------------------------------------------------------------------------
    // IStriparManagerValidator
    //

    @Override
    public ValidationResult canCreateItem( String aId, IOptionSet aInfo ) {
      TsNullArgumentRtException.checkNulls( aId, aInfo );
      ValidationResult vr = ValidationResult.SUCCESS;
      for( IStriparManagerValidator v : validatorsList() ) {
        vr = ValidationResult.firstNonOk( vr, v.canCreateItem( aId, aInfo ) );
      }
      return vr;
    }

    @Override
    public ValidationResult canEditItem( String aOldId, String aId, IOptionSet aInfo ) {
      TsNullArgumentRtException.checkNulls( aOldId, aId, aInfo );
      ValidationResult vr = ValidationResult.SUCCESS;
      for( IStriparManagerValidator v : validatorsList() ) {
        vr = ValidationResult.firstNonOk( vr, v.canEditItem( aOldId, aId, aInfo ) );
      }
      return vr;
    }

    @Override
    public ValidationResult canRemoveItem( String aId ) {
      TsNullArgumentRtException.checkNull( aId );
      ValidationResult vr = ValidationResult.SUCCESS;
      for( IStriparManagerValidator v : validatorsList() ) {
        vr = ValidationResult.firstNonOk( vr, v.canRemoveItem( aId ) );
      }
      return vr;
    }
  }

  private final IStriparManagerValidator builtinValidator = new IStriparManagerValidator() {

    @Override
    public ValidationResult canCreateItem( String aId, IOptionSet aInfo ) {
      // aId must be an IDpath
      if( !StridUtils.isValidIdPath( aId ) ) {
        return ValidationResult.error( FMT_ERR_ID_NOT_ID_PATH, aId );
      }
      // check ID already exists
      if( items.hasKey( aId ) ) {
        return ValidationResult.error( FMT_ERR_DUP_ID_ON_CREATE, aId );
      }
      // warn on bad name
      String name = DDEF_NAME.getValue( aInfo ).asString();
      if( name.isBlank() || name.equals( DDEF_NAME.defaultValue().asString() ) ) {
        return ValidationResult.warn( MSG_WARN_NO_NAME );
      }
      return ValidationResult.SUCCESS;
    }

    @Override
    public ValidationResult canEditItem( String aOldId, String aId, IOptionSet aInfo ) {
      // aId must be an IDpath
      if( !StridUtils.isValidIdPath( aId ) ) {
        return ValidationResult.error( FMT_ERR_ID_NOT_ID_PATH, aId );
      }
      // check new ID already exists (only when changing IDs)
      if( !aOldId.equals( aId ) ) {
        if( items.hasKey( aId ) ) {
          return ValidationResult.error( FMT_ERR_DUP_ID_ON_EDIT, aId );
        }
      }
      // warn on bad name
      String name = DDEF_NAME.getValue( aInfo ).asString();
      if( name.isBlank() || name.equals( DDEF_NAME.defaultValue().asString() ) ) {
        return ValidationResult.warn( MSG_WARN_NO_NAME );
      }
      return ValidationResult.SUCCESS;
    }

    @Override
    public ValidationResult canRemoveItem( String aId ) {
      // can't remove unexisting item
      if( !items.hasKey( aId ) ) {
        return ValidationResult.error( FMT_ERR_NO_SUCH_ID, aId );
      }
      return ValidationResult.SUCCESS;
    }
  };

  private final Eventer           eventer           = new Eventer();
  private final ValidationSupport validationSupport = new ValidationSupport();

  private final IStriparCreator<E>          creator;
  private final IStridablesListBasicEdit<E> items;

  /**
   * Constructor.
   *
   * @param aCreator {@link IStriparCreator} - elements creator
   * @param aSorted boolean - flags to keep items sorted by ID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public StriparManager( IStriparCreator<E> aCreator, boolean aSorted ) {
    creator = TsNullArgumentRtException.checkNull( aCreator );
    validationSupport.addValidator( builtinValidator );
    if( aSorted ) {
      items = new SortedStridablesList<>();
    }
    else {
      items = new StridablesList<>();
    }
  }

  /**
   * Constructor for unsorted items.
   *
   * @param aCreator {@link IStriparCreator} - elements creator
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public StriparManager( IStriparCreator<E> aCreator ) {
    this( aCreator, false );
  }

  // ------------------------------------------------------------------------------------
  // AbstractProjDataUnit
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
  // IStriparManager
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
  public E editItem( String aId, IOptionSet aParams ) {
    return editItem( aId, aId, aParams );
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

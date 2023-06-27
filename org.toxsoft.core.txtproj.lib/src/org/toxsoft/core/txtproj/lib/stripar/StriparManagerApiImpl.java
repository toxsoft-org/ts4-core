package org.toxsoft.core.txtproj.lib.stripar;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.txtproj.lib.stripar.ITsResources.*;

import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * {@link IStriparManager} implementation.
 *
 * @author hazard157
 * @param <E> - stridable parameterized entity type
 */
public class StriparManagerApiImpl<E extends IStridable & IParameterized>
    implements IStriparManagerApi<E> {

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
          l.onChanged( StriparManagerApiImpl.this, aOp, aItemId );
        }
        catch( Exception ex ) {
          LoggerUtils.errorLogger().error( ex );
        }
      }
      genericChangeEventer().fireChangeEvent();
    }

  }

  /**
   * {@link IStriparManager#svs()} implementation.
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
      // can't remove non-existing item
      if( !items.hasKey( aId ) ) {
        return ValidationResult.error( FMT_ERR_NO_SUCH_ID, aId );
      }
      return ValidationResult.SUCCESS;
    }
  };

  private final GenericChangeEventer          genericChangeEventer;
  private final ValidationSupport             validationSupport = new ValidationSupport();
  private final IStriparCreator<E>            creator;
  private final IStridablesListEdit<IDataDef> paramDefs         = new StridablesList<>();

  protected final Eventer                     eventer = new Eventer();
  protected final IStridablesListBasicEdit<E> items;

  /**
   * Constructor.
   *
   * @param aCreator {@link IStriparCreator} - elements creator
   * @param aSorted boolean - flags to keep items sorted by ID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public StriparManagerApiImpl( IStriparCreator<E> aCreator, boolean aSorted ) {
    creator = TsNullArgumentRtException.checkNull( aCreator );
    genericChangeEventer = new GenericChangeEventer( this );
    validationSupport.addValidator( builtinValidator );
    if( aSorted ) {
      items = new SortedStridablesList<>();
    }
    else {
      items = new StridablesList<>();
    }
    paramDefs.addAll( DDEF_NAME );
    paramDefs.addAll( DDEF_DESCRIPTION );
  }

  /**
   * Constructor for unsorted items.
   *
   * @param aCreator {@link IStriparCreator} - elements creator
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public StriparManagerApiImpl( IStriparCreator<E> aCreator ) {
    this( aCreator, false );
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
  public IStridablesList<IDataDef> listParamDefs() {
    return paramDefs;
  }

  @Override
  public ITsEventer<IStriparManagerListener> eventer() {
    return eventer;
  }

  @Override
  public ITsValidationSupport<IStriparManagerValidator> svs() {
    return validationSupport;
  }

  // ------------------------------------------------------------------------------------
  // ITsClearableEx
  //

  @Override
  public void clear() {
    if( !items.isEmpty() ) {
      items.clear();
      eventer.fireChangeEvent( ECrudOp.LIST, null );
    }
  }

  @Override
  public boolean isClearContent() {
    return items.isEmpty();
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public GenericChangeEventer genericChangeEventer() {
    return genericChangeEventer;
  }

  // ------------------------------------------------------------------------------------
  // API for subclasses
  //

  /**
   * Replaces current content of {@link #items()} by the entities from the argument.
   * <p>
   * Does <b>not</b> generates generic change event.
   *
   * @param aNewContent {@link IStridablesList}&lt;E&gt; - the list of entities
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  protected void replaceItems( IStridablesList<E> aNewContent ) {
    TsNullArgumentRtException.checkNull( aNewContent );
    if( !items.equals( aNewContent ) ) {
      items.setAll( aNewContent );
    }
  }

  /**
   * Replaces current content of {@link #items()} by the entities created from the supplied data.
   * <p>
   * Does <b>not</b> generates generic change event.
   *
   * @param <T> - the type of {@link IStridable} and {@link IParameterized} data item
   * @param aItemsData {@link ITsCollection}&lt;T&gt; - the data items
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  protected <T extends IStridable & IParameterized> void setItemsFromData( ITsCollection<T> aItemsData ) {
    TsNullArgumentRtException.checkNull( aItemsData );
    eventer.pauseFiring();
    items.clear();
    try {
      for( T p : aItemsData ) {
        E elem = creator.create( p.id(), p.params() );
        items.add( elem );
      }
    }
    finally {
      eventer.resumeFiring( false );
    }
  }

  /**
   * Add option definitions to the {@link #listParamDefs()}.
   * <p>
   * Existing options with the same IDs will be replaced.
   *
   * @param aOptionDefs {@link IDataDef}[] - array of options definitions
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  protected void defineOptions( IDataDef... aOptionDefs ) {
    TsErrorUtils.checkArrayArg( aOptionDefs );
    paramDefs.addAll( aOptionDefs );
  }

  /**
   * Add option definitions to the {@link #listParamDefs()}.
   * <p>
   * Existing options with the same IDs will be replaced.
   *
   * @param aOptionDefs {@link IList}&lt;{@link IDataDef}&gt; - list of options definitions
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  protected void defineOptions( IList<IDataDef> aOptionDefs ) {
    TsNullArgumentRtException.checkNull( aOptionDefs );
    paramDefs.addAll( aOptionDefs );
  }

}

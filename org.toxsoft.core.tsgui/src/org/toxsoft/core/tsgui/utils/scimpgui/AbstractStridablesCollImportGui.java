package org.toxsoft.core.tsgui.utils.scimpgui;

import static org.toxsoft.core.tsgui.utils.scimpgui.ITsResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.dialogs.misc.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Helps to perform importing source collection if {@link IStridable} items to the destination editable collection.
 * <p>
 * It is common task to import external (source) collection from the existing (destination) one. If keys in collections
 * are different, there is no problem, simple {@link IStridablesListEdit#addAll(ITsCollection)}. When source contains
 * keys from destination collection there may be different strategies (see {@link EResolveStrategy}). Whole process need
 * GUI for user to select strategy and show every single element overwrite decision dialog if needed. This class helps
 * to implement such process.
 * <p>
 * Usage:
 * <ul>
 * <li>create subclass with {@link #doImportElements(IStridablesList)} implemented;</li>
 * <li>create instance of the subclass and call {@link #prepare(IStridablesList, IStridablesList)};</li>
 * <li>perform import with GUI via by calling method {@link #run()}. Method checks if there is intersection and requests
 * user interaction if needed. In any case, final informational dialog is displayed;</li>
 * <li>optionally {@link #getConflicResolveStrategy()} may be specified <i>before</i> {@link #run()}. If not specified
 * (or specified as <code>null</code>) user will be asked in case of import ID conflicts;</li>
 * <li>method {@link #prepare(IStridablesList, IStridablesList)} and {@link #run()} may be called multiple times.</li>
 * </ul>
 *
 * @author hazard157
 * @param <T> - collection elements type
 */
public abstract class AbstractStridablesCollImportGui<T extends IStridable>
    implements Runnable, ITsGuiContextable {

  private final ITsGuiContext tsContext;

  private IStridablesList<T> source      = null;
  private IStridablesList<T> destination = null;

  /**
   * Conflict resolve strategy, <code>null</code> means not specified yet, user has to select.
   */
  private EResolveStrategy resolveStrategy = null;

  /**
   * Constructor.
   *
   * @param aCotyext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public AbstractStridablesCollImportGui( ITsGuiContext aCotyext ) {
    tsContext = TsNullArgumentRtException.checkNull( aCotyext );
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  /**
   * Asks user to determine resolve strategy.
   *
   * @return {@link EResolveStrategy} - the strategy or <code>null</code> to cancel import process
   */
  private EResolveStrategy askUserForResolveStrategy() {
    ITsGuiContext ctx = new TsGuiContext( tsContext );
    AskValueDialogInfo di = new AskValueDialogInfo( ctx, DLG_SELECT_IMPORT_STRATEGY, DLG_SELECT_IMPORT_STRATEGY_D,
        DataType.create( VALOBJ, TSID_KEEPER_ID, EResolveStrategy.KEEPER_ID ) );
    IAtomicValue av = DialogAskValue.askValue( di, avValobj( EResolveStrategy.CANCEL_IF_ANY_ITEM_EXISTS ) );
    if( av == null ) {
      return null;
    }
    return av.asValobj();
  }

  private void showImportFinishedSuccessDialog( IStridablesList<T> aImportedElems ) {
    if( aImportedElems.isEmpty() ) {
      TsDialogUtils.info( getShell(), MSG_IMPORT_FINISHED_NOTHING );
    }
    else {
      TsDialogUtils.info( getShell(), FMT_IMPORT_FINISHED, Integer.valueOf( aImportedElems.size() ) );
    }
  }

  // ------------------------------------------------------------------------------------
  // Runnable
  //

  /**
   * Starts import process, invokes dialogs if necessary.
   *
   * @throws TsIllegalStateRtException importer was not prepared
   */
  @Override
  public void run() {
    TsIllegalStateRtException.checkNull( source );
    TsIllegalStateRtException.checkNull( destination );
    // if there is no key conflict, simply import elements without any additional user interaction
    boolean hasKeyConflict = TsCollectionsUtils.intersects( destination.keys(), source.keys() );
    if( !hasKeyConflict ) {
      doImportElements( source );
      showImportFinishedSuccessDialog( source );
      return;
    }
    // determine resolve strategy
    if( resolveStrategy == null ) { // ask user if not fixed
      resolveStrategy = askUserForResolveStrategy();
    }
    if( resolveStrategy == null ) { // user cancelled import
      return;
    }
    // prepare list of elements to be imported
    IStridablesListEdit<T> llToImport = new StridablesList<>();

    switch( resolveStrategy ) {
      case CANCEL_IF_ANY_ITEM_EXISTS: {
        TsDialogUtils.warn( getShell(), MSG_DUP_STRID_ON_INPORT_CANCELLED );
        return;
      }
      case ASK_EACH_ITEM_OVERWRITE: {
        for( T item : source ) {
          if( destination.hasKey( item.id() ) ) {
            ETsDialogCode dc = TsDialogUtils.askYesNoCancel( getShell(), FMT_ASK_IMPORT_REPLACE_ID, item.id() );
            switch( dc ) {
              case YES: {
                llToImport.add( item );
                break;
              }
              case NO: { // do not import this item and continue loop
                continue;
              }
              // $CASES-OMITTED$
              default: // cancel import process with no changes
                TsDialogUtils.warn( getShell(), MSG_USER_CANCELLED_INPORT );
                return;
            }
          }
        }
        break;
      }
      case BYPASS_EXISTING_ITEMS: {
        for( T item : source ) {
          if( !destination.hasKey( item.id() ) ) { // skip existing items
            llToImport.add( item );
          }
        }
        break;
      }
      case OVERWRITE_EXISTING: {
        for( T item : source ) {
          if( destination.hasKey( item.id() ) ) { // remove existing items
            llToImport.add( item );
          }
        }
        break;
      }
      default:
        throw new TsNotAllEnumsUsedRtException( resolveStrategy.id() );
    }
    doImportElements( llToImport );
    showImportFinishedSuccessDialog( llToImport );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Prepares {@link #run()} to import from <code>aSource</code> to <code>aDestination</code>.
   *
   * @param aSource {@link IStridablesList}&lt;T&gt; - collection where to import to
   * @param aDestination {@link IStridablesList}&lt;T&gt; - collection to import from
   */
  public void prepare( IStridablesList<T> aSource, IStridablesList<T> aDestination ) {
    TsNullArgumentRtException.checkNulls( aSource, aDestination );
    source = aSource;
    destination = aDestination;
  }

  /**
   * Returns the current conflict resolve strategy.
   *
   * @return {@link EResolveStrategy} - resolve strategy or <code>null</code> if not specified
   */
  public EResolveStrategy getConflicResolveStrategy() {
    return resolveStrategy;
  }

  /**
   * Sets the conflict resolve strategy.
   *
   * @param aStrategy {@link EResolveStrategy} - strategy or <code>null</code> to ask user
   */
  public void setConflicResolveStrategy( EResolveStrategy aStrategy ) {
    resolveStrategy = aStrategy;
  }

  // ------------------------------------------------------------------------------------
  // To override/implement
  //

  /**
   * Implementation must import all items from argument.
   * <p>
   * Argument contains item that must be imported including items with the same IDs (id any) as exited one.
   *
   * @param aColl {@link IStridablesList}&lt;T&gt; - items to be imported
   */
  protected abstract void doImportElements( IStridablesList<T> aColl );

}

package org.toxsoft.core.tsgui.ved.screen.impl;

import static org.toxsoft.core.tsgui.ved.l10n.ITsguiVedSharedResources.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;

import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.errors.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.gw.time.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedVisel} base implementation.
 *
 * @author hazard157
 */
public class VedAbstractActor
    extends VedAbstractItem
    implements IVedActor, ITsUserInputListener, IGwTimeFleetable, IRealTimeSensitive {

  /**
   * Constructor.
   *
   * @param aConfig {@link IVedItemCfg} - the item config
   * @param aPropDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - properties definitions
   * @param aVedScreen {@link VedScreen} - the owner screen
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public VedAbstractActor( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
    TsIllegalArgumentRtException.checkTrue( aConfig.kind() != EVedItemKind.ACTOR );
    for( String pid : ACTOR_MANDATORY_PROP_IDS ) {
      if( !aPropDefs.hasKey( pid ) ) {
        throw new TsIllegalArgumentRtException( FMT_ERR_NO_MANDATORY_ACTOR_PROP, pid );
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // API for subclasses
  //

  /**
   * Finds the VISEL on screen by ID.
   *
   * @param aViselId String - the VISEL ID
   * @return {@link IVedVisel} - found VISEL or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedAbstractVisel findVisel( String aViselId ) {
    return vedScreen().model().visels().list().findByKey( aViselId );
  }

  /**
   * Returns the VISEL on screen by ID.
   *
   * @param aViselId String - the VISEL ID
   * @return {@link IVedVisel} - found VISEL
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no such VISEL found
   */
  public VedAbstractVisel getVisel( String aViselId ) {
    return vedScreen().model().visels().list().getByKey( aViselId );
  }

  /**
   * Returns the bound VISEL on screen.
   * <p>
   * Bound VISEL is the VISEL with ID specified in the property {@link IVedScreenConstants#PROP_VISEL_ID}.
   *
   * @return {@link IVedVisel} - found VISEL
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no such VISEL found
   */
  public VedAbstractVisel getVisel() {
    return getVisel( props().getStr( PROPID_VISEL_ID ) );
  }

  // ------------------------------------------------------------------------------------
  // package API
  //

  final ITsUserInputListener userInputListener() {
    return this;
  }

  // ------------------------------------------------------------------------------------
  // IGwTimeFleetable
  //

  @Override
  public void whenGwTimePassed( long aGwTime ) {
    // this method is to be overridden
  }

  // ------------------------------------------------------------------------------------
  // IRealTimeSensitive
  //

  @Override
  public void whenRealTimePassed( long aRtTime ) {
    // this method is to be overridden
  }

  // ------------------------------------------------------------------------------------
  // IVedItem
  //

  @Override
  final public EVedItemKind kind() {
    return EVedItemKind.ACTOR;
  }

  // ------------------------------------------------------------------------------------
  // IVedActor
  //

  @Override
  final public IStringList listBoundViselIds() {
    IStringList ll = doListBoundViselIds();
    TsInternalErrorRtException.checkNull( ll );
    for( String vid : ll ) {
      if( !vid.isEmpty() ) {
        TsInternalErrorRtException.checkFalse( StridUtils.isValidIdPath( vid ) );
      }
    }
    return ll;
  }

  @Override
  public void replaceBoundVisel( String aOldViselId, String aNewViselId ) {
    StridUtils.checkValidIdPath( aOldViselId );
    TsNullArgumentRtException.checkNull( aNewViselId );
    if( !aNewViselId.isEmpty() ) {
      StridUtils.checkValidIdPath( aNewViselId );
    }
    if( aOldViselId.equals( aNewViselId ) ) {
      return;
    }
    // TODO Auto-generated method stub

  }

  // ------------------------------------------------------------------------------------
  // API for subclasses
  //

  /**
   * Sets the property {@link IVedScreenConstants#PROP_VISEL_ID} of the VISEL {@link IVedScreenConstants#PROP_VISEL_ID}.
   * <p>
   * On any error (VISEL or property not exists, incompatible data type, etc.) does nothing without exceptions.
   * <p>
   * Note: does not redraws VISEL.
   *
   * @param aValue {@link IAtomicValue} - the value to set
   * @return boolean - <code>true</code> if property value change method was called
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public boolean setStdViselPropValue( IAtomicValue aValue ) {
    TsNullArgumentRtException.checkNull( aValue );
    String viselId = props().getStr( PROP_VISEL_ID );
    String viselPropId = props().getStr( PROP_VISEL_PROP_ID );
    return setViselPropValue( viselId, viselPropId, aValue );
  }

  /**
   * Sets the property <code>aViselPropId</code> of the VISEL <code>aViselId</code>.
   * <p>
   * On any error (VISEL or property not exists, incompatible data type, etc.) does nothing without exceptions.
   * <p>
   * Note: does not redraws VISEL.
   *
   * @param aViselId String - the VISEL ID
   * @param aViselPropId String - the ID of the property to be changed
   * @param aValue {@link IAtomicValue} - the value to set
   * @return boolean - <code>true</code> if property value change method was called
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public boolean setViselPropValue( String aViselId, String aViselPropId, IAtomicValue aValue ) {
    TsNullArgumentRtException.checkNulls( aViselId, aViselPropId, aValue );
    IVedVisel visel = findVisel( aViselId );
    if( visel != null ) {
      IDataDef propDef = visel.props().propDefs().findByKey( aViselPropId );
      if( propDef != null ) {
        if( AvTypeCastRtException.canAssign( propDef.atomicType(), aValue.atomicType() ) ) {
          visel.props().setValue( aViselPropId, aValue );

          // DEBUG ---
          // while not drawing whole screen, redraw visel
          vedScreen().view().redrawVisel( aViselId );
          // ---

          return true;
        }
      }
    }
    return false;
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * If bound to VISEL other than {@link IVedScreenConstants#PROP_VISEL_ID}, subclass must return bound VISEL IDs.
   * <p>
   * In the base class returns the value of the property {@link IVedScreenConstants#PROP_VISEL_ID}, if such property
   * exists in actor and contains valid IDpath value. No need to call superclass method when overriding.
   * <p>
   * Returned list must contain only valid IDpaths, however it is <b>not</b> necessary the VISEL with the ID exists.
   *
   * @return {@link IStringList} - list of bound VISEL IDs
   */
  protected IStringList doListBoundViselIds() {
    if( props().hasKey( PROPID_VISEL_ID ) ) {
      String viselId = props().getStr( PROPID_VISEL_ID );
      if( StridUtils.isValidIdPath( viselId ) ) {
        return new SingleStringList( viselId );
      }
    }
    return IStringList.EMPTY;
  }

  /**
   * Subclass should process process VISEL ID replacement {@link #replaceBoundVisel(String, String)}.
   *
   * @param aOldViselId String - the ID of VISEL this actor currently is bind to, always an IDpath
   * @param aNewViselId String - the ID of the replacement VISEL, always an IDpath or or an empty string
   */
  protected void doReplaceBoundVisel( String aOldViselId, String aNewViselId ) {
    if( props().hasKey( PROPID_VISEL_ID ) ) {
      if( props().hasKey( PROPID_VISEL_ID ) ) {
        if( props().getStr( PROPID_VISEL_ID ).equals( aOldViselId ) ) {
          props().setStr( PROPID_VISEL_ID, aNewViselId );
        }
      }
    }
  }

}

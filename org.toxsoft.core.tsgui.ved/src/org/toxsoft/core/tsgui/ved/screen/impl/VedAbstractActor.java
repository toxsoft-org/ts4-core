package org.toxsoft.core.tsgui.ved.screen.impl;

import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;

import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
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
    /**
     * TODO check that mandatory properties exists in the actor
     */
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
  public IVedVisel findVisel( String aViselId ) {
    return vedScreen().model().visels().listAllItems().findByKey( aViselId );
  }

  /**
   * Returns the VISEL on screen by ID.
   *
   * @param aViselId String - the VISEL ID
   * @return {@link IVedVisel} - found VISEL
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no such VISEL found
   */
  public IVedVisel getVisel( String aViselId ) {
    return vedScreen().model().visels().listAllItems().getByKey( aViselId );
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
  public IVedVisel getVisel() {
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
  // IVedActor
  //

  // nop

}

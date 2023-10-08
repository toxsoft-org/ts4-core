package org.toxsoft.core.tsgui.ved.screen.impl;

import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;

import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.d2.helpers.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
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

  private final D2Convertor convertor = new D2Convertor();

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

  /**
   * Converts SWT related coordinates of the specified VISEL to the VED screen virtual coordinates.
   *
   * @param aCoors {@link ITsPoint} - SWT coordinates
   * @param aItem {@link VedAbstractVisel} - the VISEL
   * @return {@link ID2Point} - the point in the VED screen virtual coordinates space
   */
  public ID2Point toVisel( ITsPoint aCoors, VedAbstractVisel aItem ) {
    convertor.setConversion( vedScreen().view().getConversion() );
    double x1 = convertor.reverseX( aCoors.x(), aCoors.y() );
    double y1 = convertor.reverseY( aCoors.x(), aCoors.y() );
    convertor.setConversion( aItem.getConversion() );
    double x = convertor.reverseX( x1, y1 );
    double y = convertor.reverseY( x1, y1 );
    return new D2Point( x, y );
  }

  /**
   * Converts SWT related coordinates of the specified VISEL to the VED screen virtual coordinates.
   *
   * @param aX int - SWT X-coordinates
   * @param aY int - SWT Y-coordinates
   * @param aItem {@link VedAbstractVisel} - the VISEL
   * @return {@link ID2Point} - the point in the VED screen virtual coordinates space
   */
  public ID2Point toVisel( int aX, int aY, VedAbstractVisel aItem ) {
    convertor.setConversion( vedScreen().view().getConversion() );
    double x1 = convertor.reverseX( aX, aY );
    double y1 = convertor.reverseY( aX, aY );
    convertor.setConversion( aItem.getConversion() );
    double x = convertor.reverseX( x1, y1 );
    double y = convertor.reverseY( x1, y1 );
    return new D2Point( x, y );
  }

  /**
   * Converts SWT related coordinates to the VED screen virtual coordinates.
   *
   * @param aX int - SWT X-coordinates
   * @param aY int - SWT Y-coordinates
   * @return {@link ID2Point} - the point in the VED screen virtual coordinates space
   */
  public ID2Point toVedScreen( int aX, int aY ) {
    convertor.setConversion( vedScreen().view().getConversion() );
    double x = convertor.reverseX( aX, aY );
    double y = convertor.reverseY( aX, aY );
    return new D2Point( x, y );
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

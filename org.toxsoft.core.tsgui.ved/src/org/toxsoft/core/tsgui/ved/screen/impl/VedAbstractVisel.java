package org.toxsoft.core.tsgui.ved.screen.impl;

import static org.toxsoft.core.tsgui.ved.l10n.ITsguiVedSharedResources.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedVisel} base implementation.
 *
 * @author hazard157, vs
 */
public abstract class VedAbstractVisel
    extends VedAbstractItem
    implements IVedVisel {

  private static final IAtomicValue MIN_DIMENSION_AV = avFloat( 1.0 );

  private final D2RectangleEdit boundsRect = new D2RectangleEdit( 0.0, 0.0, 100.0, 100.0 );

  /**
   * Constructor.
   *
   * @param aConfig {@link IVedItemCfg} - the item config
   * @param aPropDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - properties definitions
   * @param aVedScreen {@link VedScreen} - the owner screen
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public VedAbstractVisel( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
    TsIllegalArgumentRtException.checkTrue( aConfig.kind() != EVedItemKind.VISEL );
    for( String pid : VISEL_MANDATORY_PROP_IDS ) {
      if( !aPropDefs.hasKey( pid ) ) {
        throw new TsIllegalArgumentRtException( FMT_ERR_NO_MANDATORY_VISEL_PROP, pid );
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void internalUpdateBoundsRect() {
    int x = (int)props().getFloat( PROP_X );
    int y = (int)props().getFloat( PROP_Y );
    int w = (int)props().getFloat( PROP_WIDTH );
    int h = (int)props().getFloat( PROP_HEIGHT );
    boundsRect.setRect( x, y, w, h );
  }

  // ------------------------------------------------------------------------------------
  // IPointsHost
  //

  @Override
  public ID2Rectangle bounds() {
    return boundsRect;
  }

  @Override
  public boolean isYours( double aX, double aY ) {
    return boundsRect.contains( aX, aY );
  }

  // ------------------------------------------------------------------------------------
  // ID2Conversionable
  //

  @Override
  public ID2Conversion getConversion() {
    return props().getValobj( PROP_TRANSFORM );
  }

  @Override
  public void setConversion( ID2Conversion aConversion ) {
    props().setValobj( PROP_TRANSFORM, aConversion );
  }

  // ------------------------------------------------------------------------------------
  // ID2Portable
  //

  @Override
  public double originX() {
    return props().getDouble( PROP_X );
  }

  @Override
  public double originY() {
    return props().getDouble( PROP_Y );
  }

  @Override
  public void setLocation( double aX, double aY ) {
    IStringMapEdit<IAtomicValue> values = new StringMap<>();
    values.put( PROPID_X, avFloat( aX ) );
    values.put( PROPID_Y, avFloat( aY ) );
    props().setProps( values );
  }

  // ------------------------------------------------------------------------------------
  // ID2Resizable
  //

  @Override
  public double width() {
    return props().getDouble( PROP_WIDTH );
  }

  @Override
  public double height() {
    return props().getDouble( PROP_HEIGHT );
  }

  @Override
  public void setSize( double aWidth, double aHeight ) {
    IStringMapEdit<IAtomicValue> values = new StringMap<>();
    values.put( PROPID_WIDTH, avFloat( aWidth ) );
    values.put( PROPID_HEIGHT, avFloat( aHeight ) );
    props().setProps( values );
  }

  // ------------------------------------------------------------------------------------
  // IVedItem
  //

  @Override
  final public EVedItemKind kind() {
    return EVedItemKind.VISEL;
  }

  // ------------------------------------------------------------------------------------
  // IVisel
  //

  // ------------------------------------------------------------------------------------
  // VedAbstractItem
  //

  @Override
  final protected void doInterceptPropsChange( IOptionSet aNewValues, IOptionSetEdit aValuesToSet ) {
    // ensure width and height always are greater or equal to 1.0
    if( aNewValues.hasKey( PROPID_WIDTH ) ) {
      double width = aNewValues.getDouble( PROP_WIDTH );
      if( width < MIN_DIMENSION_AV.asDouble() ) {
        aValuesToSet.setValue( PROP_WIDTH, MIN_DIMENSION_AV );
      }
    }
    if( aNewValues.hasKey( PROPID_HEIGHT ) ) {
      double height = aNewValues.getDouble( PROP_HEIGHT );
      if( height < MIN_DIMENSION_AV.asDouble() ) {
        aValuesToSet.setValue( PROP_HEIGHT, MIN_DIMENSION_AV );
      }
    }
    // subclasses
    doDoInterceptPropsChange( aNewValues, aValuesToSet );
    // check after subclass
    if( aValuesToSet.hasKey( PROPID_WIDTH ) ) {
      double width = aValuesToSet.getDouble( PROP_WIDTH );
      TsInternalErrorRtException.checkTrue( width < MIN_DIMENSION_AV.asDouble() );
    }
    if( aValuesToSet.hasKey( PROPID_HEIGHT ) ) {
      double height = aValuesToSet.getDouble( PROP_HEIGHT );
      TsInternalErrorRtException.checkTrue( height < MIN_DIMENSION_AV.asDouble() );
    }
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * Returns the vertex set used for VISEL editing.
   * <p>
   * By default returns an instance of the {@link VedFulcrumVertexSet} with all fulcrums created.
   * <p>
   * Subclass may override to return it's own implementation of the {@link IVedVertexSet}.
   *
   * @return {@link VedAbstractVertexSet} - created vertex set, never is <code>null</code>
   */
  public VedAbstractVertexSet createVertexSet() {
    return VedFulcrumVertexSet.createWithFulcrums( this, vedScreen() );
  }

  /**
   * In {@link VedAbstractVisel} updates internal caches.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void doUpdateCachesAfterPropsChange( IOptionSet aChangedValue ) {
    internalUpdateBoundsRect();
  }

  /**
   * Subclass may process property values change request.
   * <p>
   * Editable argument <code>aValuesToSet</code> is the values, that will be set to properties. It initially contains
   * the same vales as <code>aNewValues</code>. Interceptor may remove values from <code>aValuesToSet</code> edit
   * existing, add any other properties values or event clear to cancel changes. Current values of the properties may be
   * accessed via {@link #props()}.
   * <p>
   * Does nothing in the base class, but in the inheritance tree, subclasses must call the superclass method.
   *
   * @param aNewValues {@link IOptionSetEdit} - changed properties values after change
   * @param aValuesToSet {@link IOptionSet} - the values to be set after interception
   */
  protected void doDoInterceptPropsChange( IOptionSet aNewValues, IOptionSetEdit aValuesToSet ) {
    // nop
  }

}

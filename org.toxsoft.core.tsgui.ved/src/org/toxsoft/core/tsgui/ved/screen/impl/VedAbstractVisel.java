package org.toxsoft.core.tsgui.ved.screen.impl;

import static org.toxsoft.core.tsgui.bricks.actions.TsActionDef.*;
import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.l10n.ITsguiVedSharedResources.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.actions.asp.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.mws.services.timers.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.math.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedVisel} base implementation.
 *
 * @author hazard157, vs
 */
public abstract class VedAbstractVisel
    extends VedAbstractItem
    implements IVedVisel {

  /**
   * Поставщик действия "упаковать".
   *
   * @author vs
   */
  public class AspPackVisel
      extends MethodPerActionTsActionSetProvider {

    /**
     * ID of action {@link #ACDEF_VED_PACK_VISEL}.
     */
    protected static final String ACTID_VED_PACK_VISEL = "act.visel.pack"; //$NON-NLS-1$

    /**
     * Action: copy selected visels and associated actors to the internal buffer.
     */
    public static final ITsActionDef ACDEF_VED_PACK_VISEL = ofPush2( ACTID_VED_PACK_VISEL, //
        "Поджать размер", "Убрать все дополнительные внешние отсупы и поля для минимизации размера", ICONID_SHRINK );

    public AspPackVisel() {
      defineAction( ACDEF_VED_PACK_VISEL, () -> pack() );
    }

  }

  private static final DoubleRange DIMENSION_RANGE = new DoubleRange( 1.0, Double.MAX_VALUE );

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
    // built-in interceptors
    addInterceptor( new VedViselInterceptorMinWidthHeight( this ) );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void internalUpdateBoundsRect() {
    TsFulcrum tsf = props().getValobj( PROPID_TS_FULCRUM );
    double x = props().getFloat( PROP_X );
    double y = props().getFloat( PROP_Y );
    double w = props().getFloat( PROP_WIDTH );
    double h = props().getFloat( PROP_HEIGHT );

    double dx = (tsf.xPerc() * w) / 100.;
    double dy = (tsf.yPerc() * h) / 100.;

    boundsRect.setRect( x - dx, y - dy, w, h );
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
    double width = props().getDouble( PROP_WIDTH );
    double height = props().getDouble( PROP_HEIGHT );
    return aX >= 0 && aX <= width && aY >= 0 && aY <= height;
  }

  // ------------------------------------------------------------------------------------
  // ID2Conversionable
  //

  @Override
  public ID2Conversion getConversion() {
    double zoom = props().getDouble( PROP_ZOOM );
    ID2Angle angle = props().getValobj( PROP_ANGLE );
    TsFulcrum tsf = props().getValobj( PROP_TS_FULCRUM );

    double x = props().getDouble( PROP_X );
    double y = props().getDouble( PROP_Y );
    double width = props().getDouble( PROP_WIDTH );
    double height = props().getDouble( PROP_HEIGHT );
    double dx = (tsf.xPerc() * width) / 100.;
    double dy = (tsf.yPerc() * height) / 100.;

    D2ConversionEdit d2Conversion = new D2ConversionEdit( angle, zoom, new D2Point( x - dx * zoom, y - dy * zoom ) );
    // D2ConversionEdit d2Conversion = new D2ConversionEdit( angle, zoom, new D2Point( x - dx, y - dy ) );
    return d2Conversion;

    // return props().getValobj( PROP_TRANSFORM );
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
    // subclasses
    doDoInterceptPropsChange( aNewValues, aValuesToSet );
    // check after subclass
    if( aValuesToSet.hasKey( PROPID_WIDTH ) ) {
      DIMENSION_RANGE.checkInRange( aValuesToSet.getDouble( PROP_WIDTH ) );
    }
    if( aValuesToSet.hasKey( PROPID_HEIGHT ) ) {
      DIMENSION_RANGE.checkInRange( aValuesToSet.getDouble( PROP_HEIGHT ) );
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

  // ------------------------------------------------------------------------------------
  // VedAbstractItem
  //

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

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * Implementation may process time flow to animate drawing.
   * <p>
   * This method is called by the screen at the {@link ITsGuiTimersService#getQuickTimerPeriod()} interval.
   * <p>
   * VISEL can not and must not redraw itself event for animation needs. Instead VISEL informs container (the VED
   * screen) it needs to be redrawn by returning <code>true</code>.
   * <p>
   * Method in base class returns <code>false</code>, no need to call parent method when overriding.
   *
   * @param aRtTime long - current time in milliseconds from epoch start
   * @return boolean - <code>true</code> when VISEL needs redraw
   */
  protected boolean doProcessRealTimePassed( long aRtTime ) {
    return false;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает X-координату точки, вокруг которой осуществляется вращение.
   *
   * @return double - X-координату точки, вокруг которой осуществляется вращение
   */
  public double rotationX() {
    double zoom = props().getDouble( PROP_ZOOM );
    TsFulcrum tsf = props().getValobj( PROP_TS_FULCRUM );
    double width = props().getDouble( PROP_WIDTH );
    return zoom * (tsf.xPerc() * width) / 100.;
  }

  /**
   * Возвращает Y-координату точки, вокруг которой осуществляется вращение.
   *
   * @return double - Y-координату точки, вокруг которой осуществляется вращение
   */
  public double rotationY() {
    double zoom = props().getDouble( PROP_ZOOM );
    TsFulcrum tsf = props().getValobj( PROP_TS_FULCRUM );
    double height = props().getDouble( PROP_HEIGHT );
    return zoom * (tsf.yPerc() * height) / 100.;
  }

  // ------------------------------------------------------------------------------------
  // Action handlers
  //

  /**
   * Reduces visel size to its minimal meaningful size.<br>
   * by default - does nothig.
   */
  private final void pack() {
    ID2Point p = getPackedSize( -1., -1. );
    setSize( p.x(), p.y() );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  /**
   * Возвращает упакованный размер визуального элемента если один из aWidth или aHeight < 0.0, то соответствующее
   * измерение вычисляется (запрос типа "Дай минимальную высоту при заданной ширине"). А если оба меньше нуля, то
   * вычисляется минимальный обрамляющие прямоугольник. При этом, реализация метода по умолчанию возвращает размеры
   * прямоугольника метода {@link #bounds()}.
   *
   * @param aWidth double - желаемая ширина или -1
   * @param aHeight double - желаемая высота или -1
   * @return {@link ID2Point} - упакованный размер визуального элемента
   */
  protected ID2Point getPackedSize( double aWidth, double aHeight ) {
    ID2Rectangle r = bounds();
    return new D2Point( r.width(), r.height() );
  }

}

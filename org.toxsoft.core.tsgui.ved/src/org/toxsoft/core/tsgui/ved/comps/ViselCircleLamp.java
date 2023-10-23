package org.toxsoft.core.tsgui.ved.comps;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.impl.DataDef.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.bricks.tin.tti.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tsgui.ved.incub.*;
import org.toxsoft.core.tsgui.ved.incub.tsg.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Идикатор в виде круглой лампочки.
 * <p>
 *
 * @author vs
 */
public class ViselCircleLamp
    extends VedAbstractVisel {

  /**
   * The VISEL factory ID.
   */
  public static final String FACTORY_ID = VED_ID + ".visel.CirceleLamp"; //$NON-NLS-1$

  private static final String PROPID_ON_OFF_STATE = "onOffState";  //$NON-NLS-1$
  private static final String PROPID_RADIUS       = "radius";      //$NON-NLS-1$
  private static final String PROPID_BORDER_WIDTH = "borderWidth"; //$NON-NLS-1$
  private static final String PROPID_SHOW_BORDER  = "showBorder";  //$NON-NLS-1$
  private static final String PROPID_USE_GRADIENT = "useGradient"; //$NON-NLS-1$

  /**
   * The VISEL factory singleton.
   */
  public static final IVedViselFactory FACTORY = new VedAbstractViselFactory( FACTORY_ID, //
      TSID_NAME, STR_VISEL_CIRCLE_LAMP, //
      TSID_DESCRIPTION, STR_VISEL_CIRCLE_LAMP_D, //
      TSID_ICON_ID, ICONID_VISEL_CIRCLE_LAMP //
  ) {

    private static final IDataDef PROP_ON_OF_STATE = create3( PROPID_ON_OFF_STATE, DDEF_BOOLEAN, //
        TSID_NAME, STR_N_ON_OFF_STATE, //
        TSID_DESCRIPTION, STR_D_ON_OFF_STATE, //
        TSID_DEFAULT_VALUE, BOOLEAN.defaultValue() //
    );

    private static final IDataDef PROP_RADIUS = create3( PROPID_RADIUS, DDEF_FLOATING, //
        TSID_NAME, STR_N_RADIUS, //
        TSID_DESCRIPTION, STR_D_RADIUS, //
        TSID_DEFAULT_VALUE, FLOATING.defaultValue() //
    );

    private static final IDataDef PROP_BORDER_WIDTH = create3( PROPID_BORDER_WIDTH, DDEF_FLOATING, //
        TSID_NAME, STR_N_BORDER_WIDTH, //
        TSID_DESCRIPTION, STR_D_BORDER_WIDTH, //
        TSID_DEFAULT_VALUE, AvUtils.avFloat( 4 ) //
    );

    private static final IDataDef PROP_SHOW_BORDER = create3( PROPID_SHOW_BORDER, DDEF_BOOLEAN, //
        TSID_NAME, STR_N_SHOW_BORDER, //
        TSID_DESCRIPTION, STR_D_SHOW_BORDER, //
        TSID_DEFAULT_VALUE, AV_TRUE //
    );

    private static final IDataDef PROP_USE_GRADIENT = create3( PROPID_USE_GRADIENT, DDEF_BOOLEAN, //
        TSID_NAME, STR_N_USE_GRADIENT, //
        TSID_DESCRIPTION, STR_D_USE_GRADIENT, //
        TSID_DEFAULT_VALUE, AV_TRUE //
    );

    private static final ITinTypeInfo  TTI_ON_OFF_STATE = new TinAtomicTypeInfo.TtiBoolean( PROP_ON_OF_STATE );
    private static final ITinFieldInfo TFI_ON_OFF_STATE = new TinFieldInfo( PROP_ON_OF_STATE, TTI_ON_OFF_STATE );

    private static final ITinTypeInfo  TTI_RADIUS = new TinAtomicTypeInfo.TtiDouble( PROP_RADIUS );
    private static final ITinFieldInfo TFI_RADIUS = new TinFieldInfo( PROP_RADIUS, TTI_RADIUS );

    private static final ITinTypeInfo  TTI_BORDER_WIDTH = new TinAtomicTypeInfo.TtiDouble( PROP_BORDER_WIDTH );
    private static final ITinFieldInfo TFI_BORDER_WIDTH = new TinFieldInfo( PROP_BORDER_WIDTH, TTI_BORDER_WIDTH );

    private static final ITinTypeInfo  TTI_SHOW_BORDER = new TinAtomicTypeInfo.TtiBoolean( PROP_SHOW_BORDER );
    private static final ITinFieldInfo TFI_SHOW_BORDER = new TinFieldInfo( PROP_SHOW_BORDER, TTI_SHOW_BORDER );

    private static final ITinTypeInfo  TTI_USE_GRADIENT = new TinAtomicTypeInfo.TtiBoolean( PROP_USE_GRADIENT );
    private static final ITinFieldInfo TFI_USE_GRADIENT = new TinFieldInfo( PROP_USE_GRADIENT, TTI_USE_GRADIENT );

    @Override
    protected ITinTypeInfo doCreateTypeInfo() {
      IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();
      fields.add( TFI_IS_ACTIVE );
      fields.add( TFI_NAME );
      fields.add( TFI_DESCRIPTION );
      fields.add( TFI_X );
      fields.add( TFI_Y );
      fields.add( TFI_WIDTH );
      fields.add( TFI_HEIGHT );
      fields.add( TFI_TRANSFORM );
      fields.add( TFI_IS_ASPECT_FIXED );
      fields.add( TFI_ASPECT_RATIO );
      fields.add( TFI_BK_COLOR );
      fields.add( TFI_ON_OFF_STATE );
      fields.add( TFI_RADIUS );
      fields.add( TFI_BORDER_WIDTH );
      fields.add( TFI_SHOW_BORDER );
      fields.add( TFI_USE_GRADIENT );
      return new PropertableEntitiesTinTypeInfo<>( fields, ViselCircleLamp.class );
    }

    @Override
    protected VedAbstractVisel doCreate( IVedItemCfg aCfg, VedScreen aVedScreen ) {
      return new ViselCircleLamp( aCfg, propDefs(), aVedScreen );
    }

  };

  private ITsRectangle swtRect = ITsRectangle.NONE;

  TsFillInfo offGradientInfo;
  TsFillInfo onGradientInfo;
  TsFillInfo borderGradientInfo;
  TsFillInfo flatOnGradientInfo;
  TsFillInfo flatOffGradientInfo;

  /**
   * Constructor.
   *
   * @param aConfig {@link IVedItemCfg} - the item config
   * @param aPropDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - properties definitions
   * @param aVedScreen {@link VedScreen} - the owner screen
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public ViselCircleLamp( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );

    IListEdit<Pair<Double, RGBA>> blackFractions = new ElemArrayList<>();

    blackFractions.add( new Pair<>( Double.valueOf( 0.0 ), new RGBA( 255, 255, 255, 255 ) ) );
    blackFractions.add( new Pair<>( Double.valueOf( 100.0 ), new RGBA( 128, 128, 128, 255 ) ) );

    LinearGradientInfo gi = new LinearGradientInfo( new D2Point( 0, 0 ), new D2Point( 100, 100 ),
        new RGBA( 240, 240, 240, 255 ), new RGBA( 32, 32, 32, 255 ), new D2Point( 1, 1 ) );

    flatOffGradientInfo = new TsFillInfo( new RGBA( 0, 0, 0, 255 ) );
    offGradientInfo = new TsFillInfo( GradientUtils.halfSphereFillInfo( new RGBA( 0, 0, 0, 255 ) ) );
    onGradientInfo = new TsFillInfo( GradientUtils.halfSphereFillInfo( props().getValobj( PROPID_BK_COLOR ) ) );
    borderGradientInfo = new TsFillInfo( new TsGradientFillInfo( gi ) );
  }

  // ------------------------------------------------------------------------------------
  // VedAbstractVisel
  //

  @Override
  public void paint( ITsGraphicsContext aPaintContext ) {
    if( props().getBool( PROPID_USE_GRADIENT ) ) {
      if( props().getBool( PROPID_ON_OFF_STATE ) ) {
        aPaintContext.setFillInfo( onGradientInfo );
      }
      else {
        aPaintContext.setFillInfo( offGradientInfo );
      }
    }
    else {
      if( props().getBool( PROPID_ON_OFF_STATE ) ) {
        aPaintContext.setFillInfo( flatOnGradientInfo );
      }
      else {
        aPaintContext.setFillInfo( flatOffGradientInfo );
      }
    }

    int d = (int)props().getDouble( PROPID_BORDER_WIDTH );
    if( !props().getBool( PROPID_SHOW_BORDER ) ) {
      d = 0;
    }

    aPaintContext.fillOval( swtRect.x1() + d, swtRect.y1() + d, swtRect.width() - 2 * d, swtRect.height() - 2 * d );

    Path p = new Path( aPaintContext.gc().getDevice() );

    p.addArc( 0, 0, swtRect.width(), swtRect.height(), 0, 360 );
    p.addArc( d, d, swtRect.width() - 2 * d, swtRect.height() - 2 * d, 0, 360 );

    aPaintContext.setFillInfo( borderGradientInfo );
    aPaintContext.fillPath( p, swtRect.x1(), swtRect.y1(), swtRect.width(), swtRect.height() );

    p.dispose();
  }

  @Override
  protected void doDoInterceptPropsChange( IOptionSet aNewValues, IOptionSetEdit aValuesToSet ) {
    super.doDoInterceptPropsChange( aNewValues, aValuesToSet );
    // get actual width and height
    double width;
    if( aValuesToSet.hasKey( PROPID_WIDTH ) ) {
      width = aValuesToSet.getDouble( PROPID_WIDTH );
    }
    else {
      width = props().getDouble( PROPID_WIDTH );
    }
    if( width < 8 ) {
      width = 8;
      aValuesToSet.setDouble( PROPID_WIDTH, width );
    }
    double height;
    if( aValuesToSet.hasKey( PROPID_HEIGHT ) ) {
      height = aValuesToSet.getDouble( PROPID_HEIGHT );
    }
    else {
      height = props().getDouble( PROPID_HEIGHT );
    }
    if( height < 8 ) {
      height = 8;
      aValuesToSet.setDouble( PROPID_HEIGHT, height );
    }

    // keep current aspect ratio
    double currAspectRatio = 1.0;
    boolean wasWidthChangeRequested = aValuesToSet.hasKey( PROPID_WIDTH );
    boolean wasHeightChangeRequested = aValuesToSet.hasKey( PROPID_HEIGHT );
    // when changing both width and height, ignore height change resuset
    if( wasWidthChangeRequested && wasHeightChangeRequested ) {
      wasHeightChangeRequested = false;
    }
    if( wasWidthChangeRequested ) {
      width = width / currAspectRatio;
      if( width < 8 ) {
        width = 8;
      }
      aValuesToSet.setDouble( PROPID_HEIGHT, width );
      return;
    }
    if( wasHeightChangeRequested ) {
      aValuesToSet.setDouble( PROPID_WIDTH, height * currAspectRatio );
      return;
    }
  }

  @Override
  protected void doUpdateCachesAfterPropsChange( IOptionSet aChangedValue ) {
    super.doUpdateCachesAfterPropsChange( aChangedValue );
    ID2Rectangle bounds = bounds();
    swtRect = TsGeometryUtils.create( bounds );

    RGBA rgba = props().getByKey( PROPID_BK_COLOR ).asValobj();
    onGradientInfo = new TsFillInfo( GradientUtils.halfSphereFillInfo( rgba ) );
    flatOnGradientInfo = new TsFillInfo( rgba );
  }

}

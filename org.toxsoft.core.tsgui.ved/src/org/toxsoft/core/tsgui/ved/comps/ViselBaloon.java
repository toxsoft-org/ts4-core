package org.toxsoft.core.tsgui.ved.comps;

import static org.toxsoft.core.tsgui.bricks.tin.tti.ITtiConstants.*;
import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.comps.ITsResources.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.impl.DataDef.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.bricks.tin.tti.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.fonts.*;
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
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The VISEL: filled and outlined popup (chat) baloon with text and rounded corners.
 *
 * @author vs
 */
public class ViselBaloon
    extends VedAbstractVisel {

  /**
   * The VISEL factory ID.
   */
  public static final String FACTORY_ID = VED_ID + ".baloonViselFactory"; //$NON-NLS-1$

  static final String PROPID_ARC_WIDTH    = "arcWidth";    //$NON-NLS-1$
  static final String PROPID_ARC_HEIGHT   = "arcHeight";   //$NON-NLS-1$
  static final String PROPID_NOSE_LENGTH  = "noseLength";  //$NON-NLS-1$
  static final String PROPID_NOSE_WIDTH   = "noseWidth";   //$NON-NLS-1$
  static final String PROPID_NOSE_FULCRUM = "noseFulcrum"; //$NON-NLS-1$
  static final String PROPID_USE_GRADIENT = "useGradient"; //$NON-NLS-1$
  static final String PROPID_SHADOW_DEPTH = "shadowDepth"; //$NON-NLS-1$

  static final IDataDef PROP_ARC_WIDTH = DataDef.create3( PROPID_ARC_WIDTH, DDEF_FLOATING, //
      TSID_NAME, STR_VISEL_ARC_WIDTH, //
      TSID_DESCRIPTION, STR_VISEL_ARC_WIDTH_D, //
      TSID_DEFAULT_VALUE, avFloat( 16 ) );

  static final IDataDef PROP_ARC_HEIGHT = DataDef.create3( PROPID_ARC_HEIGHT, DDEF_FLOATING, //
      TSID_NAME, STR_VISEL_ARC_HEIGHT, //
      TSID_DESCRIPTION, STR_VISEL_ARC_HEIGHT_D, //
      TSID_DEFAULT_VALUE, avFloat( 16 ) );

  static final IDataDef PROP_NOSE_LENGTH = DataDef.create3( PROPID_NOSE_LENGTH, DDEF_FLOATING, //
      TSID_NAME, STR_VISEL_NOSE_LENGTH, //
      TSID_DESCRIPTION, STR_VISEL_NOSE_LENGTH_D, //
      TSID_DEFAULT_VALUE, avFloat( 16 ) );

  static final IDataDef PROP_NOSE_WIDTH = DataDef.create3( PROPID_NOSE_WIDTH, DDEF_FLOATING, //
      TSID_NAME, STR_VISEL_NOSE_WIDTH, //
      TSID_DESCRIPTION, STR_VISEL_NOSE_WIDTH_D, //
      TSID_DEFAULT_VALUE, avFloat( 8 ) );

  static final IDataDef PROP_NOSE_FULCRUM = DataDef.create3( PROPID_NOSE_FULCRUM, DT_AV_ENUM, //
      TSID_NAME, STR_NOSE_FULCRUM, //
      TSID_DESCRIPTION, STR_HOR_NOSE_FULCRUM, //
      TSID_KEEPER_ID, ETsFulcrum.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( ETsFulcrum.BOTTOM_CENTER ) //
  );

  static final IDataDef PROP_SHADOW_DEPTH = DataDef.create3( PROPID_SHADOW_DEPTH, DDEF_INTEGER, //
      TSID_NAME, STR_VISEL_SHADOW_DEPTH, //
      TSID_DESCRIPTION, STR_VISEL_SHADOW_DEPTH_D, //
      TSID_DEFAULT_VALUE, avInt( 7 ) );

  private static final IDataDef PROP_USE_GRADIENT = create3( PROPID_USE_GRADIENT, DDEF_BOOLEAN, //
      TSID_NAME, STR_USE_GRADIENT, //
      TSID_DESCRIPTION, STR_USE_GRADIENT_D, //
      TSID_DEFAULT_VALUE, AV_TRUE //
  );

  private static final ITinTypeInfo  TTI_USE_GRADIENT = new TinAtomicTypeInfo.TtiBoolean( PROP_USE_GRADIENT );
  private static final ITinFieldInfo TFI_USE_GRADIENT = new TinFieldInfo( PROP_USE_GRADIENT, TTI_USE_GRADIENT );

  /**
   * The VISEL factory singleton.
   */
  public static final IVedViselFactory FACTORY = new VedAbstractViselFactory( FACTORY_ID, //
      TSID_NAME, "STR_VISEL_BALOON", //
      TSID_DESCRIPTION, "STR_VISEL_BALOON_D", //
      TSID_ICON_ID, ICONID_VISEL_BALOON ) {

    @Override
    protected VedAbstractVisel doCreate( IVedItemCfg aCfg, VedScreen aVedScreen ) {
      return new ViselBaloon( aCfg, propDefs(), aVedScreen );
    }

    @Override
    protected ITinTypeInfo doCreateTypeInfo() {
      IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();
      fields.add( TFI_NAME );
      fields.add( TFI_DESCRIPTION );
      fields.add( TFI_X );
      fields.add( TFI_Y );
      fields.add( TFI_WIDTH );
      fields.add( TFI_HEIGHT );
      fields.add( new TinFieldInfo( PROPID_ARC_WIDTH, TTI_AT_FLOATING, PROP_ARC_WIDTH.params() ) );
      fields.add( new TinFieldInfo( PROPID_ARC_HEIGHT, TTI_AT_FLOATING, PROP_ARC_HEIGHT.params() ) );
      fields.add( new TinFieldInfo( PROPID_NOSE_LENGTH, TTI_AT_FLOATING, PROP_NOSE_LENGTH.params() ) );
      fields.add( new TinFieldInfo( PROPID_NOSE_WIDTH, TTI_AT_FLOATING, PROP_NOSE_WIDTH.params() ) );
      fields.add( new TinFieldInfo( PROP_NOSE_FULCRUM, TtiAvEnum.INSTANCE ) );
      fields.add( TFI_TEXT );
      fields.add( TFI_FONT );
      fields.add( TFI_FG_COLOR );
      fields.add( TFI_BK_COLOR );
      fields.add( TFI_USE_GRADIENT );
      fields.add( TFI_LINE_INFO );
      fields.add( new TinFieldInfo( PROPID_SHADOW_DEPTH, TTI_AT_INTEGER, PROP_SHADOW_DEPTH.params() ) );
      fields.add( TFI_IS_ASPECT_FIXED );
      fields.add( TFI_ASPECT_RATIO );
      fields.add( TFI_TRANSFORM );
      fields.add( TFI_IS_ACTIVE );
      return new PropertableEntitiesTinTypeInfo<>( fields, ViselBaloon.class );
    }

  };

  private Rectangle swtRect = new Rectangle( 0, 0, 1, 1 );

  private Image image = null;

  TsFillInfo gradientInfo     = TsFillInfo.NONE;
  TsFillInfo flatGradientInfo = new TsFillInfo( new RGBA( 255, 255, 255, 255 ) );

  private Font font = null;

  private IStringListEdit texts = new StringArrayList();

  int shadowShift = 5;

  /**
   * Constructor.
   *
   * @param aConfig {@link IVedItemCfg} - the item config
   * @param aPropDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - properties definitions
   * @param aVedScreen {@link VedScreen} - the owner screen
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public ViselBaloon( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
    updatePaths();
  }

  // ------------------------------------------------------------------------------------
  // VedAbstractVisel
  //

  @Override
  public void paint( ITsGraphicsContext aPaintContext ) {
    aPaintContext.gc().setAdvanced( true );
    aPaintContext.gc().setAntialias( SWT.ON );
    aPaintContext.gc().setTextAntialias( SWT.ON );
    aPaintContext.gc().setInterpolation( SWT.HIGH );
    if( image != null ) {
      aPaintContext.gc().drawImage( image, swtRect.x, swtRect.y );
    }
    if( props().getBool( PROPID_USE_GRADIENT ) ) {
      aPaintContext.setFillInfo( gradientInfo );
    }
    else {
      aPaintContext.setFillInfo( flatGradientInfo );
    }
    aPaintContext.fillPath( baloonPath, swtRect.x, swtRect.y, swtRect.width, swtRect.height );
    aPaintContext.setFillInfo( TsFillInfo.NONE );

    IGradient grad = gradientInfo.gradientFillInfo().createGradient( tsContext() );
    Pattern pat = null;
    if( grad != null ) {
      pat = grad.pattern( aPaintContext.gc(), swtRect.width, swtRect.height );
      aPaintContext.gc().setForegroundPattern( pat );
    }
    aPaintContext.drawPath( baloonPath, swtRect.x, swtRect.y );
    if( pat != null ) {
      pat.dispose();
    }

    if( font == null ) {
      IFontInfo fi = props().getValobj( PROPID_FONT );
      font = fontManager().getFont( fi );
    }
    paintTexts( aPaintContext );
  }

  @Override
  protected void doUpdateCachesAfterPropsChange( IOptionSet aChangedValue ) {
    super.doUpdateCachesAfterPropsChange( aChangedValue );
    updatePaths();
    updateSwtRect();
    RGBA rgba = props().getValobj( PROPID_BK_COLOR );
    gradientInfo = new TsFillInfo( GradientUtils.baloonFillInfo( rgba ) );
    flatGradientInfo = new TsFillInfo( rgba );
    if( aChangedValue.hasKey( PROPID_FONT ) ) {
      IFontInfo fi = props().getValobj( PROPID_FONT );
      font = fontManager().getFont( fi );
    }
    if( aChangedValue.hasKey( PROPID_TEXT ) ) {
      texts.clear();
      String str = aChangedValue.getStr( PROPID_TEXT );
      String[] strs = str.split( "/n" ); //$NON-NLS-1$
      texts.addAll( strs );
    }

    if( aChangedValue.hasKey( PROPID_SHADOW_DEPTH ) ) {
      shadowShift = 0;
      if( image != null ) {
        image.dispose();
        image = null;
      }
      shadowShift = aChangedValue.getInt( PROPID_SHADOW_DEPTH );
      updatePaths();
      updateSwtRect();
    }

  }

  @Override
  public VedAbstractVertexSet createVertexSet() {
    return ViselBaloonVertexSet.create( this, vedScreen() );
  }

  // ------------------------------------------------------------------------------------
  // API пакета
  //

  ETsFulcrum noseFulcrum() {
    return props().getValobj( PROPID_NOSE_FULCRUM );
  }

  ITsPoint noseTop() {
    double noseLength = props().getDouble( PROPID_NOSE_LENGTH );
    switch( noseFulcrum() ) {
      case BOTTOM_CENTER:
        return new TsPoint( (int)(rectX() + rectWidth() / 2.), (int)(rectY() + rectHeight() + noseLength) );
      case LEFT_CENTER:
        return new TsPoint( (int)(rectX() - noseLength), (int)(rectY() + rectHeight() / 2.) );
      case RIGHT_CENTER:
        return new TsPoint( (int)(rectX() + rectWidth() + noseLength), (int)(rectY() + rectHeight() / 2.) );
      case TOP_CENTER:
        return new TsPoint( (int)(rectX() + rectWidth() / 2.), (int)(rectY() - noseLength) );
      case LEFT_TOP:
      case RIGHT_BOTTOM:
      case RIGHT_TOP:
      case CENTER:
      case LEFT_BOTTOM:
      default:
        throw new IllegalArgumentException( "Unexpected value: " + noseFulcrum() ); //$NON-NLS-1$
    }
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  Path baloonPath;
  Path shadowPath;
  Path nosePath;

  private int calcTextHeight( GC aGc ) {
    int height = 0;
    aGc.setFont( font );

    if( texts.size() > 0 ) {
      Point p = aGc.textExtent( texts.first() );
      // height = 2 * (texts.size() - 1) + p.y * texts.size();
      height = p.y * texts.size();
    }
    return height;
  }

  private void paintTexts( ITsGraphicsContext aPaintContext ) {
    aPaintContext.gc().setFont( font );
    aPaintContext.setForegroundRgba( props().getValobj( PROPID_FG_COLOR ) );
    double x = rectX();
    double y = rectY();
    double width = rectWidth();
    double height = rectHeight();
    int blockH = calcTextHeight( aPaintContext.gc() );
    int txtY = (int)((height - blockH) / 2.);
    for( String str : texts ) {
      Point p = aPaintContext.gc().textExtent( str );
      int txtX = (int)((width - p.x) / 2.);
      aPaintContext.gc().drawText( str, (int)(x + txtX), (int)(y + txtY), true );
      // txtY += 2 + p.y;
      txtY += p.y;
    }
  }

  private void updateSwtRect() {
    ID2Rectangle r = bounds();
    swtRect.x = (int)Math.round( r.x1() );
    swtRect.y = (int)Math.round( r.y1() );
    swtRect.width = (int)Math.round( r.width() );
    swtRect.height = (int)Math.round( r.height() );
  }

  private void updatePaths() {
    if( image != null && !image.isDisposed() ) {
      image.dispose();
    }
    disposePaths();

    float rw = (float)rectWidth();
    float rh = (float)rectHeight();

    baloonPath = new Path( Display.getDefault() );
    shadowPath = new Path( Display.getDefault() );
    float cx = (float)rectX();
    float cy = (float)rectY();
    float x = (float)rectX();
    float y = (float)rectY();

    cx = cy = x = y = 0;
    float arcW = props().getFloat( PROP_ARC_WIDTH );
    float arcH = props().getFloat( PROP_ARC_HEIGHT );
    float noseWidth = props().getFloat( PROP_NOSE_WIDTH );
    float noseLength = props().getFloat( PROP_NOSE_LENGTH );

    ETsFulcrum fulcrum = props().getValobj( PROP_NOSE_FULCRUM );
    if( fulcrum == ETsFulcrum.TOP_CENTER ) {
      cy = y = noseLength;
    }
    if( fulcrum == ETsFulcrum.LEFT_CENTER ) {
      cx = x = noseLength;
    }

    baloonPath.addArc( cx, cy, arcW, arcH, -180, -90 );
    shadowPath.addArc( cx, cy, arcW, arcH, -180, -90 );
    cx = x + rw - arcW;
    // baloonPath.lineTo( cx, cy );
    // shadowPath.lineTo( cx, cy );
    if( fulcrum == ETsFulcrum.TOP_CENTER ) {
      baloonPath.lineTo( (float)(x + rw / 2. - noseWidth / 2.), y );
      baloonPath.lineTo( (float)(x + rw / 2.), y - noseLength );
      baloonPath.lineTo( (float)(x + rw / 2. + noseWidth / 2.), y );
      baloonPath.lineTo( x + rw - arcW, y );
      shadowPath.lineTo( (float)(x + rw / 2. - noseWidth / 2.), y );
      shadowPath.lineTo( (float)(x + rw / 2.) - shadowShift, y - noseLength );
      shadowPath.lineTo( (float)(x + rw / 2. + noseWidth / 2.) + shadowShift, y );
      shadowPath.lineTo( x + rw - arcW, y );
    }

    baloonPath.addArc( cx, cy, arcW, arcH, 90, -90 );
    shadowPath.addArc( cx, cy, arcW, arcH, 90, -90 );
    if( fulcrum == ETsFulcrum.RIGHT_CENTER ) {
      baloonPath.lineTo( x + rw, (float)(y + rh / 2. - noseWidth / 2.) );
      baloonPath.lineTo( x + rw + noseLength, (float)(y + rh / 2.) );
      baloonPath.lineTo( x + rw, (float)(y + rh / 2. + noseWidth / 2.) );
      baloonPath.lineTo( x + rw, y + rh - arcH );
      shadowPath.lineTo( x + rw, (float)(y + rh / 2. - noseWidth / 2.) );
      shadowPath.lineTo( x + rw + noseLength - shadowShift, (float)(y + rh / 2.) - shadowShift );
      shadowPath.lineTo( x + rw, (float)(y + rh / 2. + noseWidth / 2.) );
      shadowPath.lineTo( x + rw, y + rh - arcH );
    }

    cy = y + rh - arcH;
    baloonPath.addArc( cx, cy, arcW, arcH, 0, -90 );
    shadowPath.addArc( cx, cy, arcW, arcH, 0, -90 );
    if( fulcrum == ETsFulcrum.BOTTOM_CENTER ) {
      baloonPath.lineTo( (float)(x + rw / 2. + noseWidth / 2.), y + rh );
      baloonPath.lineTo( (float)(x + rw / 2.), y + rh + noseLength );
      baloonPath.lineTo( (float)(x + rw / 2. - noseWidth / 2.), y + rh );
      baloonPath.lineTo( x + arcW, y + rh );
      shadowPath.lineTo( (float)(x + rw / 2. + noseWidth / 2.), y + rh );
      shadowPath.lineTo( (float)(x + rw / 2.) - shadowShift, y + rh + noseLength - shadowShift );
      shadowPath.lineTo( (float)(x + rw / 2. - noseWidth / 2.), y + rh );
      shadowPath.lineTo( x + arcW, y + rh );
    }

    cx = x;
    cy = y + rh - arcH;
    baloonPath.addArc( cx, cy, arcW, arcH, -90, -90 );
    shadowPath.addArc( cx, cy, arcW, arcH, -90, -90 );
    if( fulcrum == ETsFulcrum.LEFT_CENTER ) {
      baloonPath.lineTo( x, (float)(y + rh / 2. + noseWidth / 2.) );
      baloonPath.lineTo( x - noseLength, (float)(y + rh / 2.) );
      baloonPath.lineTo( x, (float)(y + rh / 2. - noseWidth / 2.) );
      baloonPath.lineTo( x, y + arcH );
      shadowPath.lineTo( x, (float)(y + rh / 2. + noseWidth / 2.) );
      shadowPath.lineTo( x - noseLength, (float)(y + rh / 2.) );
      shadowPath.lineTo( x, (float)(y + rh / 2. - noseWidth / 2.) );
      shadowPath.lineTo( x, y + arcH );
    }

    baloonPath.close();
    shadowPath.close();

    nosePath = new Path( getDisplay() );

    if( shadowShift > 0 ) {
      createShadowImage( shadowShift );
    }
  }

  public double rectX() {
    float noseLength = props().getFloat( PROP_NOSE_LENGTH );
    ID2Rectangle r = bounds();
    switch( (ETsFulcrum)props().getValobj( PROP_NOSE_FULCRUM ) ) {
      case LEFT_CENTER:
        return (int)(r.x1() + noseLength);
      case RIGHT_CENTER:
      case TOP_CENTER:
      case BOTTOM_CENTER:
        return r.x1();

      case CENTER:
      case LEFT_BOTTOM:
      case LEFT_TOP:
      case RIGHT_BOTTOM:
      case RIGHT_TOP:
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  public double rectY() {
    float noseLength = props().getFloat( PROP_NOSE_LENGTH );
    ID2Rectangle r = bounds();
    switch( (ETsFulcrum)props().getValobj( PROP_NOSE_FULCRUM ) ) {
      case TOP_CENTER:
        return (int)(r.y1() + noseLength);
      case LEFT_CENTER:
      case RIGHT_CENTER:
      case BOTTOM_CENTER:
        return r.y1();

      case CENTER:
      case LEFT_BOTTOM:
      case LEFT_TOP:
      case RIGHT_BOTTOM:
      case RIGHT_TOP:
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  public double rectWidth() {
    float noseLength = props().getFloat( PROP_NOSE_LENGTH );
    ID2Rectangle r = bounds();
    switch( (ETsFulcrum)props().getValobj( PROP_NOSE_FULCRUM ) ) {
      case LEFT_CENTER:
        return (int)(r.width() - noseLength) - shadowShift;
      case RIGHT_CENTER:
        return (int)(r.width() - noseLength) + shadowShift;
      case TOP_CENTER:
      case BOTTOM_CENTER:
        return r.width() - shadowShift;

      case CENTER:
      case LEFT_BOTTOM:
      case LEFT_TOP:
      case RIGHT_BOTTOM:
      case RIGHT_TOP:
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  public double rectHeight() {
    float noseLength = props().getFloat( PROP_NOSE_LENGTH );
    ID2Rectangle r = bounds();
    switch( (ETsFulcrum)props().getValobj( PROP_NOSE_FULCRUM ) ) {
      case LEFT_CENTER:
      case RIGHT_CENTER:
        return r.height() - shadowShift;
      case TOP_CENTER:
        return (int)(r.height() - noseLength) - shadowShift;
      case BOTTOM_CENTER:
        return (int)(r.height() - noseLength);

      case CENTER:
      case LEFT_BOTTOM:
      case LEFT_TOP:
      case RIGHT_BOTTOM:
      case RIGHT_TOP:
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  private void disposePaths() {
    if( baloonPath != null ) {
      baloonPath.dispose();
    }
    if( nosePath != null ) {
      nosePath.dispose();
    }
    if( shadowPath != null ) {
      shadowPath.dispose();
    }
  }

  private void createShadowImage( int aDepth ) {

    if( swtRect.width < 2 ) {
      return;
    }

    int alpha = 220;
    image = ShadowUtils.createShadowImage( shadowPath, aDepth, alpha, 1.0, 1.0, 3 );
  }

}

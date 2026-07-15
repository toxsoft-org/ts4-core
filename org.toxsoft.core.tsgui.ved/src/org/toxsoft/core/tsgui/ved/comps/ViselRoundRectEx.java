package org.toxsoft.core.tsgui.ved.comps;

import static org.toxsoft.core.tsgui.bricks.tin.tti.ITtiConstants.*;
import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.comps.ITsResources.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.bricks.tin.tti.*;
import org.toxsoft.core.tsgui.graphics.gc.*;
import org.toxsoft.core.tsgui.graphics.path.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tsgui.graphics.shadow.*;
import org.toxsoft.core.tsgui.ved.incub.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The VISEL: extended (with border and shadow) filled and outlined rectangle with rounded corners.
 *
 * @author vs
 */
public class ViselRoundRectEx
    extends VedAbstractVisel {

  /**
   * The VISEL factory ID.
   */
  public static final String FACTORY_ID = VED_ID + ".roundRectViselExFactory"; //$NON-NLS-1$

  /**
   * Field ID for arc width
   */
  public static final String PROPID_ARC_WIDTH = "arcWidth"; //$NON-NLS-1$

  /**
   * Field ID for arc height
   */
  public static final String PROPID_ARC_HEIGHT = "arcHeight"; //$NON-NLS-1$

  static final String PROPID_DROP_SHADOW  = "dropShadow";  //$NON-NLS-1$
  static final String PROPID_BORDER_THICK = "borderThick"; //$NON-NLS-1$
  static final String PROPID_BORDER_FILL  = "borderFill";  //$NON-NLS-1$

  static final ITinFieldInfo TFI_DROP_SHADOW = TtiUtils.booleanFieldInfo( PROPID_DROP_SHADOW, //
      "Отбрасываемая тень", "Признак того, что тень является отбрасываемой, а не внутренней", true );

  static final ITinFieldInfo TFI_BORDER_THICK = TtiUtils.doubleFieldInfo( PROPID_BORDER_THICK, //
      "Толщина границы", "Толщина границы - 0 для отсутствия", 1.0 );

  static final ITinFieldInfo TFI_BORDER_FILL = TtiUtils.typedFieldInfo( PROPID_BORDER_FILL, TtiTsFillInfo.INSTANCE, //
      "Заливка границы", "Параметры заливки границы" );

  /**
   * Arc width
   */
  public static final IDataDef PROP_ARC_WIDTH = DataDef.create3( PROPID_ARC_WIDTH, DT_FLOATING, //
      TSID_NAME, STR_VISEL_ARC_WIDTH, //
      TSID_DESCRIPTION, STR_VISEL_ARC_WIDTH_D, //
      TSID_DEFAULT_VALUE, avFloat( 16 ) );

  /**
   * Arc height
   */
  public static final IDataDef PROP_ARC_HEIGHT = DataDef.create3( PROPID_ARC_HEIGHT, DT_FLOATING, //
      TSID_NAME, STR_VISEL_ARC_HEIGHT, //
      TSID_DESCRIPTION, STR_VISEL_ARC_HEIGHT_D, //
      TSID_DEFAULT_VALUE, avFloat( 16 ) );

  /**
   * The VISEL factory singleton.
   */
  public static final IVedViselFactory FACTORY = new VedAbstractViselFactory( FACTORY_ID, //
      TSID_NAME, STR_VISEL_ROUND_RECT, //
      TSID_DESCRIPTION, STR_VISEL_ROUND_RECT_D, //
      TSID_ICON_ID, ICONID_VISEL_ROUND_RECT_EX ) {

    @Override
    protected VedAbstractVisel doCreate( IVedItemCfg aCfg, VedScreen aScreen ) {
      return new ViselRoundRectEx( aCfg, propDefs(), aScreen );
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
      fields.add( TFI_FULCRUM );
      fields.add( new TinFieldInfo( PROPID_ARC_WIDTH, TTI_AT_FLOATING, PROP_ARC_WIDTH.params() ) );
      fields.add( new TinFieldInfo( PROPID_ARC_HEIGHT, TTI_AT_FLOATING, PROP_ARC_HEIGHT.params() ) );
      fields.add( TFI_BK_FILL );
      // fields.add( TFI_FG_COLOR );
      fields.add( TFI_LINE_INFO );
      fields.add( TFI_SHADOW_INFO );
      fields.add( TFI_DROP_SHADOW );
      fields.add( TFI_BORDER_THICK );
      fields.add( TFI_BORDER_FILL );

      fields.add( TFI_IS_ASPECT_FIXED );
      fields.add( TFI_ASPECT_RATIO );
      // fields.add( TFI_TRANSFORM );
      fields.add( TFI_ZOOM );
      fields.add( TFI_ANGLE );
      fields.add( TinFieldInfo.makeCopy( TFI_TRANSFORM, ITinWidgetConstants.PRMID_IS_HIDDEN, AV_TRUE ) );
      fields.add( TFI_IS_ACTIVE );
      return new PropertableEntitiesTinTypeInfo<>( fields, ViselRoundRectEx.class );
    }

  };

  private Rectangle swtRect = new Rectangle( 0, 0, 1, 1 );

  // private Color fgColor;

  private TsShadowInfo shadowInfo = TsShadowInfo.NONE;

  private boolean isDropShadow = true;

  private double     borderThick = 1.0;
  private TsFillInfo borderFill  = new TsFillInfo( new RGBA( 0, 0, 0, 255 ) );
  private Path       borderPath  = null;

  /**
   * Constructor.
   *
   * @param aConfig {@link IVedItemCfg} - the item config
   * @param aPropDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - properties definitions
   * @param aVedScreen {@link VedScreen} - the owner screen
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public ViselRoundRectEx( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
    setLocation( aConfig.propValues().getDouble( PROP_X.id() ), aConfig.propValues().getDouble( PROP_Y.id() ) );
    setSize( aConfig.propValues().getDouble( PROP_WIDTH.id() ), aConfig.propValues().getDouble( PROP_HEIGHT.id() ) );
    updateSwtRect();
    addInterceptor( VedViselInterceptorAspectRatio.INSTANCE );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private void updateSwtRect() {
    ID2Rectangle r = bounds();
    swtRect.x = (int)Math.round( r.x1() );
    swtRect.y = (int)Math.round( r.y1() );
    swtRect.width = (int)Math.round( r.width() );
    swtRect.height = (int)Math.round( r.height() );
    if( borderPath != null ) {
      borderPath.dispose();
      borderPath = null;
    }
    if( borderThick > 0 ) {
      int arcW = (int)props().getDouble( PROPID_ARC_WIDTH );
      int arcH = (int)props().getDouble( PROPID_ARC_HEIGHT );
      borderPath = PathDataUtils.createRoundedBorderPath( getDisplay(), //
          0, 0, swtRect.width, swtRect.height, arcW, arcH, (float)borderThick );
    }
  }

  // ------------------------------------------------------------------------------------
  // VedAbstractVisel
  //

  @Override
  public void paint( ITsGraphicsContext aPaintContext ) {

    int arcW = (int)props().getDouble( PROPID_ARC_WIDTH );
    int arcH = (int)props().getDouble( PROPID_ARC_HEIGHT );

    Device d = aPaintContext.getDisplay();

    if( shadowInfo != TsShadowInfo.NONE && isDropShadow ) {
      Path boundPath = new Path( d );
      PathDataUtils.addRoundedRect( boundPath, 0, 0, swtRect.width, swtRect.height, arcW, arcH );
      ImageData imd = TsShadowUtils.buildDropShadowData( boundPath, shadowInfo, aPaintContext.gc().getDevice() );
      float[] bounds = new float[4];
      boundPath.getBounds( bounds );
      Image img = new Image( aPaintContext.gc().getDevice(), imd );
      int shadowX = Math.round( bounds[0] - shadowInfo.blur() + shadowInfo.xOffset() );
      int shadowY = Math.round( bounds[1] - shadowInfo.blur() + shadowInfo.xOffset() );
      aPaintContext.gc().drawImage( img, shadowX, shadowY );
      img.dispose();
      boundPath.dispose();
    }
    aPaintContext.setFillInfo( props().getValobj( PROPID_BK_FILL ) );
    aPaintContext.fillRoundRect( 0, 0, swtRect.width, swtRect.height, arcW, arcH );

    if( shadowInfo != TsShadowInfo.NONE && !isDropShadow ) {
      Path innerPath = new Path( d );
      float bw = (float)borderThick;
      PathDataUtils.addRoundedRect( innerPath, bw - 1, bw - 1, swtRect.width - 2 * bw + 2, swtRect.height - 2 * bw + 2,
          arcW - bw, arcH - bw );
      ImageData imd = TsShadowUtils.buildInnerShadowData( innerPath, shadowInfo, aPaintContext.gc().getDevice() );

      float[] bounds = new float[4];
      innerPath.getBounds( bounds );
      Image img = new Image( aPaintContext.gc().getDevice(), imd );
      int shadowX = Math.round( bounds[0] + shadowInfo.xOffset() - shadowInfo.blur() );
      int shadowY = Math.round( bounds[1] + shadowInfo.xOffset() - shadowInfo.blur() );
      aPaintContext.gc().drawImage( img, shadowX, shadowY );
      // aPaintContext.gc().drawImage( img, 0, 0 );
      img.dispose();
      innerPath.dispose();
    }

    if( borderPath != null ) {
      aPaintContext.setFillInfo( borderFill );
      aPaintContext.fillPath( borderPath, 0, 0, swtRect.width, swtRect.height );
    }

    aPaintContext.setFillInfo( TsFillInfo.NONE );
  }

  @Override
  protected void doUpdateCachesAfterPropsChange( IOptionSet aChangedValue ) {
    super.doUpdateCachesAfterPropsChange( aChangedValue );
    if( aChangedValue.hasKey( PROPID_SHADOW_INFO ) ) {
      shadowInfo = aChangedValue.getValobj( PROPID_SHADOW_INFO );
    }
    if( aChangedValue.hasKey( PROPID_DROP_SHADOW ) ) {
      isDropShadow = aChangedValue.getBool( PROPID_DROP_SHADOW );
    }
    if( aChangedValue.hasKey( PROPID_BORDER_THICK ) ) {
      borderThick = aChangedValue.getDouble( PROPID_BORDER_THICK );
    }
    if( aChangedValue.hasKey( PROPID_BORDER_FILL ) ) {
      borderFill = aChangedValue.getValobj( PROPID_BORDER_FILL );
    }
    updateSwtRect();
  }

  @Override
  public VedAbstractVertexSet createVertexSet() {
    return ViselRoundRectVertexSetEx.create( this, vedScreen() );
  }

  @Override
  protected void doDispose() {
    super.doDispose();
    if( borderPath != null ) {
      borderPath.dispose();
    }

  }

}

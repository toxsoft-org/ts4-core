package org.toxsoft.core.tsgui.ved.comps;

import static org.toxsoft.core.tsgui.bricks.tin.tti.ITtiConstants.*;
import static org.toxsoft.core.tsgui.graphics.ITsGraphicsConstants.*;
import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.bricks.tin.tti.*;
import org.toxsoft.core.tsgui.ved.incub.tsg.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;

/**
 * Визуальный элемент - "Прямоугольник".
 * <p>
 *
 * @author vs
 */
public class ViselRoundRect
    extends VedAbstractVisel {

  /**
   * ИД фабрики содания скругленных прямоугольников
   */
  public static final String FACTORY_ID = VED_ID + ".roundRectViselFactory"; //$NON-NLS-1$

  static final String FID_LINE_INFO  = "lineInfo";  //$NON-NLS-1$
  static final String FID_ARC_WIDTH  = "arcWidth";  //$NON-NLS-1$
  static final String FID_ARC_HEIGHT = "arcHeight"; //$NON-NLS-1$

  static final IDataDef DDEF_ARC_WIDTH = DataDef.create3( "visel.rect.arcWidth", DDEF_FLOATING, //$NON-NLS-1$
      TSID_NAME, "ширина скругления", //
      TSID_DESCRIPTION, "ширина скругления прямоугольника", //
      TSID_DEFAULT_VALUE, avFloat( 16 ) );

  static final IDataDef DDEF_ARC_HEIGHT = DataDef.create3( "visel.rect.arcHeight", DDEF_FLOATING, //$NON-NLS-1$
      TSID_NAME, "высота скругления", //
      TSID_DESCRIPTION, "высота скругления прямоугольника", //
      TSID_DEFAULT_VALUE, avFloat( 16 ) );

  static final IDataDef DDEF_LINE_INFO = DataDef.create3( "visel.rect.lineInfo", DT_TS_LINE_INFO ); //$NON-NLS-1$

  /**
   * The VISEL factory singleton.
   */
  public static final IVedViselFactory FACTORY = new VedAbstractViselFactory( FACTORY_ID, //
      TSID_NAME, STR_N_RECT_FACTORY, //
      TSID_DESCRIPTION, "Скругленный прямоугольник", //
      TSID_ICON_ID, ICONID_VISEL_ROUND_RECT ) {

    @Override
    protected VedAbstractVisel doCreate( IVedItemCfg aCfg, VedScreen aScreen ) {
      return new ViselRoundRect( aCfg, propDefs(), aScreen );
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
      fields.add( new TinFieldInfo( FID_ARC_WIDTH, TTI_AT_FLOATING, DDEF_ARC_WIDTH.params() ) );
      fields.add( new TinFieldInfo( FID_ARC_HEIGHT, TTI_AT_FLOATING, DDEF_ARC_HEIGHT.params() ) );
      fields.add( TFI_BK_FILL );
      fields.add( new TinFieldInfo( FID_LINE_INFO, TtiTsLineInfo.INSTANCE, DDEF_LINE_INFO.params() ) );
      fields.add( TFI_IS_ASPECT_FIXED );
      fields.add( TFI_ASPECT_RATIO );
      fields.add( TFI_TRANSFORM );
      fields.add( TFI_IS_ACTIVE );
      return new PropertableEntitiesTinTypeInfo<>( fields, ViselRoundRect.class );
    }

  };

  private Rectangle swtRect = new Rectangle( 0, 0, 1, 1 );

  /**
   * Constructor.<br>
   *
   * @param aConfig {@link IVedItemCfg} - configuration data of the individual visel
   * @param aPropDefs IStridablesList&lt;IDataDef> - список описаний данных свойств визеля
   * @param aVedScreen {@link VedScreen} - экран
   */
  public ViselRoundRect( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
    setLocation( aConfig.propValues().getDouble( PROP_X.id() ), aConfig.propValues().getDouble( PROP_Y.id() ) );
    setSize( aConfig.propValues().getDouble( PROP_WIDTH.id() ), aConfig.propValues().getDouble( PROP_HEIGHT.id() ) );
    updateSwtRect();
  }

  // ------------------------------------------------------------------------------------
  // VedAbstractVisel
  //

  @Override
  public void paint( ITsGraphicsContext aPaintContext ) {
    int arcW = (int)props().getDouble( FID_ARC_WIDTH );
    int arcH = (int)props().getDouble( FID_ARC_HEIGHT );
    aPaintContext.setFillInfo( props().getValobj( PROPID_BK_FILL ) );
    aPaintContext.fillRoundRect( swtRect.x, swtRect.y, swtRect.width, swtRect.height, arcW, arcH );
    aPaintContext.setLineInfo( props().getValobj( FID_LINE_INFO ) );
    aPaintContext.drawRoundRect( swtRect.x, swtRect.y, swtRect.width, swtRect.height, arcW, arcH );
  }

  @Override
  protected void doUpdateCachesAfterPropsChange( IOptionSet aChangedValue ) {
    super.doUpdateCachesAfterPropsChange( aChangedValue );
    updateSwtRect();
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

    // update PROP_ASPECT_RATIO for non-fixed aspect
    if( !props().getBool( PROP_IS_ASPECT_FIXED ) ) {
      double aspectRatio = width / height;
      aValuesToSet.setDouble( PROPID_ASPECT_RATIO, aspectRatio );
      return;
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
  public VedAbstractVertexSet createVertexSet() {
    return ViselRoundRectVertexSet.create( this, vedScreen() );
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
  }

}

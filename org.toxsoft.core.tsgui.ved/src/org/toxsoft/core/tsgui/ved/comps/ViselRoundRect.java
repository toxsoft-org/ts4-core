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
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The VISEL: filled and outlined rectangle with rounded corners.
 *
 * @author vs
 */
public class ViselRoundRect
    extends VedAbstractVisel {

  /**
   * The VISEL factory ID.
   */
  public static final String FACTORY_ID = VED_ID + ".roundRectViselFactory"; //$NON-NLS-1$

  static final String PROPID_ARC_WIDTH  = "arcWidth";  //$NON-NLS-1$
  static final String PROPID_ARC_HEIGHT = "arcHeight"; //$NON-NLS-1$

  static final IDataDef PROP_ARC_WIDTH = DataDef.create3( PROPID_ARC_WIDTH, DDEF_FLOATING, //
      TSID_NAME, STR_VISEL_ARC_WIDTH, //
      TSID_DESCRIPTION, STR_VISEL_ARC_WIDTH_D, //
      TSID_DEFAULT_VALUE, avFloat( 16 ) );

  static final IDataDef PROP_ARC_HEIGHT = DataDef.create3( PROPID_ARC_HEIGHT, DDEF_FLOATING, //
      TSID_NAME, STR_VISEL_ARC_HEIGHT, //
      TSID_DESCRIPTION, STR_VISEL_ARC_HEIGHT_D, //
      TSID_DEFAULT_VALUE, avFloat( 16 ) );

  /**
   * The VISEL factory singleton.
   */
  public static final IVedViselFactory FACTORY = new VedAbstractViselFactory( FACTORY_ID, //
      TSID_NAME, STR_VISEL_ROUND_RECT, //
      TSID_DESCRIPTION, STR_VISEL_ROUND_RECT_D, //
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
      fields.add( new TinFieldInfo( PROPID_ARC_WIDTH, TTI_AT_FLOATING, PROP_ARC_WIDTH.params() ) );
      fields.add( new TinFieldInfo( PROPID_ARC_HEIGHT, TTI_AT_FLOATING, PROP_ARC_HEIGHT.params() ) );
      fields.add( TFI_BK_FILL );
      fields.add( TFI_LINE_INFO );
      fields.add( TFI_IS_ASPECT_FIXED );
      fields.add( TFI_ASPECT_RATIO );
      fields.add( TFI_TRANSFORM );
      fields.add( TFI_IS_ACTIVE );
      return new PropertableEntitiesTinTypeInfo<>( fields, ViselRoundRect.class );
    }

  };

  private Rectangle swtRect = new Rectangle( 0, 0, 1, 1 );

  /**
   * Constructor.
   *
   * @param aConfig {@link IVedItemCfg} - the item config
   * @param aPropDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - properties definitions
   * @param aVedScreen {@link VedScreen} - the owner screen
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public ViselRoundRect( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
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
  }

  // ------------------------------------------------------------------------------------
  // VedAbstractVisel
  //

  @Override
  public void paint( ITsGraphicsContext aPaintContext ) {
    int arcW = (int)props().getDouble( PROPID_ARC_WIDTH );
    int arcH = (int)props().getDouble( PROPID_ARC_HEIGHT );
    aPaintContext.setFillInfo( props().getValobj( PROPID_BK_FILL ) );
    aPaintContext.fillRoundRect( swtRect.x, swtRect.y, swtRect.width, swtRect.height, arcW, arcH );
    aPaintContext.setLineInfo( props().getValobj( PROPID_LINE_INFO ) );
    aPaintContext.drawRoundRect( swtRect.x, swtRect.y, swtRect.width, swtRect.height, arcW, arcH );
  }

  @Override
  protected void doUpdateCachesAfterPropsChange( IOptionSet aChangedValue ) {
    super.doUpdateCachesAfterPropsChange( aChangedValue );
    updateSwtRect();
  }

  @Override
  public VedAbstractVertexSet createVertexSet() {
    return ViselRoundRectVertexSet.create( this, vedScreen() );
  }

}

package org.toxsoft.core.tsgui.ved.comps;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.comps.ITsResources.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.graphics.gc.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The VISEL: filled and outlined ellipse.
 *
 * @author vs
 */
public class ViselEllipse
    extends VedAbstractVisel {

  /**
   * The VISEL factory ID.
   */
  public static final String FACTORY_ID = VED_ID + ".ellipseViselFactory"; //$NON-NLS-1$

  /**
   * The VISEL factory singleton.
   */
  public static final IVedViselFactory FACTORY = new VedAbstractViselFactory( FACTORY_ID, //
      TSID_NAME, STR_VISEL_ELLIPSE, //
      TSID_DESCRIPTION, STR_VISEL_ELLIPSE_D, //
      TSID_ICON_ID, ICONID_VISEL_ELLIPSE ) {

    @Override
    protected VedAbstractVisel doCreate( IVedItemCfg aCfg, VedScreen aScreen ) {
      return new ViselEllipse( aCfg, propDefs(), aScreen );
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
      fields.add( TFI_BK_FILL );
      fields.add( TFI_FG_COLOR );
      fields.add( TFI_LINE_INFO );
      fields.add( TFI_IS_ASPECT_FIXED );
      fields.add( TFI_ASPECT_RATIO );
      fields.add( TFI_ZOOM );
      fields.add( TFI_ANGLE );
      fields.add( TinFieldInfo.makeCopy( TFI_TRANSFORM, ITinWidgetConstants.PRMID_IS_HIDDEN, AV_TRUE ) );
      fields.add( TinFieldInfo.makeCopy( TFI_IS_ACTIVE, ITinWidgetConstants.PRMID_IS_HIDDEN, AV_TRUE ) );
      return new PropertableEntitiesTinTypeInfo<>( fields, ViselEllipse.class );
    }

  };

  private Rectangle swtRect = new Rectangle( 0, 0, 1, 1 );

  private Color fgColor;

  /**
   * Constructor.
   *
   * @param aConfig {@link IVedItemCfg} - the item config
   * @param aPropDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - properties definitions
   * @param aVedScreen {@link VedScreen} - the owner screen
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public ViselEllipse( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
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
    aPaintContext.setFillInfo( props().getValobj( PROPID_BK_FILL ) );
    aPaintContext.fillOval( 0, 0, swtRect.width, swtRect.height );
    aPaintContext.setFillInfo( TsFillInfo.NONE );
    TsLineInfo lineInfo = props().getValobj( PROPID_LINE_INFO );
    aPaintContext.setLineInfo( lineInfo );
    aPaintContext.gc().setForeground( fgColor );
    int alpha = aPaintContext.gc().getAlpha();
    aPaintContext.gc().setAlpha( fgColor.getAlpha() );
    aPaintContext.drawOval( 0, 0, swtRect.width, swtRect.height );
    aPaintContext.gc().setAlpha( alpha );
  }

  @Override
  protected void doUpdateCachesAfterPropsChange( IOptionSet aChangedValue ) {
    super.doUpdateCachesAfterPropsChange( aChangedValue );
    RGBA fgRgba = props().getValobj( PROPID_FG_COLOR );
    fgColor = colorManager().getColor( fgRgba );
    updateSwtRect();
  }

  // @Override
  // public VedAbstractVertexSet createVertexSet() {
  // return ViselRoundRectVertexSet.create( this, vedScreen() );
  // }

}

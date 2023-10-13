package org.toxsoft.core.tsgui.ved.comps;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.comps.ITsResources.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.ved.incub.tsg.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The VISEL: filled and bordered rectangle.
 *
 * @author vs
 */
public class ViselRectangle
    extends VedAbstractVisel {

  /**
   * The VISEL factor ID.
   */
  public static final String FACTORY_ID = VED_ID + ".visel.Rectangle"; //$NON-NLS-1$

  /**
   * The VISEL factory singleton.
   */
  public static final IVedViselFactory FACTORY = new VedAbstractViselFactory( FACTORY_ID, //
      TSID_NAME, STR_VISEL_RECTANGLE, //
      TSID_DESCRIPTION, STR_VISEL_RECTANGLE_D, //
      TSID_ICON_ID, ICONID_SIMPLE_RECT //
  ) {

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
      fields.add( TFI_BK_FILL );
      fields.add( TFI_BORDER_INFO );
      fields.add( TFI_IS_ASPECT_FIXED );
      fields.add( TFI_ASPECT_RATIO );
      return new PropertableEntitiesTinTypeInfo<>( fields, ViselRectangle.class );
    }

    @Override
    protected VedAbstractVisel doCreate( IVedItemCfg aCfg, VedScreen aVedScreen ) {
      return new ViselRectangle( aCfg, propDefs(), aVedScreen );
    }

  };

  private ITsRectangle swtRect = ITsRectangle.NONE;

  /**
   * Constructor.
   *
   * @param aConfig {@link IVedItemCfg} - the item config
   * @param aPropDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - properties definitions
   * @param aVedScreen {@link VedScreen} - the owner screen
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public ViselRectangle( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
  }

  // ------------------------------------------------------------------------------------
  // VedAbstractVisel
  //

  @Override
  public void paint( ITsGraphicsContext aPaintContext ) {
    aPaintContext.setFillInfo( props().getValobj( PROPID_BK_FILL ) );
    aPaintContext.fillRect( swtRect );
    aPaintContext.setBorderInfo( props().getValobj( PROPID_BORDER_INFO ) );
    aPaintContext.drawRectBorder( swtRect );
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
    double height;
    if( aValuesToSet.hasKey( PROPID_HEIGHT ) ) {
      height = aValuesToSet.getDouble( PROPID_HEIGHT );
    }
    else {
      height = props().getDouble( PROPID_HEIGHT );
    }
    // update PROP_ASPECT_RATIO for non-fixed aspect
    if( !props().getBool( PROP_IS_ASPECT_FIXED ) ) {
      double aspectRatio = width / height;
      aValuesToSet.setDouble( PROPID_ASPECT_RATIO, aspectRatio );
      return;
    }
    // keep current aspect ratio
    double currAspectRatio = props().getDouble( PROPID_WIDTH ) / props().getDouble( PROPID_HEIGHT );
    boolean wasWidthChangeRequested = aValuesToSet.hasKey( PROPID_WIDTH );
    boolean wasHeightChangeRequested = aValuesToSet.hasKey( PROPID_HEIGHT );
    // when changing both width and height, ignore height change resuset
    if( wasWidthChangeRequested && wasHeightChangeRequested ) {
      wasHeightChangeRequested = false;
    }
    if( wasWidthChangeRequested ) {
      aValuesToSet.setDouble( PROPID_HEIGHT, width / currAspectRatio );
      return;
    }
    if( wasHeightChangeRequested ) {
      aValuesToSet.setDouble( PROPID_HEIGHT, height * currAspectRatio );
      return;
    }
  }

  @Override
  protected void doUpdateCachesAfterPropsChange( IOptionSet aChangedValue ) {
    super.doUpdateCachesAfterPropsChange( aChangedValue );
    ID2Rectangle bounds = bounds();
    swtRect = TsGeometryUtils.create( bounds );
  }

}

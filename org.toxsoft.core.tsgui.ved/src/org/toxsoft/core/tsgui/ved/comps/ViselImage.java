package org.toxsoft.core.tsgui.ved.comps;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.comps.ITsResources.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.graphics.gc.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.graphics.image.impl.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The VISEL: image.
 * <p>
 *
 * @author vs
 */
public class ViselImage
    extends VedAbstractVisel {

  /**
   * The VISEL factory ID.
   */
  public static final String FACTORY_ID = VED_ID + ".visel.Image"; //$NON-NLS-1$

  /**
   * The VISEL factory singleton.
   */
  public static final IVedViselFactory FACTORY = new VedAbstractViselFactory( FACTORY_ID, //
      TSID_NAME, STR_VISEL_IMAGE_DESCRIPTOR, //
      TSID_DESCRIPTION, STR_VISEL_IMAGE_DESCRIPTOR_D, //
      TSID_ICON_ID, ICONID_VISEL_IMAGE //
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
      fields.add( TFI_FULCRUM );
      fields.add( TinFieldInfo.makeCopy( TFI_IS_ASPECT_FIXED, //
          ITinWidgetConstants.PRMID_IS_HIDDEN, AV_TRUE, //
          TSID_DEFAULT_VALUE, AV_TRUE //
      ) );
      fields.add( TinFieldInfo.makeCopy( TFI_ASPECT_RATIO, ITinWidgetConstants.PRMID_IS_HIDDEN, AV_TRUE ) );
      fields.add( TFI_IMAGE_DESCRIPTOR );
      // fields.add( TFI_TRANSFORM );
      fields.add( TFI_ZOOM );
      fields.add( TFI_ANGLE );
      fields.add( TinFieldInfo.makeCopy( TFI_TRANSFORM, ITinWidgetConstants.PRMID_IS_HIDDEN, AV_TRUE ) );
      return new PropertableEntitiesTinTypeInfo<>( fields, ViselImage.class );
    }

    @Override
    protected VedAbstractVisel doCreate( IVedItemCfg aCfg, VedScreen aVedScreen ) {
      return new ViselImage( aCfg, propDefs(), aVedScreen );
    }

  };

  private TsImage image = null;

  /**
   * Constructor.
   *
   * @param aConfig {@link IVedItemCfg} - the item config
   * @param aPropDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - properties definitions
   * @param aVedScreen {@link VedScreen} - the owner screen
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public ViselImage( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
    addInterceptor( VedViselInterceptorAspectRatio.INSTANCE );
  }

  // ------------------------------------------------------------------------------------
  // VedAbstractVisel
  //

  @Override
  public void paint( ITsGraphicsContext aPaintContext ) {
    if( image != null ) {
      aPaintContext.gc().drawImage( image.image(), //
          0, 0, image.imageSize().x(), image.imageSize().y(), //
          0, 0, (int)bounds().width(), (int)bounds().height() //
      );
    }
  }

  @Override
  protected void doDoInterceptPropsChange( IOptionSet aNewValues, IOptionSetEdit aValuesToSet ) {
    super.doDoInterceptPropsChange( aNewValues, aValuesToSet );
    if( aValuesToSet.hasKey( PROPID_IMAGE_DESCRIPTOR ) ) {
      TsImageDescriptor imd = aValuesToSet.getValobj( PROPID_IMAGE_DESCRIPTOR );
      TsImage tmpImg = imageManager().getImage( imd );
      ITsPoint p = tmpImg.imageSize();
      aValuesToSet.setDouble( PROPID_WIDTH, p.x() );
      aValuesToSet.setDouble( PROPID_HEIGHT, p.y() );
    }
  }

  @Override
  protected void doUpdateCachesAfterPropsChange( IOptionSet aChangedValue ) {
    super.doUpdateCachesAfterPropsChange( aChangedValue );
    TsImageDescriptor imd = props().getValobj( PROPID_IMAGE_DESCRIPTOR );
    image = imageManager().getImage( imd );
  }

  @Override
  public boolean isYours( double aX, double aY ) {
    return super.isYours( aX, aY );
  }

}

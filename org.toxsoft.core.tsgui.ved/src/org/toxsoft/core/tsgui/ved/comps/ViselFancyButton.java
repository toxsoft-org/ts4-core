package org.toxsoft.core.tsgui.ved.comps;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.comps.ITsResources.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.graphics.gc.*;
import org.toxsoft.core.tsgui.ved.comps.render.*;
import org.toxsoft.core.tsgui.ved.incub.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

public class ViselFancyButton
    extends VedAbstractVisel {

  /**
   * The VISEL factor ID.
   */
  public static final String FACTORY_ID = VED_ID + ".visel.fancyButton"; //$NON-NLS-1$

  public static final String PROPID_RENDERER_CFG = "rendererCfg"; //$NON-NLS-1$

  static final ITinFieldInfo TFI_RENDERER_CFG =
      TtiUtils.typedFieldInfo( PROPID_RENDERER_CFG, RoundButtonRenderer.tinTypeInfo(), //
          "Rendering properties", "Properties of visual representation of the button" );

  /**
   * The VISEL factory singleton.
   */
  public static final IVedViselFactory FACTORY = new VedAbstractViselFactory( FACTORY_ID, //
      TSID_NAME, STR_VISEL_BUTTON, //
      TSID_DESCRIPTION, STR_VISEL_BUTTON_D, //
      TSID_ICON_ID, ICONID_VISEL_BUTTON //
  ) {

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

      fields.add( TFI_RENDERER_CFG );
      fields.add( ViselButton.TFI_STATE );
      fields.add( TFI_ENABLED );
      fields.add( TFI_HOVERED );

      fields.add( TFI_IS_ASPECT_FIXED );
      fields.add( TFI_ASPECT_RATIO );
      fields.add( TFI_ZOOM_HIDDEN );
      fields.add( TFI_ANGLE_HIDDEN );
      fields.add( TFI_TRANSFORM_HIDDEN );
      fields.add( TFI_IS_ACTIVE_HIDDEN );

      return new PropertableEntitiesTinTypeInfo<>( fields, ViselFancyButton.class );
    }

    @Override
    protected VedAbstractVisel doCreate( IVedItemCfg aCfg, VedScreen aVedScreen ) {
      return new ViselFancyButton( aCfg, propDefs(), aVedScreen );
    }

  };

  AbstractViselRenderer renderer;

  private EButtonViselState prevState = EButtonViselState.NORMAL;

  /**
   * Constructor.
   *
   * @param aConfig {@link IVedItemCfg} - the item config
   * @param aPropDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - properties definitions
   * @param aVedScreen {@link VedScreen} - the owner screen
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public ViselFancyButton( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
  }

  @Override
  protected void doUpdateCachesAfterPropsChange( IOptionSet aChangedValue ) {
    super.doUpdateCachesAfterPropsChange( aChangedValue );
    if( aChangedValue.hasKey( PROPID_RENDERER_CFG ) ) {
      ViselRendererCfg cfg = aChangedValue.getValobj( PROPID_RENDERER_CFG );
      VedRendererFactoriesRegistry reg = tsContext().get( VedRendererFactoriesRegistry.class );
      IViselRendererFactory factory = reg.find( cfg.factoryId() );
      if( factory != null ) {
        renderer = factory.create( cfg, this, vedScreen() );
        if( aChangedValue.hasKey( PROPID_WIDTH ) ) {
          double width = aChangedValue.getDouble( PROPID_WIDTH );
          renderer.props().setDouble( PROPID_WIDTH, width );
        }
        if( aChangedValue.hasKey( PROPID_HEIGHT ) ) {
          double height = aChangedValue.getDouble( PROPID_HEIGHT );
          renderer.props().setDouble( PROPID_HEIGHT, height );
        }
      }
    }

    if( aChangedValue.keys().hasElem( PROPID_WIDTH ) ) {
      double width = aChangedValue.getDouble( PROPID_WIDTH );
      if( renderer != null ) {
        renderer.props().setDouble( PROPID_WIDTH, width );
      }
    }

    if( aChangedValue.keys().hasElem( PROPID_HEIGHT ) ) {
      double height = aChangedValue.getDouble( PROPID_HEIGHT );
      if( renderer != null ) {
        renderer.props().setDouble( PROPID_HEIGHT, height );
      }
    }

    if( aChangedValue.hasKey( PROPID_ENABLED ) ) {
      IAtomicValue av = aChangedValue.getByKey( PROPID_ENABLED );
      if( av != null && av.isAssigned() ) {
        props().propsEventer().pauseFiring();
        if( av.asBool() ) {
          if( prevState == EButtonViselState.DISABLED ) {
            prevState = EButtonViselState.NORMAL;
          }
          props().setValobj( ViselButton.PROPID_STATE, prevState );
        }
        else {
          prevState = props().getValobj( ViselButton.PROPID_STATE );
          props().setValobj( ViselButton.PROPID_STATE, EButtonViselState.DISABLED );
        }
        props().propsEventer().resumeFiring( false );
      }
    }
  }

  @Override
  public void paint( ITsGraphicsContext aPaintContext ) {
    renderer.paint( aPaintContext );
  }

}

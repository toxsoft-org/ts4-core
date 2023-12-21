package org.toxsoft.core.tsgui.ved.comps;

import static org.toxsoft.core.tsgui.bricks.tin.tti.ITtiConstants.*;
import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.comps.ITsResources.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
//import static org.toxsoft.sandbox.ved.ISandboxVedConstants.*;
//import static org.toxsoft.sandbox.ved.vs.comps.ITsResources.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.bricks.tin.tti.*;
import org.toxsoft.core.tsgui.graphics.gc.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The VISEL: push button.
 *
 * @author vs
 */
public class ViselButton
    extends VedAbstractVisel {

  // TODO разобраться с комментариями - либо раскомментировать, либо удалить

  /**
   * The VISEL factor ID.
   */
  public static final String FACTORY_ID = VED_ID + ".visel.button"; //$NON-NLS-1$

  // static final String PROPID_ARC_WIDTH = "arcWidth"; //$NON-NLS-1$
  // static final String PROPID_ARC_HEIGHT = "arcHeight"; //$NON-NLS-1$

  /**
   * Property id for buttons tate
   */
  public static final String PROPID_STATE = "state"; //$NON-NLS-1$ (EButtonViselState)

  /**
   * Property id for button hovered sign
   */
  public static final String PROPID_HOVERED = "hovered"; //$NON-NLS-1$ находится под курсором

  /**
   * The VISEL factory singleton.
   */
  public static final IVedViselFactory FACTORY = new VedAbstractViselFactory( FACTORY_ID, //
      TSID_NAME, STR_VISEL_BUTTON, //
      TSID_DESCRIPTION, STR_VISEL_BUTTON_D, //
      TSID_ICON_ID, ICONID_VISEL_BUTTON //
  ) {

    private static final IDataDef PROP_HOVERED = DataDef.create3( PROPID_HOVERED, DDEF_BOOLEAN, //
        TSID_NAME, "Hovered", //
        TSID_DESCRIPTION, "Hovered", //
        TSID_DEFAULT_VALUE, AV_FALSE );

    // static final IDataDef PROP_ARC_WIDTH = DataDef.create3( PROPID_ARC_WIDTH, DDEF_FLOATING, //
    // TSID_NAME, STR_VISEL_ARC_WIDTH, //
    // TSID_DESCRIPTION, STR_VISEL_ARC_WIDTH_D, //
    // TSID_DEFAULT_VALUE, avFloat( 16 ) );
    //
    // static final IDataDef PROP_ARC_HEIGHT = DataDef.create3( PROPID_ARC_HEIGHT, DDEF_FLOATING, //
    // TSID_NAME, STR_VISEL_ARC_HEIGHT, //
    // TSID_DESCRIPTION, STR_VISEL_ARC_HEIGHT_D, //
    // TSID_DEFAULT_VALUE, avFloat( 16 ) );

    private static final TinFieldInfo TFI_STATE = new TinFieldInfo( PROPID_STATE, TtiAvEnum.INSTANCE, //
        TSID_NAME, STR_N_BUTTON_STATE, //
        TSID_DESCRIPTION, STR_D_BUTTON_STATE, //
        TSID_KEEPER_ID, EButtonViselState.KEEPER_ID, //
        TSID_DEFAULT_VALUE, avValobj( EButtonViselState.NORMAL ) );

    private static final TinFieldInfo TFI_HOVERED = new TinFieldInfo( PROP_HOVERED, TTI_AT_BOOLEAN );

    @Override
    protected VedAbstractVisel doCreate( IVedItemCfg aCfg, VedScreen aVedScreen ) {
      return new ViselButton( aCfg, propDefs(), aVedScreen );
    }

    @Override
    protected ITinTypeInfo doCreateTypeInfo() {
      IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();
      fields.add( TFI_NAME );
      fields.add( TFI_DESCRIPTION );
      fields.add( TFI_TEXT );
      fields.add( TFI_FONT );
      fields.add( TFI_X );
      fields.add( TFI_Y );
      fields.add( TFI_WIDTH );
      fields.add( TFI_HEIGHT );
      fields.add( TFI_FULCRUM );
      fields.add( TFI_FG_COLOR );
      fields.add( TFI_BK_COLOR );
      fields.add( TFI_STATE );
      fields.add( TFI_HOVERED );
      // fields.add( TFI_TRANSFORM );
      fields.add( TFI_ZOOM );
      fields.add( TFI_ANGLE );
      fields.add( TinFieldInfo.makeCopy( TFI_TRANSFORM, ITinWidgetConstants.PRMID_IS_HIDDEN, AV_TRUE ) );
      fields.add( TFI_IS_ACTIVE );

      return new PropertableEntitiesTinTypeInfo<>( fields, ViselButton.class );
    }

  };

  TsFillInfo fillInfo = null;

  TsFillInfo pressedFillInfo = null;

  TsFillInfo disableFillInfo = new TsFillInfo( new RGBA( 164, 164, 164, 255 ) );

  private final IButtonRenderer btnRenderer;

  /**
   * Constructor.
   *
   * @param aConfig {@link IVedItemCfg} - the item config
   * @param aPropDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - properties definitions
   * @param aVedScreen {@link VedScreen} - the owner screen
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public ViselButton( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
    addInterceptor( new VedViselInterceptorMinWidthHeight( this ) );
    btnRenderer = new GradientButtonRenderer( this );
    // btnRenderer = new CoolButtonRenderer( this, 0.3 );
  }

  // ------------------------------------------------------------------------------------
  // VedAbstractVisel
  //

  @Override
  public void paint( ITsGraphicsContext aPaintContext ) {
    btnRenderer.drawButton( aPaintContext );
  }

  @Override
  protected void doUpdateCachesAfterPropsChange( IOptionSet aChangedValue ) {
    super.doUpdateCachesAfterPropsChange( aChangedValue );
    btnRenderer.update();
  }

}

package org.toxsoft.core.tsgui.ved.comps;

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
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tsgui.ved.editor.palette.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The VISEL: push button.
 *
 * @author vs
 */
public class ViselCheckbox
    extends VedAbstractVisel
    implements IViselButton {

  /**
   * The VISEL factor ID.
   */
  public static final String FACTORY_ID = VED_ID + ".visel.checkbox"; //$NON-NLS-1$

  /**
   * Property id for buttons state
   */
  public static final String PROPID_STATE = "state"; //$NON-NLS-1$ (EButtonViselState)

  private static final ITinTypeInfo  TTI_ON_OFF_STATE = new TinAtomicTypeInfo.TtiBoolean( PROP_ON_OF_STATE );
  private static final ITinFieldInfo TFI_ON_OFF_STATE = new TinFieldInfo( PROP_ON_OF_STATE, TTI_ON_OFF_STATE );

  private static final ITinFieldInfo TFI_CHECKED = TinFieldInfo.makeCopy( TFI_ON_OFF_STATE, //
      TSID_NAME, "Значение" );

  /**
   * The VISEL factory singleton.
   */
  public static final IVedViselFactory FACTORY = new VedAbstractViselFactory( FACTORY_ID, //
      TSID_NAME, STR_VISEL_BUTTON, //
      TSID_DESCRIPTION, STR_VISEL_BUTTON_D, //
      TSID_ICON_ID, ICONID_VISEL_CHECKBOX //
  ) {

    private static final TinFieldInfo TFI_STATE = new TinFieldInfo( PROPID_STATE, TtiAvEnum.INSTANCE, //
        TSID_NAME, STR_N_BUTTON_STATE, //
        TSID_DESCRIPTION, STR_D_BUTTON_STATE, //
        TSID_KEEPER_ID, EButtonViselState.KEEPER_ID, //
        TSID_DEFAULT_VALUE, avValobj( EButtonViselState.NORMAL ) );

    @Override
    protected VedAbstractVisel doCreate( IVedItemCfg aCfg, VedScreen aVedScreen ) {
      return new ViselCheckbox( aCfg, propDefs(), aVedScreen );
    }

    @Override
    protected ITinTypeInfo doCreateTypeInfo() {
      IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();
      fields.add( TFI_NAME );
      fields.add( TFI_DESCRIPTION );
      fields.add( TFI_CHECKED );
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
      fields.add( TFI_ZOOM );
      fields.add( TFI_ANGLE );
      fields.add( TinFieldInfo.makeCopy( TFI_TRANSFORM, ITinWidgetConstants.PRMID_IS_HIDDEN, AV_TRUE ) );
      fields.add( TFI_IS_ACTIVE );

      return new PropertableEntitiesTinTypeInfo<>( fields, ViselCheckbox.class );
    }

    @Override
    protected StridablesList<IVedItemsPaletteEntry> doCreatePaletteEntries() {
      VedItemCfg cfg = new VedItemCfg( id(), kind(), id(), IOptionSet.NULL );
      OptionSetUtils.initOptionSet( cfg.propValues(), propDefs() );
      cfg.propValues().setDouble( PROPID_HEIGHT, 24.0 );
      IVedItemsPaletteEntry pent = new VedItemPaletteEntry( id(), params(), cfg );
      return new StridablesList<>( pent );
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
  public ViselCheckbox( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
    addInterceptor( new VedViselInterceptorMinWidthHeight( this ) );
    btnRenderer = new CheckboxRenderer( this );
    actionsProvider().addHandler( new AspPackVisel() );
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

  // ------------------------------------------------------------------------------------
  // IViselButton
  //

  @Override
  public boolean isChecked() {
    return props().getBool( PROPID_ON_OFF_STATE );
  }

  @Override
  public EButtonViselState buttonState() {
    return props().getValobj( PROPID_STATE );
  }

  @Override
  public ID2Point getPackedSize( double aWidth, double aHeight ) {
    return btnRenderer.getPackedSize( aWidth, aHeight );
  }
}

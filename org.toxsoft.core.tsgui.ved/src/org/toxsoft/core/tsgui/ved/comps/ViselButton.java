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
import org.toxsoft.core.tsgui.graphics.lines.*;
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
  static final String PROPID_STATE   = "state";   //$NON-NLS-1$ (EButtonViselState)
  static final String PROPID_HOVERED = "hovered"; //$NON-NLS-1$ находится под курсором

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
      fields.add( TFI_FG_COLOR );
      fields.add( TFI_BK_COLOR );
      fields.add( TFI_STATE );
      fields.add( TFI_HOVERED );
      // fields.add( new TinFieldInfo( PROPID_ARC_WIDTH, TTI_AT_FLOATING, PROP_ARC_WIDTH.params() ) );
      // fields.add( new TinFieldInfo( PROPID_ARC_HEIGHT, TTI_AT_FLOATING, PROP_ARC_HEIGHT.params() ) );
      fields.add( TFI_TRANSFORM );
      fields.add( TFI_IS_ACTIVE );

      return new PropertableEntitiesTinTypeInfo<>( fields, ViselButton.class );
    }

  };

  private Rectangle swtRect = new Rectangle( 0, 0, 1, 1 );

  Font font = null;

  TsFillInfo fillInfo = null;

  TsFillInfo pressedFillInfo = null;

  TsFillInfo disableFillInfo = new TsFillInfo( new RGBA( 164, 164, 164, 255 ) );

  TsLineInfo lineInfo = TsLineInfo.ofWidth( 1 );

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
  }

  // ------------------------------------------------------------------------------------
  // VedAbstractVisel
  //

  @Override
  public void paint( ITsGraphicsContext aPaintContext ) {

    aPaintContext.gc().setFont( font );

    TsFillInfo fi = fillInfo;

    EButtonViselState state = props().getValobj( PROPID_STATE );
    if( state == EButtonViselState.DISABLED ) {
      aPaintContext.gc().setForeground( colorManager().getColor( new RGB( 96, 96, 96 ) ) );
      aPaintContext.gc().setBackground( colorManager().getColor( new RGB( 164, 164, 164 ) ) );
      fi = disableFillInfo;
    }
    else {
      if( state == EButtonViselState.PRESSED ) {
        fi = pressedFillInfo;
      }
    }
    ID2Rectangle r = bounds();

    // int arcW = (int)props().getDouble( PROPID_ARC_WIDTH );
    // int arcH = (int)props().getDouble( PROPID_ARC_HEIGHT );
    int arcW = 16;
    int arcH = 16;
    aPaintContext.setFillInfo( fi );
    aPaintContext.fillRoundRect( swtRect.x, swtRect.y, swtRect.width, swtRect.height, arcW, arcH );
    aPaintContext.setLineInfo( lineInfo );

    aPaintContext.drawRoundRect( swtRect.x, swtRect.y, swtRect.width, swtRect.height, arcW, arcH );

    String text = props().getStr( PROPID_TEXT );
    Point p = aPaintContext.gc().textExtent( text );
    int x = (int)(r.x1() + (r.width() - p.x) / 2.);
    int y = (int)(r.y1() + (r.height() - p.y) / 2.);

    if( state == EButtonViselState.PRESSED ) {
      x += 2;
      y += 2;
    }

    aPaintContext.gc().setBackgroundPattern( null );
    aPaintContext.gc().setForeground( colorManager().getColor( (RGBA)props().getValobj( PROPID_FG_COLOR ) ) );
    aPaintContext.gc().drawText( text, x, y, true );

    // if( props().getBool( PROPID_HOVERED ) && state != EButtonViselState.DISABLED ) {
    // aPaintContext.gc().setForeground( colorManager().getColor( new RGB( 80, 100, 130 ) ) );
    // }
  }

  @Override
  protected void doUpdateCachesAfterPropsChange( IOptionSet aChangedValue ) {
    super.doUpdateCachesAfterPropsChange( aChangedValue );
    font = fontManager().getFont( props().getValobj( PROPID_FONT ) );
    RGBA bkRgba = props().getValobj( PROPID_BK_COLOR );

    D2Point sp = new D2Point( 0, 0 );
    D2Point ep = new D2Point( 0, 100 );
    RGBA sc = new RGBA( 220, 220, 220, 255 );
    RGBA ec = new RGBA( 190, 190, 190, 255 );

    RGB rgb = GradientUtils.tuneBrightness( bkRgba.rgb, 0.2 );
    sc = new RGBA( rgb.red, rgb.green, rgb.blue, 255 );
    rgb = GradientUtils.tuneBrightness( bkRgba.rgb, -0.2 );
    ec = new RGBA( rgb.red, rgb.green, rgb.blue, 255 );

    LinearGradientInfo lgi = new LinearGradientInfo( sp, ep, sc, ec, new D2Point( 1.1, 1.1 ) );
    fillInfo = new TsFillInfo( new TsGradientFillInfo( lgi ) );
    lgi = new LinearGradientInfo( sp, ep, ec, sc, new D2Point( 1.1, 1.1 ) );
    pressedFillInfo = new TsFillInfo( new TsGradientFillInfo( lgi ) );
    updateSwtRect();
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

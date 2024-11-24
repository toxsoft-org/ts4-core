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
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.gc.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
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
 * Visel отображающий значение в заданном диапазоне в виде "столбика".
 *
 * @author vs
 */
public class ViselLinearGauge
    extends VedAbstractVisel {

  /**
   * The VISEL factor ID.
   */
  public static final String FACTORY_ID = VED_ID + ".visel.LinearGauge"; //$NON-NLS-1$

  private static final ITinFieldInfo TFI_VALUE_FILL = new TinFieldInfo( "valueFill", TtiTsFillInfo.INSTANCE, // //$NON-NLS-1$
      TSID_NAME, STR_VALUE_BACKGOUND );

  private static final ITinFieldInfo TFI_VALUE = new TinFieldInfo( PROPID_VALUE, TTI_AT_FLOATING, //
      TSID_NAME, STR_VALUE );

  private static final ITinFieldInfo TFI_MIN_VALUE = new TinFieldInfo( "minValue", TTI_AT_FLOATING, // //$NON-NLS-1$
      TSID_NAME, STR_MIN );

  private static final ITinFieldInfo TFI_MAX_VALUE = new TinFieldInfo( "maxValue", TTI_AT_FLOATING, // //$NON-NLS-1$
      TSID_NAME, STR_MAX );

  private static final ITinFieldInfo TFI_ARROW_HEIGHT = new TinFieldInfo( "arrowHeight", TTI_AT_INTEGER, // //$NON-NLS-1$
      TSID_NAME, STR_ARROW_HEIGHT );

  private static final ITinFieldInfo TFI_ARROW_COLOR = new TinFieldInfo( "arrowFill", TtiTsFillInfo.INSTANCE, // //$NON-NLS-1$
      TSID_NAME, STR_ARROW_COLOR );

  /**
   * The VISEL factory singleton.
   */
  public static final IVedViselFactory FACTORY = new VedAbstractViselFactory( FACTORY_ID, //
      TSID_NAME, STR_VISEL_LINEAR_GAUGE, //
      TSID_DESCRIPTION, STR_VISEL_LINEAR_GAUGE_D, //
      TSID_ICON_ID, ICONID_VISEL_LINEAR_GAUGE //
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
      fields.add( TFI_ORIENTATION );
      fields.add( TFI_FULCRUM );
      fields.add( TFI_VALUE );
      fields.add( TFI_MIN_VALUE );
      fields.add( TFI_MAX_VALUE );
      fields.add( TFI_BK_FILL );
      fields.add( TFI_VALUE_FILL );
      fields.add( TFI_BORDER_INFO );
      fields.add( TFI_ARROW_HEIGHT );
      fields.add( TFI_ARROW_COLOR );
      fields.add( TinFieldInfo.makeCopy( TFI_TRANSFORM, ITinWidgetConstants.PRMID_IS_HIDDEN, AV_TRUE ) );
      fields.add( TFI_ZOOM );
      fields.add( TFI_ANGLE );
      return new PropertableEntitiesTinTypeInfo<>( fields, ViselLinearGauge.class );
    }

    @Override
    protected VedAbstractVisel doCreate( IVedItemCfg aCfg, VedScreen aVedScreen ) {
      return new ViselLinearGauge( aCfg, propDefs(), aVedScreen );
    }

    @Override
    protected StridablesList<IVedItemsPaletteEntry> doCreatePaletteEntries() {
      VedItemCfg cfg = new VedItemCfg( id(), kind(), id(), IOptionSet.NULL );
      OptionSetUtils.initOptionSet( cfg.propValues(), propDefs() );
      cfg.propValues().setDouble( TFI_WIDTH.id(), 25.0 );
      cfg.propValues().setDouble( TFI_HEIGHT.id(), 100.0 );
      cfg.propValues().setDouble( TFI_MIN_VALUE.id(), 0.0 );
      cfg.propValues().setDouble( TFI_MAX_VALUE.id(), 100.0 );
      cfg.propValues().setDouble( TFI_VALUE.id(), 50.0 );
      cfg.propValues().setValobj( TFI_BK_FILL.id(), new TsFillInfo( new RGBA( 128, 128, 128, 255 ) ) );
      cfg.propValues().setValobj( TFI_VALUE_FILL.id(), new TsFillInfo( new RGBA( 0, 0, 200, 255 ) ) );
      cfg.propValues().setValobj( TFI_ARROW_COLOR.id(), new TsFillInfo( new RGBA( 255, 0, 0, 255 ) ) );
      cfg.propValues().setInt( TFI_ARROW_HEIGHT.id(), 16 );
      cfg.propValues().setValobj( TFI_ORIENTATION.id(), ETsOrientation.VERTICAL );

      IVedItemsPaletteEntry pent = new VedItemPaletteEntry( id(), params(), cfg );
      return new StridablesList<>( pent );
    }

  };

  /**
   * Constructor.
   *
   * @param aConfig {@link IVedItemCfg} - the item config
   * @param aPropDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - properties definitions
   * @param aVedScreen {@link VedScreen} - the owner screen
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public ViselLinearGauge( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
  }

  private double     value     = 50;
  private double     minValue  = 0;
  private double     maxValue  = 100;
  private int        arrowH    = 16;
  private TsFillInfo arrowFill = new TsFillInfo( new RGBA( 255, 0, 0, 255 ) );

  private ETsOrientation orientation = ETsOrientation.VERTICAL;

  private final int[] arrowPoints = new int[6];

  // ------------------------------------------------------------------------------------
  // VedAbstractVisel
  //

  @Override
  protected void doUpdateCachesAfterPropsChange( IOptionSet aChangedValue ) {
    if( aChangedValue.hasKey( TFI_VALUE.id() ) ) {
      value = aChangedValue.getDouble( TFI_VALUE.id() );
    }
    if( aChangedValue.hasKey( TFI_MIN_VALUE.id() ) ) {
      minValue = aChangedValue.getDouble( TFI_MIN_VALUE.id() );
    }
    if( aChangedValue.hasKey( TFI_MAX_VALUE.id() ) ) {
      maxValue = aChangedValue.getDouble( TFI_MAX_VALUE.id() );
    }
    if( aChangedValue.hasKey( TFI_ARROW_HEIGHT.id() ) ) {
      arrowH = aChangedValue.getInt( TFI_ARROW_HEIGHT.id() );
    }
    if( aChangedValue.hasKey( TFI_ARROW_COLOR.id() ) ) {
      arrowFill = aChangedValue.getValobj( TFI_ARROW_COLOR.id() );
    }
    if( aChangedValue.hasKey( TFI_ORIENTATION.id() ) ) {
      orientation = aChangedValue.getValobj( TFI_ORIENTATION.id() );
    }
    super.doUpdateCachesAfterPropsChange( aChangedValue );
  }

  @Override
  public void paint( ITsGraphicsContext aPaintContext ) {
    ID2Rectangle br = borderRect();
    aPaintContext.setFillInfo( props().getValobj( PROPID_BK_FILL ) );
    aPaintContext.fillD2Rect( br.x1(), br.y1(), (int)br.width(), (int)br.height() );
    if( value <= minValue ) {
      aPaintContext.setBorderInfo( props().getValobj( PROPID_BORDER_INFO ) );
      aPaintContext.drawD2RectBorder( br.x1(), br.y1(), (int)br.width(), (int)br.height() );
      if( value < minValue ) {
        if( orientation == ETsOrientation.VERTICAL ) {
          fillDownArrowPoints( clientRect() );
        }

        aPaintContext.gc().setBackground( new Color( 255, 0, 0 ) );
        aPaintContext.gc().fillPolygon( arrowPoints );
        aPaintContext.setFillInfo( arrowFill );
        aPaintContext.fillPolygon( arrowPoints );
      }

      return;
    }

    if( value >= maxValue ) {
      aPaintContext.setFillInfo( props().getValobj( TFI_VALUE_FILL.id() ) );
      aPaintContext.fillD2Rect( br.x1(), br.y1(), (int)br.width(), (int)br.height() );
      aPaintContext.setBorderInfo( props().getValobj( PROPID_BORDER_INFO ) );
      aPaintContext.drawD2RectBorder( br.x1(), br.y1(), (int)br.width(), (int)br.height() );
      if( value > maxValue ) {
        if( orientation == ETsOrientation.VERTICAL ) {
          fillUpArrowPoints( br );
        }

        // aPaintContext.gc().setBackground( new Color( 255, 0, 0 ) );
        // aPaintContext.gc().fillPolygon( arrowPoints );
        aPaintContext.setFillInfo( arrowFill );
        aPaintContext.fillPolygon( arrowPoints );
      }
      return;
    }

    ID2Rectangle cr = clientRect();
    if( orientation == ETsOrientation.VERTICAL ) {
      int y;
      y = (int)(br.height() * value / (maxValue - minValue));

      aPaintContext.setFillInfo( props().getValobj( TFI_VALUE_FILL.id() ) );
      aPaintContext.fillD2Rect( br.x1(), (int)(br.y1() + br.height()) - y, (int)br.width(), y );
    }
    else {
      int x;
      x = (int)(br.width() * value / (maxValue - minValue));

      aPaintContext.setFillInfo( props().getValobj( TFI_VALUE_FILL.id() ) );
      aPaintContext.fillD2Rect( br.x1(), br.y1(), x, (int)br.height() );
    }

    aPaintContext.setBorderInfo( props().getValobj( PROPID_BORDER_INFO ) );
    aPaintContext.drawD2RectBorder( br.x1(), br.y1(), (int)br.width(), (int)br.height() );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  ID2Rectangle borderRect() {
    ID2Rectangle r = bounds();
    if( orientation == ETsOrientation.VERTICAL ) {
      return new D2Rectangle( 0, 0 + arrowH, r.width(), r.height() - 2 * arrowH );
    }
    return new D2Rectangle( 0 + arrowH, 0, r.width() - 2 * arrowH, r.height() );
  }

  ID2Rectangle clientRect() {
    // ID2Rectangle r = borderRect();
    ID2Rectangle r = bounds();
    TsBorderInfo bi = props().getValobj( TFI_BORDER_INFO.id() );
    int bw = bi.width();
    D2Rectangle cr = new D2Rectangle( 0 + bw, 0 + bw, r.width() - 2 * bw, r.height() - 2 * bw );
    return cr;
  }

  void fillUpArrowPoints( ID2Rectangle aBorderRect ) {
    TsBorderInfo bi = props().getValobj( TFI_BORDER_INFO.id() );
    int bw = bi.width();

    arrowPoints[0] = (int)aBorderRect.x1(); // x
    arrowPoints[1] = (int)aBorderRect.y1() - bw; // y
    arrowPoints[2] = (int)(aBorderRect.x1() + (aBorderRect.width() - 2 * bw) / 2.); // x1
    arrowPoints[3] = (int)aBorderRect.y1() - arrowH; // y1
    arrowPoints[4] = (int)aBorderRect.x2() - bw; // x2
    arrowPoints[5] = (int)aBorderRect.y1() - bw; // y2
  }

  void fillDownArrowPoints( ID2Rectangle aR ) {
    TsBorderInfo bi = props().getValobj( TFI_BORDER_INFO.id() );
    int bw = bi.width();

    ID2Rectangle r = bounds();

    arrowPoints[0] = 0 - 1; // x
    arrowPoints[1] = (int)r.height() - arrowH; // y
    arrowPoints[2] = (int)(r.width() / 2.) - 1; // x1
    arrowPoints[3] = (int)r.height();// + arrowH; // y1
    arrowPoints[4] = (int)r.width() - 1 - 1; // x2
    arrowPoints[5] = (int)r.height() - arrowH; // y2
  }

}

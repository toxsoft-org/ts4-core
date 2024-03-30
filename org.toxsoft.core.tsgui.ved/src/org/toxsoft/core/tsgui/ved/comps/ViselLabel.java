package org.toxsoft.core.tsgui.ved.comps;

import static org.toxsoft.core.tsgui.graphics.ITsGraphicsConstants.*;
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
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.fonts.*;
import org.toxsoft.core.tsgui.graphics.gc.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tsgui.ved.editor.palette.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Визуальный элемент - "Текст".
 * <p>
 *
 * @author vs
 */
public class ViselLabel
    extends VedAbstractVisel {

  /**
   * The VISEL factory ID.
   */
  public static final String FACTORY_ID = VED_ID + ".visel.Label"; //$NON-NLS-1$

  /**
   * Id for property - "Selection"
   */
  public static final String PROPID_SELECTION = "selection"; //$NON-NLS-1$

  /**
   * Id for property - "Selection color"
   */
  public static final String PROPID_SELECTION_COLOR = "selectionColor"; //$NON-NLS-1$

  /**
   * Id for property - "Selection background color"
   */
  public static final String PROPID_SELECTION_BACKGROUND = "selectionBackground"; //$NON-NLS-1$

  static final IDataDef PROP_SELECTION = DataDef.create3( PROPID_SELECTION, DT_TSPOINT, //
      TSID_NAME, STR_SELECTION, //
      TSID_DESCRIPTION, STR_SELECTION_D, //
      TSID_DEFAULT_VALUE, avValobj( ITsPoint.ZERO ) //
  );

  static final ITinFieldInfo TFI_SELECTION = new TinFieldInfo( PROP_SELECTION, TtiTsPoint.INSTANCE );

  static final IDataDef PROP_SELECTION_COLOR = DataDef.create3( PROPID_SELECTION_COLOR, DT_COLOR_RGB, //
      TSID_NAME, STR_SELECTION_COLOR, //
      TSID_DESCRIPTION, STR_SELECTION_COLOR_D, //
      TSID_DEFAULT_VALUE, avValobj( ETsColor.WHITE.rgb() ) //
  );

  static final IDataDef PROP_SELECTION_BACKGROUND = DataDef.create3( PROPID_SELECTION_BACKGROUND, DT_COLOR_RGB, //
      TSID_NAME, STR_SELECTION_BACKGROUND, //
      TSID_DESCRIPTION, STR_SELECTION_BACKGROUND_D, //
      TSID_DEFAULT_VALUE, avValobj( ETsColor.BLUE.rgb() ) //
  );

  static final ITinFieldInfo TFI_SELECTION_COLOR = new TinFieldInfo( PROP_SELECTION_COLOR, TtiRGB.INSTANCE );

  static final ITinFieldInfo TFI_SELECTION_BACKGROUND = new TinFieldInfo( PROP_SELECTION_BACKGROUND, TtiRGB.INSTANCE );

  /**
   * The VISEL factory singleton.
   */
  public static final IVedViselFactory FACTORY = new VedAbstractViselFactory( FACTORY_ID, //
      TSID_NAME, STR_VISEL_LABEL, //
      TSID_DESCRIPTION, STR_VISEL_LABEL_D, //
      TSID_ICON_ID, ICONID_VISEL_LABEL ) {

    // ------------------------------------------------------------------------------------
    // VedAbstractViselFactory
    //

    @Override
    protected VedAbstractVisel doCreate( IVedItemCfg aCfg, VedScreen aScreen ) {
      return new ViselLabel( aCfg, propDefs(), aScreen );
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
      fields.add( TFI_TEXT );
      fields.add( TFI_FG_COLOR );
      fields.add( TFI_FONT );
      fields.add( TFI_HOR_ALIGNMENT );
      fields.add( TFI_VER_ALIGNMENT );

      fields.add( TFI_FG_COLOR );

      fields.add( TFI_SELECTION_COLOR );
      fields.add( TFI_SELECTION_BACKGROUND );

      fields.add( TFI_LEFT_INDENT );
      fields.add( TFI_TOP_INDENT );
      fields.add( TFI_RIGHT_INDENT );
      fields.add( TFI_BOTTOM_INDENT );

      fields.add( TFI_BK_FILL );
      fields.add( TFI_BORDER_INFO );
      fields.add( TFI_ZOOM );
      fields.add( TFI_ANGLE );
      fields.add( TFI_IS_ACTIVE );
      fields.add( TinFieldInfo.makeCopy( TFI_TRANSFORM, ITinWidgetConstants.PRMID_IS_HIDDEN, AV_TRUE ) );
      fields.add( TinFieldInfo.makeCopy( TFI_CARET_POS, ITinWidgetConstants.PRMID_IS_HIDDEN, AV_TRUE ) );
      // fields.add( TinFieldInfo.makeCopy( TFI_SELECTION, ITinWidgetConstants.PRMID_IS_HIDDEN, AV_TRUE ) );
      fields.add( TFI_SELECTION );
      return new PropertableEntitiesTinTypeInfo<>( fields, ViselLabel.class );
    }

    @Override
    protected StridablesList<IVedItemsPaletteEntry> doCreatePaletteEntries() {
      VedItemCfg cfg = new VedItemCfg( id(), kind(), id(), IOptionSet.NULL );
      OptionSetUtils.initOptionSet( cfg.propValues(), propDefs() );
      cfg.propValues().setDouble( PROPID_HEIGHT, 30.0 );
      cfg.propValues().setValobj( PROPID_HOR_ALIGNMENT, EHorAlignment.CENTER );
      IVedItemsPaletteEntry pent1 = new VedItemPaletteEntry( id(), params(), cfg );
      return new StridablesList<>( pent1 );
    }

  };

  private Font font = null;

  /**
   * Constructor.
   *
   * @param aConfig {@link IVedItemCfg} - the item config
   * @param aPropDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - properties definitions
   * @param aVedScreen {@link VedScreen} - the owner screen
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public ViselLabel( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
    actionsProvider().addHandler( new AspPackVisel() );
  }

  // ------------------------------------------------------------------------------------
  // VedAbstractVisel
  //

  @Override
  public void paint( ITsGraphicsContext aPaintContext ) {
    ID2Rectangle r = bounds();

    aPaintContext.setFillInfo( props().getValobj( PROPID_BK_FILL ) );
    aPaintContext.fillRect( 0, 0, (int)r.width(), (int)r.height() );

    if( font == null ) {
      IFontInfo fi = props().getValobj( PROPID_FONT );
      font = fontManager().getFont( fi );
    }
    aPaintContext.gc().setFont( font );
    aPaintContext.setForegroundRgba( props().getValobj( PROPID_FG_COLOR ) );

    String text = props().getStr( PROPID_TEXT );
    Point p = aPaintContext.gc().textExtent( text );
    int x = calcTextX( p, r );
    int y = calcTextY( p, r );

    aPaintContext.gc().setBackgroundPattern( null );
    aPaintContext.gc().drawText( text, x, y, true );
    aPaintContext.gc().setAlpha( 255 );
    paintSelection( x, y, p, aPaintContext );
    aPaintContext.setBorderInfo( props().getValobj( PROPID_BORDER_INFO ) );
    aPaintContext.drawRectBorder( 0, 0, (int)r.width(), (int)r.height() );

    int caretPos = props().getInt( PROPID_CARET_POS );
    if( caretPos != -1 ) {
      aPaintContext.gc().setLineWidth( 1 );
      aPaintContext.gc().setForeground( colorManager().getColor( ETsColor.BLACK ) );

      Point subExt = new Point( 0, 0 );
      if( text.length() > 0 ) {
        String subStr = text.substring( caretPos );
        subExt = aPaintContext.gc().textExtent( subStr );
      }

      aPaintContext.gc().drawLine( x + p.x - subExt.x, y, x + p.x - subExt.x, y + p.y );
    }

  }

  @Override
  protected void doDoInterceptPropsChange( IOptionSet aNewValues, IOptionSetEdit aValuesToSet ) {
    super.doDoInterceptPropsChange( aNewValues, aValuesToSet );
    GC gc = null;
    try {
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
      gc = new GC( getShell() );
      IFontInfo fi = props().getValobj( PROPID_FONT );
      if( aValuesToSet.hasKey( PROPID_FONT ) ) {
        fi = aValuesToSet.getValobj( PROPID_FONT );
      }
      font = fontManager().getFont( fi );
      gc.setFont( font );
      String text = props().getStr( PROPID_TEXT );
      if( aValuesToSet.hasKey( PROPID_TEXT ) ) {
        text = aValuesToSet.getStr( PROPID_TEXT );
      }
      if( text.isEmpty() ) {
        text = " "; //$NON-NLS-1$
      }
      Point p = gc.textExtent( text );
      int textWidth = p.x;
      int textHeight = p.y;

      if( width < textWidth ) {
        width = textWidth;
        aValuesToSet.setDouble( PROPID_WIDTH, textWidth );
      }
      if( height < textHeight ) {
        height = textHeight;
        aValuesToSet.setDouble( PROPID_HEIGHT, textHeight );
      }
    }
    finally {
      if( gc != null ) {
        gc.dispose();
      }
    }
  }

  @Override
  protected ID2Point getPackedSize( double aWidth, double aHeight ) {
    TsBorderInfo bi = props().getValobj( PROPID_BORDER_INFO );
    if( bi.kind() == ETsBorderKind.NONE ) {
      GC gc = null;
      try {
        gc = new GC( getShell() );
        Point p = gc.textExtent( props().getStr( PROPID_TEXT ) );
        double w = props().getInt( PROPID_LEFT_INDENT ) + p.x + props().getInt( PROPID_RIGHT_INDENT );
        double h = props().getInt( PROPID_TOP_INDENT ) + p.y + props().getInt( PROPID_BOTTOM_INDENT );
        return new D2Point( w, h );
      }
      finally {
        if( gc != null ) {
          gc.dispose();
        }
      }

    }
    return super.getPackedSize( aWidth, aHeight );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает индекс символа для текстового курсора по Х - координате.
   *
   * @param aX - X координата в системе координат визуального элемента
   * @return int - индекс символа для текстового курсора
   */
  public int findCaretPos( int aX ) {
    GC gc = new GC( getDisplay() );

    String text = props().getStr( PROPID_TEXT );
    int txtX = calcTextX( gc.textExtent( text ), bounds() );
    if( aX < txtX ) {
      gc.dispose();
      return 0;
    }

    if( aX > txtX + gc.textExtent( text ).x ) {
      gc.dispose();
      return text.length();
    }

    String subStr;
    for( int i = 0; i < text.length(); i++ ) {
      subStr = text.substring( 0, i + 1 );
      int posX = txtX + gc.textExtent( subStr ).x;
      if( posX >= aX ) {
        gc.dispose();
        return i;
      }
    }

    gc.dispose();
    return -1;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private void paintSelection( int aTextX, int aTextY, Point aFullExtent, ITsGraphicsContext aPaintContext ) {
    ITsPoint sel = props().getValobj( PROPID_SELECTION );
    if( sel != ITsPoint.ZERO ) {
      ID2Rectangle r = bounds();

      int startIdx = Math.min( sel.x(), sel.y() );
      int endIdx = Math.max( sel.x(), sel.y() );

      String text = props().getStr( PROPID_TEXT );

      String subStr = text.substring( startIdx );
      Point subExt = aPaintContext.gc().textExtent( subStr );
      int startX = aTextX + aFullExtent.x - subExt.x;

      subStr = text.substring( endIdx );
      subExt = aPaintContext.gc().textExtent( subStr );
      int endX = aTextX + aFullExtent.x - subExt.x;

      subStr = text.substring( startIdx, endIdx );
      RGB selFgRgb = props().getValobj( PROPID_SELECTION_COLOR );
      RGB selBkRgb = props().getValobj( PROPID_SELECTION_BACKGROUND );
      aPaintContext.gc().setForeground( colorManager().getColor( selFgRgb ) );
      aPaintContext.gc().setBackground( colorManager().getColor( selBkRgb ) );
      aPaintContext.gc().fillRectangle( startX, 2, endX - startX, (int)r.height() - 4 );

      aPaintContext.gc().drawText( subStr, startX, aTextY, true );
    }
  }

  int calcTextX( Point aTextExtent, ID2Rectangle aBounds ) {
    int x = 0;

    EHorAlignment ha = props().getValobj( PROPID_HOR_ALIGNMENT );
    x = switch( ha ) {
      case LEFT -> 0 + props().getInt( PROPID_LEFT_INDENT );
      case FILL, CENTER -> (int)((aBounds.width() - aTextExtent.x) / 2.);
      case RIGHT -> (int)aBounds.width() - aTextExtent.x - props().getInt( PROPID_RIGHT_INDENT );
      default -> throw new TsNotAllEnumsUsedRtException();
    };
    return x;
  }

  int calcTextY( Point aTextExtent, ID2Rectangle aBounds ) {
    int y = 0;
    EVerAlignment va = props().getValobj( PROPID_VER_ALIGNMENT );
    y = switch( va ) {
      case TOP -> 0 + props().getInt( PROPID_TOP_INDENT );
      case FILL, CENTER -> (int)((aBounds.height() - aTextExtent.y) / 2.);
      case BOTTOM -> (int)aBounds.height() - aTextExtent.y - props().getInt( PROPID_BOTTOM_INDENT );
      default -> throw new TsNotAllEnumsUsedRtException();
    };
    return y;
  }

}

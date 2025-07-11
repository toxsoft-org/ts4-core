package org.toxsoft.core.tsgui.ved.comps;

import static org.toxsoft.core.tsgui.bricks.tin.tti.ITtiConstants.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.comps.ITsResources.*;
import static org.toxsoft.core.tsgui.ved.l10n.ITsguiVedSharedResources.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.fonts.*;
import org.toxsoft.core.tsgui.graphics.gc.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tsgui.ved.editor.palette.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tsgui.ved.valeds.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Визуальный элемент - "Многострочный текст".
 * <p>
 * <b>Важно:</b><br>
 * Данный элемент не поддерживает редактирование
 *
 * @author vs
 */
public class ViselMultiLineLabel
    extends VedAbstractVisel {

  /**
   * The VISEL factory ID.
   */
  public static final String FACTORY_ID = VED_ID + ".visel.MultiStringLabel"; //$NON-NLS-1$

  static final IDataDef PROP_MULTI_TEXT = DataDef.create3( PROPID_TEXT, DDEF_STRING, //
      TSID_NAME, STR_TEXT, //
      TSID_DESCRIPTION, STR_TEXT_D, //
      OPDEF_EDITOR_FACTORY_NAME, ValedAvMultiLineTextEditor.FACTORY_NAME, //
      OPID_VERTICAL_SPAN, avInt( 3 ), //
      TSID_DEFAULT_VALUE, avStr( TsLibUtils.EMPTY_STRING ) //
  );

  static final IDataDef PROP_IS_ACTOR_MANDATORY = DataDef.create( PROPID_IS_ACTOR_MANDATORY, EAtomicType.BOOLEAN, //
      TSID_NAME, STR_IS_ACTOR_MANDATORY, //
      TSID_DESCRIPTION, STR_IS_ACTOR_MANDATORY_D, //
      TSID_DEFAULT_VALUE, AvUtils.AV_TRUE //
  );

  static final ITinFieldInfo TFI_IS_ACTOR_NEEDED = new TinFieldInfo( PROP_IS_ACTOR_MANDATORY, TTI_AT_BOOLEAN );

  static final ITinFieldInfo TFI_MULTILINE_TEXT = new TinFieldInfo( PROP_MULTI_TEXT, TTI_AT_STRING );

  /**
   * The VISEL factory singleton.
   */
  public static final IVedViselFactory FACTORY = new VedAbstractViselFactory( FACTORY_ID, //
      TSID_NAME, STR_VISEL_LABEL, //
      TSID_DESCRIPTION, STR_VISEL_LABEL_D, //
      TSID_ICON_ID, ICONID_VISEL_MULTI_LABEL ) {

    // ------------------------------------------------------------------------------------
    // VedAbstractViselFactory
    //

    @Override
    protected VedAbstractVisel doCreate( IVedItemCfg aCfg, VedScreen aScreen ) {
      return new ViselMultiLineLabel( aCfg, propDefs(), aScreen );
    }

    @Override
    protected ITinTypeInfo doCreateTypeInfo() {
      IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();
      fields.add( TFI_IS_ACTOR_NEEDED );
      fields.add( TFI_NAME );
      fields.add( TFI_DESCRIPTION );
      fields.add( TFI_X );
      fields.add( TFI_Y );
      fields.add( TFI_WIDTH );
      fields.add( TFI_HEIGHT );
      fields.add( TFI_FULCRUM );
      fields.add( TFI_MULTILINE_TEXT );
      fields.add( TFI_FG_COLOR );
      fields.add( TFI_FONT );
      fields.add( TFI_HOR_ALIGNMENT );
      fields.add( TFI_VER_ALIGNMENT );

      fields.add( TFI_FG_COLOR );

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
      return new PropertableEntitiesTinTypeInfo<>( fields, ViselMultiLineLabel.class );
    }

    @Override
    protected StridablesList<IVedItemsPaletteEntry> doCreatePaletteEntries() {
      VedItemCfg cfg = new VedItemCfg( id(), kind(), id(), IOptionSet.NULL );
      OptionSetUtils.initOptionSet( cfg.propValues(), propDefs() );
      cfg.propValues().setStr( PROPID_TEXT, "Multi-line" ); //$NON-NLS-1$
      cfg.propValues().setDouble( PROPID_HEIGHT, 30.0 );
      cfg.propValues().setValobj( PROPID_HOR_ALIGNMENT, EHorAlignment.CENTER );
      IVedItemsPaletteEntry pent1 = new VedItemPaletteEntry( id(), params(), cfg );
      return new StridablesList<>( pent1 );
    }

  };

  private Font font = null;

  private String[] subStrings = {};

  double linesGap = 0.0;

  IListEdit<Point> textExtents = new ElemArrayList<>();

  /**
   * Constructor.
   *
   * @param aConfig {@link IVedItemCfg} - the item config
   * @param aPropDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - properties definitions
   * @param aVedScreen {@link VedScreen} - the owner screen
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public ViselMultiLineLabel( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
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

    // String text = props().getStr( PROPID_TEXT );
    // Point p = aPaintContext.gc().textExtent( text );
    Point p = calcTecxtSize();
    int x = calcTextX( p, r );
    int y = calcTextY( p, r );
    ID2Rectangle textRect = new D2Rectangle( 0, 0, p.x, p.y );

    aPaintContext.gc().setBackgroundPattern( null );
    // aPaintContext.gc().drawText( text, x, y, true );

    int strH = aPaintContext.gc().getFontMetrics().getHeight();
    int textY = y;
    for( int i = 0; i < subStrings.length; i++ ) {
      String str = subStrings[i];
      p = textExtents.get( i );
      aPaintContext.gc().drawText( str, x + calcTextX( p, textRect ), textY, true );
      textY += strH;
    }

    aPaintContext.gc().setAlpha( 255 );
    // paintSelection( x, y, p, aPaintContext );
    aPaintContext.setBorderInfo( props().getValobj( PROPID_BORDER_INFO ) );
    aPaintContext.drawRectBorder( 0, 0, (int)r.width(), (int)r.height() );
  }

  @Override
  protected void doUpdateCachesAfterPropsChange( IOptionSet aChangedValue ) {
    super.doUpdateCachesAfterPropsChange( aChangedValue );
    GC gc = null;
    boolean recalc = false;
    try {
      if( aChangedValue.hasKey( TFI_TEXT.id() ) ) {
        String str = aChangedValue.getStr( TFI_TEXT.id() );
        str = str.replace( "\r\n", "\n" ); //$NON-NLS-1$//$NON-NLS-2$
        str = str.replace( "\\n", "\n" ); //$NON-NLS-1$//$NON-NLS-2$
        subStrings = str.split( "\n" ); //$NON-NLS-1$
        recalc = true;
      }
      if( aChangedValue.hasKey( PROPID_FONT ) ) {
        IFontInfo fi = props().getValobj( PROPID_FONT );
        font = fontManager().getFont( fi );
        recalc = true;
      }
      if( recalc ) {
        gc = new GC( getShell() );
        gc.setFont( font );
        textExtents.clear();
        for( String s : subStrings ) {
          textExtents.add( gc.textExtent( s ) );
        }
      }
    }
    finally {
      if( gc != null ) {
        gc.dispose();
      }
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
      // gc = new GC( getShell() );
      // IFontInfo fi = props().getValobj( PROPID_FONT );
      // if( aValuesToSet.hasKey( PROPID_FONT ) ) {
      // fi = aValuesToSet.getValobj( PROPID_FONT );
      // }
      // font = fontManager().getFont( fi );
      // gc.setFont( font );
      // String text = props().getStr( PROPID_TEXT );
      // if( aValuesToSet.hasKey( PROPID_TEXT ) ) {
      // text = aValuesToSet.getStr( PROPID_TEXT );
      // }
      // if( text.isEmpty() ) {
      // text = " "; //$NON-NLS-1$
      // }
      // Point p = gc.textExtent( text );
      // int textWidth = p.x;
      // int textHeight = p.y;

      ID2Point d2p = getPackedSize( width, height );
      int textWidth = d2p.intX();
      int textHeight = d2p.intY();
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
  public ID2Point getPackedSize( double aWidth, double aHeight ) {
    TsBorderInfo bi = props().getValobj( PROPID_BORDER_INFO );
    if( bi.kind() == ETsBorderKind.NONE ) {
      Point p = calcTecxtSize();
      return new D2Point( p.x, p.y );
    }
    return super.getPackedSize( aWidth, aHeight );
  }

  @Override
  protected void beforePack() {
    props().propsEventer().pauseFiring();
    props().setInt( PROPID_LEFT_INDENT, 0 );
    props().setInt( PROPID_TOP_INDENT, 0 );
    props().setInt( PROPID_RIGHT_INDENT, 0 );
    props().setInt( PROPID_BOTTOM_INDENT, 0 );
    props().propsEventer().resumeFiring( true );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  Point calcTecxtSize() {
    int maxW = 0;
    int maxH = 0;
    for( int i = 0; i < subStrings.length; i++ ) {
      Point te = textExtents.get( i );
      if( te.x > maxW ) {
        maxW = te.x;
      }
      maxH += te.y;
    }
    maxH += (int)((subStrings.length - 1) * linesGap);
    return new Point( maxW, maxH );
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

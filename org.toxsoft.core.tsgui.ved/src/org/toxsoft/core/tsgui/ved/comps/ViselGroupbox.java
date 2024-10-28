package org.toxsoft.core.tsgui.ved.comps;

import static org.toxsoft.core.tsgui.bricks.tin.tti.ITtiConstants.*;
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
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tsgui.ved.editor.palette.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Визуальный элемент - "Обрамление группы (Groupbox)".
 * <p>
 *
 * @author vs
 */
public class ViselGroupbox
    extends VedAbstractVisel {

  /**
   * The VISEL factory ID.
   */
  public static final String FACTORY_ID = VED_ID + ".visel.Groupbox"; //$NON-NLS-1$

  static final IDataDef PROP_IS_ACTOR_MANDATORY = DataDef.create( PROPID_IS_ACTOR_MANDATORY, EAtomicType.BOOLEAN, //
      TSID_NAME, STR_IS_ACTOR_MANDATORY, //
      TSID_DESCRIPTION, STR_IS_ACTOR_MANDATORY_D, //
      TSID_DEFAULT_VALUE, AvUtils.AV_FALSE //
  );

  static final ITinFieldInfo TFI_IS_ACTOR_NEEDED = new TinFieldInfo( PROP_IS_ACTOR_MANDATORY, TTI_AT_BOOLEAN );

  /**
   * The VISEL factory singleton.
   */
  public static final IVedViselFactory FACTORY = new VedAbstractViselFactory( FACTORY_ID, //
      TSID_NAME, STR_VISEL_GROUPBOX, //
      TSID_DESCRIPTION, STR_VISEL_GROUPBOX_D, //
      TSID_ICON_ID, ICONID_VISEL_GROUPBOX ) {

    // ------------------------------------------------------------------------------------
    // VedAbstractViselFactory
    //

    @Override
    protected VedAbstractVisel doCreate( IVedItemCfg aCfg, VedScreen aScreen ) {
      return new ViselGroupbox( aCfg, propDefs(), aScreen );
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
      // fields.add( TFI_VER_ALIGNMENT );

      fields.add( TFI_FG_COLOR );

      fields.add( TFI_LEFT_INDENT );
      // fields.add( TFI_TOP_INDENT );
      fields.add( TFI_RIGHT_INDENT );
      // fields.add( TFI_BOTTOM_INDENT );

      fields.add( TFI_BK_FILL );
      fields.add( TFI_BORDER_INFO );
      fields.add( TFI_ZOOM );
      fields.add( TFI_ANGLE );
      fields.add( TFI_IS_ACTIVE );
      fields.add( TinFieldInfo.makeCopy( TFI_TRANSFORM, ITinWidgetConstants.PRMID_IS_HIDDEN, AV_TRUE ) );
      fields.add( TFI_IS_ACTOR_NEEDED );
      return new PropertableEntitiesTinTypeInfo<>( fields, ViselGroupbox.class );
    }

    @Override
    protected StridablesList<IVedItemsPaletteEntry> doCreatePaletteEntries() {
      VedItemCfg cfg = new VedItemCfg( id(), kind(), id(), IOptionSet.NULL );
      OptionSetUtils.initOptionSet( cfg.propValues(), propDefs() );
      cfg.propValues().setStr( PROPID_TEXT, "Name" ); //$NON-NLS-1$
      cfg.propValues().setDouble( PROPID_WIDTH, 200.0 );
      cfg.propValues().setDouble( PROPID_HEIGHT, 150.0 );
      cfg.propValues().setValobj( PROPID_HOR_ALIGNMENT, EHorAlignment.CENTER );
      cfg.propValues().setBool( PROPID_IS_ACTOR_MANDATORY, false );
      IVedItemsPaletteEntry pent = new VedItemPaletteEntry( id(), params(), cfg );
      return new StridablesList<>( pent );
    }

  };

  private Font font = null;

  private D2RectangleEdit textRect = new D2RectangleEdit( 0, 0, 0, 0 );

  private TsFillInfo fillInfo = TsFillInfo.NONE;

  /**
   * Constructor.
   *
   * @param aConfig {@link IVedItemCfg} - the item config
   * @param aPropDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - properties definitions
   * @param aVedScreen {@link VedScreen} - the owner screen
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public ViselGroupbox( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
  }

  // ------------------------------------------------------------------------------------
  // VedAbstractVisel
  //

  @Override
  public void paint( ITsGraphicsContext aPaintContext ) {
    ID2Rectangle r = bounds();

    if( font == null ) {
      IFontInfo fi = props().getValobj( PROPID_FONT );
      font = fontManager().getFont( fi );
    }
    aPaintContext.gc().setFont( font );

    String text = props().getStr( PROPID_TEXT );

    aPaintContext.gc().setAlpha( 255 );
    aPaintContext.setBorderInfo( props().getValobj( PROPID_BORDER_INFO ) );
    double dy = textRect.height() / 2.;
    aPaintContext.drawRectBorder( 0, (int)(dy), (int)r.width(), (int)(r.height() - dy) );

    if( !text.isEmpty() ) {
      text = " " + text + " "; //$NON-NLS-1$ //$NON-NLS-2$
      // aPaintContext.setFillInfo( props().getValobj( PROPID_BK_FILL ) );
      aPaintContext.setFillInfo( fillInfo );
      aPaintContext.fillRect( (int)textRect.x1(), (int)textRect.y1(), (int)textRect.width(), (int)textRect.height() );
      aPaintContext.gc().setBackgroundPattern( null );
      aPaintContext.setForegroundRgba( props().getValobj( PROPID_FG_COLOR ) );
      aPaintContext.gc().drawText( text, (int)textRect.x1(), (int)textRect.y1(), fillInfo == TsFillInfo.NONE );
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
      if( aValuesToSet.hasKey( PROPID_BK_FILL ) ) {
        fillInfo = aValuesToSet.getValobj( PROPID_BK_FILL );
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
        textRect.moveTo( 0, 0 );
        textRect.setSize( 0, 0 );
      }
      else {
        text = " " + text + " "; //$NON-NLS-1$ //$NON-NLS-2$
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
        textRect.moveTo( calcTextX( p, bounds() ), 0 );
        textRect.setSize( textWidth, textHeight );
      }
    }
    finally {
      if( gc != null ) {
        gc.dispose();
      }
    }
  }

  // @Override
  // public boolean isYours( double aX, double aY ) {
  // if( textRect.contains( aX, aY ) ) {
  // return true;
  // }
  // boolean result = super.isYours( aX, aY );
  // if( result ) {
  // double dx = Math.min( aX, props().getDouble( PROPID_WIDTH ) - aX );
  // if( Math.abs( dx ) < 6 ) {
  // return true;
  // }
  // double dy = Math.min( aY - textRect.height() / 2., props().getDouble( PROPID_HEIGHT ) - aY );
  // if( Math.abs( dy ) < 6 ) {
  // return true;
  // }
  // }
  // return false;
  // }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  double calcTextX( Point aTextExtent, ID2Rectangle aBounds ) {
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

  @SuppressWarnings( "unused" )
  double calcTextY( Point aTextExtent, ID2Rectangle aBounds ) {
    return 0.0;
    // int y = 0;
    // EVerAlignment va = props().getValobj( PROPID_VER_ALIGNMENT );
    // y = switch( va ) {
    // case TOP -> 0 + props().getInt( PROPID_TOP_INDENT );
    // case FILL, CENTER -> (int)((aBounds.height() - aTextExtent.y) / 2.);
    // case BOTTOM -> (int)aBounds.height() - aTextExtent.y - props().getInt( PROPID_BOTTOM_INDENT );
    // default -> throw new TsNotAllEnumsUsedRtException();
    // };
    // return y;
  }

}

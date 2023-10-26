package org.toxsoft.core.tsgui.ved.comps;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.comps.ITsResources.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.fonts.*;
import org.toxsoft.core.tsgui.ved.editor.palette.*;
import org.toxsoft.core.tsgui.ved.incub.tsg.*;
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
      fields.add( TFI_TEXT );
      fields.add( TFI_FG_COLOR );
      fields.add( TFI_FONT );
      fields.add( TFI_HOR_ALIGNMENT );
      fields.add( TFI_VER_ALIGNMENT );
      fields.add( TFI_BK_FILL );
      fields.add( TFI_BORDER_INFO );
      fields.add( TFI_TRANSFORM );
      fields.add( TFI_IS_ACTIVE );
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
  }

  // ------------------------------------------------------------------------------------
  // VedAbstractVisel
  //

  @Override
  public void paint( ITsGraphicsContext aPaintContext ) {
    ID2Rectangle r = bounds();

    aPaintContext.setFillInfo( props().getValobj( PROPID_BK_FILL ) );
    aPaintContext.fillRect( (int)r.x1(), (int)r.y1(), (int)r.width(), (int)r.height() );

    if( font == null ) {
      IFontInfo fi = props().getValobj( PROPID_FONT );
      font = fontManager().getFont( fi );
    }
    aPaintContext.gc().setFont( font );
    aPaintContext.setForegroundRgba( props().getValobj( PROPID_FG_COLOR ) );

    String text = props().getStr( PROPID_TEXT );
    Point p = aPaintContext.gc().textExtent( text );
    int x = (int)r.x1();
    int y = (int)r.y1();

    EHorAlignment ha = props().getValobj( PROPID_HOR_ALIGNMENT );
    x = switch( ha ) {
      case LEFT -> (int)r.x1();
      case FILL, CENTER -> (int)(r.x1() + (r.width() - p.x) / 2.);
      case RIGHT -> (int)r.x1() + (int)r.width() - p.x;
      default -> throw new TsNotAllEnumsUsedRtException();
    };

    EVerAlignment va = props().getValobj( PROPID_VER_ALIGNMENT );
    y = switch( va ) {
      case TOP -> (int)r.y1();
      case FILL, CENTER -> (int)(r.y1() + (r.height() - p.y) / 2.);
      case BOTTOM -> (int)r.y1() + (int)r.height() - p.y;
      default -> throw new TsNotAllEnumsUsedRtException();
    };

    aPaintContext.gc().setBackgroundPattern( null );
    aPaintContext.gc().drawText( text, x, y );
    aPaintContext.gc().setAlpha( 255 );
    aPaintContext.setBorderInfo( props().getValobj( PROPID_BORDER_INFO ) );
    aPaintContext.drawRectBorder( (int)r.x1(), (int)r.y1(), (int)r.width(), (int)r.height() );
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

}

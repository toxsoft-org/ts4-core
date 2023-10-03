package org.toxsoft.core.tsgui.ved.comps;

import static org.toxsoft.core.tsgui.ved.comps.LabelViselFactory.*;
import static org.toxsoft.core.tsgui.ved.impl.VedAbstractViselFactory.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.fonts.*;
import org.toxsoft.core.tsgui.ved.api.cfg.*;
import org.toxsoft.core.tsgui.ved.devel.*;
import org.toxsoft.core.tsgui.ved.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Визуальный элемент - "Текст".
 * <p>
 *
 * @author vs
 */
public class LabelVisel
    extends VedAbstractVisel {

  Font font = null;

  /**
   * Конструктор.<br>
   *
   * @param aConfig {@link IVedItemCfg} - configuration data of the individual visel
   * @param aPropDefs IStridablesList&lt;IDataDef> - список описаний данных свойств визеля
   * @param aTsContext {@link ITsGuiContext} - соответствующий контекст
   */
  public LabelVisel( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, ITsGuiContext aTsContext ) {
    super( aConfig, aPropDefs, aTsContext );
    setLocation( aConfig.propValues().getDouble( FID_VISEL_X ), aConfig.propValues().getDouble( FID_VISEL_Y ) );
    setSize( aConfig.propValues().getDouble( FID_VISEL_WIDTH ), aConfig.propValues().getDouble( FID_VISEL_HEIGHT ) );
  }

  // ------------------------------------------------------------------------------------
  // VedAbstractVisel
  //

  @Override
  public void paint( ITsGraphicsContext aPaintContext ) {
    ITsRectangle r = bounds();

    if( !props().getBool( FID_TRANSPARENT ) ) {
      aPaintContext.setFillInfo( props().getValobj( FID_FILL_INFO ) );
      aPaintContext.fillRect( r.x1(), r.y1(), r.width(), r.height() );
    }

    if( font == null ) {
      IFontInfo fi = props().getValobj( FID_FONT );
      font = new Font( aPaintContext.gc().getDevice(), fi.fontName(), fi.fontSize(), fi.getSwtStyle() );
    }
    aPaintContext.gc().setFont( font );
    aPaintContext.setForegroundRgba( props().getValobj( FID_TEXT_COLOR ) );

    String text = props().getStr( FID_TEXT );
    Point p = aPaintContext.gc().textExtent( text );
    int x = r.x1();
    int y = r.y1();

    EHorAlignment ha = props().getValobj( FID_HOR_ALIGNMENT );
    x = switch( ha ) {
      case LEFT -> r.x1();
      case FILL, CENTER -> (int)(r.x1() + (r.width() - p.x) / 2.);
      case RIGHT -> r.x1() + r.width() - p.x;
      default -> throw new TsNotAllEnumsUsedRtException();
    };

    EVerAlignment va = props().getValobj( FID_VER_ALIGNMENT );
    y = switch( va ) {
      case TOP -> r.y1();
      case FILL, CENTER -> (int)(r.y1() + (r.height() - p.y) / 2.);
      case BOTTOM -> r.y1() + r.height() - p.y;
      default -> throw new TsNotAllEnumsUsedRtException();
    };

    aPaintContext.gc().drawText( text, x, y );
    aPaintContext.gc().setAlpha( 255 );
    aPaintContext.setBorderInfo( props().getValobj( FID_BORDER_INFO ) );
    aPaintContext.drawRectBorder( r.x1(), r.y1(), r.width(), r.height() );
  }

  @Override
  protected void doOnPropsChanged() {
    font = null;
  }

}

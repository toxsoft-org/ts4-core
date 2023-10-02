package org.toxsoft.core.tsgui.ved.comps;

import static org.toxsoft.core.tsgui.ved.comps.LabelViselFactory.*;
import static org.toxsoft.core.tsgui.ved.impl.VedAbstractViselFactory.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.api.cfg.*;
import org.toxsoft.core.tsgui.ved.devel.*;
import org.toxsoft.core.tsgui.ved.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;

/**
 * Визуальный элемент - "Текст".
 * <p>
 *
 * @author vs
 */
public class LabelVisel
    extends VedAbstractVisel {

  // ------------------------------------------------------------------------------------
  // VedAbstractVisel
  //

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

  @Override
  public void paint( ITsGraphicsContext aPaintContext ) {
    aPaintContext.setFillInfo( props().getValobj( FID_FILL_INFO ) );
    ITsRectangle r = bounds();
    aPaintContext.fillRect( r.x1(), r.y1(), r.width(), r.height() );

    aPaintContext.setForegroundRgba( props().getValobj( FID_TEXT_COLOR ) );
    aPaintContext.gc().drawText( props().getStr( FID_TEXT ), r.x1(), r.y1() );
    aPaintContext.setBorderInfo( props().getValobj( FID_BORDER_INFO ) );
    aPaintContext.drawRectBorder( r.x1(), r.y1(), r.width(), r.height() );
  }

}

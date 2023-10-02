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
  }

  @Override
  public void paint( ITsGraphicsContext aPaintContext ) {
    aPaintContext.setFillInfo( props().getValobj( FID_FILL_INFO ) );
    ITsRectangle r = bounds();
    aPaintContext.fillRect( r.x1(), r.x2(), r.width(), r.height() );
    aPaintContext.gc().drawText( "FID_D2CONVERSION", r.x1(), r.x2() );
  }

}

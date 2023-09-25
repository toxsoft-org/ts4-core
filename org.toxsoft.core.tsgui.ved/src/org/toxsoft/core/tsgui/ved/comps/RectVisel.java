package org.toxsoft.core.tsgui.ved.comps;

import static org.toxsoft.core.tsgui.ved.impl.VedAbstractViselFactory.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.api.cfg.*;
import org.toxsoft.core.tsgui.ved.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;

public class RectVisel
    extends VedAbstractVisel {

  private Rectangle swtRect = new Rectangle( 0, 0, 1, 1 );

  public RectVisel( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, ITsGuiContext aTsContext ) {
    super( aConfig, aPropDefs, aTsContext );
    setLocation( aConfig.propValues().getDouble( FID_VISEL_X ), aConfig.propValues().getDouble( FID_VISEL_Y ) );
    setSize( aConfig.propValues().getDouble( FID_VISEL_WIDTH ), aConfig.propValues().getDouble( FID_VISEL_HEIGHT ) );
    updateSwtRect();
  }

  @Override
  public void paint( GC aGc ) {
    // TODO Auto-generated method stub

  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private void updateSwtRect() {
    ITsRectangle r = bounds();

    swtRect.x = r.x1();
    swtRect.y = r.y1();
    swtRect.width = r.width();
    swtRect.height = r.height();
  }

}

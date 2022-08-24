package org.toxsoft.core.tsgui.ved.std.comps;

import static org.toxsoft.core.tsgui.ved.std.IVedStdProperties.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tsgui.ved.core.impl.*;
import org.toxsoft.core.tsgui.ved.core.view.*;
import org.toxsoft.core.tsgui.ved.utils.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;

/**
 * {@link IVedComponentView} implementation for {@link VedStdCompBorder}.
 *
 * @author vs
 */
class VedStdCompBorderView
    extends VedAbstractComponentView {

  // --- CACHE cached properties with conversion applied
  private D2BorderOutline outline;
  private TsBorderInfo    borderInfo;
  private double          x, y, w, h;
  // ---

  public VedStdCompBorderView( VedAbstractComponent aComponent, IVedScreen aScreen ) {
    super( aComponent, aScreen );
  }

  // ------------------------------------------------------------------------------------
  // VedAbstractComponentView
  //

  @Override
  public IVedOutline outline() {
    return outline;
  }

  @Override
  protected void doUpdateOnConversionChange() {
    // TODO Auto-generated method stub
  }

  @Override
  protected void doUpdateOnPropertiesChange( String aPropId, IAtomicValue aOldValue, IAtomicValue aNewValue ) {
    x = props().getDouble( PDEF_X );
    y = props().getDouble( PDEF_Y );
    w = props().getDouble( PDEF_WIDTH );
    h = props().getDouble( PDEF_HEIGHT );
    borderInfo = props().getValobj( VedStdCompBorder.PDEF_BORDER_INFO );
    outline = new D2BorderOutline( x, y, w, h, borderInfo.lineInfo().width() );
  }

  @Override
  protected void doPaint( GC aGc, ITsRectangle aPaintBounds ) {
    aGc.setAdvanced( true );
    aGc.setAntialias( SWT.ON );
    Transform oldTransfrom = null;
    Transform t = null;
    ID2Conversion d2conv = getConversion();
    if( d2conv.isConversion() ) {
      oldTransfrom = new Transform( aGc.getDevice() );
      aGc.getTransform( oldTransfrom );

      t = TsGraphicsUtils.conv2transform( d2conv, aGc );

      aGc.setTransform( t );
      t.dispose();
    }

    TsGraphicsUtils.drawBorder( aGc, borderInfo, new TsRectangle( (int)x, (int)y, (int)w, (int)h ), colorManager() );

    if( oldTransfrom != null ) { // восстановим старый transform
      aGc.setTransform( oldTransfrom );
      oldTransfrom.dispose();
    }
  }

}

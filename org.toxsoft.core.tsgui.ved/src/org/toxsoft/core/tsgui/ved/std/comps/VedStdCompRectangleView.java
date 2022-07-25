package org.toxsoft.core.tsgui.ved.std.comps;

import static org.toxsoft.core.tsgui.ved.std.IVedStdProperties.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tsgui.ved.impl.*;
import org.toxsoft.core.tsgui.ved.utils.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.geometry.*;

/**
 * {@link IVedComponentView} implementation for {@link VedStdCompRectangle}.
 *
 * @author hazard157
 */
class VedStdCompRectangleView
    extends VedAbstractComponentView {

  // --- CACHE cached properties with conversion applied
  private D2RectOutline outline;
  private Color         bgColor;
  private Color         fgColor;
  int                   lineWidth;
  double                x, y, w, h;
  // ---

  public VedStdCompRectangleView( VedAbstractComponent aComponent, IVedScreen aScreen ) {
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
    outline = new D2RectOutline( x, y, w, h );

    // TODO respect conversion!
    RGB fgRgb = props().getValobj( PDEF_FG_COLOR );
    fgColor = colorManager().getColor( fgRgb );
    RGB bgRgb = props().getValobj( PDEF_BG_COLOR );
    bgColor = colorManager().getColor( bgRgb );
    lineWidth = 2;
    x = (int)props().getDouble( PDEF_X );
    y = (int)props().getDouble( PDEF_Y );
    w = (int)props().getDouble( PDEF_WIDTH );
    h = (int)props().getDouble( PDEF_HEIGHT );
  }

  @Override
  protected void doPaint( GC aGc, ITsRectangle aPaintBounds ) {
    // TODO respect conversion!
    // TODO filling properties
    // TODO line drawing properties
    int xi = (int)x;
    int yi = (int)y;
    int wi = (int)w;
    int hi = (int)h;
    aGc.setBackground( bgColor );
    aGc.fillRectangle( xi, yi, wi, hi );
    aGc.setForeground( fgColor );
    aGc.setLineWidth( lineWidth );
    aGc.drawRectangle( xi, yi, wi, hi );
  }

}

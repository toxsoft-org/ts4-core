package org.toxsoft.core.tsgui.ved.comps;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.gc.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;

public class CheckboxRenderer
    extends AbstractButtonRenderer {

  TsFillInfo fillInfo = null;

  TsFillInfo pressedFillInfo = null;

  TsFillInfo disableFillInfo = new TsFillInfo( new RGBA( 164, 164, 164, 255 ) );

  protected CheckboxRenderer( ViselCheckbox aButton ) {
    super( aButton );
  }

  // ------------------------------------------------------------------------------------
  // AbstractButtonRenderer
  //

  @Override
  protected void doUpdate() {
    // nop
  }

  @Override
  protected void paintBackground( ITsGraphicsContext aPaintContext ) {
    aPaintContext.gc().setFont( font );
    Point p = aPaintContext.gc().textExtent( buttonText() );

    int x = 0;
    int y = (p.y - 16) / 2;
    if( y < 0 ) {
      y = 0;
    }
    aPaintContext.gc().setBackground( colorManager().getColor( ETsColor.WHITE ) );
    aPaintContext.gc().fillRectangle( x, y, 16, 16 );
    aPaintContext.gc().setForeground( colorManager().getColor( ETsColor.BLACK ) );
    aPaintContext.gc().setLineWidth( 1 );
    aPaintContext.gc().drawRectangle( x, y, 16, 16 );

    if( buttonState() == EButtonViselState.PRESSED ) {
      paintCheck( x, y, aPaintContext.gc() );
    }
  }

  @Override
  void drawText( ITsGraphicsContext aPaintContext ) {
    aPaintContext.gc().setFont( font );
    Point p = aPaintContext.gc().textExtent( buttonText() );
    int y = (16 - p.y) / 2;
    if( y < 0 ) {
      y = 0;
    }
    aPaintContext.gc().drawText( buttonText(), 22, 0, true );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  void paintCheck( int aX, int aY, GC aGc ) {
    int[] points = new int[6];
    points[0] = aX + 2 + 1;
    points[1] = aY + 7;
    points[2] = aX + 6 + 1;
    points[3] = aY + 12;
    points[4] = aX + 13 + 1;
    points[5] = aY + 4;
    aGc.setLineWidth( 2 );
    aGc.drawPolyline( points );
  }

}

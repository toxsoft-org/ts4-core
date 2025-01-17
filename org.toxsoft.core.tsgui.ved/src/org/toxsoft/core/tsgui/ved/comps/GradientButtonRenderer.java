package org.toxsoft.core.tsgui.ved.comps;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.gc.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tsgui.ved.incub.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * Градиентный отрисовщик кнопки.
 * <p>
 *
 * @author vs
 */
public class GradientButtonRenderer
    extends AbstractButtonRenderer {

  TsFillInfo fillInfo = null;

  TsFillInfo pressedFillInfo = null;

  TsFillInfo hoveredFillInfo = null;

  TsFillInfo selectedFillInfo = null;

  TsFillInfo disableFillInfo = new TsFillInfo( new RGBA( 164, 164, 164, 255 ) );

  protected GradientButtonRenderer( ViselButton aButton ) {
    super( aButton );
  }

  // ------------------------------------------------------------------------------------
  // AbstractButtonRenderer
  //

  @Override
  protected void doUpdate() {
    RGBA sc = new RGBA( 220, 220, 220, 255 );
    RGBA ec = new RGBA( 190, 190, 190, 255 );

    RGB rgb = GradientUtils.tuneBrightness( bkRgba.rgb, 0.2 );
    sc = new RGBA( rgb.red, rgb.green, rgb.blue, 255 );
    rgb = GradientUtils.tuneBrightness( bkRgba.rgb, -0.2 );
    ec = new RGBA( rgb.red, rgb.green, rgb.blue, 255 );

    Pair<Double, RGBA> p1 = new Pair<>( Double.valueOf( 0 ), sc );
    Pair<Double, RGBA> p2 = new Pair<>( Double.valueOf( 100 ), ec );
    IListEdit<Pair<Double, RGBA>> fractions = new ElemArrayList<>();
    fractions.add( p1 );
    fractions.add( p2 );
    LinearGradientInfo lgi = new LinearGradientInfo( fractions, 90 );
    fillInfo = new TsFillInfo( new TsGradientFillInfo( lgi ) );

    p1 = new Pair<>( Double.valueOf( 0 ), ec );
    p2 = new Pair<>( Double.valueOf( 100 ), sc );
    fractions = new ElemArrayList<>();
    fractions.add( p1 );
    fractions.add( p2 );
    lgi = new LinearGradientInfo( fractions, 90 );
    pressedFillInfo = new TsFillInfo( new TsGradientFillInfo( lgi ) );

    rgb = GradientUtils.tuneBrightness( selRgba.rgb, 0.2 );
    sc = new RGBA( rgb.red, rgb.green, rgb.blue, 255 );
    rgb = GradientUtils.tuneBrightness( selRgba.rgb, -0.2 );
    ec = new RGBA( rgb.red, rgb.green, rgb.blue, 255 );
    p1 = new Pair<>( Double.valueOf( 0 ), ec );
    p2 = new Pair<>( Double.valueOf( 100 ), sc );
    fractions = new ElemArrayList<>();
    fractions.add( p1 );
    fractions.add( p2 );
    lgi = new LinearGradientInfo( fractions, 90 );
    selectedFillInfo = new TsFillInfo( new TsGradientFillInfo( lgi ) );

    hoveredFillInfo = new TsFillInfo( hvRgba );
  }

  @Override
  protected void paintBackground( ITsGraphicsContext aPaintContext ) {
    TsFillInfo fi = fillInfo;

    EButtonViselState state = buttonState();
    aPaintContext.gc().setForeground( colorManager().getColor( new RGB( 0, 0, 0 ) ) );
    if( state == EButtonViselState.DISABLED ) {
      aPaintContext.gc().setForeground( colorManager().getColor( new RGB( 96, 96, 96 ) ) );
      aPaintContext.gc().setBackground( colorManager().getColor( new RGB( 164, 164, 164 ) ) );
      fi = disableFillInfo;
    }
    else {
      if( hovered ) {
        fi = hoveredFillInfo;
      }
      if( state == EButtonViselState.SELECTED ) {
        fi = selectedFillInfo;
      }
      if( state == EButtonViselState.PRESSED ) {
        fi = pressedFillInfo;
      }
    }

    int arcW = 8;
    int arcH = 8;
    aPaintContext.setFillInfo( fi );
    aPaintContext.fillRoundRect( 0, 0, swtRect.width, swtRect.height, arcW, arcH );
    aPaintContext.setLineInfo( lineInfo );

    aPaintContext.drawRoundRect( 0, 0, swtRect.width, swtRect.height, arcW, arcH );
  }

  @Override
  public ID2Point getPackedSize( double aWidth, double aHeight ) {
    ID2Rectangle r = button.bounds();
    return new D2Point( r.width(), r.height() );
  }

}

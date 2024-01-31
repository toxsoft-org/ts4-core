package org.toxsoft.core.tsgui.ved.comps;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.graphics.gc.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * "Прикольный" отрисовщик кнопки (тест).
 * <p>
 *
 * @author vs
 */
public class CoolButtonRenderer
    extends AbstractButtonRenderer {

  // Pair[left=0.0, right=RGBA {140, 140, 140, 255}]
  // Pair[left=7.738095238095238, right=RGBA {104, 104, 104, 255}]
  // Pair[left=92.55952380952381, right=RGBA {70, 70, 70, 255}]
  // Pair[left=100.0, right=RGBA {72, 72, 72, 255}]

  // 115

  double arcW = 32.;
  double arcH = 32.;

  double lineWidth = 4;

  double zoom = 1.0;

  TsFillInfo fillInfo = null;

  Image shadowImage = null;

  Color fgColor;

  @SuppressWarnings( "boxing" )
  protected CoolButtonRenderer( ViselButton aButton, double aZoom ) {
    super( aButton );
    Pair<Double, RGBA> p1 = new Pair<>( 0.0, new RGBA( 140, 140, 140, 255 ) );
    Pair<Double, RGBA> p2 = new Pair<>( 8.5, new RGBA( 104, 104, 104, 255 ) );
    // Pair<Double, RGBA> p2 = new Pair<>( 7.738095238095238, new RGBA( 104, 104, 104, 255 ) );
    // Pair<Double, RGBA> p2 = new Pair<>( aZoom * 7.738095238095238, new RGBA( 104, 104, 104, 255 ) );
    // Pair<Double, RGBA> p3 = new Pair<>( aZoom * 92.55952380952381, new RGBA( 70, 70, 70, 255 ) );
    Pair<Double, RGBA> p3 = new Pair<>( 92.55952380952381, new RGBA( 70, 70, 70, 255 ) );
    // Pair<Double, RGBA> p3 = new Pair<>( 100.0 - aZoom * 92.55952380952381, new RGBA( 70, 70, 70, 255 ) );
    Pair<Double, RGBA> p4 = new Pair<>( 100.0, new RGBA( 72, 72, 72, 255 ) );

    IListEdit<Pair<Double, RGBA>> fractions = new ElemArrayList<>();
    fractions.add( p1 );
    fractions.add( p2 );
    fractions.add( p3 );
    fractions.add( p4 );
    LinearGradientInfo lgi = new LinearGradientInfo( fractions, 90 );
    fillInfo = new TsFillInfo( new TsGradientFillInfo( lgi ) );

    fgColor = colorManager().getColor( new RGB( 0, 0, 0 ) );

    zoom = aZoom;
  }

  // ------------------------------------------------------------------------------------
  // AbstractButtonRenderer
  //

  @Override
  protected void doUpdate() {
    if( shadowImage != null ) {
      shadowImage.dispose();
    }
    Display display = Display.getDefault();
    int fl = ShadowUtils.movingAverageFrameLength( 5, 3 ); // длина скользящего буфера

    int width = (int)Math.floor( swtRect.width + 4 * fl );
    int height = (int)Math.floor( swtRect.height + 4 * fl );
    Image image = ShadowUtils.createTransparentImage( display, width, height, 255 );

    GC gc = ShadowUtils.createGcForShadow( image, fl, 127, 0.95, 1.0 );

    int arcw = (int)(arcW * zoom);
    int arch = (int)(arcH * zoom);

    if( arcw < 8 ) {
      arcw = 8;
    }

    if( arch < 8 ) {
      arch = 8;
    }
    gc.setBackground( new Color( 255, 255, 255 ) );
    gc.fillRoundRectangle( 0, 0, swtRect.width, swtRect.height, arcw, arch );

    shadowImage = ShadowUtils.image2shadow( image, fl, 220, 3 );

    gc.dispose();
    image.dispose();
  }

  @Override
  protected void paintBackground( ITsGraphicsContext aPaintContext ) {
    TsFillInfo fi = fillInfo;

    aPaintContext.gc().drawImage( shadowImage, swtRect.x - 1, swtRect.y + 5 );

    aPaintContext.gc().setLineWidth( (int)(lineWidth * zoom) );
    int arcw = (int)(arcW * zoom);
    int arch = (int)(arcH * zoom);

    if( arcw < 8 ) {
      arcw = 8;
    }

    if( arch < 8 ) {
      arch = 8;
    }

    aPaintContext.setFillInfo( fi );
    aPaintContext.fillRoundRect( swtRect.x, swtRect.y, swtRect.width, swtRect.height, arcw, arch );

    // aPaintContext.setLineInfo( TsLineInfo.ofWidth( (int)(zoom * lineWidth) ) );
    aPaintContext.setLineInfo( TsLineInfo.ofWidth( 2 ) );
    aPaintContext.gc().setForeground( fgColor );
    aPaintContext.drawRoundRect( swtRect.x, swtRect.y, swtRect.width, swtRect.height, arcw, arch );
  }

}

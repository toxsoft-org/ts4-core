package org.toxsoft.core.tsgui.ved.incub;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;

public class GradientUtils {

  public static class HSV {

    double hue;
    double saturation;
    double value;
  }

  public static HSV rgb2Hsv( RGB aRgb ) {
    HSV hsv = new HSV();

    double R1 = aRgb.red / 255.;
    double G1 = aRgb.green / 255.;
    double B1 = aRgb.blue / 255.;

    double Cmax = Math.max( Math.max( R1, G1 ), B1 );
    double Cmin = Math.min( Math.min( R1, G1 ), B1 );

    double delta = Cmax - Cmin;

    double h = 0;
    if( delta != 0 ) {
      if( Cmax == R1 ) {
        h = 60 * (((G1 - B1) / delta) % 6);
      }
      if( Cmax == G1 ) {
        h = 60 * (((B1 - R1) / delta) + 2);
      }
      if( Cmax == B1 ) {
        h = 60 * (((R1 - G1) / delta) + 4);
      }
    }

    double s = 0;
    if( Cmax != 0 ) {
      s = delta / Cmax;
    }

    double v = Cmax;

    hsv.hue = h;
    hsv.saturation = s;
    hsv.value = v;

    return hsv;
  }

  public static RGB Hsv2Rgb( HSV aHsv ) {
    RGB rgb = new RGB( 0, 0, 0 );

    double c = aHsv.value * aHsv.saturation;
    double x = c * (1.0 - Math.abs( (aHsv.hue / 60) % 2 - 1 ));
    double m = aHsv.value - c;

    double R1 = 0;
    double G1 = 0;
    double B1 = 0;

    if( aHsv.hue >= 0 && aHsv.hue < 60 ) {
      R1 = c;
      G1 = x;
      B1 = 0;
    }
    if( aHsv.hue >= 60 && aHsv.hue < 120 ) {
      R1 = x;
      G1 = c;
      B1 = 0;
    }
    if( aHsv.hue >= 120 && aHsv.hue < 180 ) {
      R1 = 0;
      G1 = c;
      B1 = x;
    }
    if( aHsv.hue >= 180 && aHsv.hue < 240 ) {
      R1 = 0;
      G1 = x;
      B1 = c;
    }
    if( aHsv.hue >= 240 && aHsv.hue < 300 ) {
      R1 = x;
      G1 = 0;
      B1 = c;
    }
    if( aHsv.hue >= 300 && aHsv.hue < 360 ) {
      R1 = c;
      G1 = 0;
      B1 = x;
    }

    rgb.red = (int)((R1 + m) * 255);
    rgb.green = (int)((G1 + m) * 255);
    rgb.blue = (int)((B1 + m) * 255);

    return rgb;
  }

  public static IList<Pair<Double, RGBA>> halfSphereFractions( RGBA aRgba ) {
    IListEdit<Pair<Double, RGBA>> fractions = new ElemArrayList<>();

    HSV hsv = rgb2Hsv( aRgba.rgb );

    hsv.value += 0.3;
    if( hsv.value > 1 ) {
      double newSat = 0.6 - (hsv.value - 1);
      if( newSat > hsv.saturation ) {
        hsv.value = 1;
        hsv.saturation = 0;
        RGB rgb = Hsv2Rgb( hsv );
        RGBA rgba = new RGBA( rgb.red, rgb.green, rgb.blue, 255 );
        fractions.add( new Pair<>( Double.valueOf( 0 ), rgba ) );
        int r = (int)(aRgba.rgb.red * 0.85);
        int g = (int)(aRgba.rgb.green * 0.85);
        int b = (int)(aRgba.rgb.blue * 0.85);
        fractions.add( new Pair<>( Double.valueOf( 70 ), new RGBA( r, g, b, 255 ) ) );
        fractions.add( new Pair<>( Double.valueOf( 100 ), aRgba ) );
        return fractions;
      }

      hsv.saturation = newSat;
      hsv.value = 1;
      if( hsv.saturation < 0 ) {
        hsv.saturation = 0;
      }
    }
    RGB rgb = Hsv2Rgb( hsv );
    RGBA rgba = new RGBA( rgb.red, rgb.green, rgb.blue, 255 );
    fractions.add( new Pair<>( Double.valueOf( 0 ), rgba ) );

    // fractions.add( new Pair<>( Double.valueOf( 40 ), rgba ) );

    hsv = rgb2Hsv( aRgba.rgb );
    hsv.value = hsv.value / 1.05;
    if( hsv.value < 0 ) {
      hsv.value = 0;
    }
    rgb = Hsv2Rgb( hsv );
    rgba = new RGBA( rgb.red, rgb.green, rgb.blue, 255 );
    fractions.add( new Pair<>( Double.valueOf( 60 ), rgba ) );
    fractions.add( new Pair<>( Double.valueOf( 100 ), aRgba ) );

    return fractions;
  }

  public static TsGradientFillInfo halfSphereFillInfo( RGBA aRgba ) {
    return new TsGradientFillInfo( new RadialGradientInfo( 40, 40, halfSphereFractions( aRgba ) ) );
  }

  private GradientUtils() {
    // nop
  }
}

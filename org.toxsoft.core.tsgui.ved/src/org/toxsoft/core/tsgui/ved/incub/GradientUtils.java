package org.toxsoft.core.tsgui.ved.incub;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * Вспомогательные метода создания градиентов.
 * <p>
 *
 * @author vs
 */
public class GradientUtils {

  /**
   * Кодировка цвета HSV
   *
   * @author vs
   */
  public static class HSV {

    double hue;        // цветовой тон (красный, оранжевый, сине-зеленый и т.д.)
    double saturation; // насыщенность
    double value;      // яркость (освещенность)
  }

  /**
   * Преобразует компоненты цвета из кодировки RGB в HSV.
   *
   * @param aRgb - компоненты цвета в кодировке RGB
   * @return HSV - компоненты цвета в кодировке HSV
   */
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

  /**
   * Преобразует компоненты цвета из кодировки HSV в RGB.
   *
   * @param aHsv - компоненты цвета в кодировке HSV
   * @return RGB - компоненты цвета в кодировке RGB
   */
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

  /**
   * Подстраивает яркость в соответствии с переданным коеффициентом.
   *
   * @param aSource RGB - компоненты цвета
   * @param aFactor double - коэффициент изменения яркости
   * @return RGB - подстроенные компоненты цвета
   */
  public static RGB tuneBrightness( RGB aSource, double aFactor ) {
    HSV hsv = rgb2Hsv( aSource );
    hsv.value += aFactor;
    if( hsv.value > 1 ) {
      double newSat = 0.6 - (hsv.value - 1);
      if( newSat > hsv.saturation ) {
        hsv.saturation = 0;
      }
      else {
        hsv.saturation = newSat;
      }
      hsv.value = 1;
      if( hsv.saturation < 0 ) {
        hsv.saturation = 0;
      }
    }
    return Hsv2Rgb( hsv );
  }

  /**
   * Возвращает список фракций для градиента popup сообщения.
   *
   * @param aRgba GRBA - компонент цвета
   * @return IList&lt;Pair&lt;Double, RGBA>> - список фракций для градиента имитирующего полусферу
   */
  public static IList<Pair<Double, RGBA>> baloonFractions( RGBA aRgba ) {
    IListEdit<Pair<Double, RGBA>> fractions = new ElemArrayList<>();
    fractions.add( new Pair<>( Double.valueOf( 0 ), aRgba ) );
    RGB rgb = tuneBrightness( aRgba.rgb, -0.3 );
    fractions.add( new Pair<>( Double.valueOf( 100 ), new RGBA( rgb.red, rgb.green, rgb.blue, 255 ) ) );
    return fractions;
  }

  /**
   * Возвращает параметры заливки для popup сообщения.
   *
   * @param aRgba RGBA - кмпоненты цвета
   * @return {@link TsGradientFillInfo} - параметры заливки
   */
  public static TsGradientFillInfo baloonFillInfo( RGBA aRgba ) {
    return new TsGradientFillInfo( new RadialGradientInfo( 20, 20, baloonFractions( aRgba ) ) );
  }

  /**
   * Возвращает список фракций для градиента имитирующего полусферу.
   *
   * @param aRgba GRBA - компонент цвета
   * @return IList&lt;Pair&lt;Double, RGBA>> - список фракций для градиента имитирующего полусферу
   */
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

  /**
   * Возвращает параметры заливки имитирующей полусферу.
   *
   * @param aRgba RGBA - кмпоненты цвета
   * @return {@link TsGradientFillInfo} - параметры заливки
   */
  public static TsGradientFillInfo halfSphereFillInfo( RGBA aRgba ) {
    return new TsGradientFillInfo( new RadialGradientInfo( 40, 40, halfSphereFractions( aRgba ) ) );
  }

  /**
   * Преобразует переданный RGB в градации серого.
   *
   * @param aRgb RGB - компоненты цвета
   * @return RGB - градации серого
   */
  public static RGB rgb2gray( RGB aRgb ) {
    int y = (int)Math.round( 0.2126 * aRgb.red + 0.7152 * aRgb.green + 0.0722 * aRgb.blue );
    return new RGB( y, y, y );
  }

  /**
   * Рисует "шахматную доску".<br>
   * Используется в качестве фона, для прозрачных элементов.
   *
   * @param aGc {@link GC} - графический контект
   * @param aRect {@link Rectangle} - размер доски
   * @param aColor1 {@link Color} - цвет "светлых" клеток
   * @param aColor2 {@link Color} - цвет "темных" клеток
   * @param aCellSize int - размер клетки в пикселях
   */
  public static void drawChessBoard( GC aGc, Rectangle aRect, Color aColor1, Color aColor2, int aCellSize ) {
    int x = 0;
    int y = 0;
    int colorIdx = 0;
    Color[] c = { aColor1, aColor2 };
    while( x <= aRect.width ) {
      while( y <= aRect.height ) {
        aGc.setBackground( c[colorIdx] );
        aGc.fillRectangle( aRect.x + x, aRect.y + y, aCellSize, aCellSize );
        colorIdx = (colorIdx + 1) % 2;
        y += aCellSize;
      }
      y = 0;
      x += aCellSize;
      colorIdx = (colorIdx + (aRect.height / aCellSize) % 2) % 2;
    }
  }

  private GradientUtils() {
    // nop
  }

}

package org.toxsoft.core.tsgui.graphics.colors.impl;

import static org.toxsoft.core.tsgui.graphics.colors.impl.ITsResources.*;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Реализация {@link ITsColorManager}.
 *
 * @author hazard157
 */
public class TsColorManager
    implements ITsColorManager {

  /**
   * TODO the manager needs to be completely redesigned since SWT Color is no longer a disposable resource.
   */

  private final Display display;

  /**
   * Карта "RGB-имя" - "цвет".
   */
  private final IStringMapEdit<Color> colorsMap = new StringMap<>();

  /**
   * Карта соответствия "имя цвета" - "RGB-имя".
   */
  private final IStringMapEdit<String> namesMap = new StringMap<>();

  /**
   * Конструктор.
   *
   * @param aDisplay {@link Display} - дисплей
   * @throws TsNullArgumentRtException аргумент = null
   */
  public TsColorManager( Display aDisplay ) {
    display = TsNullArgumentRtException.checkNull( aDisplay );
    TsItemNotFoundRtException.checkNull( display );
    // запланируем освобождение ресурсов
    display.disposeExec( this::releaseResources );
    // разместим в карте имен стандартные имена цветов ETsColor
    for( ETsColor tsc : ETsColor.values() ) {
      String rgbName = rgb2name( tsc.rgb() );
      namesMap.put( tsc.id(), rgbName );
      colorsMap.put( rgbName, new Color( display, tsc.rgb() ) );
    }
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  void releaseResources() {
    while( !colorsMap.isEmpty() ) {
      Color c = colorsMap.removeByKey( colorsMap.keys().first() );
      c.dispose();
    }
    namesMap.clear();
  }

  private static String rgb2name( int aRed, int aGreen, int aBlue ) {
    if( aRed < 0 || aRed > 255 || aGreen < 0 || aGreen > 255 || aBlue < 0 || aBlue > 255 ) {
      throw new TsIllegalArgumentRtException( MSG_ERR_INV_RGB_VALS );
    }
    return TsLibUtils.EMPTY_STRING + aRed + 'x' + aGreen + 'x' + aBlue;
  }

  private static String rgb2name( RGB aRgb ) {
    return rgb2name( aRgb.red, aRgb.green, aRgb.blue );
  }

  private static RGB name2rgb( String aRgbName ) {
    TsNullArgumentRtException.checkNull( aRgbName );
    String[] colorStrs = aRgbName.toLowerCase().split( "x" ); //$NON-NLS-1$
    if( colorStrs.length != 3 ) {
      throw new TsIllegalArgumentRtException( FMT_ERR_INV_RGB_NAME, aRgbName );
    }
    int r, g, b;
    try {
      r = Integer.parseInt( colorStrs[0] );
      g = Integer.parseInt( colorStrs[1] );
      b = Integer.parseInt( colorStrs[2] );
    }
    catch( NumberFormatException ex ) {
      throw new TsIllegalArgumentRtException( ex, FMT_ERR_INV_RGB_NAME, aRgbName );
    }
    if( r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255 ) {
      throw new TsIllegalArgumentRtException( FMT_ERR_INV_RGB_NAME_VALS, aRgbName );
    }
    return new RGB( r, g, b );
  }

  // ------------------------------------------------------------------------------------
  // ITsColorManager
  //

  @Override
  public boolean hasColor( String aColorName ) {
    return namesMap.hasKey( aColorName );
  }

  @Override
  public Color getColor( ETsColor aPredefindColor ) {
    TsNullArgumentRtException.checkNull( aPredefindColor );
    return getColor( aPredefindColor.id() );
  }

  @Override
  public Color getColor( String aColorName ) {
    String rgbName = namesMap.findByKey( aColorName );
    TsItemNotFoundRtException.checkNull( rgbName, FMT_ERR_NO_COLOR_BY_NAME, aColorName );
    Color c = colorsMap.findByKey( rgbName );
    if( c != null ) {
      return c;
    }
    c = new Color( display, name2rgb( rgbName ) );
    colorsMap.put( rgbName, c );
    return c;
  }

  @Override
  public Color putColor( String aColorName, RGB aRgb ) {
    TsNullArgumentRtException.checkNull( aRgb );
    String rgbName = namesMap.findByKey( aColorName );
    if( rgbName != null ) {
      Color c = colorsMap.getByKey( rgbName );
      if( c.getRGB().equals( aRgb ) ) {
        return c;
      }
      throw new TsItemAlreadyExistsRtException( FMT_ERR_COLOR_ALREADY_EXISTS, aColorName, c.getRGB().toString() );
    }
    Color c = new Color( display, aRgb );
    colorsMap.put( rgbName, c );
    namesMap.put( aColorName, rgbName );
    return c;
  }

  @Override
  public Color getColorByRgbName( String aRgbName ) {
    Color c = colorsMap.findByKey( aRgbName );
    if( c == null ) {
      c = new Color( display, name2rgb( aRgbName ) );
      colorsMap.put( aRgbName, c );
    }
    return c;
  }

  @Override
  public Color getColor( int aR, int aG, int aB ) {
    String rgbName = rgb2name( aR, aG, aB );
    Color c = colorsMap.findByKey( rgbName );
    if( c == null ) {
      c = new Color( display, new RGB( aR, aG, aB ) );
      colorsMap.put( rgbName, c );
    }
    return c;
  }

  @Override
  public Color getColor( int aRed, int aGreen, int aBlue, int aAlpha ) {
    return new Color( aRed, aGreen, aBlue, aAlpha );
  }

  @Override
  public Color getColor( RGB aRgb ) {
    return getColor( aRgb.red, aRgb.green, aRgb.blue );
  }

  @Override
  public Color getColor( RGBA aRgba ) {
    TsNullArgumentRtException.checkNull( aRgba );
    return new Color( aRgba );
  }

  @SuppressWarnings( "boxing" )
  @Override
  public String getHumanReadableName( RGB aRgb ) {
    ETsColor c = ETsColor.findByRgb( aRgb );
    if( c != null ) {
      return c.nmName();
    }
    // HERE may be added other recognized color names
    return String.format( "(%d,%d,%d)", aRgb.red, aRgb.green, aRgb.blue ); //$NON-NLS-1$
  }

}

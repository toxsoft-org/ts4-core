package org.toxsoft.core.tsgui.valed.controls.graphics;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.graphics.colors.*;

/**
 * Панель просмотра выбранного цвета.
 * <p>
 * Имеет "шахматную" подложку для оенки уровня прозрачности.
 *
 * @author vs
 */
public class PanelColorPreview
    extends Canvas {

  int width  = 8;
  int height = 8;

  private final Color lightColor;
  private final Color darkColor;

  Color color = null;
  int   alpha = 255;

  Color colorBlack;
  Color colorWhite;

  PanelColorPreview( Composite aParent, Color aLightColor, Color aDarkColor, ITsColorManager aColorManager ) {
    super( aParent, SWT.NO_BACKGROUND | SWT.DOUBLE_BUFFERED );

    lightColor = aLightColor;
    darkColor = aDarkColor;

    colorBlack = aColorManager.getColor( ETsColor.BLACK );
    colorWhite = aColorManager.getColor( ETsColor.WHITE );

    addPaintListener( aEvent -> {
      Point p = getSize();
      int rows = p.y / 8 + 1;
      int columns = p.x / 8 + 1;
      for( int i = 0; i < columns; i++ ) {
        for( int j = 0; j < rows; j++ ) {
          if( (i + j) % 2 == 1 ) {
            aEvent.gc.setBackground( darkColor );
          }
          else {
            aEvent.gc.setBackground( lightColor );
          }
          aEvent.gc.fillRectangle( i * 8, j * 8, width, height );
        }
      }
      if( color != null ) {
        aEvent.gc.setAlpha( alpha );
        aEvent.gc.setBackground( color );
        aEvent.gc.fillRectangle( 0, 0, p.x, p.y );
      }
      aEvent.gc.setForeground( colorBlack );
      aEvent.gc.drawRectangle( 0, 0, p.x - 1, p.y - 1 );
      aEvent.gc.setForeground( colorWhite );
      aEvent.gc.drawRectangle( 1, 1, p.x - 3, p.y - 3 );
    } );

  }

  void setColor( Color aColor, int aAlpha ) {
    color = aColor;
    alpha = aAlpha;
  }

}

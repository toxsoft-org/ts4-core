package org.toxsoft.core.tsgui.valed.controls.graphics;

import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.core.tsgui.valed.controls.graphics.ITsResources.*;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.valed.controls.helpers.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Simple eidot of {@link RGB} as uneditable text field with edit button at right.
 * <p>
 * Thisd class is generic because may edit both {@link RGBA} and {@link IAtomicValue}.
 *
 * @author hazard157
 * @author dima
 * @author vs
 * @param <V> - the edited value type
 */
public abstract class AbstractValedSimpleRgba<V>
    extends AbstractValedLabelAndButton<V> {

  private static final int IMAGE_WIDTH  = 20;
  private static final int IMAGE_HEIGHT = 20;
  private static final int CELL_WIDH    = 5;
  private static final int CELL_HEIGHT  = 5;
  private RGBA             value        = null;
  private Image            colorImage   = null;

  /**
   * Constructor for subclasses.
   *
   * @param aTsContext {@link ITsGuiContext} - the valed context
   * @throws TsNullArgumentRtException аргумент = null
   */
  protected AbstractValedSimpleRgba( ITsGuiContext aTsContext ) {
    super( aTsContext );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  /**
   * Цвет в формате RGB переводим в человеческое название
   *
   * @param aRgba RGBA - цвет с прозрачностью
   * @return человеческое название RGB
   */
  @SuppressWarnings( { "boxing" } )
  public static String rgba2text( RGBA aRgba ) {
    if( aRgba == null ) {
      return STR_MSG_DEFAULT_COLOR;
    }
    ETsColor c = ETsColor.findByRgb( aRgba.rgb );
    if( c != null ) {
      return c.nmName();
    }
    RGB rgb = aRgba.rgb;
    return String.format( "(%d,%d,%d,%d)", rgb.red, rgb.green, rgb.blue, aRgba.alpha ); //$NON-NLS-1$
  }

  private void updateTextControl() {
    getLabelControl().setText( rgba2text( value ) );
    // обновим прямоугольник с цветом
    if( colorImage != null ) {
      colorImage.dispose();
    }
    if( value != null ) {
      colorImage = new Image( Display.getCurrent(), IMAGE_WIDTH, IMAGE_HEIGHT );
      GC gc = new GC( colorImage );
      Color[] cellColors = { colorManager().getColor( ETsColor.GRAY ), //
          colorManager().getColor( ETsColor.DARK_GRAY ) };
      int x = 0;
      int y = 0;
      int colorIdx = 0;
      while( x < IMAGE_WIDTH ) {
        while( y < IMAGE_HEIGHT ) {
          gc.setBackground( cellColors[colorIdx] );
          gc.fillRectangle( x, y, CELL_WIDH, CELL_HEIGHT );
          y += CELL_HEIGHT;
          colorIdx = (colorIdx + 1) % 2;
        }
        y = 0;
        x += CELL_WIDH;
        colorIdx = (colorIdx + 1) % 2;
      }
      gc.setAlpha( value.alpha );
      gc.setBackground( new Color( value.rgb.red, value.rgb.green, value.rgb.blue ) );
      gc.fillRectangle( 0, 0, colorImage.getImageData().width, colorImage.getImageData().height );
      gc.dispose();

      getLabelControl().setImage( colorImage );
    }
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedTextAndButton
  //

  @Override
  final protected void doAfterControlCreated() {
    ITsIconManager iconManager = tsContext().get( ITsIconManager.class );
    EIconSize iconSize = hdpiService().getJFaceCellIconsSize();
    getButtonControl().setImage( iconManager.loadStdIcon( ICONID_COLORS, iconSize ) );
    updateTextControl();
    // чистим ресурсы за собой
    getLabelControl().addDisposeListener( event -> {
      if( colorImage != null ) {
        colorImage.dispose();
      }
    } );
  }

  @Override
  final protected void doProcessButtonPress() {
    RGBA newVal = PanelRgbaSelector.editRgba( value, tsContext() );
    if( newVal == null ) {
      return;
    }
    value = newVal;
    updateTextControl();
    fireModifyEvent( true );
  }

  // ------------------------------------------------------------------------------------
  // For subclasses
  //

  protected RGBA getRgba() {
    return value;
  }

  protected void setRgba( RGBA aRgba ) {
    value = aRgba;
    updateTextControl();
  }

}

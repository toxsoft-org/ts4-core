package org.toxsoft.core.tsgui.valed.controls.graphics;

import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.core.tsgui.valed.controls.graphics.ITsResources.*;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.valed.controls.helpers.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Базовый редактор {@link RGB} в виде нередактируемой строки текста с кнопкой вызова диалога выбора.
 * <p>
 * Значение получаемое и задаваемое методами {@link #getRgb()} и {@link #setRgb(RGB)} может быть <code>null</code>.
 *
 * @author goga
 * @author dima // доработка для отображения квадрата цвета
 * @param <V> - конкретный тип (класс) редактируемого значения
 */
public abstract class AbstractValedSimpleRgb<V>
    extends AbstractValedLabelAndButton<V> {

  private static final int IMAGE_WIDTH  = 20;
  private static final int IMAGE_HEIGHT = 20;
  private RGB              value        = null;
  private Image            colorImage   = null;

  /**
   * Конструкторe.
   *
   * @param aContext {@link ITsGuiContext} - контекст редактора
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  AbstractValedSimpleRgb( ITsGuiContext aContext ) {
    super( aContext );
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  /**
   * Цвет в формате RGB переводим в человеческое название
   *
   * @param aRgb цвет
   * @return человеческое название RGB
   */
  @SuppressWarnings( { "boxing" } )
  public static String rgb2text( RGB aRgb ) {
    if( aRgb == null ) {
      return STR_MSG_DEFAULT_COLOR;
    }
    ETsColor c = ETsColor.findByRgb( aRgb );
    if( c != null ) {
      return c.nmName();
    }
    return String.format( "(%d,%d,%d)", aRgb.red, aRgb.green, aRgb.blue ); //$NON-NLS-1$
  }

  private void updateTextControl() {
    getLabelControl().setText( rgb2text( value ) );
    // обновим прямоугольник с цветом
    if( colorImage != null ) {
      colorImage.dispose();
    }
    if( value != null ) {
      colorImage = new Image( Display.getCurrent(), IMAGE_WIDTH, IMAGE_HEIGHT );
      GC gc = new GC( colorImage );
      gc.setBackground( new Color( value.red, value.green, value.blue ) );
      gc.fillRectangle( 0, 0, colorImage.getImageData().width, colorImage.getImageData().height );
      gc.dispose();

      getLabelControl().setImage( colorImage );
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов базового класса
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
    ColorDialog dlg = new ColorDialog( getShell() );
    dlg.setText( DLG_T_COLOR_SELECT );
    dlg.setRGB( value );
    RGB newVal = dlg.open();
    if( newVal == null ) {
      return;
    }
    value = newVal;
    updateTextControl();
    fireModifyEvent( true );
  }

  // ------------------------------------------------------------------------------------
  // Методя для наследников
  //

  protected RGB getRgb() {
    return value;
  }

  protected void setRgb( RGB aRgb ) {
    value = aRgb;
    updateTextControl();
  }

}

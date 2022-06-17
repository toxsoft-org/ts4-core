package org.toxsoft.core.tsgui.valed.controls.graphics;

import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.core.tsgui.valed.controls.graphics.ITsResources.*;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.fonts.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.valed.controls.helpers.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Базовый редактор {@link IFontInfo} в виде нередактируемой строки текста с кнопкой вызова диалога выбора.
 *
 * @author goga
 * @param <V> - конкретный тип (класс) редактируемого значения
 */
public abstract class AbstractValedSimpleFontInfo<V>
    extends AbstractValedTextAndButton<V> {

  private IFontInfo fontInfo = IFontInfo.NULL;

  /**
   * Конструкторe.
   *
   * @param aContext {@link ITsGuiContext} - контекст редактора
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  AbstractValedSimpleFontInfo( ITsGuiContext aContext ) {
    super( aContext );
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  private void updateTextControl() {
    if( fontInfo != IFontInfo.NULL ) {
      getTextControl().setText( fontInfo.toString() );
    }
    else {
      getTextControl().setText( STR_MSG_DEFAULT_FONT );
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов базового класса
  //

  @Override
  final protected void doAfterControlCreated() {
    ITsIconManager iconManager = tsContext().get( ITsIconManager.class );
    EIconSize iconSize = hdpiService().getJFaceCellIconsSize();
    getButtonControl().setImage( iconManager.loadStdIcon( ICONID_FONTS, iconSize ) );
    updateTextControl();
  }

  @Override
  final protected void doProcessButtonPress() {
    FontDialog dlg = new FontDialog( getShell() );
    dlg.setText( DLG_T_FONT_SELECT );
    ITsFontManager fontManager = tsContext().get( ITsFontManager.class );
    if( fontInfo != IFontInfo.NULL ) {
      FontData initialFdntData = fontManager.info2data( fontInfo );
      dlg.setFontList( new FontData[] { initialFdntData } );
    }
    FontData fd = dlg.open();
    if( fd == null ) {
      return;
    }
    fontInfo = fontManager.data2info( fd );
    updateTextControl();
    fireModifyEvent( true );
  }

  // ------------------------------------------------------------------------------------
  // Методя для наследников
  //

  protected IFontInfo getFontInfo() {
    return fontInfo;
  }

  protected void setFontInfo( IFontInfo aFontInfo ) {
    if( aFontInfo != null ) {
      fontInfo = aFontInfo;
    }
    else {
      fontInfo = IFontInfo.NULL;
    }
    updateTextControl();
  }

}

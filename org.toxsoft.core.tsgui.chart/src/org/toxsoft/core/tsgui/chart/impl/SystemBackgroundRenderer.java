package org.toxsoft.core.tsgui.chart.impl;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Реализация отрисовщика стандартного системного фона заливки.
 *
 * @author dima
 */
public final class SystemBackgroundRenderer
    implements IBackgroundRenderer {

  private final Color backColor;

  /**
   * Конструктор с параметрами настройки.
   *
   * @param aOpSet {@link IOptionSet} - параметры настройки
   * @param aContext {@link ITsGuiContext } - контекст приложения
   */
  SystemBackgroundRenderer( IOptionSet aOpSet, ITsGuiContext aContext ) {
    backColor = Display.getDefault().getSystemColor( SWT.COLOR_WIDGET_BACKGROUND );
  }

  // -------------------------------------------------------------------------
  // Реализация методов IBackgroundRenderer
  //

  @Override
  public void drawBackground( GC aGc, ITsRectangle aBounds, EBorderLayoutPlacement aPlace, boolean aFocused,
      boolean aSelected ) {
    TsNullArgumentRtException.checkNulls( aGc, aBounds, aPlace );
    aGc.setBackground( backColor );
    aGc.setForeground( backColor );
    aGc.fillRectangle( aBounds.x1(), aBounds.y1(), aBounds.width(), aBounds.height() );
  }

}

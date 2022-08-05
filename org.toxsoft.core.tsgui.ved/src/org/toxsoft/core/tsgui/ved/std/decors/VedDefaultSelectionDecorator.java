package org.toxsoft.core.tsgui.ved.std.decors;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.ved.core.*;
import org.toxsoft.core.tsgui.ved.core.impl.*;
import org.toxsoft.core.tsgui.ved.core.view.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;

/**
 * Отрисовщик выделения компонент по-умолчанию.
 * <p>
 *
 * @author vs
 */
public class VedDefaultSelectionDecorator
    extends VedAbstractSelectionDecorator {

  private final Color selColor;

  /**
   * Конструктор.<br>
   *
   * @param aScreen IVedScreen - экран редактора
   * @param aEnv IVedEnvironment - окружение редактора
   * @param aSelectionColor Color - цвет выделения
   */
  public VedDefaultSelectionDecorator( IVedScreen aScreen, IVedEnvironment aEnv, Color aSelectionColor ) {
    super( aScreen, aEnv );
    selColor = aSelectionColor;
  }

  // ------------------------------------------------------------------------------------
  // IVedScreenSelectionDecorator
  //

  @Override
  protected void paintSelection( IVedComponentView aView, GC aGc, ITsRectangle aPaintBounds ) {
    aGc.setAdvanced( true );
    aGc.setAntialias( SWT.ON );

    int oldStyle = aGc.getLineStyle();
    int oldWidth = aGc.getLineWidth();
    Color oldColor = aGc.getForeground();

    aGc.setLineStyle( SWT.LINE_DASH );
    aGc.setLineWidth( 1 );
    aGc.setForeground( selColor );

    ID2Rectangle d2r = aView.outline().bounds();
    // ITsRectangle rect = vedScreen().coorsConvertor().rectBounds( aView.outline().bounds() );

    int x = (int)d2r.a().x();
    int y = (int)d2r.a().y();
    int w = (int)d2r.width();
    int h = (int)d2r.height();

    Transform oldTransfrom = null;
    Transform t = null;
    ID2Conversion d2conv = vedScreen().getConversion();
    if( d2conv != ID2Conversion.NONE ) {
      oldTransfrom = new Transform( aGc.getDevice() );
      aGc.getTransform( oldTransfrom );

      t = ((VedAbstractComponentView)aView).conv2transform( aGc );
      aGc.setTransform( t );
      t.dispose();
    }

    // aGc.drawRectangle( rect.a().x() - 2, rect.a().y() - 2, rect.width() + 4, rect.height() + 4 );
    aGc.drawRectangle( x - 2, y - 2, w + 4, h + 4 );

    if( oldTransfrom != null ) { // восстановим старый transform
      aGc.setTransform( oldTransfrom );
      oldTransfrom.dispose();
    }

    aGc.setLineStyle( oldStyle );
    aGc.setLineWidth( oldWidth );
    aGc.setForeground( oldColor );
  }

  @Override
  public IOptionSet params() {
    // TODO Auto-generated method stub
    return null;
  }

}

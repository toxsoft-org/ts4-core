package org.toxsoft.core.tsgui.graphics.patterns;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;

/**
 * Параметры линейного градиента.
 * <p>
 *
 * @author vs
 */
public class LinearGradientPattern
    extends AbstractSwtPattern {

  private final LinearGradientInfo info;

  private final ITsGuiContext context;

  /**
   * Конструкторы.<br>
   *
   * @param aInfo LinearGradientInfo - параметры линейного градиента
   * @param aContext ITsGuiContext - соответствующий контекст
   */
  public LinearGradientPattern( LinearGradientInfo aInfo, ITsGuiContext aContext ) {
    super( aContext );
    info = aInfo;
    context = aContext;
  }

  @Override
  public ITsGuiContext tsContext() {
    return context;
  }

  // ------------------------------------------------------------------------------------
  // ISwtPattern
  //

  @Override
  public ISwtPatternInfo patternInfo() {
    return info;
  }

  @Override
  public Pattern pattern( GC aGc, int aWidth, int aHeight ) {
    float x1 = (float)(aWidth * info.startPoint().x() / 100.);
    float y1 = (float)(aHeight * info.startPoint().y() / 100.);
    float x2 = (float)(aWidth * info.endPoint().x() / 100.);
    float y2 = (float)(aHeight * info.endPoint().y() / 100.);

    Color c1 = colorManager().getColor( info.startRGBA().rgb );
    Color c2 = colorManager().getColor( info.endRGBA().rgb );
    int alpha1 = info.startRGBA().alpha;
    int alpha2 = info.endRGBA().alpha;

    return new Pattern( aGc.getDevice(), x1, y1, x2, y2, c1, alpha1, c2, alpha2 );
  }

}

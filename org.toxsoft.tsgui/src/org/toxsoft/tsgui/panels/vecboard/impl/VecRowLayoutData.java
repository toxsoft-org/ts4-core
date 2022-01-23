package org.toxsoft.tsgui.panels.vecboard.impl;

import org.eclipse.swt.SWT;
import org.toxsoft.tsgui.panels.vecboard.IVecRowLayoutData;

/**
 * Неизменяемая реализация {@link IVecRowLayoutData}.
 *
 * @author goga
 */
public class VecRowLayoutData
    implements IVecRowLayoutData {

  private final int width;
  private final int height;

  /**
   * Создает объект со всеми инвариантами.
   *
   * @param aWidth int - положительная высота в пикселях или {@link SWT#DEFAULT}
   * @param aHeight int - положительная ширина в пикселях или {@link SWT#DEFAULT}
   */
  public VecRowLayoutData( int aWidth, int aHeight ) {
    width = aWidth;
    height = aHeight;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IAopRowLayoutData
  //

  @Override
  public int width() {
    return width;
  }

  @Override
  public int height() {
    return height;
  }

}

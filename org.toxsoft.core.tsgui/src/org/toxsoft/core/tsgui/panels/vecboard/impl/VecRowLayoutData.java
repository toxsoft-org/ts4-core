package org.toxsoft.core.tsgui.panels.vecboard.impl;

import org.eclipse.swt.SWT;
import org.toxsoft.core.tsgui.panels.vecboard.IVecRowLayoutData;

/**
 * Неизменяемая реализация {@link IVecRowLayoutData}.
 *
 * @author hazard157
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
  // IAopRowLayoutData
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

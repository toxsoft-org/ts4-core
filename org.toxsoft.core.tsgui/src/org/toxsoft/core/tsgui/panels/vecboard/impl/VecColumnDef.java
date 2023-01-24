package org.toxsoft.core.tsgui.panels.vecboard.impl;

import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.panels.vecboard.*;

/**
 * Неизменяемый класс параметров размещения элементов в колонке для {@link IVecColumnLayout}.
 *
 * @author vs
 */
public class VecColumnDef
    implements IVecColumnDef {

  private final boolean fixedWidth;

  private final EHorAlignment horAlignment;
  private final EVerAlignment verAlignment;

  private final int minWidth;
  private final int minHeight;

  /**
   * Конструктор задающий выравнивание положения элемента.
   *
   * @param aHorAlignment EHorAlignment - выравнивание положения элемента по горизонтали
   * @param aVerAlignment EVerAlignment - выравнивание положения элемента по вертикали
   */
  public VecColumnDef( EHorAlignment aHorAlignment, EVerAlignment aVerAlignment ) {
    fixedWidth = false;
    horAlignment = aHorAlignment;
    verAlignment = aVerAlignment;
    minWidth = 0;
    minHeight = 0;
  }

  /**
   * Конструктор со всеми инвариантами.
   *
   * @param aHorAlignment EHorAlignment - выравнивание положения элемента по горизонтали
   * @param aVerAlignment EVerAlignment - выравнивание положения элемента по вертикали
   * @param aFixedWidh boolean - признак изменения ширины колонки при изменении размеров окна (<b>true<b> - постоянная)
   * @param aMinWidth int - минимальная ширина колонки в пикселях
   * @param aMinHeight int - минимальная высота элемента в пикселях
   */
  public VecColumnDef( EHorAlignment aHorAlignment, EVerAlignment aVerAlignment, boolean aFixedWidh,
      int aMinWidth, int aMinHeight ) {
    fixedWidth = false;
    horAlignment = aHorAlignment;
    verAlignment = aVerAlignment;
    minWidth = 0;
    minHeight = 0;
  }

  // ------------------------------------------------------------------------------------
  // IVecColumnLayoutData
  //

  @Override
  public boolean isWidthFixed() {
    return fixedWidth;
  }

  @Override
  public EHorAlignment horAlignment() {
    return horAlignment;
  }

  @Override
  public EVerAlignment verAlignment() {
    return verAlignment;
  }

  @Override
  public int minWidth() {
    return minWidth;
  }

  @Override
  public int minHeight() {
    return minHeight;
  }

}

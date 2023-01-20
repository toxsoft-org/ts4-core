package org.toxsoft.core.tsgui.panels.vecboard;

import org.toxsoft.core.tsgui.graphics.*;

/**
 * Параметры расположения элементов в раскладке {@link IVecColumnLayout}.
 * <p>
 *
 * @author vs
 */
public interface IVecColumnLayoutData {

  /**
   * Параметры размещения элементов по умолчанию
   */
  IVecColumnLayoutData DEFAULT = new InternalDefaultColumnData();

  /**
   * Возвращет признак изменяемости ширины колонки при изменении размеров окна.
   *
   * @return <b>false</b> - ширина колонки будет изменяться<br>
   *         <b>true</b> - ширина колонки останется постоянной
   */
  boolean isWidthFixed();

  /**
   * Возвращает тип выравнивания положения элемента в колонке по горизонтали.
   *
   * @return EHorAlignment - тип выравнивания положения элемента в колонке по горизонтали
   */
  EHorAlignment horAlignment();

  /**
   * Возвращает тип выравнивания положения элемента в строке колонки по вертикали.
   *
   * @return EVerAlignment - тип выравнивания положения элемента в строке колонки по вертикали
   */
  EVerAlignment verAlignment();

  /**
   * Возвращает минимальную ширину колонки.<br>
   * По умолчанию 0.
   *
   * @return int - минимальную ширину колонки
   */
  int minWidth();

  /**
   * Возвращает минимальную высоту элемента.<br>
   * По умолчанию 0.
   *
   * @return int - минимальную высоту элемента
   */
  int minHeight();

}

class InternalDefaultColumnData
    implements IVecColumnLayoutData {

  @Override
  public boolean isWidthFixed() {
    return false;
  }

  @Override
  public EHorAlignment horAlignment() {
    return EHorAlignment.LEFT;
  }

  @Override
  public EVerAlignment verAlignment() {
    return EVerAlignment.CENTER;
  }

  @Override
  public int minWidth() {
    return 0;
  }

  @Override
  public int minHeight() {
    return 0;
  }

}

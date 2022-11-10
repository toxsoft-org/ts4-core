package org.toxsoft.core.tsgui.graphics.image;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Примешиваемый интерфейс визуальных элементов, имеющих понятие "размер миниатюры".
 * <p>
 * FIXME TRANSLATE<br>
 * FIXME ThumbSizeableZoomDropDownMenuCreator
 *
 * @author hazard157
 */
public interface IThumbSizeable {

  /**
   * Возвращает размер миниатюр.
   *
   * @return {@link EThumbSize} - размер миниатюр
   */
  EThumbSize thumbSize();

  /**
   * Задает размер миниатюр.
   *
   * @param aThumbSize {@link EThumbSize} - размер миниатюр
   * @throws TsNullArgumentRtException аргумент = null
   */
  void setThumbSize( EThumbSize aThumbSize );

  /**
   * Возвращает размер миниатюр, считающейся размером по умолчанию.
   *
   * @return {@link EThumbSize} - размер миниатюр по умолчанию
   */
  EThumbSize defaultThumbSize();

  /**
   * Sets the thumb size to the {@link #defaultThumbSize()}.
   */
  default void setDefaultThumbSize() {
    setThumbSize( defaultThumbSize() );
  }

}

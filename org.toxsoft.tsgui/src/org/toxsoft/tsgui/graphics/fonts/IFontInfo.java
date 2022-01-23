package org.toxsoft.tsgui.graphics.fonts;

import org.eclipse.swt.SWT;
import org.toxsoft.tslib.utils.TsLibUtils;

/**
 * Информация о шрифте, которым рисуется текст.
 *
 * @author hazard157
 */
public interface IFontInfo {

  /**
   * "Нулевой" шрифт (без имени, размер 0), испольуется вместо null и для обозначения шрифта по умочанию.
   */
  IFontInfo NULL = new InternalNullFontInfo();

  /**
   * Возвращает название шрифта.
   *
   * @return String - название шрифта
   */
  String fontName();

  /**
   * Возвращает размер шрифта.
   *
   * @return int - размер шрифта в TODO в каких единицах указывается размер шрифта?
   */
  int fontSize();

  /**
   * Возвращает признак жирного шрифта.
   *
   * @return boolean - признак жирного шрифта
   */
  boolean isBold();

  /**
   * Возвращает признак курсива.
   *
   * @return boolean - признак курсива
   */
  boolean isItalic();

  /**
   * Возвращает признаки шрифта в виде набора по ИЛИ битов {@link SWT#BOLD} и {@link SWT#ITALIC}.
   * <p>
   * Если шрифт нормальный (т.е. не жирный, и не курсив), возвращает 0.
   *
   * @return int - биты SWT.BOLD и/или SWT.ITALIC или 0
   * @see SWT#BOLD
   * @see SWT#ITALIC
   */
  int getSwtStyle();

}

class InternalNullFontInfo
    implements IFontInfo {

  @Override
  public String fontName() {
    return TsLibUtils.EMPTY_STRING;
  }

  @Override
  public int fontSize() {
    return 0;
  }

  @Override
  public boolean isBold() {
    return false;
  }

  @Override
  public boolean isItalic() {
    return false;
  }

  @Override
  public int getSwtStyle() {
    return 0;
  }

}

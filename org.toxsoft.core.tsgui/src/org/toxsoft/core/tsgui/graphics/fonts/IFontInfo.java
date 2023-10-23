package org.toxsoft.core.tsgui.graphics.fonts;

import org.eclipse.swt.*;
import org.toxsoft.core.tsgui.graphics.fonts.impl.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * Information about font for text drawing.
 *
 * @author hazard157
 */
public interface IFontInfo {

  /**
   * "Zero" font (no name, size 0), used instead of <code>null</code>.
   */
  IFontInfo NULL = new InternalNullFontInfo();

  /**
   * Some default font (regular Arial, size 10).
   */
  IFontInfo DEFAULT = new FontInfo( "Arial", 10, 0 ); //$NON-NLS-1$

  /**
   * Returns the font typeface name.
   *
   * @return String - the font typeface name
   */
  String fontName();

  /**
   * Returns the font size.
   *
   * @return int - the font size in points (1/72 inch)
   */
  int fontSize();

  /**
   * Determines if font is bold.
   *
   * @return boolean - <code>true</code> for bold font
   */
  boolean isBold();

  /**
   * Determines if font is italic.
   *
   * @return boolean - <code>true</code> for italic font
   */
  boolean isItalic();

  /**
   * Returns the SWT bits of a font style, ORed bits {@link SWT#BOLD} and {@link SWT#ITALIC}.
   *
   * @return int - bits {@link SWT#BOLD} and {@link SWT#ITALIC}, 0 for regular font
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

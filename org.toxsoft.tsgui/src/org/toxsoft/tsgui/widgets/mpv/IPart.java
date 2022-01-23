package org.toxsoft.tsgui.widgets.mpv;

import org.toxsoft.tslib.math.IntRange;

/**
 * The part of the {@link IMultiPartValue}.
 *
 * @author hazard157
 */
public interface IPart {

  /**
   * Returns the part name.
   *
   * @return String - the part name
   */
  String name();

  /**
   * Returns the number of digits in textual representation of the part value.
   *
   * @return int - the number of digits of value text, always >= 1
   */
  int digitsCount();

  /**
   * Returna the number of char symbols of part textual representation.
   * <p>
   * Part chars count consists of {@link #digitsCount()}
   *
   * @return int - parts text chars number
   */
  int charsCount();

  /**
   * Returns maximal allowed range of value.
   * <p>
   * {@link IMultiPartValue} implementation may apply additional restictions to the part value.
   *
   * @return {@link IntRange} - allowed range of value
   */
  IntRange range();

  /**
   * Returns the value of the part.
   *
   * @return int - the value
   */
  int value();

  /**
   * Returns the value string having width of {@link #digitsCount()} chars.
   *
   * @return String - value string
   */
  String valueString();

  /**
   * Returns the part string having width of {@link #charsCount()} chars.
   * <p>
   * Part string is {@link #valueString()} surrounded with optional {@link #charBefore()} and {@link #charAfter()}
   * symbols.
   *
   * @return String - part string
   */
  String partString();

  /**
   * The symbol immeadiately before value digits of this part.
   *
   * @return {@link Character} - leading char or <code>null</code>
   */
  Character charBefore();

  /**
   * The symbol immeadiately after value digits of this part.
   *
   * @return {@link Character} - trailing char or <code>null</code>
   */
  Character charAfter();

}

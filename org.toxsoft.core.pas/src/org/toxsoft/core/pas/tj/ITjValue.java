package org.toxsoft.core.pas.tj;

import org.toxsoft.core.pas.tj.impl.TjUtils;
import org.toxsoft.core.tslib.coll.IListEdit;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsUnsupportedFeatureRtException;

/**
 * Значение поля структуры {@link ITjObject}.
 * <p>
 * Точкой входа в подсистему является класс {@link TjUtils}.
 *
 * @author goga
 */
public interface ITjValue {

  /**
   * Возвращает вид значения.
   * <p>
   * В зависимости от вида, значение можно получить (задать) следующими методами:
   * <ul>
   * <li>{@link ETjKind#STRING} - {@link #asString()};</li>
   * <li>{@link ETjKind#OBJECT} - {@link #asObject()};</li>
   * <li>{@link ETjKind#NUMBER} - {@link #asNumber()};</li>
   * <li>{@link ETjKind#ARRAY} - {@link #asArray()};</li>
   * <li>{@link ETjKind#NULL}, {@link ETjKind#TRUE}, {@link ETjKind#FALSE} - соответствуют константам и у них нельзя
   * порлучить значение.</li>
   * </ul>
   * Вызов недопустимого метода приводит к исключению {@link TsUnsupportedFeatureRtException}.
   *
   * @return {@link ETjKind} - вид значения
   */
  ETjKind kind();

  /**
   * Возвращает строковое значение.
   *
   * @return String - строковое значение
   * @throws TsUnsupportedFeatureRtException {@link #kind()} != {@link ETjKind#STRING}
   */
  String asString();

  /**
   * Возвращает числовое значение.
   *
   * @return {@link Number} - числовое значение
   * @throws TsUnsupportedFeatureRtException {@link #kind()} != {@link ETjKind#NUMBER}
   */
  Number asNumber();

  /**
   * Возвращает значение как объект {@link ITjObject}.
   *
   * @return {@link ITjObject} - значение как объект {@link ITjObject}
   * @throws TsUnsupportedFeatureRtException {@link #kind()} != {@link ETjKind#OBJECT}
   */
  ITjObject asObject();

  /**
   * Возвращает значение-массив в виде редактируемого списка.
   *
   * @return {@link IListEdit}&lt;{@link ITjValue}&gt; - элементы массива в виде редактируемого списка
   * @throws TsUnsupportedFeatureRtException {@link #kind()} != {@link ETjKind#ARRAY}
   */
  IListEdit<ITjValue> asArray();

  /**
   * Задает строковое значение.
   *
   * @param aString String - строковое значение
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsUnsupportedFeatureRtException {@link #kind()} != {@link ETjKind#STRING}
   */
  void setString( String aString );

  /**
   * Задает числовое значение.
   *
   * @param aNumber {@link Number} - числовое значение
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsUnsupportedFeatureRtException {@link #kind()} != {@link ETjKind#NUMBER}
   */
  void setNumber( Number aNumber );

  /**
   * Задает целочисленное значение.
   *
   * @param aNumber int - целочисленное значение s * @throws TsUnsupportedFeatureRtException {@link #kind()} !=
   *          {@link ETjKind#NUMBER}
   */
  void setNumber( int aNumber );

  /**
   * Задает целочисленное значение.
   *
   * @param aNumber long - целочисленное значение
   * @throws TsUnsupportedFeatureRtException {@link #kind()} != {@link ETjKind#NUMBER}
   */
  void setNumber( long aNumber );

  /**
   * Задает вещественное значение.
   *
   * @param aNumber double - вещественное значение
   * @throws TsUnsupportedFeatureRtException {@link #kind()} != {@link ETjKind#NUMBER}
   */
  void setNumber( double aNumber );

  /**
   * Опредеяет, является ли число {@link #asNumber()} целым.
   *
   * @return boolean - признак целого числа
   * @throws TsUnsupportedFeatureRtException {@link #kind()} != {@link ETjKind#NUMBER}
   */
  boolean isInteger();

  /**
   * Определяет, что вид значения - {@link ETjKind#STRING}.
   *
   * @return boolean - признак, что вид значения - {@link ETjKind#STRING}.
   */
  default boolean isString() {
    return kind() == ETjKind.STRING;
  }

  /**
   * Определяет, что вид значения - {@link ETjKind#STRING}.
   *
   * @return boolean - признак, что вид значения - {@link ETjKind#NUMBER}.
   */
  default boolean isNumber() {
    return kind() == ETjKind.NUMBER;
  }

  /**
   * Определяет, что вид значения - {@link ETjKind#OBJECT}.
   *
   * @return boolean - признак, что вид значения - {@link ETjKind#OBJECT}.
   */
  default boolean isObject() {
    return kind() == ETjKind.OBJECT;
  }

  /**
   * Определяет, что вид значения - {@link ETjKind#TRUE}.
   *
   * @return boolean - признак, что вид значения - {@link ETjKind#TRUE}.
   */
  default boolean isTrue() {
    return kind() == ETjKind.TRUE;
  }

  /**
   * Определяет, что вид значения - {@link ETjKind#FALSE}.
   *
   * @return boolean - признак, что вид значения - {@link ETjKind#FALSE}.
   */
  default boolean isFalse() {
    return kind() == ETjKind.FALSE;
  }

  /**
   * Определяет, что вид значения - {@link ETjKind#NULL}.
   *
   * @return boolean - признак, что вид значения - {@link ETjKind#NULL}.
   */
  default boolean isNull() {
    return kind() == ETjKind.NULL;
  }

}

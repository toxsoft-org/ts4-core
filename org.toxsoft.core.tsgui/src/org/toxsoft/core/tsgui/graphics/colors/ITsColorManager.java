package org.toxsoft.core.tsgui.graphics.colors;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Менеджер работы с цветами {@link Color}.
 * <p>
 * У этого менеджера две функции:
 * <ul>
 * <li>содержать в себе кеш цветов {@link Color} и автоматиечсик удалять закег=шированные ресурсы цветов при завершении
 * программы (точнее, при уничтожении дисплея {@link Display}, ассоциированной с программой);</li>
 * <li>поддерживать понятие символьного имени цвета (например,"black");</li>
 * <li>поддерживать понятие rgb-имя цвета, в котором зашито значение цвета (например, "255x0x0" - красный).</li>
 * </ul>
 * <p>
 * Ссылка на экземпляр этого класса должен находится в контексте приложения.
 *
 * @author hazard157
 */
public interface ITsColorManager {

  /**
   * Определяет, существует ли в кеше цвет с заданным символическим именем.
   *
   * @param aColorName String - символическое имя цвета
   * @return boolean - признак существования цвета с заданным символическим именем в кеше
   * @throws TsNullArgumentRtException аргумент = null
   */
  boolean hasColor( String aColorName );

  /**
   * Возвращает объект цвета по константе предопределенного цвета {@link ETsColor}.
   *
   * @param aTsColor {@link ETsColor} - константа предопределенного цвета
   * @return {@link Color} - кешированный цвет
   * @throws TsNullArgumentRtException аргумент = null
   */
  Color getColor( ETsColor aTsColor );

  /**
   * Возвращает цвет по символическому имени.
   * <p>
   * Символическим именем цвета может быть любой, заданный пользователем в {@link #putColor(String, RGB)} название
   * цвета, или одна из предопределенных названий {@link ETsColor#id()}.
   *
   * @param aColorName String - символическое имя цвета
   * @return {@link Color} - кешированный цвет
   * @throws TsNullArgumentRtException аргумент = null
   */
  Color getColor( String aColorName );

  /**
   * Размещает в кеше цвет с заданным символическим именем.
   * <p>
   * В случае, если цвет с таким именем уже закширован, и он другой (т.е. с другимы значениями красного, зеленого и
   * синего), то выбрасывает исключение.
   *
   * @param aColorName String - символическое название цвета
   * @param aRgb {@link RGB} - значение компонент цвета
   * @return {@link Color} - кешированный цвет
   * @throws TsItemAlreadyExistsRtException цвет с таким символичесим именем уже сущсествует
   */
  Color putColor( String aColorName, RGB aRgb );

  /**
   * Возвращает цвет по идентификатору вида "RRRxGGGxBBB".
   * <p>
   * В этом методе используется строковое представление цвета, которое в содержит в себе всю информацию о цвете.
   * Строковое представление имеет формат "RxGxB", где R, G и B значения красного, зеленого и синего цветов в диапазоне
   * "0".."255" (например, "17x255x0").
   * <p>
   * Если такой цвет не был создан, создает и кеширует его.
   *
   * @param aRgbName String - идентификатор цвета вида "RRRxGGGxBBB"
   * @return {@link Color} - кешированный цвет
   * @throws TsNullArgumentRtException аргумент = null
   */
  Color getColorByRgbName( String aRgbName );

  /**
   * Возвращает цвет по заданному сочетанию красного, зеленого, синего.
   * <p>
   * Если такой цвет не был создан, создает и кеширует его.
   *
   * @param aR int - красный вдиапазоне 0.255
   * @param aG int - зеленый вдиапазоне 0.255
   * @param aB int - синий вдиапазоне 0.255
   * @return {@link Color} - кешированный цвет
   * @throws TsIllegalArgumentRtException - любой цвет выходт за допустимый диапазон
   */
  Color getColor( int aR, int aG, int aB );

  /**
   * Возвращает цвет по заданному сочетанию красного, зеленого, синего.
   * <p>
   * Если такой цвет не был создан, создает и кеширует его.
   *
   * @param aRgb {@link RGB} - сочетание красного, зеленого, синего
   * @return {@link Color} - кешированный цвет
   * @throws TsNullArgumentRtException аргумент = null
   */
  Color getColor( RGB aRgb );

  /**
   * Returns human-readable name of the RGB color.
   *
   * @param aRgb {@link RGB} - the color
   * @return String - human-readable color name
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  String getHumanReadableName( RGB aRgb );

  /**
   * Returns human-readable name of the {@link Color}.
   *
   * @param aColor {@link Color} - the color
   * @return String - human-readable color name
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  default String getHumanReadableName( Color aColor ) {
    TsNullArgumentRtException.checkNull( aColor );
    return getHumanReadableName( aColor.getRGB() );
  }

  /**
   * Создает {@link RGB} из целочисленного значения цвета.
   * <p>
   * Три младших байта целого хранят значения (в диапазоне 0..255) красного, зеленого и синего (начиная с самого
   * младшего байта).
   *
   * @param aValue int - целое число, в котором содержатся значения красной, зеленой и синей компоненты цвета
   * @return {@link RGB} - цвет в формате SWT
   */
  static RGB int2rgb( int aValue ) {
    int r = aValue & 0xff;
    int g = (aValue >> 8) & 0xff;
    int b = (aValue >> 16) & 0xff;
    return new RGB( r, g, b );
  }

  /**
   * Создает {@link RGBA} из целочисленного значения цвета.
   * <p>
   * Три младших байта целого хранят значения (в диапазоне 0..255) красного, зеленого и синего (начиная с самого
   * младшего байта), старший - значение альфа.
   *
   * @param aValue int - целое число, в котором содержатся значения красной, зеленой, синей и альфа компоненты цвета
   * @return {@link RGBA} - цвет в формате SWT
   */
  static RGBA int2rgba( int aValue ) {
    int r = aValue & 0xff;
    int g = (aValue >> 8) & 0xff;
    int b = (aValue >> 16) & 0xff;
    int a = (aValue >> 24) & 0xff;
    return new RGBA( r, g, b, a );
  }

  /**
   * Создает целочисленное значения цвета из {@link RGB}.
   * <p>
   * Три младших байта целого хранят значения (в диапазоне 0..255) красного, зеленого и синего (начиная с самого
   * младшего байта).
   *
   * @param aRgb {@link RGB} - цвет в формате SWT
   * @return int - целое число, в котором содержатся значения красной, зеленой и синей компоненты цвета
   */
  static int rgb2int( RGB aRgb ) {
    int r = aRgb.red;
    int g = aRgb.green << 8;
    int b = aRgb.blue << 16;
    return r | g | b;
  }

  /**
   * Создает целочисленное значения цвета из {@link RGBA}.
   * <p>
   * Три младших байта целого хранят значения (в диапазоне 0..255) красного, зеленого и синего (начиная с самого
   * младшего байта), старший - значение альфа.
   *
   * @param aRgba {@link RGBA} - цвет в формате SWT
   * @return int - целое число, в котором содержатся значения красной, зеленой, синей и альфа компоненты цвета
   */
  static int rgba2int( RGBA aRgba ) {
    int r = aRgba.rgb.red;
    int g = aRgba.rgb.green << 8;
    int b = aRgba.rgb.blue << 16;
    int a = aRgba.alpha << 24;
    return r | g | b | a;
  }

}

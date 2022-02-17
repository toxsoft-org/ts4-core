package org.toxsoft.core.tsgui.valed.api;

import java.io.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * VALue EDitor control interface.
 *
 * @author hazard157
 * @param <V> - the edited value type
 */
public interface IValedControl<V>
    extends ILazyControl<Control>, ITsContextable, IParameterizedEdit {

  // TODO TRANSLATE

  /**
   * Возвращает текущее состояние разрешения редактирования значения (признак "редактируемости").
   * <p>
   * Этот метод по смыслу аналогичен {@link Control#isEnabled()}, точнее, для контролей, поддерживающих понятие
   * "редактируемость". Например, {@link Text#getEditable()}.
   * <p>
   * Состояние контроля в момент создания задается параметром {@link IValedControlConstants#OPDEF_CREATE_UNEDITABLE}.
   * Если при создании контроль является нередактируемым, в дальнейшем его сделать редактируемым невозможно.
   *
   * @return booolean - признак разрешения редактирования значения
   */
  boolean isEditable();

  /**
   * Задает признак признак разрешения редакторования.
   * <p>
   * Этот метод по смыслу аналогичен {@link Control#setEnabled(boolean)}, а точнее, {@link Text#setEditable(boolean)}.
   * Отличие проявлется в том, что реально редактор состоять из нескольких компонент, и в этом случае этот метод
   * корректно запрещает/разрешает нужные компоненты составного контроля.
   * <p>
   * Обратите внимание, что если контроль был создан с заданным в <code>true</code> параметром
   * {@link IValedControlConstants#OPDEF_CREATE_UNEDITABLE}, то контроль будет нередактиремым, и этот метод молча
   * игнорируется.
   *
   * @param aEditable booolean - разрешение редактирования
   */
  void setEditable( boolean aEditable );

  /**
   * Определяет, содержиться ли в редакторе значение допустимого типа, которое можно считать.
   * <p>
   * Другими словами, проверяет, приведет ли вызов {@link #getValue()} к исключению, или вернет значение.
   * <p>
   * Проверка допустимости типа должна происходить в следующем порядке:
   * <ul>
   * <li>сначала проверяется, что визуальная компонента (виджет) содержит в себе значение типа &lt;V&gt;. Например, если
   * V это {@link File}, а виджет это {@link Text}, то должно быть проверено, что строка содержит строку в формате пути
   * к файлу, а не, например, HTTP ссылку;</li>
   * <li>потом проверяется "уточняющий тип". Имеется в виду следующее: часть редакторов внутри типа &lt;V&gt; вводит еще
   * уточнение типа, например для {@link IAtomicValue} уточняющим типом является {@link EAtomicType}. Или еще: для типа
   * {@link IList}, уточняющим типом будет являтся тип элементов списка;</li>
   * </ul>
   *
   * @return {@link ValidationResult} - результат проверки допустимости значения в редакторе
   */
  ValidationResult canGetValue();

  /**
   * Возвращает редактируемое значение.
   * <p>
   * Возвращаемое значение гарантированно имеет нужный <b>тип</b>. Допустимость значения <b>не</b> проверятся.
   * <p>
   * Внимание: реализация должна гарантировать, что значение будет возвращено вне зависимости от того, существует ли
   * виджет редактора. В отсутствие виджета редактора должно вернутся значение заданное методом
   * {@link #setValue(Object)}, или значение по умолчанию.
   *
   * @return &lt;V&gt; - редактируемое значение, может быть null (если контроль допускает это)
   * @throws TsIllegalStateRtException запрошено значение до того, как оно было задано методом {@link #setValue(Object)}
   * @throws TsValidationFailedRtException не прошла проверка {@link #canGetValue()}
   */
  V getValue();

  /**
   * Sets the value to be edited by this control.
   * <p>
   * Setting value does <b>not</b> generates notification {@link IValedControlValueChangeListener}.
   * <p>
   * Calling this method with <code>null</code> argument is allowed and is the same as call to {@link #clearValue()}.
   *
   * @param aValue &lt;V&gt; - new value, may be <code>null</code> to {@link #clearValue()}
   */
  void setValue( V aValue );

  /**
   * Clears the value in the widget.
   * <p>
   * Clearing value does <b>not</b> generates notification {@link IValedControlValueChangeListener}.
   * <p>
   * The method is designed to set an element to the "no value" (or <code>null</code>) state. If <code>null</code> is
   * not valid value for control, immediately after this method {@link #canGetValue()} will return an error and
   * {@link #getValue()} will trow an exception. To get valid value from control either user must input the value to the
   * widget or non-<code>null</code> value must be set programmatically by {@link #setValue(Object)} method.
   */
  void clearValue();

  /**
   * Returns the value editing notifications manager.
   *
   * @return {@link ITsEventer}&lt;{@link IValedControlValueChangeListener}&gt; - the eventer
   */
  ITsEventer<IValedControlValueChangeListener> eventer();

}

package org.toxsoft.core.tsgui.valed.api;

/**
 * Слушатель изменений пользовательского ввода в каком-нибудь редактире.
 *
 * @author hazard157
 */
public interface IValedControlValueChangeListener {

  /**
   * Вызывается, при изменении <b>пользователем</b> значения в редакторе.
   * <p>
   * Если значение в контроле меняется программно (методами API контроля), данный слушатель <b>не</b> вызывается.
   * <p>
   * Для некторых контролей (редакторов) процесс изменения значения является продолжительным (состоящим из нескольких
   * действий) и надо отличать изменение значение в процессе правки, и в момент завершения. Типичный случай - поле ввода
   * текста. По мере набора символов вызывается метод с параметром aEditFinished = false, а при завершениии ввода - со
   * значением true. При этом, завершением ввода могут считаться такие события как, например, нажатие Enter и потеря
   * фокуса контролем.
   *
   * @param aSource {@link IValedControl} - редактор - источник сообщения
   * @param aEditFinished boolean - признак завершения редактирования (ввода значения)
   */
  void onEditorValueChanged( IValedControl<?> aSource, boolean aEditFinished );

}
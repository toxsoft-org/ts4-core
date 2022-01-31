package org.toxsoft.core.tsgui.panels.vecboard;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.toxsoft.core.tsgui.panels.lazy.ILazyControl;
import org.toxsoft.core.tslib.utils.errors.TsIllegalStateRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Панель для отложенной инициализации визульных компонент.
 *
 * @author goga
 */
public interface IVecBoard
    extends ILazyControl<Composite> {

  /**
   * Возвращает раскладку контролей на панели.
   * <p>
   * Если раскладка еще не была задана методом {@link #setLayout(IVecLayout)}, возвращает null.
   *
   * @return {@link IVecLayout} - раскладка контролей или null
   */
  IVecLayout<?> getLayout();

  /**
   * Задает раскладку контролей на панели.
   *
   * @param aLayout {@link IVecLayout} - раскладка
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalStateRtException раскладка уже была задана
   */
  void setLayout( IVecLayout<?> aLayout );

  /**
   * Определяет, является ли панель группой контролей (то есть, реализуется контролем {@link Group}).
   * <p>
   * Панель по умолчанию создается как не-группа. Для того, чтобы сделать его группой, надо задать описание группы
   * методом {@link #setGroupBoxInfo(IVecGroupBoxInfo)}.
   *
   * @return boolean - признак оформления панели как группы контроей<br>
   *         <b>true</b> - да, контроль оформлена как группа контролей со свойствами {@link #groupBoxInfo()};<br>
   *         <b>false</b> - это обыная панель, {@link #groupBoxInfo()} вернет null.
   */
  boolean isGroupBox();

  /**
   * Возвращает свойства офоромления (обрамления) панели как группы контролей {@link Group}.
   * <p>
   * Если свойства не были заданы методом {@link #setGroupBoxInfo(IVecGroupBoxInfo)} (то есть, признак
   * {@link #isGroupBox()}=false), метод возвращает null.
   *
   * @return {@link IVecGroupBoxInfo} - свойства офоромления (обрамления) панели как группы контролей или null
   */
  IVecGroupBoxInfo groupBoxInfo();

  /**
   * Задает свойства оформления панели как группы контролей.
   * <p>
   * Сам факт вызова метода делает панель группой (устанавливает признак {@link #isGroupBox()}).
   *
   * @param aGroupBoxInfo {@link IVecGroupBoxInfo} - свойства обрамления панели как группы
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalStateRtException свойства группы уже были заданы
   */
  void setGroupBoxInfo( IVecGroupBoxInfo aGroupBoxInfo );

}

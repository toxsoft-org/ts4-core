package org.toxsoft.core.tsgui.panels.vecboard;

import org.eclipse.swt.widgets.Layout;
import org.toxsoft.core.tsgui.panels.lazy.ILazyControl;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Базовый интерфейс всех видов раскладок панели {@link IVecBoard}.
 * <p>
 * Напомним, что в отличие от SWT-раскладки {@link Layout}, эта раскладка не создает и не оперирует реальными
 * контролями, а только <b>описанием</b> того, как создавать визуальные компоненты во время конструирования родительской
 * панели.
 *
 * @author goga
 * @param <D> - конкретный тип параметров раскладки (layout Data)
 */
public interface IVecLayout<D> {

  /**
   * Возвращает вид раскладки.
   * <p>
   * В зависимости от вид раскладки это интерфейс приводится к одному из IXxxLayout.
   *
   * @return {@link EVecLayoutKind} - вид раскладки
   */
  EVecLayoutKind layoutKind();

  /**
   * Добавляет произвольный SWT-контроль.
   *
   * @param aLazyControl {@link ILazyControl} - создатель контроля
   * @param aLayoutData D - параметры раскладки
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException aControlBuilder уже содержит инициализированный контроль
   * @throws TsRuntimeException при недопустмом сочетании параметров
   */
  void addControl( ILazyControl<?> aLazyControl, D aLayoutData );

}

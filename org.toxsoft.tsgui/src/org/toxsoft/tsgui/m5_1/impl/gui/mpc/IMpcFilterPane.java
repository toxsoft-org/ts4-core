package org.toxsoft.tsgui.m5_1.impl.gui.mpc;

import org.toxsoft.tslib.bricks.events.change.IGenericChangeListener;

import ru.toxsoft.tslib.polyfilter.IPolyFilter;
import ru.toxsoft.tslib.utils.misc.IGenericChangeEventProducer;

/**
 * Интерфейс, которую должна реализовывать панель фильтра.
 * <p>
 * При изменений параметров фильтра, приводящему к изменению {@link #getFilter()}, должно генерироваться сообщение
 * {@link IGenericChangeListener#onGenericChanged(Object)}.
 *
 * @author hazard157
 * @param <T> - displayed M5-modelled entity type
 */
public interface IMpcFilterPane<T>
    extends IMpcPaneBase<T>, IGenericChangeEventProducer {

  /**
   * Возвращает фильтр, заданный в панели.
   *
   * @return {@link IPolyFilter} - фильтр, заданный в панели
   */
  IPolyFilter getFilter();

  /**
   * Возвращает признак, что фильтр включен.
   * <p>
   * На пенели может находится флажок включения/выключения фильтра, в таком случае, метод возвращает состояние этого
   * переключателя.
   * <p>
   * Реализации, которые не предусматриваю такого переключателя, всегда возвращают <code>true</code> и не реагируют на
   * {@link #setFilterOn(boolean)}.
   *
   * @return boolean - признак включения (использования) фильтра
   */
  boolean isFilterOn();

  /**
   * Программно включает/отключает признак фильтрации.
   *
   * @param aOn boolean - признак включения (использования) фильтра
   */
  void setFilterOn( boolean aOn );

}

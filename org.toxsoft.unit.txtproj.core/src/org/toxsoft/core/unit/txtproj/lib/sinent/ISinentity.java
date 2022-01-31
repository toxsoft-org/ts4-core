package org.toxsoft.core.unit.txtproj.lib.sinent;

import org.toxsoft.core.tslib.bricks.events.change.IGenericChangeEventer;
import org.toxsoft.core.tslib.bricks.events.change.IGenericChangeListener;
import org.toxsoft.core.tslib.bricks.strid.IStridable;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link IStridable} сущностей с XxxInfo описанием.
 *
 * @author hazard157
 * @param <F> - тип (класс) информации о сущности
 */
public interface ISinentity<F>
    extends IStridable, Comparable<ISinentity<F>> {

  /**
   * Возвращает информацию о сущности.
   *
   * @return F - информация о сущности
   */
  F info();

  /**
   * Задает информацию о сущности.
   * <p>
   * Генерирует сообщение {@link IGenericChangeListener#onGenericChangeEvent(Object)}.
   *
   * @param ainfo F - информация о сущности
   * @throws TsNullArgumentRtException аргумент = null
   */
  void setInfo( F ainfo );

  /**
   * Returns the change eventer.
   *
   * @return {@link IGenericChangeEventer} - the change eventer
   */
  IGenericChangeEventer eventer();

}

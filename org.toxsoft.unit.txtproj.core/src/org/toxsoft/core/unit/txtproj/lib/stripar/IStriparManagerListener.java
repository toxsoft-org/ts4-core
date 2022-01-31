package org.toxsoft.core.unit.txtproj.lib.stripar;

import org.toxsoft.core.tslib.coll.helpers.ECrudOp;

/**
 * Слушатель событий {@link IStriparManager}.
 *
 * @author hazard157
 */
public interface IStriparManagerListener {

  /**
   * Вызывается при изменениях в списке {@link IStriparManager#items()}.
   *
   * @param aSource {@link IStriparManager} - истоник сообщения
   * @param aOp {@link ECrudOp} - вид измненения
   * @param aId String - идентификатор измененного элемента или <code>null</code> при пактеных изменениях
   */
  void onChanged( IStriparManager<?> aSource, ECrudOp aOp, String aId );

}

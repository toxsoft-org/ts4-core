package org.toxsoft.core.tsgui.ved.editor;

import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;

/**
 * Менеджер положения визуальных элементов.
 * <p>
 *
 * @author vs
 */
public interface IVedViselsPositionManager {

  /**
   * Возвращает список ИДов визуальных элементов, которые необходимо переместить.
   *
   * @param aViselId String - ИД захваченного мышью визуального элемента или <code>null</code>
   * @return IStringList - список ИДов визуальных элементов, которые необходимо переместить
   */
  IStringList listViselIds2Move( String aViselId );

  /**
   * Добавляет процессор.<br>
   * Если такой процессор уже есть, то ничего не делает.
   *
   * @param aProcessor {@link IViselsPositionProcessor} - процессор обрабатывающий перемещение визуальных элементов
   */
  void addProcessor( IViselsPositionProcessor aProcessor );

  /**
   * Перемещает визуальные элементы на величину приращения по SWT координатам X и Y.
   *
   * @param aViselIds {@link IStringList} - список ИДов визуальных элементов
   * @param aSwtDx int - приращение по оси X
   * @param aSwtDy int - приращение по оси Y
   */
  void moveOnSwtIncrement( IStringList aViselIds, int aSwtDx, int aSwtDy );

  /**
   * Перемещает визуальный элемент на величину приращения по SWT координатам X и Y.
   *
   * @param aViselId String - ИД визуального элемента
   * @param aSwtDx int - приращение по оси X
   * @param aSwtDy int - приращение по оси Y
   */
  default void moveOnSwtIncrement( String aViselId, int aSwtDx, int aSwtDy ) {
    IStringListEdit ids = new StringArrayList( aViselId );
    moveOnSwtIncrement( ids, aSwtDx, aSwtDy );
  }

  // FIXME реализовать
  // void alignVisels(String aAnchorViselId, IStringList )
}

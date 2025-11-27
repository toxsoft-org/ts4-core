package org.toxsoft.core.tsgui.bricks.qtree.tmm;

import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.coll.notifier.*;
import org.toxsoft.core.tslib.utils.errors.*;

// TODO TRANSLATE

/**
 * Управление показом элементов в режиме таблицы или дерева с различными группировками.
 * <p>
 * При смене текущего режима {@link #currModeId()} или наличия режима дерева {@link #hasTreeMode()} генерируется
 * сообщение {@link IGenericChangeListener}.
 *
 * @author hazard157
 * @param <T> - displayed M5-modeled entity type
 */
public interface IQTreeModeManager<T>
    extends IGenericChangeEventCapable {

  /**
   * Определяет, есть ли поддержка режимов показа в виде дерева.
   *
   * @return boolean - признак наличия режимов дерева
   */
  boolean hasTreeMode();

  /**
   * Задает (включает/выключает) поддержку режимов показа в виде дерева.
   *
   * @param aValue boolean - признак наличия режимов дерева
   */
  void setHasTreeMode( boolean aValue );

  /**
   * Возвращает все доступные режимы группировки в виде дерева.
   *
   * @return INotifierStridablesList&lt;{@link QTreeModeInfo}&gt; - список доступных режимов группировки в виде дерева
   */
  INotifierStridablesList<QTreeModeInfo<T>> treeModeInfoes();

  /**
   * Задает (добавляет еще один) способ группировки элементов виде дерева.
   * <p>
   * Идентификатор значка используется для метода {@link ITsIconManager#loadStdIcon(String, EIconSize)}.
   *
   * @param aModeInfo {@link QTreeModeInfo} - описание режима группировки в дерево
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   */
  void addTreeMode( QTreeModeInfo<T> aModeInfo );

  /**
   * Удаляет все виды группировок и отменяет режим дерева, переводя панель в режим таблицы.
   */
  void clearTreeModes();

  /**
   * Определяет, является ли текущий режим просмотром дерева.
   * <p>
   * Возвращает результат проверки <code>{@link #currModeId()} != null</code>.
   *
   * @return boolean - признак текущего режима дерева<br>
   *         <b>true</b> - элементы сгруппированы и отображаются в виде дерева;<br>
   *         <b>false</b> - элементы отображаются в виде таблицы.
   */
  boolean isCurrentTreeMode();

  /**
   * Возвращает идентификатор текущего режима дерева или <code>null</code> в режиме таблицы.
   *
   * @return String - идентификатор текущего режима дерева или <code>null</code> в режиме таблицы
   */
  String currModeId();

  /**
   * Возвращает идентификатор режима дерева, использованного в последний раз.
   * <p>
   * Если режим дерева не поддерживается {@link #hasTreeMode()} = <code>false</code> или список
   * {@link #treeModeInfoes()} пустой, возвращает <code>null</code>. Если режим дерева еще ни разу не был задан,
   * возвращает идентификатор первого режима из списка {@link #treeModeInfoes()}.
   *
   * @return String - идентификатор режима дерева, использованного в последний раз или <code>null</code>
   */
  String lastModeId();

  /**
   * Задает идентификатор групировки режима дерева или <code>null</code> для режима таблицы.
   * <p>
   * Вызов метода приводит к немедленному изменению режима отображения.
   *
   * @param aModeId String - идентификатор текущего режима дерева или <code>null</code> для режима таблицы
   * @throws TsItemNotFoundRtException идентификатор не соответствут ни одному режиму дерева
   */
  void setCurrentMode( String aModeId );

}

package org.toxsoft.core.tsgui.m5.gui.viewers;

import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.widgets.Menu;
import org.toxsoft.core.tsgui.bricks.tstree.ITsNode;

// TODO TRANSLATE

/**
 * Слушатель, вызывамый перед показом локального меню просмотрщиков {@link IM5CollectionViewer}.
 * <p>
 * Слушатель вызывается, когда пользователь щелкнет правой кнопкой мыши на просмотрщике (или иным способом вызовет
 * локальное меню). В отличие от {@link MenuDetectListener}, этот слушатель предоставляет больше информации, а именно:
 * <ul>
 * <li>источник {@link IM5CollectionViewer}, от которого пришло сообщение;</li>
 * <li>область, было запрошенго и будет показано меню: заголовок
 * {@link #onHeaderMenu(IM5CollectionViewer, MenuDetectEvent, IM5Column)} или рабочая область
 * {@link #onCellMenu(IM5CollectionViewer, MenuDetectEvent, IM5Column, Object, ITsNode)};</li>
 * <li>оригинальное событие {@link MenuDetectEvent} с координатами указателя (или выделения, фокуса) в момент запроса
 * локального меню;</li>
 * <li>столбец {@link IM5Column}, на которой будеи показано меню или null, если указатель был вне колонок просмотрщика;
 * </li>
 * <li>только для меню рабочей области - элемента в исходном списке элементов {@link IM5CollectionViewer#items()} или
 * null, если указатель был вне строк просмотрщика. Для дерева, также передается узел {@link ITsNode} или null;</li>
 * <li>можно запрещать показ меню возвратом значения false из методов onXxxMenu().</li>
 * </ul>
 *
 * @author goga
 * @param <T> - тип моделированного объекта
 */
public interface IM5MenuDetectHandler<T> {

  /**
   * "Нулевой" слушатель, ничего не делает.
   * <p>
   * Предназначено для передачи в {@link IM5MenuManager#setMenuDetectListener(IM5MenuDetectHandler)} вместо null.
   */
  @SuppressWarnings( "rawtypes" )
  IM5MenuDetectHandler NULL = new InternalNullMenuDetectListener();

  /**
   * Вызывается перед показом локального меню заголовка просмотрщика.
   * <p>
   * Использование этого метода следующее:
   * <ul>
   * <li>исходя из предоставленной информации (просмотрщик-источник, оргинальное событие с координатами и номер
   * колонки), следует создать и настроить локальное меню;</li>
   * <li>созданное меню передается просмотрщике методом {@link IM5MenuManager#setHeaderMenu(Menu)} для показа после
   * возврата из этого меню;</li>
   * <li>вернуть значение true, чтобы меню было показано, или false, чтобы отменить показ меню.</li>
   * </ul>
   * В случае если запрос меню произошло вне столбцов просмотрщика, в качестве аргумента aColumn передается null.
   *
   * @param aSource {@link IM5CollectionViewer} - просмотрщик, источник сообщения
   * @param aEvent {@link MenuDetectEvent} - оригинальное событие (не следует его редактировать!)
   * @param aColumn {@link IM5Column} - столбец, на которой будет показано меню или null
   * @return boolean - показывать ли меню после возврата из этого метода<br>
   *         <b>true</b> - да, запрошенное пользователем локальное меню будет показано;<br>
   *         <b>false</b> - нет, меню не появится.
   */
  boolean onHeaderMenu( IM5CollectionViewer<T> aSource, MenuDetectEvent aEvent, IM5Column<T> aColumn );

  /**
   * Вызывается перед показом локального меню в рабочей области (там, где ячейками с данными) просмотрщика.
   * <p>
   * Использование этого метода следующее:
   * <ul>
   * <li>исходя из предоставленной информации (источник, оргинальное событие с координатами, номер колонки и элемента и
   * или узла), следует создать и настроить локальное меню;</li>
   * <li>созданное меню передается просмотрщику методом {@link IM5MenuManager#setCellMenu(Menu)} для показа после
   * возврата из этого меню;</li>
   * <li>вернуть значение true, чтобы меню было показано, или false, чтобы отменить показ меню.</li>
   * </ul>
   * В случае если запрос меню произошло вне столбцов просмотрщика, в качестве аргумента aColumn передается null.
   * <p>
   * Поскольку этот интерейс используется для таблицы и для дерева, то для дерева имеет смысл аргумент aNode (для дерева
   * всегда null). Для дерева указатель может быть на узле, который не содержит в себе элемент (такие узлы называются
   * агрегирующими). В таком случае, элемент aItem=null, а узел не-null. Для не-null элементов aItem возвращается узел,
   * содержащий этот элемент, то есть, {@link ITsNode#entity()} = aItem.
   *
   * @param aSource {@link IM5CollectionViewer} - просмотрщик, источник сообщения
   * @param aEvent {@link MenuDetectEvent} - оригинальное событие (не следует его редактировать!)
   * @param aColumn {@link IM5Column} - столбец, на которой будет показано меню или null
   * @param aItem &lt;T&gt; - элемент списка {@link IM5CollectionViewer#items()} или null
   * @param aNode {@link ITsNode} - узел дерева или null
   * @return boolean - показывать ли меню после возврата из этого метода<br>
   *         <b>true</b> - да, запрошенное пользователем локальное меню будет показано;<br>
   *         <b>false</b> - нет, меню не появится.
   */
  boolean onCellMenu( IM5CollectionViewer<T> aSource, MenuDetectEvent aEvent, IM5Column<T> aColumn, T aItem,
      ITsNode aNode );

}

class InternalNullMenuDetectListener
    implements IM5MenuDetectHandler<Object> {

  @Override
  public boolean onHeaderMenu( IM5CollectionViewer<Object> aSource, MenuDetectEvent aEvent,
      IM5Column<Object> aColumn ) {
    return false;
  }

  @Override
  public boolean onCellMenu( IM5CollectionViewer<Object> aSource, MenuDetectEvent aEvent, IM5Column<Object> aColumn,
      Object aItem, ITsNode aNode ) {
    return false;
  }

}

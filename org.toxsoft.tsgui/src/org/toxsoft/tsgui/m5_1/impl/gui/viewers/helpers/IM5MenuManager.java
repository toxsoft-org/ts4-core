package org.toxsoft.tsgui.m5_1.impl.gui.viewers.helpers;

import org.eclipse.swt.widgets.Menu;
import org.toxsoft.tsgui.m5_1.impl.gui.viewers.IM5CollectionViewer;

/**
 * Менеджер локальных меню в просмотрщиках {@link IM5CollectionViewer}.
 *
 * @author hazard157
 * @param <T> - displayed M5-modelled entity type
 */
public interface IM5MenuManager<T> {

  /**
   * Возвращает локальное меню заголовка просмотрщика.
   * <p>
   * При щелчке правой кнопкой мыши на заголовке просмотрщика (или другим способом) вызывается это меню. Если даже задан
   * пользовательский слушатель меню (методом {@link #setMenuDetectListener(IM5MenuDetectListener)}), то все из
   * слушателя устанавливается локальное меню заголовка методом {@link #setHeaderMenu(Menu)}, и все равно, вызывается
   * меню, возвращаемое этим методом.
   *
   * @return {@link Menu} - локальное меню заголока или null
   */
  Menu getHeaderMenu();

  /**
   * Задает локальное меню заголовка просмотрщика.
   *
   * @param aMenu {@link Menu} - локальное меню заголовка или null, чтобы не использовать локальное меню
   */
  void setHeaderMenu( Menu aMenu );

  /**
   * Возвращает локальное меню рабочей области (ячеек) просмотрщика.
   * <p>
   * При щелчке правой кнопкой мыши на рабочей области просмотрщика (или другим способом) вызывается это меню. Если даже
   * задан пользовательский слушатель меню (методом {@link #setMenuDetectListener(IM5MenuDetectListener)}), то все из
   * слушателя устанавливается локальное меню рабочей области методом {@link #setCellMenu(Menu)}, и все равно,
   * вызывается меню, возвращаемое этим методом.
   *
   * @return {@link Menu} - локальное меню рабочей области или null
   */
  Menu getCellMenu();

  /**
   * Задает локальное меню рабочей области (ячеек) просмотрщика.
   *
   * @param aMenu {@link Menu} - локальное меню рабочей области (ячеек) или null, чтобы не использовать локальное меню
   */
  void setCellMenu( Menu aMenu );

  /**
   * Устанавливает слушатель, вызываемый перед показом локального меню (как заголовка, так и рабочей области).
   * <p>
   * Перед вызовом локаьного меню вызывается один из методов слушателя. В зависимости от того, где было запрошено меню
   * (на заголовке или в рабочей области), из слушателя следует задать локальное меню одним из методов
   * {@link #setHeaderMenu(Menu)} или {@link #setCellMenu(Menu)}, которое и будет показано.
   *
   * @param aListener {@link IM5MenuDetectListener} - слушатель меню или {@link IM5MenuDetectListener#NULL} для отмены
   */
  void setMenuDetectListener( IM5MenuDetectListener<T> aListener );

}

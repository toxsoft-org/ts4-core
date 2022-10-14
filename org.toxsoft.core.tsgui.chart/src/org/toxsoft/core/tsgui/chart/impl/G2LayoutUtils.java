package org.toxsoft.core.tsgui.chart.impl;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.chart.layouts.*;

/**
 * Вспомогательные методы работы с менеджерами размещения элементов компоненты графиков.
 *
 * @author vs
 */
public class G2LayoutUtils {

  /**
   * Создает менеджер размещения по умолчанию.
   *
   * @param aContext ITsGuiContext - соответствующий контекст
   * @return IDefaultChartLayout - менеджер размещения по умолчанию
   */
  public static IStdChartLayout createDefaultChartLayout( ITsGuiContext aContext ) {
    return new StdChartLayout( aContext );
  }

  /**
   * Запрет на создание экземпляров.
   */
  private G2LayoutUtils() {
    // nop
  }

}

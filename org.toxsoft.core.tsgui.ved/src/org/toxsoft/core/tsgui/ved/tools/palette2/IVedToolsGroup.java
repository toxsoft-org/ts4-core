package org.toxsoft.core.tsgui.ved.tools.palette2;

import org.toxsoft.core.tsgui.ved.core.view.*;
import org.toxsoft.core.tsgui.ved.extra.tools.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;

/**
 * Группа инструментов.
 * <p>
 * Способ организации инструментов в двух-уровневое дерево, чтобы не загромождать панель. Например, такие инструменты
 * как: рисование прямоугольников, прямоугольников со скругленными углами и квадратов могут быть объеденены в одну
 * группу.<br>
 * В каждый момент времени в группе может быть выбран один и только один инструмент, именно он и отображается в панели
 * инструментов.
 *
 * @author vs
 */
public interface IVedToolsGroup
    extends IStridable {

  /**
   * Возвращает список инструментов группы.<br>
   *
   * @return IStridablesList&lt;IVedEditorTool> - список инструментов группы
   */
  IStridablesList<IVedEditorTool> listTools();

  /**
   * Выбранный инструмент внутри группы.<br>
   *
   * @return IVedEditorTool - выбранный инструмент внутри группы
   */
  IVedEditorTool selectedTool();

  /**
   * Выбирает указанный "инструмент".<br>
   *
   * @param aTool IVedEditorTool - инструмент, который должен быть выбран
   */
  void selectTool( IVedEditorTool aTool );

}

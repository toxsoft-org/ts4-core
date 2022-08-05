package org.toxsoft.core.tsgui.ved.tools.palette2;

import org.toxsoft.core.tsgui.ved.core.view.*;
import org.toxsoft.core.tsgui.ved.extra.tools.*;
import org.toxsoft.core.tsgui.ved.utils.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Панель инструментов.
 * <p>
 * По поведению является аналогом группы radio-buttons.То есть в каждый момент времени активным (выбранным) может быть
 * один и только один инcтрумент.<br>
 * Порядок следования элементов в панели определяется порядком вызовов функций {@linkplain #createGroup(String)} и
 * {@linkplain #addTool(IVedEditorTool, String)} с нулевым идентификатором группы.
 *
 * @author vs
 */
public interface IVedToolsPanel
    extends IVedToolSelectionEventProducer, IVedContextable {

  /**
   * Создает содержимое панели, в соотвествии с текущим списком групп и инструментов.<br>
   */
  void createContent();

  /**
   * Возвращает активный инструмент.<br>
   *
   * @return IEditorTool - активный инструмент
   */
  IVedEditorTool activeTool();

  /**
   * Возвращает список всех инструментов.
   *
   * @return IStridablesList&lt;IVedEditorTool> - список всех инструментов
   */
  IStridablesList<IVedEditorTool> listTools();

  /**
   * Возвращает идентификатор группы, в которой находится инструмент или null если инструмент вне группы.<br>
   *
   * @param aToolId String - идентификатор инструмента
   * @return String - идентификатор группы, в которой находится инструмент или null если инструмент вне группы
   */
  String findGroupId( String aToolId );

  /**
   * Добавляет "инструмент" в группу.<br>
   *
   * @param aTool IVedEditorTool - добавляемый инструмент
   * @param aGroupId String - идентификатор группы
   * @throws TsItemNotFoundRtException в случае отсутсвия группы с переданным ИДом
   * @throws TsItemAlreadyExistsRtException если такой инструмент уже присутствует в группе
   */
  void addTool( IVedEditorTool aTool, String aGroupId );

  /**
   * Создает и возвращает группу с переданным ИДом.<br>
   *
   * @param aId String - ИД группы
   * @return IVedToolsGroup - созданная группа
   * @throws TsItemAlreadyExistsRtException если группа с таким ИДом уже есть
   */
  IVedToolsGroup createGroup( String aId );

}

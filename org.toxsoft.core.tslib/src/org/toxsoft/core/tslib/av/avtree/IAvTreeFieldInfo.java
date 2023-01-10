package org.toxsoft.core.tslib.av.avtree;

import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.*;

/**
 * Описание одного поля в узле дерева {@link IAvTree}.
 * <p>
 * Этот интерфейс реализует {@link IStridable}, поля которого имеют следующий смысл:
 * <ul>
 * <li><b>id</b>() - уникальный (среди других полей объекта) идентификатор поля (ИД-путь);</li>
 * <li><b>description</b>() - удобочитаемое описание поля (используется например, как всплывающая подсказка, Tooltip,
 * пользователю при показе/редактировании значения поля).</li>
 * <li><b>name</b>() - удобочитаемое, краткое имя поля (используется например, как имя столбца в таблице или как подпись
 * (Label) к виджету на панели при показе/редактировании значения поля).</li>
 * </ul>
 *
 * @author hazard157
 */
public interface IAvTreeFieldInfo
    extends IStridable {

  /**
   * Возвращает тип данных поля.
   *
   * @return {@link IDataType} - тип поля
   */
  IDataType fieldType();

  /**
   * Возвращает набор произвольных дополнительных опции поля.
   * <p>
   * Например, тут может храниться информация о графическом контроле для редактирования данного значения.
   *
   * @return {@link IOptionSet} - набор произвольных дополнительных опции поля
   */
  IOptionSet extraOps();

}

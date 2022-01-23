package org.toxsoft.tsgui.m5_3.gui.viewers;

import org.eclipse.swt.SWT;
import org.toxsoft.tsgui.m5_3.IM5FieldDef;
import org.toxsoft.tslib.utils.ESortOrder;
import org.toxsoft.tslib.utils.errors.*;

/**
 * Управление сотрировкой в таблице {@link IM5CollectionViewer}.
 *
 * @author goga
 */
public interface IM5SortManager {

  /**
   * Возвращает текущий порядок сортировки талицы.
   * <p>
   * ru.toxsoft.tsgui.m3.gui.viewers Обращаем внимание, что сортировка таблицы и сортировка самой модели данных - это
   * две разные сортировки.
   *
   * @return {@link ESortOrder} - порядок сортировки
   */
  ESortOrder sortOrder();

  /**
   * Возвращает идентификатор поля, по которому происходит сортировка.
   *
   * @return String - идентификатор поля, по которому происходит сортировка
   * @throws TsIllegalStateRtException порядок сортировки не задан ({@link #sortOrder()} = {@link ESortOrder#NONE})
   */
  String sortFieldId();

  /**
   * Сортирует таблицу в заданном порядке по указанному полю.
   * <p>
   * Внимание: указанное поле может не поддерживать сортировку (то есть, {@link IM5FieldDef#comparator()} =
   * <code>null</code>. В таком случае, метод не возвращает исключение - только элементы останутся не отсортиованными.
   * <p>
   * Если задан порядок сортировки {@link SWT#NONE}, то сортировка отключается, и это эквивалентно вызову
   * {@link #unsort()}.
   * <p>
   * Поле должно находится следи колонок таблицы.
   * <p>
   * Значение идентификатор поля aFieldId игнорируется при отключении сортировки, когда aOrder = {@link ESortOrder#NONE}
   * , и в таком случае допускаетмся чтобы aFieldId = null.
   *
   * @param aOrder {@link ESortOrder} - порядок сортировки
   * @param aFieldId String - идентификатор поля сортировки
   * @throws TsNullArgumentRtException aOrder = null
   * @throws TsNullArgumentRtException aFieldId = null (когда aOrder != {@link ESortOrder#NONE})
   * @throws TsNullArgumentRtException aSortOrder = null
   * @throws TsItemNotFoundRtException нет такого поля среди колонок таблицы
   */
  void sort( ESortOrder aOrder, String aFieldId );

  /**
   * Убирает сортировку таблицы (делает {@link #sortOrder()}= {@link ESortOrder#NONE}).
   */
  void unsort();

}

package org.toxsoft.core.tsgui.m5.gui.viewers;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tsgui.utils.checkcoll.*;
import org.toxsoft.core.tslib.coll.notifier.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * M5 viewers base interface.
 *
 * @author hazard157
 * @param <T> - modeled entity type
 */
public interface IM5CollectionViewer<T>
    extends //
    IM5ModelRelated<T>, // bind to the model
    ILazyControl<Control>, // this SWT control will be initialized lazely
    ITsSelectionProvider<T>, // user is informed about change of elements selection
    ITsDoubleClickEventProducer<T>, // user can handle mouse double click on viewer
    ITsKeyInputProducer, // user can handle key events on viewer
    ITsCheckSupportable<T>, // possible elements check state support
    IThumbSizeableEx, // manages thumbs size (if applicable)
    IIconSizeableEx, // manages icons size (if applicable)
    ITsGuiContextable // ITsGuiContext
{

  // TODO TRANSLATE

  /**
   * Возвращает редактируемый список элементов.
   * <p>
   * Каждое изменение в списке приводит к изменению в отображении, поэтому следует стараться изменения производить в
   * пакетном режиме. В случае внесения изменения в элемент списка (без правки самого списка) следует известить вьюер
   * методами <code>{@link INotifierListEdit}.fireItemByXxxChangeEvent()</code>.
   * <p>
   * Метод возвращает нефильтрованный список элементов.
   *
   * @return {@link INotifierListEdit}&lt;T&gt; - редактируемый список элементов
   */
  INotifierListEdit<T> items();

  /**
   * Возвращает менеджер управления колонками просмотрщика.
   *
   * @return {@link IM5ColumnManager} - менеджер управления колонками просмотрщика
   */
  IM5ColumnManager<T> columnManager();

  /**
   * Возвращает менеджер управления сортировкой строк просмотрщика.
   *
   * @return {@link IM5ColumnManager} - менеджер управления сортировкой строк просмотрщика
   */
  IM5SortManager sortManager();

  /**
   * Возвращает менеджер управления фильтрацией строк и отфильтрованными элементами просмотрщика.
   *
   * @return {@link IM5FilterManager} - менеджер фильтрации в просмотрщике
   */
  IM5FilterManager<T> filterManager();

  /**
   * Возвращает менеджер локальных меню.
   *
   * @return {@link IM5MenuManager} - менеджер локальных меню
   */
  IM5MenuManager<T> menuManager();

  // ------------------------------------------------------------------------------------
  //

  /**
   * Показывает указанный элемент в просмотрщике (прокручивает просмотрщик так, чтобы строка была видима).
   *
   * @param aItem &lt;T&gt; - показываемый элемент
   * @throws TsNullArgumentRtException аргумент = null
   */
  void reveal( T aItem );

  /**
   * Возвращает элемент модели данных, который является первым (свурху) видимым элементом в просмотрщике.
   *
   * @return &lt;T&gt; - первый видимый элемент или null, если нет видимых элементов
   */
  T getTopItem();

  /**
   * Возвращает количество строк (элементов), видимых в просмотрщике.
   * <p>
   * Точнее, возвращается количество строк, которое может отображаться одновременно. Грубо говоря, это высота рабочей
   * области разделить на высоту одной строки.
   * <p>
   * <b>Внимание:</b> возвращаемое количество весьма ориентировочное, полагаться на точное соответствие ральности
   * нельзя.
   *
   * @return int - количество строк, которое помещается в рабочей области просмотрщика
   */
  int getVisibleRowsCount();

  /**
   * Обновляет визуальное содержимое просмотрщика в соответствии с набором элементов {@link #items()}.
   * <p>
   * В обычном режиме, просмотрщик сама отслеживает изменения в {@link #items()}, и обновляет себя. Но если пользователь
   * отключит генерацию сообщений методом {@link INotifierList#pauseFiring()}, например, для множестенных изменений, то
   * просмотрщик перестает обновляться. Если пользователь завершит обновления методом
   * {@link INotifierList#resumeFiring(boolean)} с аргументом <code>false</code> (чтобы другие слушатели не были
   * извещены), то и просмотрщик остается не обновленной. В таком случае и нужно вызвать {@link #refresh()}.
   */
  void refresh();

  /**
   * Задает поставщика шрифта для ячеек просмотрщика или null, если используются только шрифты по умолчанию.
   * <p>
   * Поставщик шрифта в качестве элемента ожидает объекты типа &lt;T&gt;.
   *
   * @param aFontProvider {@link ITableFontProvider} - поставщик шрифтов ячеек или null
   */
  void setFontProvider( ITableFontProvider aFontProvider );

  /**
   * Задает поставщика цветов для ячеек просмотрщика или null, если используются только цвета по умолчанию.
   * <p>
   * Поставщик шрифта в качестве элемента ожидает объекты типа &lt;T&gt;.
   *
   * @param aColorProvider {@link ITableColorProvider} - поставщик цветов ячеек или null
   */
  void setColorProvider( ITableColorProvider aColorProvider );

  /**
   * Determines if columns header row is visible in viewer.
   *
   * @return boolean - column header visibility flag
   */
  boolean isColumnHeaderVisible();

  /**
   * Sets if columns header row is visible in viewer.
   *
   * @param aVisible boolean - column header visibility flag
   */
  void setColumnHeaderVisible( boolean aVisible );

}

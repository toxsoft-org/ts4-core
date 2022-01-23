package org.toxsoft.tsgui.utils.jface;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.*;
import org.toxsoft.tsgui.utils.swt.ISwt;
import org.toxsoft.tslib.utils.errors.TsIllegalStateRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

// TODO TRANSLATE

/**
 * Класс для облегчения реализации пользотаельской отрисовки {@link Table} и {@link Tree} в RCP.
 * <p>
 * Класс предназначен для облегчения реализации таблиц с пользовательской отрисовкой. После привязки помощника к таблице
 * методом {@link #install(Composite)}, позволяет осуществить указанные в аргументах конструктора действия.
 * <p>
 * Напомним, что рисование ячеек таблицы осуществляется в три этапа:
 * <ul>
 * <li>определение желаемых размеров ячейки (сообщение {@link ISwt#MeasureItem});</li>
 * <li>рисование фона ячейки (сообщение {@link ISwt#EraseItem});</li>
 * <li>после отрисовки переднего плана по умолчанию, есть возможность дополнить, или полностью его перерысовать
 * (сообщение {@link ISwt#PaintItem}).</li>
 * </ul>
 * <p>
 * Внимание: этот класс работает только в RCP, хотя компилируется и в RAP.
 * <p>
 * Обратите внимание,что ниже в комментариях для иллюстрации пояснений используется класс {@link TableItem}, но все
 * также применимо к {@link TreeItem}.
 *
 * @author goga
 * @param <T> - конкретный тип {@link Table} или {@link Tree}
 */
public class ViewerPaintHelper<T extends Composite> {

  private final Listener paintListener = aEvent -> doPaintItem( aEvent );

  private final Listener eraseListener = aEvent -> aEvent.doit = doEraseItem( aEvent );

  private final Listener measureListener = aEvent -> doMeasureItem( aEvent );

  private final boolean overridePaint;
  private final boolean overrideErase;
  private final boolean overrideMeasure;
  private T             owner = null;

  /**
   * Создает помощник с указанием осуществляемых функции.
   * <p>
   * В зависимости от значений аргументов осуществляет следующие функцииЖ
   * <ul>
   * <li>aPaint=<code>true</code> - перехватывает {@link ISwt#PaintItem} и позволяет дорисовать свое поверх уже
   * отрисовываного переднего плана. Отрисовка происходит переопределением метода {@link #doPaintItem(Event)};</li>
   * <li>aErase=<code>true</code> - перехватывает {@link ISwt#EraseItem} и позволяет изменить рисование фона
   * переопределенным методом {@link #doEraseItem(Event)};</li>
   * <li>aMeasure=<code>true</code> - перехватывает {@link ISwt#MeasureItem} и позволяет изменить размеры ячейки.</li>
   * </ul>
   *
   * @param aPaint bolean - признак (до)рисования переднего плана ячейки
   * @param aErase bolean - признак рисования фона ячейки
   * @param aMeasure bolean - признак задания размера ячейки
   */
  public ViewerPaintHelper( boolean aPaint, boolean aErase, boolean aMeasure ) {
    overridePaint = aPaint;
    overrideErase = aErase;
    overrideMeasure = aMeasure;
  }

  /**
   * Привязывает помощник к таблице/дереву.
   *
   * @param aOwner &lt;T&gt; - таблица/дерево, к которой привязывается помощник
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalStateRtException помощник уже привязан к таблице
   */
  public void install( T aOwner ) {
    TsNullArgumentRtException.checkNull( aOwner );
    TsIllegalStateRtException.checkNoNull( owner );
    owner = aOwner;
    if( overridePaint ) {
      owner.addListener( ISwt.PaintItem, paintListener );
    }
    if( overrideErase ) {
      owner.addListener( ISwt.EraseItem, eraseListener );
    }
    if( overrideMeasure ) {
      owner.addListener( ISwt.MeasureItem, measureListener );
    }
  }

  /**
   * Отвязывает помощник от привязки, сделанной методом {@link #install(Composite)}.
   * <p>
   * Если помощник не привязан к таблице, метод ничего не делает.
   */
  public void deinstall() {
    if( owner == null ) {
      return;
    }
    if( overridePaint ) {
      owner.removeListener( ISwt.PaintItem, paintListener );
    }
    if( overrideErase ) {
      owner.removeListener( ISwt.EraseItem, eraseListener );
    }
    if( overrideMeasure ) {
      owner.removeListener( ISwt.MeasureItem, measureListener );
    }
    owner = null;
  }

  // ------------------------------------------------------------------------------------
  // Вспомогательные мтоды для облегчения реализации методов doXxxItem()
  //

  /**
   * Определяет, установлел ли запрошенный бит в поле {@link Event#detail}.
   *
   * @param aEvent {@link Event} - проверяемое событие
   * @param aBit int - запрашиваемый бит (как битовая маска, то есть, один бит, выставленный из 32-битов int-а)
   * @return boolean - признак, что запрошенный бит установлен
   */
  public boolean hasBit( Event aEvent, int aBit ) {
    return (aEvent.detail & aBit) != 0;
  }

  /**
   * Выставляет в {@link Event#detail} запрошенный бит в 1.
   *
   * @param aEvent {@link Event} - проверяемое событие
   * @param aBit int - запрашиваемый бит (как битовая маска, то есть, один бит, выставленный из 32-битов int-а)
   */
  public void setBit( Event aEvent, int aBit ) {
    aEvent.detail = aEvent.detail | aBit;
  }

  /**
   * Сбрасывает в {@link Event#detail} запрошенный бит в 0.
   *
   * @param aEvent {@link Event} - проверяемое событие
   * @param aBit int - запрашиваемый бит (как битовая маска, то есть, один бит, выставленный из 32-битов int-а)
   */
  public void resetBit( Event aEvent, int aBit ) {
    aEvent.detail = aEvent.detail & (~aBit);
  }

  /**
   * Извлекает элемент таблицы из события.
   *
   * @param aEvent {@link Event} - событие
   * @return {@link TabItem} - элемент таблицы
   */
  public TableItem getTableItem( Event aEvent ) {
    return (TableItem)aEvent.item;
  }

  /**
   * Возвращает пользовательский элемент из
   *
   * @param <E> - класс пользовательского элемента
   * @param aEvent {@link Event} - событие
   * @param aItemClass {@link Class}&lt;E&gt; - класс пользовательского элемента
   * @return &lt;T&gt; - пользовательский элемент
   */
  public <E> E getItem( Event aEvent, Class<E> aItemClass ) {
    Object o = getTableItem( aEvent ).getData();
    return aItemClass.cast( o );
  }

  // ------------------------------------------------------------------------------------
  // Методы для переопределения наследниками
  //

  /**
   * Наследник может изменить заданные по умолчанию размеры ячейки.
   * <p>
   * На входе используются следующие поля:
   * <ul>
   * <li>{@link Event#item} - приводится к {@link TableItem}, соответствующий рисуемой строке;</li>
   * <li>{@link Event#index} - индекс столбца ячейки ({@link Table#getColumn(int)});</li>
   * <li>{@link Event#width} - ширина ячейки в пикселях по умолчанию;</li>
   * <li>{@link Event#height} - высота ячейки в пикселях по умолчанию;</li>
   * <li>{@link Event#gc} - канва {@link GC}, со сконфигурированным шрифтом, полезно для определения размеров текста.
   * </li>
   * </ul>
   * <p>
   * Метод может менять следующие поля:
   * <ul>
   * <li>{@link Event#width} - новая ширина ячейки в пикселях;</li>
   * <li>{@link Event#height} - новая высота ячейки в пикселях;</li>
   * </ul>
   * <p>
   * В базовом классе метод ничего не делает, при переопределении родительский метод вызывать не нужно.
   *
   * @param aEvent {@link Event} - событие SWT
   */
  protected void doMeasureItem( Event aEvent ) {
    // nop
  }

  /**
   * Наследник может изменить рисования фона и другие параметры отрисвоки.
   * <p>
   * На входе используются следующие поля:
   * <ul>
   * <li>{@link Event#item} - приводится к {@link TableItem}, соответствующий рисуемой строке;</li>
   * <li>{@link Event#index} - индекс столбца ячейки ({@link Table#getColumn(int)});</li>
   * <li>{@link Event#x} - X-координата левого верхнего угла ячейки (то есть, области отрисовки) внутри таблицы;</li>
   * <li>{@link Event#y} - Y-координата левого верхнего угла ячейки (то есть, области отрисовки) внутри таблицы;</li>
   * <li>{@link Event#width} - ширина ячейки в пикселях;</li>
   * <li>{@link Event#height} - высота ячейки в пикселях;</li>
   * <li>{@link Event#gc} - канва {@link GC}, со сконфигурированным шрифтом, цветами фона и переднего плана, а также
   * области обрезки (clipping area). То есть, можно сразу рисовать от координат {@link Event#x} и {@link Event#y};</li>
   * <li>{@link Event#detail} - набор битовых полей:
   * <ul>
   * <li>{@link SWT#SELECTED} - ячейка является выбранной;</li>
   * <li>{@link SWT#FOCUSED} - ячейка имеет фокус;</li>
   * <li>{@link SWT#HOT} - мышь находится над ячейкой.</li>
   * </ul>
   * </li>
   * </ul>
   * <p>
   * Метод может менять следующие поля:
   * <ul>
   * <li>{@link Event#doit} - осуществлять ли рисование согласно битам, указанным в {@link Event#detail}. Поле
   * устанавливается родителем согласно возвращаемому этим методом, в теле метода выставлять значение поля не имеет
   * смысла;</li>
   * <li>{@link Event#detail} - набор битовых полей, показывает, что надо рисовать после возврата из этого метода:
   * <ul>
   * <li>{@link SWT#FOREGROUND} - надо ли рисовать передный план. Если бит выставлен, то рисуется передный план по
   * умолчанию, а потом, если задано, вызывается метод {@link #doPaintItem(Event)}. Усли бит установлен, то сначала
   * нарисуется содержимое по умолчанию, а потом вызоветься {@link #doPaintItem(Event)}. Если бит сброшен, рисования по
   * умолчанию не будет, а только методом {@link #doPaintItem(Event)};</li>
   * <li>{@link SWT#BACKGROUND} - надо ли рисовать фон по умолчанию. То есть, после вызова этого метода будет рисоваться
   * фон по умолчанию, как задано в {@link TableItem#setBackground(Color)} или
   * {@link TableItem#setBackground(int, Color)}.</li>
   * </ul>
   * </li>
   * </ul>
   * <p>
   * В базовом классе метод просто возвращает <code>true</code>, при переопределении родительский метод вызывать не
   * нужно.
   *
   * @param aEvent {@link Event} - событие SWT
   * @return boolean - признак, что надо продолжить рисование, согласно битам поля {@link Event#detail}
   */
  protected boolean doEraseItem( Event aEvent ) {
    return true;
  }

  /**
   * Наследник может дорисовать содержимое ячеки.
   * <p>
   * В момент вызова этого метода уже отрисван фон, и если пользователь не запретил, также и передный план по умолчанию.
   * <p>
   * На входе используются следующие поля:
   * <ul>
   * <li>{@link Event#item} - приводится к {@link TableItem}, соответствующий рисуемой строке;</li>
   * <li>{@link Event#index} - индекс столбца ячейки ({@link Table#getColumn(int)});</li>
   * <li>{@link Event#x} - X-координата левого верхнего угла ячейки (то есть, области отрисовки) внутри таблицы;</li>
   * <li>{@link Event#y} - Y-координата левого верхнего угла ячейки (то есть, области отрисовки) внутри таблицы;</li>
   * <li>{@link Event#width} - ширина ячейки в пикселях;</li>
   * <li>{@link Event#height} - высота ячейки в пикселях;</li>
   * <li>{@link Event#gc} - канва {@link GC}, со сконфигурированным шрифтом, цветами фона и переднего плана, а также
   * области обрезки (clipping area). То есть, можно сразу рисовать от координат {@link Event#x} и {@link Event#y};</li>
   * <li>{@link Event#detail} - набор битовых полей:
   * <ul>
   * <li>{@link SWT#SELECTED} - ячейка является выбранной;</li>
   * <li>{@link SWT#FOCUSED} - ячейка имеет фокус;</li>
   * <li>{@link SWT#HOT} - мышь находится над ячейкой.</li>
   * </ul>
   * </li>
   * </ul>
   * <p>
   * <b>Внимание:</b> есть особенности определения координат области рисование (с чем они явязаны, я не знаю): нельзя
   * напрямую использовать {@link Event#x} и {@link Event#y}, это дает ошибку, особенно, для первого столбца. Лучше
   * определить прямоугольник, соответствующий рисуемой ячейке методом {@link TreeItem#getBounds(int)} (или
   * {@link TableItem#getBounds(int)}) которым в качестве аргумента передается {@link Event#index}. Это методы
   * возвращают прямоугольник, соответствующий рисуемой ячейке. Напомним, что к {@link TreeItem} (или к
   * {@link TableItem}) приводится поле {@link Event#item} события.
   * <p>
   * В базовом классе метод ничего не делает, при переопределении родительский метод вызывать не нужно.
   *
   * @param aEvent {@link Event} - событие SWT
   */
  protected void doPaintItem( Event aEvent ) {
    // nop
  }

}

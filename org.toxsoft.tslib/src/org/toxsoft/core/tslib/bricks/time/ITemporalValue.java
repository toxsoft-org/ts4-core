package org.toxsoft.core.tslib.bricks.time;

import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.av.temporal.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper;
import org.toxsoft.core.tslib.bricks.time.impl.TemporalValueBase;
import org.toxsoft.core.tslib.utils.errors.TsNullObjectErrorRtException;

/**
 * Значение чего-либо в конкретный момент времени.
 * <p>
 * Назначение интерфеса - служить базой для не-шаблонных интерфейсов с указанием конкретного типа значения.
 * <p>
 * По своей сути, этот интерфейс (точнее, конкретные наследники) являются классами-значением, как {@link String},
 * {@link Integer} или {@link IAtomicValue}. Поэтому, надо выполнять следующие правила при наследовании:
 * <ul>
 * <li>указать в описании, что интерфес-наследник <code>ITemporalXxxValue</code> не предназначен для реализации
 * клиентами;</li>
 * <li>в <code>ITemporalXxxValue</code> надо реализовать паттерн "нулевого" объекта, то есть, объявить константу NULL,
 * аналогично {@link IAtomicValue#NULL}, со классом <b>InternalNullTemporalXxxValue</b>, у которого методы
 * {@link #timestamp()} и {@link #value()} выбрасывают исключение {@link TsNullObjectErrorRtException};</li>
 * <li>сделать единственную реализацию - класс <code>TemporalXxxValue</code> (унаследовавшись от
 * {@link TemporalValueBase});</li>
 * <li>указать в комментарии к методу {@link #value()}, может ли он возвращаеть null;</li>
 * <li>реализовать типовой хранитель <code>TemporalXxxValueKeeper</code> унаследовавшись от класса
 * {@link AbstractEntityKeeper}.</li>
 * </ul>
 * Как пример, можно посмотреть реализацию {@link ITemporalAtomicValue}, {@link TemporalAtomicValue} и
 * {@link TemporalAtomicValueKeeper}.
 *
 * @author hazard157
 * @param <E> - конкретный тип значения во времени
 */
public interface ITemporalValue<E>
    extends ITemporal<ITemporalValue<E>> {

  /**
   * Возвращает значение в указанный момент времени.
   * <p>
   * Может ли быть возвращаемое значение null, зависит от наследника и конкретного типа. Общее правило заключается в
   * том, что если тип &lt;E&gt; реализует паттерн "нулевого" объекта, то метод <b>не</b> возвращает null, а это
   * "нулевое" значение. Например, для {@link IAtomicValue} это метод вместо null будет возвращаеть
   * {@link IAtomicValue#NULL}. В любом случае, наследник <b>обязан</b> документировать возможность возврата null.
   *
   * @return &lt;E&gtl - значение в указанный момент времени
   */
  E value();

}

package org.toxsoft.core.tslib.av.opset;

import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.coll.helpers.ECrudOp;
import org.toxsoft.core.tslib.coll.notifier.INotifierMap;
import org.toxsoft.core.tslib.coll.notifier.basis.ITsCollectionChangeListener;

/**
 * Набор именованных переменных с возможностю извещения об изменениях.
 * <p>
 * Обратите внимание, что в сообщениях этого класса
 * {@link ITsCollectionChangeListener#onCollectionChanged(Object, ECrudOp, Object)} аргумент aItem имеет тип
 * {@link String} и является именем одной из переменных {@link #keys()}.
 *
 * @author hazard157
 */
public interface INotifierOptionSet
    extends IOptionSet, INotifierMap<String, IAtomicValue> {

  // nop

}

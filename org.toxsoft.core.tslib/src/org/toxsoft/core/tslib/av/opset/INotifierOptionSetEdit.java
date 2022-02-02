package org.toxsoft.core.tslib.av.opset;

import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.coll.helpers.ECrudOp;
import org.toxsoft.core.tslib.coll.notifier.INotifierMapEdit;
import org.toxsoft.core.tslib.coll.notifier.basis.ITsCollectionChangeListener;

/**
 * Редактируемый набор именованных переменных с возможностю извещения об изменениях.
 * <p>
 * Обратите внимание, что в сообщениях этого класса
 * {@link ITsCollectionChangeListener#onCollectionChanged(Object, ECrudOp, Object)} аргумент aItem имеет тип
 * {@link String} и является именем одной из переменных {@link #keys()}.
 *
 * @author hazard157
 */
public interface INotifierOptionSetEdit
    extends IOptionSetEdit, INotifierOptionSet, INotifierMapEdit<String, IAtomicValue> {

  // nop

}

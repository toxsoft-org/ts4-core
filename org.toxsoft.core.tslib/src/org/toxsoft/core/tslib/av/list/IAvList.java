package org.toxsoft.core.tslib.av.list;

import java.io.ObjectStreamException;

import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.impl.ImmutableList;
import org.toxsoft.core.tslib.utils.TsLibUtils;

/**
 * Read-only interface of list collection of {@link IAtomicValue}.
 *
 * @author hazard157
 */
public interface IAvList
    extends IList<IAtomicValue> {

  /**
   * Singleton of always empty uneditable (immutable) list.
   */
  IAvList EMPTY = new InternalNullAvList();

  @Override
  IAtomicValue[] toArray();

}

/**
 * Internal class for {@link IAvList#EMPTY} singleton implementation.
 *
 * @author hazard157
 */
class InternalNullAvList
    extends ImmutableList<IAtomicValue>
    implements IAvList {

  private static final long serialVersionUID = 157157L;

  /**
   * This method guarantees that serialized {@link IAvList#EMPTY} will be read correctly.
   *
   * @return Object - always {@link IAvList#EMPTY}
   * @throws ObjectStreamException never thrown
   */
  @SuppressWarnings( { "static-method" } )
  private Object readResolve()
      throws ObjectStreamException {
    return IAvList.EMPTY;
  }

  @Override
  public IAtomicValue[] toArray() {
    return TsLibUtils.EMPTY_AV_ARRAY;
  }
}

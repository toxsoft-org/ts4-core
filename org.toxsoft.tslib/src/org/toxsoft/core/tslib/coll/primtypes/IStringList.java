package org.toxsoft.core.tslib.coll.primtypes;

import java.io.ObjectStreamException;

import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.impl.ImmutableList;
import org.toxsoft.core.tslib.utils.TsLibUtils;

/**
 * Read-only interface of list collection of {@link String}.
 *
 * @author hazard157
 */
public interface IStringList
    extends IList<String> {

  /**
   * Singleton of always empty uneditable (immutable) list.
   */
  IStringListEdit EMPTY = new InternalNullStringList();

  @Override
  String[] toArray();

}

/**
 * Internal class for {@link IStringList#EMPTY} singleton implementation.
 *
 * @author hazard157
 */
class InternalNullStringList
    extends ImmutableList<String>
    implements IStringListEdit {

  private static final long serialVersionUID = 157157L;

  /**
   * This method guarantees that serialized {@link IStringList#EMPTY} will be read correctly.
   *
   * @return Object - always {@link IStringList#EMPTY}
   * @throws ObjectStreamException never thrown
   */
  @SuppressWarnings( { "static-method" } )
  private Object readResolve()
      throws ObjectStreamException {
    return IStringList.EMPTY;
  }

  @Override
  public String[] toArray() {
    return TsLibUtils.EMPTY_ARRAY_OF_STRINGS;
  }
}

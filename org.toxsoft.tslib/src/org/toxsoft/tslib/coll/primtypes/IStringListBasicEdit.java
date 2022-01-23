package org.toxsoft.tslib.coll.primtypes;

import org.toxsoft.tslib.coll.IListBasicEdit;

/**
 * Editable extension of {@link IStringList}.
 * <p>
 * This interface adds basic editing methods to {@link IStringList}. Editing methods is applicable to the sorted
 * collections.
 *
 * @author hazard157
 */
public interface IStringListBasicEdit
    extends IStringList, IListBasicEdit<String> {

  // nop

}

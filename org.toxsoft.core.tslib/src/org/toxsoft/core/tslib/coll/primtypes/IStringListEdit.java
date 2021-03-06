package org.toxsoft.core.tslib.coll.primtypes;

import org.toxsoft.core.tslib.coll.IListEdit;

/**
 * Editable list of {@link String} elements.
 * <p>
 * Extends {@link IStringListBasicEdit} with editing methods applicable only to unsorted collections.
 *
 * @author hazard157
 */
public interface IStringListEdit
    extends IStringListBasicEdit, IListEdit<String> {

  // nop

}

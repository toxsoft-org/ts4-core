package org.toxsoft.unit.txtproj.core;

import org.toxsoft.tslib.bricks.events.change.IGenericChangeEventCapable;
import org.toxsoft.tslib.bricks.keeper.IKeepableEntity;
import org.toxsoft.tslib.coll.basis.ITsClearable;

/**
 * The textual project file unit.
 *
 * @author hazard157
 */
public interface IProjDataUnit
    extends //
    IKeepableEntity, // may be saved/loaded in textual form
    ITsClearable, // may be cleared (reset to empty state)
    IGenericChangeEventCapable // generates content change notifications
{

  // nop

}

package org.toxsoft.core.txtproj.lib;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.coll.basis.*;

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

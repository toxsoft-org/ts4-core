package org.toxsoft.core.tslib.bricks.filebound;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.coll.basis.*;

/**
 * Content in the memory which may be saved as textfile with {@link IKeepableEntity} means.
 * <p>
 * Any entoty implementing this interface may be used with {@link IKeepedContentFileBound} to easlily implement New /
 * Open / Save / SaveAs commands. When used with {@link IKeepedContentFileBound} then all listed operations must be
 * performed by means of {@link IKeepedContentFileBound} methods.
 *
 * @author hazard157
 */
public interface IKeepedContent
    extends //
    ITsClearable, // to implement "New" command
    IKeepableEntity, // to implement "Open" / "Save" / "Save as" commands
    IGenericChangeEventCapable // inform about user edits
{

  /**
   * Resets content of the {@link IKeepedContent} as it was newly created.
   * <p>
   * Needed to implement "New" command.
   */
  @Override
  void clear();

}

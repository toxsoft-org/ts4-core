package org.toxsoft.core.tsgui.ved.incub;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

/**
 * Arbitrary KTOR sections to be stored as textual data using KTOR {@link IEntityKeeper} format.
 * <p>
 * KTOR section is simply identified String content as defined by {@link StrioUtils#readInterbaceContent(IStrioReader)}.
 * <p>
 * This is read-only interface.
 *
 * @author hazard157
 */
public interface IKtorSectionsContainer {

  /**
   * Returns the contained sections.
   *
   * @return {@link IListEdit}&lt;String&gt; - map "section ID" - "section content"
   */
  IStringMap<String> sections();

}

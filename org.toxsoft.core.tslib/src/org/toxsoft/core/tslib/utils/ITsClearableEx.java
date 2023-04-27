package org.toxsoft.core.tslib.utils;

import org.toxsoft.core.tslib.coll.basis.*;

/**
 * Extends {@link ITsClearable} with the {@link #isClearContent()} flag.
 *
 * @author hazard157
 */
public interface ITsClearableEx
    extends ITsClearable {

  /**
   * Determines if content is cleared.
   * <p>
   * By definition, calling {@link #clear()} on already cleared content does nothing and has no side effects. While
   * clearing non-clear content alwayes leads to content change.
   *
   * @return boolean - the flag of the clear content
   */
  boolean isClearContent();

}

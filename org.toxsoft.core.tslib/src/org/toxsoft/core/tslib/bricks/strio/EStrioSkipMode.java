package org.toxsoft.core.tslib.bricks.strio;

/**
 * Which characters to ignore when reading from input stream.
 *
 * @author hazard157
 */
public enum EStrioSkipMode {

  /**
   * Read all symbols, nothing is skipped.
   */
  SKIP_NONE,

  /**
   * Space characters from set {@link IStrioStreamBase#getSpaceChars()} are skipped.
   */
  SKIP_SPACES,

  /**
   * Bypassed characters from set {@link IStrioStreamBase#getBypassedChars()} are skipped.
   */
  SKIP_BYPASSED,

  /**
   * Like {@link #SKIP_BYPASSED} and additionally comments are skipped.
   */
  SKIP_COMMENTS

}

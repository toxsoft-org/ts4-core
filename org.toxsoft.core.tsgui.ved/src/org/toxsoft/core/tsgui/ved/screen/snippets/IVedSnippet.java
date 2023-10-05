package org.toxsoft.core.tsgui.ved.screen.snippets;

/**
 * VED screen snippet base interface.
 * <p>
 * VED screen snippets are decorators and user input handlers ({@link IVedDecorator} and {@link IVedUserInputHandler}
 * respectively).
 * <p>
 * Inactive state of the snippet temporary disables snippet operations (decorator drawing, input handling).
 *
 * @author hazard157
 */
public interface IVedSnippet {

  /**
   * Determines if snippet operation is enabled.
   * <p>
   * This value is <b>not</b> used by snippet itself, rather VED screen bypasses the calls to the inactive snippets.
   *
   * @return boolean - the sign the snippet is enabled (is active)
   */
  boolean isActive();

  /**
   * Sets the value of the {@link #isActive()} flag.
   *
   * @param aIsActive boolean - boolean - the sign the snippet is enabled (is active)
   */
  void setActive( boolean aIsActive );

}

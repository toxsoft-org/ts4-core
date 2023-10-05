package org.toxsoft.core.tsgui.ved.screen.impl;

import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.helpers.*;
import org.toxsoft.core.tsgui.ved.screen.snippets.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedSnippet} base implementation.
 *
 * @author hazard157
 */
public abstract class VedAbstractSnippet
    implements IVedSnippet, IDisposable {

  private final VedScreen vedScreen;

  private boolean disposed = false;

  /**
   * Constructor.
   *
   * @param aScreen {@link VedScreen} - the owner screen
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedAbstractSnippet( VedScreen aScreen ) {
    TsNullArgumentRtException.checkNull( aScreen );
    vedScreen = aScreen;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  // ------------------------------------------------------------------------------------
  // IVedSnippet
  //

  @Override
  public abstract boolean isActive();

  @Override
  public abstract void setActive( boolean aActive );

  // ------------------------------------------------------------------------------------
  // IDisposable
  //

  @Override
  final public boolean isDisposed() {
    return disposed;
  }

  @Override
  final public void dispose() {
    if( !disposed ) {
      try {
        doDispose();
      }
      finally {
        disposed = true;
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the owner VED screen.
   *
   * @return {@link IVedScreen} - VED screen
   */
  final public IVedScreen vedScreen() {
    return vedScreen;
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  /**
   * Subclass may perform additional action on snippet dispose.
   * <p>
   * Called once in a lifetime, when {@link #isDisposed()} = <code>false</code>.
   * <p>
   * Does nothing in the base class so there is no need to call base class method when overriding.
   */
  protected void doDispose() {
    // nop
  }

}

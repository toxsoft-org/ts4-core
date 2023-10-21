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

  private final IVedScreen vedScreen;

  private boolean active   = true;
  private boolean disposed = false;

  /**
   * Constructor.
   *
   * @param aScreen {@link IVedScreen} - the owner screen
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedAbstractSnippet( IVedScreen aScreen ) {
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
  final public boolean isActive() {
    return active;
  }

  @Override
  final public void setActive( boolean aActive ) {
    if( active != aActive ) {
      active = aActive;
      onAfterActiveStateChanged();
    }
  }

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

  /**
   * Finds the VISEL on screen by ID.
   *
   * @param aViselId String - the VISEL ID
   * @return {@link VedAbstractVisel} - found VISEL or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedAbstractVisel findVisel( String aViselId ) {
    return vedScreen().model().visels().list().findByKey( aViselId );
  }

  /**
   * Returns the VISEL on screen by ID.
   *
   * @param aViselId String - the VISEL ID
   * @return {@link VedAbstractVisel} - found VISEL
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no such VISEL found
   */
  public VedAbstractVisel getVisel( String aViselId ) {
    return vedScreen().model().visels().list().getByKey( aViselId );
  }

  /**
   * Finds the actor on screen by ID.
   *
   * @param aActorId String - the actor ID
   * @return {@link VedAbstractActor} - found actor or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedAbstractActor findActor( String aActorId ) {
    return vedScreen().model().actors().list().findByKey( aActorId );
  }

  /**
   * Returns the actor on screen by ID.
   *
   * @param aActorId String - the actor ID
   * @return {@link VedAbstractActor} - found actor
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no such actor found
   */
  public VedAbstractActor getActor( String aActorId ) {
    return vedScreen().model().actors().list().getByKey( aActorId );
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  /**
   * Subclass may process {@link #isActive()} state change.
   * <p>
   * Does nothing in the base class so there is no need to call base class method when overriding.
   */
  protected void onAfterActiveStateChanged() {
    // nop
  }

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

package org.toxsoft.core.tslib.bricks.events.change;

/**
 * {@link IGenericChangeEventer} implementation that does nothing.
 * <p>
 * Useful for special case entities implementation.
 *
 * @author hazard157
 */
public class NoneGenericChangeEventer
    implements IGenericChangeEventer {

  /**
   * Singleton instance.
   */
  public static final IGenericChangeEventer INSTANCE = new NoneGenericChangeEventer();

  private NoneGenericChangeEventer() {
    // nop
  }

  @Override
  public void addListener( IGenericChangeListener aListener ) {
    // nop
  }

  @Override
  public void removeListener( IGenericChangeListener aListener ) {
    // nop
  }

  @Override
  public void muteListener( IGenericChangeListener aListener ) {
    // nop
  }

  @Override
  public void unmuteListener( IGenericChangeListener aListener ) {
    // nop
  }

  @Override
  public boolean isListenerMuted( IGenericChangeListener aListener ) {
    return false;
  }

  @Override
  public void pauseFiring() {
    // nop
  }

  @Override
  public void resumeFiring( boolean aFireDelayed ) {
    // nop
  }

  @Override
  public boolean isFiringPaused() {
    return false;
  }

  @Override
  public boolean isPendingEvents() {
    return false;
  }

  @Override
  public void resetPendingEvents() {
    // nop
  }

}

package org.toxsoft.core.tsgui.ved.screen.impl;

import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.ved.screen.snippets.*;
import org.toxsoft.core.tslib.bricks.*;
import org.toxsoft.core.tslib.gw.time.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedUserInputHandler} base implementation.
 *
 * @author hazard157
 */
public class VedAbstractUserInputHandler
    extends VedAbstractSnippet
    implements IVedUserInputHandler, ITsUserInputListener, IGwTimeFleetable, IRealTimeSensitive {

  /**
   * Constructor.
   *
   * @param aScreen {@link VedScreen} - the owner screen
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedAbstractUserInputHandler( VedScreen aScreen ) {
    super( aScreen );
  }

  // ------------------------------------------------------------------------------------
  // IVedUserInputHandler
  //

  @Override
  public ITsUserInputListener userInputListener() {
    return this;
  }

  // ------------------------------------------------------------------------------------
  // IGwTimeFleetable
  //

  @Override
  public void whenGwTimePassed( long aGwTime ) {
    // this method is to be overridden
  }

  // ------------------------------------------------------------------------------------
  // IRealTimeSensitive
  //

  @Override
  public void whenRealTimePassed( long aRtTime ) {
    // this method is to be overridden
  }

}

package org.toxsoft.core.tsgui.ved.zver2.api.doc;

import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.ved.zver2.api.comp.*;
import org.toxsoft.core.tsgui.ved.zver2.api.entity.*;
import org.toxsoft.core.tslib.bricks.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The actor is active entity, providing intreaction with the GUI user.
 * <p>
 * Implementa {@link IRealTimeSensitive} assuming that actor is reasonably often called to perform time-critical
 * actions.
 *
 * @author hazard157
 */
public interface IVedActor
    extends IVedEntity, IRealTimeSensitive {

  /**
   * Determines if actor is designed to handle user input.
   *
   * @return boolean <code>true</code> is user input should be handled
   */
  boolean isUserInputHandler();

  /**
   * Creates new instance of the user input handler to be added to {@link IVedScreen}
   *
   * @return {@link ITsUserInputListener} - new instance of the lsitener
   * @throws TsUnsupportedFeatureRtException {@link #isUserInputHandler()} = <code>false</code>
   */
  ITsUserInputListener createUserInputListener();

  /**
   * Determines if actor is designed to draw screen decoraions.
   *
   * @return boolean <code>true</code> is screen decotrations should be handled
   */
  boolean isScreenDecorator();

  /**
   * Creates new instance of the screen decorator to be added to {@link IVedScreen}
   *
   * @return {@link ITsUserInputListener} - new instance of the lsitener
   * @throws TsUnsupportedFeatureRtException {@link #isScreenDecorator()} = <code>false</code>
   */
  IVedScreenDecorator createScreenDecorator();

  /**
   * Determines if actor is designed to draw view decoraions.
   *
   * @return boolean <code>true</code> is view decotrations should be handled
   */
  boolean isViewDecorator();

  /**
   * Creates new instance of the view decorator to be added to {@link IVedScreen}
   *
   * @return {@link ITsUserInputListener} - new instance of the lsitener
   * @throws TsUnsupportedFeatureRtException {@link #isScreenDecorator()} = <code>false</code>
   */
  IVedViewDecorator createViewDecorator();

}

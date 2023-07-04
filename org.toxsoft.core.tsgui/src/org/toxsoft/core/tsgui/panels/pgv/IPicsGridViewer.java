package org.toxsoft.core.tsgui.panels.pgv;

import java.util.ResourceBundle.*;

import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tsgui.utils.margins.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Displays items of type &lt;V&gt; as the thumbnails grid.
 * <p>
 * Viewer may be configured at creation time by the constants listed in {@link IPicsGridViewerConstants}.
 *
 * @author hazard157
 * @param <V> - displayed items type
 */
public interface IPicsGridViewer<V>
    extends ITsDoubleClickEventProducer<V>, ITsSelectionProvider<V>, IThumbSizeableEx, ITsUserInputProducer,
    ITsKeyInputProducer, ITsMouseInputProducer, ITsContextable {

  // TODO item pop-up menu support

  /**
   * Returns the displayed items.
   *
   * @return {@link IList}&lt;V&gt; - the list of displayed items in the order of placement
   */
  IList<V> items();

  /**
   * Sets the items to be displayed.
   * <p>
   * To clear the content both an empty list or <code>null</code> may be passed.
   *
   * @param aItems {@link IList}&lt;V&gt; - the list of items to display or <code>null</code>
   */
  void setItems( IList<V> aItems );

  /**
   * Returns the thumbnail grid geometric parameters.
   *
   * @return {@link ITsGridMargins} - grid parameters
   */
  ITsGridMargins getMargins();

  /**
   * Sets the thumbnail grid geometric parameters.
   * <p>
   * The changes are applied immediately.
   *
   * @param aMargins {@link ITsGridMargins} - gird parameters
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setMargins( ITsGridMargins aMargins );

  /**
   * Determines if still image display is forced.
   * <p>
   * When "force still" mode is activated for all animated images the still image {@link TsImage#image()} is displayed,
   * as soon as mode is deactivated image animation continues.
   * <p>
   * Initially the mode is deactiveted, so animations are turned on.
   *
   * @return boolean - <code>true</code> no animation, still images are displayed, <code>false</code> - animation is on
   */
  boolean isForceStill();

  /**
   * Sets {@link #isForceStill()} mode.
   * <p>
   * The changes are applied immediately.
   *
   * @param aForceStill boolean - <code>true</code> to turn off animation
   */
  void setFocreStill( boolean aForceStill );

  /**
   * Return the means to display texts, thumbnails and tooltips of the items.
   *
   * @return {@link ITsVisualsProvider} - the visuals provider, never is <code>null</code>
   */
  ITsVisualsProvider<V> getVisualsProvider();

  /**
   * Sets the visuals provider.
   *
   * @param aVisualsProvider {@link ITsVisualsProvider} - the visuals provider
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setVisualsProvider( ITsVisualsProvider<V> aVisualsProvider );

  /**
   * Refreshes the panel content.
   */
  void refresh();

  /**
   * Return SWT {@link Control} implementing the viewer.
   *
   * @return {@link TsComposite} - SWT-control
   */
  TsComposite getControl();

}

package org.toxsoft.core.tsgui.panels.generic;

import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.utils.checkcoll.*;
import org.toxsoft.core.tslib.coll.*;

/**
 * Generic items collection viewer / editor panel.
 *
 * @author hazard157
 * @param <T> - the entity type
 */
public interface IGenericCollPanel<T>
    extends IGenericSelectorPanel<T>, ITsDoubleClickEventProducer<T> {

  /**
   * Returns the list of items to display.
   * <p>
   * Not all items may be displayed in panel, for example, if panel contains filter pane to select subset of items.
   *
   * @return {@link IList}&lt;T&gt; - all items to how in panel
   */
  IList<T> items();

  /**
   * Returns the items checking means.
   * <p>
   * For panels without checks support {@link ITsCheckSupport#isChecksSupported()} returns <code>false</code>.
   *
   * @return {@link ITsCheckSupport} - items checking helper
   */
  ITsCheckSupport<T> checkSupport();

}

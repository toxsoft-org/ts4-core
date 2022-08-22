package org.toxsoft.core.tsgui.bricks.qtree;

import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.bricks.qnodes.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The colemn manager for {@link IQTreeViewer}.
 *
 * @author hazard157
 */
public interface IQTreeColumnManager
    extends ITsClearable {

  /**
   * Return the columns of tree.
   * <p>
   * List order is the same as initial left-to-right order of columns.
   *
   * @return {@link IList}&lt;{@link IQTreeColumn}&gt; - ordered list of columns
   */
  IList<IQTreeColumn> columns();

  /**
   * Add the column to the end of {@link #columns()}.
   *
   * @param aTitle String - text on the header bar
   * @param aAlignment {@link EHorAlignment} - horizontal alignment in cell
   * @param aVisProvider {@link ITsVisualsProvider} - visual provider for the column cell
   * @return {@link IQTreeColumn} - creted column instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IQTreeColumn addColumn( String aTitle, EHorAlignment aAlignment, ITsVisualsProvider<IQNode> aVisProvider );

}

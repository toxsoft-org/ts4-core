package org.toxsoft.core.tsgui.bricks.tstree;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.valed.api.*;

/**
 * Provides the {@link IValedControl} based editing for the {@link ITsNode} based cells in the tree.
 * <p>
 * For each editable column in tree viewer there must be own instance of this interface.
 *
 * @author hazard157
 */
public interface ITsNodeValedProvider {

  /**
   * Determines if the value for the specified node can be edited.
   * <p>
   * If method returns <code>false</code> then VALED will not be created for this node.
   *
   * @param aNode {@link ITsNode} - the node, never is <code>null</code>
   * @return boolean - the sign if the VALED in-cell editor can be created for editing
   */
  boolean canEdit( ITsNode aNode );

  /**
   * Creates the VALKED for the specified node.
   *
   * @param aNode {@link ITsNode} - the node, never is <code>null</code>
   * @param aContext {@link ITsGuiContext} - current context, never is <code>null</code>
   * @return {@link IValedControl} - created VALED
   */
  IValedControl<?> createValed( ITsNode aNode, ITsGuiContext aContext );

  /**
   * Returns the value to be edited in the created VALED.
   *
   * @param aNode {@link ITsNode} - the node, never is <code>null</code>
   * @return {@link Object} - the value, may be <code>null</code>
   */
  Object getValueForValed( ITsNode aNode );

  /**
   * Called after VALED finished editing to inform that value has been changed by user.
   * <p>
   * Implementation must store new value in the place where the {@link #getValueForValed(ITsNode)} originated.
   *
   * @param aNode {@link ITsNode} - the node, never is <code>null</code>
   * @param aValue {@link Object} - the edited value, may be <code>null</code>
   */
  void setValueFromValed( ITsNode aNode, Object aValue );

}

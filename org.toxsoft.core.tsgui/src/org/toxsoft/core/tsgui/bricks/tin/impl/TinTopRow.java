package org.toxsoft.core.tsgui.bricks.tin.impl;

import org.eclipse.jface.viewers.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The root node used as input for {@link TinTree}.
 *
 * @author hazard157
 */
public class TinTopRow
    extends TinRow {

  private static final String TOP_ROW_FIELD_INFO_ID = "TinTopRow"; //$NON-NLS-1$

  private final TinWidget tinWidget;

  /**
   * Constructor.
   *
   * @param aOwnerWidget {@link TinWidget} - the owner widget
   * @param aFieldInfo {@link ITinFieldInfo} - the field info of this node
   * @param aTreeViewer {@link TreeViewer} - the owner viewer
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TinTopRow( TinWidget aOwnerWidget, ITinTypeInfo aFieldInfo, TreeViewer aTreeViewer ) {
    super( new TinFieldInfo( TOP_ROW_FIELD_INFO_ID, aFieldInfo ), aTreeViewer );
    tinWidget = aOwnerWidget;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  /**
   * Recursively collects rows with changed subtree structure <b>after</b> values changed in tree but <b>before</b>
   * {@link TinRow#visibleChildren()} list is updated.
   * <p>
   * The result is used to visually refresh only changed set of rows in {@link #refreshSubtreeStructure()}.
   *
   * @param aRow {@link ITinRow} - root of subtree to collect changed rows from
   * @param aRowsToRefresh {@link IListEdit}&lt;{@link ITinRow}&gt; - editable list where the rows are collected
   */
  private static void processRowForSubtreeRefresh( ITinRow aRow, IListEdit<ITinRow> aRowsToRefresh ) {
    if( !aRow.fieldInfo().typeInfo().kind().hasChildren() ) {
      return;
    }
    IStringList visibleChilIds = makeVisibleFieldIds( aRow.fieldInfo().typeInfo(), aRow.getTinValue() );
    if( !visibleChilIds.equals( aRow.visibleChildren().ids() ) ) {
      aRowsToRefresh.add( aRow );
      return;
    }
    for( ITinRow childRow : aRow.allChildren() ) {
      processRowForSubtreeRefresh( childRow, aRowsToRefresh );
    }
  }

  // ------------------------------------------------------------------------------------
  // TinRow
  //

  @Override
  public TinTopRow root() {
    return this;
  }

  @Override
  void papiChildValueChangedByValed( String aFieldId ) {
    super.papiChildValueChangedByValed( aFieldId );
    tinWidget.papiFireProperyChangeEvent( aFieldId );
  }

  @Override
  public void setTinValue( ITinValue aValue ) {
    super.setTinValue( aValue );
    refreshSubtreeStructure();
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Updates {@link TinRow#visibleChildren()} list in whole subtree according to current values in nodes (rows).
   * <p>
   * Also visually updates the nodes with changed subtree structure.
   */
  void refreshSubtreeStructure() {
    // remember which row to refresh BEFORE update child rows lists
    IListEdit<ITinRow> rowsToRefresh = new ElemArrayList<>();
    processRowForSubtreeRefresh( this, rowsToRefresh );
    // update visible child rows lists down whole tree
    papiRecursivelyUpdateVisibleChildNodesFromCurrentNodeValue();
    // visually refresh rows AFTER refreshing internals
    for( ITinRow row : rowsToRefresh ) {
      treeViewer.refresh( row, true );
    }
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  // @Override
  // public IGenericChangeEventer genericChangeEventer() {
  // return genericChangeEventer;
  // }
  //
}

package org.toxsoft.core.tsgui.bricks.tin.impl;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Represents the one row in the inspector tree.
 * <p>
 * TIN Row contains all information to display name and show and edit the value. As an inspected object representation,
 * each row is in one-to-one relationship with the object field {@link #fieldInfo()}. The interface extends
 * {@link IStridable} simply returning values from the {@link ITinFieldInfo}.
 * <p>
 * From the other side, the row is node of the inspector tree, so it has {@link #parent()} and children. As an object
 * model the row lists all fields of the declared object as {@link #allChildren()}. As an tree node, TIN row may display
 * subset of fields listed in {@link #visibleChildren()}.
 *
 * @author hazard157
 */
interface ITinRow
    extends IStridable {

  TinTopRow root();

  ITinRow parent();

  IStridablesList<ITinRow> allChildren();

  IStridablesList<ITinRow> visibleChildren();

  ITinFieldInfo fieldInfo();

  boolean canEdit(); // true only for AtomicValues editing

  IValedControl<IAtomicValue> createValed( ITsGuiContext aContext );

  IAtomicValue getAtomicValueForValed();

  /**
   * This method sets node value as edited by this node tree cell editor.
   * <p>
   * It creates {@link ITinValue} from VALED's atomic value, sets node's value (like {@link #setTinValue(ITinValue)} and
   * fires a change event.
   *
   * @param aValue {@link IAtomicValue} - the new value of node as an atomic value
   * @throws TsUnsupportedFeatureRtException inappropriate node kind, atomic value can't be applied
   * @throws TsValidationFailedRtException {@link ITinTypeInfo#canDecompose(IAtomicValue)} failed
   */
  void setAtomicValueFromValed( IAtomicValue aValue );

  /**
   * Returns value contained in row (all it's child rows).
   *
   * @return {@link ITinValue} - the value of row
   */
  ITinValue getTinValue();

  /**
   * Sets value to the rows and all it's child rows.
   * <p>
   * This method does <b>not</b> affects parent row so caller is responsible to ensure that parent (and upper) rows have
   * correct values.
   * <p>
   * Does <b>not</b> generates any event.
   *
   * @param aValue {@link ITinValue} - the row value
   */
  void setTinValue( ITinValue aValue );

}

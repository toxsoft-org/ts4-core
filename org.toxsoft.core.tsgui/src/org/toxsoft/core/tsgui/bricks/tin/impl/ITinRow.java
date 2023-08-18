package org.toxsoft.core.tsgui.bricks.tin.impl;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.validator.*;

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

  ValidationResult canSetAtomicValueFromValed( IAtomicValue aValue );

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
   * Does <b>not</b> generates any event.
   *
   * @param aValue {@link ITinValue} - the row value
   */
  void setTinValue( ITinValue aValue );

}

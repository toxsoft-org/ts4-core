package org.toxsoft.core.tslib.av.metainfo.constr;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.*;

/**
 * Meta-information used to edit single data type constraint, on of the {@link IDataType#params()}.
 *
 * @author hazard157
 */
public interface IConstraintInfo
    extends IStridable {

  /**
   * Returns the ID of the category this constraint belongs to.
   * <p>
   * Categories are used for several purposes:
   * <ul>
   * <li>group constraints into the tree nodes for easier visual management;</li>
   * <li>to restrict categories to be used in any particular case.</li>
   * </ul>
   * Note: right now only categories management is not implemented it is a TODO to implement categories
   *
   * @return String category ID, a valid IDpath
   */
  String categoryId();

  /**
   * Determines if constraint may have value {@link IAtomicValue#NULL}.
   *
   * @return boolean - the sign that constraint may have value {@link IAtomicValue#NULL}
   */
  boolean isNullAllowed();

  /**
   * Determines if constraint must have value of type {@link #constraintType()} or same as data type.
   *
   * @return boolean - the sign that constraint must have the same type as {@link IDataType#atomicType()}<br>
   *         <b>true</b> - constraint must have value of {@link IDataType#atomicType()};<br>
   *         <b>false</b> - constraint must have value of {@link #constraintType()};.
   */
  boolean isConstraintTypeSameAsDataType();

  /**
   * Returns atomic type of the constraint value if {@link #isConstraintTypeSameAsDataType()} = <code>false</code>.
   * <p>
   * Note: method may return {@link EAtomicType#NONE} indicating thet constraint may have value of any type.
   *
   * @return {@link EAtomicType} - atomic type of the constraint value
   */
  EAtomicType constraintType();

  /**
   * Determines if constraint value may be only from the supplied list {@link #listLookupValues()}.
   *
   * @return boolean - determines if only lookup values are allowed<br>
   *         <b>true</b> - constraint must have value one of {@link #listLookupValues()};<br>
   *         <b>false</b> - constraint may have any value, including lookup ones.
   */
  boolean isOnlyLookupValuesAllowed();

  /**
   * Returns predefined values of the constraint.
   * <p>
   * If {@link #isConstraintTypeSameAsDataType()} is <code>true</code> and {@link #isNullAllowed()} = <code>false</code>
   * this method returns at least one value, otherwise constraint can not be assigned any value. If
   * {@link #isConstraintTypeSameAsDataType()} = <code>false</code> then method may return an empty list.
   *
   * @return {@link IList}&gt;{@link IAtomicValue}&gt; - lookup values to be set as a constraint value
   */
  IList<IAtomicValue> listLookupValues();

  /**
   * Returns atomic type of the {@link IDataType} this constraint is applicable for.
   *
   * @return {@link IStridablesList}&gt;{@link EAtomicType}&lt; - list of applicable {@link IDataType#atomicType()}
   */
  IStridablesList<EAtomicType> listApplicableDataTypes();

}

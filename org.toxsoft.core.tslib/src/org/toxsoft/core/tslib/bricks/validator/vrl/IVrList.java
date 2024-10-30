package org.toxsoft.core.tslib.bricks.validator.vrl;

import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;

/**
 * The result of the validation as a set of {@link ValidationResult} with additional information.
 * <p>
 * This is a read-=only interface, use {@link IVrListEdit} to add items and {@link VrList} for full control over
 * content.
 *
 * @author hazard157
 */
public interface IVrList {

  /**
   * Returns the worst result type found in list.
   * <p>
   * Returns {@link EValidationResultType#OK} for an empty list.
   *
   * @return {@link EValidationResultType} - the worst item type
   */
  EValidationResultType getWorstType();

  /**
   * Returns check results in order as they were added to list.
   *
   * @return {@link IList}&lt;{@link VrlItem}&gt; - ordered list of check result items
   */
  IList<VrlItem> items();

  /**
   * Returns option definitions used in {@link VrlItem#info()} of this list.
   * <p>
   * Defining options are not mandatory. This method is just convenient place to store meta-information together with
   * check results. This list only stores but not uses the option definition.
   *
   * @return {@link IStridablesList}&lt;{@link IDataDef}&gt; - optional definitions of {@link VrlItem#info()} options
   */
  IStridablesList<IDataDef> listInfoOpDefs();

  /**
   * Returns the first worst result item from the {@link #items()} list.
   * <p>
   * Returns first ERROR from list, if no error, returns first WARNING. Otherwise returns first item in
   * {@link #items()}. If results is an empty list returns {@link VrlItem#OK}.
   *
   * @return {@link VrlItem} - the first occurrence of the worst result, never is <code>null</code>
   */
  VrlItem getFirstWorst();

  // ------------------------------------------------------------------------------------
  // inline methods for convenience
  //

  /**
   * Determines if {@link #items()} list is empty.
   *
   * @return boolean - if {@link #items()} list is empty
   */
  default boolean isEmpty() {
    return items().isEmpty();
  }

  @SuppressWarnings( "javadoc" )
  default ValidationResult firstWorstResult() {
    return getFirstWorst().vr();
  }

}

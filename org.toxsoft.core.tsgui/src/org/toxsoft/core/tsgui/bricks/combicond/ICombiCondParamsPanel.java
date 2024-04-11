package org.toxsoft.core.tsgui.bricks.combicond;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.panels.generic.*;
import org.toxsoft.core.tsgui.panels.misc.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.math.combicond.*;
import org.toxsoft.core.tslib.math.lexan.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Panel to edit {@link ICombiCondParamsPanel}.
 * <p>
 * Panel contains:
 * <ul>
 * <li>formula string editor - SWT {@link Text} control to edit logical formula;</li>
 * <li>conditions list - to edit conditions mentioned in the formula by the keywords;</li>
 * <li>optional validation pane - {@link ValidationResultPanel} displaying the formula correctness.</li>
 * </ul>
 *
 * @author hazard157
 */
public interface ICombiCondParamsPanel
    extends IGenericEntityEditPanel<ICombiCondParams> {

  /**
   * Determines how the empty formula ({@link IFormulaTokens#isEmpty()} = <code>true</code>) is represented.
   * <p>
   * <code>null</code> means that empty formula is not allowed, {@link #getEntity()} with throw
   * {@link TsValidationFailedRtException} exception.
   * <p>
   * Common values are {@link ISingleCondParams#ALWAYS} or {@link ISingleCondParams#NEVER}. Default value is
   * {@link ISingleCondParams#NEVER}.
   *
   * @return {@link ISingleCondParams} - empty formula representation or <code>null</code>
   */
  ISingleCondParams getEmptyFormulaRepresentation();

  /**
   * Sets the value {@link #getEmptyFormulaRepresentation()}.
   *
   * @param aScp {@link ISingleCondParams} - empty formula representation or <code>null</code>
   */
  void setEmptyFormulaRepresentation( ISingleCondParams aScp );

  /**
   * Adds condition types from the argument to the internal registry.
   *
   * @param aTypes {@link IStridablesList}&lt;{@link ISingleCondType}&gt; - types to register
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException any type ID is already registered
   */
  void addRegistry( IStridablesList<? extends ISingleCondType> aTypes );

  /**
   * Adds condition types from the argument to the internal registry.
   *
   * @param aTypesRegistry {@link IStridablesRegisrty} - the registry of {@link ISingleCondType}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException any type ID is already registered
   */
  default void addRegistry( IStridablesRegisrty<? extends ISingleCondType> aTypesRegistry ) {
    TsNullArgumentRtException.checkNull( aTypesRegistry );
    addRegistry( aTypesRegistry.items() );
  }

}

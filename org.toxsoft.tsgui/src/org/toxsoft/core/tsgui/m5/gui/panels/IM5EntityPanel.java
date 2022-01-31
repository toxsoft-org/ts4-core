package org.toxsoft.core.tsgui.m5.gui.panels;

import org.toxsoft.core.tsgui.m5.IM5Bunch;
import org.toxsoft.core.tsgui.m5.IM5Model;
import org.toxsoft.core.tsgui.m5.model.IM5LifecycleManager;
import org.toxsoft.core.tsgui.panels.generic.IGenericEntityPanel;
import org.toxsoft.core.tslib.bricks.events.change.IGenericChangeEventCapable;
import org.toxsoft.core.tslib.bricks.validator.EValidationResultType;
import org.toxsoft.core.tslib.bricks.validator.ValidationResult;
import org.toxsoft.core.tslib.bricks.validator.impl.TsValidationFailedRtException;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;

/**
 * One entity viewer / editor panel.
 * <p>
 * Panel is {@link IGenericChangeEventCapable} so when in editing mode generates events that content was changed by GUI
 * user.
 *
 * @author hazard157
 * @param <T> - modelled entity type
 */
public interface IM5EntityPanel<T>
    extends IM5PanelBase<T>, IGenericEntityPanel<T> {

  /**
   * Sets panel content from bunch.
   *
   * @param aBunch {@link IM5Bunch} - set of entity fiedl values, may be <code>null</code>
   * @throws TsIllegalArgumentRtException bunch model does not matches {@link #model()}
   */
  void setValues( IM5Bunch<T> aBunch );

  /**
   * Sets panel content from the entity.
   *
   * @param aEntity &lt;T&gt; - the entity, may be <code>null</code>
   * @throws TsIllegalArgumentRtException the entioty is not {@link IM5Model#isModelledObject(Object)}
   */
  @Override
  void setEntity( T aEntity );

  /**
   * Checks if values may be obtaied from all widgets in panel.
   * <p>
   * If this method returns {@link EValidationResultType#ERROR} then calling {@link #getValues()} will throw an
   * exception.
   * <p>
   * Note: method checks just ability to get all field values from widgets. It does not checks if data in widgets has
   * valid values.
   *
   * @return {@link ValidationResult} - check result
   */
  ValidationResult canGetValues();

  /**
   * Returns the current values of the modelled entity fields from the panel.
   * <p>
   * For the firlds represented by widgets values are retrieved from widgets, for invisible fields values are retrieved
   * from {@link #lastValues()}.
   * <p>
   * Method nay return <code>null</code> that means there is no entity displayed in panel.
   *
   * @return {@link IM5Bunch} - modelled entity fields values, may be <code>null</code>
   * @throws TsValidationFailedRtException failed {@link #canGetValues()}
   */
  IM5Bunch<T> getValues();

  /**
   * Returns values last set by method {@link #setValues(IM5Bunch)} or {@link #setEntity(Object)}.
   * <p>
   * After panel is created initial values are set to {@link IM5Model#valuesOf(Object) valuesOf(<b>null</b>)}.
   * <p>
   * Note: panel holds two bunches of entity field values:
   * <ul>
   * <li>initial values set by {@link #setValues(IM5Bunch)} or {@link #setEntity(Object)};</li>
   * <li>currently edited values in the panel widgets.</li>
   * </ul>
   * The first one is returned by this method, the second one - by <{@link #getValues()}.
   *
   * @return {@link IM5Bunch} - заданные значения полей, может быть <code>null</code>
   */
  IM5Bunch<T> lastValues();

  /**
   * Returns the lifecycle manager if defined.
   *
   * @return {@link IM5LifecycleManager} - the lifecycle manager or <code>null</code>
   */
  IM5LifecycleManager<T> lifecycleManager();

  /**
   * Sets the lifecycle manager.
   * <p>
   * This method is useful only for editor panels, when {@link #isViewer()} = <code>false</code>.
   *
   * @param aLifecycleManager {@link IM5LifecycleManager} - the lifecycle manager or <code>null</code>
   */
  void setLifecycleManager( IM5LifecycleManager<T> aLifecycleManager );

}

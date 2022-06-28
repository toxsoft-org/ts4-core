package org.toxsoft.core.tsgui.ved.incub.props;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.opset.*;

/**
 * Listens to the property value changed event.
 *
 * @author hazard157
 */
public interface IPropertyChangeListener {

  /**
   * Called when single or multiple proprty value changes.
   * <p>
   * If more whan one property changes at once <code>aPropId</code>, <code>aOldValue</code> and <code>aNewValue</code>
   * are <code>null</code> and {@link #onSeveralPropsChanged(IPropertiesSetRo, IOptionSet, IOptionSet)} is called after
   * this method.
   *
   * @param aSource {@link IPropertiesSetRo} - the event source
   * @param aPropId String changed property ID or <code>null</code> for batch changes
   * @param aOldValue {@link IAtomicValue} - property value before change or <code>null</code> for batch changes
   * @param aNewValue {@link IAtomicValue} - property value after change or <code>null</code> for batch changes
   */
  void onPropertyChanged( IPropertiesSetRo aSource, String aPropId, IAtomicValue aOldValue, IAtomicValue aNewValue );

  /**
   * Called when more than one properties values changes at once.
   * <p>
   * Argument <code>aNewValues</code> contain only changed properties while <code>aOldValues</code> is not limited to
   * contain only changed values.
   * <p>
   * This method is called after {@link #onPropertyChanged(IPropertiesSetRo, String, IAtomicValue, IAtomicValue)} with
   * <code>null</code> arguments.
   *
   * @param aSource {@link IPropertiesSetRo} - the event source
   * @param aOldValues {@link IOptionSet} - changed properties values before change
   * @param aNewValues {@link IOptionSet} - changed properties values after change
   */
  default void onSeveralPropsChanged( IPropertiesSetRo aSource, IOptionSet aOldValues, IOptionSet aNewValues ) {
    // nop
  }

}

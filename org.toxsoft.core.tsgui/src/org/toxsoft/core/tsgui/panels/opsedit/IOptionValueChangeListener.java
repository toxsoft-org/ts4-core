package org.toxsoft.core.tsgui.panels.opsedit;

import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.opset.*;

/**
 * Listener to the option value change.
 *
 * @author hazard157
 */
public interface IOptionValueChangeListener {

  /**
   * The singleton of the dummy listener.
   */
  IOptionValueChangeListener NONE = ( aSource, aOptionId, aNewValue ) -> { /* nop */ };

  /**
   * Called when value of the option is changed.
   * <p>
   * This method is called only when {@link IValedControl#canGetValue()} permits to read value in editor.
   *
   * @param aSource Object - the source
   * @param aOptionId String - ID of the option in {@link IOptionSet#keys()}
   * @param aNewValue {@link IAtomicValue} - the new value never is <code>null</code>
   */
  void onOptionValueChange( Object aSource, String aOptionId, IAtomicValue aNewValue );

}

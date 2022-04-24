package org.toxsoft.core.tsgui.panels.opsedit.set;

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
  IOptionValueChangeListener NONE = ( aSource, aOptionId ) -> { /* nop */ };

  /**
   * Called when value of the option is changed.
   *
   * @param aSource Object - the source
   * @param aOptionId String - ID of the option in {@link IOptionSet#keys()} or <code>null</code>
   */
  void onOptionValueChange( Object aSource, String aOptionId );

}

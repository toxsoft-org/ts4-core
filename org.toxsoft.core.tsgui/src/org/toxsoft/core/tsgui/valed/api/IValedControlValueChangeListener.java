package org.toxsoft.core.tsgui.valed.api;

/**
 * Listener to the user input in the VALED.
 *
 * @author hazard157
 */
public interface IValedControlValueChangeListener {

  /**
   * Called when the value in the editor is changed by the <b>user</b>.
   * <p>
   * If the value in the control is changed programmatically (by the {@link IValedControl} API methods), the listener
   * <b>is not</b> called.
   * <p>
   * For some controls (editors), the process of changing the value is lengthy (consisting of several actions) and it is
   * necessary to distinguish between changing the value during editing and at the moment of completion. A typical case
   * is a text input field. As characters are typed, the method is called with the parameter aEditFinished = false, and
   * when the input is completed - with the value true. The completion of input can be considered events such as
   * pressing Enter and focus loss of the control.
   *
   * @param aSource {@link IValedControl} - the event source VALED
   * @param aEditFinished boolean - the sign that editing was finished
   */
  void onEditorValueChanged( IValedControl<?> aSource, boolean aEditFinished );

}

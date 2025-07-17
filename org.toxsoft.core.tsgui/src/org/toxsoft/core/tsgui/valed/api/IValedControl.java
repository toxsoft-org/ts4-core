package org.toxsoft.core.tsgui.valed.api;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * VALue EDitor control interface.
 *
 * @author hazard157
 * @param <V> - the edited value type
 */
public interface IValedControl<V>
    extends ILazyControl<Control>, ITsGuiContextable, IParameterizedEdit {

  /**
   * Determines if the VALED allows user to edit the value.
   * <p>
   * The VALED is designed to allow user edit the value via SWT widget(s). However, sometimes editing may be disabled
   * (for example, you can't change file name on the read-only filesystem). Editing may be enabled/disabled by the
   * method {@link #setEditable(boolean)}.
   * <p>
   * However, VALED may be created with {@link IValedControlConstants#OPDEF_CREATE_UNEDITABLE} flag set to disable the
   * editing during VALED's lifecycle. Example usage is VALED in the entity editor dialog where some ID may be edited
   * when entity dialog is invoked for entity creation. However, when dialog is invoked for entity editing the ID can
   * not be changed so corresponding VALED is created with {@link IValedControlConstants#OPDEF_CREATE_UNEDITABLE} flag
   * set. For such uneditable VALEDs method always returns <code>false</code>.
   * <p>
   * Note: the GUI implementation of the VALED in editing enabled/disabled state may be different. For example,
   * <b>int</b> value VALED may be represented as a {@link Spinner} when editing is enabled and as a {@link Text} when
   * disabled.
   *
   * @return boolean - flag for allowing editing of a value
   */
  boolean isEditable();

  /**
   * Enables/disabled VALED editing mode, changes {@link #isEditable()}.
   * <p>
   * If VALED was created with {@link IValedControlConstants#OPDEF_CREATE_UNEDITABLE} flag set then this method is
   * ignored and {@link #isEditable()} always returns <code>false</code>.
   *
   * @param aEditable boolean - flag for allowing editing of a value
   */
  void setEditable( boolean aEditable );

  /**
   * Checks if widget contains value that may be read by the method {@link #getValue()}.
   *
   * @return {@link ValidationResult} - the check result, if error {@link #getValue()} will throw an exception
   */
  ValidationResult canGetValue();

  /**
   * Returns the edited value.
   * <p>
   * As a lazy control, VALED is instantiated by the constructor but corresponding SWT widget is created by the method
   * {@link #createControl(Composite)}. Some implementations may throws a {@link TsIllegalStateRtException} if called
   * before widget is created. This behavior is due to the fact that VALED must return the value entered (or at least
   * viewed) by the user. Naturally, the user cannot see the value before the widget is created.
   *
   * @return &lt;V&gt; - the edited value, may be {@link NullPointerException}
   * @throws TsIllegalStateRtException SWT widget is not created yet (some implementations)
   * @throws TsValidationFailedRtException failed {@link #canGetValue()}
   */
  V getValue();

  /**
   * Sets the value to be edited by this control.
   * <p>
   * Setting value does <b>not</b> generates notification {@link IValedControlValueChangeListener}.
   * <p>
   * Calling this method with <code>null</code> argument is allowed and is the same as call to {@link #clearValue()}.
   *
   * @param aValue &lt;V&gt; - new value, may be <code>null</code> to {@link #clearValue()}
   */
  void setValue( V aValue );

  /**
   * Clears the value in the widget.
   * <p>
   * Clearing value does <b>not</b> generates notification {@link IValedControlValueChangeListener}.
   * <p>
   * The method is designed to set an element to the "no value" (or <code>null</code>) state. If <code>null</code> is
   * not valid value for control, immediately after this method {@link #canGetValue()} will return an error and
   * {@link #getValue()} will throw an exception. To get valid value from control either user must input the value to
   * the widget or non-<code>null</code> value must be set programmatically by {@link #setValue(Object)} method.
   */
  void clearValue();

  /**
   * Returns the value editing notifications manager.
   *
   * @return {@link ITsEventer}&lt;{@link IValedControlValueChangeListener}&gt; - the eventer
   */
  ITsEventer<IValedControlValueChangeListener> eventer();

}

package org.toxsoft.core.tsgui.valed.controls.helpers;

import static org.toxsoft.core.tsgui.valed.IValedImplementationHelpers.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Base implementation of VALED with text editor with optional edit button at right.
 *
 * @author hazard157
 * @param <V> - the edited value type
 */
public abstract class AbstractValedTextAndButton<V>
    extends AbstractValedControl<V, Composite> {

  private Composite board;
  private Text      text;
  private Button    button;

  /**
   * Constructor for subclasses.
   *
   * @param aContext {@link ITsGuiContext} - the VALED context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  protected AbstractValedTextAndButton( ITsGuiContext aContext ) {
    super( aContext );
    setParamIfNull( OPDEF_IS_WIDTH_FIXED, AV_FALSE );
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedControl
  //

  @Override
  protected Composite doCreateControl( Composite aParent ) {
    board = new Composite( aParent, SWT.NO_FOCUS );
    board.setLayout( new BorderLayout() );
    setControl( board );
    // text
    text = new Text( board, SWT.BORDER );
    text.setLayoutData( BorderLayout.CENTER );
    text.addModifyListener( notificationModifyListener );
    // button
    button = new Button( board, SWT.PUSH | SWT.FLAT );
    button.setLayoutData( BorderLayout.EAST );
    button.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        if( doProcessButtonPress() ) {
          doUpdateTextControl();
          fireModifyEvent( true );
        }
      }
    } );
    // setup
    text.setEditable( isEditable() && !isCreatedUneditable() );
    button.setEnabled( isEditable() && !isCreatedUneditable() );
    button.setText( STR_ELLIPSIS );
    doAfterControlCreated();
    doUpdateTextControl();
    return board;
  }

  @Override
  protected void doSetEditable( boolean aEditable ) {
    if( isWidget() && !isCreatedUneditable() ) {
      button.setEnabled( aEditable );
    }
  }

  @Override
  protected void doClearValue() {
    text.setText( TsLibUtils.EMPTY_STRING );
    doUpdateTextControl();
  }

  @Override
  final protected void doSetUnvalidatedValue( V aValue ) {
    doDoSetUnvalidatedValue( aValue );
    doUpdateTextControl();
  }

  // ------------------------------------------------------------------------------------
  // For subclasses
  //

  /**
   * Returns the text control.
   *
   * @return {@link Text} - the text control
   */
  public Text getTextControl() {
    return text;
  }

  /**
   * Returns the button at right.
   *
   * @return {@link Button} - the button at right
   */
  public Button getButtonControl() {
    return button;
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * Subclass may additionally setup controls after creation.
   * <p>
   * Is called at the end of {@link #createControl(Composite)}, when controls {@link #getButtonControl()} and
   * {@link #getTextControl()} are already created and {@link #getControl()} returns backplate {@link Composite}. For
   * example, method may reset button text (contains ellipsis) and set own icon.
   * <p>
   * Does nothing in the base class hence there is no need to call superclass method when overriding.
   */
  protected void doAfterControlCreated() {
    // nop
  }

  /**
   * Subclass must update {@link #getTextControl()}.
   * <p>
   * Called after value was changed not by user in text field, but by button-invoked dialog or by VALED API.
   * <p>
   * In most cases there is no need to override this method. However< if one of the following condition is met, subclass
   * should override this method and write correct text to the text field:
   * <ul>
   * <li>If {@link #clearValue()} must write something different when an empty string as a textual representation;</li>
   * <li>any of methods {@link #doProcessButtonPress()}, {@link #doAfterControlCreated()} or
   * {@link #doSetUnvalidatedValue(Object)} does not updates text field correctly.;</li>
   * </ul>
   * <p>
   * In other words, this method should be overridden when subclass stores non-String edited value in it's fiedl, so
   * this method writes in text field correctly formatted text representation of the edited value.
   * <p>
   * Does nothing in the base class, there is no need to call the parent medthod when overriding.
   */
  protected void doUpdateTextControl() {
    // nop
  }

  /**
   * Subclass must call value editor dialog and set value to this VALED.
   * <p>
   * Is called when user pushes button {@link #getButtonControl()}.
   *
   * @return boolean - the flag indicates that value was changed
   */
  protected abstract boolean doProcessButtonPress();

  /**
   * Subclass must the value to editor widget(s).
   * <p>
   * Called from {@link AbstractValedTextAndButton#doSetUnvalidatedValue(Object)}.
   *
   * @param aValue &lt;V&gt; - new value, never is <code>null</code>
   * @throws TsIllegalArgumentRtException value has incompatible type
   */
  protected abstract void doDoSetUnvalidatedValue( V aValue );

}

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
   * @param aContext {@link ITsGuiContext} - the valed context
   * @throws TsNullArgumentRtException аргумент = null
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
        doProcessButtonPress();
      }
    } );
    // setup
    text.setEditable( isEditable() && !isCreatedUneditable() );
    button.setEnabled( isEditable() && !isCreatedUneditable() );
    button.setText( STR_ELLIPSIS );
    doAfterControlCreated();
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
  }

  // ------------------------------------------------------------------------------------
  // Для наследников
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
   * @return Buntton - the button at right
   */
  public Button getButtonControl() {
    return button;
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * Subclass may additionally setup conotrols after creation.
   * <p>
   * Is called at the end of {@link #createControl(Composite)}, when controls {@link #getButtonControl()} and
   * {@link #getTextControl()} are already created and {@link #getControl()} returns backplate {@link Composite}. For
   * example, method may reset button text (contains ellipsis) and set own icon.
   * <p>
   * Does nothng in the base class hence there is no need to call superclass method when overriding.
   */
  protected void doAfterControlCreated() {
    // nop
  }

  /**
   * Subclass must call value editor dialog and set value to this VALED.
   * <p>
   * Is called when user pushes button {@link #getButtonControl()}.
   */
  protected abstract void doProcessButtonPress();

}

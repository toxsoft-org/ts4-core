package org.toxsoft.core.tsgui.valed.controls.helpers;

import static org.toxsoft.core.tsgui.valed.IValedImplementationHelpers.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Base implementation of VALED with label with optional edit button at right.
 * <p>
 * Optionally an icon may be added to the left as part of mandatory {@link CLabel}.
 *
 * @author hazard157
 * @author dima
 * @param <V> - the edited value type
 */
public abstract class AbstractValedLabelAndButton<V>
    extends AbstractValedControl<V, Composite> {

  private Composite board;
  private CLabel    label;
  private Button    button;

  /**
   * Constructor for subclasses.
   *
   * @param aContext {@link ITsGuiContext} - the valed context
   * @throws TsNullArgumentRtException аргумент = null
   */
  protected AbstractValedLabelAndButton( ITsGuiContext aContext ) {
    super( aContext );
    setParamIfNull( OPDEF_IS_WIDTH_FIXED, AV_FALSE );
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedControl
  //

  @Override
  protected Composite doCreateControl( Composite aParent ) {
    board = new Composite( aParent, SWT.NO_FOCUS );
    board.setLayout( new GridLayout( 2, false ) );
    setControl( board );
    // label
    label = new CLabel( board, SWT.BORDER | SWT.LEFT );
    label.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false, 1, 1 ) );
    // button
    button = new Button( board, SWT.PUSH | SWT.FLAT );
    button.setLayoutData( new GridData( SWT.FILL, SWT.FILL, false, false, 1, 1 ) );
    button.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        doProcessButtonPress();
      }
    } );
    // setup
    button.setEnabled( isEditable() && !isCreatedUneditable() );
    button.setText( STR_ELLIPSIS );
    setControl( board );
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
    label.setText( TsLibUtils.EMPTY_STRING );
  }

  // ------------------------------------------------------------------------------------
  // For subclasses
  //

  /**
   * Returns the label control.
   *
   * @return {@link CLabel} - the label control
   */
  public CLabel getLabelControl() {
    return label;
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
   * {@link #getLabelControl()} are already created and {@link #getControl()} returns backplate {@link Composite}. For
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

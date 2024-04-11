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
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Base implementation of VALED with label with optional edit button at right.
 * <p>
 * Optionally an icon may be added to the left as part of mandatory {@link CLabel}.
 * <p>
 * TODO add optional button to reset value (to set to <code>null</code> or some predefined value like
 * {@link IAtomicValue#NULL})
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
   * @param aContext {@link ITsGuiContext} - the VALED context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  protected AbstractValedLabelAndButton( ITsGuiContext aContext ) {
    super( aContext );
    setParamIfNull( OPDEF_IS_WIDTH_FIXED, AV_FALSE );
    setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_TRUE );
    setParamIfNull( OPDEF_VERTICAL_SPAN, AV_1 );
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedControl
  //

  @Override
  protected Composite doCreateControl( Composite aParent ) {
    board = new Composite( aParent, SWT.NO_FOCUS );
    GridLayout gl = new GridLayout( 2, false );
    gl.marginLeft = 0;
    gl.marginTop = 0;
    gl.marginRight = 0;
    gl.marginBottom = 0;
    gl.verticalSpacing = 0;
    gl.horizontalSpacing = 0;
    gl.marginWidth = 0;
    gl.marginHeight = 0;
    board.setLayout( gl );
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
        if( doProcessButtonPress() ) {
          doUpdateLabelControl();
          fireModifyEvent( true );
        }
      }
    } );
    // setup
    button.setEnabled( isEditable() && !isCreatedUneditable() );
    button.setText( STR_ELLIPSIS );
    doAfterControlCreated();
    doUpdateLabelControl();
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
    doUpdateLabelControl();
  }

  @Override
  final protected void doSetUnvalidatedValue( V aValue ) {
    doDoSetUnvalidatedValue( aValue );
    doUpdateLabelControl();
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
   * @return {@link Button} - the button at right
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
   * Does nothing in the base class hence there is no need to call superclass method when overriding.
   */
  protected void doAfterControlCreated() {
    // nop
  }

  /**
   * Subclass must update {@link #getLabelControl()}.
   * <p>
   * Called after value change.
   */
  protected abstract void doUpdateLabelControl();

  /**
   * Subclass must call value editor dialog and set value to this VALED.
   * <p>
   * Is called when user pushes button {@link #getButtonControl()}.
   *
   * @return boolean - the flag indicates that value was changed
   */
  protected abstract boolean doProcessButtonPress();

  /**
   * Subclass must hold the value for for further use.
   * <p>
   * There is no need to update text of {@link #getLabelControl()} - method {@link #doUpdateLabelControl()} is called
   * immediately after this method.
   * <p>
   * Called from {@link AbstractValedLabelAndButton#doSetUnvalidatedValue(Object)}.
   *
   * @param aValue &lt;V&gt; - new value, never is <code>null</code>
   * @throws TsIllegalArgumentRtException value has incompatible type
   */
  protected abstract void doDoSetUnvalidatedValue( V aValue );

}

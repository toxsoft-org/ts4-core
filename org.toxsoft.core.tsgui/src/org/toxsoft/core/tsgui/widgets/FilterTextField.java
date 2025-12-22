package org.toxsoft.core.tsgui.widgets;

import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.core.tsgui.widgets.IHzResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Filter text field control.
 * <p>
 * Contains filter on/off button, text field and clear button.
 * <p>
 * Generates {@link IGenericChangeListener#onGenericChangeEvent(Object)} when text field content or filter on/off button
 * state changes.
 *
 * @author hazard157
 */
public class FilterTextField
    extends TsComposite
    implements ITsGuiContextable, IGenericChangeEventCapable {

  private final GenericChangeEventer eventer;
  private final ITsGuiContext        tsContext;

  private final Button btnOnOff;
  private final Text   textField;
  private final Button btnClear;

  /**
   * Constructor.
   *
   * @param aParent {@link Comparable} - the parent
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public FilterTextField( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, SWT.NONE );
    tsContext = TsNullArgumentRtException.checkNull( aContext );
    eventer = new GenericChangeEventer( this );
    GridLayout gl = new GridLayout( 3, false );
    gl.makeColumnsEqualWidth = false;
    gl.horizontalSpacing = gl.verticalSpacing = 0;
    gl.marginBottom = gl.marginHeight = gl.marginLeft = gl.marginRight = gl.marginTop = 0;
    gl.marginWidth = 5;
    this.setLayout( gl );
    // btnOnOff
    btnOnOff = new Button( this, SWT.TOGGLE );
    btnOnOff.setLayoutData( new GridData( SWT.LEFT, SWT.CENTER, false, false ) );
    btnOnOff.setToolTipText( BTN_P_FILTER );
    Image icon = iconManager().loadStdIcon( ICONID_VIEW_FILTER, EIconSize.IS_16X16 ); // TODO what icon size to use?
    btnOnOff.setImage( icon );
    btnOnOff.setSelection( true );
    btnOnOff.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        eventer.fireChangeEvent();
      }
    } );
    // textField
    textField = new Text( this, SWT.BORDER );
    textField.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false ) );
    textField.addModifyListener( aEvent -> eventer.fireChangeEvent() );
    // btnCleat
    btnClear = new Button( this, SWT.PUSH );
    btnClear.setLayoutData( new GridData( SWT.RIGHT, SWT.FILL, false, false ) );
    btnClear.setText( "<" ); //$NON-NLS-1$
    btnClear.setToolTipText( BTN_P_CLEAR );
    btnClear.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        reset();
        textField.setFocus();
      }
    } );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  void reset() {
    textField.setText( TsLibUtils.EMPTY_STRING );
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Determines if filtering is turned on.
   *
   * @return boolean - a flag that filtering is on
   */
  public boolean isFilterOn() {
    return btnOnOff.getSelection();
  }

  /**
   * Turnw on/off the filtering if implementation allows it.
   *
   * @param aOn boolean - a flag that filtering is on
   */
  public void setFilterOn( boolean aOn ) {
    btnOnOff.setSelection( aOn );
  }

  /**
   * Returns the text for filtering.
   *
   * @return String - the filter text, may be an empty string but never is <code>null</code>
   */
  public String getFilterText() {
    return textField.getText();
  }

  /**
   * Sets the text for filtering.
   *
   * @param aText - String - the filter text, may be an empty string
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void setFilterText( String aText ) {
    TsNullArgumentRtException.checkNull( aText );
    textField.setText( aText );
  }

}

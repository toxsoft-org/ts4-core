package org.toxsoft.core.tsgui.valed.controls.time;

import java.time.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tsgui.widgets.mpv.*;

/**
 * Displays {@link LocalDate} value as YYYY-MM-DD.
 *
 * @author hazard157
 */
class ValedLocalDateViewer
    extends AbstractValedControl<LocalDate, Text> {

  private static final LocalDate VALUE_OF_CLEARED = IMpvLocalDate.MIN_MIN_DATE;

  private LocalDate value = VALUE_OF_CLEARED;

  public ValedLocalDateViewer( ITsGuiContext aTsContext ) {
    super( aTsContext );
  }

  // ------------------------------------------------------------------------------------
  // inplementation
  //

  private void updateDisplayedValue() {
    Text txt = getControl();
    if( txt != null ) {
      // value must never be null, this is just protection
      String s = value != null ? value.toString() : "????-??-??"; //$NON-NLS-1$
      txt.setText( s );
    }
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedControl
  //

  @Override
  protected Text doCreateControl( Composite aParent ) {
    Text txt = new Text( aParent, SWT.READ_ONLY | SWT.BORDER );
    updateDisplayedValue();
    return txt;
  }

  @Override
  protected void doSetEditable( boolean aEditable ) {
    // nop
  }

  @Override
  protected LocalDate doGetUnvalidatedValue() {
    return value;
  }

  @Override
  protected void doSetUnvalidatedValue( LocalDate aValue ) {
    LocalDate val = aValue != null ? aValue : VALUE_OF_CLEARED;
    value = val;
    updateDisplayedValue();
  }

  @Override
  protected void doClearValue() {
    value = IMpvLocalDate.MIN_MIN_DATE;
    updateDisplayedValue();
  }

}

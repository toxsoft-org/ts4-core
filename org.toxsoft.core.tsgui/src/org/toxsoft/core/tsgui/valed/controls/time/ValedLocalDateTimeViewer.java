package org.toxsoft.core.tsgui.valed.controls.time;

import java.time.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tsgui.widgets.mpv.*;

/**
 * Displays {@link LocalDateTime} value as moment of time in millisecons from epoch start.
 * <p>
 * When displaying time part respects the value of the option {@link ValedLocalDateTimeMpv#OPDEF_MPV_TIME_LEN}.
 *
 * @author hazard157
 */
public class ValedLocalDateTimeViewer
    extends AbstractValedControl<LocalDateTime, Text> {

  private static final LocalDateTime VALUE_OF_CLEARED = LocalDateTime.MIN;

  private LocalDateTime value = VALUE_OF_CLEARED;

  protected ValedLocalDateTimeViewer( ITsGuiContext aTsContext ) {
    super( aTsContext );
  }

  // ------------------------------------------------------------------------------------
  // inplementation
  //

  private void updateDisplayedValue() {
    Text txt = getControl();
    if( txt != null ) {
      EMpvTimeLen timeLen = ValedLocalDateTimeMpv.OPDEF_MPV_TIME_LEN.getValue( params() ).asValobj();
      txt.setText( timeLen.toString( value ) );
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
  protected LocalDateTime doGetUnvalidatedValue() {
    return value;
  }

  @Override
  protected void doSetUnvalidatedValue( LocalDateTime aValue ) {
    LocalDateTime val = aValue != null ? aValue : VALUE_OF_CLEARED;
    value = val;
    updateDisplayedValue();
  }

  @Override
  protected void doClearValue() {
    value = VALUE_OF_CLEARED;
    updateDisplayedValue();
  }

}

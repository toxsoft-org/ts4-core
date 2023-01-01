package org.toxsoft.core.tsgui.valed.controls.time;

import java.time.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tsgui.widgets.mpv.*;

/**
 * Displays {@link LocalTime} value as moment of time in millisecons from epoch start.
 * <p>
 * When displaying time part respects the value of the option {@link ValedLocalTimeMpv#OPDEF_MPV_TIME_LEN}.
 *
 * @author hazard157
 */
public class ValedLocalTimeViewer
    extends AbstractValedControl<LocalTime, Text> {

  private static final LocalTime VALUE_OF_CLEARED = LocalTime.MIN;

  private LocalTime value = VALUE_OF_CLEARED;

  protected ValedLocalTimeViewer( ITsGuiContext aTsContext ) {
    super( aTsContext );
  }

  // ------------------------------------------------------------------------------------
  // inplementation
  //

  private void updateDisplayedValue() {
    Text txt = getControl();
    if( txt != null ) {
      EMpvTimeLen timeLen = ValedLocalTimeMpv.OPDEF_MPV_TIME_LEN.getValue( params() ).asValobj();
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
  protected LocalTime doGetUnvalidatedValue() {
    return value;
  }

  @Override
  protected void doSetUnvalidatedValue( LocalTime aValue ) {
    LocalTime val = aValue != null ? aValue : VALUE_OF_CLEARED;
    value = val;
    updateDisplayedValue();
  }

  @Override
  protected void doClearValue() {
    value = VALUE_OF_CLEARED;
    updateDisplayedValue();
  }

}

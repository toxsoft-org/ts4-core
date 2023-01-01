package org.toxsoft.core.tsgui.valed.controls.time;

import java.time.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tsgui.widgets.mpv.*;

/**
 * Displays {@link Long} value as moment of time in millisecons from epoch start.
 * <p>
 * Display format depends on value of the option {@link ValedTimestampMpv#OPDEF_MPV_TIME_LEN}.
 *
 * @author hazard157
 */
class ValedTimestampViewer
    extends AbstractValedControl<Long, Text> {

  private static final Long VALUE_OF_CLEARED = Long.valueOf( 0L );

  private Long value = VALUE_OF_CLEARED;

  ValedTimestampViewer( ITsGuiContext aTsContext ) {
    super( aTsContext );
  }

  // ------------------------------------------------------------------------------------
  // inplementation
  //

  private void updateDisplayedValue() {
    Text txt = getControl();
    if( txt != null ) {
      Instant instant = Instant.ofEpochMilli( value.longValue() );
      LocalDateTime ldt = LocalDateTime.ofInstant( instant, ZoneId.systemDefault() );
      EMpvTimeLen timeLen = ValedLocalDateTimeMpv.OPDEF_MPV_TIME_LEN.getValue( params() ).asValobj();
      txt.setText( timeLen.toString( ldt ) );
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
  protected Long doGetUnvalidatedValue() {
    return value;
  }

  @Override
  protected void doSetUnvalidatedValue( Long aValue ) {
    Long val = aValue;
    if( val == null ) {
      val = VALUE_OF_CLEARED;
    }
    value = val;
    updateDisplayedValue();
  }

  @Override
  protected void doClearValue() {
    value = VALUE_OF_CLEARED;
    updateDisplayedValue();
  }

}

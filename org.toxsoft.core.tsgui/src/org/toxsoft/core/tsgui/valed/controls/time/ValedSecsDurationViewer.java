package org.toxsoft.core.tsgui.valed.controls.time;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Displays {@link Integer} value of duration seconds using as "HH:MM:SS" or "HH:MM" or "MM:SS".
 * <p>
 * Display format depends on options {@link ValedSecsDurationMpv#OPDEF_IS_HOURS_PART} and
 * {@link ValedSecsDurationMpv#OPDEF_IS_SECONDS_PART}.
 *
 * @author hazard157
 */
class ValedSecsDurationViewer
    extends AbstractValedControl<Integer, Text> {

  private static final Integer VALUE_OF_CLEARED = Integer.valueOf( 0 );

  private Integer value = VALUE_OF_CLEARED;

  ValedSecsDurationViewer( ITsGuiContext aTsContext ) {
    super( aTsContext );
  }

  // ------------------------------------------------------------------------------------
  // inplementation
  //

  @SuppressWarnings( "boxing" )
  private void updateDisplayedValue() {
    Text txt = getControl();
    if( txt != null ) {
      int hhh = value / 3600;
      int v = value - hhh * 3600;
      int mm = v / 60;
      int ss = v % 60;
      int what = 0x00;
      if( ValedSecsDurationMpv.OPDEF_IS_SECONDS_PART.getValue( tsContext().params() ).asBool() ) {
        what |= 0x01;
      }
      if( ValedSecsDurationMpv.OPDEF_IS_HOURS_PART.getValue( tsContext().params() ).asBool() ) {
        what |= 0x02;
      }
      String s;
      switch( what ) {
        case 0x00: {
          s = String.format( "%02d", mm ); //$NON-NLS-1$
          break;
        }
        case 0x01: {
          s = String.format( "%02d:%02d", mm, ss ); //$NON-NLS-1$
          break;
        }
        case 0x02: {
          s = String.format( "%02d:%02d", hhh, mm ); //$NON-NLS-1$
          break;
        }
        case 0x03: {
          s = String.format( "%02d:%02d:%02d", hhh, mm, ss ); //$NON-NLS-1$
          break;
        }
        default:
          throw new TsNotAllEnumsUsedRtException();
      }
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
  protected Integer doGetUnvalidatedValue() {
    return value;
  }

  @Override
  protected void doSetUnvalidatedValue( Integer aValue ) {
    Integer val = aValue;
    if( val == null || val.intValue() < 0 ) {
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

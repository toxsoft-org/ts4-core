package org.toxsoft.core.tsgui.valed.controls.time;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tsgui.valed.controls.time.ITsResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tsgui.widgets.mpv.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.time.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.math.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Edits {@link Long} value as moment of time in millisecons from epoch start.
 * <p>
 * Display format depends on value of the option {@link ValedTimestampMpv#OPDEF_MPV_TIME_LEN}.
 *
 * @author hazard157
 */
public class ValedTimestampMpv
    extends AbstractValedControl<Long, MultiPartValueWidget> {

  /**
   * ID of the {@link #OPDEF_MPV_TIME_LEN}.
   */
  public static final String OPID_MPV_TIME_LEN = VALED_OPID_PREFIX + ".ValedTimestampMpv.MpvTimeLen"; //$NON-NLS-1$

  /**
   * The flag determines which parts of time will be visible.
   * <p>
   * <b>Warning:</b> this option is used only in constructor, changes after will have no effect.
   */
  public static final IDataDef OPDEF_MPV_TIME_LEN = DataDef.create( OPID_MPV_TIME_LEN, BOOLEAN, //
      TSID_NAME, STR_MPV_TIME_LEN, //
      TSID_DESCRIPTION, STR_MPV_TIME_LEN_D, //
      TSID_DEFAULT_VALUE, avValobj( EMpvTimeLen.HH_MM_SS )//
  );

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".TimestampMpv"; //$NON-NLS-1$

  /**
   * The factory class.
   *
   * @author hazard157
   */
  @SuppressWarnings( "unchecked" )
  static class Factory
      extends AbstractValedControlFactory {

    protected Factory() {
      super( FACTORY_NAME );
    }

    @Override
    protected IValedControl<Long> doCreateEditor( ITsGuiContext aContext ) {
      AbstractValedControl<Long, ?> e = new ValedTimestampMpv( aContext );
      return e;
    }

    @Override
    protected IValedControl<Long> doCreateViewer( ITsGuiContext aContext ) {
      AbstractValedControl<Long, ?> e = new ValedTimestampViewer( aContext );
      return e;
    }

  }

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  private final IMpvTimestamp mpv;

  /**
   * Constructor.
   *
   * @param aTsContext {@link ITsGuiContext} - the valed context
   * @throws TsNullArgumentRtException аргумент = null
   */
  public ValedTimestampMpv( ITsGuiContext aTsContext ) {
    super( aTsContext );
    setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_TRUE );
    setParamIfNull( OPDEF_VERTICAL_SPAN, AV_1 );
    mpv = IMpvTimestamp.create( OPDEF_MPV_TIME_LEN.getValue( params() ).asValobj() );
    updateAllowedRange();
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private void updateAllowedRange() {
    LongRange r = IAvMetaConstants.makeTimestampRangeFromConstraints( params() );
    if( r != LongRange.FULL ) {
      mpv.setInterval( new TimeInterval( r.minValue(), r.maxValue() ) );
    }
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedControl
  //

  @Override
  public <X extends ITsContextRo> void onContextOpChanged( X aSource, String aId, IAtomicValue aValue ) {
    if( aId == null ) {
      updateAllowedRange();
      return;
    }
    switch( aId ) {
      case TSID_MIN_EXCLUSIVE:
      case TSID_MIN_INCLUSIVE:
      case TSID_MAX_EXCLUSIVE:
      case TSID_MAX_INCLUSIVE: {
        updateAllowedRange();
        break;
      }
      default:
        break;
    }
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedControl
  //

  @Override
  protected MultiPartValueWidget doCreateControl( Composite aParent ) {
    MultiPartValueWidget w = new MultiPartValueWidget( aParent, SWT.NONE, mpv );
    w.setEditable( isEditable() );
    return w;
  }

  @Override
  protected void doSetEditable( boolean aEditable ) {
    getControl().setEditable( aEditable );
  }

  @Override
  public ValidationResult canGetValue() {
    return ValidationResult.SUCCESS;
  }

  @Override
  protected Long doGetUnvalidatedValue() {
    return Long.valueOf( mpv.getAsTimestamp() );
  }

  @Override
  protected void doSetUnvalidatedValue( Long aValue ) {
    long val = aValue != null ? aValue.longValue() : 0;
    mpv.setAsTimestamp( val );
  }

  @Override
  protected void doClearValue() {
    mpv.setAsTimestamp( 0 );
  }

}

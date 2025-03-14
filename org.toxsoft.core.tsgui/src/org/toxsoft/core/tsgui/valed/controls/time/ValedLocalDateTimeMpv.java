package org.toxsoft.core.tsgui.valed.controls.time;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tsgui.valed.controls.time.ITsResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import java.time.*;

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
import org.toxsoft.core.tslib.bricks.time.*;
import org.toxsoft.core.tslib.bricks.time.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Edits {@link LocalDateTime} value as moment of time in millisecons from epoch start.
 * <p>
 * This valed accepts {@link IAvMetaConstants#TSID_MIN_INCLUSIVE} and {@link IAvMetaConstants#TSID_MAX_INCLUSIVE}
 * constraints of type {@link LocalDateTime}, but <b>ignores</b> {@link IAvMetaConstants#TSID_MIN_EXCLUSIVE} and
 * {@link IAvMetaConstants#TSID_MAX_EXCLUSIVE} constraints.
 *
 * @author hazard157
 */
public class ValedLocalDateTimeMpv
    extends AbstractValedControl<LocalDateTime, MultiPartValueWidget> {

  /**
   * ID of the {@link #OPDEF_MPV_TIME_LEN}.
   */
  public static final String OPID_MPV_TIME_LEN = VALED_OPID_PREFIX + ".ValedTimestampMpv.MpvTimeLen"; //$NON-NLS-1$

  /**
   * The flag determines which parts of time will be visible.
   * <p>
   * <b>Warning:</b> this option is used only in construictor, changes after will have no effect.
   */
  public static final IDataDef OPDEF_MPV_TIME_LEN = DataDef.create( OPID_MPV_TIME_LEN, BOOLEAN, //
      TSID_NAME, STR_MPV_TIME_LEN, //
      TSID_DESCRIPTION, STR_MPV_TIME_LEN_D, //
      TSID_DEFAULT_VALUE, avValobj( EMpvTimeLen.HH_MM_SS )//
  );

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".LocalDateTimeMpv"; //$NON-NLS-1$

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
    protected IValedControl<LocalDateTime> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedLocalDateTimeMpv( aContext );
    }

    @Override
    protected IValedControl<LocalDateTime> doCreateViewer( ITsGuiContext aContext ) {
      return new ValedLocalDateTimeViewer( aContext );
    }

    @Override
    protected boolean isSuitableRawEditor( Class<?> aValueClass, ITsGuiContext aEditorContext ) {
      return aValueClass.equals( LocalDateTime.class );
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
  public ValedLocalDateTimeMpv( ITsGuiContext aTsContext ) {
    super( aTsContext );
    setParamIfNull( OPDEF_IS_WIDTH_FIXED, AV_FALSE );
    setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_TRUE );
    setParamIfNull( OPDEF_VERTICAL_SPAN, AV_1 );
    mpv = IMpvTimestamp.create( OPDEF_MPV_TIME_LEN.getValue( params() ).asValobj() );
    updateAllowedRange();
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private void updateAllowedRange() {
    LocalDateTime minVal = params().getValobj( TSID_MIN_INCLUSIVE, mpv.getWidestInterval().getStartDatetime() );
    LocalDateTime maxVal = params().getValobj( TSID_MAX_INCLUSIVE, mpv.getWidestInterval().getEndDatetime() );
    if( minVal.isBefore( maxVal ) ) {
      ITimeInterval newRange = new TimeInterval( minVal, maxVal );
      if( !newRange.equals( mpv.getInterval() ) ) {
        mpv.setInterval( newRange );
      }
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
      case TSID_MIN_INCLUSIVE:
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
  protected LocalDateTime doGetUnvalidatedValue() {
    return mpv.getAsDatetime();
  }

  @Override
  protected void doSetUnvalidatedValue( LocalDateTime aValue ) {
    LocalDateTime val = aValue != null ? aValue : LocalDateTime.MIN;
    mpv.setAsDatetime( val );
  }

  @Override
  protected void doClearValue() {
    mpv.setAsDatetime( LocalDateTime.MIN );
  }

}

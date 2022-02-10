package org.toxsoft.core.tsgui.valed.controls.time;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tsgui.valed.controls.time.ITsResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import java.time.LocalDateTime;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.valed.api.IValedControl;
import org.toxsoft.core.tsgui.valed.impl.AbstractValedControl;
import org.toxsoft.core.tsgui.valed.impl.AbstractValedControlFactory;
import org.toxsoft.core.tsgui.widgets.mpv.*;
import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.av.impl.DataDef;
import org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants;
import org.toxsoft.core.tslib.av.metainfo.IDataDef;
import org.toxsoft.core.tslib.bricks.ctx.ITsContextRo;
import org.toxsoft.core.tslib.bricks.time.ITimeInterval;
import org.toxsoft.core.tslib.bricks.time.impl.TimeInterval;
import org.toxsoft.core.tslib.bricks.validator.ValidationResult;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Edits {@link Long} value as moment of time in millisecons from epoch start.
 * <p>
 * This valed accepts {@link IAvMetaConstants#TSID_MIN_INCLUSIVE} and {@link IAvMetaConstants#TSID_MAX_INCLUSIVE}
 * constraints of type {@link LocalDateTime}, but <b>ignores</b> {@link IAvMetaConstants#TSID_MIN_EXCLUSIVE} and
 * {@link IAvMetaConstants#TSID_MAX_EXCLUSIVE} constraints.
 *
 * @author goga
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
      TSID_NAME, STR_N_MPV_TIME_LEN, //
      TSID_DESCRIPTION, STR_D_MPV_TIME_LEN, //
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
  static class Factory
      extends AbstractValedControlFactory {

    protected Factory() {
      super( FACTORY_NAME );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected IValedControl<LocalDateTime> doCreateEditor( ITsGuiContext aContext ) {
      AbstractValedControl<LocalDateTime, ?> e = new ValedLocalDateTimeMpv( aContext );
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
  public ValedLocalDateTimeMpv( ITsGuiContext aTsContext ) {
    super( aTsContext );
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

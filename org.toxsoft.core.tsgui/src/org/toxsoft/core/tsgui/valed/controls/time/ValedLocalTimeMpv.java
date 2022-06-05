package org.toxsoft.core.tsgui.valed.controls.time;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tsgui.valed.controls.time.ITsResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import java.time.LocalTime;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.valed.api.IValedControl;
import org.toxsoft.core.tsgui.valed.impl.AbstractValedControl;
import org.toxsoft.core.tsgui.valed.impl.AbstractValedControlFactory;
import org.toxsoft.core.tsgui.widgets.mpv.IMpvLocalTime;
import org.toxsoft.core.tsgui.widgets.mpv.MultiPartValueWidget;
import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.av.impl.DataDef;
import org.toxsoft.core.tslib.av.metainfo.IDataDef;
import org.toxsoft.core.tslib.bricks.ctx.ITsContextRo;
import org.toxsoft.core.tslib.bricks.validator.ValidationResult;
import org.toxsoft.core.tslib.math.IntRange;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Edits {@link LocalTime} value as HH:MM:SS or HH:MM:SS.mmm.
 *
 * @author hazard157
 */
public class ValedLocalTimeMpv
    extends AbstractValedControl<LocalTime, MultiPartValueWidget> {

  /**
   * ID of the {@link #OPDEF_MPV_TIME_LEN}.
   */
  public static final String OPID_MPV_TIME_LEN = VALED_OPID_PREFIX + ".ValedLocalTimeMpv.IsMillisecs"; //$NON-NLS-1$

  /**
   * The flag determines if millisecons field will be visible.
   * <p>
   * <b>Warning:</b> this option is used only in construictor, changes after will have no effect.
   */
  public static final IDataDef OPDEF_MPV_TIME_LEN = DataDef.create( OPID_MPV_TIME_LEN, BOOLEAN, //
      TSID_NAME, STR_N_MPV_TIME_LEN, //
      TSID_DESCRIPTION, STR_D_MPV_TIME_LEN, //
      TSID_DEFAULT_VALUE, AV_FALSE //
  );

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".LocalTimeMpv"; //$NON-NLS-1$

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
    protected IValedControl<LocalTime> doCreateEditor( ITsGuiContext aContext ) {
      AbstractValedControl<LocalTime, ?> e = new ValedLocalTimeMpv( aContext );
      return e;
    }

  }

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  private final IMpvLocalTime mpv;

  /**
   * Constructor.
   *
   * @param aTsContext {@link ITsGuiContext} - the valed context
   * @throws TsNullArgumentRtException аргумент = null
   */
  public ValedLocalTimeMpv( ITsGuiContext aTsContext ) {
    super( aTsContext );
    mpv = IMpvLocalTime.create( OPDEF_MPV_TIME_LEN.getValue( params() ).asValobj() );
    updateAllowedRange();
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private void updateAllowedRange() {
    int minValue = Integer.MIN_VALUE;
    if( params().hasValue( TSID_MIN_INCLUSIVE ) ) {
      minValue = params().getInt( TSID_MIN_INCLUSIVE );
    }
    else {
      if( params().hasValue( TSID_MIN_EXCLUSIVE ) ) {
        minValue = params().getInt( TSID_MIN_EXCLUSIVE ) + 1;
      }
    }
    int maxValue = Integer.MAX_VALUE;
    if( params().hasValue( TSID_MAX_INCLUSIVE ) ) {
      maxValue = params().getInt( TSID_MAX_INCLUSIVE );
    }
    else {
      if( params().hasValue( TSID_MAX_EXCLUSIVE ) ) {
        maxValue = params().getInt( TSID_MAX_EXCLUSIVE ) - 1;
      }
    }
    if( minValue <= maxValue ) {
      mpv.setRangeSecs( new IntRange( minValue, maxValue ) );
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
  protected LocalTime doGetUnvalidatedValue() {
    return mpv.getAsLocalTime();
  }

  @Override
  protected void doSetUnvalidatedValue( LocalTime aValue ) {
    LocalTime val = aValue != null ? aValue : LocalTime.MIDNIGHT;
    mpv.setAsLocalTime( val );
  }

  @Override
  protected void doClearValue() {
    mpv.setAsLocalTime( LocalTime.MIDNIGHT );
  }

}

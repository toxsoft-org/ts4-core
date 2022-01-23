package org.toxsoft.tsgui.valed.controls.time;

import static org.toxsoft.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.tsgui.valed.controls.time.ITsResources.*;
import static org.toxsoft.tslib.av.EAtomicType.*;
import static org.toxsoft.tslib.av.impl.AvUtils.*;
import static org.toxsoft.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.toxsoft.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.tsgui.valed.api.IValedControl;
import org.toxsoft.tsgui.valed.impl.AbstractValedControl;
import org.toxsoft.tsgui.valed.impl.AbstractValedControlFactory;
import org.toxsoft.tsgui.widgets.mpv.IMpvSecsDuration;
import org.toxsoft.tsgui.widgets.mpv.MultiPartValueWidget;
import org.toxsoft.tslib.av.IAtomicValue;
import org.toxsoft.tslib.av.impl.DataDef;
import org.toxsoft.tslib.av.metainfo.IDataDef;
import org.toxsoft.tslib.bricks.ctx.ITsContextRo;
import org.toxsoft.tslib.bricks.validator.ValidationResult;
import org.toxsoft.tslib.math.IntRange;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Edits {@link Integer} value of duration seconds using {@link IMpvSecsDuration} as "HH:MM:SS".
 *
 * @author goga
 */
public class ValedSecsDurationMpv
    extends AbstractValedControl<Integer, MultiPartValueWidget> {

  /**
   * ID of the {@link #OPDEF_IS_HOURS_PART}.
   */
  public static final String OPID_IS_HOURS_PART = VALED_OPID_PREFIX + ".ValedTimestampMpv.MpvTimeLen"; //$NON-NLS-1$

  /**
   * The flag determines if HH part is present in HH:MM:SS or HH:MM editor.
   * <p>
   * <b>Warning:</b> this option is used only in construictor, changes after will have no effect.
   */
  public static final IDataDef OPDEF_IS_HOURS_PART = DataDef.create( OPID_IS_HOURS_PART, BOOLEAN, //
      TSID_NAME, STR_N_IS_HOURS_PART, //
      TSID_DESCRIPTION, STR_D_IS_HOURS_PART, //
      TSID_DEFAULT_VALUE, AV_TRUE //
  );

  /**
   * ID of the {@link #OPDEF_IS_SECONDS_PART}.
   */
  public static final String OPID_IS_SECONDS_PART = VALED_OPID_PREFIX + ".ValedTimestampMpv.MpvTimeLen"; //$NON-NLS-1$

  /**
   * The flag determines if SS part is present in HH:MM:SS or MM:SS editor.
   * <p>
   * <b>Warning:</b> this option is used only in construictor, changes after will have no effect.
   */
  public static final IDataDef OPDEF_IS_SECONDS_PART = DataDef.create( OPID_IS_SECONDS_PART, BOOLEAN, //
      TSID_NAME, STR_N_IS_SECONDS_PART, //
      TSID_DESCRIPTION, STR_D_IS_SECONDS_PART, //
      TSID_DEFAULT_VALUE, AV_TRUE //
  );

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".SecsDurationMpv"; //$NON-NLS-1$

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
    protected IValedControl<Integer> doCreateEditor( ITsGuiContext aContext ) {
      AbstractValedControl<Integer, ?> e = new ValedSecsDurationMpv( aContext );
      return e;
    }

  }

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  private final IMpvSecsDuration mpv;

  /**
   * Constructor.
   *
   * @param aTsContext {@link ITsGuiContext} - the valed context
   * @throws TsNullArgumentRtException аргумент = null
   */
  public ValedSecsDurationMpv( ITsGuiContext aTsContext ) {
    super( aTsContext );
    boolean isHoursPart = params().getBool( OPDEF_IS_HOURS_PART );
    boolean isSecondsPart = params().getBool( OPDEF_IS_SECONDS_PART );
    mpv = IMpvSecsDuration.create( isHoursPart, isSecondsPart );
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
      mpv.setRange( new IntRange( minValue, maxValue ) );
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
  protected Integer doGetUnvalidatedValue() {
    return Integer.valueOf( mpv.getDurationSecs() );
  }

  @Override
  protected void doSetUnvalidatedValue( Integer aValue ) {
    int val = aValue != null ? aValue.intValue() : 0;
    mpv.setDurationSecs( val );
  }

  @Override
  protected void doClearValue() {
    mpv.setDurationSecs( 0 );
  }

}

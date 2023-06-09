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
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.math.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Edits {@link Integer} value of duration seconds using {@link IMpvSecsDuration} as "HH:MM:SS" or "HH:MM" or "MM:SS".
 * <p>
 * Display format depends on options {@link ValedSecsDurationMpv#OPDEF_IS_HOURS_PART} and
 * {@link ValedSecsDurationMpv#OPDEF_IS_SECONDS_PART}.
 *
 * @author hazard157
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
  @SuppressWarnings( "unchecked" )
  static class Factory
      extends AbstractValedControlFactory {

    protected Factory() {
      super( FACTORY_NAME );
    }

    @Override
    protected IValedControl<Integer> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedSecsDurationMpv( aContext );
    }

    @Override
    protected IValedControl<Integer> doCreateViewer( ITsGuiContext aContext ) {
      return new ValedSecsDurationViewer( aContext );
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
    setParamIfNull( OPDEF_IS_WIDTH_FIXED, AV_FALSE );
    setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_TRUE );
    setParamIfNull( OPDEF_VERTICAL_SPAN, AV_1 );
    boolean isHoursPart = params().getBool( OPDEF_IS_HOURS_PART );
    boolean isSecondsPart = params().getBool( OPDEF_IS_SECONDS_PART );
    mpv = IMpvSecsDuration.create( isHoursPart, isSecondsPart );
    updateAllowedRange();
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private void updateAllowedRange() {
    IntRange r = IAvMetaConstants.makeIntRangeFromConstraints( params() );
    if( r != IntRange.FULL ) {
      mpv.setRange( r );
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

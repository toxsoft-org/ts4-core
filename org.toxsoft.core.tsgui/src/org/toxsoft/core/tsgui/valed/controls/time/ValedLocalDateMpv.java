package org.toxsoft.core.tsgui.valed.controls.time;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import java.time.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tsgui.widgets.mpv.*;
import org.toxsoft.core.tsgui.widgets.mpv.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Edits {@link LocalDate} value as YYYY-MM-DD.
 *
 * @author hazard157
 */
public class ValedLocalDateMpv
    extends AbstractValedControl<LocalDate, MultiPartValueWidget> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".LocalDateMpv"; //$NON-NLS-1$

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
    protected IValedControl<LocalDate> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedLocalDateMpv( aContext );
    }

  }

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  private final IMpvLocalDate mpv;

  /**
   * Constructor.
   *
   * @param aTsContext {@link ITsGuiContext} - the valed context
   * @throws TsNullArgumentRtException аргумент = null
   */
  public ValedLocalDateMpv( ITsGuiContext aTsContext ) {
    super( aTsContext );
    setParamIfNull( OPDEF_IS_WIDTH_FIXED, AV_FALSE );
    setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_TRUE );
    mpv = new MpvLocatDate();
    updateAllowedRange();
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private void updateAllowedRange() {
    LocalDate minValue = IMpvLocalDate.MIN_MIN_DATE;
    if( params().hasValue( TSID_MIN_INCLUSIVE ) ) {
      minValue = params().getValobj( TSID_MIN_INCLUSIVE );
    }
    else {
      if( params().hasValue( TSID_MIN_EXCLUSIVE ) ) {
        minValue = params().getValobj( TSID_MIN_EXCLUSIVE );
        minValue = minValue.plusDays( 1 );
      }
    }
    LocalDate maxValue = IMpvLocalDate.MIN_MIN_DATE;
    if( params().hasValue( TSID_MAX_INCLUSIVE ) ) {
      maxValue = params().getValobj( TSID_MAX_INCLUSIVE );
    }
    else {
      if( params().hasValue( TSID_MAX_EXCLUSIVE ) ) {
        maxValue = params().getValobj( TSID_MAX_EXCLUSIVE );
        maxValue = minValue.minusDays( 1 );
      }
    }
    if( maxValue.isAfter( minValue ) ) {
      mpv.setDateRange( minValue, maxValue );
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
  protected LocalDate doGetUnvalidatedValue() {
    return mpv.getAsLocalDate();
  }

  @Override
  protected void doSetUnvalidatedValue( LocalDate aValue ) {
    LocalDate val = aValue != null ? aValue : IMpvLocalDate.MIN_MIN_DATE;
    mpv.setAsLocalDate( val );
  }

  @Override
  protected void doClearValue() {
    mpv.setAsLocalDate( IMpvLocalDate.MIN_MIN_DATE );
  }

}

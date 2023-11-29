package org.toxsoft.core.tsgui.ved.valeds;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tsgui.ved.valeds.ITsResources.*;
import static org.toxsoft.core.tslib.bricks.strid.impl.StridUtils.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tsgui.valed.controls.helpers.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.bricks.validator.*;

/**
 * VALED to select a VISEL ID from the {@link IVedScreenModel#visels()}.
 * <p>
 * Expects {@link IVedScreen} reference to be placed in the argument context.
 *
 * @author hazard157
 */
public class ValedVedViselIdSelector
    extends AbstractValedTextAndButton<String> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".VedViselIdSelector"; //$NON-NLS-1$

  /**
   * The factory singleton.
   */
  @SuppressWarnings( "unchecked" )
  public static final AbstractValedControlFactory FACTORY = new AbstractValedControlFactory( FACTORY_NAME ) {

    @Override
    protected AbstractValedControl<String, Composite> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedVedViselIdSelector( aContext );
    }

  };

  /**
   * Provides VISEL as "ID - Name" in a visel selection dialog.
   */
  private static final ITsVisualsProvider<VedAbstractVisel> VISUALS_PROVIDER = aItem -> printf( FORMAT_ID_NAME, aItem );

  ValedVedViselIdSelector( ITsGuiContext aContext ) {
    super( aContext );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private IVedScreen vedScreen() {
    return tsContext().get( IVedScreen.class );
  }

  private IVedItemsManager<VedAbstractVisel> viselManager() {
    return vedScreen().model().visels();
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedTextAndButton
  //

  @Override
  protected boolean doProcessButtonPress() {
    ITsDialogInfo tdi = new TsDialogInfo( tsContext(), DLG_SELECT_VISEL_ID, DLG_SELECT_VISEL_ID_D );
    IVedVisel selVisel = viselManager().list().findByKey( doGetUnvalidatedValue() );
    IVedVisel visel = DialogItemsList.select( tdi, viselManager().list(), selVisel, VISUALS_PROVIDER );
    if( visel != null && visel != selVisel ) {
      String viselId = visel.id();
      getTextControl().setText( viselId );
    }
    return false;
  }

  @Override
  protected void doDoSetUnvalidatedValue( String aValue ) {
    String s = aValue != null ? aValue : EMPTY_STRING;
    getTextControl().setText( s );
  }

  @Override
  public ValidationResult canGetValue() {
    String id = getTextControl().getText();
    if( !viselManager().list().hasKey( id ) ) {
      return ValidationResult.warn( FMT_WARN_NO_VISEL_ID, id );
    }
    return ValidationResult.SUCCESS;
  }

  @Override
  protected String doGetUnvalidatedValue() {
    return getTextControl().getText();
  }

}

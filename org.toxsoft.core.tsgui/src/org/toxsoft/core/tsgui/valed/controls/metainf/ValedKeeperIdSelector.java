package org.toxsoft.core.tsgui.valed.controls.metainf;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tsgui.valed.controls.metainf.ITsResources.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.helpers.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.std.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.valobj.*;

/**
 * VALED to specify keeper ID either registered in {@link TsValobjUtils} or entered manually.
 * <p>
 * This is a single-line control based on {@link AbstractValedTextAndButton}.
 *
 * @author hazard157
 */
public class ValedKeeperIdSelector
    extends AbstractValedTextAndButton<String> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".KeeperIdSelector"; //$NON-NLS-1$

  /**
   * The factory class.
   *
   * @author hazard157
   */
  @SuppressWarnings( "unchecked" )
  public static final AbstractValedControlFactory FACTORY = new AbstractValedControlFactory( FACTORY_NAME ) {

    @Override
    protected IValedControl<String> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedKeeperIdSelector( aContext );
    }

    @Override
    protected IValedControl<String> doCreateSingleLine( ITsGuiContext aContext ) {
      return new ValedKeeperIdSelector( aContext );
    }

  };

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the VALED context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ValedKeeperIdSelector( ITsGuiContext aContext ) {
    super( aContext );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private static ValidationResult validateKeeperIsRegistered( String aKeeperId ) {
    if( TsValobjUtils.findKeeperById( aKeeperId ) == null ) {
      return ValidationResult.warn( FMT_UNREGISTERED_KEEPER_ID, aKeeperId );
    }
    return ValidationResult.SUCCESS;
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedTextAndButton
  //

  @Override
  protected boolean doProcessButtonPress() {
    ITsDialogInfo di = TsDialogInfo.forSelectEntity( tsContext() );
    IStringListBasicEdit items = new SortedStringLinkedBundleList();
    IStringMap<IEntityKeeper<?>> kmap = TsValobjUtils.getRegisteredKeepers();
    IStringMap<ValobjInfo> vimap = TsValobjUtils.getRegisteredInfos();
    items.addAll( kmap.keys() );
    String sel = getTextControl().getText();
    ITsNameProvider<String> nameProvider = aItem -> {
      ValobjInfo vi = vimap.findByKey( aItem );
      if( vi != null ) {
        return aItem + " - " + vi.name(); //$NON-NLS-1$
      }
      return aItem;
    };
    String keeperId = DialogItemsList.select( di, items, sel, nameProvider );
    if( keeperId != null && !keeperId.equals( sel ) ) {
      getTextControl().setText( keeperId );
      return true;
    }
    return false;
  }

  @Override
  protected void doDoSetUnvalidatedValue( String aValue ) {
    getTextControl().setText( aValue );
  }

  @Override
  protected ValidationResult doCanGetValue() {
    String s = getTextControl().getText();
    ValidationResult vr = IdPathStringValidator.IDPATH_VALIDATOR.validate( s );
    if( vr.isError() ) {
      return vr;
    }
    return ValidationResult.firstNonOk( vr, validateKeeperIsRegistered( s ) );
  }

  @Override
  protected String doGetUnvalidatedValue() {
    return getTextControl().getText();
  }

}

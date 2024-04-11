package org.toxsoft.core.tsgui.bricks.combicond;

import static org.toxsoft.core.tsgui.bricks.combicond.FormulaScpItemM5Model.*;
import static org.toxsoft.core.tsgui.bricks.combicond.ITsResources.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.math.combicond.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * LM for {@link FormulaScpItemM5Model}.
 * <p>
 * Contains internal list of the {@link FormulaScpItem}.
 *
 * @author hazard157
 */
class FormulaScpItemM5LifecycleManager
    extends M5LifecycleManager<FormulaScpItem, CombiCondParamsPanel> {

  public FormulaScpItemM5LifecycleManager( IM5Model<FormulaScpItem> aModel, CombiCondParamsPanel aMaster ) {
    super( aModel, true, true, true, true, aMaster );
    TsNullArgumentRtException.checkNull( aMaster );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  @Override
  protected ValidationResult doBeforeCreate( IM5Bunch<FormulaScpItem> aValues ) {
    return ValidationResult.SUCCESS;
  }

  @Override
  protected FormulaScpItem doCreate( IM5Bunch<FormulaScpItem> aValues ) {
    String kw = aValues.getAsAv( FID_KEYWORD ).asString();
    ISingleCondParams scp = aValues.getAsAv( FID_SCP ).asValobj();
    FormulaScpItem fsi = new FormulaScpItem( kw, scp );
    master().fsiItems().add( fsi );
    return fsi;
  }

  @Override
  protected ValidationResult doBeforeEdit( IM5Bunch<FormulaScpItem> aValues ) {
    return ValidationResult.SUCCESS;
  }

  @Override
  protected FormulaScpItem doEdit( IM5Bunch<FormulaScpItem> aValues ) {
    String kw = aValues.getAsAv( FID_KEYWORD ).asString();
    ISingleCondParams scp = aValues.getAsAv( FID_SCP ).asValobj();
    FormulaScpItem fsi = new FormulaScpItem( kw, scp );
    int index = master().fsiItems().indexOf( aValues.originalEntity() );
    master().fsiItems().set( index, fsi );
    return fsi;
  }

  @Override
  protected ValidationResult doBeforeRemove( FormulaScpItem aEntity ) {
    IStringList keywordsInFormula = master().parser().formulaTokens().listKeywords();
    if( keywordsInFormula.hasElem( aEntity.keyword() ) ) {
      return ValidationResult.error( FMT_ERR_CAN_REMOVE_FORMULA_KW, aEntity.keyword() );
    }
    return ValidationResult.SUCCESS;
  }

  @Override
  protected void doRemove( FormulaScpItem aEntity ) {
    master().fsiItems().remove( aEntity );
  }

  @Override
  protected IList<FormulaScpItem> doListEntities() {
    return master().fsiItems();
  }

}

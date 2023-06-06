package org.toxsoft.core.tsgui.bricks.gw;

import static org.toxsoft.core.tsgui.bricks.gw.IGwM5Constants.*;
import static org.toxsoft.core.tsgui.bricks.gw.l10n.ITsguiGwSharedResources.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.std.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The default lifecycle manager for {@link SkidM5Model}, considering SKID as simple value-object.
 * <p>
 * No master-object is needed for this lifecycle manager. Supports only creation and editing of entities. Removal and
 * listing is not supported.
 * <p>
 * Created and edits entity based only on {@link SkidM5Model#CLASS_ID} and {@link SkidM5Model#OBJ_STRID}. Field
 * {@link SkidM5Model#SKID} is ignored.
 *
 * @author hazard157
 */
public class DefaultSkidM5LifecycleManager
    extends M5LifecycleManager<Skid, Object> {

  /**
   * Constructor.
   *
   * @param aModel {@link IM5Model}&lt;{@link Skid}&gt; - the model
   * @throws TsNullArgumentRtException model is <code>null</code>
   */
  public DefaultSkidM5LifecycleManager( IM5Model<Skid> aModel ) {
    super( aModel, true, true, false, false, null );
  }

  // ------------------------------------------------------------------------------------
  // M5LifecycleManager
  //

  @Override
  protected ValidationResult doBeforeCreate( IM5Bunch<Skid> aValues ) {
    String classId = aValues.getAsAv( FID_CLASS_ID ).asString();
    if( !IdPathStringValidator.IDPATH_VALIDATOR.isValid( classId ) ) {
      return ValidationResult.error( FMT_ERR_SKID_INV_CLASS_ID, classId );
    }
    String objStrid = aValues.getAsAv( FID_OBJ_STRID ).asString();
    if( !IdPathStringValidator.IDPATH_VALIDATOR.isValid( objStrid ) ) {
      return ValidationResult.error( FMT_ERR_SKID_INV_OBJ_STRID, objStrid );
    }
    return ValidationResult.SUCCESS;
  }

  @Override
  protected Skid doCreate( IM5Bunch<Skid> aValues ) {
    String classId = aValues.getAsAv( FID_CLASS_ID ).asString();
    String objStrid = aValues.getAsAv( FID_OBJ_STRID ).asString();
    return new Skid( classId, objStrid );
  }

  @Override
  protected ValidationResult doBeforeEdit( IM5Bunch<Skid> aValues ) {
    return doBeforeCreate( aValues );
  }

  @Override
  protected Skid doEdit( IM5Bunch<Skid> aValues ) {
    return doCreate( aValues );
  }

}

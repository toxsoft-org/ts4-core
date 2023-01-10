package org.toxsoft.core.tsgui.m5.std.models.misc;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.m5.std.models.misc.ITsResources.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.coll.helpers.*;

/**
 * M5-model of the {@link CollConstraintM5Model}.
 *
 * @author dima
 */
public class CollConstraintM5Model
    extends M5Model<CollConstraint> {

  /**
   * The model ID.
   */
  public static final String MODEL_ID = M5_ID + ".CollConstraint"; //$NON-NLS-1$

  /**
   * ID of field {@link #IS_DUPS_PROHIBITED}.
   */
  public static final String FID_IS_DUPS_PROHIBITED = "isDuplicatesProhibited"; //$NON-NLS-1$

  /**
   * ID of field {@link #IS_EMPTY_PROHIBITED}.
   */
  public static final String FID_IS_EMPTY_PROHIBITED = "isEmptyProhibited"; //$NON-NLS-1$

  /**
   * ID of field {@link #IS_EXACT_COUNT}.
   */
  public static final String FID_IS_EXACT_COUNT = "isExactCount"; //$NON-NLS-1$

  /**
   * ID of field {@link #MAX_COUNT}.
   */
  public static final String FID_MAX_COUNT = "maxCount"; //$NON-NLS-1$

  /**
   * M5-attribute {@link CollConstraint#isDuplicatesProhibited()}.
   */
  public final IM5AttributeFieldDef<CollConstraint> IS_DUPS_PROHIBITED =
      new M5AttributeFieldDef<>( FID_IS_DUPS_PROHIBITED, DDEF_BOOLEAN ) {

        @Override
        protected void doInit() {
          setNameAndDescription( STR_N_IS_DUPS_PROHIBITED, STR_D_IS_DUPS_PROHIBITED );
          setFlags( M5FF_COLUMN );
        }

        @Override
        protected IAtomicValue doGetFieldValue( CollConstraint aEntity ) {
          return avBool( aEntity.isDuplicatesProhibited() );
        }

      };

  /**
   * M5-attribute {@link CollConstraint#isEmptyProhibited()}.
   */
  public final IM5AttributeFieldDef<CollConstraint> IS_EMPTY_PROHIBITED =
      new M5AttributeFieldDef<>( FID_IS_EMPTY_PROHIBITED, DDEF_BOOLEAN ) {

        @Override
        protected void doInit() {
          setNameAndDescription( STR_N_IS_EMPTY_PROHIBITED, STR_D_IS_EMPTY_PROHIBITED );
          setFlags( M5FF_COLUMN );
        }

        @Override
        protected IAtomicValue doGetFieldValue( CollConstraint aEntity ) {
          return avBool( aEntity.isEmptyProhibited() );
        }

      };

  /**
   * M5-attribute {@link CollConstraint#isExactCount()}.
   */
  public final IM5AttributeFieldDef<CollConstraint> IS_EXACT_COUNT =
      new M5AttributeFieldDef<>( FID_IS_EXACT_COUNT, DDEF_BOOLEAN ) {

        @Override
        protected void doInit() {
          setNameAndDescription( STR_N_IS_EXACT_COUNT, STR_D_IS_EXACT_COUNT );
          setFlags( M5FF_COLUMN );
        }

        @Override
        protected IAtomicValue doGetFieldValue( CollConstraint aEntity ) {
          return avBool( aEntity.isExactCount() );
        }

      };

  /**
   * M5-attribute {@link CollConstraint#maxCount()}.
   */
  public final IM5AttributeFieldDef<CollConstraint> MAX_COUNT =
      new M5AttributeFieldDef<>( FID_MAX_COUNT, DDEF_INTEGER ) {

        @Override
        protected void doInit() {
          setNameAndDescription( STR_N_MAX_COUNT, STR_D_MAX_COUNT );
          setFlags( M5FF_COLUMN );
        }

        @Override
        protected IAtomicValue doGetFieldValue( CollConstraint aEntity ) {
          return avInt( aEntity.maxCount() );
        }

      };

  /**
   * LM for this model.
   *
   * @author hazard157
   */
  class LifecycleManager
      extends M5LifecycleManager<CollConstraint, Object> {

    public LifecycleManager( IM5Model<CollConstraint> aModel ) {
      super( aModel, true, true, true, false, null );
    }

    private CollConstraint makeCollConstraint( IM5Bunch<CollConstraint> aValues ) {
      IAtomicValue isDuplicatesProhibited = IS_DUPS_PROHIBITED.getFieldValue( aValues );
      IAtomicValue isEmptyProhibited = IS_EMPTY_PROHIBITED.getFieldValue( aValues );
      IAtomicValue isExactCount = IS_EXACT_COUNT.getFieldValue( aValues );
      IAtomicValue maxCount = MAX_COUNT.getFieldValue( aValues );
      CollConstraint collConstraint = new CollConstraint( maxCount.asInt(), isExactCount.asBool(),
          isEmptyProhibited.asBool(), isDuplicatesProhibited.asBool() );
      return collConstraint;
    }

    @Override
    protected CollConstraint doCreate( IM5Bunch<CollConstraint> aValues ) {
      return makeCollConstraint( aValues );
    }

    @Override
    protected CollConstraint doEdit( IM5Bunch<CollConstraint> aValues ) {
      return makeCollConstraint( aValues );
    }

    @Override
    protected void doRemove( CollConstraint aEntity ) {
      // nop
    }

  }

  /**
   * Constructor.
   */
  public CollConstraintM5Model() {
    super( MODEL_ID, CollConstraint.class );
    addFieldDefs( IS_DUPS_PROHIBITED, IS_EMPTY_PROHIBITED, IS_EXACT_COUNT, MAX_COUNT );
  }

  @Override
  protected IM5LifecycleManager<CollConstraint> doCreateDefaultLifecycleManager() {
    return new LifecycleManager( this );
  }

  @Override
  protected IM5LifecycleManager<CollConstraint> doCreateLifecycleManager( Object aMaster ) {
    return getLifecycleManager( null );
  }

}

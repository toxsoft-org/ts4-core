package org.toxsoft.core.tsgui.m5.std.models.av;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.m5.std.models.av.ITsResources.*;

import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.misc.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Basic M5-model of {@link IDataType} and {@link IDataDef}.
 *
 * @author hazard157
 * @param <T> - concrete type of modelled entity
 */
public class DataMetaInfoBasicM5Model<T extends IDataType>
    extends M5Model<T> {

  /**
   * The ID of the field {@link #ATOMIC_TYPE}.
   */
  public final String FID_ATOMIC_TYPE = "atomicType"; //$NON-NLS-1$

  /**
   * The ID of the field {@link #CONSTRAINTS}.
   */
  public final String FID_CONSTRAINTS = "constraints"; //$NON-NLS-1$

  /**
   * Field {@link T#atomicType()}.
   */
  public final IM5SingleLookupFieldDef<T, EAtomicType> ATOMIC_TYPE =
      new M5SingleLookupFieldDef<>( FID_ATOMIC_TYPE, AtomicTypeM5Model.MODEL_ID ) {

        @Override
        protected void doInit() {
          setNameAndDescription( STR_ATOMIC_TYPE, STR_ATOMIC_TYPE_D );
          setFlags( M5FF_COLUMN | M5FF_INVARIANT );
        }

        protected EAtomicType doGetFieldValue( T aEntity ) {
          return aEntity.atomicType();
        }

      };

  /**
   * Field {@link T#params()}.
   */
  public final IM5MultiModownFieldDef<T, IdValue> CONSTRAINTS =
      new M5MultiModownFieldDef<>( FID_CONSTRAINTS, IdValueM5Model.MODEL_ID ) {

        @Override
        protected void doInit() {
          setNameAndDescription( STR_N_CONSTRAINTS, STR_D_CONSTRAINTS );
          setFlags( M5FF_DETAIL );
          params().setInt( IValedControlConstants.OPDEF_VERTICAL_SPAN, 5 );
          params().setBool( IValedControlConstants.OPDEF_NO_FIELD_LABEL, true );
        }

        protected IList<IdValue> doGetFieldValue( T aEntity ) {
          return IdValue.makeIdValuesCollFromOptionSet( aEntity.params() ).values();
        }

      };

  /**
   * Constructor.
   *
   * @param aId String - model ID
   * @param aEntityClass {@link Class}&lt;T&gt; - modelled entity type
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException identifier is not an IDpath
   * @throws TsItemAlreadyExistsRtException model with specified ID already exists in domain
   */
  public DataMetaInfoBasicM5Model( String aId, Class<T> aEntityClass ) {
    super( aId, aEntityClass );
  }

}

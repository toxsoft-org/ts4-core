package org.toxsoft.core.tsgui.m5.std.models.av;

import static org.toxsoft.core.tsgui.m5.std.models.av.ITsResources.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.metainf.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * M5-model base implementation of {@link IDataType} and {@link IDataDef}..
 * <p>
 * Base implementation is designed with one editable field - the {@link #DATA_TYPE}. Other fields are read-only to be
 * displayed in columns, details, etc.
 * <p>
 * Does not has a lifetime manager.
 *
 * @author hazard157
 * @param <T> - modeled entity type
 */
public class DataTypeM5ModelBase<T extends IDataType>
    extends M5Model<T> {

  /**
   * ID of field {@link #DATA_TYPE}.
   */
  public static final String FID_DATA_TYPE = "dataType"; //$NON-NLS-1$

  /**
   * ID of field {@link #ATOMIC_TYPE}.
   */
  public static final String FID_ATOMIC_TYPE = "atomicType"; //$NON-NLS-1$

  /**
   * Field returns modeled entity as is to be edited with {@link ValedDataType}.
   */
  public final IM5FieldDef<IDataType, IDataType> DATA_TYPE = new M5FieldDef<>( FID_DATA_TYPE, IDataType.class ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_FIELD_DATA_TYPE, STR_FIELD_DATA_TYPE_D );
      params().setBool( IValedControlConstants.OPDEF_NO_FIELD_LABEL, true );
      params().setInt( IValedControlConstants.OPDEF_VERTICAL_SPAN, 10 );
      params().setBool( IValedControlConstants.OPDEF_IS_HEIGHT_FIXED, false );
      setValedEditor( ValedDataType.FACTORY_NAME );
    }

    protected IDataType doGetFieldValue( IDataType aEntity ) {
      return aEntity;
    }

    protected String doGetFieldValueName( IDataType aEntity ) {
      return aEntity.toString();
    }

  };

  /**
   * Field {@link IDataType#atomicType()}
   */
  public final IM5SingleLookupFieldDef<IDataType, EAtomicType> ATOMIC_TYPE =
      new M5SingleLookupFieldDef<>( FID_ATOMIC_TYPE, AtomicTypeM5Model.MODEL_ID ) {

        @Override
        protected void doInit() {
          setNameAndDescription( STR_ATOMIC_TYPE, STR_ATOMIC_TYPE_D );
        }

        protected EAtomicType doGetFieldValue( IDataType aEntity ) {
          return aEntity.atomicType();
        }

      };

  /**
   * Constructor.
   *
   * @param aModelId String - model ID
   * @param aEntityClass {@link Class}&lt;T&gt; - modeled entity type
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException identifier is not an IDpath
   */
  protected DataTypeM5ModelBase( String aModelId, Class<T> aEntityClass ) {
    super( aModelId, aEntityClass );
  }

}

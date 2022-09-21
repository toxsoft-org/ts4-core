package org.toxsoft.core.tsgui.m5.std.models.av;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.m5.std.models.av.ITsResources.*;
import static org.toxsoft.core.tslib.ITsHardConstants.*;

import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;

/**
 * M5-model of {@link IDataType}.
 *
 * @author hazard157
 */
public class DataTypeM5Model
    extends M5Model<IDataType> {

  /**
   * The model ID.
   */
  public static final String MODEL_ID = TS_ID + "DataType"; //$NON-NLS-1$

  /**
   * The ID of the field {@link #ATOMIC_TYPE}.
   */
  public static final String FID_ATOMIC_TYPE = "atomicType"; //$NON-NLS-1$

  public static final IM5SingleLookupFieldDef<IDataType, EAtomicType> ATOMIC_TYPE =
      new M5SingleLookupFieldDef<>( FID_ATOMIC_TYPE, DataTypeM5Model.MODEL_ID ) {

        @Override
        protected void doInit() {
          setNameAndDescription( STR_N_ATOMIC_TYPE, STR_D_ATOMIC_TYPE );
          setFlags( M5FF_COLUMN | M5FF_INVARIANT );
        }

        protected EAtomicType doGetFieldValue( IDataType aEntity ) {
          return aEntity.atomicType();
        }

      };

  /**
   * Constructor.
   */
  public DataTypeM5Model() {
    super( MODEL_ID, IDataType.class );
    setNameAndDescription( STR_N_DATA_TYPE, STR_D_DATA_TYPE );
    addFieldDefs( ATOMIC_TYPE );
    // TODO Auto-generated constructor stub
  }

}

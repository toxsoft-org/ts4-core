package org.toxsoft.unit.txtproj.core.categs;

import static org.toxsoft.tslib.av.EAtomicType.*;
import static org.toxsoft.tslib.av.impl.AvUtils.*;
import static org.toxsoft.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.unit.txtproj.core.categs.ITsResources.*;

import org.toxsoft.tslib.av.impl.DataDef;
import org.toxsoft.tslib.av.metainfo.IDataDef;

/**
 * Опции {@link ICategory#params()}, используемые в каталоге {@link ICatalogue}.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ICatalogueParamOptions {

  String CATALOGUE_PARAM_ID_START  = "ts.catalogue";                 //$NON-NLS-1$
  String CATALOGUE_PARAM_ID_PREFIX = CATALOGUE_PARAM_ID_START + "."; //$NON-NLS-1$

  /**
   * Идентификатор опции {@link #OP_CAN_HAVE_CHILDREN}.
   */
  String OPID_CAN_HAVE_CHILDREN = CATALOGUE_PARAM_ID_PREFIX + "IsLeaf"; //$NON-NLS-1$

  /**
   * Опция: Признак, что категория не может иметь дочерные категория.
   */
  IDataDef OP_CAN_HAVE_CHILDREN = DataDef.create( OPID_CAN_HAVE_CHILDREN, BOOLEAN, //
      TSID_NAME, STR_N_OP_CAN_HAVE_CHILDREN, //
      TSID_DESCRIPTION, STR_D_OP_CAN_HAVE_CHILDREN, //
      TSID_DEFAULT_VALUE, AV_FALSE //
  );

}

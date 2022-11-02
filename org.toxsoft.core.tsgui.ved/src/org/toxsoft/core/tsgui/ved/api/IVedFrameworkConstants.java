package org.toxsoft.core.tsgui.ved.api;

import static org.toxsoft.core.tsgui.ved.api.ITsResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;

/**
 * Constants of the framework.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface IVedFrameworkConstants {

  // ------------------------------------------------------------------------------------
  // Document properties

  String PROPID_VED_DOC_DIMENSIONS = "Dimensions"; //$NON-NLS-1$
  String PROPID_VED_DOC_BACKGROUND = "Background"; //$NON-NLS-1$

  IDataDef PROPDEF_VED_DOC_DIMENSIONS = DataDef.create( PROPID_VED_DOC_DIMENSIONS, VALOBJ, //
      TSID_NAME, STR_N_VED_DOC_DIMENSIONS, //
      TSID_DESCRIPTION, STR_N_VED_DOC_DIMENSIONS, //
      TSID_KEEPER_ID, D2Point.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( new D2Point( 800, 600 ) ) //
  );

  IDataDef PROPDEF_VED_DOC_BACKGROUND = DataDef.create( PROPID_VED_DOC_BACKGROUND, VALOBJ, //
      TSID_NAME, STR_N_VED_DOC_BACKGROUND, //
      TSID_DESCRIPTION, STR_N_VED_DOC_BACKGROUND, //
      TSID_KEEPER_ID, TsFillInfo.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( new TsFillInfo( new RGBA( 255, 255, 255, 0 ) ) ) //
  );

  IStridablesList<IDataDef> VED_DOCUMENT_PROP_DEFS = new StridablesList<>( //
      PROPDEF_VED_DOC_DIMENSIONS, //
      PROPDEF_VED_DOC_BACKGROUND //
  );

}

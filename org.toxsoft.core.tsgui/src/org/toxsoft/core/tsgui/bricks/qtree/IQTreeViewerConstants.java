package org.toxsoft.core.tsgui.bricks.qtree;

import static org.toxsoft.core.tsgui.bricks.qtree.ITsResources.*;
import static org.toxsoft.core.tslib.ITsHardConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;

/**
 * Constants of {@link IQTreeViewer} implementation.
 *
 * @author hazard157
 */
public interface IQTreeViewerConstants {

  /**
   * Option IDs prefix.
   */
  String OPID_PREFIX = TS_ID + ".gui.QTreeViewer"; //$NON-NLS-1$

  /**
   * ID of option {@link #OPDEF_IS_HEADER_SHOWN}.
   */
  String OPID_IS_HEADER_SHOWN = OPID_PREFIX + ".isHeaderShown"; //$NON-NLS-1$

  /**
   * ID of option {@link #OPDEF_IS_LINES_VISIBLE}.
   */
  String OPID_IS_LINES_VISIBLE = OPID_PREFIX + ".isLinesVisible"; //$NON-NLS-1$

  /**
   * ID of option {@link #OPDEF_DEFAULT_ICON_SIZE}.
   */
  String OPID_DEFAULT_ICON_SIZE = OPID_PREFIX + ".defaultIconSize"; //$NON-NLS-1$

  /**
   * ID of option {@link #OPDEF_DEFAULT_THUMB_SIZE}.
   */
  String OPID_DEFAULT_THUMB_SIZE = OPID_PREFIX + ".defaultThumbSize"; //$NON-NLS-1$

  /**
   * Option: the flag marks if tree columns header bar is shown.
   */
  IDataDef OPDEF_IS_HEADER_SHOWN = DataDef.create( OPID_IS_HEADER_SHOWN, BOOLEAN, //
      TSID_NAME, STR_N_IS_HEADER_SHOWN, //
      TSID_DESCRIPTION, STR_D_IS_HEADER_SHOWN, //
      TSID_DEFAULT_VALUE, AV_TRUE //
  );

  /**
   * Option: the flag marks if tree columns header bar is shown.
   */
  IDataDef OPDEF_IS_LINES_VISIBLE = DataDef.create( OPID_IS_LINES_VISIBLE, BOOLEAN, //
      TSID_NAME, STR_N_IS_LINES_VISIBLE, //
      TSID_DESCRIPTION, STR_D_IS_LINES_VISIBLE, //
      TSID_DEFAULT_VALUE, AV_TRUE //
  );

  /**
   * Option: default and hence initial icons size in rows.
   * <p>
   * On icon/thumb size usage see comments for {@link IQTreeViewer}.
   *
   * @see IQTreeViewer
   */
  IDataDef OPDEF_DEFAULT_ICON_SIZE = DataDef.create( OPID_DEFAULT_ICON_SIZE, VALOBJ, //
      TSID_NAME, STR_N_DEFAULT_ICON_SIZE, //
      TSID_DESCRIPTION, STR_D_DEFAULT_ICON_SIZE, //
      TSID_DEFAULT_VALUE, avValobj( EIconSize.IS_16X16 ) //
  );

  /**
   * Option: default and hence initial thumb size in rows.
   * <p>
   * On icon/thumb size usage see comments for {@link IQTreeViewer}.
   *
   * @see IQTreeViewer
   */
  IDataDef OPDEF_DEFAULT_THUMB_SIZE = DataDef.create( OPID_DEFAULT_THUMB_SIZE, VALOBJ, //
      TSID_NAME, STR_N_DEFAULT_THUMB_SIZE, //
      TSID_DESCRIPTION, STR_D_DEFAULT_THUMB_SIZE, //
      TSID_DEFAULT_VALUE, avValobj( EThumbSize.SZ32 ) //
  );

}

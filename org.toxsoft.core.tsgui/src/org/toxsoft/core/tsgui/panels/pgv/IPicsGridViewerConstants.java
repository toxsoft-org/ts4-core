package org.toxsoft.core.tsgui.panels.pgv;

import static org.toxsoft.core.tsgui.panels.pgv.ITsResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;

/**
 * Package constants.
 *
 * @author hazard157
 */
public interface IPicsGridViewerConstants {

  /**
   * The flag for displaying labels under images.
   */
  IDataDef OPDEF_IS_LABELS_SHOWN = DataDef.create( "isLabelsShown", BOOLEAN, //$NON-NLS-1$
      TSID_NAME, STR_N_IS_LABELS_SHOWN, //
      TSID_DESCRIPTION, STR_D_IS_LABELS_SHOWN, //
      TSID_DEFAULT_VALUE, AV_TRUE //
  );

  /**
   * Max number of lines to display label under item.
   */
  IDataDef OPDEF_LABEL_LINES = DataDef.create( "labelLines", INTEGER, //$NON-NLS-1$
      TSID_NAME, STR_N_LABEL_LINES, //
      TSID_DESCRIPTION, STR_D_LABEL_LINES, //
      TSID_MIN_INCLUSIVE, avInt( 1 ), //
      TSID_MAX_INCLUSIVE, avInt( 4 ), //
      TSID_DEFAULT_VALUE, avInt( 2 ) //
  );

  /**
   * The flag for displaying pop-up hints for the image.
   */
  IDataDef OPDEF_IS_TOOLTIPS_SHOWN = DataDef.create( "isTooltipsShown", BOOLEAN, //$NON-NLS-1$
      TSID_NAME, STR_N_IS_TOOLTIPS_SHOWN, //
      TSID_DESCRIPTION, STR_D_IS_TOOLTIPS_SHOWN, //
      TSID_DEFAULT_VALUE, AV_TRUE //
  );

  /**
   * The default size of the displayed thumbnails.
   */
  IDataDef OPDEF_DEFAULT_THUMB_SIZE = DataDef.create( "defaultThumbSize", VALOBJ, //$NON-NLS-1$
      TSID_NAME, STR_N_DEFAULT_THUMB_SIZE, //
      TSID_DESCRIPTION, STR_D_DEFAULT_THUMB_SIZE, //
      TSID_KEEPER_ID, EThumbSize.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( EThumbSize.SZ128 )//
  );

  /**
   * The selection rectangle (border around item) drawing settings.
   */
  IDataDef OPDEF_SELECTION_BORDER_SETTINGS = DataDef.create( "selectionBorderSetting", VALOBJ, //$NON-NLS-1$
      TSID_NAME, STR_N_SELECTION_BORDER_SETTINGS, //
      TSID_DESCRIPTION, STR_D_SELECTION_BORDER_SETTINGS, //
      TSID_KEEPER_ID, TsBorderInfo.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( TsBorderInfo.createSimpleBorder( 1, ETsColor.DARK_GRAY.rgba() ) ) //
  );

  /**
   * The flag for displaying pop-up hints for the image.
   */
  IDataDef OPDEF_IS_ICONS_INSTEAD_OF_THUMBS = DataDef.create( "isIconsInsteadOfThumbs", BOOLEAN, //$NON-NLS-1$
      TSID_NAME, STR_N_IS_ICONS_INSTEAD_OF_THUMBS, //
      TSID_DESCRIPTION, STR_D_IS_ICONS_INSTEAD_OF_THUMBS, //
      TSID_DEFAULT_VALUE, AV_FALSE //
  );

  /**
   * All options as one list.
   */
  IStridablesList<IDataDef> ALL_OPDEFS = new StridablesList<>( //
      OPDEF_IS_LABELS_SHOWN, //
      OPDEF_LABEL_LINES, //
      OPDEF_IS_TOOLTIPS_SHOWN, //
      OPDEF_DEFAULT_THUMB_SIZE, //
      OPDEF_SELECTION_BORDER_SETTINGS, //
      OPDEF_IS_ICONS_INSTEAD_OF_THUMBS //
  );

}

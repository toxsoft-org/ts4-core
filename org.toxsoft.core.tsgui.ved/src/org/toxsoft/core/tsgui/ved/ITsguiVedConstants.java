package org.toxsoft.core.tsgui.ved;

import static org.toxsoft.core.tsgui.rcp.valed.IValedFileConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.tool.sfv.gui.ITsResources.*;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.graphics.fonts.impl.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.apprefs.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;

/**
 * Plugin constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ITsguiVedConstants {

  String SKIDE_ID = "org.toxsoft.skide"; //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // E4

  String PERSPID_SFV_TOOL           = "org.toxsoft.too.sfv.persp.main";           //$NON-NLS-1$
  String PARTID_SFV_SECTIONS_LIST   = "org.toxsoft.too.sfv.part.sections_list";   //$NON-NLS-1$
  String PARTID_SFV_SECTION_CONTENT = "org.toxsoft.too.sfv.part.section_content"; //$NON-NLS-1$
  String MMNUID_SFV_TOOL            = "org.toxsoft.too.sfv.mainmenu.sfv_tool";    //$NON-NLS-1$

  String CMDCATEGID_SFV_TOOL = "org.toxsoft.too.sfv.cmdcateg.sfv_tool"; //$NON-NLS-1$
  String CMDID_NEW_FILE      = "org.toxsoft.too.sfv.cmd.new_file";      //$NON-NLS-1$
  String CMDID_OPEN_FILE     = "org.toxsoft.too.sfv.cmd.open_file";     //$NON-NLS-1$
  String CMDID_SAVE_FILE     = "org.toxsoft.too.sfv.cmd.save_file";     //$NON-NLS-1$
  String CMDID_SAVEN_FILE_AS = "org.toxsoft.too.sfv.cmd.save_file_as";  //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Icons

  String PREFIX_OF_ICON_FIELD_NAME = "ICONID_";  //$NON-NLS-1$
  String ICONID_SFV_TOOL           = "sfv-tool"; //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // App prefs

  String PBID_SFV_TOOL = PERSPID_SFV_TOOL;

  IDataDef APPRM_LAST_PATH = DataDef.create3( "LastPath", DT_FILE_OPEN_NAME, //$NON-NLS-1$
      TSID_NAME, STR_N_APPREF_LAST_PATH, //
      TSID_DESCRIPTION, STR_D_APPREF_LAST_PATH, //
      TSID_DEFAULT_VALUE, AV_STR_EMPTY //
  );

  // FIXME IDataDef APPRM_SECTION_FONT = DataDef.create3( "SectionFont", DT_FONT, //$NON-NLS-1$
  IDataDef APPRM_SECTION_FONT = DataDef.create( "SectionFont", VALOBJ, //$NON-NLS-1$
      TSID_NAME, STR_N_APPREF_SECTION_FONT, //
      TSID_DESCRIPTION, STR_D_APPREF_SECTION_FONT, //
      TSID_KEEPER_ID, FontInfo.KEEPER_ID, //
      TSID_DEFAULT_VALUE, FontInfo.AV_FONT_INFO_NULL //
  );

  IStridablesList<IDataDef> ALL_APPRMS = new StridablesList<>( //
      APPRM_LAST_PATH, //
      APPRM_SECTION_FONT //
  );

  /**
   * Constants registration.
   *
   * @param aWinContext {@link IEclipseContext} - windows level context
   */
  static void init( IEclipseContext aWinContext ) {
    ITsIconManager iconManager = aWinContext.get( ITsIconManager.class );
    iconManager.registerStdIconByIds( Activator.PLUGIN_ID, ITsguiVedConstants.class, PREFIX_OF_ICON_FIELD_NAME );
    //
    IAppPreferences appPrefs = aWinContext.get( IAppPreferences.class );
    IPrefBundle pb = appPrefs.defineBundle( PBID_SFV_TOOL, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_N_PB_SFV_TOOL, //
        TSID_DEFAULT_VALUE, STR_D_PB_SFV_TOOL, //
        TSID_ICON_ID, ICONID_SFV_TOOL //
    ) );
    for( IDataDef p : ALL_APPRMS ) {
      pb.defineOption( p );
    }
  }

}

package org.toxsoft.core.tsgui.mws.e4.handlers;

import static org.toxsoft.core.tsgui.mws.IMwsCoreConstants.*;
import static org.toxsoft.core.tsgui.mws.e4.handlers.ITsResources.*;

import javax.inject.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.e4.core.di.annotations.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.mws.*;
import org.toxsoft.core.tsgui.panels.opsedit.group.*;
import org.toxsoft.core.tsgui.panels.opsedit.impl.*;
import org.toxsoft.core.tsgui.panels.opsedit.set.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.apprefs.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * E4 command handler: invoke application preferences edit dialog.
 * <p>
 * Command ID: {@link IMwsCoreConstants#MWSID_CMD_EDIT_APP_PREFS}.<br>
 * Arguments (E4 command parameters):<br>
 * <ul>
 * <li>{@link IMwsCoreConstants#MWSID_CMDARG_EAP_INIT_SECTID} - (optional arg) ID of the section selected initially
 * (must be an IDpath);</li>
 * <li>{@link IMwsCoreConstants#MWSID_CMDARG_EAP_HIDE_SECTS_LIST} - (optional arg) hide left list of sections in dialog.
 * <b>Warning:</b> must be a string retpersentation of <code>boolean</code> value as in
 * {@link Boolean#parseBoolean(String)};</li>
 * <li>{@link IMwsCoreConstants#MWSID_CMDARG_EAP_SECTIDS} - (optional arg) IDs list of sections to show. <b>Warning:</b>
 * must be a string created by {@link IEntityKeeper#ent2str(Object) StringListKeeper.KEEPER.ent2str(IStringList)}</li>
 * </ul>
 * <p>
 * Command with no argumenmts (as it is called from application main menu "File" -"Preferences") displays
 * {@link DialogOptionSetEdit} with sections, corresponding to preference bundles
 * {@link IAppPreferences#listPrefBundleIds()}. Bundles with no known params, that is with an empty list
 * {@link IPrefBundle#listKnownOptions()}, are not shown.
 * <p>
 * Arguments usage aloows developer to use this command as common way to configure parts of the program in similar way.
 * Command with arguments may by run either by menu/toolbar items created in E4 model editor or directly from java code.
 * Static helper methods {@link #invokeCommand1()} and {@link #invokeCommand2()} makes it easy to invoke preferences
 * edit dialog from source code.
 *
 * @author hazard157
 */
public class CmdMwsEditAppPrefs {

  @Execute
  void execute( IAppPreferences aAprefs, IEclipseContext aEclipseContext, //
      @Named( MWSID_CMDARG_EAP_INIT_SECTID ) @Optional String aInitSectId, //
      @Named( MWSID_CMDARG_EAP_HIDE_SECTS_LIST ) @Optional String aStrEntHideSectsList, //
      @Named( MWSID_CMDARG_EAP_SECTIDS ) @Optional String aStrEntSectIds //
  ) {
    String initSectId = aInitSectId;
    boolean hideSectsList = Boolean.parseBoolean( aStrEntHideSectsList );
    IStringList shownSectIds = slFromArg( aStrEntSectIds );
    IStridablesList<ISectionDef> sectDefs = PrepareSectionDefs( aAprefs, shownSectIds );
    Shell shell = aEclipseContext.get( Shell.class );
    if( sectDefs.isEmpty() ) {
      TsDialogUtils.info( shell, MSG_WARN_NO_KNOWN_PREF );
      return;
    }

    ITsGuiContext ctx = new TsGuiContext( aEclipseContext );
    ITsDialogInfo dlgInfo = new TsDialogInfo( ctx, shell, DLG_C_APP_PREFS, DLG_T_APP_PREFS, 0 );
    // DialogOptionSetGroupEdit.edit( dlgInfo, sectDefs, IOptionValueChangeListener.NONE );

    // TODO CmdMwsEditAppPrefs.exec()
    TsDialogUtils.underDevelopment( shell );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private static IStringList slFromArg( String aStrEntSectIds ) {
    if( aStrEntSectIds != null ) {
      try {
        return StringListKeeper.KEEPER.str2ent( aStrEntSectIds );
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex );
      }
    }
    return IStringList.EMPTY;
  }

  private static IStridablesList<ISectionDef> PrepareSectionDefs( IAppPreferences aAprefs, IStringList aShownSectIds ) {
    IStridablesListEdit<ISectionDef> ll = new StridablesList<>();
    for( String pbId : aAprefs.listPrefBundleIds() ) {
      // include only allowed sections (or all sections)
      if( aShownSectIds.isEmpty() || aShownSectIds.hasElem( pbId ) ) {
        IPrefBundle pb = aAprefs.getBundle( pbId );
        // dont show bundles with no known options to display
        if( !pb.listKnownOptions().isEmpty() ) {
          ISectionDef sdef = new SectionDef( pbId, pb.params() );
          ll.add( sdef );
        }
      }
    }
    return ll;
  }

  // ------------------------------------------------------------------------------------
  // static API
  //

  // // TODO ???
  public static IStringMap<IOptionSet> invokeCommand1() {
    // TODO реализовать CmdMwsEditAppPrefs.invokeCommand1()
    throw new TsUnderDevelopmentRtException( "CmdMwsEditAppPrefs.invokeCommand1()" );
  }

  public static void invokeCommand2() {
    // TODO реализовать CmdMwsEditAppPrefs.invokeCommand1()
    throw new TsUnderDevelopmentRtException( "CmdMwsEditAppPrefs.invokeCommand1()" );
  }

}

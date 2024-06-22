package org.toxsoft.core.tsgui.mws.e4.handlers;

import static org.toxsoft.core.tsgui.mws.IMwsCoreConstants.*;
import static org.toxsoft.core.tsgui.mws.e4.handlers.ITsResources.*;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.bricks.ctx.impl.TsGuiContext;
import org.toxsoft.core.tsgui.dialogs.TsDialogUtils;
import org.toxsoft.core.tsgui.dialogs.datarec.ITsDialogInfo;
import org.toxsoft.core.tsgui.dialogs.datarec.TsDialogInfo;
import org.toxsoft.core.tsgui.mws.IMwsCoreConstants;
import org.toxsoft.core.tsgui.mws.services.e4helper.ITsE4Helper;
import org.toxsoft.core.tsgui.panels.opsedit.DialogOptionsEdit;
import org.toxsoft.core.tsgui.panels.opsedit.IOpsetsKitItemDef;
import org.toxsoft.core.tsgui.panels.opsedit.impl.OpsetsKitItemDef;
import org.toxsoft.core.tslib.av.opset.IOptionSet;
import org.toxsoft.core.tslib.bricks.apprefs.IAppPreferences;
import org.toxsoft.core.tslib.bricks.apprefs.IPrefBundle;
import org.toxsoft.core.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.core.tslib.bricks.keeper.std.StringListKeeper;
import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesList;
import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesListEdit;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.StridablesList;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.SingleStringList;
import org.toxsoft.core.tslib.coll.primtypes.impl.StringMap;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.LoggerUtils;

import jakarta.inject.Named;

/**
 * E4 command handler: invoke application preferences edit dialog.
 * <p>
 * Command ID: {@link IMwsCoreConstants#MWSID_CMD_EDIT_APP_PREFS}.<br>
 * Arguments (E4 command parameters):<br>
 * <ul>
 * <li>{@link IMwsCoreConstants#MWSID_CMDARG_EAP_INIT_SECTID} - (optional arg) ID of the section selected initially
 * (must be an IDpath). If this arguement is not set current perspective ID is used;</li>
 * <li>{@link IMwsCoreConstants#MWSID_CMDARG_EAP_SECTIDS} - (optional arg) IDs list of sections to show. <b>Warning:</b>
 * must be a string created by {@link IEntityKeeper#ent2str(Object) StringListKeeper.KEEPER.ent2str(IStringList)}</li>
 * </ul>
 * <p>
 * Command with no argumenmts (as it is called from application main menu "File" -"Preferences") displays
 * {@link DialogOptionsEdit#editKit(ITsDialogInfo, IStridablesList, IStringMap, String)} with sections, corresponding to
 * preference bundles {@link IAppPreferences#listPrefBundleIds()}. Bundles with no known params, that is with an empty
 * list {@link IPrefBundle#listKnownOptions()}, are not shown.
 * <p>
 * Arguments usage allows developer to use this command as common way to configure parts of the program in similar way.
 * Command with arguments may by run either by menu/toolbar items created in E4 model editor or directly from java code.
 * Static helper methods <code>invokeXxx()</code> are provided.
 * <p>
 *
 * @author hazard157
 */
public class CmdMwsEditAppPrefs {

  @Execute
  void execute( IAppPreferences aAprefs, IEclipseContext aEclipseContext, //
      ITsE4Helper aE4Helper, //
      @Named( MWSID_CMDARG_EAP_INIT_SECTID ) @Optional String aInitSectId, //
      @Named( MWSID_CMDARG_EAP_SECTIDS ) @Optional String aStrEntSectIds //
  ) {
    // determine section ID to be used as initially selected in preferences dialog
    String initSectId = aInitSectId;
    if( initSectId == null ) { // assume perspective ID if not set
      initSectId = aE4Helper.currentPerspId();
    }
    // determine sections to shown
    IStringList shownSectIds = slFromArg( aStrEntSectIds );
    IStridablesList<IOpsetsKitItemDef> sectDefs = prepareSectionDefs( aAprefs, shownSectIds );
    Shell shell = aEclipseContext.get( Shell.class );
    if( sectDefs.isEmpty() ) {
      TsDialogUtils.info( shell, MSG_WARN_NO_KNOWN_PREF );
      return;
    }
    // make initial values map
    IStringMapEdit<IOptionSet> initVals = new StringMap<>();
    for( String id : aAprefs.listPrefBundleIds() ) {
      initVals.put( id, aAprefs.getBundle( id ).prefs() );
    }
    // invoke dialog
    ITsGuiContext ctx = new TsGuiContext( aEclipseContext );
    ITsDialogInfo dlgInfo = new TsDialogInfo( ctx, shell, DLG_C_APP_PREFS, DLG_T_APP_PREFS, 0 );
    IStringMap<IOptionSet> vals = DialogOptionsEdit.editKit( dlgInfo, sectDefs, initVals, initSectId );
    // update changed prefs bundles
    if( vals != null ) {
      for( String id : aAprefs.listPrefBundleIds() ) {
        IOptionSet newOpset = vals.findByKey( id );
        // consider only bundlesthat were displayed
        if( newOpset != null ) {
          IPrefBundle pb = aAprefs.getBundle( id );
          IOptionSet oldOpset = pb.prefs();
          if( !newOpset.equals( oldOpset ) ) {
            pb.prefs().setAll( newOpset );
          }
        }
      }
    }
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

  private static IStridablesList<IOpsetsKitItemDef> prepareSectionDefs( IAppPreferences aAprefs,
      IStringList aShownSectIds ) {
    IStridablesListEdit<IOpsetsKitItemDef> ll = new StridablesList<>();
    for( String pbId : aAprefs.listPrefBundleIds() ) {
      // include only allowed sections (or all sections)
      if( aShownSectIds.isEmpty() || aShownSectIds.hasElem( pbId ) ) {
        IPrefBundle pb = aAprefs.getBundle( pbId );
        // dont show bundles with no known options to display
        if( !pb.listKnownOptions().isEmpty() ) {
          OpsetsKitItemDef sdef = OpsetsKitItemDef.create( pbId, pb.nmName(), pb.description(), pb.iconId() );
          sdef.optionDefs().addAll( pb.listKnownOptions() );
          ll.add( sdef );
        }
      }
    }
    return ll;
  }

  // ------------------------------------------------------------------------------------
  // static API
  //

  /**
   * Invokes application preferences editor for with a single section.
   *
   * @param aE4Helper {@link ITsE4Helper} - E4 helper
   * @param aSectionId String - edited section ID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException <code>aSectionId</code> is a blank string
   */
  public static void invokeSingleBundleEditCommand( ITsE4Helper aE4Helper, String aSectionId ) {
    TsNullArgumentRtException.checkNull( aE4Helper );
    TsErrorUtils.checkNonBlank( aSectionId );
    invokeAppPrefsEditCommand( aE4Helper, aSectionId, new SingleStringList( aSectionId ) );
  }

  /**
   * Invokes application preferences editor with all sections listed.
   *
   * @param aE4Helper {@link ITsE4Helper} - E4 helper
   * @param aInitialSecId String - initially selected section ID or <code>null</code>
   * @throws TsNullArgumentRtException <code>aE4Helper</code> = <code>null</code>
   * @throws TsNullArgumentRtException <code>aE4Helper</code> = <code>null</code>
   */
  public static void invokeAppPrefsEditCommand( ITsE4Helper aE4Helper, String aInitialSecId ) {
    invokeAppPrefsEditCommand( aE4Helper, aInitialSecId, null );
  }

  /**
   * Invokes application preferences editor.
   *
   * @param aE4Helper {@link ITsE4Helper} - E4 helper
   * @param aInitialSecId String - initially selected section ID or <code>null</code>
   * @param aShownSectionIds {@link IStringList} - listed section IDs or <code>null</code>
   * @throws TsNullArgumentRtException <code>aE4Helper</code> = <code>null</code>
   */
  public static void invokeAppPrefsEditCommand( ITsE4Helper aE4Helper, String aInitialSecId,
      IStringList aShownSectionIds ) {
    IStringMapEdit<String> args = new StringMap<>();
    if( aInitialSecId != null ) {
      args.put( MWSID_CMDARG_EAP_INIT_SECTID, aInitialSecId );
    }
    if( aShownSectionIds != null ) {
      String sval = StringListKeeper.KEEPER.ent2str( aShownSectionIds );
      args.put( MWSID_CMDARG_EAP_SECTIDS, sval );
    }
    aE4Helper.execCmd( MWSID_CMD_EDIT_APP_PREFS, args );
  }

}

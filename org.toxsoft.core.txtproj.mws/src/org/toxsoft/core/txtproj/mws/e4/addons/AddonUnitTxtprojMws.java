package org.toxsoft.core.txtproj.mws.e4.addons;

import static org.toxsoft.core.txtproj.mws.IUnitTxtprojMwsConstants.*;
import static org.toxsoft.core.txtproj.mws.IUnitTxtprojMwsResources.*;

import java.io.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.mws.appinf.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.mws.osgi.*;
import org.toxsoft.core.tsgui.mws.services.e4helper.*;
import org.toxsoft.core.tsgui.rcp.utils.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.core.tslib.utils.progargs.*;
import org.toxsoft.core.tslib.utils.valobj.*;
import org.toxsoft.core.txtproj.lib.*;
import org.toxsoft.core.txtproj.lib.bound.*;
import org.toxsoft.core.txtproj.lib.impl.*;
import org.toxsoft.core.txtproj.mws.*;

/**
 * Plugin addon.
 *
 * @author hazard157
 */
public class AddonUnitTxtprojMws
    extends MwsAbstractAddon {

  /**
   * Main windows title format string when changes are NOT saved to the file.
   * <p>
   * Format arguemnts are: 1) project file path, 2) {@link ITsApplicationInfo#nmName()}
   */
  private static final String FMT_WIN_TITLE_UNSAVED = "(*) %s - %s"; //$NON-NLS-1$

  /**
   * Main windows title format string when changes are saved to the file.
   * <p>
   * Format arguemnts are: 1) project file path, 2) {@link ITsApplicationInfo#nmName()}
   */
  private static final String FMT_WIN_TITLE_SAVED = "%s - %s"; //$NON-NLS-1$

  /**
   * Constructor.
   */
  public AddonUnitTxtprojMws() {
    super( Activator.PLUGIN_ID );
    TsValobjUtils.registerKeeperIfNone( TsProjectFileFormatInfoKeeper.KEEPER_ID, TsProjectFileFormatInfoKeeper.KEEPER );
  }

  // ------------------------------------------------------------------------------------
  // MwsAbstractAddon
  //

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    // retrieve module settings (earlier set in application exe plugin's activator)
    IMwsOsgiService mws = findOsgiService( IMwsOsgiService.class );
    TsInternalErrorRtException.checkNull( mws );
    IOptionSet params = mws.context().params();
    TsProjectFileFormatInfo formatInfo = OPDEF_PROJECT_FILE_FORMAT_INFO.getValue( params ).asValobj();
    // create ITsProject and ITsProjectFileBound instances and set them to the context
    TsProject proj = new TsProject( formatInfo );
    ITsProjectFileBound bound = new TsProjectFileBound( proj, params );
    aAppContext.set( ITsProjectFileBound.class, bound );
    aAppContext.set( TsProject.class, proj );
    aAppContext.set( ITsProject.class, proj );
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    openProjectIfSpecified( aWinContext );
    // listen to the project file bound state change and update windows title if specified
    IMwsOsgiService mws = findOsgiService( IMwsOsgiService.class );
    TsInternalErrorRtException.checkNull( mws );
    IOptionSet params = mws.context().params();
    if( OPDEF_IS_WINDOWS_TITLE_BOUND.getValue( params ).asBool() ) {
      ITsE4Helper helper = aWinContext.get( ITsE4Helper.class );
      ITsProjectFileBound projHolder = aWinContext.get( ITsProjectFileBound.class );
      projHolder.addTsProjectFileBoundListener( new ITsProjectFileBoundListener() {

        @Override
        public void onFileBindingChanged( ITsProjectFileBound aSource ) {
          updateWindowsTitle( aWinContext );
          helper.updateHandlersCanExecuteState();
        }

        @Override
        public void onAlteredStateChanged( ITsProjectFileBound aSource ) {
          updateWindowsTitle( aWinContext );
          helper.updateHandlersCanExecuteState();
        }

      } );
      updateWindowsTitle( aWinContext );
    }
  }

  @Override
  protected boolean doCanCloseMainWindow( IEclipseContext aWinContext, MWindow aWindow ) {
    IEclipseContext context = aWindow.getContext();
    ITsProjectFileBound projHolder = context.get( ITsProjectFileBound.class );
    Shell shell = context.get( Shell.class );
    return askAndSaveEdits( projHolder, shell );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  /**
   * Load project if specified in command line by calling method {@link #loadProject(ITsProjectFileBound, File)}.
   *
   * @param aWinContext - the context
   */
  void openProjectIfSpecified( IEclipseContext aWinContext ) {
    ProgramArgs pa = aWinContext.get( ProgramArgs.class );
    String dbPath = pa.getArgValue( CMDLINE_ARG_PROJ_PATH );
    ITsProjectFileBound projHolder = aWinContext.get( ITsProjectFileBound.class );
    if( !dbPath.isEmpty() ) {
      // check project file is accessible
      File f = new File( dbPath );
      ValidationResult vr = TsFileUtils.VALIDATOR_FILE_READABLE.validate( f );
      if( !vr.isError() ) {
        IMwsOsgiService mws = aWinContext.get( IMwsOsgiService.class );
        // load immediately
        if( OPDEF_IMMEDIATE_LOAD_PROJ.getValue( mws.context().params() ).asBool() ) {
          loadProject( projHolder, f );
        }
        else {
          // load project file after all plugins will be inited
          Display display = aWinContext.get( Display.class );
          display.asyncExec( () -> loadProject( projHolder, f ) );
        }
      }
      else {
        LoggerUtils.errorLogger().warning( vr.message() );
      }
    }
  }

  /**
   * Performs project loading.
   *
   * @param aProjHolder {@link ITsProjectFileBound} - project file bound manager
   * @param aFile {@link File} - project file
   */
  void loadProject( ITsProjectFileBound aProjHolder, File aFile ) {
    try {
      aProjHolder.open( aFile );
      LoggerUtils.defaultLogger().info( "Loaded project file %s", aProjHolder.getFile().getAbsolutePath() ); //$NON-NLS-1$
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex.getMessage() );
      aProjHolder.project().clear();
    }
  }

  /**
   * Detects if there were changes in project content in memory, asks used and saves it to file.
   * <p>
   * If there were no changes then silently returns <code>true</code>.
   *
   * @param aProjHolder {@link ITsProjectFileBound} - project file bound manager
   * @param aShell {@link Shell} - parent shell for query dialog
   * @return boolean - признак продолжения или отмены запрошенного действия <br>
   *         <b>true</b> - изменений не было, или пользователь сохранил или отменил изменения, выполнение запрошенного
   *         действия следует продолжить;<br>
   *         <b>false</b> - пользователь отаказался от продолжения запрошенного действия.
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public static boolean askAndSaveEdits( ITsProjectFileBound aProjHolder, Shell aShell ) {
    TsNullArgumentRtException.checkNulls( aProjHolder, aShell );
    if( !aProjHolder.isAltered() ) {
      return true; // no changes in prject in-memory content
    }
    // ask user for save
    switch( TsDialogUtils.askYesNoCancel( aShell, MSG_ASK_SAVE_CHANGES_DLG_MESSAGE ) ) {
      case NO:
        return true; // user does not want to save, changes will be lost
      case YES:
        break; // continue with saving
      // $CASES-OMITTED$
      default:
        return false; // user cancels current action
    }
    File saveFile; // file to save the project content
    if( aProjHolder.hasFileBound() ) { // file is already specified (bound to project) in application
      saveFile = aProjHolder.getFile();
    }
    else {
      // file was not bound to project, ask file name
      saveFile = TsRcpDialogUtils.askFileSave( aShell );
      if( saveFile == null ) { // user cancels current action
        return false;
      }
    }
    // try to save the file
    try {
      aProjHolder.saveAs( saveFile );
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
      TsDialogUtils.error( aShell, ex );
      return false; // changes can't be saved, behave like action was cancelled
    }
    return true;
  }

  /**
   * Update main windows title reflecting project file bound and altered state.
   *
   * @param aWinContext {@link IEclipseContext} - windows leve context
   */
  void updateWindowsTitle( IEclipseContext aWinContext ) {
    ITsProjectFileBound projHolder = aWinContext.get( ITsProjectFileBound.class );
    MWindow window = aWinContext.get( MWindow.class );
    IMwsOsgiService mws = findOsgiService( IMwsOsgiService.class );
    File f = projHolder.getFile();
    if( f != null ) {
      String fmtStr = projHolder.isAltered() ? FMT_WIN_TITLE_UNSAVED : FMT_WIN_TITLE_SAVED;
      String title = String.format( fmtStr, f.getPath(), mws.appInfo().nmName() );
      window.setLabel( title );
    }
    else {
      window.setLabel( mws.appInfo().nmName() );
    }
  }

}

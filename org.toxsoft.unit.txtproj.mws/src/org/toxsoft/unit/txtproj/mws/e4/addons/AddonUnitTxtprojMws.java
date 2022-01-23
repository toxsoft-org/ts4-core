package org.toxsoft.unit.txtproj.mws.e4.addons;

import java.io.File;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.toxsoft.tsgui.dialogs.TsDialogUtils;
import org.toxsoft.tsgui.mws.bases.MwsAbstractAddon;
import org.toxsoft.tsgui.mws.osgi.IMwsOsgiService;
import org.toxsoft.tslib.av.opset.IOptionSet;
import org.toxsoft.tslib.bricks.ctx.ITsContext;
import org.toxsoft.tslib.bricks.validator.ValidationResult;
import org.toxsoft.tslib.utils.errors.TsInternalErrorRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.tslib.utils.logs.impl.LoggerUtils;
import org.toxsoft.tslib.utils.progargs.ProgramArgs;
import org.toxsoft.unit.txtproj.core.ITsProject;
import org.toxsoft.unit.txtproj.core.bound.ITsProjectFileBound;
import org.toxsoft.unit.txtproj.core.bound.TsProjectFileBound;
import org.toxsoft.unit.txtproj.core.impl.TsProject;
import org.toxsoft.unit.txtproj.core.impl.TsProjectFileFormatInfo;
import org.toxsoft.unit.txtproj.mws.Activator;

/**
 * Plugin addon.
 *
 * @author hazard157
 */
public class AddonUnitTxtprojMws
    extends MwsAbstractAddon {

  /**
   * Constructor.
   */
  public AddonUnitTxtprojMws() {
    super( Activator.PLUGIN_ID );
  }

  // ------------------------------------------------------------------------------------
  // MwsAbstractAddon
  //

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    // TODO retrieve module settings (earlier set in application exe plugin's activator)
    IMwsOsgiService mws = findOsgiService( IMwsOsgiService.class );
    TsInternalErrorRtException.checkNull( mws );
    ITsContext mwsContext = mws.context();
    TsProjectFileFormatInfo formatInfo = REF_PROJECT_FILE_FORMAT_INFO.getValue( mwsContext, null );
    if( formatInfo == null ) {
      formatInfo = PARAM_PROJECT_FILE_FORMAT_INFO.getValue( mwsContext.params(), DEFAULT_PROJECT_FILE_FORMAT_INFO );
    }
    // create ITsProject and ITsProjectFileBound instances and set them to the context
    TsProject proj = new TsProject( formatInfo );
    // FIXME get bound options from MWS context params
    ITsProjectFileBound projHolder = new TsProjectFileBound( proj, IOptionSet.NULL );
    aAppContext.set( ITsProjectFileBound.class, projHolder );
    aAppContext.set( TsProject.class, proj );
    aAppContext.set( ITsProject.class, proj );
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    openProjectIfSpecified();
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

  void openProjectIfSpecified() {
    ProgramArgs pa = appContext().get( ProgramArgs.class );
    String dbPath = pa.getArgValue( CMDLINE_ARG_PROJ_PATH );
    ITsProjectFileBound projHolder = appContext().get( ITsProjectFileBound.class );
    if( !dbPath.isEmpty() ) {
      File f = new File( dbPath );
      ValidationResult vr = FileUtils.validateFileReadable( f );
      if( !vr.isError() ) {
        boolean immediateProjectLoad = IMMEDIATE_LOAD_PROJ.getValue( mwsContext.params() ).asBool();
        if( immediateProjectLoad ) {
          loadProject( projHolder, f );
        }
        else {
          // загрузим проект после окончания инициализации программы
          Display display = appContext().get( Display.class );
          display.asyncExec( () -> loadProject( projHolder, f ) );
        }
      }
      else {
        LoggerUtils.errorLogger().warning( vr.message() );
      }
    }
  }

  void loadProject( ITsProjectFileBound aProjHolder, File aFile ) {
    try {
      aProjHolder.open( aFile );
      LoggerUtils.defaultLogger().info( FMT_INFO_PROJECT_LOADED, aProjHolder.getFile().getAbsolutePath() );
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex.getMessage() );
      aProjHolder.project().clear();
    }
  }

  /**
   * Если есть изменения в проекте, запрашивает у пользователя и сохраняет его.
   * <p>
   * Если нет несохраненных изменений, то метод возвращает true, ничего не делая.
   *
   * @param aHolder {@link ITsProjectFileBound} - "Держатель" редактируемого проекта
   * @param aShell {@link Shell} - родительское окно для диалога (может быть null)
   * @return boolean - признак продолжения или отмены запрошенного действия <br>
   *         <b>true</b> - изменений не было, или пользователь сохранил или отменил изменения, выполнение запрошенного
   *         действия следует продолжить;<br>
   *         <b>false</b> - пользователь отаказался от продолжения запрошенного действия.
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public static boolean askAndSaveEdits( ITsProjectFileBound aHolder, Shell aShell ) {
    TsNullArgumentRtException.checkNulls( aHolder, aShell );
    if( !aHolder.isAltered() ) {
      return true; // нечего сохранять, продолжаем действие
    }
    // спросить о сохранении
    switch( TsDialogUtils.askYesNoCancel( aShell, MSG_ASK_SAVE_CHANGES_DLG_MESSAGE ) ) {
      case NO:
        return true; // отказ от сохранения, продолжаем игнорируя текущие правки
      case YES:
        break; // продолжим с сохранением
      // $CASES-OMITTED$
      default:
        return false; // отказ от продолжения, прерываем действие
    }
    File saveFile;
    if( aHolder.hasFileBound() ) {
      saveFile = aHolder.getFile();
    }
    else {
      // запросить имя файла
      saveFile = TsRcpDialogUtils.askFileSaveOrNull( aShell );
      if( saveFile == null ) { // если отказ - то прерываем
        return false;
      }
    }
    aHolder.saveAs( saveFile );
    return true;
  }

}

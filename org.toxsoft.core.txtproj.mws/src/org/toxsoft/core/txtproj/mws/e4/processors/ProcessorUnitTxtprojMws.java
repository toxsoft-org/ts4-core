package org.toxsoft.core.txtproj.mws.e4.processors;

import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.core.tsgui.mws.IMwsCoreConstants.*;
import static org.toxsoft.core.txtproj.mws.IUnitTxtprojMwsConstants.*;

import org.eclipse.e4.ui.model.application.commands.*;
import org.eclipse.e4.ui.model.application.ui.menu.*;
import org.eclipse.e4.ui.workbench.modeling.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.txtproj.mws.*;

/**
 * Processor initializes GUI depending on unit config options from {@link IUnitTxtprojMwsConstants}.
 * <p>
 * Unit config option values must be set as MWS context parameters in application EXE plugin's Activator.doStart()
 *
 * @author hazard157
 */
public class ProcessorUnitTxtprojMws
    extends MwsAbstractProcessor {

  /**
   * Icons size of icons in menu.
   * <p>
   * Warning: must match size of icons specified for menu in Application.e4xmi in plugin org.toxsoft.tsgui.mws!
   */
  private static final EIconSize INITIAL_MENU_ICON_SIZE = EIconSize.IS_24X24;

  // ------------------------------------------------------------------------------------
  // MwsAbstractProcessor
  //

  @Override
  protected void doProcess() {
    // MENU: if needed, fill "File" or "Project" menu with project management commands
    if( isShowCmdInMenu() ) {
      // choose which menu to use
      MMenu menu;
      if( isFileMenuAlwaysUsed() ) {
        menu = findElement( mainWindow(), MWSID_MENU_MAIN_FILE, MMenu.class, EModelService.IN_MAIN_MENU );
      }
      else {
        menu = findElement( mainWindow(), MWSID_MENU_MAIN_PROJECT, MMenu.class, EModelService.IN_MAIN_MENU );
      }
      if( menu != null ) {
        fillMenuWithProjectCommands( menu );
      }
    }
    // TOOLBAR: hide or show project commands toolbar
    MToolBar tbProject = findElement( mainWindow(), TOOLBARID_TXTPROJ, MToolBar.class, EModelService.IN_TRIM );
    if( tbProject != null ) {
      /**
       * TODO The line below does not hides toolbar. Is this an eclipse bug?
       */
      tbProject.setVisible( isShowCmdInToolbar() );
      // reduce number of buttons if requested
      if( isReducedToolbarButtons() ) {
        MHandledToolItem btn =
            findElement( mainWindow(), BTNID_PROJECT_NEW, MHandledToolItem.class, EModelService.IN_TRIM );
        if( btn != null ) {
          btn.setVisible( false );
        }
        btn = findElement( mainWindow(), BTNID_PROJECT_SAVE_AS, MHandledToolItem.class, EModelService.IN_TRIM );
        if( btn != null ) {
          btn.setVisible( false );
        }
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void fillMenuWithProjectCommands( MMenu aProjectMenu ) {
    MHandledMenuItem mItem;
    MCommand cmd;
    // project save as
    mItem = modelService().createModelElement( MHandledMenuItem.class );
    cmd = findElement( application(), CMDID_PROJECT_SAVE_AS, MCommand.class, EModelService.ANYWHERE );
    mItem.setCommand( cmd );
    mItem.setIconURI( makeTsguiIconUri( ICONID_DOCUMENT_SAVE_AS, INITIAL_MENU_ICON_SIZE ) );
    aProjectMenu.getChildren().add( 0, mItem );
    // project save
    mItem = modelService().createModelElement( MHandledMenuItem.class );
    cmd = findElement( application(), CMDID_PROJECT_SAVE, MCommand.class, EModelService.ANYWHERE );
    mItem.setCommand( cmd );
    mItem.setIconURI( makeTsguiIconUri( ICONID_DOCUMENT_SAVE, INITIAL_MENU_ICON_SIZE ) );
    aProjectMenu.getChildren().add( 0, mItem );
    // project open
    mItem = modelService().createModelElement( MHandledMenuItem.class );
    cmd = findElement( application(), CMDID_PROJECT_OPEN, MCommand.class, EModelService.ANYWHERE );
    mItem.setCommand( cmd );
    mItem.setIconURI( makeTsguiIconUri( ICONID_DOCUMENT_OPEN, INITIAL_MENU_ICON_SIZE ) );
    aProjectMenu.getChildren().add( 0, mItem );
    // project new
    mItem = modelService().createModelElement( MHandledMenuItem.class );
    cmd = findElement( application(), CMDID_PROJECT_NEW, MCommand.class, EModelService.ANYWHERE );
    mItem.setCommand( cmd );
    mItem.setIconURI( makeTsguiIconUri( ICONID_DOCUMENT_NEW, INITIAL_MENU_ICON_SIZE ) );
    aProjectMenu.getChildren().add( 0, mItem );
  }

  private boolean isShowCmdInMenu() {
    return OPDEF_SHOW_CMD_IN_MENU.getValue( mwsService().context().params() ).asBool();
  }

  private boolean isShowCmdInToolbar() {
    return OPDEF_SHOW_CMD_IN_TOOLBAR.getValue( mwsService().context().params() ).asBool();
  }

  private boolean isFileMenuAlwaysUsed() {
    return OPDEF_ALWAYS_USE_FILE_MENU.getValue( mwsService().context().params() ).asBool();
  }

  private boolean isReducedToolbarButtons() {
    return OPDEF_IS_REDUCED_TOOLBAR_BUTTONS.getValue( mwsService().context().params() ).asBool();
  }

}

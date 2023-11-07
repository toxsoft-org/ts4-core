package org.toxsoft.core.tsgui.ved.screen.asp;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;

import java.io.*;

import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.actions.asp.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.rcp.utils.*;
import org.toxsoft.core.tsgui.ved.editor.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * File export/import operations.
 * <p>
 * Handles the actions:
 * <ul>
 * <li>{@link ITsStdActionDefs#ACTID_FILE_EXPORT};</li>
 * <li>{@link ITsStdActionDefs#ACTID_FILE_IMPORT}.</li>
 * </ul>
 *
 * @author hazard157
 */
public class VedAspFileImpex
    extends MethodPerActionTsActionSetProvider
    implements ITsGuiContextable {

  private final IVedScreen vedScreen;

  private String lastPath = TsLibUtils.EMPTY_STRING;

  /**
   * Constructor.
   *
   * @param aVedScreen {@link IVedScreen} - the screen to handle
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedAspFileImpex( IVedScreen aVedScreen ) {
    vedScreen = TsNullArgumentRtException.checkNull( aVedScreen );
    defineAction( ACDEF_FILE_EXPORT, this::doHandleExport );
    defineAction( ACDEF_FILE_IMPORT, this::doHandleImport );
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return vedScreen.tsContext();
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  void doHandleExport() {
    File f = TsRcpDialogUtils.askFileSave( getShell(), lastPath, SCREEN_CFG_FILE_EXT );
    if( f != null ) {
      IVedScreenCfg screenCfg = VedEditorUtils.getVedScreenConfig( vedScreen );
      VedScreenCfg.KEEPER.write( f, screenCfg );
      lastPath = f.getAbsolutePath();
    }
  }

  void doHandleImport() {
    // TODO warn about changes overwrite
    File f = TsRcpDialogUtils.askFileOpen( getShell(), lastPath, new StringArrayList( SCREEN_CFG_FILE_AST_EXT ) );
    if( f != null ) {
      IVedScreenCfg screenCfg = VedScreenCfg.KEEPER.read( f );
      VedEditorUtils.setVedScreenConfig( vedScreen, screenCfg );
      lastPath = f.getAbsolutePath();
    }
  }

}

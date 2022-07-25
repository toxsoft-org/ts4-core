package org.toxsoft.core.tsgui.ved.std.tools;

import static org.toxsoft.core.tsgui.ved.std.tools.ITsResources.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.bricks.swtevents.*;
import org.toxsoft.core.tsgui.ved.*;
import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tsgui.ved.api.library.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tsgui.ved.impl.*;
import org.toxsoft.core.tsgui.ved.std.library.*;
import org.toxsoft.core.tslib.av.opset.impl.*;

/**
 * Инструмент "Указатель".
 * <p>
 * Является базовым инструментом, который позволяет выделять компонеты, перемещать их и изменять их размеры.
 *
 * @author vs
 */
public class VedStdPointerTool
    extends VedAbstractEditorTool {

  private static final String TOOL_ID = ITsguiVedConstants.VED_ID + ".Pointer"; //$NON-NLS-1$

  /**
   * Tool provider singleton.
   */
  public static final IVedEditorToolProvider PROVIDER = new VedAbstractEditorToolProvider( //
      VedStdLibraryShapes.LIBRARY_ID, TOOL_ID, OptionSetUtils.createOpSet( //
          TSID_NAME, STR_N_ET_POINTER, //
          TSID_DESCRIPTION, STR_D_ET_POINTER //
      ) ) {

    @Override
    protected VedAbstractEditorTool doCreateTool( IVedEnvironment aEnvironment, IVedScreen aScreen ) {
      return new VedStdPointerTool( aEnvironment, aScreen );
    }

  };

  private final VedPointerToolMouseHandler mouseHandler;

  /**
   * Constructor.
   *
   * @param aEnv {@link IVedEnvironment} - the VED framefork environment
   * @param aScreen {@link IVedScreen} - one of the screens in environment
   */
  public VedStdPointerTool( IVedEnvironment aEnv, IVedScreen aScreen ) {
    super( PROVIDER, aEnv, aScreen );
    mouseHandler = new VedPointerToolMouseHandler( aEnv.eclipseContext() );
    aScreen.conversionChangeEventer().addListener( aSource -> {
      mouseHandler.onZoomFactorChanged( vedScreen().getConversion().zoomFactor() );
    } );
  }

  @Override
  public ISwtMouseListener mouseListener() {
    return mouseHandler;
  }

  @Override
  public ISwtKeyListener keyListener() {
    return null;
  }

  @Override
  public IVedScreenDecorator screenDecorator() {
    return null;
  }

  @Override
  public IVedViewDecorator viewDecorator() {
    return null;
  }
}

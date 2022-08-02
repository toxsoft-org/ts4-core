package org.toxsoft.core.tsgui.ved.std.tools;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.std.tools.ITsResources.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.ved.*;
import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tsgui.ved.api.library.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tsgui.ved.impl.*;
import org.toxsoft.core.tsgui.ved.std.library.*;
import org.toxsoft.core.tsgui.ved.utils.drag.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;

/**
 * Инструмент "Указатель".
 * <p>
 * Является базовым инструментом, который позволяет выделять компоненты, перемещать их и изменять их размеры.
 *
 * @author vs
 */
public class VedStdPointerTool
    extends VedAbstractVertexBasedTool {

  private static final String TOOL_ID = ITsguiVedConstants.VED_ID + ".Pointer"; //$NON-NLS-1$

  /**
   * Tool provider singleton.
   */
  public static final IVedEditorToolProvider PROVIDER = new VedAbstractEditorToolProvider( //
      VedStdLibraryShapes.LIBRARY_ID, TOOL_ID, OptionSetUtils.createOpSet( //
          TSID_NAME, STR_N_ET_POINTER, //
          TSID_DESCRIPTION, STR_D_ET_POINTER, //
          TSID_ICON_ID, ICONID_TOOL_POINTER //
      ) ) {

    @Override
    protected VedAbstractEditorTool doCreateTool( IVedEnvironment aEnvironment, IVedScreen aScreen ) {
      return new VedStdPointerTool( aEnvironment, aScreen );
    }

  };

  private final VedPointerToolMouseHandler mouseHandler;

  /**
   * Набор вершин прямоугольника
   */
  private final VedRectVertexSetView vertexSet;

  /**
   * Constructor.
   *
   * @param aEnv {@link IVedEnvironment} - the VED framefork environment
   * @param aScreen {@link IVedScreen} - one of the screens in environment
   */
  public VedStdPointerTool( IVedEnvironment aEnv, IVedScreen aScreen ) {
    super( PROVIDER, aEnv, aScreen );

    vertexSet = new VedRectVertexSetView( tsContext() );
    mouseHandler = new VedPointerToolMouseHandler( this, aEnv, aScreen );

    aScreen.conversionChangeEventer().addListener( aSource -> {
      mouseHandler.onZoomFactorChanged( vedScreen().getConversion().zoomFactor() );
    } );
  }

  @Override
  public VedAbstractToolMouseHandler mouseListener() {
    return mouseHandler;
  }

  @Override
  protected IVedVertexSetView vertexSet() {
    return vertexSet;
  }

  @Override
  protected IStridablesList<IVedComponentView> listComponentViews() {
    return vedScreen().listViews();
  }

  @Override
  protected boolean accept( IVedComponentView aView ) {
    return true; // работаем с любыми компонентами
  }

  // ------------------------------------------------------------------------------------
  //
  //

  @Override
  public IVedScreenDecorator screenDecorator() {
    return null;
  }

  @Override
  public IVedViewDecorator viewDecorator() {
    return this;
  }

  // ------------------------------------------------------------------------------------
  // IVedViewDecorator
  //

  @Override
  public void paintAfter( IVedComponentView aView, GC aGc, ITsRectangle aPaintBounds ) {
    // if( mouseHandler.vertexSet() != null ) {
    // mouseHandler.vertexSet().paintAfter( aView, aGc, aPaintBounds );
    // }
    vertexSet.paintAfter( aView, aGc, aPaintBounds );
  }

}

package org.toxsoft.core.tsgui.ved.extra.tools;

import static java.lang.Math.*;
import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.extra.tools.ITsResources.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.cursors.*;
import org.toxsoft.core.tsgui.ved.*;
import org.toxsoft.core.tsgui.ved.core.*;
import org.toxsoft.core.tsgui.ved.core.view.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.d2.*;
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
    super( TOOL_ID, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_N_ET_POINTER, //
        TSID_DESCRIPTION, STR_D_ET_POINTER, //
        TSID_ICON_ID, ICONID_TOOL_POINTER //
    ), aScreen, aEnv );

    vertexSet = new VedRectVertexSetView( aScreen, tsContext() );
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

  @Override
  protected void onVertexDragged( double aDx, double aDy, IVedVertex aVertex, ETsDragState aState ) {
    IVedComponentView slaveShape = selectedViews().first();
    Rectangle r1 = vertexSet().bounds();

    r1 = new Rectangle( r1.x, r1.y, r1.width, r1.height );
    vertexSet().update( aDx, aDy, aVertex.id() );
    Rectangle r2 = vertexSet().bounds();
    Rectangle rr = substract( r2, r1 );

    ID2Point origin = vedScreen().getConversion().origin();
    ID2Point d2p = vedScreen().coorsConvertor().reversePoint( rr.x + origin.x(), rr.y + origin.y() );
    slaveShape.porter().shiftOn( d2p.x(), d2p.y() );

    double alpha = vedScreen().getConversion().rotation().radians();
    double w = rr.width * cos( -alpha ) - rr.height * sin( -alpha );
    double h = rr.height * cos( -alpha ) + rr.width * sin( -alpha );

    w = rr.width * cos( -alpha );
    h = rr.height * cos( -alpha );

    double zf = vedScreen().getConversion().zoomFactor();
    slaveShape.porter().setSize( slaveShape.outline().bounds().width() + w / zf,
        slaveShape.outline().bounds().height() + h / zf );
  }

  @Override
  protected void doUpdateCursor( IScreenObject aScrObj ) {
    if( aScrObj == null ) {
      vedScreen().paintingManager().setCursor( null );
    }
    else {
      if( aScrObj.kind() == EScreenObjectKind.COMPONENT ) {
        vedScreen().paintingManager().setCursor( cursorManager().getCursor( ECursorType.HAND ) );
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // internal
  //

  private static Rectangle substract( Rectangle aRect1, Rectangle aRect2 ) {
    Rectangle r = new Rectangle( 0, 0, 0, 0 );
    r.x = aRect1.x - aRect2.x;
    r.y = aRect1.y - aRect2.y;
    r.width = aRect1.width - aRect2.width;
    r.height = aRect1.height - aRect2.height;
    return r;
  }

}

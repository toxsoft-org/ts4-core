package org.toxsoft.core.tsgui.ved.impl;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;

/**
 * Базовый класс для создания наборов вершин.
 * <p>
 *
 * @author vs
 */
public abstract class VedAbstractVertexSetView
    implements IVedVertexSetView, IVedViewDecorator {

  private final IStridablesListEdit<IVedVertex> vertexes = new StridablesList<>();

  private boolean disposed = false;

  private boolean visible = false;

  /**
   * Конструктор для наследников.<br>
   */
  protected VedAbstractVertexSetView() {
    setVisible( false );
  }

  // ------------------------------------------------------------------------------------
  // {@link IVedVertexSetView}
  //

  @Override
  public IStridablesList<? extends IVedVertex> listVertexes() {
    return vertexes;
  }

  @Override
  public void addVertex( IVedVertex aVertex ) {
    vertexes.add( aVertex );
  }

  @Override
  public boolean visible() {
    return visible;
  }

  @Override
  public void setVisible( boolean aVisible ) {
    visible = aVisible;
  }

  // ------------------------------------------------------------------------------------
  // IVedDisposable
  //

  @Override
  public boolean isDisposed() {
    return disposed;
  }

  @Override
  public void dispose() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // IVedViewDecorator
  //

  @Override
  public final void paintBefore( IVedComponentView aView, GC aGc, ITsRectangle aPaintBounds ) {
    if( !visible ) {
      return;
    }
    doPaintBefore( aView, aGc, aPaintBounds );
  }

  @Override
  public final void paintAfter( IVedComponentView aView, GC aGc, ITsRectangle aPaintBounds ) {
    if( !visible ) {
      return;
    }
    doPaintAfter( aView, aGc, aPaintBounds );
  }

  // ------------------------------------------------------------------------------------
  // Методы для возможного переопределения в наследниках
  //

  @SuppressWarnings( "unused" )
  protected void doPaintBefore( IVedComponentView aView, GC aGc, ITsRectangle aPaintBounds ) {
    // nop
  }

  @SuppressWarnings( "unused" )
  protected void doPaintAfter( IVedComponentView aView, GC aGc, ITsRectangle aPaintBounds ) {
    // nop
  }

}

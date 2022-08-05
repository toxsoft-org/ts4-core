package org.toxsoft.core.tsgui.ved.extra.tools.impl;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.ved.core.view.*;
import org.toxsoft.core.tslib.bricks.d2.*;
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

  private IStridablesList<IVedComponentView> compViews = IStridablesList.EMPTY;

  ID2Conversion d2Conv = ID2Conversion.NONE;

  private final IVedScreen vedScreen;

  /**
   * Конструктор для наследников.<br>
   *
   * @param aVedScreen IVedScreen - экран редактора
   */
  protected VedAbstractVertexSetView( IVedScreen aVedScreen ) {
    setVisible( false );
    vedScreen = aVedScreen;
    d2Conv = vedScreen.getConversion();
  }

  // ------------------------------------------------------------------------------------
  // {@link IVedVertexSetView}
  //

  @Override
  public IStridablesList<? extends IVedVertex> listVertexes() {
    return vertexes;
  }

  @Override
  public final void init( IStridablesList<IVedComponentView> aCompViews ) {
    compViews = aCompViews;
    doInit();
  }

  @Override
  public final IStridablesList<IVedComponentView> componentViews() {
    return compViews;
  }

  @Override
  public final boolean visible() {
    return visible;
  }

  @Override
  public final void setVisible( boolean aVisible ) {
    visible = aVisible;
  }

  // ------------------------------------------------------------------------------------
  // IDisposable
  //

  @Override
  public final boolean isDisposed() {
    return disposed;
  }

  @Override
  public void dispose() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // ID2Conversionable
  //

  @Override
  public final ID2Conversion getConversion() {
    return d2Conv;
  }

  @Override
  public final void setConversion( ID2Conversion aConversion ) {
    d2Conv = aConversion;
    onConversionChanged();
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
  // to use
  //

  protected void addVertex( IVedVertex aVertex ) {
    vertexes.add( aVertex );
  }

  protected IVedScreen vedScreen() {
    return vedScreen;
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

  protected void doInit() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // to implement
  //

  protected abstract void onConversionChanged();
}

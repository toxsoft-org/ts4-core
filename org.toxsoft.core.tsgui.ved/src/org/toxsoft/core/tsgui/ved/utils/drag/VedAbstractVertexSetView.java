package org.toxsoft.core.tsgui.ved.utils.drag;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;

/**
 * Базовый класс для создания наборов вершин.
 * <p>
 *
 * @author vs
 */
public abstract class VedAbstractVertexSetView
    extends Stridable
    implements IVedVertexSetView, IVedViewDecorator {

  private final IStridablesListEdit<IVedVertex> vertexes = new StridablesList<>();

  private double zoomFactor = 1.0;

  private boolean disposed = false;

  private ID2Conversion d2Conv = ID2Conversion.NONE;

  /**
   * Конструктор для наследников.<br>
   *
   * @param aId String - идентификатор
   * @param aName String - имя
   * @param aDescription String - описание
   */
  protected VedAbstractVertexSetView( String aId, String aName, String aDescription ) {
    super( aId, aName, aDescription );
  }

  // ------------------------------------------------------------------------------------
  // {@link IVedVertexSetView}
  //

  /**
   * Возвращает коэффициент масштабирования.
   *
   * @return double - коэффициент масштабирования
   */
  public final double zoomFactor() {
    return zoomFactor;
  }

  @Override
  public final void setZoomFactor( double aZoomFactor ) {
    if( Double.compare( zoomFactor, aZoomFactor ) != 0 ) {
      zoomFactor = aZoomFactor;
      onZoomFactorChanged();
    }
  }

  @Override
  public IStridablesList<? extends IVedVertex> listVertexes() {
    return vertexes;
  }

  @Override
  public void addVertex( IVedVertex aVertex ) {
    vertexes.add( aVertex );
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
  // ID2Conversionable
  //

  @Override
  public ID2Conversion getConversion() {
    return d2Conv;
  }

  @Override
  public void setConversion( ID2Conversion aConversion ) {
    d2Conv = aConversion;
    zoomFactor = d2Conv.zoomFactor();
    onZoomFactorChanged();
  }

  // ------------------------------------------------------------------------------------
  // IVedViewDecorator
  //

  @Override
  public void paintBefore( IVedComponentView aView, GC aGc, ITsRectangle aPaintBounds ) {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // Методы для обязательного переопределения в наследниках
  //

  protected abstract void onZoomFactorChanged();

}

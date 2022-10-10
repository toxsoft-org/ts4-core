package org.toxsoft.core.tsgui.ved.zver1.extra.tools;

import org.toxsoft.core.tsgui.ved.zver1.core.view.*;
import org.toxsoft.core.tsgui.ved.zver1.incub.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;

/**
 * Базовый класс для создания наборов вершин.
 * <p>
 *
 * @author vs
 */
public abstract class VedAbstractVertexSetView
    implements IVedVertexSetView, IDisposable {

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
  public final void dispose() {
    if( !disposed ) {
      disposed = false;
      doDispose();
    }
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

  protected void doInit() {
    // nop
  }

  protected void doDispose() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // to implement
  //

  protected abstract void onConversionChanged();
}

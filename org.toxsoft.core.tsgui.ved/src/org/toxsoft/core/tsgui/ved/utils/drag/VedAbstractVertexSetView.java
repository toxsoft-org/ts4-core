package org.toxsoft.core.tsgui.ved.utils.drag;

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
    implements IVedVertexSetView {

  private final IStridablesListEdit<IVedVertex> vertexes = new StridablesList<>();

  private double zoomFactor = 1.0;

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

  @Override
  public final double zoomFactor() {
    return zoomFactor;
  }

  @Override
  public void setZoomFactor( double aZoomFactor ) {
    zoomFactor = aZoomFactor;
  }

  @Override
  public IStridablesList<? extends IVedVertex> listVertexes() {
    return vertexes;
  }

  @Override
  public void addVertex( IVedVertex aVertex ) {
    vertexes.add( aVertex );
  }

}

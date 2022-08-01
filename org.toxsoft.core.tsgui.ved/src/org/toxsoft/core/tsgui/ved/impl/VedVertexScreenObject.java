package org.toxsoft.core.tsgui.ved.impl;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.cursors.*;

/**
 * Экранный объект "сутью" которого, является представление (view) компоненты.
 * <p>
 *
 * @author vs
 */
public class VedVertexScreenObject
    extends VedAbstractScreenObject<IVedVertex> {

  private final IVedVertex vertex;

  /**
   * Конструктор.<br>
   *
   * @param aVertex IVedVertex - "активная" вершина
   */
  public VedVertexScreenObject( IVedVertex aVertex ) {
    super( EScreenObjectKind.VERTEX, aVertex );
    vertex = aVertex;
  }

  @Override
  public Rectangle bounds() {
    return vertex.bounds();
  }

  @Override
  public void paint( GC aGc ) {
    vertex.paint( aGc );
  }

  @Override
  public IVedVertex entity() {
    return vertex;
  }

  @Override
  public boolean containsScreenPoint( int aX, int aY ) {
    // ID2Convertor convertor = view.ownerScreen().coorsConvertor();
    // ID2Point d2p = convertor.reversePoint( aX, aY );
    // return containsNormPoint( d2p.x(), d2p.y() );
    return vertex.containsScreenPoint( aX, aY );
  }

  // @Override
  // public boolean containsNormPoint( double aX, double aY ) {
  // return view.outline().contains( aX, aY );
  // }

  @Override
  public ECursorType cursorType() {
    return vertex.cursorType();
  }

}

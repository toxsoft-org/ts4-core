package org.toxsoft.core.tsgui.ved.utils.drag;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.cursors.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;

public class VedComponentViewScreenObject
    extends Stridable
    implements IScreenObject {

  private final IVedComponentView view;

  boolean visible = true;

  /**
   * Конструктор.<br>
   *
   * @param aView IVedComponentView - "представление" компоненты редактора
   */
  public VedComponentViewScreenObject( IVedComponentView aView ) {
    super( aView.id(), aView.nmName(), aView.description() );
    view = aView;
  }

  @Override
  public Rectangle bounds() {
    ITsRectangle tsRect = view.ownerScreen().coorsConvertor().rectBounds( view.outline().bounds() );
    return new Rectangle( tsRect.a().x(), tsRect.a().y(), tsRect.width(), tsRect.height() );
  }

  @Override
  public void paint( GC aGc ) {
    view.painter().paint( aGc, null );
  }

  @Override
  public IVedComponentView entity() {
    return view;
  }

  @Override
  public boolean containsScreenPoint( int aX, int aY ) {
    double zf = view.ownerScreen().getConversion().zoomFactor();
    return containsNormPoint( aX / zf, aY / zf );
  }

  @Override
  public boolean containsNormPoint( double aX, double aY ) {
    return view.outline().contains( aX, aY );
  }

  @Override
  public ECursorType cursorType() {
    return ECursorType.HAND;
  }

  @Override
  public boolean visible() {
    return visible;
  }

  @Override
  public void setVisible( boolean aVisible ) {
    visible = aVisible;
  }

  @Override
  public void setZoomFactor( double aZoomFactor ) {
    // TODO Auto-generated method stub
  }
}

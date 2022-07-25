package org.toxsoft.core.tsgui.ved.olds.drag;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.cursors.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tsgui.ved.impl.*;
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
    return VedScreen.boundsToScreen( view );
  }

  @Override
  public void paint( GC aGc ) {
    view.painter().paint( aGc );
  }

  @Override
  public IVedComponentView entity() {
    return view;
  }

  @Override
  public boolean containsScreenPoint( int aX, int aY ) {
    return containsNormPoint( aX / view.painter().zoomFactor(), aY / view.painter().zoomFactor() );
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

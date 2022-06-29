package org.toxsoft.core.tsgui.ved.utils.drag;

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
  public boolean contains( double aX, double aY ) {
    return view.outline().contains( aX, aY );
  }

  @Override
  public ECursorType cursorType() {
    return ECursorType.ARROW;
  }

  @Override
  public boolean visible() {
    return visible;
  }

  @Override
  public void setVisible( boolean aVisible ) {
    visible = aVisible;
  }

}

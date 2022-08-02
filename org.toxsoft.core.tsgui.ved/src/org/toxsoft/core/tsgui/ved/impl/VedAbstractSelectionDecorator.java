package org.toxsoft.core.tsgui.ved.impl;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tsgui.ved.utils.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;

/**
 * Базовый класс для отрисовщиков выделения компонент.
 * <p>
 *
 * @author vs
 */
public abstract class VedAbstractSelectionDecorator
    implements IVedScreenSelectionDecorator, IVedContextable {

  ID2Conversion d2Conv = ID2Conversion.NONE;

  boolean disposed = false;

  private final IVedEnvironment vedEnv;

  private final IVedScreen vedScreen;

  private final IStringListEdit hiddenIds = new StringArrayList();

  /**
   * Конструктор.<br>
   *
   * @param aScreen IVedScreen - экран редактора
   * @param aEnv IVedEnvironment - окружение редактора
   */
  protected VedAbstractSelectionDecorator( IVedScreen aScreen, IVedEnvironment aEnv ) {
    vedScreen = aScreen;
    vedEnv = aEnv;
  }

  // ------------------------------------------------------------------------------------
  // IVedScreenSelectionDecorator
  //

  @Override
  public void hideSelection( String aViewId ) {
    hiddenIds.add( aViewId );
  }

  @Override
  public void showSelection( String aViewId ) {
    hiddenIds.remove( aViewId );
  }

  @Override
  public void showAll() {
    hiddenIds.clear();
  }

  // ------------------------------------------------------------------------------------
  // IVedViewDecorator
  //

  @Override
  public void paintAfter( IVedComponentView aView, GC aGc, ITsRectangle aPaintBounds ) {
    for( IVedComponentView view : vedScreen().selectionManager().selectedComponentViews() ) {
      if( view.id().equals( aView.id() ) && !isHidden( aView.id() ) ) {
        paintSelection( aView, aGc, aPaintBounds );
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // IVedContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return vedEnv.tsContext();
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
    if( !disposed ) {
      disposed = true;
      doDispose();
    }
  }

  // ------------------------------------------------------------------------------------
  // to implement
  //

  protected abstract void paintSelection( IVedComponentView aView, GC aGc, ITsRectangle aPaintBounds );

  // ------------------------------------------------------------------------------------
  // to use
  //

  IVedScreen vedScreen() {
    return vedScreen;
  }

  boolean isHidden( String aViewId ) {
    return hiddenIds.hasElem( aViewId );
  }

  // ------------------------------------------------------------------------------------
  // to override
  //

  protected void doDispose() {
    // nop
  }

}

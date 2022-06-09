package org.toxsoft.core.tsgui.widgets.pdw;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.bricks.stdevents.impl.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.utils.anim.*;
import org.toxsoft.core.tsgui.utils.rectfit.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.utils.errors.*;

// TRANSLATE

/**
 * {@link IPdwWidget} simple implementation without scrollbars, toolbar, etc.
 *
 * @author hazard157
 */
public class PdwWidgetSimple
    implements IPdwWidget {

  private final IGeneralAnimationCallback<Object> animationCallback = new IGeneralAnimationCallback<>() {

    @Override
    public boolean onNextStep( IGeneralAnimator<Object> aAnimator, long aCounter, Object aUserData ) {
      if( tsImage == null || imageWidget.getControl().isDisposed() || tsImage.isDisposed() || !tsImage.isAnimated() ) {
        return true;
      }
      int index = (int)aCounter;
      boolean retVal = false;
      if( index >= tsImage.count() || index < 0 ) {
        index = 0;
        retVal = true;
      }
      imageWidget.setImage( tsImage.frames().get( index ) );
      imageWidget.redraw();
      return retVal;
    }
  };

  private final TsMouseEventProducerHelper mouseEventHelper;

  final ITsGuiContext     tsContext;
  final ImageWidget       imageWidget;
  final IAnimationSupport animationSupport;

  IGeneralAnimator<Object> animator = null;
  TsImage                  tsImage  = null;

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public PdwWidgetSimple( ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    tsContext = aContext;
    animationSupport = tsContext.get( IAnimationSupport.class );
    imageWidget = new ImageWidget();
    mouseEventHelper = new TsMouseEventProducerHelper( this );
  }

  // ------------------------------------------------------------------------------------
  // ILazyControl
  //

  @Override
  public Control createControl( Composite aParent ) {
    Control c = imageWidget.createControl( aParent );
    mouseEventHelper.bindToControl( c );
    return c;
  }

  @Override
  public Control getControl() {
    return imageWidget.getControl();
  }

  // ------------------------------------------------------------------------------------
  // ITsContextable
  //

  @Override
  public ITsContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // IPausableAnimation
  //

  @Override
  public boolean isPaused() {
    if( animator != null ) {
      return animator.isPaused();
    }
    return true;
  }

  @Override
  public void pause() {
    if( animator != null ) {
      animator.pause();
    }
  }

  @Override
  public void resume() {
    if( animator != null ) {
      animator.pause();
    }
  }

  // ------------------------------------------------------------------------------------
  // IPdwWidget
  //

  @Override
  public TsImage getTsImage() {
    return tsImage;
  }

  @Override
  public void setTsImage( TsImage aImage ) {
    if( tsImage == aImage ) {
      return;
    }
    if( animator != null ) {
      animator.pause();
      animationSupport.unregister( animator );
      animator = null;
    }
    tsImage = aImage;
    if( tsImage == null || tsImage.isDisposed() ) {
      imageWidget.setImage( null );
      return;
    }
    if( tsImage.isAnimated() ) {
      animator = animationSupport.registerGeneral( tsImage.delay(), animationCallback, null );
      animator.resume();
    }
    imageWidget.setImage( aImage.image() );
  }

  @Override
  public ITsPoint getImageSize() {
    if( tsImage != null ) {
      return imageWidget.getImageSize();
    }
    return ITsPoint.ZERO;
  }

  @Override
  public ETsFulcrum getFulcrum() {
    return imageWidget.getFulcrum();
  }

  @Override
  public void setFulcrum( ETsFulcrum aFulcrum ) {
    imageWidget.setFulcrum( aFulcrum );
  }

  @Override
  public RectFitInfo getFitInfo() {
    return imageWidget.getFitInfo();
  }

  @Override
  public void setFitInfo( RectFitInfo aInfo ) {
    imageWidget.setFitInfo( aInfo );
  }

  @Override
  public ITsPoint getAreaPreferredSize() {
    return imageWidget.getDefaultSize();
  }

  @Override
  public void setAreaPreferredSize( ITsPoint aSize ) {
    imageWidget.setDefaultSize( aSize );
  }

  @Override
  public boolean isPreferredSizeFixed() {
    return imageWidget.isPreferredSizeFixed();
  }

  @Override
  public void setPreferredSizeFixed( boolean aFixed ) {
    imageWidget.setPreferredSizeFixed( aFixed );
  }

  @Override
  public void redraw() {
    imageWidget.redraw();
  }

  // ------------------------------------------------------------------------------------
  // ITsMouseEventProducer
  //

  @Override
  public void addTsMouseListener( ITsMouseListener aListener ) {
    mouseEventHelper.addTsMouseListener( aListener );
  }

  @Override
  public void removeTsMouseListener( ITsMouseListener aListener ) {
    mouseEventHelper.removeTsMouseListener( aListener );
  }

}

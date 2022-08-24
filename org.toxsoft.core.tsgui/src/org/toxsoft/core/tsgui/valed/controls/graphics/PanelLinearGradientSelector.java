package org.toxsoft.core.tsgui.valed.controls.graphics;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.cursors.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.utils.errors.*;

public class PanelLinearGradientSelector
    extends TsPanel {

  RgbaSelector startRgbaSelector;

  RgbaSelector endRgbaSelector;

  ResultPanel resultPanel;

  static class ThumbVertex {

    Color fgColor;
    Color bgColor;

    Rectangle rect = new Rectangle( 0, 0, 8, 8 );

    int x = 0;
    int y = 0;

    ThumbVertex( Color aFgColor, Color aBgColor ) {
      fgColor = aFgColor;
      bgColor = aBgColor;
    }

    void paint( GC aGc, Control aParent ) {
      Point size = aParent.getSize();
      rect.x = (int)(size.x * x / 100.);
      if( rect.x >= size.x ) {
        rect.x -= 8;
      }
      rect.y = (int)(size.y * y / 100.);
      if( rect.y >= size.y ) {
        rect.y -= 8;
      }

      aGc.setBackground( bgColor );
      aGc.fillRectangle( rect.x, rect.y, 8, 8 );
      aGc.setForeground( fgColor );
      aGc.drawRectangle( rect.x, rect.y, 8, 8 );
    }

    boolean contains( int aX, int aY ) {
      return rect.contains( aX, aY );
    }

    void update( int aX, int aY ) {
      x = aX;
      y = aY;
    }
  }

  ThumbVertex startVertex;
  ThumbVertex endVertex;

  class ResultPanel
      extends TsPanel {

    double x1 = 0;
    double y1 = 50;
    double x2 = 100;
    double y2 = 50;

    RGBA startRgba = null;
    RGBA endRgba   = null;

    IGradient pattern = null;

    PaintListener paintListener = aE -> {

      Rectangle r = clientRect();

      int x = 0;
      int y = 0;
      int colorIdx = 0;
      Color[] c = { colorManager().getColor( ETsColor.GRAY ), colorManager().getColor( ETsColor.DARK_GRAY ) };
      while( x <= r.width ) {
        while( y <= r.height ) {
          aE.gc.setBackground( c[colorIdx] );
          aE.gc.fillRectangle( r.x + x, r.y + y, 16, 16 );
          colorIdx = (colorIdx + 1) % 2;
          y += 16;
        }
        y = 0;
        x += 16;
        colorIdx = (colorIdx + (r.height / 16) % 2) % 2;
      }

      pattern = gradientInfo().createGradient( gradientInfo(), tsContext );
      if( pattern != null ) {
        Pattern p = pattern.pattern( aE.gc, r.width, r.height );
        aE.gc.setBackgroundPattern( p );
        aE.gc.fillRectangle( getClientArea() );
        p.dispose();
      }

      aE.gc.setBackground( colorManager().getColor( ETsColor.WHITE ) );
      aE.gc.fillRectangle( 0, 0, getSize().x, 8 );
      aE.gc.fillRectangle( 0, getSize().y - 8, getSize().x, getSize().y - 8 );
      aE.gc.fillRectangle( 0, 0, 8, getSize().y );
      aE.gc.fillRectangle( getSize().x - 8, 0, 8, getSize().y );

      startVertex.paint( aE.gc, this );
      endVertex.paint( aE.gc, this );

      // aE.gc.setBackground( colorManager().getColor( ETsColor.RED ) );
      // int thumbX = (int)(getSize().x * x1 / 100.);
      // int thumbY = (int)(getSize().y * y1 / 100.);
      // aE.gc.fillRectangle( thumbX, thumbY - 8, 8, 8 );
      // thumbX = (int)(getSize().x * x2 / 100.);
      // thumbY = (int)(getSize().y * y2 / 100.);
      // aE.gc.fillRectangle( thumbX - 8, thumbY - 8, 8, 8 );
    };

    ITsMouseInputListener mouseListener = new ITsMouseInputListener() {

      @Override
      public boolean onMouseMove( Object aSource, int aState, ITsPoint aCoors, Control aWidget ) {
        if( startVertex.contains( aCoors.x(), aCoors.y() ) ) {
          setCursor( handCursor );
          return false;
        }
        if( endVertex.contains( aCoors.x(), aCoors.y() ) ) {
          setCursor( handCursor );
          return false;
        }
        setCursor( null );
        return false;
      }

      @Override
      public boolean onMouseDragStart( Object aSource, DragOperationInfo aDragInfo ) {
        Object obj = objectAt( aDragInfo.startingPoint().x(), aDragInfo.startingPoint().y() );
        aDragInfo.setCargo( obj );
        return obj != null;
      }

      @Override
      public boolean onMouseDragMove( Object aSource, DragOperationInfo aDragInfo, int aState, ITsPoint aCoors ) {
        Object obj = aDragInfo.cargo();
        if( obj == null ) {
          return false;
        }
        Point size = getSize();
        int x = (int)(aCoors.x() * 100. / size.x);
        int y = (int)(aCoors.y() * 100. / size.y);
        if( obj == startVertex ) {
          x1 = x;
          y1 = y;
          startVertex.update( x, y );
        }
        if( obj == endVertex ) {
          x2 = x;
          y2 = y;
          endVertex.update( x, y );
        }
        redraw();
        update();

        return true;
      }

    };

    public ResultPanel( Composite aParent, ITsGuiContext aContext ) {
      super( aParent, aContext, SWT.DOUBLE_BUFFERED );
      addPaintListener( paintListener );
      Color colorBlack = colorManager().getColor( ETsColor.BLACK );
      Color colorRed = colorManager().getColor( ETsColor.RED );
      startVertex = new ThumbVertex( colorBlack, colorRed );
      startVertex.update( (int)x1, (int)y1 );
      endVertex = new ThumbVertex( colorBlack, colorRed );
      endVertex.update( (int)x2, (int)y2 );
      TsUserInputEventsBinder binder = new TsUserInputEventsBinder( this );
      binder.bindToControl( this, TsUserInputEventsBinder.BIND_ALL_MOUSE_EVENTS );
      binder.addTsMouseInputListener( mouseListener );
    }

    void setGradientInfo( LinearGradientInfo aInfo ) {
      startVertex.update( (int)aInfo.startPoint().x(), (int)aInfo.startPoint().y() );
      endVertex.update( (int)aInfo.endPoint().x(), (int)aInfo.endPoint().y() );
    }

    Rectangle clientRect() {
      Point p = getSize();
      return new Rectangle( 8, 8, p.x - 8, p.y - 8 );
    }

    void setColors( RGBA aStartRgba, RGBA aEndRgba ) {
      startRgba = aStartRgba;
      endRgba = aEndRgba;

      LinearGradientInfo gradientInfo = gradientInfo();
      pattern = gradientInfo.createGradient( gradientInfo, tsContext() );
      redraw();
    }

    Object objectAt( int aX, int aY ) {
      if( startVertex.contains( aX, aY ) ) {
        return startVertex;
      }
      if( endVertex.contains( aX, aY ) ) {
        return endVertex;
      }
      return null;
    }

    LinearGradientInfo gradientInfo() {
      D2Point p1 = new D2Point( x1, y1 );
      D2Point p2 = new D2Point( x2, y2 );
      return new LinearGradientInfo( p1, p2, startRgba, endRgba );
    }

  }

  IGenericChangeListener changeListener = aSource -> {
    resultPanel.setColors( startRgbaSelector.rgba(), endRgbaSelector.rgba() );
    resultPanel.redraw();
  };

  private final ITsGuiContext tsContext;

  Cursor handCursor;

  PanelLinearGradientSelector( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    tsContext = aContext;
    handCursor = cursorManager().getCursor( ECursorType.HAND );

    setLayout( new GridLayout( 2, false ) );

    startRgbaSelector = new RgbaSelector( this, SWT.NONE, aContext.eclipseContext() );
    startRgbaSelector.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );

    resultPanel = new ResultPanel( this, aContext );
    resultPanel.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true, 1, 2 ) );

    endRgbaSelector = new RgbaSelector( this, SWT.NONE, aContext.eclipseContext() );
    endRgbaSelector.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );

    startRgbaSelector.setRgba( new RGBA( 255, 255, 255, 255 ) );
    endRgbaSelector.setRgba( new RGBA( 0, 0, 0, 255 ) );

    resultPanel.setColors( startRgbaSelector.rgba(), endRgbaSelector.rgba() );

    startRgbaSelector.genericChangeEventer().addListener( changeListener );
    endRgbaSelector.genericChangeEventer().addListener( changeListener );
  }

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает параметры заливки.<br>
   *
   * @return IGradientInfo - параметры заливки
   */
  public IGradientInfo patternInfo() {
    return resultPanel.gradientInfo();
  }

  public void setPatternInfo( IGradientInfo aInfo ) {
    TsIllegalArgumentRtException.checkFalse( aInfo.gradientType() == EGradientType.LINEAR );
    LinearGradientInfo info = (LinearGradientInfo)aInfo;
    resultPanel.setGradientInfo( info );
    startRgbaSelector.setRgba( info.startRGBA() );
    endRgbaSelector.setRgba( info.endRGBA() );
  }

}

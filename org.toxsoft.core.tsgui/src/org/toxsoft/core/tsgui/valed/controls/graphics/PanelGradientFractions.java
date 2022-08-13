package org.toxsoft.core.tsgui.valed.controls.graphics;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.cursors.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;

class PanelGradientFractions
    extends TsPanel
    implements IGenericChangeEventCapable {

  private final Color colorWhite;
  private final Color colorBlack;
  private final Color colorRed;
  private final Color colorBlue;
  private final Color colorGray;

  static class ThumbVertex {

    Color fgColor;
    Color bgColor;

    Rectangle rect = new Rectangle( 0, 0, 8, 8 );

    double value = 0;

    RGBA rgba;

    ThumbVertex( RGBA aRgba, double aValue, Color aFgColor, Color aBgColor ) {
      rgba = aRgba;
      value = aValue;
      fgColor = aFgColor;
      bgColor = aBgColor;
    }

    void paint( GC aGc, int aY, int aLength ) {
      update( aLength, aY );
      aGc.setBackground( bgColor );
      aGc.fillRectangle( rect.x, aY, 8, 8 );
      aGc.setForeground( fgColor );
      aGc.drawRectangle( rect.x, aY, 8, 8 );
    }

    void setBgColor( Color aBgColor ) {
      bgColor = aBgColor;
    }

    boolean contains( int aX, int aY ) {
      return rect.contains( aX, aY );
    }

    void setValue( double aValue ) {
      value = aValue;
    }

    void update( int aLength, int aY ) {
      rect.x = (int)((aLength * value) / 100.);
      rect.y = aY;
    }

  }

  ThumbVertex selectedVertex = null;

  IListEdit<ThumbVertex> thumbsList = new ElemArrayList<>();

  ITsKeyInputListener keyListener = new ITsKeyInputListener() {

    @Override
    public boolean onKeyDown( Object aSource, int aCode, char aChar, int aState ) {
      if( aCode == SWT.DEL ) {
        if( selectedVertex != startVertex && selectedVertex != endVertex ) {
          thumbsList.remove( selectedVertex );
        }
      }
      onSelectionchanged( null );
      return false;
    }
  };

  ITsMouseInputListener mouseListener = new ITsMouseInputListener() {

    @Override
    public boolean onMouseClick( Object aSource, ETsMouseButton aButton, int aState, ITsPoint aCoors,
        Control aWidget ) {
      setFocus();
      ThumbVertex vertex = objectAt( aCoors.x(), aCoors.y() );
      onSelectionchanged( vertex );
      return true;
    }

    @Override
    public boolean onMouseDoubleClick( Object aSource, ETsMouseButton aButton, int aState, ITsPoint aCoors,
        Control aWidget ) {
      setFocus();
      ThumbVertex hoveredVertex = objectAt( aCoors.x(), aCoors.x() );
      if( hoveredVertex == null ) {
        double value = aCoors.x() * 100. / aWidget.getSize().x;
        ThumbVertex v = new ThumbVertex( new RGBA( 0, 0, 0, 255 ), value, colorBlue, colorBlack );
        int idx = 0;
        for( ThumbVertex vert : thumbsList ) {
          if( vert.value > value ) {
            thumbsList.insert( idx, v );
            break;
          }
          idx++;
          onSelectionchanged( v );
        }
      }
      return true;
    }

    @Override
    public boolean onMouseMove( Object aSource, int aState, ITsPoint aCoors, Control aWidget ) {
      ThumbVertex vertex = objectAt( aCoors.x(), aCoors.y() );
      if( vertex != null ) {
        setCursor( handCursor );
        return false;
      }
      setCursor( null );
      return false;
    }

    @Override
    public boolean onMouseDragStart( Object aSource, DragOperationInfo aDragInfo ) {
      ThumbVertex vertex = objectAt( aDragInfo.startingPoint().x(), aDragInfo.startingPoint().y() );
      aDragInfo.setCargo( vertex );
      return vertex != null;
    }

    @Override
    public boolean onMouseDragMove( Object aSource, DragOperationInfo aDragInfo, int aState, ITsPoint aCoors ) {
      Object obj = aDragInfo.cargo();
      if( obj == null ) {
        return false;
      }
      double value = aCoors.x() * 100. / aDragInfo.starterControl().getSize().x;
      ThumbVertex v = (ThumbVertex)obj;
      v.setValue( value );
      redraw();
      update();
      eventer.fireChangeEvent();
      return true;
    }

  };

  Cursor handCursor;

  ThumbVertex startVertex;
  ThumbVertex endVertex;

  GenericChangeEventer eventer;

  PanelGradientFractions( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );

    eventer = new GenericChangeEventer( this );

    colorWhite = colorManager().getColor( ETsColor.WHITE );
    colorBlack = colorManager().getColor( ETsColor.BLACK );
    colorRed = colorManager().getColor( ETsColor.RED );
    colorBlue = colorManager().getColor( ETsColor.BLUE );
    colorGray = colorManager().getColor( ETsColor.DARK_GRAY );

    handCursor = cursorManager().getCursor( ECursorType.HAND );
    TsUserInputEventsBinder binder = new TsUserInputEventsBinder( this );
    binder.bindToControl( this, TsUserInputEventsBinder.BIND_ALL_INPUT_EVENTS );
    binder.addTsMouseInputListener( mouseListener );
    binder.addTsKeyInputListener( keyListener );

    startVertex = new ThumbVertex( new RGBA( 255, 255, 255, 255 ), 0, colorBlack, colorBlue );
    thumbsList.add( startVertex );
    endVertex = new ThumbVertex( new RGBA( 0, 0, 0, 255 ), 100, colorBlack, colorBlue );
    thumbsList.add( endVertex );

    setBackground( colorWhite );

    addPaintListener( aE -> {
      int y = getSize().y / 2;
      int width = getSize().x;

      aE.gc.setForeground( colorBlack );
      aE.gc.setLineWidth( 2 );
      aE.gc.drawLine( 0, y, width, y );

      for( ThumbVertex v : thumbsList ) {
        v.paint( aE.gc, y - 4, width - 8 );
      }

    } );

    setSize( SWT.DEFAULT, 24 );
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // to use
  //

  RGBA selectedRgba() {
    if( selectedVertex != null ) {
      return selectedVertex.rgba;
    }
    return null;
  }

  void setRGBA( RGBA aRgba ) {
    if( selectedVertex != null ) {
      selectedVertex.rgba = aRgba;
    }
  }

  IList<Pair<Double, RGBA>> fractions() {
    IListEdit<Pair<Double, RGBA>> result = new ElemArrayList<>();
    for( ThumbVertex v : thumbsList ) {
      Pair<Double, RGBA> p = new Pair<>( Double.valueOf( v.value ), v.rgba );
      result.add( p );
    }
    return result;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  ThumbVertex objectAt( int aX, int aY ) {
    for( ThumbVertex vertex : thumbsList ) {
      if( vertex.contains( aX, aY ) ) {
        return vertex;
      }
    }
    return null;
  }

  void onSelectionchanged( ThumbVertex aVertex ) {
    for( ThumbVertex v : thumbsList ) {
      v.setBgColor( colorBlue );
    }
    thumbsList.first().setBgColor( colorGray );
    thumbsList.last().setBgColor( colorGray );
    selectedVertex = null;
    if( aVertex != null ) {
      aVertex.setBgColor( colorRed );
      selectedVertex = aVertex;
      eventer.fireChangeEvent();
    }
    redraw();
    update();
  }

}

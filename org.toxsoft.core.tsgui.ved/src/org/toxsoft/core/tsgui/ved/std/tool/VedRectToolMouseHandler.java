package org.toxsoft.core.tsgui.ved.std.tool;

import static org.toxsoft.core.tsgui.ved.std.IVedStdProperties.*;

import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tsgui.ved.api.library.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tsgui.ved.utils.drag.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;

/**
 * Обработчик мыши для инструмента работы с прямоугольниками.
 * <p>
 *
 * @author vs
 */
public class VedRectToolMouseHandler
    extends VedAbstractToolMouseHandler {

  IVedComponentProvider compProvider;
  IVedComponent         comp     = null;
  IVedComponentView     compView = null;

  IGenericChangeListener changeListener = aSource -> {
    Rectangle r = ((VedCreateCompDragExecutor)aSource).currRect();
    System.out.println( "Rectangle r = " + r );
    double zf = canvas.zoomFactor();
    if( this.comp == null ) {
      if( r.width > 0 && r.height > 0 ) {
        String id = "rect" + System.currentTimeMillis(); //$NON-NLS-1$

        IOptionSet props = OptionSetUtils.createOpSet( //
            PDEF_X, Double.valueOf( r.x / zf ), //
            PDEF_Y, Double.valueOf( r.y / zf ), //
            PDEF_WIDTH, Double.valueOf( r.width / zf ), //
            PDEF_HEIGHT, Double.valueOf( r.height / zf ), //
            PDEF_FG_COLOR, ETsColor.RED.rgb(), //
            PDEF_FG_COLOR, ETsColor.RED.rgb() //
        );

        comp = compProvider.createComponent( id, canvas.vedEnv(), props, new OptionSet() );
        compView = comp.createView( canvas );
        canvas.dataModel().addComponent( comp );
        canvas.redraw();
      }
    }
    else {
      if( r == null ) { // перетаскивание завершено
        comp = null;
        compView = null;
      }
      else {
        compView.porter().setBounds( r.x / zf, r.y / zf, r.width / zf, r.height / zf );
        canvas.redraw();
      }
    }
  };

  VedCreateCompDragExecutor createCompExector = null;

  VedRectToolMouseHandler( IVedComponentProvider aCompProvider ) {
    compProvider = aCompProvider;
  }

  @Override
  protected IVedDragExecutor dragExecutor( IScreenObject aHoveredObject ) {
    System.out.println( "Drag executor !!!!!" );
    if( aHoveredObject == null ) { // drag на пустом месте
      if( createCompExector == null ) {
        createCompExector = new VedCreateCompDragExecutor();
        createCompExector.genericChangeEventer().addListener( changeListener );
      }
      return createCompExector;
    }
    return IVedDragExecutor.NULL;
  }

  @Override
  protected IStridablesList<IScreenObject> objectsForDrag( IScreenObject aHoveredObject, MouseEvent aEvent ) {
    return IStridablesList.EMPTY;
  }

  @Override
  protected void doDispose() {
    // if( createCompExector != null ) {
    // createCompExector.genericChangeEventer().removeListener( changeListener );
    // }
  }

}

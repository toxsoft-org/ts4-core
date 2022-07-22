package org.toxsoft.core.tsgui.ved.std.comps;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.std.IVedStdProperties.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tsgui.ved.impl.*;
import org.toxsoft.core.tsgui.ved.incub.geom.*;
import org.toxsoft.core.tsgui.ved.std.library.*;
import org.toxsoft.core.tsgui.ved.utils.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * VED staandard component: filled rectangle.
 *
 * @author hazard157, vs
 */
public class VedStdCompRectangle
    extends VedAbstractComponent {

  /**
   * Component kind ID.
   */
  public static final String KIND_ID = "rectangle"; //$NON-NLS-1$

  public static final VedAbstractComponentProvider PROVIDER =
      new VedAbstractComponentProvider( VedStdLibraryShapes.LIBRARY_ID, KIND_ID, OptionSetUtils.createOpSet( //
          TSID_NAME, "Rectangle", //
          TSID_DESCRIPTION, "Filled rectangle", //
          TSID_ICON_ID, ICONID_RECTANGLE_SHAPE //
      ), //
          PDEF_X, //
          PDEF_Y, //
          PDEF_WIDTH, //
          PDEF_HEIGHT, //
          PDEF_FG_COLOR, //
          PDEF_BG_COLOR, //
          PDEF_ROTATION_ANGLE //
      ) {

        @Override
        protected IVedComponent doCreateComponent( String aCompId, IVedEnvironment aEnvironment, IOptionSet aProps,
            IOptionSet aExtdata ) {
          IVedComponent c = new VedStdCompRectangle( aCompId, aEnvironment );
          c.props().setProps( aProps );
          c.extdata().setAll( aExtdata );
          return c;
        }
      };

  static class StdRectView
      extends VedAbstractComponentView
      implements IVedPainter, IVedPorter {

    Color fgColor;
    Color bgColor;

    private final Rectangle visRect = new Rectangle( 0, 0, 0, 0 );

    private double zoomFactor = 1.0;

    private D2RectOutline outline;

    StdRectView( VedStdCompRectangle aOwner ) {
      super( aOwner );
      update();
      // owner().props().propsEventer().pauseFiring();
    }

    @Override
    public IVedPainter painter() {
      return this;
    }

    @Override
    public IVedPorter porter() {
      return this;
    }

    @Override
    public IVedOutline outline() {
      return outline;
    }

    @Override
    protected void doDispose() {
      // TODO Auto-generated method stub
    }

    // ------------------------------------------------------------------------------------
    // {@link IVedPaintable}
    //

    @Override
    public void paint( GC aGc ) {
      aGc.setForeground( fgColor );
      aGc.setBackground( bgColor );

      Transform tr = null;
      try {
        double angle = component().props().getDouble( PDEF_ROTATION_ANGLE );
        angle = 0;
        // angle = 45;
        if( Double.compare( angle, 0.0 ) != 0 ) {
          tr = new Transform( aGc.getDevice() );
          ID2Point center = outline.boundsCenter();
          int centerX = (int)Math.round( center.x() * zoomFactor );
          int centerY = (int)Math.round( center.y() * zoomFactor );
          tr.translate( centerX, centerY );
          tr.rotate( (float)angle );
          tr.translate( -centerX, -centerY );
          aGc.setTransform( tr );
        }
        aGc.fillRectangle( visRect );
        aGc.setLineWidth( 1 );
        aGc.drawRectangle( visRect );
      }
      finally {
        if( tr != null ) {
          tr.dispose();
          aGc.setTransform( null );
        }
      }
    }

    @Override
    public double zoomFactor() {
      return zoomFactor;
    }

    @Override
    public void setZoomFactor( double aZoomFactor ) {
      if( Double.compare( zoomFactor, aZoomFactor ) != 0 ) {
        zoomFactor = aZoomFactor;
        updateVisRect();
      }

    }

    // ------------------------------------------------------------------------------------
    // {@link IVedPorter}
    //

    @Override
    public void locate( double aX, double aY ) {
      component().props().setDouble( PDEF_X, aX );
      component().props().setDouble( PDEF_Y, aY );
      double width = outline.width();
      double height = outline.height();
      outline = new D2RectOutline( aX, aY, width, height );
      updateVisRect();
    }

    @Override
    public void shiftOn( double aDx, double aDy ) {
      double x = outline.x() + aDx;
      double y = outline.y() + aDy;
      component().props().setDouble( PDEF_X, x );
      component().props().setDouble( PDEF_Y, y );
      double width = outline.width();
      double height = outline.height();
      outline = new D2RectOutline( x, y, width, height );
      updateVisRect();
    }

    @Override
    public void setSize( double aWidth, double aHeight ) {
      component().props().setDouble( PDEF_WIDTH, aWidth );
      component().props().setDouble( PDEF_HEIGHT, aHeight );
      double x = outline.x();
      double y = outline.y();
      outline = new D2RectOutline( x, y, aWidth, aHeight );
      updateVisRect();
    }

    @Override
    public void setBounds( double aX, double aY, double aWidth, double aHeight ) {
      component().props().setDouble( PDEF_X, aX );
      component().props().setDouble( PDEF_Y, aY );
      component().props().setDouble( PDEF_WIDTH, aWidth );
      component().props().setDouble( PDEF_HEIGHT, aHeight );
      outline = new D2RectOutline( aX, aY, aWidth, aHeight );
      updateVisRect();
    }

    @Override
    public void rotate( ID2Point aRotationCenter, double aDegrees ) {
      throw new TsUnderDevelopmentRtException();
    }

    @Override
    public void flipHor( boolean aFlip ) {
      throw new TsUnderDevelopmentRtException();
    }

    @Override
    public void flipVer( boolean aFlip ) {
      throw new TsUnderDevelopmentRtException();
    }

    @Override
    public void zoom( double aZoomFactorX, double aZoomFactorY ) {
      double x = outline.x() * aZoomFactorX;
      double y = outline.x() * aZoomFactorY;
      double width = outline.width() * aZoomFactorX;
      double height = outline.height() * aZoomFactorX;
      outline = new D2RectOutline( x, y, width, height );
      updateVisRect();
    }

    // ------------------------------------------------------------------------------------
    // Внутренняя реалиизация
    //

    private void update() {
      RGB rgb = component().props().getValobj( PID_FG_COLOR );
      fgColor = colorManager().getColor( rgb );
      rgb = component().props().getValobj( PID_BG_COLOR );
      bgColor = colorManager().getColor( rgb );
      updateOutline();
      updateVisRect();
    }

    private void updateVisRect() {
      visRect.x = (int)Math.round( outline.x() * zoomFactor );
      visRect.y = (int)Math.round( outline.y() * zoomFactor );
      visRect.width = (int)Math.round( outline.width() * zoomFactor );
      visRect.height = (int)Math.round( outline.height() * zoomFactor );
    }

    private void updateOutline() {
      double x = component().props().getDouble( PID_X );
      double y = component().props().getDouble( PID_Y );
      double width = component().props().getDouble( PID_WIDTH );
      double height = component().props().getDouble( PID_HEIGHT );
      outline = new D2RectOutline( x, y, width, height );
    }

  }

  /**
   * Конструктор.<br>
   *
   * @param aId String - идентификатор
   * @param aVedEnv {@link IVedEnvironment} -
   */
  public VedStdCompRectangle( String aId, IVedEnvironment aVedEnv ) {
    super( PROVIDER, aVedEnv, aId );
    // TODO Auto-generated constructor stub
  }

  @Override
  public IVedComponentView createView( IVedScreen aScreen ) {
    return new StdRectView( this );
  }

}

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
          PDEF_BG_COLOR //
      ) {

        @Override
        protected IVedComponent doCreateComponent( IVedEnvironment aEnvironment, IOptionSet aProps,
            IOptionSet aExtdata ) {
          return new VedStdCompRectangle( "rect", this );
          // TODO реализовать VedStdCompRectangle.PROVIDER.new VedAbstractComponentProvider() {...}.doCreateComponent()
          // throw new TsUnderDevelopmentRtException(
          // "VedStdCompRectangle.PROVIDER.new VedAbstractComponentProvider() {...}.doCreateComponent()" );
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
      aGc.fillRectangle( visRect );
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
      double width = outline.width();
      double height = outline.height();
      outline = new D2RectOutline( aX, aY, width, height );
      updateVisRect();
    }

    @Override
    public void shiftOn( double aDx, double aDy ) {
      double x = outline.x() + aDx;
      double y = outline.x() + aDy;
      double width = outline.width();
      double height = outline.height();
      outline = new D2RectOutline( x, y, width, height );
      updateVisRect();
    }

    @Override
    public void setSize( double aWidth, double aHeight ) {
      double x = outline.x();
      double y = outline.x();
      outline = new D2RectOutline( x, y, aWidth, aHeight );
      updateVisRect();
    }

    @Override
    public void setBounds( double aX, double aY, double aWidth, double aHeight ) {
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
      RGB rgb = owner().props().getValobj( PID_FG_COLOR );
      fgColor = colorManager().getColor( rgb );
      rgb = owner().props().getValobj( PID_BG_COLOR );
      bgColor = colorManager().getColor( rgb );
      updateOutline();
      updateVisRect();
    }

    private void updateVisRect() {
      visRect.x = (int)Math.round( outline.x() );
      visRect.y = (int)Math.round( outline.y() );
      visRect.width = (int)Math.round( outline.width() );
      visRect.height = (int)Math.round( outline.height() );
    }

    private void updateOutline() {
      double x = owner().props().getDouble( PID_X );
      double y = owner().props().getDouble( PID_Y );
      double width = owner().props().getDouble( PID_WIDTH );
      double height = owner().props().getDouble( PID_HEIGHT );
      outline = new D2RectOutline( x, y, width, height );
    }

  }

  /**
   * Конструктор.<br>
   *
   * @param aId String - идентификатор
   * @param aProvider VedAbstractComponentProvider - поставщик компонент
   */
  public VedStdCompRectangle( String aId, VedAbstractComponentProvider aProvider ) {
    super( aProvider, aId );
    // TODO Auto-generated constructor stub
  }

  @Override
  public IVedComponentView createView( IVedScreen aScreen ) {
    return new StdRectView( this );
  }

}

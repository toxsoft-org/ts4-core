package org.toxsoft.core.tsgui.chart.impl;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.chart.api.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.temporal.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

public class G2StateGraphic
    implements IG2StateGraphic {

  private final IG2DataSet              dataSet;
  private final IG2StateGraphicRenderer renderer;
  private final XAxisView               xAxisView;
  private YAxisView                     yAxisView;
  private final int                     number;

  private boolean visible = true;

  public G2StateGraphic( int aNumber, IG2DataSet aDataSet, XAxisView aXAxis, YAxisView aYAxis, IPlotDef aPlotDef,
      ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNulls( aDataSet, aXAxis, aYAxis );
    number = aNumber;
    dataSet = aDataSet;
    renderer = new G2StateGraphicRenderer( aPlotDef, aContext );
    xAxisView = aXAxis;
    yAxisView = aYAxis;
  }

  @Override
  public IPlotDef plotDef() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void draw( GC aGc, ITsRectangle aClientRect ) {
    ITsRectangle cr = aClientRect;
    IList<ITemporalAtomicValue> values = dataSet.getValues( xAxisView.axisModel().timeInterval() );
    // int y = G2ChartUtils.normToScreen( yAxisView.normalizeValue( DvUtils.avInt( number ) ), cr.height() );
    int y = G2ChartUtils.normToScreen( yAxisView.normalizeValue( AvUtils.avInt( number ) ),
        yAxisView.markingBounds().height() );
    IListEdit<StateGraphSegment> segments = new ElemArrayList<>();

    int startX = -1;
    int stateIdx = -1;

    if( values == null ) {
      return;
    }
    for( int i = 0; i < values.size(); i++ ) {
      long time = values.get( i ).timestamp();
      IAtomicValue value = values.get( i ).value();
      if( value == IAtomicValue.NULL ) {
        // System.out.println( ">>> value - null <<<" ); //$NON-NLS-1$
        // устанавливаем признак того, что нужно начать новый сегмент
        stateIdx = -1;
      }
      else {
        double nt = xAxisView.normalizeValue( AvUtils.avTimestamp( time ) );
        int scrX = cr.x1() + G2ChartUtils.normToScreen( nt, cr.width() );
        if( stateIdx == -1 ) {
          // начинаем новый сегмент
          stateIdx = value.asInt();
          startX = scrX;
          // if( startX < cr.x1() ) {
          // startX = cr.x1();
          // }
        }
        else {
          int length = scrX - startX;
          // if( startX + length > cr.x2() ) {
          // length = cr.x2() - startX;
          // }
          // if( length < 0 ) {
          // length = 0;
          // }
          segments.add( new StateGraphSegment( startX, length, stateIdx ) );
          stateIdx = value.asInt();
          startX = scrX;
          if( startX < cr.x1() ) {
            startX = cr.x1();
          }
        }
      }
    }
    renderer.drawGraphic( aGc, cr.y2() - y, segments, cr );
  }

  @Override
  public ITemporalAtomicValue valueAt( long aTimestamp ) {
    Pair<ITemporalAtomicValue, ITemporalAtomicValue> pair = dataSet.locate( aTimestamp );
    if( pair.left() == ITemporalAtomicValue.NULL ) {
      return ITemporalAtomicValue.NULL;
    }
    return pair.left();
  }

  @Override
  public void drawRepresentation( GC aGc, ITsRectangle aBounds ) {
    TsNullArgumentRtException.checkNulls( aGc, aBounds );
    // TODO StateGraphic drawRepresentation - разобраться
  }

  @Override
  public String valueToString( ITemporalAtomicValue aValue ) {
    String valStr = "null";
    if( aValue != ITemporalAtomicValue.NULL ) {
      IAtomicValue value = aValue.value();
      if( value != IAtomicValue.NULL ) {
        return renderer.stateName( value.asInt() );
      }
    }
    return valStr;
  }

  @Override
  public int calcHeight( GC aGc ) {
    return renderer.calcHeight( aGc );
  }

  @Override
  public boolean isVisible() {
    return visible;
  }

  @Override
  public void setVisible( boolean aVisible ) {
    visible = aVisible;
  }

  @Override
  public void setYAxisView( YAxisView aAxis ) {
    yAxisView = aAxis;
  }
}

package org.toxsoft.core.tsgui.chart.impl;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.chart.api.*;
import org.toxsoft.core.tsgui.chart.renderers.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Отрисовщик графика состояний.
 * <p>
 *
 * @author vs
 */
public class G2StateGraphicRenderer
    implements IG2StateGraphicRenderer {

  private final IListEdit<Color> colors      = new ElemArrayList<>();
  private final IStringList      stateNames;
  private final boolean          drawLabel;
  private final int              thickness;
  private final String           name;
  private final int              labelIndent = 0;                    // 2;

  ITsColorManager colorManager;

  G2StateGraphicRenderer( IPlotDef aPlotDef, ITsGuiContext aContext ) {
    colorManager = aContext.get( ITsColorManager.class );

    IG2Params rendererParams = aPlotDef.rendererParams();
    name = aPlotDef.description();
    thickness = IG2StateGraphicRendererOptions.LINE_THICKNESS.getValue( rendererParams.params() ).asInt();
    drawLabel = IG2StateGraphicRendererOptions.DRAW_LABEL.getValue( rendererParams.params() ).asBool();
    IStringList cList = IG2StateGraphicRendererOptions.COLORS.getValue( rendererParams.params() ).asValobj();
    for( String str : cList ) {
      int intColor = Integer.parseInt( str );
      RGB rgb = ITsColorManager.int2rgb( intColor );
      colors.add( colorManager.getColor( rgb ) );
    }
    stateNames = IG2StateGraphicRendererOptions.STATE_NAMES.getValue( rendererParams.params() ).asValobj();
  }

  @Override
  public void drawGraphic( GC aGc, int aY, IList<StateGraphSegment> aSegments, ITsRectangle aBounds ) {

    if( drawLabel ) {
      aGc.setForeground( colorManager.getColor( ETsColor.BLACK ) );
      aGc.drawText( name, aBounds.x1() + 10, aY - labelIndent - aGc.textExtent( name ).y - thickness, true );
    }

    for( int i = 0; i < aSegments.size(); i++ ) {
      StateGraphSegment segment = aSegments.get( i );
      aGc.setBackground( colors.get( segment.stateIdx() ) );
      aGc.fillRectangle( segment.startX(), aY - thickness, segment.length(), thickness );
    }
    aGc.drawLine( aBounds.x1(), aY - thickness, aBounds.x2(), aY - thickness );

  }

  @Override
  public String stateName( int aIdx ) {
    return stateNames.get( aIdx );
  }

  @Override
  public int calcHeight( GC aGc ) {
    TsNullArgumentRtException.checkNull( aGc );
    int h = thickness;
    if( drawLabel ) {
      h += aGc.textExtent( "Pg" ).y + labelIndent; //$NON-NLS-1$
    }
    return h;
  }

}

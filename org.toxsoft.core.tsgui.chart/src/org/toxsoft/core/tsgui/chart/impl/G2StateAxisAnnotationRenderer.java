package org.toxsoft.core.tsgui.chart.impl;

import static org.toxsoft.core.tsgui.chart.renderers.IG2StateAxisAnnotationRendererOptions.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.chart.api.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Отрисовщик шкалы графика состояния.
 * <p>
 *
 * @author vs
 */
public class G2StateAxisAnnotationRenderer
    extends AbstractAxisPartRenderer
    implements IAxisAnnotationRenderer {

  private final String      valueFormat;
  private final int         indent;
  private final Color       color;
  private final Font        font;
  private final IStringList names;
  private boolean           disposed = false;

  G2StateAxisAnnotationRenderer( IOptionSet aOptions, ITsGuiContext aContext ) {
    super( aContext );

    valueFormat = ANNOTATION_FORMAT.getValue( aOptions ).asString();
    indent = ANNOTATION_INDENT.getValue( aOptions ).asInt();
    // color = colorManager().getColor( ITsColorManager.int2rgb( TEXT_COLOR.getValue( aOptions ).asInt() ) );
    RGBA rgba = TEXT_COLOR.getValue( aOptions ).asValobj();
    color = colorManager().getColor( rgba.rgb );
    font = fontManager().getFont( FONT_INFO.getValue( aOptions ).asValobj() );
    names = TICK_NAMES.getValue( aOptions ).asValobj();
  }

  @Override
  public ITsPoint requiredSizeForAnnotation( GC aGc, int aHorHint, int aVertHint ) {
    switch( axisView.cardinalPoint() ) {
      case EAST:
      case WEST:
        int maxW = 0;
        for( String str : names ) {
          Point p = aGc.textExtent( str );
          if( p.x > maxW ) {
            maxW = p.x;
          }
        }
        return new TsPoint( maxW + indent + indent, SWT.DEFAULT );
      case NORTH:
      case SOUTH:
      case CENTER:
        throw new TsUnsupportedFeatureRtException();
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  @Override
  public void drawAnnotation( GC aGc ) {
    TsNullArgumentRtException.checkNull( aGc );
    switch( axisView.cardinalPoint() ) {
      case EAST:
      case WEST:
        annotateVertical( aGc, axisView.cardinalPoint() );
        break;
      case NORTH:
      case SOUTH:
      case CENTER:
        throw new TsIllegalArgumentRtException();
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  @Override
  public ITsPoint requiredSizeForTitle( GC aGc ) {
    return TsPoint.ZERO;
  }

  @Override
  public ETsOrientation titleOrientation() {
    return ETsOrientation.VERTICAL;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IDisposable
  //

  @Override
  public void dispose() {
    // nop
  }

  @Override
  public boolean isDisposed() {
    return disposed;
  }

  // ------------------------------------------------------------------------------------
  // Внутренняя реализация
  //

  void annotateVertical( GC aGc, EBorderLayoutPlacement aPlace ) {
    IList<Pair<IAtomicValue, Integer>> values = axisView.doListAnnotationValues( axisView.markingBounds().height() );
    for( int i = 0; i < values.size(); i++ ) {
      Pair<IAtomicValue, Integer> p = values.get( i );
      int idx = p.left().asInt();
      if( idx < 0 || idx >= values.size() ) {
        continue;
      }
      if( idx < 0 || idx >= names.size() ) {
        continue;
      }
      String valueStr = names.get( idx );
      Point te = aGc.textExtent( valueStr );
      switch( aPlace ) {
        case EAST: {
          int x = axisView.markingBounds().x1() + 2 * indent
              + axisView.axisModel().axisMarkingDef().tickSize( ETickType.BIG );
          int y = p.right().intValue() - te.y / 2;
          aGc.drawText( valueStr, x, y, true );
        }
          break;
        case WEST: {
          int x = axisView.markingBounds().x2() - te.x - 2 * indent
              - axisView.axisModel().axisMarkingDef().tickSize( ETickType.BIG );
          int y = p.right().intValue() - te.y / 2;
          aGc.drawText( valueStr, x, y, true );
        }
          break;
        case NORTH:
        case CENTER:
        case SOUTH:
          throw new TsIllegalArgumentRtException();
        default:
          throw new TsNotAllEnumsUsedRtException();
      }
    }
  }

}

package org.toxsoft.core.tsgui.chart.impl;

import static ru.toxsoft.tsgui.chart.renderers.IStdG2TimeAxisAnnotationRendererOptions.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

import ru.toxsoft.tsgui.chart.api.*;

/**
 * Стандартный отрисовщик подписей на шкале времени.
 *
 * @author vs
 */
public class StdG2TimeAxisAnnotationRenderer
    extends AbstractAxisPartRenderer
    implements IAxisAnnotationRenderer {

  private final String  dateFormat;
  private final String  timeFormat;
  private final int     indent;
  private final Color   color;
  private final Font    font;
  private final boolean dateWrap;
  private boolean       disposed = false;

  StdG2TimeAxisAnnotationRenderer( IOptionSet aOptions, ITsGuiContext aContext ) {
    super( aContext );
    dateFormat = DATE_FORMAT.getValue( aOptions ).asString();
    timeFormat = TIME_FORMAT.getValue( aOptions ).asString();
    dateWrap = DATE_WRAP.getValue( aOptions ).asBool();
    indent = ANNOTATION_INDENT.getValue( aOptions ).asInt();

    RGBA rgba = TEXT_COLOR.getValue( aOptions ).asValobj();
    color = colorManager().getColor( rgba.rgb );
    font = fontManager().getFont( FONT_INFO.getValue( aOptions ).asValobj() );
  }

  @Override
  public TsPoint requiredSizeForAnnotation( GC aGc, int aHorHint, int aVertHint ) {
    if( dateFormat.isEmpty() && timeFormat.isEmpty() ) {
      return new TsPoint( 0, 0 );
    }
    aGc.setFont( font );
    Point p1 = new Point( 0, 0 );
    Point p2 = new Point( 0, 0 );

    IList<Pair<IAtomicValue, Integer>> values = axisView.listAnnotationValues( aHorHint );
    IAtomicValue value = IAtomicValue.NULL;
    if( values.size() > 0 ) {
      value = values.get( 0 ).left();
    }

    if( !dateFormat.isEmpty() ) {
      p1 = aGc.textExtent( AvUtils.printAv( dateFormat, value ) );
    }
    if( !timeFormat.isEmpty() ) {
      p2 = aGc.textExtent( AvUtils.printAv( timeFormat, value ) );
    }
    if( !dateFormat.isEmpty() && !timeFormat.isEmpty() && dateWrap ) {
      return new TsPoint( SWT.DEFAULT, p1.y + p2.y + 2 * indent );
    }
    return new TsPoint( SWT.DEFAULT, Math.max( p1.y, p2.y ) + 1 * indent );
  }

  @Override
  public void drawAnnotation( GC aGc ) {
    TsNullArgumentRtException.checkNull( aGc );
    aGc.setFont( font );
    aGc.setForeground( color );
    switch( axisView.cardinalPoint() ) {
      case NORTH:
      case SOUTH:
        annotateHorizontal( aGc, axisView.cardinalPoint() );
        break;
      case EAST:
      case WEST:
      case CENTER:
        throw new TsIllegalArgumentRtException();
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  @Override
  public ITsPoint requiredSizeForTitle( GC aGc ) {
    return ITsPoint.ZERO;
  }

  @Override
  public ETsOrientation titleOrientation() {
    return ETsOrientation.HORIZONTAL;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IDisposable
  //

  @Override
  public void dispose() {
    disposed = true;
  }

  @Override
  public boolean isDisposed() {
    return disposed;
  }

  // ------------------------------------------------------------------------------------
  // Внутренняя реализация
  //

  void annotateHorizontal( GC aGc, EBorderLayoutPlacement aPlace ) {
    String dateStr = TsLibUtils.EMPTY_STRING;
    String timeStr = TsLibUtils.EMPTY_STRING;

    IList<Pair<IAtomicValue, Integer>> values = axisView.doListAnnotationValues( axisView.markingBounds().width() );
    for( int i = 0; i < values.size(); i++ ) {
      Pair<IAtomicValue, Integer> p = values.get( i );
      if( timeFormat != null && !timeFormat.isEmpty() ) {
        timeStr = AvUtils.printAv( timeFormat, p.left() );
      }

      if( dateFormat != null && !dateFormat.isEmpty() ) {
        dateStr = AvUtils.printAv( dateFormat, p.left() );
      }

      if( !dateWrap ) {
        String valueStr = TsLibUtils.EMPTY_STRING;
        if( !timeStr.isEmpty() ) {
          valueStr = timeStr;
        }
        if( !dateStr.isEmpty() ) {
          if( !valueStr.isEmpty() ) {
            valueStr += " " + dateStr;
          }
          else {
            valueStr = dateStr;
          }
        }
        if( !valueStr.isEmpty() ) {
          int y =
              axisView.markingBounds().y1() + axisView.axisModel().axisMarkingDef().tickSize( ETickType.BIG ) + indent;
          if( aPlace == EBorderLayoutPlacement.NORTH ) {
            y = axisView.markingBounds().y1() - axisView.axisModel().axisMarkingDef().tickSize( ETickType.BIG )
                - indent;
          }
          Point extent = aGc.textExtent( valueStr );
          if( aPlace == EBorderLayoutPlacement.SOUTH ) {
            aGc.drawText( valueStr, p.right().intValue() - extent.x / 2, y, true );
          }
          if( aPlace == EBorderLayoutPlacement.NORTH ) {
            aGc.drawText( valueStr, p.right().intValue() - extent.x / 2, y - extent.y, true );
          }
        }
      }
      else {
        int y =
            axisView.markingBounds().y1() + axisView.axisModel().axisMarkingDef().tickSize( ETickType.BIG ) + indent;
        if( aPlace == EBorderLayoutPlacement.NORTH ) {
          y = axisView.markingBounds().y1() - axisView.axisModel().axisMarkingDef().tickSize( ETickType.BIG ) - indent;
        }
        if( !timeStr.isEmpty() ) {
          Point extent = aGc.textExtent( timeStr );
          if( aPlace == EBorderLayoutPlacement.SOUTH ) {
            aGc.drawText( timeStr, p.right().intValue() - extent.x / 2, y, true );
            // y = y + indent + extent.y;
            y = y + extent.y;
          }
          if( aPlace == EBorderLayoutPlacement.NORTH ) {
            aGc.drawText( timeStr, p.right().intValue() - extent.x / 2, y - extent.y, true );
            y = y - indent - extent.y;
          }
        }

        if( !dateStr.isEmpty() ) {
          Point extent = aGc.textExtent( dateStr );
          if( aPlace == EBorderLayoutPlacement.SOUTH ) {
            aGc.drawText( dateStr, p.right().intValue() - extent.x / 2, y, true );
          }
          if( aPlace == EBorderLayoutPlacement.NORTH ) {
            aGc.drawText( dateStr, p.right().intValue() - extent.x / 2, y - extent.y, true );
          }
        }

      }
    }

  }

}

package org.toxsoft.core.tsgui.chart.impl;

import static ru.toxsoft.tsgui.chart.renderers.IStdG2AxisAnnotationRendererOptions.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.fonts.*;
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
import ru.toxsoft.tsgui.chart.legaсy.*;

/**
 * Стандартный аннотатор шкалы графика.
 * <p>
 *
 * @author vs
 */
public class StdG2AxisAnnotationRenderer
    extends AbstractAxisPartRenderer
    implements IAxisAnnotationRenderer {

  private final String         valueFormat;
  private final int            indent;
  private final Color          color;
  private final Font           font;
  private final String         title;
  private final Color          titleColor;
  private final IFontInfo      titleFontInfo;
  private final Font           titleFont;
  private final ETsOrientation orientation;

  // GOGA 2020-08-14
  // private final EVerAlignment verAlign;
  // private final EHorAlignment horAlign;

  private final Margins titleMargins;
  private Image         titleImage = null;
  private boolean       disposed   = false;

  private int titleW = -1;
  private int titleH = -1;

  StdG2AxisAnnotationRenderer( IOptionSet aOptions, ITsGuiContext aContext ) {
    super( aContext );
    if( ANNOTATION_FORMAT != null ) {
      System.out.println( ANNOTATION_FORMAT.description() );
    }

    valueFormat = ANNOTATION_FORMAT.getValue( aOptions ).asString();

    indent = ANNOTATION_INDENT.getValue( aOptions ).asInt();
    color = colorManager().getColor( ITsColorManager.int2rgb( TEXT_COLOR.getValue( aOptions ).asInt() ) );
    font = fontManager().getFont( FONT_INFO.getValue( aOptions ).asValobj() );
    title = TITLE.getValue( aOptions ).asString();
    titleFontInfo = TITLE_FONT_INFO.getValue( aOptions ).asValobj();
    titleFont = fontManager().getFont( titleFontInfo );
    titleColor = colorManager().getColor( ITsColorManager.int2rgb( TITLE_COLOR.getValue( aOptions ).asInt() ) );
    orientation = TITLE_ORIENTATION.getValue( aOptions ).asValobj();

    // if( ETsOrientation.findByIdOrNull( TITLE_ORIENTATION.getValue( aOptions ).asString() ) != null ) {
    // orientation = ETsOrientation.findByIdOrNull( TITLE_ORIENTATION.getValue( aOptions ).asString() );
    // }
    // else {
    // // System.out.println( "ERROR: orientation = null !!!" );
    // orientation = ETsOrientation.VERTICAL;
    // }

    // GOGA 2020-08-14 ---
    // if( ETsOrientation.findByIdOrNull( TITLE_VER_ALIGNMENT.getValue( aOptions ).asString() ) != null ) {
    // verAlign = EVerAlignment.findByIdOrNull( TITLE_VER_ALIGNMENT.getValue( aOptions ).asString() );
    // }
    // else {
    // // System.out.println( "ERROR: ver alignment = null !!!" );
    // verAlign = EVerAlignment.CENTER;
    // }
    //
    // if( ETsOrientation.findByIdOrNull( TITLE_HOR_ALIGNMENT.getValue( aOptions ).asString() ) != null ) {
    // horAlign = EHorAlignment.findByIdOrNull( TITLE_HOR_ALIGNMENT.getValue( aOptions ).asString() );
    // }
    // else {
    // // System.out.println( "ERROR: hor alignment = null !!!" );
    // horAlign = EHorAlignment.CENTER;
    // }
    // ---

    titleMargins = TITLE_MARGINS.getValue( aOptions ).asValobj();
  }

  @Override
  public TsPoint requiredSizeForAnnotation( GC aGc, int aHorHint, int aVertHint ) {
    // FIXME определить что передавать aHorHint или aVertHint
    aGc.setFont( font );
    IList<Pair<IAtomicValue, Integer>> values = axisView.listAnnotationValues( aVertHint );
    String startStr = TsLibUtils.EMPTY_STRING;
    String endStr = TsLibUtils.EMPTY_STRING;
    if( values.size() > 0 ) {
      String format = valueFormat;
      if( valueFormat.isEmpty() ) {
        format = null;
      }
      startStr = AvUtils.printAv( format, values.get( 0 ).left() );
      endStr = AvUtils.printAv( format, values.get( values.size() - 1 ).left() );
    }
    switch( axisView.cardinalPoint() ) {
      case EAST:
      case WEST:
        Point p1 = aGc.textExtent( startStr );
        Point p2 = aGc.textExtent( endStr );
        int width = Math.max( p1.x, p2.x ) + indent + indent;
        if( orientation == ETsOrientation.VERTICAL ) {
          int titleWidth = 0;
          if( title != TsLibUtils.EMPTY_STRING ) {
            titleWidth = aGc.textExtent( title ).y + titleMargins.left() + titleMargins.right();
          }
          return new TsPoint( width + titleWidth, SWT.DEFAULT );
        }
        return new TsPoint( width, SWT.DEFAULT ); // FIXME
      case NORTH:
      case SOUTH:
        Point p = aGc.textExtent( startStr );
        return new TsPoint( SWT.DEFAULT, p.y + indent + indent );
      case CENTER:
        throw new TsUnsupportedFeatureRtException();
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  @Override
  public ITsPoint requiredSizeForTitle( GC aGc ) {
    if( title == TsLibUtils.EMPTY_STRING ) {
      return TsPoint.ZERO;
    }
    aGc.setFont( titleFont );
    Point p = aGc.textExtent( title );

    int w = titleMargins.left() + titleMargins.right();
    int h = titleMargins.top() + titleMargins.bottom();
    if( orientation == ETsOrientation.HORIZONTAL ) {
      return new TsPoint( p.x + w, p.y + h );
    }
    else {
      return new TsPoint( p.y + w, p.x + h );
    }
  }

  @Override
  public void drawAnnotation( GC aGc ) {
    TsNullArgumentRtException.checkNull( aGc );
    aGc.setFont( font );
    aGc.setForeground( color );
    switch( axisView.cardinalPoint() ) {
      case EAST:
      case WEST:
        annotateVertical( aGc, axisView.cardinalPoint() );
        break;
      case NORTH:
      case SOUTH:
        annotateHorizontal( aGc, axisView.cardinalPoint() );
        break;
      case CENTER:
        throw new TsIllegalArgumentRtException();
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  @Override
  public ETsOrientation titleOrientation() {
    return orientation;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IDisposable
  //
  @Override
  public void dispose() {
    disposed = true;
    if( titleImage != null ) {
      titleImage.dispose();
    }
  }

  @Override
  public boolean isDisposed() {
    return disposed;
  }

  // ------------------------------------------------------------------------------------
  // Внутренняя реализация
  //

  void annotateHorizontal( GC aGc, EBorderLayoutPlacement aPlace ) {
    IList<Pair<IAtomicValue, Integer>> values = axisView.doListAnnotationValues( axisView.markingBounds().width() );
    for( int i = 0; i < values.size(); i++ ) {
      Pair<IAtomicValue, Integer> p = values.get( i );
      String valueStr;
      if( valueFormat.isEmpty() ) {
        valueStr = AvUtils.printAv( null, p.left() );
      }
      else {
        valueStr = AvUtils.printAv( valueFormat, p.left() );
      }
      Point te = aGc.textExtent( valueStr );
      switch( aPlace ) {
        case NORTH: {
          int x = p.right() - te.x / 2;
          int y = axisView.markingBounds().y1() - indent - te.y;
          aGc.drawText( valueStr, x, y, true );
        }
          break;
        case SOUTH: {
          int x = p.right() - te.x / 2;
          int y =
              axisView.markingBounds().y1() + axisView.axisModel().axisMarkingDef().tickSize( ETickType.BIG ) + indent;
          aGc.drawText( valueStr, x, y, true );
        }
          break;
        case EAST:
        case CENTER:
        case WEST:
          throw new TsIllegalArgumentRtException();
        default:
          throw new TsNotAllEnumsUsedRtException();
      }
    }
  }

  void annotateVertical( GC aGc, EBorderLayoutPlacement aPlace ) {
    IList<Pair<IAtomicValue, Integer>> values = axisView.doListAnnotationValues( axisView.markingBounds().height() );
    for( int i = 0; i < values.size(); i++ ) {
      Pair<IAtomicValue, Integer> p = values.get( i );
      String valueStr;
      if( valueFormat.isEmpty() ) {
        valueStr = AvUtils.printAv( null, p.left() );
      }
      else {
        valueStr = AvUtils.printAv( valueFormat, p.left() );
      }
      Point te = aGc.textExtent( valueStr );
      switch( aPlace ) {
        case EAST: {
          int x = axisView.markingBounds().x1() + 2 * indent
              + axisView.axisModel().axisMarkingDef().tickSize( ETickType.BIG );
          int y = p.right().intValue() - te.y / 2;
          aGc.drawText( valueStr, x, y, true );
          drawTitle( aGc );
        }
          break;
        case WEST: {
          int x = axisView.markingBounds().x2() - te.x - 2 * indent
              - axisView.axisModel().axisMarkingDef().tickSize( ETickType.BIG );
          int y = p.right().intValue() - te.y / 2;
          aGc.drawText( valueStr, x, y, true );
          drawTitle( aGc );
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

  void drawTitle( GC aGc ) {
    if( title == TsLibUtils.EMPTY_STRING ) {
      return;
    }

    if( orientation == ETsOrientation.VERTICAL && titleImage == null ) {
      titleImage = createTitleImage( axisView.cardinalPoint() );
    }

    if( titleImage != null ) {
      int w = titleImage.getImageData().width;
      int h = titleImage.getImageData().height;
      int x = axisView.bounds.x1() + titleMargins.left() + 20;
      int y = axisView.bounds.y1() + (axisView.bounds.height() - h) / 2 + 10;
      if( axisView.cardinalPoint() == EBorderLayoutPlacement.WEST ) {
        x = axisView.bounds.x1() + titleMargins.left();
      }
      else {
        x = axisView.bounds.x1() + axisView.bounds.width() - titleMargins.right() - titleImage.getImageData().width;
      }
      aGc.drawImage( titleImage, x, y );
    }
    else {
      // FIXME нарисовать горизонтальный заголовок шкалы
      int titleWidth = aGc.textExtent( title ).x;
      int x = axisView.bounds().x1() + (axisView.bounds().width() - titleWidth) / 2;
      int y = axisView.bounds().y1() + titleMargins.top();
      aGc.setFont( titleFont );
      aGc.setForeground( titleColor );
      aGc.drawString( title, x, y, true );
    }

  }

  Image createTitleImage( EBorderLayoutPlacement aPlace ) {
    if( title == TsLibUtils.EMPTY_STRING || orientation == ETsOrientation.HORIZONTAL ) {
      return null;
    }
    double angle = 0;
    angle = switch( aPlace ) {
      case EAST -> 90;
      case WEST -> -90;
      case CENTER, NORTH, SOUTH -> throw new TsUnsupportedFeatureRtException();
    };
    return TextUtils.createRotatedTextImage( title, titleFontInfo, angle, titleColor );
  }

}

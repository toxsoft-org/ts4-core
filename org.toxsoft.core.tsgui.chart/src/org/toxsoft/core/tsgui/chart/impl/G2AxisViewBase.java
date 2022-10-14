package org.toxsoft.core.tsgui.chart.impl;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.chart.api.*;
import org.toxsoft.core.tsgui.chart.legaсy.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * Базовый класс визуальных компонент для отображения шкал.
 * <p>
 *
 * @author vs
 */
abstract class G2AxisViewBase
    extends AbstractG2ChartArea
    implements IGenericChangeListener {

  private static final long serialVersionUID = 050563L;

  private final EG2ChartElementKind elementKind;
  protected final IG2AxisRenderer   renderer;
  protected final AxisModelBase     axisModel;
  protected EBorderLayoutPlacement  place           = EBorderLayoutPlacement.WEST;
  protected int                     firstTickIndent = 0;

  // protected int ticksQtty = 10;// FIXME определить количество тиков
  private Margins markingMargins = new Margins( 0, 0, 0, 0 );

  G2AxisViewBase( String aId, String aName, String aDescription, EG2ChartElementKind aKind, AxisModelBase aAxisModel ) {
    super( aId, aName, aDescription );
    elementKind = aKind;
    axisModel = aAxisModel;
    axisModel.genericChangeEventer().addListener( this );
    renderer = (IG2AxisRenderer)G2ChartUtils.createObject( axisModel.axisDef().rendererParams() );
    ((AbstractAxisPartRenderer)renderer).setAxisView( this );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов AbstractG2ChartArea
  //

  @Override
  public EG2ChartElementKind elementKind() {
    return elementKind;
  }

  @Override
  public String elementId() {
    return id();
  }

  @Override
  public void drawBackground( GC aGc ) {
    renderer.drawBackground( aGc, bounds, place );
  }

  @Override
  public void draw( GC aGc ) {
    renderer.drawAxis( aGc, bounds, place );
  }

  @Override
  public ITsPoint prefSize( GC aGc, int aHorHint, int aVertHint ) {
    return renderer.prefSize( aGc, aHorHint, aVertHint );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IGenericChangeListener
  //

  // @Override
  // public void onGenericChanged( Object aSource ) {
  // onModelChanged();
  // }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса
  //

  /**
   * Возвращает значения для подписей на шкале в виде пар: значение-экранная координата.
   *
   * @param aLength - размер шкалы в пикселях по горизонтали для Х шкалы, по вертикали для Y шкал.
   * @return IList - список пар: значение - экранная координата
   */
  IList<Pair<IAtomicValue, Integer>> listAnnotationValues( int aLength ) {
    return doListAnnotationValues( aLength );
  }

  /**
   * Вычисляет координаты засечек в нормализованных координатах
   *
   * @return IList - координаты засечек в нормализованных координатах
   */
  public IList<Pair<Double, ETickType>> calcTickPositions() {

    IListEdit<Pair<Double, ETickType>> tickPositions = new ElemArrayList<>();

    double tickDelta = unitValue() / axisModel.axisMarkingDef().bigTickNumber();
    double x = normalizeValue( axisModel().startMarkingValue() );

    int idx = 0;
    while( x <= 100 ) {
      if( x >= 0 ) {
        Pair<Double, ETickType> pair = new Pair<>( Double.valueOf( x ), axisModel.axisMarkingDef().tickType( idx ) );
        tickPositions.add( pair );
      }
      x += tickDelta;
      idx++;
    }
    return tickPositions;
  }

  /**
   * Местоположение шкалы в терминах "сторон света".
   *
   * @return EBorderLayoutPlacement - местоположение шкалы в терминах "сторон света"
   */
  EBorderLayoutPlacement cardinalPoint() {
    return place;
  }

  /**
   * Задает местоположение шкалы в терминах "сторон света".
   *
   * @param aPlace EBorderLayoutPlacement - местоположение шкалы в терминах "сторон света".
   */
  public void setCardinalPoint( EBorderLayoutPlacement aPlace ) {
    place = aPlace;
  }

  /**
   * Устанавливает поля для области отрисовки разметки.
   *
   * @param aMargings Margins - поля для области отрисовки разметки
   */
  public void setMarkingMargins( Margins aMargings ) {
    markingMargins = aMargings;
  }

  /**
   * Возвращает размеры прямоугольной области для нанесения разметки.
   *
   * @return TsRectangle - размеры прямоугольной области для нанесения разметки
   */
  ITsRectangle markingBounds() {
    try {
      return new TsRectangle( bounds.x1() + markingMargins.left(), bounds.y1() + markingMargins.top(), //
          bounds.width() - markingMargins.left() - markingMargins.right(), //
          bounds.height() - markingMargins.top() - markingMargins.bottom() );
    }
    catch( Throwable e ) {
      e.printStackTrace();
      return new TsRectangle( 0, 0, 1, 1 );
    }
  }

  AxisModelBase axisModel() {
    return axisModel;
  }

  // ------------------------------------------------------------------------------------
  // Методы для переопределения в наследниках
  //

  protected abstract IList<Pair<IAtomicValue, Integer>> doListAnnotationValues( int aLength );

  protected abstract void onModelChanged();

  abstract double normalizeValue( IAtomicValue aValue );

  abstract double unitValue();

  abstract int normToScreen( double aNormVal );

  abstract IAtomicValue normToValue( double aNormVal );

}

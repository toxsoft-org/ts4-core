package org.toxsoft.core.tsgui.ved.incub;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tslib.bricks.d2.*;

/**
 * Набор вспомогательных методов для работы с преобразованием координат для плосоксти.
 *
 * @author vs
 */
public class D2TransformUtils {

  /**
   * FiXME
   */

  /**
   * Создает матрицу преобразования координат для графического контекста SWT, в соотвествии с указанными праметрами
   * смещения, масштабирования и поворота.
   *
   * @param aGc GC - графический контекст
   * @param aD2Conv ID2Conversion - параметры преобразования координат
   * @return Transform - матрица преобразования координат для графического контекста SWT
   */
  public static Transform d2ConversionToTransfrom( GC aGc, ID2Conversion aD2Conv ) {
    Transform tr = new Transform( aGc.getDevice() );
    convertTransfrom( tr, aD2Conv );
    return tr;
  }

  /**
   * Преобразует переданный {@link Transform} в соответствии с указанными праметрами смещения, масштабирования и
   * поворота.
   *
   * @param aTransform Transform - матрица преобразования координат для графического контекста SWT
   * @param aD2Conv ID2Conversion - параметры преобразования координат
   */
  public static void convertTransfrom( Transform aTransform, ID2Conversion aD2Conv ) {
    aTransform.translate( (float)aD2Conv.origin().x(), (float)aD2Conv.origin().y() );
    aTransform.rotate( -(float)aD2Conv.rotation().degrees() );
    aTransform.scale( (float)aD2Conv.zoomFactor(), (float)aD2Conv.zoomFactor() );
    // aTransform.translate( -(float)aD2Conv.origin().x(), -(float)aD2Conv.origin().y() );
  }

  /**
   * Преобразует переданный {@link Transform} в соответствии с указанными праметрами смещения, масштабирования и
   * поворота.
   *
   * @param aTransform Transform - матрица преобразования координат для графического контекста SWT
   * @param aD2Conv ID2Conversion - параметры преобразования координат
   * @param aRotX double - X координата точки поворота (в координатной системе элемента)
   * @param aRotY double - Y координата точки поворота (в координатной системе элемента)
   */
  public static void convertItemTransfrom( Transform aTransform, ID2Conversion aD2Conv, double aRotX, double aRotY ) {
    aTransform.translate( (float)aD2Conv.origin().x(), (float)aD2Conv.origin().y() );
    aTransform.translate( (float)aRotX, (float)aRotY );
    aTransform.rotate( -(float)aD2Conv.rotation().degrees() );
    aTransform.translate( -(float)aRotX, -(float)aRotY );
    aTransform.scale( (float)aD2Conv.zoomFactor(), (float)aD2Conv.zoomFactor() );
  }

  // /**
  // * Преобразует точку (aX:aY) в координатах отображаемого элемента, в координаты не трансформированного экрана.
  // *
  // * @param aX int - координата X в системе отображаемого элемента
  // * @param aY - координата Y в системе отображаемого элемента
  // * @param aItemConv {@link ID2Conversion} - параметры преобразования отображаемого элемента
  // * @param aScreenConv {@link ID2Conversion} - параметры преобразования экрана
  // * @return {@link Point} - точка в координатах не трансформированного экрана
  // */
  // public static Point toScreen( int aX, int aY, ID2Conversion aItemConv, ID2Conversion aScreenConv ) {
  // D2Convertor convertor = new D2Convertor();
  // convertor.setConversion( aItemConv );
  //
  // int rx1 = (int)convertor.convertX( aX, aY );
  // int ry1 = (int)convertor.convertY( aX, aY );
  //
  // convertor.setConversion( aScreenConv );
  // int rx = (int)convertor.convertX( rx1, ry1 );
  // int ry = (int)convertor.convertY( rx1, ry1 );
  // return new Point( rx, ry );
  // }
  //
  // public static ITsPoint toControl( double aX, double aY, ID2Conversion aItemConv, ID2Conversion aScreenConv ) {
  // D2Convertor convertor = new D2Convertor();
  // convertor.setConversion( aScreenConv );
  // int x1 = (int)convertor.reverseX( aX, aY );
  // int y1 = (int)convertor.reverseY( aX, aY );
  //
  // convertor.setConversion( aItemConv );
  // int x = (int)convertor.reverseX( x1, y1 );
  // int y = (int)convertor.reverseY( x1, y1 );
  //
  // return new TsPoint( x, y );
  // }

  /**
   * No subclasses.
   */
  private D2TransformUtils() {
    // nop
  }

}

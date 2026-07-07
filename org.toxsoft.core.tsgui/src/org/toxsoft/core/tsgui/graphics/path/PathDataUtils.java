package org.toxsoft.core.tsgui.graphics.path;

import java.io.*;

import javax.xml.parsers.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.w3c.dom.*;

/**
 * Вспомогательные методы для работы контурами {@link Path} и их данными.
 *
 * @author vs
 */
public class PathDataUtils {

  /**
   * Создает новый экземпляр {@link PathData} с координатами сдвинутыми на указанные значения.
   *
   * @param aSrc {@link PathData} - данные сдвигаемого контура
   * @param aDx double - сдвиг п оси Х
   * @param aDy double - сдвиг п оси Y
   * @return {@link PathData} с координатами сдвинутыми на указанные значения
   */
  public static PathData shift( PathData aSrc, double aDx, double aDy ) {
    byte[] types = new byte[aSrc.types.length];
    float[] points = new float[aSrc.points.length];
    PathData newData = new PathData();
    for( int i = 0; i < points.length; i += 2 ) {
      points[i] = aSrc.points[i] + (float)aDx;
      points[i + 1] = aSrc.points[i + 1] + (float)aDy;
    }
    System.arraycopy( aSrc.types, 0, types, 0, aSrc.types.length );
    newData.types = types;
    newData.points = points;
    return newData;
  }

  /**
   * Возвращает новый экземпляр {@link PathData} с повернутой относительно указанной точки.
   *
   * @param aSrc {@link PathData} - данные контура
   * @param aCx float - x координата точки, относительно которой осуществляется поворот
   * @param aCy float - y координата точки, относительно которой осуществляется поворот
   * @param aAngleDeg float - угол поворота в градусах
   * @return {@link PathData} - новый экземпляр {@link PathData} с измененным масштабом относительно указанной точки
   */
  public static PathData rotate( PathData aSrc, float aCx, float aCy, float aAngleDeg ) {
    double rad = Math.toRadians( aAngleDeg );
    double cosA = Math.cos( rad );
    double sinA = Math.sin( rad );

    byte[] types = new byte[aSrc.types.length];
    float[] newPoints = new float[aSrc.points.length];
    for( int i = 0; i < aSrc.points.length; i += 2 ) {
      double dx = aSrc.points[i] - aCx;
      double dy = aSrc.points[i + 1] - aCy;
      newPoints[i] = (float)(aCx + dx * cosA - dy * sinA);
      newPoints[i + 1] = (float)(aCy + dx * sinA + dy * cosA);
    }
    System.arraycopy( aSrc.types, 0, types, 0, aSrc.types.length );
    PathData newData = new PathData();
    newData.types = types;
    newData.points = newPoints;
    return newData;
  }

  /**
   * Возвращает новый экземпляр {@link PathData} с измененным масштабом относительно указанной точки.
   *
   * @param aSrc {@link PathData} - данные контура
   * @param aAnchorX float - x координата точки, относительно которой осуществляется масштабирование
   * @param aAnchorY float - y координата точки, относительно которой осуществляется масштабирование
   * @param aScaleX float - коэффициент масштабирования по оси x
   * @param aScaleY float - коэффициент масштабирования по оси y
   * @return {@link PathData} - новый экземпляр {@link PathData} с измененным масштабом относительно указанной точки
   */
  public static PathData scale( PathData aSrc, float aAnchorX, float aAnchorY, float aScaleX, float aScaleY ) {
    float[] newPoints = new float[aSrc.points.length];
    byte[] types = new byte[aSrc.types.length];
    for( int i = 0; i < aSrc.points.length; i += 2 ) {
      newPoints[i] = aAnchorX + (aSrc.points[i] - aAnchorX) * aScaleX;
      newPoints[i + 1] = aAnchorY + (aSrc.points[i + 1] - aAnchorY) * aScaleY;
    }
    System.arraycopy( aSrc.types, 0, types, 0, aSrc.types.length );
    PathData pd = new PathData();
    pd.types = types;
    pd.points = newPoints;
    return pd;
  }

  /**
   * Считывает и возвращает информацию о контурах, в указанном SVG файле.
   *
   * @param aFile {@link File} - файл формата SVG
   * @return IStringMap&lt;String> - информация о контурах, ключ - ИД контура, значение - значение атрибута "d"
   */
  public static IStringMap<String> importPathInfoesFromSvg( File aFile ) {
    try {
      DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document xml = builder.parse( aFile );

      xml.getDocumentElement().normalize();
      Element root = xml.getDocumentElement();

      IStringMapEdit<String> pathInfoes = new StringMap<>();
      parseChildren( root, pathInfoes );

      return pathInfoes;
    }
    catch( Throwable e ) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Рисует залитую дугу (кольцевой сегмент) через SWT Path. Система углов совпадает с SWT/математической: 0° = вправо,
   * положительный sweepDeg = против часовой стрелки.
   *
   * @param aRadius double - радиус дуги
   * @param aThickness double - толщина дуги
   * @param aStartDeg начальный угол в градусах
   * @param aSweepDeg угол дуги в градусах (+ против часовой, - по часовой)
   * @param aRoundCaps true = закруглённые концы, false = плоские
   * @param aDevice Device - устройство для отображения
   * @return Path - контур дуги
   */
  public static Path createArcPath( double aRadius, double aThickness, float aStartDeg, float aSweepDeg,
      boolean aRoundCaps, Device aDevice ) {

    float cx = (float)aRadius;
    float cy = (float)aRadius;

    Path path = new Path( aDevice );

    float half = (float)aThickness / 2.0f;
    float outer = (float)aRadius;
    float inner = Math.max( 0.5f, (float)aRadius - 2 * half );

    float[] ox1 = pointOn( cx, cy, outer, aStartDeg );
    float[] ix1 = pointOn( cx, cy, inner, aStartDeg );
    float[] ox2 = pointOn( cx, cy, outer, aStartDeg + aSweepDeg );
    float[] ix2 = pointOn( cx, cy, inner, aStartDeg + aSweepDeg );

    if( aRoundCaps ) {
      path.moveTo( ox1[0], ox1[1] );
      // Внешняя дуга
      path.addArc( cx - outer, cy - outer, outer * 2, outer * 2, aStartDeg, aSweepDeg );
      // Полукруг на конечном торце: от ox2 к ix2
      addCapSemicircle( path, ox2[0], ox2[1], ix2[0], ix2[1], half, aSweepDeg );
      // Внутренняя дуга обратно
      path.addArc( cx - inner, cy - inner, inner * 2, inner * 2, aStartDeg + aSweepDeg, -aSweepDeg );
      // Полукруг на начальном торце: от ix1 к ox1
      addCapSemicircle( path, ix1[0], ix1[1], ox1[0], ox1[1], half, aSweepDeg );
    }
    else {
      path.moveTo( ox1[0], ox1[1] );
      path.addArc( cx - outer, cy - outer, outer * 2, outer * 2, aStartDeg, aSweepDeg );
      path.lineTo( ix2[0], ix2[1] );
      path.addArc( cx - inner, cy - inner, inner * 2, inner * 2, aStartDeg + aSweepDeg, -aSweepDeg );
      path.close();
    }
    return path;
  }

  public static Path createBorderPath( Device aDevice, Path aPath, float aBorderThick ) {
    float[] pathBounds = new float[4];
    aPath.getBounds( pathBounds );
    float zoomFactor = (pathBounds[2] - 2 * aBorderThick) / pathBounds[2];
    PathData innerPathData = scale( aPath.getPathData(), 0, 0, zoomFactor, zoomFactor );
    innerPathData = shift( innerPathData, aBorderThick, aBorderThick );
    // Path innerPath = new Path( aDevice, innerPathData );
    Path result = new Path( aDevice, innerPathData );
    result.addPath( aPath );
    // innerPath.dispose();
    return result;
  }

  /**
   * Создаёт Path для границы прямоугольника со скруглёнными углами.
   *
   * @param device SWT Device (Display)
   * @param x X-координата левого верхнего угла
   * @param y Y-координата левого верхнего угла
   * @param width Ширина прямоугольника
   * @param height Высота прямоугольника
   * @param arcWidth Ширина дуги скругления угла
   * @param arcHeight Высота дуги скругления угла
   * @param borderWidth Толщина границы
   * @return Path, описывающий границу (нужно вызвать dispose() после использования)
   */
  public static Path createRoundedBorderPath( Device device, float x, float y, float width, float height,
      float arcWidth, float arcHeight, float borderWidth ) {

    Path path = new Path( device );

    // --- Внешний контур (по часовой стрелке) ---
    addRoundedRect( path, x, y, width, height, arcWidth, arcHeight );

    // --- Внутренний контур (против часовой стрелки — «вычитается» из заливки) ---
    float ix = x + borderWidth;
    float iy = y + borderWidth;
    float iw = width - 2 * borderWidth;
    float ih = height - 2 * borderWidth;

    // Радиус дуги уменьшается пропорционально толщине границы
    float iArcW = Math.max( 0, arcWidth - borderWidth );
    float iArcH = Math.max( 0, arcHeight - borderWidth );

    if( iw > 0 && ih > 0 ) {
      addRoundedRectReverse( path, ix, iy, iw, ih, iArcW, iArcH );
    }

    return path;
  }

  /**
   * Добавляет скруглённый прямоугольник в Path по часовой стрелке.
   */
  public static void addRoundedRect( Path path, float x, float y, float w, float h, float aw, float ah ) {

    float rx = aw / 2f;
    float ry = ah / 2f;

    path.moveTo( x + rx, y );
    path.lineTo( x + w - rx, y );
    path.quadTo( x + w, y, x + w, y + ry );
    path.lineTo( x + w, y + h - ry );
    path.quadTo( x + w, y + h, x + w - rx, y + h );
    path.lineTo( x + rx, y + h );
    path.quadTo( x, y + h, x, y + h - ry );
    path.lineTo( x, y + ry );
    path.quadTo( x, y, x + rx, y );
    path.close();
  }

  /**
   * Добавляет скруглённый прямоугольник в Path против часовой стрелки (для создания «дырки» при заливке
   * Path.WIND_EVEN_ODD или WIND_NON_ZERO).
   */
  public static void addRoundedRectReverse( Path path, float x, float y, float w, float h, float aw, float ah ) {

    float rx = aw / 2f;
    float ry = ah / 2f;

    path.moveTo( x + rx, y );
    path.quadTo( x, y, x, y + ry );
    path.lineTo( x, y + h - ry );
    path.quadTo( x, y + h, x + rx, y + h );
    path.lineTo( x + w - rx, y + h );
    path.quadTo( x + w, y + h, x + w, y + h - ry );
    path.lineTo( x + w, y + ry );
    path.quadTo( x + w, y, x + w - rx, y );
    path.close();
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private static void parseChildren( Element parent, IStringMapEdit<String> aResult ) {
    NodeList children = parent.getChildNodes();
    for( int i = 0; i < children.getLength(); i++ ) {
      Node node = children.item( i );
      if( node.getNodeType() != Node.ELEMENT_NODE ) {
        continue;
      }

      Element el = (Element)node;
      // убрать префикс пространства имен
      String tag = el.getLocalName() != null ? el.getLocalName() : el.getTagName().replaceAll( ".*:", "" ); //$NON-NLS-1$//$NON-NLS-2$
      switch( tag ) {
        case "path": //$NON-NLS-1$
          aResult.put( el.getAttribute( "id" ), el.getAttribute( "d" ) ); //$NON-NLS-1$//$NON-NLS-2$
          break;
        case "g": //$NON-NLS-1$
        case "svg": //$NON-NLS-1$
          // Группа или вложенный SVG — спускаемся глубже
          parseChildren( el, aResult );
          break;
        // default:
        // throw new TsNotAllEnumsUsedRtException();
      }
    }
  }

  /**
   * Точка на окружности радиуса R при математическом угле angleDeg. Экранная Y-ось смотрит вниз, поэтому y = cy -
   * R*sin(a).
   */
  private static float[] pointOn( float cx, float cy, float R, float angleDeg ) {
    double a = Math.toRadians( angleDeg );
    return new float[] { cx + (float)(R * Math.cos( a )), cy - (float)(R * Math.sin( a )) };
  }

  /**
   * Добавляет полукруг-торец между точками A=(fromX,fromY) и B=(toX,toY). A и B — концы диаметра полукруга (внешняя и
   * внутренняя точки торца). Полукруг выступает НАРУЖУ от кольца. Выбор направления обхода: Вектор A→B смотрит внутрь
   * (от внешней к внутренней точке). sweepDeg > 0 (CCW-контур) → полукруг идёт по часовой: arcAngle = -180 sweepDeg < 0
   * (CW-контур) → полукруг идёт против часовой: arcAngle = +180 Угол вектора A→B вычисляется в математической системе
   * (Y вверх): atan2 принимает экранный dy с инвертированным знаком.
   */
  private static void addCapSemicircle( Path path, float fromX, float fromY, float toX, float toY, float aRadius,
      float sweepDeg ) {
    float mx = (fromX + toX) / 2f;
    float my = (fromY + toY) / 2f;

    // Угол вектора A→B в математической системе координат (инвертируем Y)
    float phiMath = (float)Math.toDegrees( Math.atan2( -(toY - fromY), toX - fromX ) );

    float startAngle = phiMath + 180f;
    float arcAngle = sweepDeg > 0 ? 180f : -180f;

    path.addArc( mx - aRadius, my - aRadius, aRadius * 2, aRadius * 2, startAngle, arcAngle );
  }

  /**
   * Constructor. Instantions restrictions
   */
  private PathDataUtils() {
    // nop
  }
}

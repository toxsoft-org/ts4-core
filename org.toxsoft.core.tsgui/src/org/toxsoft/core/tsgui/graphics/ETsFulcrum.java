package org.toxsoft.core.tsgui.graphics;

import static org.toxsoft.core.tsgui.graphics.ITsResources.*;

import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Опорная точка (точка привяки) прямоугольника.
 * <p>
 * От слова Fulcrum - точка опоры рычага.
 *
 * @author hazard157
 */
@SuppressWarnings( { "nls", "javadoc" } )
public enum ETsFulcrum
    implements IStridable {

  CENTER( "Center", STR_D_ERP_CENTER, STR_N_ERP_CENTER ),

  LEFT_TOP( "LeftTop", STR_D_ERP_LEFT_TOP, STR_N_ERP_LEFT_TOP ),

  LEFT_BOTTOM( "LeftBottom", STR_D_ERP_LEFT_BOTTOM, STR_N_ERP_LEFT_BOTTOM ),

  LEFT_CENTER( "LeftCenter", STR_D_ERP_LEFT_CENTER, STR_N_ERP_LEFT_CENTER ),

  RIGHT_TOP( "RightTop", STR_D_ERP_RIGHT_TOP, STR_N_ERP_RIGHT_TOP ),

  RIGHT_BOTTOM( "RightBottom", STR_D_ERP_RIGHT_BOTTOM, STR_N_ERP_RIGHT_BOTTOM ),

  RIGHT_CENTER( "RightCenter", STR_D_ERP_RIGHT_CENTER, STR_N_ERP_RIGHT_CENTER ),

  TOP_CENTER( "TopCenter", STR_D_ERP_TOP_CENTER, STR_N_ERP_TOP_CENTER ),

  BOTTOM_CENTER( "BottomCenter", STR_D_ERP_BOTTOM_CENTER, STR_N_ERP_BOTTOM_CENTER );

  /**
   * Keeper ID.
   */
  public static final String KEEPER_ID = "TsFulcrum"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static IEntityKeeper<ETsFulcrum> KEEPER = new StridableEnumKeeper<>( ETsFulcrum.class );

  private final String id;
  private final String description;
  private final String name;

  ETsFulcrum( String aId, String aDescr, String aName ) {
    id = aId;
    description = aDescr;
    name = aName;
  }

  // --------------------------------------------------------------------------
  // Реализация интерфейса IStridable
  //

  @Override
  public String id() {
    return id;
  }

  @Override
  public String description() {
    return description;
  }

  @Override
  public String nmName() {
    return name;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Рассчитывает координаты левого верхнего угла прямоугольника указанной ширины и высоты при указанной точке привязки.
   *
   * @param aFulcrumX int - X координата точки привязки
   * @param aFulcrumY int - Y координата точки привязки
   * @param aWidth int - ширина прямоугольника
   * @param aHeight int - ширина прямоугольника
   * @return {@link ITsRectangle} - рассчитанный прямоугольник
   * @throws TsIllegalArgumentRtException ширина или высота < 0
   */
  public ITsRectangle calcRect( int aFulcrumX, int aFulcrumY, int aWidth, int aHeight ) {
    return new TsRectangle( calcSegmentX( aFulcrumX, aWidth ), calcSegmentY( aFulcrumY, aHeight ), aWidth, aHeight );
  }

  /**
   * Рассчитывает x координату левого конца горизонтального отрезка указанной длины при указанной точке привязки.
   *
   * @param aFulcrumX int - x координата точки привязки
   * @param aSegmentLength int - длина отрезка
   * @return int - x координата левого конца
   * @throws TsIllegalArgumentRtException длина отрезка < 0
   */
  public int calcSegmentX( int aFulcrumX, int aSegmentLength ) {
    TsIllegalArgumentRtException.checkTrue( aSegmentLength < 0 );
    int x = switch( this ) {
      case CENTER, BOTTOM_CENTER, TOP_CENTER -> aFulcrumX - aSegmentLength / 2;
      case LEFT_TOP, LEFT_CENTER, LEFT_BOTTOM -> aFulcrumX;
      case RIGHT_TOP, RIGHT_CENTER, RIGHT_BOTTOM -> aFulcrumX - aSegmentLength;
      default -> throw new TsNotAllEnumsUsedRtException();
    };
    return x;
  }

  /**
   * Рассчитывает y координату верхнего конца вертикального отрезка указанной длины при указанной точке привязки.
   *
   * @param aFulcrumY int - Y координата точки привязки
   * @param aSegmentLength int - длина отрезка
   * @return int - y координата верхнего конца
   * @throws TsIllegalArgumentRtException длина отрезка < 0
   */
  public int calcSegmentY( int aFulcrumY, int aSegmentLength ) {
    TsIllegalArgumentRtException.checkTrue( aSegmentLength < 0 );
    int y = switch( this ) {
      case CENTER, LEFT_CENTER, RIGHT_CENTER -> aFulcrumY - aSegmentLength / 2;
      case LEFT_TOP, RIGHT_TOP, TOP_CENTER -> aFulcrumY;
      case LEFT_BOTTOM, RIGHT_BOTTOM, BOTTOM_CENTER -> aFulcrumY - aSegmentLength;
      default -> throw new TsNotAllEnumsUsedRtException();
    };
    return y;
  }

  /**
   * Рассчитывает X координату прямогуольника в обрасти отображения.
   *
   * @param aCanvasWidth int - ширина области отображения
   * @param aRectWidth int - ширина прямоугольника
   * @return int - X координата левого верхнего угла
   */
  public int calcTopleftX( int aCanvasWidth, int aRectWidth ) {
    switch( this ) {
      case CENTER:
      case TOP_CENTER:
      case BOTTOM_CENTER:
        return (aCanvasWidth - aRectWidth) / 2;
      case LEFT_TOP:
      case LEFT_CENTER:
      case LEFT_BOTTOM:
        return 0;
      case RIGHT_TOP:
      case RIGHT_CENTER:
      case RIGHT_BOTTOM:
        return aCanvasWidth - aRectWidth;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  /**
   * Рассчитывает Y координату прямогуольника в обрасти отображения.
   *
   * @param aCanvasHeight int - высота области отображения
   * @param aRectHeight int - высота прямоугольника
   * @return int - Y координата левого верхнего угла
   */
  public int calcTopleftY( int aCanvasHeight, int aRectHeight ) {
    switch( this ) {
      case CENTER:
      case LEFT_CENTER:
      case RIGHT_CENTER:
        return (aCanvasHeight - aRectHeight) / 2;
      case LEFT_TOP:
      case RIGHT_TOP:
      case TOP_CENTER:
        return 0;
      case LEFT_BOTTOM:
      case RIGHT_BOTTOM:
      case BOTTOM_CENTER:
        return aCanvasHeight - aRectHeight;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  /**
   * Определяет, расположена ли точка привязки по левому краю прямоугольника.
   *
   * @return boolean - призак, что точка привязки находится слева
   */
  public boolean isLeft() {
    switch( this ) {
      case CENTER:
      case RIGHT_CENTER:
      case RIGHT_TOP:
      case TOP_CENTER:
      case RIGHT_BOTTOM:
      case BOTTOM_CENTER:
        return false;
      case LEFT_CENTER:
      case LEFT_TOP:
      case LEFT_BOTTOM:
        return true;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  /**
   * Определяет, расположена ли точка привязки по правому краю прямоугольника.
   *
   * @return boolean - призак, что точка привязки находится справа
   */
  public boolean isRight() {
    switch( this ) {
      case CENTER:
      case LEFT_CENTER:
      case LEFT_TOP:
      case TOP_CENTER:
      case LEFT_BOTTOM:
      case BOTTOM_CENTER:
        return false;
      case RIGHT_CENTER:
      case RIGHT_TOP:
      case RIGHT_BOTTOM:
        return true;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  /**
   * Определяет, расположена ли точка привязки по верхнему краю прямоугольника.
   *
   * @return boolean - призак, что точка привязки находится сверху
   */
  public boolean isTop() {
    switch( this ) {
      case CENTER:
      case LEFT_CENTER:
      case RIGHT_CENTER:
      case LEFT_BOTTOM:
      case RIGHT_BOTTOM:
      case BOTTOM_CENTER:
        return false;
      case LEFT_TOP:
      case RIGHT_TOP:
      case TOP_CENTER:
        return true;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  /**
   * Определяет, расположена ли точка привязки по нижнему краю прямоугольника.
   *
   * @return boolean - призак, что точка привязки находится снизу
   */
  public boolean isBottom() {
    switch( this ) {
      case CENTER:
      case LEFT_CENTER:
      case RIGHT_CENTER:
      case LEFT_TOP:
      case RIGHT_TOP:
      case TOP_CENTER:
        return false;
      case LEFT_BOTTOM:
      case RIGHT_BOTTOM:
      case BOTTOM_CENTER:
        return true;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  // ----------------------------------------------------------------------------------
  // Методы проверки
  //

  /**
   * Определяет, существует ли константа перечисления с заданным идентификатором.
   *
   * @param aId String - идентификатор искомой константы
   * @return boolean - признак существования константы <br>
   *         <b>true</b> - константа с заданным идентификатором существует;<br>
   *         <b>false</b> - неет константы с таким идентификатором.
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static boolean isItemById( String aId ) {
    return findByIdOrNull( aId ) != null;
  }

  /**
   * Определяет, существует ли константа перечисления с заданным описанием.
   *
   * @param aDescription String - описание искомой константы
   * @return boolean - признак существования константы <br>
   *         <b>true</b> - константа с заданным описанием существует;<br>
   *         <b>false</b> - неет константы с таким описанием.
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static boolean isItemByDescription( String aDescription ) {
    return findByDescriptionOrNull( aDescription ) != null;
  }

  /**
   * Определяет, существует ли константа перечисления с заданным именем.
   *
   * @param aName String - имя (название) искомой константы
   * @return boolean - признак существования константы <br>
   *         <b>true</b> - константа с заданным именем существует;<br>
   *         <b>false</b> - неет константы с таким именем.
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static boolean isItemByName( String aName ) {
    return findByNameOrNull( aName ) != null;
  }

  // ----------------------------------------------------------------------------------
  // Методы поиска
  //

  /**
   * Возвращает константу по идентификатору или null.
   *
   * @param aId String - идентификатор искомой константы
   * @return ETsRectFulcrum - найденная константа, или null если нет константы с таимк идентификатором
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static ETsFulcrum findByIdOrNull( String aId ) {
    TsNullArgumentRtException.checkNull( aId );
    for( ETsFulcrum item : values() ) {
      if( item.id.equals( aId ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Возвращает константу по идентификатору или выбрасывает исключение.
   *
   * @param aId String - идентификатор искомой константы
   * @return ETsRectFulcrum - найденная константа
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким идентификатором
   */
  public static ETsFulcrum findById( String aId ) {
    return TsItemNotFoundRtException.checkNull( findByIdOrNull( aId ) );
  }

  /**
   * Возвращает константу по описанию или null.
   *
   * @param aDescription String - описание искомой константы
   * @return ETsRectFulcrum - найденная константа, или null если нет константы с таким описанием
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static ETsFulcrum findByDescriptionOrNull( String aDescription ) {
    TsNullArgumentRtException.checkNull( aDescription );
    for( ETsFulcrum item : values() ) {
      if( item.description.equals( aDescription ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Возвращает константу по описанию или выбрасывает исключение.
   *
   * @param aDescription String - описание искомой константы
   * @return ETsRectFulcrum - найденная константа
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким описанием
   */
  public static ETsFulcrum findByDescription( String aDescription ) {
    return TsItemNotFoundRtException.checkNull( findByDescriptionOrNull( aDescription ) );
  }

  /**
   * Возвращает константу по имени или null.
   *
   * @param aName String - имя искомой константы
   * @return ETsRectFulcrum - найденная константа, или null если нет константы с таким именем
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static ETsFulcrum findByNameOrNull( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( ETsFulcrum item : values() ) {
      if( item.name.equals( aName ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Возвращает константу по имени или выбрасывает исключение.
   *
   * @param aName String - имя искомой константы
   * @return ETsRectFulcrum - найденная константа
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким именем
   */
  public static ETsFulcrum findByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByNameOrNull( aName ) );
  }

}

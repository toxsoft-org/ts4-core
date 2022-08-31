package org.toxsoft.core.tsgui.graphics;

import static org.toxsoft.core.tsgui.graphics.ITsResources.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.toxsoft.core.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.core.tslib.bricks.keeper.std.StridableEnumKeeper;
import org.toxsoft.core.tslib.bricks.strid.IStridable;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.valobj.TsValobjUtils;

/**
 * Тип выравнивания по горизонтали (влево, по центру, вправо).
 *
 * @author hazard157
 */
public enum EHorAlignment
    implements IStridable {

  /**
   * Выравнивание по горизонтали влево.
   */
  LEFT( "Left", STR_N_HA_LEFT, STR_D_HA_LEFT, SWT.LEFT ), //$NON-NLS-1$

  /**
   * Выравнивание по горизонтали по центру.
   */
  CENTER( "Center", STR_N_HA_CENTER, STR_D_HA_CENTER, SWT.CENTER ), //$NON-NLS-1$

  /**
   * Выравнивание по горизонтали по центру.
   */
  RIGHT( "Right", STR_N_HA_RIGHT, STR_D_HA_RIGHT, SWT.RIGHT ), //$NON-NLS-1$

  /**
   * Выравнивание по горизонтали на всю ширину.
   */
  FILL( "Fill", STR_N_HA_FILL, STR_D_HA_FILL, SWT.FILL ), //$NON-NLS-1$

  ;

  /**
   * Keeper ID for registration in {@link TsValobjUtils}.
   */
  public static final String KEEPER_ID = "HorAlignment"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static IEntityKeeper<EHorAlignment> KEEPER = new StridableEnumKeeper<>( EHorAlignment.class );

  private final String id;
  private final String name;
  private final String description;
  private final int    swtStyle;

  /**
   * Создать константу с заданием всех инвариантов.
   *
   * @param aId String - идентифицирующее название константы
   * @param aName String - short name
   * @param aDescr String - отображаемое описание константы
   * @param aSwtStyle int - SWT стиль
   */
  EHorAlignment( String aId, String aName, String aDescr, int aSwtStyle ) {
    id = aId;
    name = aName;
    description = aDescr;
    swtStyle = aSwtStyle;
  }

  // --------------------------------------------------------------------------
  // IStridable
  //

  @Override
  public String id() {
    return id;
  }

  @Override
  public String nmName() {
    return name;
  }

  @Override
  public String description() {
    return description;
  }

  // ------------------------------------------------------------------------------------
  // Публичное API
  //

  /**
   * Возвращает SWT-стиль, соответствующий данному выравниванию.
   * <p>
   * Метод возвращает одну из констант {@link SWT#LEFT} (она же имеет другое имя {@link SWT#LEAD}), {@link SWT#CENTER}
   * или {@link SWT#RIGHT} (она же имеет длругое имя {@link SWT#TRAIL}).
   *
   * @return int - SWT стиль выравнивания
   */
  public int swtStyle() {
    return swtStyle;
  }

  /**
   * Вычисляет Х координату левой точки отрезка шириной aWidth, распологаемой относительно точки привяки aX.
   *
   * @param aX int - X координата точки привязки
   * @param aWidth int - ширина отрезка, который нужно разместить в соответсвии с переданными параметрами
   * @return int - Х координата левой точки отрезка
   */
  public int computeX( int aX, int aWidth ) {
    switch( this ) {
      case LEFT:
        return aX;
      case CENTER:
        return aX - aWidth / 2;
      case RIGHT:
        return aX - aWidth;
      case FILL:
        return aX;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  /**
   * Вычисляет Х координату левой точки отрезка шириной aForegroundWidth, над отрезком шириной aBackgroundWidth.
   *
   * @param aBackgroundWidth int - ширина отрезка, относительно которого позиционируется отрезок aForegroundWidth
   * @param aForegroundWidth int - ширина отрезка, местоположение которого ищеться
   * @return int - X координата левого конца aForegroundWidth относительно левого конца aBackgroundWidth
   */
  public int relativeX( int aBackgroundWidth, int aForegroundWidth ) {
    switch( this ) {
      case LEFT:
        return 0;
      case CENTER:
        return (aBackgroundWidth - aForegroundWidth) / 2;
      case RIGHT:
        return aBackgroundWidth - aForegroundWidth;
      case FILL:
        return 0;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  // ----------------------------------------------------------------------------------
  // Методы проверки
  //

  /**
   * Определяет, существует ли константа с заданным идентификатором.
   *
   * @param aId String - идентификатор {@link #id()} константы
   * @return boolean - <b>true</b> - да, есть константа с таким идентификатором;<br>
   *         <b>false</b> - нет такой константы.
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static boolean isItemById( String aId ) {
    return findByIdOrNull( aId ) != null;
  }

  /**
   * Определяет, существует ли константа с заданным описанием.
   *
   * @param aDescription String - описание {@link #id()} константы
   * @return boolean - <b>true</b> - да, есть константа с таким описанием;<br>
   *         <b>false</b> - нет такой константы.
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static boolean isItemByDescription( String aDescription ) {
    return findByDescriptionOrNull( aDescription ) != null;
  }

  // ----------------------------------------------------------------------------------
  // Методы поиска
  //

  /**
   * Находит константу с заданным идентификатором, а если нет такой константы, возвращает null.
   *
   * @param aId String - идентификатор {@link #id()} константы
   * @return EAnchorHorPosition - найденная константа или null
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static EHorAlignment findByIdOrNull( String aId ) {
    TsNullArgumentRtException.checkNull( aId );
    for( EHorAlignment item : values() ) {
      if( item.id.equals( aId ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Находит константу с заданным идентификатором, а если нет такой константы, выбрасывает исключение.
   *
   * @param aId String - идентификатор {@link #id()} константы
   * @return EAnchorHorPosition - найденная константа или null
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким идентификатором
   */
  public static EHorAlignment findById( String aId ) {
    return TsItemNotFoundRtException.checkNull( findByIdOrNull( aId ) );
  }

  /**
   * Находит константу с заданным описанием, а если нет такой константы, возвращает null.
   *
   * @param aDescription String - описание {@link #description()} константы
   * @return EAnchorHorPosition - найденная константа или null
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static EHorAlignment findByDescriptionOrNull( String aDescription ) {
    TsNullArgumentRtException.checkNull( aDescription );
    for( EHorAlignment item : values() ) {
      if( item.description.equals( aDescription ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Находит константу с заданным описанием, а если нет такой константы, выбрасывает исключение.
   *
   * @param aDescription String - описание {@link #description()} константы
   * @return EAnchorHorPosition - найденная константа или null
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким описанием
   */
  public static EHorAlignment findByDescription( String aDescription ) {
    return TsItemNotFoundRtException.checkNull( findByDescriptionOrNull( aDescription ) );
  }

  /**
   * Находит константу по SWT-стилю выравнивания, а если аргумент неверный, возвращает null.
   * <p>
   * Из аргумента все ьиты кроме {@link SWT#LEFT}, {@link SWT#RIGHT} и {@link SWT#CENTER} игнорируются. То есть, можно
   * прямо передавать слово SWT-стиля так, как он испольуется например в конструкторе {@link Composite}.
   * <p>
   * <b>Внимание:</b>аргумент должен содержать <b>ровно один</b> выставленный бит из {@link SWT#LEFT}, {@link SWT#RIGHT}
   * или {@link SWT#CENTER}, иначе метод вернет null.
   *
   * @param aSwtStyle int - слово с одним высталенным битом из трех {@link SWT#LEFT}, {@link SWT#CENTER} или
   *          {@link SWT#RIGHT}
   * @return {@link EHorAlignment} - найденная константа или null
   */
  public static final EHorAlignment findBySwtStyleOrNull( int aSwtStyle ) {
    switch( aSwtStyle & (SWT.LEFT | SWT.CENTER | SWT.RIGHT) ) {
      case SWT.LEFT:
        // case SWT.LEAD:
        return LEFT;
      case SWT.CENTER:
        return CENTER;
      case SWT.RIGHT:
        // case SWT.TRAIL:
        return RIGHT;
      default:
        return null;
    }
  }

  /**
   * Находит константу по SWT-стилю выравнивания, а если аргумент неверный, выбрасывает исключения.
   * <p>
   * Из аргумента все ьиты кроме {@link SWT#LEFT}, {@link SWT#RIGHT} и {@link SWT#CENTER} игнорируются. То есть, можно
   * прямо передавать слово SWT-стиля так, как он испольуется например в конструкторе {@link Composite}.
   * <p>
   * <b>Внимание:</b>аргумент должен содержать <b>ровно один</b> выставленный бит из {@link SWT#LEFT}, {@link SWT#RIGHT}
   * или {@link SWT#CENTER}, иначе метод вернет выбросит искючение.
   *
   * @param aSwtStyle int - слово с одним высталенным битом из трех {@link SWT#LEFT}, {@link SWT#CENTER} или
   *          {@link SWT#RIGHT}
   * @return {@link EHorAlignment} - найденная константа
   * @throws TsIllegalArgumentRtException аргумент неверный
   */
  public static final EHorAlignment findBySwtStyle( int aSwtStyle ) {
    EHorAlignment result = findBySwtStyleOrNull( aSwtStyle );
    TsIllegalArgumentRtException.checkTrue( result == null );
    return result;
  }

}

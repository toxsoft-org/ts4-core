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
 * Тип выравнивания по вертикали (по верху, по центру, по низу).
 *
 * @author hazard157
 */
public enum EVerAlignment
    implements IStridable {

  /**
   * Выравнивание по вертикали по верху.
   */
  TOP( "Top", STR_N_VA_TOP, STR_D_VA_TOP, SWT.TOP ), //$NON-NLS-1$

  /**
   * Выравнивание по вертикали по центру.
   */
  CENTER( "Center", STR_N_VA_CENTER, STR_D_VA_CENTER, SWT.CENTER ), //$NON-NLS-1$

  /**
   * Выравнивание по вертикали по низу.
   */
  BOTTOM( "Bottom", STR_N_VA_BOTTOM, STR_D_VA_BOTTOM, SWT.BOTTOM ), //$NON-NLS-1$

  /**
   * Выравнивание по вертикали по низу.
   */
  FILL( "Fill", STR_N_VA_FILL, STR_D_VA_FILL, SWT.FILL ), //$NON-NLS-1$

  ;

  /**
   * Идентификатор регистрации хранителя {@link #KEEPER} в реестре {@link TsValobjUtils}.
   */
  public static final String KEEPER_ID = "VerAlignment"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static IEntityKeeper<EVerAlignment> KEEPER = new StridableEnumKeeper<>( EVerAlignment.class );

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
  EVerAlignment( String aId, String aName, String aDescr, int aSwtStyle ) {
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
   * Метод возвращает одну из констант {@link SWT#TOP} (она же имеет другое имя {@link SWT#UP}), {@link SWT#CENTER} или
   * {@link SWT#BOTTOM} (она же имеет другое имя {@link SWT#DOWN}).
   *
   * @return int - SWT стиль выравнивания
   */
  public int swtStyle() {
    return swtStyle;
  }

  /**
   * Вычисляет Y координату верхней точки отрезка высотой aHeight, распологаемой относительно точки привяки aX.
   *
   * @param aY int - Y координата точки привязки
   * @param aHeight int - высота отрезка, местоположение которого ищеться
   * @return int - Y координата левой точки отрезка
   */
  public int computeY( int aY, int aHeight ) {
    switch( this ) {
      case TOP:
        return aY;
      case CENTER:
        return aY - aHeight / 2;
      case BOTTOM:
        return aY - aHeight;
      case FILL:
        return aY;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  /**
   * Вычисляет X координату BOTTOM точки отрезка высотой aForegroundHeight, над отрезком высотой aBackgroundHeight.
   *
   * @param aBackgroundHeight int - высота отрезка, относительно которого позиционируется отрезок aForegroundHeight
   * @param aForegroundHeight int - высота отрезка, местоположение которого ищеться
   * @return int - Y координата левого конца aForegroundHeight относительно левого конца aBackgroundHeight
   */
  public int relativeY( int aBackgroundHeight, int aForegroundHeight ) {
    switch( this ) {
      case TOP:
        return 0;
      case CENTER:
        return (aBackgroundHeight - aForegroundHeight) / 2;
      case BOTTOM:
        return aBackgroundHeight - aForegroundHeight;
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
   * @return EAnchorVerPosition - найденная константа или null
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static EVerAlignment findByIdOrNull( String aId ) {
    TsNullArgumentRtException.checkNull( aId );
    for( EVerAlignment item : values() ) {
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
   * @return EAnchorVerPosition - найденная константа или null
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким идентификатором
   */
  public static EVerAlignment findById( String aId ) {
    return TsItemNotFoundRtException.checkNull( findByIdOrNull( aId ) );
  }

  /**
   * Находит константу с заданным описанием, а если нет такой константы, возвращает null.
   *
   * @param aDescription String - описание {@link #description()} константы
   * @return EAnchorVerPosition - найденная константа или null
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static EVerAlignment findByDescriptionOrNull( String aDescription ) {
    TsNullArgumentRtException.checkNull( aDescription );
    for( EVerAlignment item : values() ) {
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
   * @return EAnchorVerPosition - найденная константа или null
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким описанием
   */
  public static EVerAlignment findByDescription( String aDescription ) {
    return TsItemNotFoundRtException.checkNull( findByDescriptionOrNull( aDescription ) );
  }

  /**
   * Находит константу по SWT-стилю выравнивания, а если аргумент неверный, возвращает null.
   * <p>
   * Из аргумента все биты кроме {@link SWT#TOP}, {@link SWT#BOTTOM} и {@link SWT#CENTER} игнорируются. То есть, можно
   * прямо передавать слово SWT-стиля так, как он испольуется например в конструкторе {@link Composite}.
   * <p>
   * <b>Внимание:</b>аргумент должен содержать <b>ровно один</b> выставленный бит из {@link SWT#TOP}, {@link SWT#BOTTOM}
   * или {@link SWT#CENTER}, иначе метод вернет null.
   *
   * @param aSwtStyle int - слово с одним высталенным битом из трех {@link SWT#TOP}, {@link SWT#CENTER} или
   *          {@link SWT#BOTTOM}
   * @return {@link EVerAlignment} - найденная константа или null
   */
  public static final EVerAlignment findBySwtStyleOrNull( int aSwtStyle ) {
    switch( aSwtStyle & (SWT.TOP | SWT.CENTER | SWT.BOTTOM) ) {
      case SWT.TOP:
        // case SWT.UP
        return TOP;
      case SWT.CENTER:
        return CENTER;
      case SWT.BOTTOM:
        // case SWT.DOWN:
        return BOTTOM;
      default:
        return null;
    }
  }

  /**
   * Находит константу по SWT-стилю выравнивания, а если аргумент неверный, выбрасывает исключения.
   * <p>
   * Из аргумента все биты кроме {@link SWT#TOP}, {@link SWT#BOTTOM} и {@link SWT#CENTER} игнорируются. То есть, можно
   * прямо передавать слово SWT-стиля так, как он испольуется например в конструкторе {@link Composite}.
   * <p>
   * <b>Внимание:</b>аргумент должен содержать <b>ровно один</b> выставленный бит из {@link SWT#TOP}, {@link SWT#BOTTOM}
   * или {@link SWT#CENTER}, иначе метод вернет null.
   *
   * @param aSwtStyle int - слово с одним высталенным битом из трех {@link SWT#TOP}, {@link SWT#CENTER} или
   *          {@link SWT#BOTTOM}
   * @return {@link EVerAlignment} - найденная константа
   * @throws TsIllegalArgumentRtException аргумент неверный
   */
  public static final EVerAlignment findBySwtStyle( int aSwtStyle ) {
    EVerAlignment result = findBySwtStyleOrNull( aSwtStyle );
    TsIllegalArgumentRtException.checkTrue( result == null );
    return result;
  }

}

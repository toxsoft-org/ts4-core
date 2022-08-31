package org.toxsoft.core.tsgui.graphics.icons;

import static org.toxsoft.core.tsgui.graphics.icons.ITsResources.*;

import org.toxsoft.core.tslib.bricks.geometry.ITsPoint;
import org.toxsoft.core.tslib.bricks.geometry.impl.TsPoint;
import org.toxsoft.core.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.core.tslib.bricks.keeper.std.StridableEnumKeeper;
import org.toxsoft.core.tslib.bricks.strid.IStridable;
import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesList;
import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesListEdit;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.StridablesList;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Allowed icon sizes.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public enum EIconSize
    implements IStridable {

  IS_16X16( 16 ),

  IS_24X24( 24 ),

  IS_32X32( 32 ),

  IS_48X48( 48 ),

  IS_64X64( 64 ),

  IS_96X96( 96 ),

  IS_128X128( 128 );

  /**
   * Идентификатор регистрации хранителя {@link #KEEPER} в реестре {@link ValobjUtils}.
   */
  public static final String KEEPER_ID = "EIconSize"; //$NON-NLS-1$

  /**
   * Экземпляр-синглтон хранителя.
   */
  public static final IEntityKeeper<EIconSize> KEEPER = new StridableEnumKeeper<>( EIconSize.class );

  private static final String ID_PREFIX = "is"; //$NON-NLS-1$

  private static IStridablesListEdit<EIconSize> list = null;

  private final String   id;
  private final String   name;
  private final String   description;
  private final int      size;
  private final ITsPoint pointSize;

  private static EIconSize minSize = null;
  private static EIconSize maxSize = null;

  /**
   * Создает константу со всеми инвариантами.
   *
   * @param aSize int - размер грани значка в пикселях
   */
  EIconSize( int aSize ) {
    size = aSize;
    Integer intSize = Integer.valueOf( size );
    String s = String.format( "%dx%d", intSize, intSize ); //$NON-NLS-1$
    id = ID_PREFIX + s;
    name = String.format( FMT_N_ICON_SIZE, s );
    description = String.format( FMT_D_ICON_SIZE, s );
    pointSize = new TsPoint( size, size );
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

  // ----------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает размер значка в пикселях.
   *
   * @return int - размер значка в пикселях
   */
  public int size() {
    return size;
  }

  /**
   * Возвращает размер значка в виде {@link ITsPoint}.
   *
   * @return int - размер значка в виде {@link ITsPoint}
   */
  public ITsPoint pointSize() {
    return pointSize;
  }

  /**
   * Возвращает следующий (больший) размер, вплоть до максимального.
   *
   * @return {@link EIconSize} - следующий (больший) размер
   */
  public EIconSize nextSize() {
    if( isMaxSize() ) {
      return this;
    }
    return values()[ordinal() + 1];
  }

  /**
   * Возвращает следующий (больший) размер, с переходом на наименьший после наибольшего.
   *
   * @return {@link EIconSize} - следующий (больший) размер
   */
  public EIconSize nextSizeW() {
    if( isMaxSize() ) {
      return minSize();
    }
    return values()[ordinal() + 1];
  }

  /**
   * Возвращает предыдущий (меньший) размер, вплоть до минимаьного.
   *
   * @return {@link EIconSize} - предыдущий (меньший) размер
   */
  public EIconSize prevSize() {
    if( isMinSize() ) {
      return this;
    }
    return values()[ordinal() - 1];
  }

  /**
   * Возвращает предыдущий (меньший) размер, с переходом на максимальный после минимального.
   *
   * @return {@link EIconSize} - предыдущий (меньший) размер
   */
  public EIconSize prevSizeW() {
    if( isMinSize() ) {
      return maxSize();
    }
    return values()[ordinal() - 1];
  }

  /**
   * Определяет, является ли этот размер минимальным.
   *
   * @return boolean - признак минимального размера
   */
  public boolean isMinSize() {
    return this == minSize();
  }

  /**
   * Определяет, является ли этот размер максимальным.
   *
   * @return boolean - признак максимальным размера
   */
  public boolean isMaxSize() {
    return this == maxSize();
  }

  /**
   * Возвращает минимальный размер.
   *
   * @return {@link EIconSize} - минимальный размер
   */
  public static EIconSize minSize() {
    if( minSize == null ) {
      minSize = values()[0];
    }
    return minSize;
  }

  /**
   * Возвращает максмальный размер.
   *
   * @return {@link EIconSize} - максмальный размер
   */
  public static EIconSize maxSize() {
    if( maxSize == null ) {
      maxSize = values()[values().length - 1];
    }
    return maxSize;
  }

  /**
   * Находит наименший размер, обрамляющий прямоугольник указанного размера.
   * <p>
   * Если размеры больше {@link #maxSize()}, возвращает {@link #maxSize()}.
   *
   * @param aWidth int - ширина в пикселях
   * @param aHeight int - высота в пикселях
   * @return {@link EIconSize} - наименший размер, обрамляющий прямоугольник указанного размера
   * @throws TsIllegalArgumentRtException любой размер < 0
   */
  public static EIconSize findIncluding( int aWidth, int aHeight ) {
    TsIllegalArgumentRtException.checkTrue( aWidth < 0 || aHeight < 0 );
    int dim = Math.max( aWidth, aHeight );
    for( EIconSize sz : values() ) {
      if( sz.size >= dim ) {
        return sz;
      }
    }
    return maxSize();
  }

  /**
   * Возвращает все константы в виде списка.
   *
   * @return {@link IStridablesList}&lt; {@link EIconSize} &gt; - список всех констант
   */
  public static IStridablesList<EIconSize> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Определяет, существует ли константа перечисления с заданным идентификатором.
   *
   * @param aId String - идентификатор искомой константы
   * @return boolean - признак существования константы <br>
   *         <b>true</b> - константа с заданным идентификатором существует;<br>
   *         <b>false</b> - нет константы с таким идентификатором.
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   */
  public static boolean isItemById( String aId ) {
    return findById( aId ) != null;
  }

  /**
   * Находит константу по идентификатору.
   *
   * @param aId String - идентификатор искомой константы
   * @return EIconSize - найденная константа, или <code>null</code> если нет константы с таимк идентификатором
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   */
  public static EIconSize findById( String aId ) {
    return asList().findByKey( aId );
  }

  /**
   * Возвращает константу по идентификатору.
   *
   * @param aId String - идентификатор искомой константы
   * @return EIconSize - найденная константа
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   * @throws TsItemNotFoundRtException нет константы с таким идентификатором
   */
  public static EIconSize getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Определяет, существует ли константа перечисления с заданным именем.
   *
   * @param aName String - имя (название) искомой константы
   * @return boolean - признак существования константы <br>
   *         <b>true</b> - константа с заданным именем существует;<br>
   *         <b>false</b> - нет константы с таким именем.
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   */
  public static boolean isItemByName( String aName ) {
    return findByName( aName ) != null;
  }

  /**
   * Находит константу по имени.
   *
   * @param aName String - имя искомой константы
   * @return EIconSize - найденная константа, или <code>null</code> если нет константы с таким именем
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   */
  public static EIconSize findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EIconSize item : values() ) {
      if( item.name.equals( aName ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Возвращает константу по имени.
   *
   * @param aName String - имя искомой константы
   * @return EIconSize - найденная константа
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   * @throws TsItemNotFoundRtException нет константы с таким именем
   */
  public static EIconSize getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}

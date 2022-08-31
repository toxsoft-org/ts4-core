package org.toxsoft.core.tsgui.graphics.image;

import static org.toxsoft.core.tsgui.graphics.image.IThumbSizeConstants.*;
import static org.toxsoft.core.tsgui.graphics.image.ITsResources.*;

import org.toxsoft.core.tsgui.graphics.icons.EIconSize;
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
 * Image thumbnail size.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public enum EThumbSize
    implements IStridable {

  // constants must be arranged strictly according to the size increase

  SZ16( THUMB_SIZE_ID_016, 16 ),
  SZ24( THUMB_SIZE_ID_024, 24 ),
  SZ32( THUMB_SIZE_ID_032, 32 ),
  SZ48( THUMB_SIZE_ID_048, 48 ),
  SZ64( THUMB_SIZE_ID_064, 64 ),
  SZ96( THUMB_SIZE_ID_096, 96 ),
  SZ128( THUMB_SIZE_ID_128, 128 ),
  SZ180( THUMB_SIZE_ID_180, 180 ),
  SZ256( THUMB_SIZE_ID_256, 256 ),
  SZ360( THUMB_SIZE_ID_360, 360 ),
  SZ512( THUMB_SIZE_ID_512, 512 ),
  SZ724( THUMB_SIZE_ID_724, 724 ),
  SZ1024( THUMB_SIZE_ID_1024, 1024 ),

  ;

  /**
   * Keeper ID.
   */
  public static final String KEEPER_ID = "EThumbSize"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<EThumbSize> KEEPER = new StridableEnumKeeper<>( EThumbSize.class );

  private static IStridablesListEdit<EThumbSize> list = null;

  private final String   id;
  private final String   name;
  private final String   description;
  private final int      size;
  private final ITsPoint pointSize;

  // private final int height16_9;
  // private final int height16_10;
  // private final int height4_3;
  // private final int height5_4; // DVD 720x576

  /**
   * Создает константу со всеми инвариантами.
   *
   * @param aId String - идентифицирующее название константы
   * @param aSize int - размер значка в пикселях
   */
  EThumbSize( String aId, int aSize ) {
    id = aId;
    size = aSize;
    Integer sz = Integer.valueOf( size );
    name = String.format( FMT_N_THUMB_SIZE, sz, sz );
    description = String.format( FMT_D_THUMB_SIZE, sz, sz );
    pointSize = new TsPoint( aSize, aSize );
    // height16_10 = aSize * 10 / 16;
    // height16_9 = aSize * 9 / 16;
    // height4_3 = aSize * 3 / 4;
    // height5_4 = aSize * 4 / 5;
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
   * @return {@link EThumbSize} - следующий (больший) размер
   */
  public EThumbSize nextSize() {
    if( isMaxSize() ) {
      return this;
    }
    return values()[ordinal() + 1];
  }

  /**
   * Возвращает следующий (больший) размер, с переходом на наименьший после наибольшего.
   *
   * @return {@link EThumbSize} - следующий (больший) размер
   */
  public EThumbSize nextSizeW() {
    if( isMaxSize() ) {
      return minSize();
    }
    return values()[ordinal() + 1];
  }

  /**
   * Возвращает предыдущий (меньший) размер, вплоть до минимаьного.
   *
   * @return {@link EThumbSize} - предыдущий (меньший) размер
   */
  public EThumbSize prevSize() {
    if( isMinSize() ) {
      return this;
    }
    return values()[ordinal() - 1];
  }

  /**
   * Возвращает предыдущий (меньший) размер, с переходом на максимальный после минимального.
   *
   * @return {@link EThumbSize} - предыдущий (меньший) размер
   */
  public EThumbSize prevSizeW() {
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
    return ordinal() == 0;
  }

  /**
   * Определяет, является ли этот размер максимальным.
   *
   * @return boolean - признак максимальным размера
   */
  public boolean isMaxSize() {
    return ordinal() == values().length - 1;
  }

  /**
   * Возвращает минимальный размер.
   *
   * @return {@link EThumbSize} - минимальный размер
   */
  public static EThumbSize minSize() {
    return values()[0];
  }

  /**
   * Возвращает максмальный размер.
   *
   * @return {@link EThumbSize} - максмальный размер
   */
  public static EThumbSize maxSize() {
    return values()[values().length - 1];
  }

  // /**
  // * Возвращает высоту, соответствующей соотношению сторон 16:10.
  // *
  // * @return int - выстоа для соотношения сторон 16:10
  // */
  // public int getHeight16_10() {
  // return height16_10;
  // }
  //
  // /**
  // * Возвращает высоту, соответствующей соотношению сторон 16:9.
  // *
  // * @return int - выстоа для соотношения сторон 16:9
  // */
  // public int getHeight16_9() {
  // return height16_9;
  // }
  //
  // /**
  // * Возвращает высоту, соответствующей соотношению сторон 4:3.
  // *
  // * @return int - выстоа для соотношения сторон 4:3
  // */
  // public int getHeight4_3() {
  // return height4_3;
  // }
  //
  // /**
  // * Возвращает высоту, соответствующей соотношению сторон 5:4.
  // *
  // * @return int - выстоа для соотношения сторон 5:4
  // */
  // public int getHeight5_4() {
  // return height5_4;
  // }

  /**
   * Находит наименший размер, обрамляющий прямоугольник указанного размера.
   * <p>
   * Если размеры больше {@link #maxSize()}, возвращает {@link #maxSize()}.
   *
   * @param aWidth int - ширина в пикселях
   * @param aHeight int - высота в пикселях
   * @return {@link EThumbSize} - наименший размер, обрамляющий прямоугольник указанного размера
   * @throws TsIllegalArgumentRtException любой размер < 0
   */
  public static EThumbSize findIncluding( int aWidth, int aHeight ) {
    TsIllegalArgumentRtException.checkTrue( aWidth < 0 || aHeight < 0 );
    int dim = Math.max( aWidth, aHeight );
    for( EThumbSize sz : values() ) {
      if( sz.size >= dim ) {
        return sz;
      }
    }
    return maxSize();
  }

  /**
   * Находит наименший размер, обрамляющий квадрат размера значка.
   *
   * @param aIconSize {@link EIconSize} - размер значка
   * @return {@link EThumbSize} - наименший размер, обрамляющий значок указаного размера
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static EThumbSize findIncluding( EIconSize aIconSize ) {
    TsNullArgumentRtException.checkNull( aIconSize );
    return findIncluding( aIconSize.size(), aIconSize.size() );
  }

  /**
   * Возвращает все константы в виде списка.
   *
   * @return {@link IStridablesList}&lt; {@link EThumbSize} &gt; - список всех констант
   */
  public static IStridablesList<EThumbSize> asList() {
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
   * @return EThumbSize - найденная константа, или <code>null</code> если нет константы с таимк идентификатором
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   */
  public static EThumbSize findById( String aId ) {
    return asList().findByKey( aId );
  }

  /**
   * Возвращает константу по идентификатору.
   *
   * @param aId String - идентификатор искомой константы
   * @return EThumbSize - найденная константа
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   * @throws TsItemNotFoundRtException нет константы с таким идентификатором
   */
  public static EThumbSize getById( String aId ) {
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
   * @return EThumbSize - найденная константа, или <code>null</code> если нет константы с таким именем
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   */
  public static EThumbSize findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EThumbSize item : values() ) {
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
   * @return EThumbSize - найденная константа
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   * @throws TsItemNotFoundRtException нет константы с таким именем
   */
  public static EThumbSize getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}

package org.toxsoft.tslib.av.math;

import static org.toxsoft.tslib.av.math.ITsResources.*;

import org.toxsoft.tslib.av.EAtomicType;
import org.toxsoft.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.tslib.bricks.keeper.std.StridableEnumKeeper;
import org.toxsoft.tslib.bricks.strid.IStridable;
import org.toxsoft.tslib.bricks.strid.coll.IStridablesList;
import org.toxsoft.tslib.bricks.strid.coll.IStridablesListEdit;
import org.toxsoft.tslib.bricks.strid.coll.impl.StridablesList;
import org.toxsoft.tslib.utils.errors.*;
import org.toxsoft.tslib.utils.valobj.TsValobjUtils;

/**
 * Two atomic value comparison operation.
 * <p>
 * In fact this enum denotes ganaral binary operation with boolean result. In most cases this is comparison of two
 * values so <code>enum</code> is named {@link EAvCompareOp}.
 *
 * @author hazard157
 */
public enum EAvCompareOp
    implements IStridable {

  // TODO TRANSLATE

  /**
   * Сравнение на равенство.
   * <p>
   * Применимо ли для следующих примитивных типов: <br>
   * <tt> {@link EAtomicType#NONE}: <b>да</b><br>
   * {@link EAtomicType#BOOLEAN}: <b>да</b><br>
   * {@link EAtomicType#INTEGER}: <b>да</b><br>
   * {@link EAtomicType#FLOATING}: <b>да</b><br>
   * {@link EAtomicType#STRING}: <b>да</b><br>
   * {@link EAtomicType#TIMESTAMP}: <b>да</b><br>
   */
  EQ( "EQ", "==", STR_D_COMP_EQ ) { //$NON-NLS-1$//$NON-NLS-2$

    @Override
    public boolean compareInt( int aV1, int aV2 ) {
      return aV1 == aV2;
    }

    @Override
    public boolean compareLong( long aV1, long aV2 ) {
      return aV1 == aV2;
    }

    @Override
    public boolean compareFloat( float aV1, float aV2 ) {
      return Float.compare( aV1, aV2 ) == 0;
    }

    @Override
    public boolean compareDouble( double aV1, double aV2 ) {
      return Double.compare( aV1, aV2 ) == 0;
    }

    @Override
    public boolean compareString( String aV1, String aV2 ) {
      return aV1.equals( aV2 );
    }

  },

  /**
   * Сравнение на неравенство.
   * <p>
   * Применимо ли для следующих примитивных типов: <br>
   * <tt> {@link EAtomicType#NONE}: <b>да</b><br>
   * {@link EAtomicType#BOOLEAN}: <b>да</b><br>
   * {@link EAtomicType#INTEGER}: <b>да</b><br>
   * {@link EAtomicType#FLOATING}: <b>да</b><br>
   * {@link EAtomicType#STRING}: <b>да</b><br>
   * {@link EAtomicType#TIMESTAMP}: <b>да</b><br>
   */
  NE( "NE", "!=", STR_D_COMP_NE ) { //$NON-NLS-1$//$NON-NLS-2$

    @Override
    public boolean compareInt( int aV1, int aV2 ) {
      return aV1 != aV2;
    }

    @Override
    public boolean compareLong( long aV1, long aV2 ) {
      return aV1 != aV2;
    }

    @Override
    public boolean compareFloat( float aV1, float aV2 ) {
      return Float.compare( aV1, aV2 ) != 0;
    }

    @Override
    public boolean compareDouble( double aV1, double aV2 ) {
      return Double.compare( aV1, aV2 ) != 0;
    }

    @Override
    public boolean compareString( String aV1, String aV2 ) {
      return !aV1.equals( aV2 );
    }

  },

  /**
   * Сравнение на больше.
   * <p>
   * Применимо ли для следующих примитивных типов: <br>
   * <tt> {@link EAtomicType#NONE}: <b>нет</b><br>
   * {@link EAtomicType#BOOLEAN}: <b>нет</b><br>
   * {@link EAtomicType#INTEGER}: <b>да</b><br>
   * {@link EAtomicType#FLOATING}: <b>да</b><br>
   * {@link EAtomicType#STRING}: <b>да</b><br>
   * {@link EAtomicType#TIMESTAMP}: <b>да</b><br>
   */
  GT( "GT", ">", STR_D_COMP_GT ) { //$NON-NLS-1$//$NON-NLS-2$

    @Override
    public boolean compareInt( int aV1, int aV2 ) {
      return aV1 > aV2;
    }

    @Override
    public boolean compareLong( long aV1, long aV2 ) {
      return aV1 > aV2;
    }

    @Override
    public boolean compareFloat( float aV1, float aV2 ) {
      return Float.compare( aV1, aV2 ) > 0;
    }

    @Override
    public boolean compareDouble( double aV1, double aV2 ) {
      return Double.compare( aV1, aV2 ) > 0;
    }

    @Override
    public boolean compareString( String aV1, String aV2 ) {
      return aV1.compareTo( aV2 ) > 0;
    }

  },

  /**
   * Сравнение на больше или равно.
   * <p>
   * Применимо ли для следующих примитивных типов: <br>
   * <tt> {@link EAtomicType#NONE}: <b>нет</b><br>
   * {@link EAtomicType#BOOLEAN}: <b>нет</b><br>
   * {@link EAtomicType#INTEGER}: <b>да</b><br>
   * {@link EAtomicType#FLOATING}: <b>да</b><br>
   * {@link EAtomicType#STRING}: <b>да</b><br>
   * {@link EAtomicType#TIMESTAMP}: <b>да</b><br>
   */
  GE( "GE", ">=", STR_D_COMP_GE ) { //$NON-NLS-1$//$NON-NLS-2$

    @Override
    public boolean compareInt( int aV1, int aV2 ) {
      return aV1 >= aV2;
    }

    @Override
    public boolean compareLong( long aV1, long aV2 ) {
      return aV1 >= aV2;
    }

    @Override
    public boolean compareFloat( float aV1, float aV2 ) {
      return Float.compare( aV1, aV2 ) >= 0;
    }

    @Override
    public boolean compareDouble( double aV1, double aV2 ) {
      return Double.compare( aV1, aV2 ) >= 0;
    }

    @Override
    public boolean compareString( String aV1, String aV2 ) {
      return aV1.compareTo( aV2 ) >= 0;
    }

  },

  /**
   * Сравнение на меньше.
   * <p>
   * Применимо ли для следующих примитивных типов: <br>
   * <tt> {@link EAtomicType#NONE}: <b>нет</b><br>
   * {@link EAtomicType#BOOLEAN}: <b>нет</b><br>
   * {@link EAtomicType#INTEGER}: <b>да</b><br>
   * {@link EAtomicType#FLOATING}: <b>да</b><br>
   * {@link EAtomicType#STRING}: <b>да</b><br>
   * {@link EAtomicType#TIMESTAMP}: <b>да</b><br>
   */
  LT( "LT", "<", STR_D_COMP_LT ) { //$NON-NLS-1$//$NON-NLS-2$

    @Override
    public boolean compareInt( int aV1, int aV2 ) {
      return aV1 < aV2;
    }

    @Override
    public boolean compareLong( long aV1, long aV2 ) {
      return aV1 < aV2;
    }

    @Override
    public boolean compareFloat( float aV1, float aV2 ) {
      return Float.compare( aV1, aV2 ) < 0;
    }

    @Override
    public boolean compareDouble( double aV1, double aV2 ) {
      return Double.compare( aV1, aV2 ) < 0;
    }

    @Override
    public boolean compareString( String aV1, String aV2 ) {
      return aV1.compareTo( aV2 ) < 0;
    }

  },

  /**
   * Сравнение на меньше или равно.
   * <p>
   * Применимо ли для следующих примитивных типов: <br>
   * <tt> {@link EAtomicType#NONE}: <b>нет</b><br>
   * {@link EAtomicType#BOOLEAN}: <b>нет</b><br>
   * {@link EAtomicType#INTEGER}: <b>да</b><br>
   * {@link EAtomicType#FLOATING}: <b>да</b><br>
   * {@link EAtomicType#STRING}: <b>да</b><br>
   * {@link EAtomicType#TIMESTAMP}: <b>да</b><br>
   */
  LE( "LE", "<=", STR_D_COMP_LE ) { //$NON-NLS-1$//$NON-NLS-2$

    @Override
    public boolean compareInt( int aV1, int aV2 ) {
      return aV1 <= aV2;
    }

    @Override
    public boolean compareLong( long aV1, long aV2 ) {
      return aV1 <= aV2;
    }

    @Override
    public boolean compareFloat( float aV1, float aV2 ) {
      return Float.compare( aV1, aV2 ) <= 0;
    }

    @Override
    public boolean compareDouble( double aV1, double aV2 ) {
      return Double.compare( aV1, aV2 ) <= 0;
    }

    @Override
    public boolean compareString( String aV1, String aV2 ) {
      return aV1.compareTo( aV2 ) <= 0;
    }

  },

  /**
   * Проверка строки на совпадение с регулярным выражением.
   * <p>
   * Применимо ли для следующих примитивных типов: <br>
   * <tt> {@link EAtomicType#NONE}: <b>нет</b><br>
   * {@link EAtomicType#BOOLEAN}: <b>нет</b><br>
   * {@link EAtomicType#INTEGER}: <b>нет</b><br>
   * {@link EAtomicType#FLOATING}: <b>нет</b><br>
   * {@link EAtomicType#STRING}: <b>да</b><br>
   * {@link EAtomicType#TIMESTAMP}: <b>нет</b><br>
   */
  MATCH( "MATCH", "", STR_D_COMP_MATCH ) { //$NON-NLS-1$//$NON-NLS-2$

    @Override
    public boolean compareInt( int aV1, int aV2 ) {
      throw new TsUnsupportedFeatureRtException();
    }

    @Override
    public boolean compareLong( long aV1, long aV2 ) {
      throw new TsUnsupportedFeatureRtException();
    }

    @Override
    public boolean compareFloat( float aV1, float aV2 ) {
      throw new TsUnsupportedFeatureRtException();
    }

    @Override
    public boolean compareDouble( double aV1, double aV2 ) {
      throw new TsUnsupportedFeatureRtException();
    }

    @Override
    public boolean compareString( String aV1, String aV2 ) {
      return aV1.matches( aV2 );
    }

  };

  /**
   * Идентификатор регистрации хранителя {@link #KEEPER} в реестре {@link TsValobjUtils}.
   */
  public static final String KEEPER_ID = "EAvCompareOp"; //$NON-NLS-1$

  /**
   * Экземпляр-синглтон хранителя.
   */
  public static final IEntityKeeper<EAvCompareOp> KEEPER = new StridableEnumKeeper<>( EAvCompareOp.class );

  private static IStridablesListEdit<EAvCompareOp> list = null;

  private final String id;
  private final String name;
  private final String description;

  /**
   * Создает константу со всеми инвариантами.
   *
   * @param aId String - идентификатор (ИД-путь) константы
   * @param aName String - краткое удобочитаемое название константы
   * @param aDescription String - отображаемое описание константы
   */
  EAvCompareOp( String aId, String aName, String aDescription ) {
    id = aId;
    name = aName;
    description = aDescription;
  }

  // --------------------------------------------------------------------------
  // Реализация интерфейса IStridable
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
  // API сравнения простых типов
  //

  /**
   * Сравнивает два int.
   *
   * @param aV1 int - первый аргумент
   * @param aV2 int - второй аргумент
   * @return boolean - результат сравнения этой константой
   */
  public abstract boolean compareInt( int aV1, int aV2 );

  /**
   * Сравнивает два long.
   *
   * @param aV1 long - первый аргумент
   * @param aV2 long - второй аргумент
   * @return boolean - результат сравнения этой константой
   */
  public abstract boolean compareLong( long aV1, long aV2 );

  /**
   * Сравнивает два float.
   *
   * @param aV1 float - первый аргумент
   * @param aV2 float - второй аргумент
   * @return boolean - результат сравнения этой константой
   */
  public abstract boolean compareFloat( float aV1, float aV2 );

  /**
   * Сравнивает два double.
   *
   * @param aV1 double - первый аргумент
   * @param aV2 double - второй аргумент
   * @return boolean - результат сравнения этой константой
   */
  public abstract boolean compareDouble( double aV1, double aV2 );

  /**
   * Сравнивает два String.
   *
   * @param aV1 String - первый аргумент
   * @param aV2 String - второй аргумент
   * @return boolean - результат сравнения этой константой
   */
  public abstract boolean compareString( String aV1, String aV2 );

  // ----------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает все константы в виде списка.
   *
   * @return {@link IStridablesList}&lt; {@link EAvCompareOp} &gt; - список всех констант
   */
  public static IStridablesList<EAvCompareOp> asList() {
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
   * @return EAvCompareOp - найденная константа, или <code>null</code> если нет константы с таимк идентификатором
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   */
  public static EAvCompareOp findById( String aId ) {
    return asList().findByKey( aId );
  }

  /**
   * Возвращает константу по идентификатору.
   *
   * @param aId String - идентификатор искомой константы
   * @return EAvCompareOp - найденная константа
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   * @throws TsItemNotFoundRtException нет константы с таким идентификатором
   */
  public static EAvCompareOp getById( String aId ) {
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
   * @return EAvCompareOp - найденная константа, или <code>null</code> если нет константы с таким именем
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   */
  public static EAvCompareOp findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EAvCompareOp item : values() ) {
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
   * @return EAvCompareOp - найденная константа
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   * @throws TsItemNotFoundRtException нет константы с таким именем
   */
  public static EAvCompareOp getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}

package org.toxsoft.core.tslib.av.math;

import static org.toxsoft.core.tslib.av.math.ITsResources.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.valobj.*;

/**
 * Two atomic value comparison operation.
 * <p>
 * Actually this enum denotes general binary operation with boolean result. In most cases this is comparison of two
 * values so <code>enum</code> is named {@link EAvCompareOp}.
 *
 * @author hazard157
 */
public enum EAvCompareOp
    implements IStridable {

  // TODO TRANSLATE

  /**
   * Compares values for equality.
   * <p>
   * Applicability for the types: <br>
   * <tt> {@link EAtomicType#NONE}: <b>YES</b><br>
   * {@link EAtomicType#BOOLEAN}: <b>YES</b><br>
   * {@link EAtomicType#INTEGER}: <b>YES</b><br>
   * {@link EAtomicType#FLOATING}: <b>YES</b><br>
   * {@link EAtomicType#STRING}: <b>YES</b><br>
   * {@link EAtomicType#TIMESTAMP}: <b>YES</b><br>
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
   * Compares values for non-equaltiy.
   * <p>
   * Applicability for the types: <br>
   * <tt> {@link EAtomicType#NONE}: <b>YES</b><br>
   * {@link EAtomicType#BOOLEAN}: <b>YES</b><br>
   * {@link EAtomicType#INTEGER}: <b>YES</b><br>
   * {@link EAtomicType#FLOATING}: <b>YES</b><br>
   * {@link EAtomicType#STRING}: <b>YES</b><br>
   * {@link EAtomicType#TIMESTAMP}: <b>YES</b><br>
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
   * Compares values for greater than.
   * <p>
   * Applicability for the types: <br>
   * <tt> {@link EAtomicType#NONE}: <b>no</b><br>
   * {@link EAtomicType#BOOLEAN}: <b>no</b><br>
   * {@link EAtomicType#INTEGER}: <b>YES</b><br>
   * {@link EAtomicType#FLOATING}: <b>YES</b><br>
   * {@link EAtomicType#STRING}: <b>YES</b><br>
   * {@link EAtomicType#TIMESTAMP}: <b>YES</b><br>
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
   * Compares values for greater or equal.
   * <p>
   * Applicability for the types: <br>
   * <tt> {@link EAtomicType#NONE}: <b>no</b><br>
   * {@link EAtomicType#BOOLEAN}: <b>no</b><br>
   * {@link EAtomicType#INTEGER}: <b>YES</b><br>
   * {@link EAtomicType#FLOATING}: <b>YES</b><br>
   * {@link EAtomicType#STRING}: <b>YES</b><br>
   * {@link EAtomicType#TIMESTAMP}: <b>YES</b><br>
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
   * Compares values for less.
   * <p>
   * Applicability for the types: <br>
   * <tt> {@link EAtomicType#NONE}: <b>no</b><br>
   * {@link EAtomicType#BOOLEAN}: <b>no</b><br>
   * {@link EAtomicType#INTEGER}: <b>YES</b><br>
   * {@link EAtomicType#FLOATING}: <b>YES</b><br>
   * {@link EAtomicType#STRING}: <b>YES</b><br>
   * {@link EAtomicType#TIMESTAMP}: <b>YES</b><br>
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
   * Compares values for less or equal.
   * <p>
   * Applicability for the types: <br>
   * <tt> {@link EAtomicType#NONE}: <b>no</b><br>
   * {@link EAtomicType#BOOLEAN}: <b>no</b><br>
   * {@link EAtomicType#INTEGER}: <b>YES</b><br>
   * {@link EAtomicType#FLOATING}: <b>YES</b><br>
   * {@link EAtomicType#STRING}: <b>YES</b><br>
   * {@link EAtomicType#TIMESTAMP}: <b>YES</b><br>
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
   * Check the String against the regular expression.
   * <p>
   * Applicability for the types: <br>
   * <tt> {@link EAtomicType#NONE}: <b>no</b><br>
   * {@link EAtomicType#BOOLEAN}: <b>no</b><br>
   * {@link EAtomicType#INTEGER}: <b>no</b><br>
   * {@link EAtomicType#FLOATING}: <b>no</b><br>
   * {@link EAtomicType#STRING}: <b>YES</b><br>
   * {@link EAtomicType#TIMESTAMP}: <b>no</b><br>
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
  // API сравнения простых типов
  //

  /**
   * Comapres two values of type int.
   *
   * @param aV1 int - first argument
   * @param aV2 int - second argument
   * @return boolean - the comparison result
   */
  public abstract boolean compareInt( int aV1, int aV2 );

  /**
   * Comapres two values of type long.
   *
   * @param aV1 long - first argument
   * @param aV2 long - second argument
   * @return boolean - the comparison result
   */
  public abstract boolean compareLong( long aV1, long aV2 );

  /**
   * Comapres two values of type float.
   *
   * @param aV1 float - first argument
   * @param aV2 float - second argument
   * @return boolean - the comparison result
   */
  public abstract boolean compareFloat( float aV1, float aV2 );

  /**
   * Comapres two values of type double.
   *
   * @param aV1 double - first argument
   * @param aV2 double - second argument
   * @return boolean - the comparison result
   */
  public abstract boolean compareDouble( double aV1, double aV2 );

  /**
   * Comapres two values of type String.
   *
   * @param aV1 String - first argument
   * @param aV2 String - second argument
   * @return boolean - the comparison result
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

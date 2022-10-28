package org.toxsoft.core.tslib.av.impl;

import static org.toxsoft.core.tslib.av.impl.ITsResources.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import java.lang.reflect.*;
import java.util.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.errors.*;
import org.toxsoft.core.tslib.av.list.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.core.tslib.utils.valobj.*;

/**
 * {@link IDataDef} implementation.
 *
 * @author hazard157
 */
public final class DataDef
    implements IDataDef {

  private final String         id;
  private final EAtomicType    atomicType;
  private final IOptionSetEdit params;

  /**
   * The validator. <br>
   * If initialized to non-<code>null</code> value either in appropriate construcor or at first call to
   * {@link #validator()}.
   */
  private transient ITsValidator<IAtomicValue> validator = null;

  /**
   * The comparator. <br>
   * Is initialized to non-<code>null</code> value either in appropriate construcor or at first call to
   * {@link #comparator()}.
   */
  private transient Comparator<IAtomicValue> comparator = null;

  /**
   * Constructor.
   *
   * @param aId String - data identifier (IDpath)
   * @param aAtomicType {@link EAtomicType} - atomic type
   * @param aParams {@link IOptionSet} - values for {@link #params()}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException identifier is not an IDpath
   */
  public DataDef( String aId, EAtomicType aAtomicType, IOptionSet aParams ) {
    id = StridUtils.checkValidIdPath( aId );
    atomicType = TsNullArgumentRtException.checkNull( aAtomicType );
    params = new OptionSet( aParams );
  }

  /**
   * Package level constructor, stores <code>aParam</code> reference without creating defensive copy.
   *
   * @param aFoo int - foo argument for unique constructor signature
   * @param aId String - data identifier (IDpath)
   * @param aAtomicType {@link EAtomicType} - atomic type
   * @param aParams {@link IOptionSetEdit} - reference to be stored as {@link #params()}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException identifier is not an IDpath
   */
  DataDef( int aFoo, String aId, EAtomicType aAtomicType, IOptionSetEdit aParams ) {
    id = StridUtils.checkValidIdPath( aId );
    TsNullArgumentRtException.checkNulls( aAtomicType, aParams );
    atomicType = aAtomicType;
    params = aParams;
  }

  /**
   * Static constructor.
   *
   * @param aId String - data identifier (IDpath)
   * @param aAtomicType {@link EAtomicType} - atomic type
   * @param aIdsAndValues Object[] - parameters as id / value pairs array
   * @return {@link DataDef} - new instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException identifier is not an IDpath
   * @throws TsIllegalArgumentRtException number of elements in array is uneven
   * @throws ClassCastException argument types convention is violated
   * @see OptionSetUtils#createOpSet(Object...)
   */
  public static DataDef create( String aId, EAtomicType aAtomicType, Object... aIdsAndValues ) {
    IOptionSet p = OptionSetUtils.createOpSet( aIdsAndValues );
    return new DataDef( aId, aAtomicType, p );
  }

  /**
   * Static constructor with validator.
   *
   * @param aId String - data identifier (IDpath)
   * @param aAtomicType {@link EAtomicType} - atomic type
   * @param aValidator {@link ITsValidator} - the {@link #validator()} or <code>null</code> for default
   * @param aComparator {@link Comparator} - the {@link #comparator()} or <code>null</code> for default
   * @param aIdsAndValues Object[] - parameters as id / value pairs array
   * @return {@link DataDef} - new instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException identifier is not an IDpath
   * @throws TsIllegalArgumentRtException number of elements in array is uneven
   * @throws ClassCastException argument types convention is violated
   * @see OptionSetUtils#createOpSet(Object...)
   */
  public static DataDef create2( String aId, EAtomicType aAtomicType, ITsValidator<IAtomicValue> aValidator,
      Comparator<IAtomicValue> aComparator, Object... aIdsAndValues ) {
    IOptionSet p = OptionSetUtils.createOpSet( aIdsAndValues );
    DataDef dd = new DataDef( aId, aAtomicType, p );
    dd.validator = aValidator;
    dd.comparator = aComparator;
    return dd;
  }

  /**
   * Static constructor from {@link IDataType}.
   *
   * @param aId String - data identifier (IDpath)
   * @param aDataType {@link IDataType} - data type
   * @param aIdsAndValues Object[] - overriding and additional parameters as id / value pairs array
   * @return {@link DataDef} - new instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException identifier is not an IDpath
   * @throws TsIllegalArgumentRtException number of elements in array is uneven
   * @see OptionSetUtils#createOpSet(Object...)
   */
  public static DataDef create3( String aId, IDataType aDataType, Object... aIdsAndValues ) {
    TsNullArgumentRtException.checkNulls( aDataType );
    DataDef dd = new DataDef( aId, aDataType.atomicType(), aDataType.params() );
    IOptionSetEdit p = OptionSetUtils.createOpSet( aIdsAndValues );
    dd.params().addAll( p );
    return dd;
  }

  /**
   * Creates template based instance.
   *
   * @param aId String - data identifier (IDpath)
   * @param aSrc {@link IDataDef} - template
   * @param aParams {@link IOptionSet} - override parameters
   * @return {@link DataDef} - new instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException identifier is not an IDpath
   */
  public static DataDef createOverride1( String aId, IDataDef aSrc, IOptionSet aParams ) {
    TsNullArgumentRtException.checkNulls( aSrc, aParams );
    DataDef dd = new DataDef( aId, aSrc.atomicType(), aSrc.params() );
    dd.params().addAll( aParams );
    dd.validator = aSrc.validator();
    dd.comparator = aSrc.comparator();
    return dd;
  }

  /**
   * Creates template based instance.
   *
   * @param aId String - data identifier (IDpath)
   * @param aSrc {@link IDataDef} - template
   * @param aIdsAndValues Object[] - override parameters as id / value pairs array
   * @return {@link DataDef} - new instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException identifier is not an IDpath
   * @throws TsIllegalArgumentRtException number of elements in array is uneven
   * @throws ClassCastException argument types convention is violated
   * @see OptionSetUtils#createOpSet(Object...)
   */
  public static DataDef createOverride2( String aId, IDataDef aSrc, Object... aIdsAndValues ) {
    TsNullArgumentRtException.checkNull( aSrc );
    DataDef dd = new DataDef( aId, aSrc.atomicType(), aSrc.params() );
    IOptionSet p = OptionSetUtils.createOpSet( aIdsAndValues );
    dd.params().addAll( p );
    dd.validator = aSrc.validator();
    dd.comparator = aSrc.comparator();
    return dd;
  }

  /**
   * Creates data definition for Java <code>enum</code> class.
   * <p>
   * Created data definition will contain {@link IAvMetaConstants#TSID_ENUMERATION} option.
   * <p>
   * Note: keeper for <code>enum</code> class must be registered in {@link TsValobjUtils}.
   *
   * @param aId String - ID {@link IDataDef#id()}
   * @param aEnumClass {@link Class} - the <code>enum</code> class
   * @param aIdsAndValues Object[] - identifier / value pairs as for {@link OptionSetUtils#createOpSet(Object...)}
   * @return {@link DataDef} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException aEnumClass is not an {@link Enum}
   * @throws TsItemNotFoundRtException keeper for aEnumClass is ot registered in {@link TsValobjUtils}
   * @throws TsIllegalArgumentRtException enum does not contains any constants
   */
  public static DataDef createEnum( String aId, Class<?> aEnumClass, Object... aIdsAndValues ) {
    // check class is Enum<>
    TsNullArgumentRtException.checkNull( aEnumClass );
    TsIllegalArgumentRtException.checkFalse( Enum.class.isAssignableFrom( aEnumClass ) );
    // check if keeper is registered
    IEntityKeeper<?> keeper = TsValobjUtils.getKeeperByClass( aEnumClass );
    // check if enum contains at least one constant
    Object[] enumConsts = aEnumClass.getEnumConstants();
    TsIllegalArgumentRtException.checkTrue( enumConsts == null || enumConsts.length == 0 );
    // create and init DataDef
    DataDef dd = new DataDef( aId, EAtomicType.VALOBJ, OptionSetUtils.createOpSet( aIdsAndValues ) );
    IAvListEdit avList = new AvList( new ElemArrayList<>() );
    for( Object o : aEnumClass.getEnumConstants() ) {
      IAtomicValue v = AvUtils.avValobj( o );
      avList.add( v );
    }
    dd.params.setValobj( TSID_ENUMERATION, avList );
    dd.params.setStr( TSID_KEEPER_ID, TsValobjUtils.findIdByKeeper( keeper ) );
    return dd;
  }

  /**
   * Static constructor of boolean flag.
   *
   * @param aId String - data identifier (IDpath)
   * @param aDefVal boolean - default (initial) value of the flag
   * @param aIdsAndValues Object[] - parameters as id / value pairs array
   * @return {@link DataDef} - new instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException identifier is not an IDpath
   * @throws TsIllegalArgumentRtException number of elements in array is uneven
   * @throws ClassCastException argument types convention is violated
   * @see OptionSetUtils#createOpSet(Object...)
   */
  public static DataDef ofBoolFlag( String aId, boolean aDefVal, Object... aIdsAndValues ) {
    IOptionSetEdit p = OptionSetUtils.createOpSet( aIdsAndValues );
    p.setBool( TSID_DEFAULT_VALUE, aDefVal );
    return new DataDef( aId, EAtomicType.BOOLEAN, p );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  /**
   * Creates (if possible) validator instance from {@link IAvMetaConstants#TSID_VALIDATOR_CLASS} option.
   *
   * @return {@link ITsValidator} - created validator or {@link ITsValidator#PASS}
   */
  private ITsValidator<IAtomicValue> internalCreateValidatorFromParams() {
    @SuppressWarnings( "unchecked" )
    ITsValidator<IAtomicValue> v = loadClassInstance( TSID_VALIDATOR_CLASS, ITsValidator.class );
    if( v != null ) {
      return v;
    }
    return ITsValidator.PASS;
  }

  /**
   * Creates (if possible) comparator instance from {@link IAvMetaConstants#TSID_COMPARATOR_CLASS} option.
   *
   * @return {@link ITsValidator} - created validator or {@link ITsValidator#PASS}
   */
  private Comparator<IAtomicValue> internalCreateComparatorFromParams() {
    @SuppressWarnings( "unchecked" )
    Comparator<IAtomicValue> c = loadClassInstance( TSID_COMPARATOR_CLASS, Comparator.class );
    if( c != null ) {
      return c;
    }
    return AvUtils.DEFAULT_AV_COMPARATOR;
  }

  /**
   * Creates instance of class with name in option <code>aOptionId</code>.
   *
   * @param <T> - the expected type of instance
   * @param aOptionId String - ID of option in {@link #params()} with name of class
   * @param aClass Class&lt;T&gt; - the expected type of instance
   * @return &lt;T&gt; - created instance or <code>null</code> if instance can not be created
   */
  private <T> T loadClassInstance( String aOptionId, Class<T> aClass ) {
    String className = params.getStr( aOptionId, TsLibUtils.EMPTY_STRING );
    if( className.isBlank() ) {
      return null;
    }
    // check class exists
    Class<?> rawClass = null;
    try {
      rawClass = Class.forName( className );
    }
    catch( ClassNotFoundException ex ) {
      LoggerUtils.errorLogger().error( ex );
      return null;
    }
    // check class type
    if( !aClass.isAssignableFrom( rawClass ) ) {
      LoggerUtils.errorLogger().error( FMT_ERR_INV_DATA_DEF_HELPER_CLASS, className, aClass.getSimpleName() );
      return null;
    }
    @SuppressWarnings( "unchecked" )
    Class<T> clazz = (Class<T>)rawClass;
    // create instance with constructor ClassName( IDataDef )
    try {
      Constructor<T> constr1 = clazz.getConstructor( IDataDef.class );
      try {
        return constr1.newInstance( this );
      }
      catch( InstantiationException | IllegalAccessException | IllegalArgumentException
          | InvocationTargetException ex ) {
        LoggerUtils.errorLogger().error( ex, FMT_ERR_INV_DATADEF_CONSTRUCTOR, className );
        // try next way - for some reasons this constructor is invalid
      }
    }
    catch( @SuppressWarnings( "unused" ) NoSuchMethodException | SecurityException ex ) {
      // just ignore - no such constructor
    }
    // create instance with constructor ClassName( IDataType )
    try {
      Constructor<T> constr2 = clazz.getConstructor( IDataType.class );
      try {
        return constr2.newInstance( this );
      }
      catch( InstantiationException | IllegalAccessException | IllegalArgumentException
          | InvocationTargetException ex ) {
        LoggerUtils.errorLogger().error( ex, FMT_ERR_INV_DATATYPE_CONSTRUCTOR, className );
        // try next way - for some reasons this constructor is invalid
      }
    }
    catch( @SuppressWarnings( "unused" ) NoSuchMethodException | SecurityException ex ) {
      // just ignore - no such constructor
    }
    // create instance with constructor without args
    try {
      Constructor<T> constr3 = clazz.getConstructor();
      try {
        return constr3.newInstance();
      }
      catch( InstantiationException | IllegalAccessException | IllegalArgumentException
          | InvocationTargetException ex ) {
        LoggerUtils.errorLogger().error( ex, FMT_ERR_INV_EMPTY_CONSTRUCTOR, className );
        // try next way - for some reasons this constructor is invalid
      }
    }
    catch( NoSuchMethodException | SecurityException ex ) {
      LoggerUtils.errorLogger().error( ex, FMT_ERR_NO_SUITABLE_CONSTRUCTORS, className );
    }
    return null;
  }

  // ------------------------------------------------------------------------------------
  // IParameterized
  //

  @Override
  public IOptionSetEdit params() {
    return params;
  }

  // ------------------------------------------------------------------------------------
  // IStridable
  //

  @Override
  public String id() {
    return id;
  }

  @Override
  public String nmName() {
    return params().getStr( DDEF_NAME );
  }

  @Override
  public String description() {
    return params().getStr( DDEF_DESCRIPTION );
  }

  // ------------------------------------------------------------------------------------
  // IDataDef
  //

  @Override
  public EAtomicType atomicType() {
    return atomicType;
  }

  @Override
  public ITsValidator<IAtomicValue> validator() {
    if( validator == null ) {
      validator = internalCreateValidatorFromParams();
    }
    return validator;
  }

  @Override
  public Comparator<IAtomicValue> comparator() {
    if( comparator == null ) {
      comparator = internalCreateComparatorFromParams();
    }
    return comparator;
  }

  @Override
  public IAtomicValue getValue( IOptionSet aOps ) {
    TsNullArgumentRtException.checkNull( aOps );
    IAtomicValue av = aOps.findValue( id() );
    if( av == null && isMandatory() ) {
      throw new TsItemNotFoundRtException( FMT_ERR_NO_MANDATORY_OP, id() );
    }
    if( av == null || av == IAtomicValue.NULL ) {
      return defaultValue();
    }
    return av;
  }

  @Override
  public void setValue( IOptionSetEdit aOps, IAtomicValue aValue ) {
    TsNullArgumentRtException.checkNull( aOps );
    IAtomicValue value = aValue;
    if( value == null ) {
      value = IAtomicValue.NULL;
    }
    AvTypeCastRtException.checkCanAssign( atomicType(), aValue.atomicType() );
    aOps.setValue( id(), aValue );
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return id() + '(' + atomicType.id() + ')';
  }

  @Override
  public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    if( aThat.getClass() == this.getClass() ) {
      DataDef that = (DataDef)aThat;
      return atomicType() == that.atomicType() && params.equals( that.params );
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = INITIAL_HASH_CODE;
    result = PRIME * result + atomicType().hashCode();
    result = PRIME * result + params.hashCode();
    return result;
  }

}

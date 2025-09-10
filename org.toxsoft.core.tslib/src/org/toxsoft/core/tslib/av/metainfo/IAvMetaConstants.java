package org.toxsoft.core.tslib.av.metainfo;

import static org.toxsoft.core.tslib.ITsHardConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.ITsResources.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.av.validators.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.math.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Miscellaneous meta information constants.
 *
 * @author hazard157
 */
@SuppressWarnings( { "javadoc", "nls" } )
public interface IAvMetaConstants {

  // ------------------------------------------------------------------------------------
  // Meta options IDs

  String TSID_ID               = TS_ID + ".Id";
  String TSID_NAME             = TS_ID + ".Name";
  String TSID_DESCRIPTION      = TS_ID + ".Description";
  String TSID_DEFAULT_VALUE    = TS_ID + ".DefaultValue";
  String TSID_MAX_INCLUSIVE    = TS_ID + ".MaxInclusive";
  String TSID_MIN_INCLUSIVE    = TS_ID + ".MinInclusive";
  String TSID_MAX_EXCLUSIVE    = TS_ID + ".MaxExclusive";
  String TSID_MIN_EXCLUSIVE    = TS_ID + ".MinExclusive";
  String TSID_IS_NAN_ALLOWED   = TS_ID + ".IsNanAllowed";
  String TSID_IS_INF_ALLOWED   = TS_ID + ".IsInfAllowed";
  String TSID_FORMAT_STRING    = TS_ID + ".FormatString";
  String TSID_IS_NULL_ALLOWED  = TS_ID + ".IsNullAllowed";
  String TSID_STRING_MASK      = TS_ID + ".StringMask";
  String TSID_MAX_LENGTH       = TS_ID + ".MaxLength";
  String TSID_MIN_LENGTH       = TS_ID + ".MinLength";
  String TSID_KEEPER_ID        = TS_ID + ".KeeperId";
  String TSID_ICON_ID          = TS_ID + ".IconId";          // holds String iconId
  String TSID_IMAGE_DESCR      = TS_ID + ".ImageDescr";      // holds VALOBJ TsImageDescriptor
  String TSID_IS_MANDATORY     = TS_ID + ".IsMandatory";
  String TSID_IS_READ_ONLY     = TS_ID + ".IsReadOnly";      // hint: not human/GUI editable
  String TSID_VALIDATOR_CLASS  = TS_ID + ".ValidatorClass";  // stores ITsValidator implementing class name
  String TSID_COMPARATOR_CLASS = TS_ID + ".ComparatorClass"; // stores ITsComparator implementing class name
  String TSID_ENUMERATION      = TS_ID + ".Enumeration";     // stores IAvList

  // ------------------------------------------------------------------------------------
  // common default values

  String       DEFAULT_ID_STR         = IStridable.NONE_ID;
  IAtomicValue DEFAULT_ID_AV          = avStr( DEFAULT_ID_STR );
  String       DEFAULT_NAME           = STR_DEF_NAME;
  IAtomicValue DEFAULT_NAME_AV        = avStr( STR_DEF_NAME );
  String       DEFAULT_DESCRIPTION    = TsLibUtils.EMPTY_STRING;
  IAtomicValue DEFAULT_DESCRIPTION_AV = avStr( DEFAULT_DESCRIPTION );
  String       FMT_BOOL_CHECK         = "%Б[-|✔]";                   // BOOLEAN as '-' or '✔'
  IAtomicValue FMT_BOOL_CHECK_AV      = avStr( FMT_BOOL_CHECK );

  // ------------------------------------------------------------------------------------
  // Builtin data type IDs

  String DDID_PREFIX  = TS_ID + ".dd";
  String DDID_IDNAME  = DDID_PREFIX + ".idname";
  String DDID_IDPATH  = DDID_PREFIX + ".idpath";
  String DDID_TS_BOOL = DDID_PREFIX + ".tsbool";

  // ------------------------------------------------------------------------------------
  // Builtin data types

  IDataType DT_NONE = DataType.create( NONE, //
      TSID_DEFAULT_VALUE, NONE.defaultValue() //
  );

  @Deprecated( forRemoval = true )
  IDataDef DDEF_NONE = DataDef.create( DDID_NONE, NONE, //
      TSID_ID, NONE.id(), //
      TSID_NAME, NONE.nmName(), //
      TSID_DESCRIPTION, NONE.description(), //
      TSID_DEFAULT_VALUE, NONE.defaultValue() //
  );

  IDataType DT_BOOLEAN = DataType.create( BOOLEAN, //
      TSID_DEFAULT_VALUE, BOOLEAN.defaultValue() //
  );

  /**
   * Same as {@link #DT_BOOLEAN} with default formatting {@link #FMT_BOOL_CHECK}.
   */
  IDataType DT_TS_BOOL = DataType.create( BOOLEAN, //
      TSID_FORMAT_STRING, FMT_BOOL_CHECK, //
      TSID_DEFAULT_VALUE, AV_FALSE //
  );

  @Deprecated( forRemoval = true )
  IDataDef DDEF_BOOLEAN = DataDef.create( DDID_BOOLEAN, BOOLEAN, //
      TSID_ID, BOOLEAN.id(), //
      TSID_NAME, BOOLEAN.nmName(), //
      TSID_DESCRIPTION, BOOLEAN.description(), //
      TSID_DEFAULT_VALUE, BOOLEAN.defaultValue() //
  );

  IDataType DT_INTEGER = DataType.create( INTEGER, //
      TSID_DEFAULT_VALUE, INTEGER.defaultValue() //
  );

  @Deprecated( forRemoval = true )
  IDataDef DDEF_INTEGER = DataDef.create( DDID_INTEGER, INTEGER, //
      TSID_ID, INTEGER.id(), //
      TSID_NAME, INTEGER.nmName(), //
      TSID_DESCRIPTION, INTEGER.description(), //
      TSID_DEFAULT_VALUE, INTEGER.defaultValue() //
  );

  IDataType DT_FLOATING = DataType.create( FLOATING, //
      TSID_DEFAULT_VALUE, FLOATING.defaultValue() //
  );

  @Deprecated( forRemoval = true )
  IDataDef DDEF_FLOATING = DataDef.create( DDID_FLOATING, FLOATING, //
      TSID_ID, FLOATING.id(), //
      TSID_NAME, FLOATING.nmName(), //
      TSID_DESCRIPTION, FLOATING.description(), //
      TSID_DEFAULT_VALUE, FLOATING.defaultValue() //
  );

  IDataType DT_TIMESTAMP = DataType.create( TIMESTAMP, //
      TSID_FORMAT_STRING, "%tF %tT", //$NON-NLS-1$
      TSID_DEFAULT_VALUE, TIMESTAMP.defaultValue() //
  );

  @Deprecated( forRemoval = true )
  IDataDef DDEF_TIMESTAMP = DataDef.create( DDID_TIMESTAMP, TIMESTAMP, //
      TSID_ID, TIMESTAMP.id(), //
      TSID_NAME, TIMESTAMP.nmName(), //
      TSID_DESCRIPTION, TIMESTAMP.description(), //
      TSID_FORMAT_STRING, "%tF %tT", //$NON-NLS-1$
      TSID_DEFAULT_VALUE, TIMESTAMP.defaultValue() //
  );

  IDataType DT_STRING = DataType.create( STRING, //
      TSID_DEFAULT_VALUE, STRING.defaultValue() //
  );

  @Deprecated( forRemoval = true )
  IDataDef DDEF_STRING = DataDef.create( DDID_STRING, STRING, //
      TSID_ID, STRING.id(), //
      TSID_NAME, STRING.nmName(), //
      TSID_DESCRIPTION, STRING.description(), //
      TSID_DEFAULT_VALUE, STRING.defaultValue() //
  );

  IDataType DT_VALOBJ = DataType.create( VALOBJ, //
      TSID_DEFAULT_VALUE, VALOBJ.defaultValue() //
  );

  @Deprecated( forRemoval = true )
  IDataDef DDEF_VALOBJ = DataDef.create( DDID_VALOBJ, VALOBJ, //
      TSID_ID, VALOBJ.id(), //
      TSID_NAME, VALOBJ.nmName(), //
      TSID_DESCRIPTION, VALOBJ.description(), //
      TSID_DEFAULT_VALUE, VALOBJ.defaultValue() //
  );

  // ------------------------------------------------------------------------------------
  // Data definitions for constraints

  IDataDef DDEF_NAME = DataDef.create2( TSID_NAME, STRING, NameStringAvValidator.VALIDATOR, null, //
      TSID_NAME, STR_NAME, //
      TSID_DESCRIPTION, STR_NAME_D, //
      TSID_IS_NULL_ALLOWED, AV_TRUE, //
      TSID_DEFAULT_VALUE, DEFAULT_NAME_AV //
  );

  IDataDef DDEF_DESCRIPTION = DataDef.create( TSID_DESCRIPTION, STRING, //
      TSID_NAME, STR_DESCRIPTION, //
      TSID_DESCRIPTION, STR_DESCRIPTION_D, //
      TSID_IS_NULL_ALLOWED, AV_TRUE, //
      TSID_DEFAULT_VALUE, AV_STR_EMPTY //
  );

  IDataDef DDEF_DEFAULT_VALUE = DataDef.create( TSID_DEFAULT_VALUE, BOOLEAN, //
      TSID_NAME, STR_DEFAULT_VALUE, //
      TSID_DESCRIPTION, STR_DEFAULT_VALUE_D, //
      TSID_IS_NULL_ALLOWED, AV_TRUE, //
      TSID_DEFAULT_VALUE, IAtomicValue.NULL //
  );

  IDataDef DDEF_IDNAME = DataDef.create2( DDID_IDNAME, STRING, IdNameStringAvValidator.IDNAME_VALIDATOR, null, //
      TSID_NAME, STR_IDNAME, //
      TSID_DESCRIPTION, STR_IDNAME_D, //
      TSID_IS_NULL_ALLOWED, AV_FALSE, //
      TSID_DEFAULT_VALUE, DEFAULT_ID_AV //
  );

  IDataDef DDEF_IDPATH = DataDef.create2( DDID_IDPATH, STRING, IdPathStringAvValidator.IDPATH_VALIDATOR, null, //
      TSID_NAME, STR_IDPATH, //
      TSID_DESCRIPTION, STR_IDPATH_D, //
      TSID_IS_NULL_ALLOWED, AV_FALSE, //
      TSID_DEFAULT_VALUE, DEFAULT_ID_AV //
  );

  IDataDef DDEF_TS_BOOL = DataDef.create( DDID_TS_BOOL, BOOLEAN, //
      TSID_NAME, STR_TS_BOOL, //
      TSID_DESCRIPTION, STR_TS_BOOL_D, //
      TSID_FORMAT_STRING, FMT_BOOL_CHECK, //
      TSID_DEFAULT_VALUE, AV_FALSE //
  );

  IDataDef DDEF_ICON_ID = DataDef.create( TSID_ICON_ID, STRING, //
      TSID_NAME, STR_ICON_ID, //
      TSID_DESCRIPTION, STR_ICON_ID_D, //
      TSID_IS_NULL_ALLOWED, AV_TRUE, //
      TSID_DEFAULT_VALUE, DEFAULT_ID_AV //
  );

  // IDataDef DDEF_SKID = create( TSID_SKID, VALOBJ, //
  // TSID_NAME, "", ///
  // TSID_DESCRIPTION, "", ///
  // TSID_IS_NULL_ALLOWED, AV_TRUE, //
  // TSID_DEFAULT_VALUE, DEFAULT_ID_AV //
  // );

  IStridablesList<IDataDef> ALL_DDEFS = new StridablesList<>( //
      DDEF_NONE, //
      DDEF_BOOLEAN, //
      DDEF_INTEGER, //
      DDEF_FLOATING, //
      DDEF_TIMESTAMP, //
      DDEF_STRING, //
      DDEF_VALOBJ, //
      DDEF_NAME, //
      DDEF_DESCRIPTION, //
      DDEF_DEFAULT_VALUE, //
      DDEF_IDNAME, //
      DDEF_IDPATH, //
      DDEF_TS_BOOL, //
      DDEF_ICON_ID //
  );

  // ------------------------------------------------------------------------------------
  // Built-in data types
  //

  IDataType DT_LOCAL_DATE = DataType.create( VALOBJ, //
      TSID_NAME, STR_LOCAL_DATE, //
      TSID_DESCRIPTION, STR_LOCAL_DATE_D, //
      TSID_KEEPER_ID, LocalDateKeeper.KEEPER_ID //
  );

  IDataType DT_LOCAL_TIME = DataType.create( VALOBJ, //
      TSID_NAME, STR_LOCAL_TIME, //
      TSID_DESCRIPTION, STR_LOCAL_TIME_D, //
      TSID_KEEPER_ID, LocalTimeKeeper.KEEPER_ID //
  );

  IDataType DT_LOCAL_DATE_TIME = DataType.create( VALOBJ, //
      TSID_NAME, STR_LOCAL_DATE_TIME, //
      TSID_DESCRIPTION, STR_LOCAL_DATE_TIME_D, //
      TSID_KEEPER_ID, LocalDateTimeKeeper.KEEPER_ID //
  );

  IDataType DT_AV_ENUM = DataType.create( VALOBJ, //
      TSID_NAME, STR_AV_ENUM, //
      TSID_DESCRIPTION, STR_AV_ENUM_D //
  );

  IDataType DT_SKID = DataType.create( VALOBJ, //
      TSID_NAME, STR_DT_SKID, //
      TSID_DESCRIPTION, STR_DT_SKID_D, //
      TSID_KEEPER_ID, Skid.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( Skid.NONE ) //
  );

  IDataType DT_SKID_LIST = DataType.create( VALOBJ, //
      TSID_NAME, STR_DT_SKID_LIST, //
      TSID_DESCRIPTION, STR_DT_SKID_LIST_D, //
      TSID_KEEPER_ID, SkidListKeeper.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( ISkidList.EMPTY ) //
  );

  IDataType DT_GWID = DataType.create( VALOBJ, //
      TSID_NAME, STR_DT_GWID, //
      TSID_DESCRIPTION, STR_DT_GWID_D, //
      TSID_KEEPER_ID, Gwid.KEEPER_ID, //
      TSID_DEFAULT_VALUE, IAtomicValue.NULL //
  );

  IDataType DT_GWID_LIST = DataType.create( VALOBJ, //
      TSID_NAME, STR_DT_GWID_LIST, //
      TSID_DESCRIPTION, STR_DT_GWID_LIST_D, //
      TSID_KEEPER_ID, GwidList.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( IGwidList.EMPTY ) //
  );

  IDataType DT_OPTION_SET = DataType.create( VALOBJ, //
      TSID_NAME, STR_DT_OPTION_SET, //
      TSID_DESCRIPTION, STR_DT_OPTION_SET_D, //
      TSID_KEEPER_ID, OptionSetKeeper.KEEPER_ID, //
      TSID_DEFAULT_VALUE, OptionSetKeeper.AV_EMPTY_OPSET //
  );

  // ------------------------------------------------------------------------------------
  // helper methods
  //

  /**
   * Creates {@link IntRange} from the constraints.
   * <p>
   * This methods defines official strategy to use {@link #TSID_MIN_INCLUSIVE}, {@link #TSID_MIN_EXCLUSIVE},
   * {@link #TSID_MAX_EXCLUSIVE}, {@link #TSID_MAX_INCLUSIVE} constants. INCLUSIVE constants have precedence over
   * EXCLUSIVE constants.
   * <p>
   * If any of MIN or MAX limit is not defined, {@link Integer#MIN_VALUE} and {@link Integer#MAX_VALUE} will be used
   * respectively. If constraints are invalid (MIN > MAX) then {@link IntRange#FULL} will return.
   *
   * @param aConstraints {@link IOptionSet} - the set of constraints
   * @return {@link IntRange} - created range
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  static IntRange makeIntRangeFromConstraints( IOptionSet aConstraints ) {
    TsNullArgumentRtException.checkNull( aConstraints );
    int minValue = Integer.MIN_VALUE;
    if( aConstraints.hasValue( TSID_MIN_INCLUSIVE ) ) {
      minValue = aConstraints.getInt( TSID_MIN_INCLUSIVE );
    }
    else {
      if( aConstraints.hasValue( TSID_MIN_EXCLUSIVE ) ) {
        minValue = aConstraints.getInt( TSID_MIN_EXCLUSIVE ) + 1;
      }
    }
    int maxValue = Integer.MAX_VALUE;
    if( aConstraints.hasValue( TSID_MAX_INCLUSIVE ) ) {
      maxValue = aConstraints.getInt( TSID_MAX_INCLUSIVE );
    }
    else {
      if( aConstraints.hasValue( TSID_MAX_EXCLUSIVE ) ) {
        maxValue = aConstraints.getInt( TSID_MAX_EXCLUSIVE ) - 1;
      }
    }
    if( minValue <= maxValue ) {
      return new IntRange( minValue, maxValue );
    }
    return IntRange.FULL;
  }

  /**
   * Creates {@link LongRange} from the constraints.
   * <p>
   * Behaves like {@link #makeIntRangeFromConstraints(IOptionSet)} but for <code>long</code> values.
   *
   * @param aConstraints {@link IOptionSet} - the set of constraints
   * @return {@link LongRange} - created range
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  static LongRange makeLongRangeFromConstraints( IOptionSet aConstraints ) {
    TsNullArgumentRtException.checkNull( aConstraints );
    long minValue = Long.MIN_VALUE;
    if( aConstraints.hasValue( TSID_MIN_INCLUSIVE ) ) {
      minValue = aConstraints.getLong( TSID_MIN_INCLUSIVE );
    }
    else {
      if( aConstraints.hasValue( TSID_MIN_EXCLUSIVE ) ) {
        minValue = aConstraints.getLong( TSID_MIN_EXCLUSIVE ) + 1;
      }
    }
    long maxValue = Long.MAX_VALUE;
    if( aConstraints.hasValue( TSID_MAX_INCLUSIVE ) ) {
      maxValue = aConstraints.getLong( TSID_MAX_INCLUSIVE );
    }
    else {
      if( aConstraints.hasValue( TSID_MAX_EXCLUSIVE ) ) {
        maxValue = aConstraints.getLong( TSID_MAX_EXCLUSIVE ) - 1;
      }
    }
    if( minValue <= maxValue ) {
      return new LongRange( minValue, maxValue );
    }
    return LongRange.FULL;
  }

  /**
   * Creates {@link DoubleRange} from the constraints.
   * <p>
   * This methods defines official strategy to use {@link #TSID_MIN_INCLUSIVE}, {@link #TSID_MIN_EXCLUSIVE},
   * {@link #TSID_MAX_EXCLUSIVE}, {@link #TSID_MAX_INCLUSIVE} constants. INCLUSIVE constants have precedence over
   * EXCLUSIVE constants.
   * <p>
   * If any of MIN or MAX limit is not defined, {@link Double#MIN_VALUE} and {@link Double#MAX_VALUE} will be used
   * respectively. If constraints are invalid (MIN > MAX) then {@link IntRange#FULL} will return.
   *
   * @param aConstraints {@link IOptionSet} - the set of constraints
   * @return {@link DoubleRange} - created range
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  static DoubleRange makeDoubleRangeFromConstraints( IOptionSet aConstraints ) {
    TsNullArgumentRtException.checkNull( aConstraints );
    double minValue = Double.MIN_VALUE;
    if( aConstraints.hasValue( TSID_MIN_INCLUSIVE ) ) {
      minValue = aConstraints.getDouble( TSID_MIN_INCLUSIVE );
    }
    else {
      if( aConstraints.hasValue( TSID_MIN_EXCLUSIVE ) ) {
        minValue = aConstraints.getDouble( TSID_MIN_EXCLUSIVE ) + Double.MIN_NORMAL;
      }
    }
    double maxValue = Double.MAX_VALUE;
    if( aConstraints.hasValue( TSID_MAX_INCLUSIVE ) ) {
      maxValue = aConstraints.getDouble( TSID_MAX_INCLUSIVE );
    }
    else {
      if( aConstraints.hasValue( TSID_MAX_EXCLUSIVE ) ) {
        maxValue = aConstraints.getDouble( TSID_MAX_EXCLUSIVE ) - Double.MIN_NORMAL;
      }
    }
    if( minValue <= maxValue ) {
      return new DoubleRange( minValue, maxValue );
    }
    return DoubleRange.FULL;
  }

  /**
   * Creates {@link LongRange} from the TIMESTAMP constraints.
   * <p>
   * Behaves like {@link #makeIntRangeFromConstraints(IOptionSet)} but for <code>long</code> values of TMESTAMP.
   *
   * @param aConstraints {@link IOptionSet} - the set of constraints
   * @return {@link LongRange} - created range
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  static LongRange makeTimestampRangeFromConstraints( IOptionSet aConstraints ) {
    TsNullArgumentRtException.checkNull( aConstraints );
    long minValue = Long.MIN_VALUE;
    if( aConstraints.hasValue( TSID_MIN_INCLUSIVE ) ) {
      minValue = aConstraints.getTime( TSID_MIN_INCLUSIVE );
    }
    else {
      if( aConstraints.hasValue( TSID_MIN_EXCLUSIVE ) ) {
        minValue = aConstraints.getTime( TSID_MIN_EXCLUSIVE ) + 1;
      }
    }
    long maxValue = Long.MAX_VALUE;
    if( aConstraints.hasValue( TSID_MAX_INCLUSIVE ) ) {
      maxValue = aConstraints.getTime( TSID_MAX_INCLUSIVE );
    }
    else {
      if( aConstraints.hasValue( TSID_MAX_EXCLUSIVE ) ) {
        maxValue = aConstraints.getTime( TSID_MAX_EXCLUSIVE ) - 1;
      }
    }
    if( minValue <= maxValue ) {
      return new LongRange( minValue, maxValue );
    }
    return LongRange.FULL;
  }

}

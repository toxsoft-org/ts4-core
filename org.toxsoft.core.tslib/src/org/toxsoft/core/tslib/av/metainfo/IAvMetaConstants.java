package org.toxsoft.core.tslib.av.metainfo;

import static org.toxsoft.core.tslib.ITsHardConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.impl.DataDef.*;
import static org.toxsoft.core.tslib.av.metainfo.ITsResources.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.validators.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * Miscallenous meta information constants.
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
  String TSID_ICON_ID          = TS_ID + ".IconId";
  String TSID_IS_MANDATORY     = TS_ID + ".IsMandatory";
  String TSID_ENUMERATION      = TS_ID + ".Enumeration";     // stores IAvList
  String TSID_VALIDATOR_CLASS  = TS_ID + ".ValidatorClass";  // stores ITsValidator implementing class name
  String TSID_COMPARATOR_CLASS = TS_ID + ".ComparatorClass"; // stores ITsComparator implementing class name

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
  // Builtin data definitions

  IDataDef DDEF_NONE = create( DDID_NONE, NONE, //
      TSID_ID, NONE.id(), //
      TSID_NAME, NONE.nmName(), //
      TSID_DESCRIPTION, NONE.description(), //
      TSID_DEFAULT_VALUE, NONE.defaultValue() //
  );

  IDataDef DDEF_BOOLEAN = create( DDID_BOOLEAN, BOOLEAN, //
      TSID_ID, BOOLEAN.id(), //
      TSID_NAME, BOOLEAN.nmName(), //
      TSID_DESCRIPTION, BOOLEAN.description(), //
      TSID_DEFAULT_VALUE, BOOLEAN.defaultValue() //
  );

  IDataDef DDEF_INTEGER = create( DDID_INTEGER, INTEGER, //
      TSID_ID, INTEGER.id(), //
      TSID_NAME, INTEGER.nmName(), //
      TSID_DESCRIPTION, INTEGER.description(), //
      TSID_DEFAULT_VALUE, INTEGER.defaultValue() //
  );

  IDataDef DDEF_FLOATING = create( DDID_FLOATING, FLOATING, //
      TSID_ID, FLOATING.id(), //
      TSID_NAME, FLOATING.nmName(), //
      TSID_DESCRIPTION, FLOATING.description(), //
      TSID_DEFAULT_VALUE, FLOATING.defaultValue() //
  );

  IDataDef DDEF_TIMESTAMP = create( DDID_TIMESTAMP, TIMESTAMP, //
      TSID_ID, TIMESTAMP.id(), //
      TSID_NAME, TIMESTAMP.nmName(), //
      TSID_DESCRIPTION, TIMESTAMP.description(), //
      TSID_DEFAULT_VALUE, TIMESTAMP.defaultValue() //
  );

  IDataDef DDEF_STRING = create( DDID_STRING, STRING, //
      TSID_ID, STRING.id(), //
      TSID_NAME, STRING.nmName(), //
      TSID_DESCRIPTION, STRING.description(), //
      TSID_DEFAULT_VALUE, STRING.defaultValue() //
  );

  IDataDef DDEF_VALOBJ = create( DDID_VALOBJ, VALOBJ, //
      TSID_ID, VALOBJ.id(), //
      TSID_NAME, VALOBJ.nmName(), //
      TSID_DESCRIPTION, VALOBJ.description(), //
      TSID_DEFAULT_VALUE, VALOBJ.defaultValue() //
  );

  IDataDef DDEF_NAME = create( TSID_NAME, STRING, //
      TSID_NAME, STR_N_NAME, //
      TSID_DESCRIPTION, STR_D_NAME, //
      TSID_IS_NULL_ALLOWED, AV_TRUE, //
      TSID_VALIDATOR_CLASS, NameStringAvValidator.class.getName(), //
      TSID_DEFAULT_VALUE, DEFAULT_NAME_AV //
  );

  IDataDef DDEF_DESCRIPTION = create( TSID_DESCRIPTION, STRING, //
      TSID_NAME, STR_N_DESCRIPTION, //
      TSID_DESCRIPTION, STR_D_DESCRIPTION, //
      TSID_IS_NULL_ALLOWED, AV_TRUE, //
      TSID_DEFAULT_VALUE, AV_STR_EMPTY //
  );

  IDataDef DDEF_DEFAULT_VALUE = create( TSID_DEFAULT_VALUE, BOOLEAN, //
      TSID_NAME, STR_N_DEFAULT_VALUE, //
      TSID_DESCRIPTION, STR_D_DEFAULT_VALUE, //
      TSID_IS_NULL_ALLOWED, AV_TRUE, //
      TSID_DEFAULT_VALUE, IAtomicValue.NULL //
  );

  IDataDef DDEF_IDNAME = create( DDID_IDNAME, STRING, //
      TSID_NAME, STR_N_IDNAME, //
      TSID_DESCRIPTION, STR_D_IDNAME, //
      TSID_IS_NULL_ALLOWED, AV_FALSE, //
      TSID_VALIDATOR_CLASS, IdNameStringAvValidator.class.getName(), //
      TSID_DEFAULT_VALUE, DEFAULT_ID_AV //
  );

  IDataDef DDEF_IDPATH = create( DDID_IDPATH, STRING, //
      TSID_NAME, STR_N_IDPATH, //
      TSID_DESCRIPTION, STR_D_IDPATH, //
      TSID_VALIDATOR_CLASS, IdPathStringAvValidator.class.getName(), //
      TSID_IS_NULL_ALLOWED, AV_FALSE, //
      TSID_DEFAULT_VALUE, DEFAULT_ID_AV //
  );

  IDataDef DDEF_TS_BOOL = create( DDID_TS_BOOL, BOOLEAN, //
      TSID_NAME, STR_N_TS_BOOL, //
      TSID_DESCRIPTION, STR_N_TS_BOOL, //
      TSID_FORMAT_STRING, FMT_BOOL_CHECK, //
      TSID_DEFAULT_VALUE, AV_FALSE //
  );

  IDataDef DDEF_ICON_ID = create( TSID_ICON_ID, STRING, //
      TSID_NAME, STR_N_ICON_ID, //
      TSID_DESCRIPTION, STR_N_ICON_ID, //
      TSID_IS_NULL_ALLOWED, AV_TRUE, //
      TSID_DEFAULT_VALUE, DEFAULT_ID_AV //
  );

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

}

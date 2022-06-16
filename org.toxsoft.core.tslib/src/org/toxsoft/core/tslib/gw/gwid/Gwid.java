package org.toxsoft.core.tslib.gw.gwid;

import static org.toxsoft.core.tslib.gw.IGwHardConstants.*;
import static org.toxsoft.core.tslib.gw.gwid.EGwidKind.*;

import java.io.*;
import java.util.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.chario.*;
import org.toxsoft.core.tslib.bricks.strio.chario.impl.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * GWID - <b>G</b>reen <b>W</b>orld entity <b>ID</b>entifier.
 * <p>
 * The general example:
 * <p>
 * <code>classId[STRID_ID]$<b>prop.sect.id</b>(PROP_ID)$<b>sub_prop.sect.id</b>(SUB_PROP_ID)</code>
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public final class Gwid
    implements Serializable, Comparable<Gwid> {

  private static final long serialVersionUID = 157157L;

  /**
   * Multi object/propery/subproperty identifier sign.
   */
  public static final char KEYCH_MULTI_ID = '*';

  /**
   * Strid surrounding left brace.
   */
  public static final char KEYCH_STRID_LEFT = '[';

  /**
   * Strid surrounding right brace.
   */
  public static final char KEYCH_STRID_RIGHT = ']';

  /**
   * The part delimiter char.
   */
  public static final char KEYCH_PART_DELIM = '$';

  /**
   * Property or sub-property surrounding left brace.
   */
  public static final char KEYCH_PRID_LEFT = '(';

  /**
   * Property or sub-property surrounding right brace.
   */
  public static final char KEYCH_PRID_RIGHT = ')';

  /**
   * Multi identifier string representation. <br>
   * May be used as object strid, property and sub-property identifier in GWID string.
   */
  public static final String STR_MULTI_ID = "" + KEYCH_MULTI_ID; //$NON-NLS-1$

  /**
   * Keeper identifier.
   */
  public static final String KEEPER_ID = "Gwid"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<Gwid> KEEPER =
      new AbstractEntityKeeper<>( Gwid.class, EEncloseMode.NOT_IN_PARENTHESES, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, Gwid aEntity ) {
          aSw.writeAsIs( aEntity.asString() );
        }

        @Override
        protected Gwid doRead( IStrioReader aSr ) {
          return readGwid( aSr );
        }
      };

  // (de)serialize only canonicalString - other fields will be restored in readObject(ObjectInputStream)
  private final String canonicalString;

  private transient EGwidKind kind          = null;
  private transient String    classId       = null;
  private transient String    strid         = null;
  private transient Skid      skid          = null;
  private transient String    propSectId    = null;
  private transient String    propId        = null;
  private transient String    subPropSectId = null;
  private transient String    subPropId     = null;

  private Gwid( String aClassId, String aStrid, String aPropSectId, String aPropId, String aSubPropSectId,
      String aSubPropId ) {

    classId = StridUtils.checkValidIdPath( aClassId );
    strid = checkValidIdPathMulti( aStrid, false, true );
    if( strid != null && !strid.equals( STR_MULTI_ID ) ) {
      skid = new Skid( classId, strid );
    }
    else {
      skid = null;
    }
    propSectId = checkValidIdPath( aPropSectId, false, true );
    boolean hasProp = propSectId != null;
    propId = checkValidIdPathMulti( aPropId, !hasProp, !hasProp );
    subPropSectId = checkValidIdPath( aSubPropSectId, !hasProp, true );
    boolean hasSubProp = subPropSectId != null;
    subPropId = checkValidIdPathMulti( aSubPropId, !hasProp || !hasSubProp, !hasSubProp );
    kind = determineKind();
    canonicalString = makeCanonicalString();
    // ensure validity
    if( !kind.hasProp() && isProp() ) {
      throw new TsIllegalArgumentRtException();
    }
    if( !kind.hasSubProp() && isSubProp() ) {
      throw new TsIllegalArgumentRtException();
    }
    //
    //
    // classId = StridUtils.checkValidIdPath( aClassId );
    // // process abstract/concrete GWID (determine if there is a SKID specofoed)
    // strid = checkValidIdPathMulti( aStrid, false, true );
    // if( strid != null && !strid.equals( STR_MULTI_ID ) ) {
    // skid = new Skid( classId, strid );
    // }
    // else {
    // skid = null;
    // }
    // // process property
    // if( aPropSectId != null ) {
    // switch( aPropSectId ) {
    // case GW_KEYWORD_ATTR:
    // case GW_KEYWORD_RIVET:
    // case GW_KEYWORD_CLOB:
    // case GW_KEYWORD_RTDATA:
    // case GW_KEYWORD_LINK:
    // case GW_KEYWORD_CMD:
    // case GW_KEYWORD_EVENT:
    // break;
    // default: // invalid property section ID
    // throw new TsIllegalArgumentRtException();
    // }
    // propSectId = aPropSectId;
    // propId = checkValidIdPathMulti( aPropId, false, false );
    // subPropSectId = checkValidIdPath( aSubPropSectId, false, true );
    // if( subPropSectId != null ) {
    // subPropId = checkValidIdPathMulti( aSubPropId, false, false );
    // }
    // else {
    // throw new TsIllegalArgumentRtException();
    // }
    // }
    // else {
    // kind = GW_CLASS;
    // propSectId = null;
    // propId = null;
    // }
    // kind = determineKind();
    // canonicalString = makeCanonicalString();
    // // ensure validity
    // if( !kind.hasProp() && isProp() ) {
    // throw new TsIllegalArgumentRtException();
    // }
    // if( !kind.hasSubProp() && isSubProp() ) {
    // throw new TsIllegalArgumentRtException();
    // }
  }

  // ------------------------------------------------------------------------------------
  // Static constructors
  //

  public static Gwid create( String aClassId, String aStrid, String aPropSectId, String aPropId, String aSubPropSectId,
      String aSubPropId ) {
    return new Gwid( aClassId, aStrid, aPropSectId, aPropId, aSubPropSectId, aSubPropId );
  }

  public static Gwid createClass( String aClassId ) {
    return new Gwid( aClassId, null, null, null, null, null );
  }

  public static Gwid createObj( String aClassId, String aStrid ) {
    return new Gwid( aClassId, aStrid, null, null, null, null );
  }

  public static Gwid createObj( Skid aSkid ) {
    TsNullArgumentRtException.checkNull( aSkid );
    return new Gwid( aSkid.classId(), aSkid.strid(), null, null, null, null );
  }

  public static Gwid createAttr( String aClassId, String aAttrId ) {
    return new Gwid( aClassId, null, GW_KEYWORD_ATTR, aAttrId, null, null );
  }

  public static Gwid createAttr( String aClassId, String aStrid, String aAttrId ) {
    return new Gwid( aClassId, aStrid, GW_KEYWORD_ATTR, aAttrId, null, null );
  }

  public static Gwid createRtdata( String aClassId, String aRtdataId ) {
    return new Gwid( aClassId, null, GW_KEYWORD_RTDATA, aRtdataId, null, null );
  }

  public static Gwid createRtdata( String aClassId, String aStrid, String aRtdataId ) {
    return new Gwid( aClassId, aStrid, GW_KEYWORD_RTDATA, aRtdataId, null, null );
  }

  public static Gwid createLink( String aClassId, String aLinkId ) {
    return new Gwid( aClassId, null, GW_KEYWORD_LINK, aLinkId, null, null );
  }

  public static Gwid createLink( String aClassId, String aStrid, String aLinkId ) {
    return new Gwid( aClassId, aStrid, GW_KEYWORD_LINK, aLinkId, null, null );
  }

  public static Gwid createRivet( String aClassId, String aRivetId ) {
    return new Gwid( aClassId, null, GW_KEYWORD_RIVET, aRivetId, null, null );
  }

  public static Gwid createRivet( String aClassId, String aStrid, String aRivetId ) {
    return new Gwid( aClassId, aStrid, GW_KEYWORD_RIVET, aRivetId, null, null );
  }

  public static Gwid createCmd( String aClassId, String aCmdId ) {
    return new Gwid( aClassId, null, GW_KEYWORD_CMD, aCmdId, null, null );
  }

  public static Gwid createCmd( String aClassId, String aStrid, String aCmdId ) {
    return new Gwid( aClassId, aStrid, GW_KEYWORD_CMD, aCmdId, null, null );
  }

  public static Gwid createCmdArg( String aClassId, String aCmdId, String aCmdArgId ) {
    return new Gwid( aClassId, null, GW_KEYWORD_CMD, aCmdId, GW_KEYWORD_CMD_ARG, aCmdArgId );
  }

  public static Gwid createCmdArg( String aClassId, String aStrid, String aCmdId, String aCmdArgId ) {
    return new Gwid( aClassId, aStrid, GW_KEYWORD_CMD, aCmdId, GW_KEYWORD_CMD_ARG, aCmdArgId );
  }

  public static Gwid createEvent( String aClassId, String aEventId ) {
    return new Gwid( aClassId, null, GW_KEYWORD_EVENT, aEventId, null, null );
  }

  public static Gwid createEvent( String aClassId, String aStrid, String aEventId ) {
    return new Gwid( aClassId, aStrid, GW_KEYWORD_EVENT, aEventId, null, null );
  }

  public static Gwid createEventParam( String aClassId, String aEventId, String aEventParamId ) {
    return new Gwid( aClassId, null, GW_KEYWORD_EVENT, aEventId, GW_KEYWORD_EVENT_PARAM, aEventParamId );
  }

  public static Gwid createEventParam( String aClassId, String aStrid, String aEventId, String aEventParamId ) {
    return new Gwid( aClassId, aStrid, GW_KEYWORD_EVENT, aEventId, GW_KEYWORD_EVENT_PARAM, aEventParamId );
  }

  public static Gwid createClob( String aClassId, String aClobId ) {
    return new Gwid( aClassId, null, GW_KEYWORD_CLOB, aClobId, null, null );
  }

  public static Gwid createClob( String aClassId, String aStrid, String aClobId ) {
    return new Gwid( aClassId, aStrid, GW_KEYWORD_CLOB, aClobId, null, null );
  }

  public static Gwid of( String aCanonicalString ) {
    // OPTIMIZE надо переделать на прямое чтение строки
    ICharInputStream chIn = new CharInputStreamString( aCanonicalString );
    IStrioReader sr = new StrioReader( chIn );
    return readGwid( sr );
  }

  /**
   * Reads canonical representation from the stream.
   *
   * @param aSr {@link IStrioReader} - characher input stream
   * @return {@link Gwid} - read instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException I/O error
   * @throws StrioRtException invalid canonical string format
   */
  public static Gwid readGwid( IStrioReader aSr ) {
    TsNullArgumentRtException.checkNull( aSr );
    String classId = aSr.readIdPath();
    String strid = null;
    char ch = aSr.peekChar( EStrioSkipMode.SKIP_NONE );
    // strid?
    if( ch == KEYCH_STRID_LEFT ) {
      aSr.ensureChar( KEYCH_STRID_LEFT );
      strid = readIdPathOrMulti( aSr );
      aSr.ensureChar( KEYCH_STRID_RIGHT );
    }
    ch = aSr.peekChar( EStrioSkipMode.SKIP_NONE );
    // no next part? - interpret as GW_CLASS
    if( ch != KEYCH_PART_DELIM ) {
      return create( classId, strid, null, null, null, null );
    }
    // property
    aSr.ensureChar( KEYCH_PART_DELIM );
    String propSectId = aSr.readIdPath();
    aSr.ensureChar( KEYCH_PRID_LEFT );
    String propId = readIdPathOrMulti( aSr );
    aSr.ensureChar( KEYCH_PRID_RIGHT );
    ch = aSr.peekChar( EStrioSkipMode.SKIP_NONE );
    // no next part? - interpret as GW_CLASS
    if( ch != KEYCH_PART_DELIM ) {
      return new Gwid( classId, strid, propSectId, propId, null, null );
    }
    // sub-property
    aSr.ensureChar( KEYCH_PART_DELIM );
    String subPropSectId = aSr.readIdPath();
    aSr.ensureChar( KEYCH_PRID_LEFT );
    String subPropId = readIdPathOrMulti( aSr );
    aSr.ensureChar( KEYCH_PRID_RIGHT );
    return new Gwid( classId, strid, propSectId, propId, subPropSectId, subPropId );
  }

  // ------------------------------------------------------------------------------------
  // Internal implementation
  //

  private String makeCanonicalString() {
    StringBuilder sb = new StringBuilder();
    sb.append( classId );
    // strid?
    if( strid != null ) {
      sb.append( KEYCH_STRID_LEFT );
      sb.append( strid );
      sb.append( KEYCH_STRID_RIGHT );
    }
    // property?
    if( propSectId != null ) {
      sb.append( KEYCH_PART_DELIM );
      sb.append( propSectId );
      sb.append( KEYCH_PRID_LEFT );
      sb.append( propId );
      sb.append( KEYCH_PRID_RIGHT );
      // sub-property
      if( subPropSectId != null ) {
        sb.append( KEYCH_PART_DELIM );
        sb.append( subPropSectId );
        sb.append( KEYCH_PRID_LEFT );
        sb.append( subPropId );
        sb.append( KEYCH_PRID_RIGHT );
      }
    }
    return sb.toString();
  }

  private void readObject( ObjectInputStream aIns )
      throws IOException,
      ClassNotFoundException {
    aIns.defaultReadObject(); // only canonicalString is read
    // сейчас разберем строку canonicalString, исходя из того, чо оибок в нем не может быть
    int lp = 0, plp = 0, len = canonicalString.length();
    char ch;
    // считываем classId
    while( StridUtils.isIdPathPart( ch = canonicalString.charAt( lp ) ) ) {
      ++lp;
      if( lp == len ) {
        classId = canonicalString;
        kind = GW_CLASS;
        return;
      }
    }
    classId = canonicalString.substring( plp, lp );
    ++lp; // пропустим KEYCH_STRID_LEFT или KEYCH_PART_DELIM
    plp = lp;
    // считываем strid, если есть
    if( ch == KEYCH_STRID_LEFT ) {
      while( StridUtils.isIdPathPart( ch = canonicalString.charAt( lp ) ) || (ch == KEYCH_MULTI_ID && plp == lp) ) {
        ++lp;
        if( lp == len ) {
          strid = canonicalString.substring( plp, lp );
          skid = !strid.equals( STR_MULTI_ID ) ? new Skid( classId, strid ) : null;
          kind = GW_CLASS;
          return;
        }
      }
      strid = canonicalString.substring( plp, lp );
      skid = !strid.equals( STR_MULTI_ID ) ? new Skid( classId, strid ) : null;
      ++lp; // пропустим KEYCH_STRID_RIGHT
      ++lp; // пропустим KEYCH_PART_DELIM
      plp = lp;
    }
    // считаем пару "propSectId(propId)"
    while( StridUtils.isIdPathPart( ch = canonicalString.charAt( lp ) ) ) {
      ++lp; // здесь не может быть конца строки
    }
    propSectId = canonicalString.substring( plp, lp );
    ++lp; // пропустим KEYCH_PRID_LEFT
    plp = lp;
    while( StridUtils.isIdPathPart( ch = canonicalString.charAt( lp ) ) || (ch == KEYCH_MULTI_ID && plp == lp) ) {
      ++lp; // здесь не может быть конца строки
    }
    propId = canonicalString.substring( plp, lp );
    ++lp; // пропустим KEYCH_PRID_RIGHT
    ++lp; // пропустим KEYCH_PART_DELIM
    plp = lp;
    if( lp > len ) {
      kind = determineKind();
      return;
    }
    // считаем пару "subPropSectId(subPropId)"
    while( StridUtils.isIdPathPart( ch = canonicalString.charAt( lp ) ) ) {
      ++lp; // здесь не может быть конца строки
    }
    subPropSectId = canonicalString.substring( plp, lp );
    ++lp; // пропустим KEYCH_PRID_LEFT
    plp = lp;
    while( StridUtils.isIdPathPart( ch = canonicalString.charAt( lp ) ) || (ch == KEYCH_MULTI_ID && plp == lp) ) {
      ++lp; // здесь не может быть конца строки
    }
    subPropId = canonicalString.substring( plp, lp );
    // ++lp; // пропустим KEYCH_PRID_RIGHT
    kind = determineKind();
  }

  private EGwidKind determineKind() {
    EGwidKind k = GW_CLASS;
    // property?
    if( propSectId != null ) {
      k = switch( propSectId ) {
        case GW_KEYWORD_ATTR -> GW_ATTR;
        case GW_KEYWORD_RTDATA -> GW_RTDATA;
        case GW_KEYWORD_RIVET -> GW_RIVET;
        case GW_KEYWORD_LINK -> GW_LINK;
        case GW_KEYWORD_CMD -> GW_CMD;
        case GW_KEYWORD_EVENT -> GW_EVENT;
        case GW_KEYWORD_CLOB -> GW_CLOB;
        default -> throw new TsInternalErrorRtException();
      };
      // sub-property?
      if( subPropSectId != null ) {
        k = switch( subPropSectId ) {
          case GW_KEYWORD_CMD_ARG -> {
            TsIllegalArgumentRtException.checkTrue( k != GW_CMD );
            yield GW_CMD_ARG;
          }
          case GW_KEYWORD_EVENT_PARAM -> {
            TsIllegalArgumentRtException.checkTrue( k != GW_EVENT );
            yield GW_EVENT_PARAM;
          }
          default -> throw new TsInternalErrorRtException();
        };
      }
    }
    return k;
  }

  private static String checkValidIdPathMulti( String aId, boolean aMustBeNull, boolean aCanBeNull ) {
    // check for null
    if( aId == null ) {
      if( aCanBeNull ) {
        return null;
      }
      throw new TsNullArgumentRtException();
    }
    if( aMustBeNull ) {
      throw new TsIllegalArgumentRtException();
    }
    // check for multi
    if( aId.equals( STR_MULTI_ID ) ) {
      return aId;
    }
    // check is IDpath
    return StridUtils.checkValidIdPath( aId );
  }

  private static String checkValidIdPath( String aId, boolean aMustBeNull, boolean aCanBeNull ) {
    // check for null
    if( aId == null ) {
      if( aCanBeNull ) {
        return null;
      }
      throw new TsNullArgumentRtException();
    }
    if( aMustBeNull ) {
      throw new TsIllegalArgumentRtException();
    }
    // check is IDpath
    return StridUtils.checkValidIdPath( aId );
  }

  private static String readIdPathOrMulti( IStrioReader aSr ) {
    char ch = aSr.peekChar( EStrioSkipMode.SKIP_NONE );
    // multi id?
    if( ch == KEYCH_MULTI_ID ) {
      aSr.ensureChar( KEYCH_MULTI_ID );
      return STR_MULTI_ID;
    }
    // must be IDpath
    return aSr.readIdPath();
  }

  // ------------------------------------------------------------------------------------
  // IGwid
  //

  /**
   * Returns the canonical string representation of the GWID.
   *
   * @return String - GWID canpnical string
   */
  public String asString() {
    return canonicalString;
  }

  /**
   * Returns the kind of the GWID.
   * <p>
   * GWID of any kind may be either abstract or concrete.
   *
   * @return {@link EGwidKind} - the GWID kind
   */
  public EGwidKind kind() {
    return kind;
  }

  /**
   * Returns the class identifier.
   *
   * @return String - the class identifier is always valid IDpath
   */
  public String classId() {
    return classId;
  }

  /**
   * Determines if any part od the GWID refers to multi entities.
   *
   * @return boolean - <code>true</code> if {@link #isStridMulti()} or {@link #isPropMulti()} returns <code>true</code>:
   */
  public boolean isMulti() {
    return isStridMulti() || isPropMulti() || isSubPropMulti();
  }

  /**
   * Determines if this GWID refers to concrete object (or all objects) of class {@link #classId()}.
   * <p>
   * By definition multi object GWIDs {@link #isStridMulti()}=<code>true</code> are considered concrete.
   * <p>
   * When this method return <code>false</code> the following method return <code>null</code>: {@link #strid()},
   * {@link #skid()}.
   *
   * @return boolean - <code>false</code> if GWID refers to concrete object(s)
   */
  public boolean isAbstract() {
    return strid == null;
  }

  /**
   * Determines if thid GWID refers to many objects.
   *
   * @return boolean - <code>true</code> only if {@link #strid()} equals to {@link #STR_MULTI_ID}
   */
  public boolean isStridMulti() {
    return Objects.equals( strid, STR_MULTI_ID );
  }

  /**
   * Returns the object strid.
   * <p>
   * For abstract GWID {@link #isAbstract()} = <code>true</code> returns <code>null</code>.
   * <p>
   * For multi-objects {@link #isStridMulti()} = <code>true</code> returns {@link Gwid#STR_MULTI_ID}.
   *
   * @return String - obbject strid or <code>null</code> or {@link Gwid#STR_MULTI_ID}
   */
  public String strid() {
    return strid;
  }

  /**
   * Returns the only one concrete object.
   * <p>
   * Exctly one object is defined for abstract and non-multi object GWID, ie when {@link #isAbstract()} =
   * <code>false</code> and {@link #isStridMulti()} = <code>false</code>
   *
   * @return {@link Skid} - the only concrete object or <code>null</code>
   */
  public Skid skid() {
    return skid;
  }

  /**
   * Determines if there is the property (and possibly sub-propery) part in this GWID.
   * <p>
   * When this method return <code>false</code> the following methods return <code>null</code>: {@link #propSectId()},
   * {@link #propId()}, {@link #subPropSectId()}, {@link #subPropId()}.
   *
   * @return boolean - <code>true</code> if GWID has subproperty part
   */
  public boolean isProp() {
    return propId != null;
  }

  /**
   * Determines if property refers to multi (all) sub-properties.
   *
   * @return boolean - <code>true</code> only if {@link #propId()} equals to {@link #STR_MULTI_ID}
   */
  public boolean isPropMulti() {
    return Objects.equals( propId, STR_MULTI_ID );
  }

  /**
   * Returns the property section name.
   *
   * @return String - the property section name IDpath or <code>null</code>
   */
  public String propSectId() {
    return propSectId;
  }

  /**
   * Returns the propery identifier.
   *
   * @return String - the propery identifier IDpath or <code>null</code> or {@link #STR_MULTI_ID}
   */
  public String propId() {
    return propId;
  }

  /**
   * Determines if there is the sub-propery part in this GWID.
   * <p>
   * When this method return <code>false</code> the following methods return <code>null</code>:
   * {@link #subPropSectId()}, {@link #subPropId()}.
   *
   * @return boolean - <code>true</code> if GWID has sub-property part
   */
  public boolean isSubProp() {
    return subPropId != null;
  }

  /**
   * Determines if sub-property refers to multi (all) sub-properties.
   *
   * @return boolean - <code>true</code> only if {@link #subPropId()} equals to {@link #STR_MULTI_ID}
   */
  public boolean isSubPropMulti() {
    return Objects.equals( subPropId, STR_MULTI_ID );
  }

  /**
   * Returns the sub-property section name.
   *
   * @return String - the sub-property section name IDpath or <code>null</code>
   */
  public String subPropSectId() {
    return subPropSectId;
  }

  /**
   * Returns the sub-propery identifier.
   *
   * @return String - the sub-propery identifier IDpath or <code>null</code> or {@link #STR_MULTI_ID}
   */
  public String subPropId() {
    return subPropId;
  }

  /**
   * Returns the levels in this GWID.
   * <p>
   * Level 1 is GWID of {@link EGwidKind#GW_CLASS}, level 2 - GWIDs with props, level 3 - with bith props and subprops.
   *
   * @return int - GWID level in range 1..3
   */
  public int level() {
    if( isSubProp() ) {
      return 3;
    }
    if( isProp() ) {
      return 2;
    }
    return 1;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return asString();
  }

  @Override
  public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    if( aThat instanceof Gwid that ) {
      return canonicalString.equals( that.canonicalString );
    }
    return false;
  }

  @Override
  public int hashCode() {
    return canonicalString.hashCode();
  }

  // ------------------------------------------------------------------------------------
  // Comparable
  //

  @Override
  public int compareTo( Gwid aThat ) {
    if( aThat == null ) {
      throw new NullPointerException();
    }
    return canonicalString.compareTo( aThat.canonicalString );
  }

}

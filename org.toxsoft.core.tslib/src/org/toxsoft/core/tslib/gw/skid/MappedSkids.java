package org.toxsoft.core.tslib.gw.skid;

import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import java.io.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * An editable implementaion of {@link IMappedSkids}.
 *
 * @author hazard157
 */
public class MappedSkids
    implements IMappedSkids, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * Registered keeper ID.
   */
  public static final String KEEPER_ID = "MappedSkids"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<IMappedSkids> KEEPER =
      new AbstractEntityKeeper<>( IMappedSkids.class, EEncloseMode.ENCLOSES_KEEPER_IMPLEMENTATION, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, IMappedSkids aEntity ) {
          StrioUtils.writeStringMap( aSw, EMPTY_STRING, aEntity.skidsMap(), SkidListKeeper.KEEPER, isEnclosed() );
        }

        @Override
        protected IMappedSkids doRead( IStrioReader aSr ) {
          MappedSkids ms = new MappedSkids();
          StrioUtils.readStringMap( aSr, EMPTY_STRING, SkidListKeeper.KEEPER, ms.skidsMap() );
          return ms;
        }
      };

  private final IStringMapEdit<SkidList> skidsMap = new StringMap<>();

  /**
   * Constructor.
   */
  public MappedSkids() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // IMappedSkids
  //

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  @Override
  public IStringMapEdit<ISkidList> skidsMap() {
    return (IStringMapEdit)skidsMap;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  public SkidList findSkidList( String aKey ) {
    return skidsMap.findByKey( aKey );
  }

  public SkidList getSkidList( String aKey ) {
    return skidsMap.getByKey( aKey );
  }

  public SkidList ensureSkidList( String aKey ) {
    SkidList sl = skidsMap.findByKey( aKey );
    if( sl == null ) {
      StridUtils.checkValidIdPath( aKey );
      sl = new SkidList();
      skidsMap.put( aKey, sl );
    }
    return sl;
  }

  public SkidList ensureSkidList( String aKey, IList<Skid> aSource ) {
    SkidList sl = skidsMap.findByKey( aKey );
    if( sl == null ) {
      StridUtils.checkValidIdPath( aKey );
      sl = new SkidList( aSource );
      skidsMap.put( aKey, sl );
    }
    return sl;
  }

  public void setAll( IStringMap<ISkidList> aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    skidsMap.clear();
    for( String s : aSource.keys() ) {
      StridUtils.checkValidIdPath( s );
      SkidList sl = new SkidList( aSource.getByKey( s ) );
      skidsMap.put( s, sl );
    }
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return MappedSkids.class.getSimpleName() + '[' + skidsMap.size() + ']';
  }

  @Override
  public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    if( aThat instanceof MappedSkids that ) {
      return skidsMap.equals( that.skidsMap );
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = INITIAL_HASH_CODE;
    result = PRIME * result + skidsMap.hashCode();
    return result;
  }

}

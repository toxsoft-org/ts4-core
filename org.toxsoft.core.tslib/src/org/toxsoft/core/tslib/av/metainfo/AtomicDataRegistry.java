package org.toxsoft.core.tslib.av.metainfo;

import static org.toxsoft.core.tslib.av.metainfo.ITsResources.*;

import org.toxsoft.core.tslib.av.impl.DataDef;
import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesList;
import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesListEdit;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.StridablesList;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IAtomicDataRegistry} implementation.
 *
 * @author hazard157
 */
public class AtomicDataRegistry
    implements IAtomicDataRegistry {

  private final IAtomicDataRegistry           parent;
  private final IStridablesListEdit<IDataDef> defs = new StridablesList<>();

  /**
   * Creates the root registry.
   */
  AtomicDataRegistry() {
    parent = null;
    for( IDataDef dd : IAvMetaConstants.ALL_DDEFS ) {
      registerDef( dd, EParamsOverwriteMode.FAIL );
    }
  }

  /**
   * The child registry constructor.
   *
   * @param aParent {@link IAtomicDataRegistry} - the parent registry
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public AtomicDataRegistry( IAtomicDataRegistry aParent ) {
    TsNullArgumentRtException.checkNull( aParent );
    parent = aParent;
  }

  // ------------------------------------------------------------------------------------
  // IAtomicDataRegistry
  //

  @Override
  public IDataDef findDef( String aId ) {
    TsNullArgumentRtException.checkNull( aId );
    IDataDef dd = defs.findByKey( aId );
    if( dd == null && parent != null ) {
      dd = parent.findDef( aId );
    }
    return dd;
  }

  @Override
  public IDataDef getDef( String aId ) {
    IDataDef dd = findDef( aId );
    if( dd == null ) {
      throw new TsItemNotFoundRtException( FMT_ERR_NO_REGISTERED_DATA_DEF, aId );
    }
    return dd;
  }

  @Override
  public IStridablesList<IDataDef> defs() {
    return defs;
  }

  @Override
  public IDataDef registerDef( IDataDef aDef, EParamsOverwriteMode aMode ) {
    TsNullArgumentRtException.checkNulls( aDef, aMode );
    // test if parent contains def with the same ID
    if( parent != null ) {
      IDataDef dd = parent.findDef( aDef.id() );
      // can't change atomic type even for parent def
      if( dd != null ) {
        if( dd.atomicType() != aDef.atomicType() ) {
          throw new TsIllegalArgumentRtException( FMT_ERR_PARENT_DATA_DEF_AT_CHANGE, aDef.id(), dd.atomicType().id(),
              aDef.atomicType().id() );
        }
      }
    }
    // test if this registry contains def with the same ID
    IDataDef ddOld = defs.findByKey( aDef.id() );
    if( ddOld == null || ddOld.equals( aDef ) ) {
      defs.put( aDef );
      return aDef;
    }
    // can't change atomic type
    if( ddOld.atomicType() != aDef.atomicType() ) {
      throw new TsIllegalArgumentRtException( FMT_ERR_DATA_DEF_AT_CHANGE, aDef.id(), ddOld.atomicType().id(),
          aDef.atomicType().id() );
    }
    // process IDataDef.params()
    switch( aMode ) {
      case FAIL: {
        throw new TsItemAlreadyExistsRtException( FMT_ERR_DATA_DEF_IS_ALREADY, aDef.id() );
      }
      case OVERRIDE: {
        defs.put( aDef );
        return aDef;
      }
      case RETAIN: {
        DataDef dd = new DataDef( ddOld.id(), ddOld.atomicType(), ddOld.params() );
        dd.params().extendSet( aDef.params() );
        defs.put( dd );
        return dd;
      }
      default:
        throw new TsNotAllEnumsUsedRtException( aMode.name() );
    }
  }

  @Override
  public IAtomicDataRegistry parent() {
    return parent;
  }

}

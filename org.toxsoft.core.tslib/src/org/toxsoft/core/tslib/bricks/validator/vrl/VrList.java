package org.toxsoft.core.tslib.bricks.validator.vrl;

import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVrListEdit} implementation.
 * <p>
 * Note: use editable {@link #listInfoOpDefs()} to edit option definitions.
 * <p>
 * {@link ITsClearable#clear()} clears only {@link #items()}, use {@link #reset()} to clear {@link #listInfoOpDefs()}
 * also.
 *
 * @author hazard157
 */
public class VrList
    implements IVrListEdit, ITsClearable {

  private final IStridablesListEdit<IDataDef> infoOpDefs = new StridablesList<>();
  private final IListEdit<VrlItem>            items      = new ElemLinkedBundleList<>();

  /**
   * Constructor.
   */
  public VrList() {
    // nop
  }

  /**
   * Constructor.
   *
   * @param aInfoOpDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - content for {@link #listInfoOpDefs()}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VrList( IStridablesList<IDataDef> aInfoOpDefs ) {
    infoOpDefs.setAll( aInfoOpDefs );
  }

  /**
   * Copy constructor.
   *
   * @param aSource {@link IVrList} - the source
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VrList( IVrList aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    items.setAll( aSource.items() );
    infoOpDefs.setAll( aSource.listInfoOpDefs() );
  }

  // ------------------------------------------------------------------------------------
  // IVrList
  //

  @Override
  public EValidationResultType getWorstType() {
    boolean wasWarn = false;
    for( VrlItem i : items ) {
      switch( i.vr().type() ) {
        case OK: {
          // nop
          break;
        }
        case WARNING: {
          wasWarn = true;
          break;
        }
        case ERROR: {
          return EValidationResultType.ERROR;
        }
        default:
          throw new TsNotAllEnumsUsedRtException();
      }
    }
    return wasWarn ? EValidationResultType.WARNING : EValidationResultType.OK;
  }

  @Override
  public IList<VrlItem> items() {
    return items;
  }

  @Override
  public IStridablesListEdit<IDataDef> listInfoOpDefs() {
    return infoOpDefs;
  }

  @Override
  public VrlItem getFirstWorst() {
    VrlItem vrlWarn = null;
    for( VrlItem i : items ) {
      if( i.vr().isError() ) {
        return i;
      }
      if( i.vr().isWarning() && vrlWarn != null ) {
        vrlWarn = i;
      }
    }
    if( vrlWarn != null ) {
      return vrlWarn;
    }
    if( !items.isEmpty() ) {
      return items.first();
    }
    return VrlItem.OK;
  }

  // ------------------------------------------------------------------------------------
  // IVrListEdit
  //

  @Override
  public VrlItem add( VrlItem aItem ) {
    items.add( aItem );
    return aItem;
  }

  @Override
  public void addAll( IVrList aVrl ) {
    TsNullArgumentRtException.checkNull( aVrl );
    items.addAll( aVrl.items() );
  }

  // ------------------------------------------------------------------------------------
  // ITsClearable
  //

  @Override
  public void clear() {
    items.clear();
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Clears everything, instance content becomes as after constructor {@link #VrList()}.
   */
  public void reset() {
    clear();
    infoOpDefs.clear();
  }

}

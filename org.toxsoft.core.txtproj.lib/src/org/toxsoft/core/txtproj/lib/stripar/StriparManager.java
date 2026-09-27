package org.toxsoft.core.txtproj.lib.stripar;

import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IStriparManager} implementation.
 *
 * @author hazard157
 * @param <E> - stridable parameterized entity type
 */
public class StriparManager<E extends IStridable & IParameterized>
    extends StriparManagerApiImpl<E>
    implements IStriparManager<E> {

  /**
   * Constructor.
   *
   * @param aCreator {@link IStriparCreator} - elements creator
   * @param aParamDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - STRIPAR parameter definitions
   * @param aSorted boolean - flags to keep items sorted by ID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public StriparManager( IStriparCreator<E> aCreator, IStridablesList<IDataDef> aParamDefs, boolean aSorted ) {
    super( aCreator, aParamDefs, aSorted );
  }

  /**
   * Constructor for unsorted items.
   *
   * @param aCreator {@link IStriparCreator} - elements creator
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public StriparManager( IStriparCreator<E> aCreator ) {
    this( aCreator, IStridablesList.EMPTY, false );
  }

  // ------------------------------------------------------------------------------------
  // AbstractProjDataUnit
  //

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  @Override
  public void write( IStrioWriter aDw ) {
    TsNullArgumentRtException.checkNull( aDw );
    StridableParameterized.KEEPER.writeColl( aDw, (ITsCollection)items, true );
  }

  @Override
  public void read( IStrioReader aDr ) {
    TsNullArgumentRtException.checkNull( aDr );
    IList<StridableParameterized> ll = StridableParameterized.KEEPER.readColl( aDr );
    setItemsFromData( ll );
  }

}

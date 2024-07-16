package org.toxsoft.core.txtproj.gui.m5.stripar;

import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.m5.std.fields.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.stripar.*;

/**
 * M5-model of STRIPR entity.
 *
 * @author hazard157
 * @param <E> - stridable parameterized modeled entity type
 */
public class StriparM5Model<E extends IStridable & IParameterized>
    extends M5Model<E> {

  private final IStridablesListEdit<IDataDef> paramDefs = new StridablesList<>();

  /**
   * Constructor.
   *
   * @param aId String - model ID
   * @param aEntityClass {@link Class}&lt;T&gt; - modeled entity type
   * @param aParamefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - all options making
   *          {@link IParameterized#params()}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException identifier is not an IDpath
   * @throws TsItemAlreadyExistsRtException model with specified ID already exists in domain
   */
  public StriparM5Model( String aId, Class<E> aEntityClass, IStridablesList<IDataDef> aParamefs ) {
    super( aId, aEntityClass );
    paramDefs.addAll( aParamefs );
    addFieldDefs( new M5StdFieldDefId<>() );
    for( IDataDef dd : paramDefs ) {
      IM5AttributeFieldDef<E> fd = new M5StdFieldDefParamAttr<>( dd );
      addFieldDefs( fd );
    }
  }

  @SuppressWarnings( "unchecked" )
  @Override
  protected IM5LifecycleManager<E> doCreateLifecycleManager( Object aMaster ) {
    return new StriparM5LifecycleManager<>( this, IStriparManagerApi.class.cast( aMaster ) );
  }

}

package org.toxsoft.core.tsgui.m5.model.impl;

import static org.toxsoft.core.tsgui.m5.model.impl.ITsResources.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.valeds.singlelookup.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IM5SingleLookupFieldDef} implementation.
 *
 * @author hazard157
 * @param <T> - modeled entity type
 * @param <V> - lookup objects type that is field value type
 */
public class M5SingleLookupFieldDef<T, V>
    extends M5SingleLinkFieldDefBase<T, V>
    implements IM5SingleLookupFieldDef<T, V> {

  private IM5LookupProvider<V> lookupProvider = null;

  /**
   * Constructor.
   * <p>
   * By default sets {@link #canUserSelectNull()} = <code>false</code>.
   *
   * @param aId String - the field ID
   * @param aItemModelId String - the ID of the lookup item M5-model {@link #itemModel()}
   * @param aIdsAndValues Object[] - identifier / value pairs of {@link #params()}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any ID is not an IDpath
   * @throws TsIllegalArgumentRtException invalid Id/value pairs for option set definition
   */
  public M5SingleLookupFieldDef( String aId, String aItemModelId, Object... aIdsAndValues ) {
    super( aId, aItemModelId );
    params().addAll( OptionSetUtils.createOpSet( aIdsAndValues ) );
    setValedEditor( ValedSingleLookupEditor.FACTORY_NAME );
  }

  /**
   * Constructor.
   * <p>
   * By default sets {@link #canUserSelectNull()} = <code>false</code>.
   *
   * @param aId String - the field ID
   * @param aItemModelId String - the ID of the lookup item M5-model {@link #itemModel()}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any ID is not an IDpath
   * @throws TsIllegalArgumentRtException invalid Id/value pairs for option set definition
   */
  public M5SingleLookupFieldDef( String aId, String aItemModelId ) {
    this( aId, aItemModelId, EMPTY_ARRAY_OF_OBJECTS );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Sets the provider of the lookup items.
   *
   * @param aLookupProvider {@link IM5LookupProvider} - lookup items provider
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void setLookupProvider( IM5LookupProvider<V> aLookupProvider ) {
    TsNullArgumentRtException.checkNull( aLookupProvider );
    lookupProvider = aLookupProvider;
  }

  // ------------------------------------------------------------------------------------
  // IM5SingleLookupFieldDef
  //

  @Override
  public IM5LookupProvider<V> lookupProvider() {
    if( lookupProvider == null ) {
      /**
       * Try to generate default provider based on default lifecycle manager.
       */
      IM5LifecycleManager<V> lm = itemModel().findLifecycleManager( null );
      // default lifecycle manager must exist and provide items list
      if( lm != null && lm.isCrudOpAllowed( ECrudOp.LIST ) ) {
        lookupProvider = new IM5LookupProvider<>() {

          @Override
          public IList<V> listItems() {
            return lm.itemsProvider().listItems();
          }

          @Override
          public String getName( V aItem ) {
            return ITsNameProvider.DEFAULT.getName( aItem );
          }
        };
      }
    }
    if( lookupProvider == null ) {
      throw new TsIllegalStateRtException( FMT_ERR_NO_LOOKUP_PROVIDER, id() );
    }
    return lookupProvider;
  }

}

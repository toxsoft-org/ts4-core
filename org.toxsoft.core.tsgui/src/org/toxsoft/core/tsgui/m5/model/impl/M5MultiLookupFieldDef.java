package org.toxsoft.core.tsgui.m5.model.impl;

import static org.toxsoft.core.tsgui.m5.model.impl.ITsResources.*;

import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.valeds.multilookup.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IM5MultiLookupFieldDef} implementation.
 *
 * @author hazard157
 * @param <T> - modeled entity type
 * @param <V> - field value type
 */
public class M5MultiLookupFieldDef<T, V>
    extends M5MultiLinkFieldDefBase<T, V>
    implements IM5MultiLookupFieldDef<T, V> {

  private IM5LookupProvider<V> lookupProvider = null;

  /**
   * Constructor.
   * <p>
   * Initially {@link #maxCount()} = 0 and {@link #isExactCount()} = <code>false</code>.
   *
   * @param aId String - field ID
   * @param aItemModelId String - ID of lookup items model {@link #itemModel()}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any argument is not an IDpath
   */
  public M5MultiLookupFieldDef( String aId, String aItemModelId ) {
    super( aId, aItemModelId );
    setDefaultValue( IList.EMPTY );
    setValedEditor( ValedMultiLookupEditor.FACTORY_NAME );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Sets lookup provider.
   *
   * @param aLookupProvider {@link IM5LookupProvider} - the lookup provider
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void setLookupProvider( IM5LookupProvider<V> aLookupProvider ) {
    TsNullArgumentRtException.checkNull( aLookupProvider );
    lookupProvider = aLookupProvider;
  }

  // ------------------------------------------------------------------------------------
  // Additional API
  //

  private static final int SHOWN_COUNT       = 3;
  private static final int FIRST_ITEMS_COUNT = 1;
  private static final int LAST_ITEMS_COUNT  = SHOWN_COUNT - FIRST_ITEMS_COUNT;

  /**
   * Formats field content as single-line text.
   * <p>
   * Displays few starting and ending items.
   *
   * @param aValue {@link IList}&lt;V&gt; - items list may be <code>null</code>
   * @return String - single-line textual representation
   */
  public String formatFieldText( IList<V> aValue ) {
    if( aValue == null || aValue.isEmpty() ) {
      return TsLibUtils.EMPTY_STRING;
    }
    StringBuilder sb = new StringBuilder();
    int i = 0;
    int count = aValue.size();
    for( V v : aValue ) {
      if( count < SHOWN_COUNT || (i < FIRST_ITEMS_COUNT || i > (SHOWN_COUNT - LAST_ITEMS_COUNT - 1)) ) {
        sb.append( TsMiscUtils.toQuotedLine( lookupProvider.getName( v ) ) );
        if( i < count - 1 ) {
          sb.append( ", " ); //$NON-NLS-1$
        }
      }
      ++i;
    }
    return sb.toString();
  }

  // ------------------------------------------------------------------------------------
  // IM5MultiLookupFieldDef
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

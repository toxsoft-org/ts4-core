package org.toxsoft.core.tsgui.m5.model.impl;

import static org.toxsoft.core.tsgui.m5.model.impl.ITsResources.*;

import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.valeds.singlelookup.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Реализация {@link IM5SingleLookupFieldDef}.
 *
 * @author goga
 * @param <T> - тип моделированного объекта
 * @param <V> - тип справочного объекта
 */
public class M5SingleLookupFieldDef<T, V>
    extends M5SingleLinkFieldDefBase<T, V>
    implements IM5SingleLookupFieldDef<T, V> {

  private IM5LookupProvider<V> lookupProvider = null;

  /**
   * Конструктор.
   * <p>
   * По умолчанию {@link #canUserSelectNull()} = <code>false</code>.
   *
   * @param aId String - идентификатор поля (ИД-путь)
   * @param aItemModelId String - идентификатор модели {@link #itemModel()}
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException aId не ИД-пут
   * @throws TsIllegalArgumentRtException aItemModelId не ИД-путь
   */
  public M5SingleLookupFieldDef( String aId, String aItemModelId ) {
    super( aId, aItemModelId );
    setValedEditor( ValedSingleLookupEditor.FACTORY_NAME );
  }

  // ------------------------------------------------------------------------------------
  // API редактирования
  //

  /**
   * Задает поставщик списка-справочника.
   *
   * @param aLookupProvider {@link IM5LookupProvider} - поставщик списка-справочника
   * @throws TsNullArgumentRtException аргумент = null
   */
  public void setLookupProvider( IM5LookupProvider<V> aLookupProvider ) {
    TsNullArgumentRtException.checkNull( aLookupProvider );
    lookupProvider = aLookupProvider;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IM5SingleLookupFieldDef
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

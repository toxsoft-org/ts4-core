package org.toxsoft.tsgui.m5.std.models.enums;

import org.toxsoft.tsgui.m5.IM5Model;
import org.toxsoft.tsgui.m5.model.impl.M5LifecycleManager;
import org.toxsoft.tslib.coll.IList;
import org.toxsoft.tslib.coll.impl.ElemArrayList;
import org.toxsoft.tslib.utils.errors.*;

/**
 * Lifecycle manager for Java <code>enum</code> type models.
 * <p>
 * Manager only allows to list entities.
 *
 * @author hazard157
 * @param <T> - modelled <code>enum</code> class
 */
public class M5EnumLifecycleManager<T extends Enum<T>>
    extends M5LifecycleManager<T, Object> {

  final IList<T> items;

  /**
   * Constructor.
   *
   * @param aModel {@link IM5Model} - the model
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException modelled entity is not Java <code>enum</code>
   */
  public M5EnumLifecycleManager( IM5Model<T> aModel ) {
    super( aModel, false, false, false, true, null );
    TsIllegalArgumentRtException.checkFalse( aModel.entityClass().isEnum() );
    try {
      items = new ElemArrayList<>( aModel.entityClass().getEnumConstants() );
    }
    catch( Exception ex ) {
      throw new TsInternalErrorRtException( ex );
    }
  }

  @Override
  protected IList<T> doListEntities() {
    return items;
  }

}

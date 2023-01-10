package org.toxsoft.core.tsgui.m5.model.helpers;

import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Смешиваемый интерфейс полей, содержащие ключи или сами справочные элементы.
 *
 * @author hazard157
 * @param <V> - тип моделированной сущности, содержащейся в поле
 */
public interface IM5MixinLookupField<V> {

  /**
   * Возвращает поставщик списка-справочника.
   *
   * @return {@link IM5LookupProvider} - поставщик списка-справочника, не бывает <code>null</code>
   * @throws TsIllegalStateRtException не был задан поставщик списка-справочника
   */
  IM5LookupProvider<V> lookupProvider();

}

package org.toxsoft.core.tsgui.m5.model.impl;

import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.valeds.multimodown.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Реализация {@link IM5MultiModownFieldDef}.
 *
 * @author hazard157
 * @param <T> - тип моделированного объекта
 * @param <V> - тип объекта-ссылки
 */
public class M5MultiModownFieldDef<T, V>
    extends M5MultiLinkFieldDefBase<T, V>
    implements IM5MultiModownFieldDef<T, V> {

  /**
   * Конструктор.
   * <p>
   * По умолчанию количество связанных объекто не ограничено, то есть {@link #maxCount()} = 0 и {@link #isExactCount()}
   * = <code>false</code>.
   *
   * @param aId String - идентификатор поля (ИД-путь)
   * @param aItemModelId String - идентификатор модели {@link #itemModel()}
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException aId не ИД-пут
   * @throws TsIllegalArgumentRtException aItemModelId не ИД-путь
   */
  public M5MultiModownFieldDef( String aId, String aItemModelId ) {
    super( aId, aItemModelId );
    setDefaultValue( IList.EMPTY );
    setValedEditor( ValedMultiModownEditor.FACTORY_NAME );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  // TODO внести в XxxUtils или базовый класс

  private static final int SHOWN_COUNT       = 2;
  private static final int FIRST_ITEMS_COUNT = 1;
  private static final int LAST_ITEMS_COUNT  = SHOWN_COUNT - FIRST_ITEMS_COUNT;

  /**
   * Создает строковое представление коллекции элементов.
   * <p>
   * Отображает несколько первых и нексолько последних элементов коллекции подряд, в одной строке.
   *
   * @param aValue {@link IList}&lt;V&gt; - коллекция элементов, может быть null
   * @return String - текстовое представление списка элементов
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
        sb.append( TsMiscUtils.toQuotedLine( itemModel().visualsProvider().getName( v ) ) );
        if( i < count - 1 ) {
          sb.append( ", " ); //$NON-NLS-1$
        }
      }
      ++i;
    }
    return sb.toString();
  }

}

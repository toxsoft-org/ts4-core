package org.toxsoft.core.tsgui.chart.api;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Редактируемая реализация {@link IG2Params}.
 * <p>
 * Обратите внимание, что метод {@link #params()} в классе (в отличие от интерфейса) возвращает редактируемый набор
 * параметров. Таким образом, имея ссылку на класс {@link G2Params} (а не интерфейс {@link IG2Params}) можно
 * редатировать параметры.
 *
 * @author goga, vs
 */
public class G2Params
    implements IG2Params {

  private final String         consumerClassName;
  private final IOptionSetEdit params = new OptionSet();

  /**
   * Конструктор с единственным инвариантом.<br>
   * <b>На заметку:</b><br>
   * Класс, имя которого передется в параметре {@link #consumerClassName}, должен иметь конструктор с единственным
   * параметром - {@link IOptionSet}
   *
   * @param aConsumerClassName String - имя класса-потребителя настроечных параметров
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException аргумент пустая строка
   */
  public G2Params( String aConsumerClassName ) {
    TsErrorUtils.checkNonEmpty( aConsumerClassName );
    consumerClassName = aConsumerClassName;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IG2Params
  //

  @Override
  public String consumerName() {
    return consumerClassName;
  }

  @Override
  public IOptionSetEdit params() {
    return params;
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса Object
  //

  @SuppressWarnings( "nls" )
  @Override
  public String toString() {
    return consumerClassName + "[" + params.size() + "]";
  }

  @Override
  public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    if( aThat instanceof G2Params that ) {
      if( consumerClassName.equals( that.consumerClassName ) ) {
        return params.equals( that.params );
      }
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + consumerClassName.hashCode();
    result = TsLibUtils.PRIME * result + params.hashCode();
    return result;
  }

}

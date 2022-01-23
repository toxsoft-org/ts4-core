package org.toxsoft.tsgui.m5_1.ext.enums;

import java.lang.reflect.Method;

import org.toxsoft.tsgui.m5_1.api.IM5Model;
import org.toxsoft.tsgui.m5_1.impl.M5LifecycleManager;
import org.toxsoft.tslib.coll.IList;
import org.toxsoft.tslib.coll.impl.ElemArrayList;
import org.toxsoft.tslib.utils.errors.TsInternalErrorRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Стандартный менеджер жизненного цикла перечисляемого типа.
 *
 * @author goga
 * @param <T> - перечисляемый (enum) класс моделированной сущности
 */
public class M5EnumLifecycleManager<T extends Enum<T>>
    extends M5LifecycleManager<T, Object> {

  final IList<T> items;

  /**
   * Конструктор.
   *
   * @param aModel {@link IM5Model} - модель
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   */
  public M5EnumLifecycleManager( IM5Model<T> aModel ) {
    super( aModel, false, false, false, true, null );
    try {
      Method m = aModel.entityClass().getDeclaredMethod( "values" ); //$NON-NLS-1$
      @SuppressWarnings( "unchecked" )
      T[] array = (T[])m.invoke( null );
      items = new ElemArrayList<>( array );
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

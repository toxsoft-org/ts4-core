package org.toxsoft.core.pas.tj;

import java.io.ObjectStreamException;
import java.io.Serializable;

import org.toxsoft.core.pas.tj.impl.TjUtils;
import org.toxsoft.core.tslib.coll.primtypes.IStringMapEdit;
import org.toxsoft.core.tslib.utils.TsLibUtils;
import org.toxsoft.core.tslib.utils.errors.TsNullObjectErrorRtException;

/**
 * Структура данных, соответствующая читаемому из (записываемому в) JSON представление.
 * <p>
 * Точкой входа в подсистему является класс {@link TjUtils}.
 *
 * @author goga
 */
public interface ITjObject {

  /**
   * Несуществующий объект
   */
  ITjObject NULL = new InternalNullConnectionInfo();

  /**
   * Возврашает поля структуры.
   *
   * @return {@link IStringMapEdit}&lt;{@link ITjValue}&gt; - карта "имя поля" - "значние поля"
   */
  IStringMapEdit<ITjValue> fields();

}

/**
 * Реализация несуществующего объекта {@link ITjObject#NULL}.
 */
class InternalNullConnectionInfo
    implements ITjObject, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * Метод корректно восстанавливает сериализированный {@link ITjObject#NULL}.
   *
   * @return Object объект {@link ITjObject#NULL}
   * @throws ObjectStreamException это обявление, оно тут не выбрасывается
   */
  @SuppressWarnings( { "static-method" } )
  private Object readResolve()
      throws ObjectStreamException {
    return ITjObject.NULL;
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов ITjObject
  //
  @Override
  public IStringMapEdit<ITjValue> fields() {
    throw new TsNullObjectErrorRtException();
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов Object
  //
  @Override
  public int hashCode() {
    return TsLibUtils.INITIAL_HASH_CODE;
  }

  @Override
  public boolean equals( Object obj ) {
    return obj == this;
  }

  @Override
  public String toString() {
    return ITjObject.class.getSimpleName() + ".NULL"; //$NON-NLS-1$
  }
}

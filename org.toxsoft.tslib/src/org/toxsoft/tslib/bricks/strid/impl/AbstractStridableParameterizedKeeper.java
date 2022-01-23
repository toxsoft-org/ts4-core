package org.toxsoft.tslib.bricks.strid.impl;

import org.toxsoft.tslib.av.opset.IOptionSet;
import org.toxsoft.tslib.av.opset.impl.OptionSetKeeper;
import org.toxsoft.tslib.av.utils.IParameterized;
import org.toxsoft.tslib.bricks.keeper.AbstractEntityKeeper;
import org.toxsoft.tslib.bricks.strid.IStridable;
import org.toxsoft.tslib.bricks.strio.IStrioReader;
import org.toxsoft.tslib.bricks.strio.IStrioWriter;
import org.toxsoft.tslib.utils.errors.TsInternalErrorRtException;

/**
 * Базовый класс хранителей сущностей {@link StridableParameterized}.
 *
 * @param <T> - конкретный класс сущности
 */
public abstract class AbstractStridableParameterizedKeeper<T extends IStridable & IParameterized>
    extends AbstractEntityKeeper<T> {

  /**
   * Конструктор для наследников.
   *
   * @param aEntityClass Class&lt;E&gt; - класс (тип) хранимих сущностей, может быть <code>null</code>
   * @param aNullObject &lt;E&gt; - "нулевой" объект или <code>null</code> для отмены использования "нулевого" объекта
   */
  protected AbstractStridableParameterizedKeeper( Class<T> aEntityClass, T aNullObject ) {
    super( aEntityClass, EEncloseMode.ENCLOSES_BASE_CLASS, aNullObject );
  }

  @Override
  protected void doWrite( IStrioWriter aSw, T aEntity ) {
    aSw.writeAsIs( aEntity.id() );
    aSw.writeSeparatorChar();
    OptionSetKeeper.KEEPER.write( aSw, aEntity.params() );
  }

  @Override
  protected T doRead( IStrioReader aSr ) {
    String id = aSr.readIdPath();
    aSr.ensureSeparatorChar();
    IOptionSet params = OptionSetKeeper.KEEPER.read( aSr );
    T obj = doCreate( id, params );
    TsInternalErrorRtException.checkNull( obj );
    return obj;
  }

  // ------------------------------------------------------------------------------------
  // Для переопределения
  //

  /**
   * Наследник должен создать экземпляр класса &lt;T&gt;.
   *
   * @param aId String - идентификатор (ИД-путь) типа, всегда ИД-путь
   * @param aParams {@link IOptionSet} - значения {@link IParameterized#params()}, не бывает <code>null</code>
   * @return &lt;T&gt; - созданный экземпляр, не должен быть <code>null</code>
   */
  protected abstract T doCreate( String aId, IOptionSet aParams );

}

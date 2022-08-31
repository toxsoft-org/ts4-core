package org.toxsoft.core.tslib.av.avtree;

import java.io.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Реализация дерева значений {@link IAvTree}.
 * <p>
 * Для создания экземпляров дерева следует использовать статические методы createXxx().
 * 
 * @author goga
 */
public abstract class AvTree
    implements IAvTreeEdit, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * Пустой конструктор для наследников.
   */
  protected AvTree() {
    // nop
  }

  /**
   * Создает копию дерева.
   * 
   * @param aSrc {@link IAvTree} - исходное дерево
   * @return {@link AvTree} - созданная копия
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static AvTree createCopy( IAvTree aSrc ) {
    TsNullArgumentRtException.checkNull( aSrc );
    if( !aSrc.isArray() ) {
      return new AvTreeSingle( aSrc.structId(), aSrc.fields(), aSrc.nodes(), true );
    }
    AvTree result = new AvTreeArray();
    for( int i = 0; i < aSrc.arrayLength(); i++ ) {
      result.addElement( createCopy( aSrc.arrayElement( i ) ) );
    }
    return result;
  }

  /**
   * Создает дерево значений со всеми инвариантами.
   * <p>
   * Обратите внимание, что содержимое aNodes не копируется, а запоминаются ссылки.
   * 
   * @param aStructId String - идентификатор структуры (ИД-путь) или пустая строка
   * @param aFields {@link IOptionSet} - поля этого узла (корня) дерева
   * @param aNodes IStringMap&lt;{@link IAvTree}&gt; - узлы дерева
   * @return {@link AvTree} - созданное единичное дерево значений
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException aStructId не пустая строка и не валидный ИД-путь
   * @throws TsIllegalArgumentRtException один из идентификаторов aNodes не ИД-путь
   */
  public static AvTree createSingleAvTree( String aStructId, IOptionSet aFields, IStringMap<IAvTree> aNodes ) {
    return new AvTreeSingle( aStructId, aFields, aNodes, false );
  }

  /**
   * Создает новое, пустое дерево значений - массив.
   * 
   * @return новое, пустое дерево значений - массив
   */
  public static AvTree createArrayAvTree() {
    return new AvTreeArray();
  }

}

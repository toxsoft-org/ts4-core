package org.toxsoft.core.tslib.av.avtree;

import java.io.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Реализация {@link IAvTreePath}.
 * 
 * @author hazard157
 */
public class AvTreePath
    implements IAvTreePath, Serializable {

  private static final long serialVersionUID = 157157L;

  private final IListEdit<IAvTreePathElement> elements;

  /**
   * Создает путь из списка элементов.
   * 
   * @param aElems IList&lt;{@link IAvTreePathElement}&gt; - список элементов
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException в пути (кроме последнего) еще есть элементы, ссылающейся на поля
   */
  public AvTreePath( IList<IAvTreePathElement> aElems ) {
    checkValidity( aElems );
    elements = new ElemArrayList<>( aElems );
  }

  /**
   * Создает путь из набора или массива элементов.
   * 
   * @param aElems {@link IAvTreePathElement}[] - массив элементов
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException в пути (кроме последнего) еще есть элементы, ссылающейся на поля
   */
  public AvTreePath( IAvTreePathElement... aElems ) {
    elements = new ElemArrayList<>( aElems );
    checkValidity( elements );
  }

  /**
   * Конструктор копирования.
   * 
   * @param aSource {@link IAvTreePath} - исходный объект
   * @throws TsNullArgumentRtException аргумент = null
   */
  public AvTreePath( IAvTreePath aSource ) {
    this( TsNullArgumentRtException.checkNull( aSource ).elements() );
  }

  /**
   * Проверяет, что элементы пути являются валидными.
   * <p>
   * Путь считается валидным, если он либо пустой, либо в нем все (может быть, кроме последнего) элементы сслаются на
   * узлы. Ведь очевидно, что спускаться по дереву можно только по узлам, а на первом же поле посик останаваливаетмся.
   * 
   * @param aElems IList&lt;{@link IAvTreePathElement}&gt; - список элементов пути
   * @throws TsIllegalArgumentRtException в пути (кроме поеследнего) еще есть элементы, ссылающейся на поля
   */
  private static final void checkValidity( IList<IAvTreePathElement> aElems ) {
    for( int i = 0, n = aElems.size() - 1; i < n; i++ ) {
      TsIllegalArgumentRtException.checkFalse( aElems.get( i ).isNode() );
    }
  }

  // ------------------------------------------------------------------------------------
  // IAvTreePath
  //

  @Override
  public IList<IAvTreePathElement> elements() {
    return elements;
  }

  @Override
  public boolean isEmpty() {
    return elements.isEmpty();
  }

  @Override
  public IAvTreePathElement lastElement() {
    TsIllegalStateRtException.checkTrue( elements.isEmpty() );
    return elements.get( elements.size() - 1 );
  }

  @Override
  public boolean isNode() {
    if( elements.isEmpty() ) {
      return true;
    }
    return elements.get( elements.size() - 1 ).isNode();
  }

}

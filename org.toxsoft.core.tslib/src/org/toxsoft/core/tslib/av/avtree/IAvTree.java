package org.toxsoft.core.tslib.av.avtree;

import java.io.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Дерево (структура) переменных {@link IAtomicValue}.
 * <p>
 * Это рекурсивная структура данных (композит) является узлом дерева. Узел сам же и содержит другие узлы. Каждый узел
 * может быть корнем дерева. В каждом узле {@link IAvTree} кроме дочерных узлов {@link #nodes()}, находятся и поля
 * данных {@link #fields()}, оформленные в виде {@link IOptionSet}.
 *
 * @author hazard157
 */
public interface IAvTree {

  /**
   * "Нулевое" дерево значений, не содержит в себе ничего, и его нельзя редактировать.
   * <p>
   * Все методы редактирования выбрасывают исключение {@link TsNullObjectErrorRtException}.
   */
  IAvTreeEdit NULL = new InternalNullAvTreeEdit();

  /**
   * Определяет, является ли дерево единичным, или массивом.
   * <p>
   * Единичное дерево легко понять - оно является корнем, который содержит поля {@link #fields()}, дочерные деревья
   * {@link #nodes()} и имеет опциональный идентификатор структуры {@link #structId()}. При этом, это корень может
   * являятся дочерным узлом вышестоящего дерева (но оно само об этом не имеет возможности знать).
   * <p>
   * Но этот интерфейс может описывать также и массив корней, которые являются <b>одним</b> дочерным узлом вышестоящего
   * дерева. Это аналогично тому, как в Java поле класса может быть как единичной ссылкой (например, String fileName),\
   * так и массивом ссылок (String[] notes), оставаясь <b>одним</b> полем класса. Длина массива {@link #arrayLength()}
   * может быть любым, в том числе и нулевым.
   *
   * @return boolean - признак массива
   */
  boolean isArray();

  /**
   * Возвращает идентификатор структуры данного узла дерева.
   * <p>
   * Данный идентификатор может быть либо пустой строкой, либо ИД-путем. Используется идентификатор структуры вместе с
   * описанием {@link IAvTreeInfo} - для узлов-семейств ({@link IAvTreeInfo#isFamily()}=true) он указывает на
   * идентификатор члена сеймества, данные о котором содержатся в этом узле.
   *
   * @return String - идентификатор (ИД-путь) структуры или пустая строка
   * @throws TsUnsupportedFeatureRtException дерево является массивом (то есть {@link #isArray()} = true)
   */
  String structId();

  /**
   * Возвращает все поля узла.
   *
   * @return {@link IOptionSet} - именованные поля данного узла дерева
   * @throws TsUnsupportedFeatureRtException дерево является массивом (то есть {@link #isArray()} = true)
   */
  IOptionSet fields();

  /**
   * Возвращает именованные дочерные узлы дерева.
   *
   * @return IStringMap&lt;{@link IAvTree}&gt; - карта "имя (идентификатор) узла" - "дочерный узел"
   * @throws TsUnsupportedFeatureRtException дерево является массивом (то есть {@link #isArray()} = true)
   */
  IStringMap<IAvTree> nodes();

  /**
   * Возвращает длинну (количество элементов) в массиве.
   *
   * @return int - длина массива (может быть 0)
   * @throws TsUnsupportedFeatureRtException дерево является единичным (то есть {@link #isArray()} = false)
   */
  int arrayLength();

  /**
   * Возвращает элемент массива.
   *
   * @param aArrayIndex int - индекс элемента массива в пределах 0 .. {@link #arrayLength()}
   * @return {@link IAvTree} - элемент массива
   * @throws TsUnsupportedFeatureRtException дерево является единичным (то есть {@link #isArray()} = false)
   * @throws TsIllegalArgumentRtException индекс массива выходит за допустимые пределы
   */
  IAvTree arrayElement( int aArrayIndex );

}

class InternalNullAvTreeEdit
    implements IAvTreeEdit, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * Метод корректно восстанавливает сериализированный {@link IAvTree#NULL}.
   *
   * @return Object объект {@link IAvTree#NULL}
   * @throws ObjectStreamException это обявление, оно тут не выбрасывается
   */
  @SuppressWarnings( { "static-method" } )
  private Object readResolve()
      throws ObjectStreamException {
    return IAvTree.NULL;
  }

  // ------------------------------------------------------------------------------------
  // IAvTree
  //

  @Override
  public boolean isArray() {
    return false;
  }

  @Override
  public String structId() {
    return TsLibUtils.EMPTY_STRING;
  }

  @Override
  public IOptionSet fields() {
    return IOptionSet.NULL;
  }

  @Override
  public IStringMap<IAvTree> nodes() {
    return IStringMap.EMPTY;
  }

  @Override
  public int arrayLength() {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public IAvTree arrayElement( int aArrayIndex ) {
    throw new TsNullObjectErrorRtException();
  }

  // ------------------------------------------------------------------------------------
  // IAvTreeEdit
  //

  @Override
  public void setStructId( String aStructId ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public IOptionSetEdit fieldsEdit() {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public IAvTreeEdit nodeEdit( String aNodeId ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void putNode( String aNodeId, IAvTree aNode ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void removeNode( String aNodeId ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void clearNodes() {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public IAvTreeEdit getElementEdit( int aArrayIndex ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void addElement( IAvTree aArrayElement ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void insertElement( int aArrayIndex, IAvTree aArrayElement ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void setElement( int aArrayIndex, IAvTree aArrayElement ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void removeElement( int aArrayIndex ) {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public void clearElemets() {
    throw new TsNullObjectErrorRtException();
  }

}

package org.toxsoft.core.tslib.av.avtree;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Редактируемое дерево значений {@link IAtomicValue}.
 *
 * @author hazard157
 */
public interface IAvTreeEdit
    extends IAvTree {

  // ------------------------------------------------------------------------------------
  // Редактирование единичного дерева (на массива)
  //

  /**
   * Задает идентификатор структуры.
   * <p>
   * Идентификатор структуры либо валидный ИД-путь, либо пустая строка - что означчает отсутствие идентификации.
   *
   * @param aStructId String - идентификатор структуры (ИД-путь) или пустая строка
   * @throws TsUnsupportedFeatureRtException дерево является массивом (то есть {@link #isArray()} = true)
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException aStructId не пустая строка и не валидный ИД-путь
   */
  void setStructId( String aStructId );

  /**
   * Возвращает поля узла для редактирования.
   *
   * @return {@link IOptionSet} - редактируемый набор полей узла
   * @throws TsUnsupportedFeatureRtException дерево является массивом (то есть {@link #isArray()} = true)
   */
  IOptionSetEdit fieldsEdit();

  /**
   * Возвращает узел в редактируемом виде.
   *
   * @param aNodeId String - идентификатор узла
   * @return {@link IAvTreeEdit} - редатируемое дерево
   * @throws TsUnsupportedFeatureRtException дерево является массивом (то есть {@link #isArray()} = true)
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemAlreadyExistsRtException нет узла с таким идентификатором
   * @throws TsUnsupportedFeatureRtException данная реализация не позволяет редактировать узел
   */
  IAvTreeEdit nodeEdit( String aNodeId );

  /**
   * Задает дочернний узел (поддерево).
   * <p>
   * Если узел с таким идентификатором уже существует, то заменяет старое поддерево новым.
   *
   * @param aNodeId String - идентификатор узла
   * @param aNode {@link IAvTree} - значение (поддерево) узла
   * @throws TsUnsupportedFeatureRtException дерево является массивом (то есть {@link #isArray()} = true)
   * @throws TsNullArgumentRtException аргумент = null
   */
  void putNode( String aNodeId, IAvTree aNode );

  /**
   * Удалает узел по его идентификатору.
   * <p>
   * Если узла с таким идентификатором не существует, то метод ничего не делает.
   *
   * @param aNodeId String - идентификатор узла
   * @throws TsUnsupportedFeatureRtException дерево является массивом (то есть {@link #isArray()} = true)
   * @throws TsNullArgumentRtException аргумент = null
   */
  void removeNode( String aNodeId );

  /**
   * Удалет все дочерные узлы, очищая карту {@link #nodes()}.
   *
   * @throws TsUnsupportedFeatureRtException дерево является массивом (то есть {@link #isArray()} = true)
   */
  void clearNodes();

  // ------------------------------------------------------------------------------------
  // Редактирование дерева-массива
  //

  /**
   * Возвращает элемент массива в редактируемом виде.
   *
   * @param aArrayIndex int - индекс элемента массива в пределах 0 .. {@link #arrayLength()}
   * @return {@link IAvTreeEdit} - элемент массива в редактируемом виде
   * @throws TsUnsupportedFeatureRtException дерево является единичным (то есть {@link #isArray()} = false)
   * @throws TsIllegalArgumentRtException индекс массива выходит за допустимые пределы
   */
  IAvTreeEdit getElementEdit( int aArrayIndex );

  /**
   * Добавляет элемент в конец массива.
   * <p>
   * Размер массива {@link #arrayLength()} увеличивается на единицу.
   *
   * @param aArrayElement {@link IAvTree} - новый элемент массива
   * @throws TsUnsupportedFeatureRtException дерево является единичным (то есть {@link #isArray()} = false)
   * @throws TsNullArgumentRtException aArrayElement = null
   * @throws TsIllegalArgumentRtException aArrayElement является массивом, а должен быть единичным деревом
   */
  void addElement( IAvTree aArrayElement );

  /**
   * Вставляет элемент массива в указанное место.
   * <p>
   * Если указанный индекс равен длине массива {@link #arrayLength()}, то элемент добавляется в конец массива.
   * <p>
   * Размер массива {@link #arrayLength()} увеличивается на единицу.
   *
   * @param aArrayIndex int - индекс элемента массива в пределах 0 .. {@link #arrayLength()}
   * @param aArrayElement {@link IAvTree} - новый элемент массива
   * @throws TsUnsupportedFeatureRtException дерево является единичным (то есть {@link #isArray()} = false)
   * @throws TsIllegalArgumentRtException индекс массива выходит за допустимые пределы
   * @throws TsNullArgumentRtException aArrayElement = null
   * @throws TsIllegalArgumentRtException aArrayElement является массивом, а должен быть единичным деревом
   */
  void insertElement( int aArrayIndex, IAvTree aArrayElement );

  /**
   * Меняет элемент массива.
   * <p>
   * Размер массива {@link #arrayLength()} не меняется.
   *
   * @param aArrayIndex int - индекс элемента массива в пределах 0 .. {@link #arrayLength()}-1
   * @param aArrayElement {@link IAvTree} - новый элемент массива
   * @throws TsUnsupportedFeatureRtException дерево является единичным (то есть {@link #isArray()} = false)
   * @throws TsIllegalArgumentRtException индекс массива выходит за допустимые пределы
   * @throws TsNullArgumentRtException aArrayElement = null
   * @throws TsIllegalArgumentRtException aArrayElement является массивом, а должен быть единичным деревом
   */
  void setElement( int aArrayIndex, IAvTree aArrayElement );

  /**
   * Удаляет указанный элемент из массива.
   * <p>
   * Размер массива {@link #arrayLength()} уменьшается на единицу.
   *
   * @param aArrayIndex int - индекс элемента массива в пределах 0 .. {@link #arrayLength()}-1
   * @throws TsUnsupportedFeatureRtException дерево является единичным (то есть {@link #isArray()} = false)
   * @throws TsIllegalArgumentRtException индекс массива выходит за допустимые пределы
   */
  void removeElement( int aArrayIndex );

  /**
   * Очищает массив (удаляет все элементы).
   * <p>
   * Размер массива {@link #arrayLength()} становиться 0.
   *
   * @throws TsUnsupportedFeatureRtException дерево является единичным (то есть {@link #isArray()} = false)
   */
  void clearElemets();

}

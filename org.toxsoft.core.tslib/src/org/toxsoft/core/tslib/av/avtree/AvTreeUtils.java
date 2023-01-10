package org.toxsoft.core.tslib.av.avtree;

import static org.toxsoft.core.tslib.av.avtree.ITsResources.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Вспомогательные мтеоды для работы с деревом значений
 *
 * @author hazard157
 */
public class AvTreeUtils {

  /**
   * Создает дерево значений по умолчанию на основе описания.
   * <p>
   * В описании полей дерева значение по умолчанию "зашивается" в описании типа {@link IAvTreeFieldInfo#fieldType()}.
   * Ожидаетя, что в описании типа {@link IDataType} приустсвует ограничение
   * {@link IAvMetaConstants#TSID_DEFAULT_VALUE}. Если такое значение пристутсвует, то оно и будет значением
   * соответствующего поля в возвращаемом дереве.
   * <p>
   * Если значение по умолчанию отсутствует, то поведение метода зависит от аргумента aForceTypeDefaults. При
   * aForceTypeDefaults = false, метод выбрасывает исключение. При aForceTypeDefaults=true, в качестве значения будет
   * использоваться типовое значение {@link EAtomicType#defaultValue()}.
   *
   * @param aTreeInfo {@link IAvTreeInfo} - описание дерева
   * @param aForceTypeDefaults boolean - признак использования типовых значений при отсутствии значения по умолчанию в
   *          типе данных
   * @return {@link IAvTreeEdit} - дерево со структурой и значениеями по умолчанию из описания aTreeInfo
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalStateRtException при aForceTypeDefaults=false, описание одного из полей не содержит значение по
   *           умолчанию
   */
  public static IAvTreeEdit createDefaultTree( IAvTreeInfo aTreeInfo, boolean aForceTypeDefaults ) {
    TsNullArgumentRtException.checkNull( aTreeInfo );
    return doCreateDefaultTree( aTreeInfo, aForceTypeDefaults );
  }

  // ------------------------------------------------------------------------------------
  // Работа с путями
  //

  /**
   * Возвращает последний узел, на который ссылается путь.
   * <p>
   * Для пути, который ссылается на узел, возвращает этот узел. Для пути, который ссылается на поле, возвращает узел,
   * который содержит это поле. Для пустого пути возвращает аргумент aTree.
   *
   * @param aTree {@link IAvTree} - дерево, в которой ищеться узел
   * @param aPath {@link IAvTreePath} - путь в дереве
   * @return {@link IAvTree} - найденный узел
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsItemNotFoundRtException в пути есть идентификатор несуществующего узла
   */
  public static IAvTree getLastNode( IAvTree aTree, IAvTreePath aPath ) {
    TsNullArgumentRtException.checkNulls( aTree, aPath );
    return stepToLastNode( aTree, aPath, 0 );
  }

  /**
   * Возвращает последний узел, на который ссылается путь.
   * <p>
   * Для пути, который ссылается на узел, возвращает этот узел. Для пути, который ссылается на поле, возвращает узел,
   * который содержит это поле. Для пустого пути возвращает аргумент aTree.
   *
   * @param aTree {@link IAvTreeEdit} - дерево, в которой ищеться узел
   * @param aPath {@link IAvTreePath} - путь в дереве
   * @return {@link IAvTreeEdit} - найденный узел
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsItemNotFoundRtException в пути есть идентификатор несуществующего узла
   */
  public static IAvTreeEdit getLastNode( IAvTreeEdit aTree, IAvTreePath aPath ) {
    TsNullArgumentRtException.checkNulls( aTree, aPath );
    return stepToLastNode( aTree, aPath, 0 );
  }

  /**
   * Возвращает набор полей, на который ссылается путь.
   * <p>
   * Если путь ссылается на узел, то возвращает поля этого узла. Если путь ссылается на поле, то возвращает набор полей,
   * среди которых и находится ссылаемое поле. Если даже нет поля с таким идентификатором, не выбрасывает исключение, а
   * возвращает набор полей.
   *
   * @param aTree {@link IAvTree} - дерево, в которой ищеться узел
   * @param aPath {@link IAvTreePath} - путь в дереве
   * @return {@link IOptionSet} - набор полей
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsItemNotFoundRtException в пути есть идентификатор несуществующего узла
   */
  public static IOptionSet getFields( IAvTree aTree, IAvTreePath aPath ) {
    TsNullArgumentRtException.checkNulls( aTree, aPath );
    return stepToLastNode( aTree, aPath, 0 ).fields();
  }

  /**
   * Возвращает набор полей, на который ссылается путь.
   * <p>
   * Если путь ссылается на узел, то возвращает поля этого узла. Если путь ссылается на поле, то возвращает набор полей,
   * среди которых и находится ссылаемое поле. Если даже нет поля с таким идентификатором, не выбрасывает исключение, а
   * возвращает редактируемый набор - может пользователь желает добавить это поле.
   *
   * @param aTree {@link IAvTreeEdit} - дерево, в которой ищеться узел
   * @param aPath {@link IAvTreePath} - путь в дереве
   * @return {@link IOptionSetEdit} - редактируемый набор полей
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsItemNotFoundRtException в пути есть идентификатор несуществующего узла
   */
  public static IOptionSetEdit getFields( IAvTreeEdit aTree, IAvTreePath aPath ) {
    TsNullArgumentRtException.checkNulls( aTree, aPath );
    return stepToLastNode( aTree, aPath, 0 ).fieldsEdit();
  }

  /**
   * Возвращает значение поля, на которое ссылается путь.
   *
   * @param aTree {@link IAvTree} - дерево, в котором ищеться значение
   * @param aPath {@link IAvTreePath} - искомый путь
   * @return {@link IAtomicValue} - значение искомого поля
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException путь ссылается на узел, не на поле
   * @throws TsItemNotFoundRtException в пути есть идентификатор несуществующего узла / поля
   */
  public static IAtomicValue getValue( IAvTree aTree, IAvTreePath aPath ) {
    TsNullArgumentRtException.checkNulls( aTree, aPath );
    TsIllegalStateRtException.checkTrue( aPath.isNode() );
    return stepToLastNode( aTree, aPath, 0 ).fields().getValue( aPath.lastElement().id() );
  }

  /**
   * Задает значение поля, на который ссылается путь.
   *
   * @param aTree {@link IAvTree} - дерево, в котором ищеться поле
   * @param aPath {@link IAvTreePath} - искомый путь
   * @param aValue {@link IAtomicValue} - новое значение поля
   * @param aCreateNonExistant boolean - добавлять ли новое поле, если не существует запрошенное<br>
   *          <b>true</b> - да, если нет поля с идентификатором последнего элемента пути, он будет создан;<br>
   *          <b>false</b> - нет, если нет поля с идентификатором последнего элемента пути, то будет исключение.
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException путь ссылается на узел, не на поле
   * @throws TsItemNotFoundRtException в пути есть идентификатор несуществующего узла
   * @throws TsItemNotFoundRtException нет поля с заданным идентификатором и aCreateNonExistant = false
   */
  public static void setValue( IAvTreeEdit aTree, IAvTreePath aPath, IAtomicValue aValue, boolean aCreateNonExistant ) {
    TsNullArgumentRtException.checkNulls( aTree, aPath, aValue );
    TsIllegalArgumentRtException.checkTrue( aPath.isNode() );
    IOptionSetEdit fields = getFields( aTree, aPath );
    if( !aCreateNonExistant ) {
      TsItemNotFoundRtException.checkFalse( fields.hasValue( aPath.lastElement().id() ) );
    }
    fields.setValue( aPath.lastElement().id(), aValue );
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  /**
   * Рекурсивно спускается до последнего узла в пути.
   * <p>
   * Для пути, который ссылается на узел, возвращает этот узел. Для пути, который ссылается на поле, возвращает узел,
   * который содержит это поле. Для пустого пути возвращает аргумент aTree.
   *
   * @param aTree {@link IAvTree} - под-дерево, по которому спускается
   * @param aPath {@link IAvTreePath} - путь в дереве
   * @param aPathElemIndex int - индекс рассматриваемого элемента в пути (увеличивается на 1 на каждом рекурсивном
   *          вызове)
   * @return {@link IAvTree} - найденный узел
   * @throws TsItemNotFoundRtException в пути есть идентификатор несуществующего узла
   * @throws TsItemNotFoundRtException индекс массива {@link IAvTreePathElement#arrayElementIndex()} выходит за размеры
   *           массива
   * @throws TsIllegalStateRtException элемент пути ожидает массив, а встретил единичное дерево
   * @throws TsIllegalStateRtException элемент пути ожидает единичное дерево, а встретил массив
   */
  private static IAvTree stepToLastNode( IAvTree aTree, IAvTreePath aPath, int aPathElemIndex ) {
    if( aPathElemIndex >= aPath.elements().size() ) {
      return aTree;
    }
    IAvTreePathElement pathElem = aPath.elements().get( aPathElemIndex );
    if( pathElem.isArrayElement() != aTree.isArray() ) { // разногласие - ожидания пути насчет массива не оправдалось
      if( pathElem.isArrayElement() ) {
        throw new TsIllegalStateRtException( STR_ERR_TREE_ARRAY_EXPECTED_IN_PATH );
      }
      throw new TsIllegalStateRtException( STR_ERR_SINGLE_TREE_EXPECTED_IN_PATH );
    }
    if( pathElem.isArrayElement() ) {
      return aTree.arrayElement( pathElem.arrayElementIndex() );
    }
    if( !pathElem.isNode() ) {
      return aTree;
    }
    return stepToLastNode( aTree.nodes().getByKey( pathElem.id() ), aPath, aPathElemIndex + 1 );
  }

  /**
   * Рекурсивно спускается до последнего узла в пути.
   * <p>
   * Для пути, который ссылается на узел, возвращает этот узел. Для пути, который ссылается на поле, возвращает узел,
   * который содержит это поле. Для пустого пути возвращает аргумент aTree.
   *
   * @param aTree {@link IAvTreeEdit} - под-дерево, по которому спускается
   * @param aPath {@link IAvTreePath} - путь в дереве
   * @param aPathElemIndex int - индекс рассматриваемого элемента в пути (увеличивается на 1 на каждом рекурсивном
   *          вызове)
   * @return {@link IAvTreeEdit} - найденный узел
   * @throws TsItemNotFoundRtException в пути есть идентификатор несуществующего узла
   * @throws TsIllegalStateRtException элемент пути сслыается на массив, что недопустимо при ходьбе по описанию
   */
  private static IAvTreeEdit stepToLastNode( IAvTreeEdit aTree, IAvTreePath aPath, int aPathElemIndex ) {
    if( aPathElemIndex >= aPath.elements().size() ) {
      return aTree;
    }
    IAvTreePathElement elem = aPath.elements().get( aPathElemIndex );
    TsIllegalStateRtException.checkTrue( elem.isArrayElement(), STR_ERR_ARRAY_REF_CANT_BE_IN_INFO_PATH );
    if( !elem.isNode() ) {
      return aTree;
    }
    return stepToLastNode( aTree.nodeEdit( elem.id() ), aPath, aPathElemIndex + 1 );
  }

  private static IAvTreeEdit doCreateDefaultTree( IAvTreeInfo aTreeInfo, boolean aForceTypeDefaults ) {
    // если дерево - массив, создаем и возвращаем пустой массив
    if( aTreeInfo.isArray() ) {
      return AvTree.createArrayAvTree();
    }
    // если дерево - семейство, создаем и возвращаем дерево без полей и узлов
    if( aTreeInfo.isFamily() ) {
      return AvTree.createSingleAvTree( EMPTY_STRING, IOptionSet.NULL, IStringMap.EMPTY );
    }
    // извлечем значения по умолчанию из описаний полей
    IOptionSetEdit fields = new OptionSet();
    for( IAvTreeFieldInfo fieldInfo : aTreeInfo.fieldInfoes() ) {
      IOptionSet tc = fieldInfo.fieldType().params();
      if( tc.hasValue( TSID_DEFAULT_VALUE ) ) {
        fields.setValue( fieldInfo.id(), tc.getValue( DDEF_DEFAULT_VALUE ) );
      }
      else {
        if( aForceTypeDefaults ) {
          fields.setValue( fieldInfo.id(), fieldInfo.fieldType().atomicType().defaultValue() );
        }
        else {
          throw new TsIllegalStateRtException();
        }
      }
    }
    // добавим значения узлов
    IStringMapEdit<IAvTree> nodes = new StringMap<>();
    for( IAvTreeInfo treeInfo : aTreeInfo.nodeInfoes() ) {
      nodes.put( treeInfo.id(), doCreateDefaultTree( treeInfo, aForceTypeDefaults ) );
    }
    return AvTree.createSingleAvTree( EMPTY_STRING, fields, nodes );
  }

  /**
   * Запрет на создание экземпляров.
   */
  private AvTreeUtils() {
    // пустой конструктор
  }
}

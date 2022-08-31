package org.toxsoft.core.tslib.av.avtree;

import static org.toxsoft.core.tslib.av.avtree.ITsResources.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Реализация единичного {@link IAvTree}, у которого {@link #isArray()} = <code>false</code>.
 *
 * @author goga
 */
class AvTreeSingle
    extends AvTree {

  private static final long serialVersionUID = 157157L;

  private final IOptionSetEdit          fields   = new OptionSet();
  private final IStringMapEdit<IAvTree> nodes    = new StringMap<>();
  private String                        structId = EMPTY_STRING;

  /**
   * Создает дерево значений со всеми инвариантами.
   *
   * @param aStructId String - идентификатор структуры (ИД-путь) или пустая строка
   * @param aFields {@link IOptionSet} - поля этого узла (корня) дерева
   * @param aNodes IStringMap&lt;{@link IAvTree}&gt; - узлы дерева
   * @param aCopyNodes boolean - признак копирования узлов, а не запоминаня ссылок
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException aStructId не пустая строка и не валидный ИД-путь
   * @throws TsIllegalArgumentRtException один из идентификаторов aNodes не ИД-путь
   */
  AvTreeSingle( String aStructId, IOptionSet aFields, IStringMap<IAvTree> aNodes, boolean aCopyNodes ) {
    TsNullArgumentRtException.checkNull( aNodes );
    setStructId( aStructId );
    fields.setAll( aFields );
    if( aCopyNodes ) {
      for( String id : aNodes.keys() ) {
        IAvTree node = AvTree.createCopy( aNodes.getByKey( id ) );
        nodes.put( id, node );
      }
    }
    else {
      nodes.putAll( aNodes );
    }
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
    return structId;
  }

  @Override
  public IOptionSet fields() {
    return fields;
  }

  @Override
  public IStringMap<IAvTree> nodes() {
    return nodes;
  }

  @Override
  public int arrayLength() {
    throw new TsUnsupportedFeatureRtException( STR_ERR_NOT_A_ARRAY_TREE );
  }

  @Override
  public IAvTree arrayElement( int aArrayIndex ) {
    throw new TsUnsupportedFeatureRtException( STR_ERR_NOT_A_ARRAY_TREE );
  }

  // ------------------------------------------------------------------------------------
  // IAvTreeEdit
  //

  @Override
  public void setStructId( String aStructId ) {
    TsNullArgumentRtException.checkNull( aStructId );
    if( !aStructId.isEmpty() ) {
      StridUtils.checkValidIdPath( aStructId );
    }
    structId = aStructId;
  }

  @Override
  public IOptionSetEdit fieldsEdit() {
    return fields;
  }

  @Override
  public IAvTreeEdit nodeEdit( String aNodeId ) {
    IAvTree node = nodes.getByKey( aNodeId );
    TsUnsupportedFeatureRtException.checkFalse( node instanceof IAvTreeEdit );
    return (IAvTreeEdit)node;
  }

  @Override
  public void putNode( String aNodeId, IAvTree aNode ) {
    nodes.put( aNodeId, aNode );
  }

  @Override
  public void removeNode( String aNodeId ) {
    nodes.removeByKey( aNodeId );
  }

  @Override
  public void clearNodes() {
    nodes.clear();
  }

  @Override
  public IAvTreeEdit getElementEdit( int aArrayIndex ) {
    throw new TsUnsupportedFeatureRtException( STR_ERR_NOT_A_ARRAY_TREE );
  }

  @Override
  public void addElement( IAvTree aArrayElement ) {
    throw new TsUnsupportedFeatureRtException( STR_ERR_NOT_A_ARRAY_TREE );
  }

  @Override
  public void insertElement( int aArrayIndex, IAvTree aArrayElement ) {
    throw new TsUnsupportedFeatureRtException( STR_ERR_NOT_A_ARRAY_TREE );
  }

  @Override
  public void setElement( int aArrayIndex, IAvTree aArrayElement ) {
    throw new TsUnsupportedFeatureRtException( STR_ERR_NOT_A_ARRAY_TREE );
  }

  @Override
  public void removeElement( int aArrayIndex ) {
    throw new TsUnsupportedFeatureRtException( STR_ERR_NOT_A_ARRAY_TREE );
  }

  @Override
  public void clearElemets() {
    throw new TsUnsupportedFeatureRtException( STR_ERR_NOT_A_ARRAY_TREE );
  }

}

package org.toxsoft.core.tslib.av.avtree;

import static org.toxsoft.core.tslib.av.avtree.ITsResources.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Реализация {@link IAvTree} - массива, у которого {@link #isArray()} = <code>true</code>.
 *
 * @author goga
 */
class AvTreeArray
    extends AvTree {

  private static final long serialVersionUID = 157157L;

  /**
   * Список, используемый для представления массива эжлементов.
   */
  private final IListEdit<IAvTreeEdit> elements = new ElemLinkedBundleList<>();

  /**
   * Создает пустой массив.
   */
  AvTreeArray() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // IAvTree
  //

  @Override
  public boolean isArray() {
    return true;
  }

  @Override
  public String structId() {
    throw new TsUnsupportedFeatureRtException( STR_ERR_NOT_A_SINGLE_TREE );
  }

  @Override
  public IOptionSet fields() {
    throw new TsUnsupportedFeatureRtException( STR_ERR_NOT_A_SINGLE_TREE );
  }

  @Override
  public IStringMap<IAvTree> nodes() {
    throw new TsUnsupportedFeatureRtException( STR_ERR_NOT_A_SINGLE_TREE );
  }

  @Override
  public int arrayLength() {
    return elements.size();
  }

  @Override
  public IAvTree arrayElement( int aArrayIndex ) {
    return elements.get( aArrayIndex );
  }

  // ------------------------------------------------------------------------------------
  // IAvTreeEdit
  //

  @Override
  public void setStructId( String aStructId ) {
    throw new TsUnsupportedFeatureRtException( STR_ERR_NOT_A_SINGLE_TREE );
  }

  @Override
  public IOptionSetEdit fieldsEdit() {
    throw new TsUnsupportedFeatureRtException( STR_ERR_NOT_A_SINGLE_TREE );
  }

  @Override
  public IAvTreeEdit nodeEdit( String aNodeId ) {
    throw new TsUnsupportedFeatureRtException( STR_ERR_NOT_A_SINGLE_TREE );
  }

  @Override
  public void putNode( String aNodeId, IAvTree aNode ) {
    throw new TsUnsupportedFeatureRtException( STR_ERR_NOT_A_SINGLE_TREE );
  }

  @Override
  public void removeNode( String aNodeId ) {
    throw new TsUnsupportedFeatureRtException( STR_ERR_NOT_A_SINGLE_TREE );
  }

  @Override
  public void clearNodes() {
    throw new TsUnsupportedFeatureRtException( STR_ERR_NOT_A_SINGLE_TREE );
  }

  @Override
  public IAvTreeEdit getElementEdit( int aArrayIndex ) {
    return elements.get( aArrayIndex );
  }

  @Override
  public void addElement( IAvTree aArrayElement ) {
    TsNullArgumentRtException.checkNull( aArrayElement );
    TsIllegalArgumentRtException.checkTrue( aArrayElement.isArray(), STR_ERR_CANT_ADD_ARRAY_TREE_TO_ARRAY );
    elements.add( (IAvTreeEdit)aArrayElement );
  }

  @Override
  public void insertElement( int aArrayIndex, IAvTree aArrayElement ) {
    TsNullArgumentRtException.checkNull( aArrayElement );
    TsIllegalArgumentRtException.checkTrue( aArrayElement.isArray(), STR_ERR_CANT_ADD_ARRAY_TREE_TO_ARRAY );
    elements.insert( aArrayIndex, (IAvTreeEdit)aArrayElement );
  }

  @Override
  public void setElement( int aArrayIndex, IAvTree aArrayElement ) {
    TsNullArgumentRtException.checkNull( aArrayElement );
    TsIllegalArgumentRtException.checkTrue( aArrayElement.isArray(), STR_ERR_CANT_ADD_ARRAY_TREE_TO_ARRAY );
    elements.set( aArrayIndex, (IAvTreeEdit)aArrayElement );
  }

  @Override
  public void removeElement( int aArrayIndex ) {
    elements.removeByIndex( aArrayIndex );
  }

  @Override
  public void clearElemets() {
    elements.clear();
  }

}

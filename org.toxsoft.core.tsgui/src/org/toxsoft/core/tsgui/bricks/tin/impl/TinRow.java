package org.toxsoft.core.tsgui.bricks.tin.impl;

import java.util.*;

import org.eclipse.jface.viewers.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITinRow} implementation.
 *
 * @author hazard157
 */
public class TinRow
    implements ITinRow {

  protected final TreeViewer treeViewer;

  private final TinRow                      parent;
  private final ITinFieldInfo               fieldInfo;
  private final IStridablesList<TinRow>     allChildRows;
  private final IStridablesListEdit<TinRow> visibleChildRows = new StridablesList<>();

  private ITinValue tinValue = ITinValue.NULL;

  /**
   * Constructor.
   *
   * @param aParnetNode {@link TinRow} - parent node
   * @param aFieldInfo {@link ITinFieldInfo} - the field info of this node
   * @param aTreeViewer {@link TreeViewer} - the owner viewer
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TinRow( TinRow aParnetNode, ITinFieldInfo aFieldInfo, TreeViewer aTreeViewer ) {
    TsNullArgumentRtException.checkNulls( aParnetNode, aFieldInfo, aTreeViewer );
    treeViewer = aTreeViewer;
    parent = aParnetNode;
    fieldInfo = aFieldInfo;
    allChildRows = internalCreateAllChildNodes();
    visibleChildRows.addAll( allChildRows );
  }

  /**
   * Constructor for root node.
   *
   * @param aFieldInfo {@link ITinFieldInfo} - the field info of this node
   * @param aTreeViewer {@link TreeViewer} - the owner viewer
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  TinRow( ITinFieldInfo aFieldInfo, TreeViewer aTreeViewer ) {
    TsNullArgumentRtException.checkNulls( aFieldInfo, aTreeViewer );
    treeViewer = aTreeViewer;
    parent = null;
    fieldInfo = aFieldInfo;
    allChildRows = internalCreateAllChildNodes();
    visibleChildRows.addAll( allChildRows );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private final ITinTypeInfo typeInfo() {
    return fieldInfo.typeInfo();
  }

  private IStridablesList<TinRow> internalCreateAllChildNodes() {
    IStridablesListEdit<TinRow> ll = new StridablesList<>();
    for( ITinFieldInfo finf : typeInfo().fieldInfos() ) {
      TinRow node = new TinRow( this, finf, treeViewer );
      ll.put( finf.id(), node );
    }
    return ll;
  }

  private void internalPropagateValueDownSubtree( ITinValue aValue ) {
    if( Objects.equals( tinValue, aValue ) ) {
      return;
    }
    tinValue = aValue;
    treeViewer.update( this, null ); // TODO maybe remember rows and update at once?
    if( tinValue.kind().hasChildren() ) {
      for( String fId : tinValue.childValues().keys() ) {
        TinRow childNode = allChildRows.getByKey( fId );
        ITinValue childValue = tinValue.childValues().getByKey( fId );
        childNode.setTinValue( childValue );
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // package API
  //

  /**
   * Method informs this node that child field value has been changed by in-cell editor.
   * <p>
   * Called by the child node when it's value changes due to user editing in the inspector widget.
   * <p>
   * TODO describe what is done by implementation
   *
   * @param aFieldId String - ID of field with changed value
   */
  void papiChildValueChangedByValed( String aFieldId ) {
    // prepare changed child values
    IStringMapEdit<ITinValue> childValues = new StringMap<>();
    for( String fid : allChildRows.keys() ) {
      ITinRow childRow = allChildRows.getByKey( fid );
      ITinValue childValue = childRow.getTinValue();
      childValues.put( fid, childValue );
    }
    // set new value of this node (without propagating down subtree)
    ITinValue newValue;
    if( typeInfo().kind().hasAtomic() ) {
      IAtomicValue av = typeInfo().compose( childValues );
      newValue = TinValue.ofFull( av, childValues );
    }
    else {
      newValue = TinValue.ofGroup( childValues );
    }
    tinValue = newValue;
    treeViewer.update( this, null );
    if( parent != null ) {
      // convert from child field ID to this field ID
      parent.papiChildValueChangedByValed( fieldInfo.id() );
    }
  }

  void papiRecursivelyUpdateVisibleChildNodesFromCurrentNodeValue() {
    visibleChildRows.clear();
    IStringList newVisibleChildIds = typeInfo().visibleFieldIds( tinValue );
    for( String fId : newVisibleChildIds ) {
      visibleChildRows.add( allChildRows.getByKey( fId ) );
    }
    for( TinRow row : allChildRows ) {
      row.papiRecursivelyUpdateVisibleChildNodesFromCurrentNodeValue();
    }
  }

  // ------------------------------------------------------------------------------------
  // IStridable
  //

  @Override
  public String id() {
    return fieldInfo.id();
  }

  @Override
  public String nmName() {
    return fieldInfo.nmName();
  }

  @Override
  public String description() {
    return fieldInfo.description();
  }

  // ------------------------------------------------------------------------------------
  // ITinNode
  //

  @Override
  public TinTopRow root() {
    return parent.root();
  }

  @Override
  public ITinRow parent() {
    return parent;
  }

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  @Override
  public IStridablesList<ITinRow> allChildren() {
    return (IStridablesList)allChildRows;
  }

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  @Override
  public IStridablesList<ITinRow> visibleChildren() {
    return (IStridablesList)visibleChildRows;
  }

  @Override
  public ITinFieldInfo fieldInfo() {
    return fieldInfo;
  }

  @Override
  public boolean canEdit() {
    if( !typeInfo().kind().hasAtomic() ) {
      return false;
    }
    // what else may NOT allow to edit?
    return true;
  }

  @Override
  public IValedControl<IAtomicValue> createValed( ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    TsUnsupportedFeatureRtException.checkFalse( canEdit() );
    ITsGuiContext ctx = new TsGuiContext( aContext );
    ctx.params().addAll( typeInfo().dataType().params() );
    ctx.params().addAll( fieldInfo.params() );
    IValedControlFactoriesRegistry vcfRegistry = ctx.get( IValedControlFactoriesRegistry.class );
    IValedControlFactory valedFactory = vcfRegistry.getSuitableAvEditor( typeInfo().dataType().atomicType(), ctx );
    return valedFactory.createEditor( ctx );
  }

  @Override
  public IAtomicValue getAtomicValueForValed() {
    TsUnsupportedFeatureRtException.checkFalse( canEdit() );
    // do we need to check if value is up to date - no, value must be up to date
    return tinValue.atomicValue();
  }

  @Override
  public ValidationResult canSetAtomicValueFromValed( IAtomicValue aValue ) {
    ValidationResult vr = typeInfo().canDecompose( aValue );
    return vr;
  }

  @Override
  public void setAtomicValueFromValed( IAtomicValue aValue ) {
    TsValidationFailedRtException.checkError( canSetAtomicValueFromValed( aValue ) );
    if( typeInfo().kind().hasChildren() ) {
      IStringMap<ITinValue> childValues = typeInfo().decompose( aValue );
      internalPropagateValueDownSubtree( TinValue.ofFull( aValue, childValues ) );
    }
    else {
      ITinValue newValue = TinValue.ofAtomic( aValue );
      if( !Objects.equals( tinValue, newValue ) ) {
        tinValue = newValue;
        treeViewer.update( this, null ); // TODO maybe remember rows and update at once?
      }
    }
    if( parent != null ) {
      // TODO what if this node is root?
      parent.papiChildValueChangedByValed( fieldInfo.id() );
    }
    root().refreshSubtreeStructure();
  }

  @Override
  public ITinValue getTinValue() {
    return tinValue;
  }

  @Override
  public void setTinValue( ITinValue aValue ) {
    TsNullArgumentRtException.checkNull( aValue );
    internalPropagateValueDownSubtree( aValue );
  }

}

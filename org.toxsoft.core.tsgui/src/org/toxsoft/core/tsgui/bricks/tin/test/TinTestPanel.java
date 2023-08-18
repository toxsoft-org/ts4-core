package org.toxsoft.core.tsgui.bricks.tin.test;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.bricks.tstree.*;
import org.toxsoft.core.tsgui.bricks.tstree.impl.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * TIN test panel contains left objects list and right object inspector.
 *
 * @author hazard157
 */
@SuppressWarnings( { "javadoc", "nls" } )
public class TinTestPanel
    extends TsPanel {

  private static final ITsNodeKind<ObjToInspect> LEFT_TREE_NODE_KIND =
      new TsNodeKind<>( "ObjToInspect", ObjToInspect.class, false );

  private final IListEdit<ObjToInspect> objsList = new ElemArrayList<>();

  private final ITsTreeViewer leftViewer;
  private final ITinWidget    objInspector;

  public TinTestPanel( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    this.setLayout( new BorderLayout() );
    SashForm sfMain = new SashForm( this, SWT.HORIZONTAL );
    sfMain.setLayoutData( BorderLayout.CENTER );
    TsGuiContext ctx;
    // left
    ctx = new TsGuiContext( tsContext() );
    leftViewer = new TsTreeViewer( ctx );
    leftViewer.createControl( sfMain );
    leftViewer.addColumn( "Name", EHorAlignment.LEFT, aItem -> ((ObjToInspect)aItem.entity()).name() );
    // right
    ctx = new TsGuiContext( tsContext() );
    objInspector = new TinWidget( ctx );
    objInspector.createControl( sfMain );
    // setup
    sfMain.setWeights( 3000, 7000 );
    leftViewer.addTsSelectionListener( ( src, sel ) -> whenLeftSelectionChanges() );
    objInspector.genericChangeEventer().addListener( src -> whenRightInspectorChanges() );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void whenLeftSelectionChanges() {
    ITsNode selNode = leftViewer.selectedItem();
    if( selNode != null && selNode.kind() == LEFT_TREE_NODE_KIND ) {
      ObjToInspect sel = ObjToInspect.class.cast( selNode.entity() );
      objInspector.setEntityInfo( sel.entityInfo() );
      ITinValue tv = sel.entityInfo().makeValue( sel.entity() );
      objInspector.setValue( tv );
    }
    else {
      objInspector.setEntityInfo( null );
      return;
    }
  }

  private void whenRightInspectorChanges() {
    ITsNode selNode = leftViewer.selectedItem();
    if( selNode != null && selNode.kind() == LEFT_TREE_NODE_KIND ) {
      ValidationResult vr = objInspector.canGetValue();
      if( !vr.isError() ) {
        ObjToInspect sel = ObjToInspect.class.cast( selNode.entity() );
        sel.setEntity( sel.entityInfo().makeEntity( objInspector.getValue() ) );
        leftViewer.console().refresh( selNode );
        TsTestUtils.pl( "Updated node %s", selNode );
      }
      if( !vr.isOk() ) {
        TsTestUtils.pl( "%s: %s", vr.type().nmName(), vr.message() );
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  public void addObject( ObjToInspect aObj ) {
    IListEdit<ITsNode> rootNodes = new ElemArrayList<>();
    objsList.add( aObj );
    for( ObjToInspect o : objsList ) {
      DefaultTsNode<ObjToInspect> node = new DefaultTsNode<>( LEFT_TREE_NODE_KIND, leftViewer, o );
      rootNodes.add( node );
    }
    leftViewer.setRootNodes( rootNodes );
  }

}

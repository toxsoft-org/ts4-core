package org.toxsoft.core.tsgui.m5.std.models.misc;

import static org.toxsoft.core.tsgui.m5.std.models.misc.ITsResources.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;

class ValidationResultMpc
    extends MultiPaneComponentModown<ValidationResult> {

  static final ITsNodeKind<EValidationResultType> NK_TYPE = new TsNodeKind<>( "VrType", //$NON-NLS-1$
      EValidationResultType.class, true );

  static final ITsNodeKind<ValidationResult> NK_VR = new TsNodeKind<>( "ValRes", //$NON-NLS-1$
      ValidationResult.class, true );

  static class TmByType
      implements ITsTreeMaker<ValidationResult> {

    @SuppressWarnings( { "unchecked", "rawtypes" } )
    @Override
    public IList<ITsNode> makeRoots( ITsNode aRootNode, IList<ValidationResult> aItems ) {
      // make roots map
      IMapEdit<EValidationResultType, DefaultTsNode<EValidationResultType>> rootsMap = new ElemMap<>();
      for( EValidationResultType t : EValidationResultType.asList() ) {
        DefaultTsNode<EValidationResultType> node = new DefaultTsNode<>( NK_TYPE, aRootNode, t );
        node.setName( t.nmName() );
        node.setIconId( t.iconId() );
        rootsMap.put( t, node );
      }
      // put items as childs of root nodes
      for( ValidationResult vr : aItems ) {
        DefaultTsNode<EValidationResultType> root = rootsMap.getByKey( vr.type() );
        DefaultTsNode<ValidationResult> node = new DefaultTsNode<>( NK_VR, root, vr );
        node.setName( vr.message() );
        root.addNode( node );
      }
      return (IList)rootsMap.values();
    }

    @Override
    public boolean isItemNode( ITsNode aNode ) {
      return aNode.kind() == NK_VR;
    }

  }

  public ValidationResultMpc( ITsGuiContext aContext, IM5Model<ValidationResult> aModel,
      IM5ItemsProvider<ValidationResult> aItemsProvider ) {
    super( aContext, aModel, aItemsProvider );
    TreeModeInfo<ValidationResult> tmiByType = new TreeModeInfo<>( "ByType", //$NON-NLS-1$
        STR_N_TMI_VR_BY_TYPE, STR_D_TMI_VR_BY_TYPE, EValidationResultType.OK.iconId(), new TmByType() );
    treeModeManager().addTreeMode( tmiByType );
  }

}

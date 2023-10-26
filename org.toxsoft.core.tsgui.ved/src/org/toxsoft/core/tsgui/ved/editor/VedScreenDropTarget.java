package org.toxsoft.core.tsgui.ved.editor;

import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;

import org.eclipse.swt.dnd.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.ved.incub.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strid.idgen.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link DropTarget} implementation accepts drops of the VED items to create the new instance.
 * <p>
 * This drop target accepts only {@link TextTransfer#getInstance()} transfer type. The value of the DND data is KTOR
 * representation of {@link IVedItemCfg}, created by the method {@link IEntityKeeper#ent2str(Object)}. The target
 * assumes that {@link DropTargetEvent#data} is set to mentioned KTOR {@link String} and
 * {@link DropTargetEvent#operations} contains {@link DND#DROP_COPY}.
 * <p>
 * Usage: to make the VED screen a drop target following steps must be done:
 * <ul>
 * <li>create the instance of the {@link VedScreenDropTarget};</li>
 * <li>after screen is attached to the canvas, attach target to the screen by the method
 * {@link #attachToScreen(IVedScreen)}.</li>
 * </ul>
 *
 * @author hazard157
 */
public class VedScreenDropTarget {

  /**
   * {@link DropTargetListener} implementation.
   *
   * @author hazard157
   */
  class InternalDropTargetListener
      extends DropTargetAdapter {

    private static TransferData findSupportedTypeInDatas( Transfer aType, TransferData[] aDatas ) {
      for( TransferData d : aDatas ) {
        if( aType.isSupportedType( d ) ) {
          return d;
        }
      }
      return null;
    }

    private static IVedItemCfg tryToGetVedItemCfgInString( Object aTheDataThatMustBeTheString ) {
      // allas, the only way to check KTOR string validity is try to convert String->Entity and check for exceptions
      try {
        return VedItemCfg.KEEPER.str2ent( (String)aTheDataThatMustBeTheString );
      }
      catch( @SuppressWarnings( "unused" ) Exception ex ) {
        return null;
      }
    }

    @Override
    public void dragEnter( DropTargetEvent aEvent ) {
      // accept only default DND operations whendrag source supports COPY
      if( aEvent.detail != DND.DROP_DEFAULT || (aEvent.operations & DND.DROP_COPY) == 0 ) {
        aEvent.detail = DND.DROP_NONE;
        return;
      }
      // request copy (by the way: is this line of code necessary?)
      aEvent.detail = DND.DROP_COPY;
      // no supported data type in drag source, we do nothing
      TransferData td = findSupportedTypeInDatas( TextTransfer.getInstance(), aEvent.dataTypes );
      if( td == null ) {
        aEvent.detail = DND.DROP_NONE;
        return;
      }
      // in #drop() we want only supported transferred data
      aEvent.currentDataType = td;
    }

    @Override
    public void drop( DropTargetEvent aEvent ) {
      // check preconditions (may this is unnecessary check due to #dropEnter() processing?)
      if( !TextTransfer.getInstance().isSupportedType( aEvent.currentDataType ) ) {
        return;
      }
      IVedItemCfg cfg = tryToGetVedItemCfgInString( aEvent.data );
      if( cfg == null ) {
        return;
      }
      createVedItemAt( cfg, new TsPoint( aEvent.x, aEvent.y ) );
    }

  }

  private final IStridGenerator viselIdGen = new TimastampMsecStridGenaretor( EVedItemKind.VISEL.id() );
  private final IStridGenerator actorIdGen = new TimastampMsecStridGenaretor( EVedItemKind.ACTOR.id() );

  private IVedScreen vedScreen    = null;
  private boolean    invokeDialog = true;

  /**
   * Constructor.
   */
  public VedScreenDropTarget() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void createVedItemAt( IVedItemCfg aCfg, ITsPoint aCursorCoors ) {
    // create the requested kind of the VED item
    EVedItemKind kind = aCfg.kind();
    String factoryId = aCfg.factoryId();
    switch( kind ) {
      case VISEL: {
        Point p = vedScreen.view().getControl().toControl( aCursorCoors.x(), aCursorCoors.y() );
        ITsPoint cp = D2TransformUtils.toControl( p.x, p.y, ID2Conversion.NONE, vedScreen.view().getConversion() );
        IOptionSetEdit opSet = new OptionSet( aCfg.propValues() );
        opSet.setDouble( PROP_X, cp.x() );
        opSet.setDouble( PROP_Y, cp.y() );
        String viselId = viselIdGen.nextId() + System.currentTimeMillis();
        VedItemCfg viselCfg = VedItemCfg.ofVisel( viselId, factoryId, aCfg.params(), opSet );
        if( invokeDialog ) {
          viselCfg = VedEditorUtils.editVedItemBasicProperties( viselCfg, vedScreen );
          if( viselCfg == null ) {
            break;
          }
        }
        vedScreen.model().visels().create( viselCfg );
        vedScreen.view().redraw();
        break;
      }
      case ACTOR: {
        String actorId = actorIdGen.nextId() + System.currentTimeMillis();
        VedItemCfg actorCfg = new VedItemCfg( actorId, aCfg );
        if( invokeDialog ) {
          actorCfg = VedEditorUtils.editVedItemBasicProperties( actorCfg, vedScreen );
          if( actorCfg == null ) {
            break;
          }
        }
        vedScreen.model().actors().create( actorCfg );
        break;
      }
      default:
        throw new TsNotAllEnumsUsedRtException( kind.nmName() );
    }
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Makes VED screen SWT control drop the drop target.
   *
   * @param aVedScreen {@link IVedScreen} - the screen
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the canvas is not attaced to the argument screen
   * @throws TsIllegalStateRtException already attached
   */
  public void attachToScreen( IVedScreen aVedScreen ) {
    TsNullArgumentRtException.checkNull( aVedScreen );
    TsIllegalArgumentRtException.checkNull( aVedScreen.view().getControl() );
    TsIllegalStateRtException.checkNoNull( vedScreen );
    vedScreen = aVedScreen;
    Control swtControl = vedScreen.view().getControl();
    DropTarget target = new DropTarget( swtControl, DND.DROP_COPY | DND.DROP_DEFAULT );
    target.setTransfer( TextTransfer.getInstance() );
    target.addDropListener( new InternalDropTargetListener() );
  }

  /**
   * Determines if after drop event the item configuration dialog will be invoked.
   *
   * @return boolean - <code>true</code> to invoke VED item ID, name and description editing dialog
   */
  public boolean isCreateDialog() {
    return invokeDialog;
  }

  /**
   * Sets {@link #isCreateDialog()} value.
   *
   * @param aInvokeDialog boolean - <code>true</code> to invoke VED item ID, name and description editing dialog
   */
  public void setCreateDialog( boolean aInvokeDialog ) {
    invokeDialog = aInvokeDialog;
  }

}

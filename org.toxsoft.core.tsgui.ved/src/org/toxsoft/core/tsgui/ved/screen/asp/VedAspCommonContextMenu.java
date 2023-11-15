package org.toxsoft.core.tsgui.ved.screen.asp;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;

import org.toxsoft.core.tsgui.bricks.actions.asp.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.editor.*;
import org.toxsoft.core.tsgui.ved.editor.IVedViselSelectionManager.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

public class VedAspCommonContextMenu
    extends MethodPerActionTsActionSetProvider
    implements ITsGuiContextable {

  private final IVedScreen vedScreen;

  private final IVedViselSelectionManager selectionManager;

  private VedAbstractVisel activeVisel = null;

  /**
   * Constructor.
   *
   * @param aVedScreen {@link IVedScreen} - the screen to handle
   * @param aSelectionManager IVedViselSelectionManager - manager of the selected visels
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedAspCommonContextMenu( IVedScreen aVedScreen, IVedViselSelectionManager aSelectionManager ) {
    vedScreen = TsNullArgumentRtException.checkNull( aVedScreen );
    selectionManager = aSelectionManager;
    selectionManager.genericChangeEventer().addListener( this::onSelectionChanged );
    defineAction( ACDEF_REMOVE, this::doRemove );
    // defineAction( ACDEF_ALIGN_LEFT, this::doAlignLeft );
    // defineAction( ACDEF_ALIGN_RIGHT, this::doAlignRight );
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiConextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return vedScreen.tsContext();
  }

  @Override
  public boolean isActionEnabled( String aActionId ) {
    if( activeVisel == null ) {
      if( aActionId.equals( ACDEF_REMOVE.id() ) ) {
        return selectionManager.selectionKind() != ESelectionKind.NONE;
      }
    }
    return true;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  public void setActiveVisel( VedAbstractVisel aVisel ) {
    activeVisel = aVisel;
    actionsStateEventer().fireChangeEvent();
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  void doRemove() {
    if( (activeVisel == null) || selectionManager.isSelected( activeVisel.id() ) ) {
      deleteSelectedVisels();
    }
    else {
      vedScreen.model().visels().remove( activeVisel.id() );
    }
    activeVisel = null;
  }

  void onSelectionChanged( @SuppressWarnings( "unused" ) Object aSource ) {
    // if( selectionManager.selectionKind() == ESelectionKind.NONE ) {
    // setAcS
    // }
    actionsStateEventer().fireChangeEvent();
  }

  void deleteSelectedVisels() {
    IStringList ids = new StringArrayList( selectionManager.selectedViselIds() );
    selectionManager.deselectAll();
    for( String id : ids ) {
      vedScreen.model().visels().remove( id );
    }
  }

}

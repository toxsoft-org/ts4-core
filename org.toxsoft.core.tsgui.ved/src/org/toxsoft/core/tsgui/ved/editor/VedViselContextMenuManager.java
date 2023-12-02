package org.toxsoft.core.tsgui.ved.editor;

import static org.toxsoft.core.tsgui.ved.editor.ITsResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.asp.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.ved.editor.IVedViselSelectionManager.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.asp.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Обработчик пользовательского ввода обеспечивающий работу контекстного меню.<br>
 *
 * @author vs
 */
public class VedViselContextMenuManager
    extends VedAbstractUserInputHandler {

  private final IVedViselSelectionManager selectionManager;

  private final VedAspViselsAlignment aspAlignment;

  private final VedAspCommonContextMenu aspCommon;

  private final VedAspCopyPaste aspCopyPaste;

  private final MenuCreatorFromAsp commonMenuCreator;

  private final MenuCreatorFromAsp alignmentMenuCreator;

  private final MenuCreatorFromAsp cpMenuCreator;

  /**
   * Constructor.
   *
   * @param aScreen {@link IVedScreen} - the owner VED screen
   * @param aSelectionManager {@link IVedViselSelectionManager} - selection manager for VISELs
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedViselContextMenuManager( IVedScreen aScreen, IVedViselSelectionManager aSelectionManager ) {
    super( aScreen );
    TsNullArgumentRtException.checkNull( aSelectionManager );
    selectionManager = aSelectionManager;
    aspAlignment = new VedAspViselsAlignment( aScreen, selectionManager );
    aspCommon = new VedAspCommonContextMenu( aScreen, selectionManager );
    aspCopyPaste = new VedAspCopyPaste( aScreen, selectionManager );
    commonMenuCreator = new MenuCreatorFromAsp( aspCommon, aScreen.tsContext() );
    alignmentMenuCreator = new MenuCreatorFromAsp( aspAlignment, aScreen.tsContext() );
    cpMenuCreator = new MenuCreatorFromAsp( aspCopyPaste, aScreen.tsContext() );
  }

  // ------------------------------------------------------------------------------------
  // ITsMouseInputListener
  //

  @SuppressWarnings( "unused" )
  @Override
  public boolean onMouseDown( Object aSource, ETsMouseButton aButton, int aState, ITsPoint aCoors, Control aWidget ) {
    vedScreen().view().getControl().setMenu( null );
    aspCopyPaste.setMouseCoords( aCoors );
    if( aButton == ETsMouseButton.RIGHT && (aState & SWT.MODIFIER_MASK) == 0 ) { // pure right click
      IStringList viselIds = vedScreen().view().listViselIdsAtPoint( aCoors );
      VedAbstractVisel visel = null;
      if( viselIds.size() > 0 ) {
        visel = vedScreen().model().visels().list().getByKey( viselIds.first() );
      }
      aspCommon.setActiveVisel( visel );
      if( visel != null ) { // click was on the visel
        aspCopyPaste.setActiveVisel( visel );
        if( selectionManager.selectionKind() == ESelectionKind.MULTI ) { // multiselection is present
          aspAlignment.setAnchorVisel( visel );

          Menu cmnMenu = new Menu( vedScreen().view().getControl() );
          cpMenuCreator.fillMenu( cmnMenu );

          MenuItem alignItem = new MenuItem( cmnMenu, SWT.CASCADE );
          alignItem.setText( STR_M_ALIGNMENT );
          Menu ctxMenu = alignmentMenuCreator.getMenu( cmnMenu );
          alignItem.setMenu( ctxMenu );

          new MenuItem( cmnMenu, SWT.SEPARATOR );
          commonMenuCreator.fillMenu( cmnMenu );

          vedScreen().view().getControl().setMenu( cmnMenu );
          cmnMenu.setVisible( true );
          return true;
        }
      }
      Menu cmnMenu = new Menu( vedScreen().view().getControl() );
      cpMenuCreator.fillMenu( cmnMenu );
      new MenuItem( cmnMenu, SWT.SEPARATOR );
      commonMenuCreator.fillMenu( cmnMenu );

      vedScreen().view().getControl().setMenu( cmnMenu );
      cmnMenu.setVisible( true );
      return true;
    }
    return false;
  }

  // ------------------------------------------------------------------------------------
  // ITsKeyInputListener
  //

  @Override
  public boolean onKeyDown( Object aSource, int aCode, char aChar, int aState ) {
    if( aCode == SWT.DEL ) {
      IStringList selList = new StringArrayList( selectionManager.selectedViselIds() );
      if( selList.size() > 0 ) {
        for( String id : selList ) {
          vedScreen().model().visels().remove( id );
          for( String actId : VedScreenUtils.viselActorIds( id, vedScreen() ) ) {
            vedScreen().model().actors().remove( actId );
          }
        }
        return true;
      }
    }
    if( (aState & SWT.MODIFIER_MASK) == SWT.CTRL ) {
      if( aCode == 99 ) { // key code for symbol "C"
        if( aspCopyPaste.isActionEnabled( VedAspCopyPaste.ACTID_COPY ) ) {
          aspCopyPaste.doHandleAction( VedAspCopyPaste.ACTID_COPY );
          aspCopyPaste.setMouseCoords( null );
        }
      }
      if( aCode == 118 ) { // key code for symbol "V"
        if( aspCopyPaste.isActionEnabled( VedAspCopyPaste.ACTID_PASTE ) ) {
          aspCopyPaste.doHandleAction( VedAspCopyPaste.ACTID_PASTE );
        }
      }
    }
    return false;
  }
}

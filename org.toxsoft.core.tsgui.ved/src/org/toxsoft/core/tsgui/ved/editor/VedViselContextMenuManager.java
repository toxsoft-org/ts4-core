package org.toxsoft.core.tsgui.ved.editor;

import static org.toxsoft.core.tsgui.ved.editor.ITsResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.asp.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.ved.editor.IVedViselSelectionManager.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.asp.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Обработчик пользовательского ввода обеспечивающий работу контекстного меню.<br>
 *
 * @author vs
 */
public class VedViselContextMenuManager
    extends VedAbstractUserInputHandler {

  static class ViselContextMenuCreator
      implements IVedContextMenuCreator {

    private final ITsGuiContext tsContext;

    ViselContextMenuCreator( ITsGuiContext aTsContext ) {
      tsContext = aTsContext;
    }

    // ------------------------------------------------------------------------------------
    // IVedContextMenuCreator
    //

    @Override
    public boolean fillMenu( Menu aMenu, VedAbstractVisel aClickedVisel, ITsPoint aSwtCoors ) {
      if( aClickedVisel != null ) {
        MenuCreatorFromAsp menuCreator = new MenuCreatorFromAsp( aClickedVisel.actionsProvider(), tsContext );
        menuCreator.fillMenu( aMenu );
        return true;
      }
      return false;
    }

  }

  private final IVedViselSelectionManager selectionManager;

  private final VedAspViselsAlignment aspAlignment;

  private final VedAspCommonContextMenu aspCommon;

  // private final VedAspCopyPaste aspCopyPaste;

  private final MenuCreatorFromAsp commonMenuCreator;

  private final MenuCreatorFromAsp alignmentMenuCreator;

  // private final MenuCreatorFromAsp cpMenuCreator;

  private final IListEdit<IVedContextMenuCreator> customMenuCreators = new ElemArrayList<>();

  private ViselContextMenuCreator viselMenuCreator;

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
    // aspCopyPaste = new VedAspCopyPaste( aScreen, selectionManager );
    commonMenuCreator = new MenuCreatorFromAsp( aspCommon, aScreen.tsContext() );
    alignmentMenuCreator = new MenuCreatorFromAsp( aspAlignment, aScreen.tsContext() );
    // cpMenuCreator = new MenuCreatorFromAsp( aspCopyPaste, aScreen.tsContext() );
    viselMenuCreator = new ViselContextMenuCreator( aScreen.tsContext() );
  }

  // ------------------------------------------------------------------------------------
  // ITsMouseInputListener
  //

  @SuppressWarnings( "unused" )
  @Override
  public boolean onMouseDown( Object aSource, ETsMouseButton aButton, int aState, ITsPoint aCoors, Control aWidget ) {
    vedScreen().view().getControl().setMenu( null );
    // aspCopyPaste.setMouseCoords( aCoors );
    if( aButton == ETsMouseButton.RIGHT && (aState & SWT.MODIFIER_MASK) == 0 ) { // pure right click
      IStringList viselIds = vedScreen().view().listViselIdsAtPoint( aCoors );
      VedAbstractVisel visel = null;
      if( viselIds.size() > 0 ) {
        visel = vedScreen().model().visels().list().getByKey( viselIds.first() );
      }
      aspCommon.setActiveVisel( visel );

      Menu cmnMenu = new Menu( vedScreen().view().getControl() );
      if( visel != null ) { // click was on the visel
        // aspCopyPaste.setActiveVisel( visel );
        if( selectionManager.selectionKind() == ESelectionKind.MULTI ) { // multiselection is present
          aspAlignment.setAnchorVisel( visel );

          // Menu cmnMenu = new Menu( vedScreen().view().getControl() );
          // cpMenuCreator.fillMenu( cmnMenu );

          MenuItem alignItem = new MenuItem( cmnMenu, SWT.CASCADE );
          alignItem.setText( STR_M_ALIGNMENT );
          Menu ctxMenu = alignmentMenuCreator.getMenu( cmnMenu );
          alignItem.setMenu( ctxMenu );

          // new MenuItem( cmnMenu, SWT.SEPARATOR );
          // commonMenuCreator.fillMenu( cmnMenu );

          vedScreen().view().getControl().setMenu( cmnMenu );
          // cmnMenu.setVisible( true );
          // return true;
        }
        viselMenuCreator.fillMenu( cmnMenu, visel, aCoors );
        for( IVedContextMenuCreator creator : customMenuCreators ) {
          creator.fillMenu( cmnMenu, visel, aCoors );
        }
      }
      else {
        // cpMenuCreator.fillMenu( cmnMenu );
        viselMenuCreator.fillMenu( cmnMenu, visel, aCoors );
        for( IVedContextMenuCreator creator : customMenuCreators ) {
          creator.fillMenu( cmnMenu, visel, aCoors );
        }
      }

      // viselMenuCreator.fillMenu( cmnMenu, visel, aCoors );
      // for( IVedContextMenuCreator creator : customMenuCreators ) {
      // creator.fillMenu( cmnMenu, visel, aCoors );
      // }

      // Menu cmnMenu = new Menu( vedScreen().view().getControl() );
      // cpMenuCreator.fillMenu( cmnMenu );
      new MenuItem( cmnMenu, SWT.SEPARATOR );
      commonMenuCreator.fillMenu( cmnMenu );

      vedScreen().view().getControl().setMenu( cmnMenu );
      cmnMenu.setVisible( true );
      return true;
    }
    return false;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Добавляет пользовательский "создатель" меню.<br>
   * Ели такой создатель уже существует, то ничего не делает.
   *
   * @param aMenuCreator {@link IVedContextMenuCreator} - пользовательский "создатель" меню
   */
  public void addCustomMenuCreator( IVedContextMenuCreator aMenuCreator ) {
    if( !customMenuCreators.hasElem( aMenuCreator ) ) {
      customMenuCreators.add( aMenuCreator );
    }
  }

  /**
   * Удаляет пользовательский "создатель" меню.<br>
   * Если такого создателя нет, то ничего не делает.
   *
   * @param aMenuCreator {@link IVedContextMenuCreator} - пользовательский "создатель" меню
   */
  public void removeCustomMenuCreator( IVedContextMenuCreator aMenuCreator ) {
    customMenuCreators.remove( aMenuCreator );
  }

}

package org.toxsoft.core.tsgui.ved.editor;

import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.ved.editor.palette.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Обработчик пользовательского ввода для создания акторов и визелей в зависимости от "запавшей" кнопки на палитре.<br>
 *
 * @author vs
 */
public class PaletteSelectionManager
    extends VedAbstractUserInputHandler {

  private final VedItemsPaletteBar palette;

  /**
   * Конструктор.
   *
   * @param aScreen {@link IVedScreen} - экран редактора
   * @param aPalette {@link VedItemsPaletteBar} - палитра компонент
   */
  public PaletteSelectionManager( IVedScreen aScreen, VedItemsPaletteBar aPalette ) {
    super( aScreen );
    palette = aPalette;
    palette.addTsSelectionListener( ( aSource, aSelectedItem ) -> {
      if( palette.selectedItem() == null ) {
        vedScreen().view().setCursor( null );
      }
    } );
  }

  // ------------------------------------------------------------------------------------
  // ITsMouseInputListener
  //

  @Override
  public boolean onMouseMove( Object aSource, int aState, ITsPoint aCoors, Control aWidget ) {
    if( palette.selectedItem() == null ) {
      return false;
    }
    Cursor cursor = palette.cursor();
    vedScreen().view().setCursor( cursor );
    return cursor != null;
  }

  @Override
  public boolean onMouseDown( Object aSource, ETsMouseButton aButton, int aState, ITsPoint aCoors, Control aWidget ) {
    if( aButton == ETsMouseButton.LEFT && (aState & SWT.MODIFIER_MASK) == 0 ) {
      IVedItemsPaletteEntry pe = palette.selectedItem();
      if( pe != null ) {
        createVedItemAt( pe.itemCfg(), aCoors );
      }
    }
    palette.deselectAllButtons();
    vedScreen().view().setCursor( null );
    return false;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void createVedItemAt( IVedItemCfg aCfg, ITsPoint aCursorCoors ) {
    EVedItemKind kind = aCfg.kind();
    switch( kind ) {
      case VISEL: {
        VedItemCfg viselCfg = vedScreen().model().visels().prepareFromTemplate( aCfg );
        viselCfg.propValues().setDouble( PROP_X, aCursorCoors.x() );
        viselCfg.propValues().setDouble( PROP_Y, aCursorCoors.y() );
        vedScreen().model().visels().create( viselCfg );
        vedScreen().view().redraw();
        break;
      }
      case ACTOR: {
        VedItemCfg actorCfg = vedScreen().model().actors().prepareFromTemplate( aCfg );
        vedScreen().model().actors().create( actorCfg );
        break;
      }
      default:
        throw new TsNotAllEnumsUsedRtException( kind.nmName() );
    }
  }

}

package org.toxsoft.core.tsgui.ved.editor.palette;

import org.eclipse.swt.*;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;

/**
 * Простая (без категорий) реализация палитры ved компонент в виде bar'a (выглядит аналогично {@link ToolBar}.
 * <p>
 * Данная палитра поддерживает только {@link DragSource} и каждый элемент является кнопкой.
 *
 * @author vs
 */
public class VedItemsSimplePaletteBar
    extends VedAbstractItemsPalette {

  private final TsPanel paletteComp;

  private final VedScreen vScreen;

  /**
   * Конструктор.
   *
   * @param aParent {@link Composite} - родительская панель
   * @param aSwtStyle int - стиль
   * @param aScreen {@link VedScreen} - экран
   */
  public VedItemsSimplePaletteBar( Composite aParent, int aSwtStyle, VedScreen aScreen ) {
    vScreen = aScreen;
    paletteComp = new TsPanel( aParent, aScreen.tsContext(), aSwtStyle );

    if( (aSwtStyle & SWT.HORIZONTAL) != 0 ) {
      paletteComp.setLayout( new RowLayout( SWT.HORIZONTAL ) );
    }
    else {
      paletteComp.setLayout( new RowLayout( SWT.VERTICAL ) );
    }

  }

  // ------------------------------------------------------------------------------------
  // IVedItemsPalette
  //

  @Override
  public Control getControl() {
    return paletteComp;
  }

  @Override
  protected void doOnEntryAdded( IVedItemsPaletteEntry aEntry, String aCategoryId ) {
    Button btn = new Button( paletteComp, SWT.FLAT );

    ITsIconManager im = vScreen.tsContext().get( ITsIconManager.class );
    Image image = im.loadStdIcon( aEntry.iconId(), EIconSize.IS_32X32 );

    btn.setImage( image );
    btn.setText( aEntry.nmName() );
    btn.setToolTipText( aEntry.description() );

    DragSource source = new DragSource( btn, DND.DROP_MOVE | DND.DROP_COPY );
    source.setData( aEntry );
    Transfer[] types = { TextTransfer.getInstance() };
    source.setTransfer( types );

    source.addDragListener( new DragSourceAdapter() {

      @Override
      public void dragSetData( DragSourceEvent aEvent ) {
        IVedItemCfg itemCfg = ((IVedItemsPaletteEntry)source).itemCfg();
        aEvent.data = VedItemCfg.KEEPER.ent2str( itemCfg );
      }
    } );

  }

}

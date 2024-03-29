package org.toxsoft.core.tsgui.ved.editor.palette;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;

import org.eclipse.swt.*;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.ved.screen.*;
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

  private final TsPanel   paletteComp;
  private final EIconSize iconSize;

  /**
   * Конструктор.
   *
   * @param aParent {@link Composite} - родительская панель
   * @param aSwtStyle int - стиль
   * @param aScreen {@link VedScreen} - экран
   * @param aVertical boolean - признак расположения патитры (вертикально/горизонтально)
   */
  public VedItemsSimplePaletteBar( Composite aParent, int aSwtStyle, IVedScreen aScreen, boolean aVertical ) {
    this( aParent, aSwtStyle, aScreen, aVertical, null );
  }

  /**
   * Конструктор.
   *
   * @param aParent {@link Composite} - родительская панель
   * @param aSwtStyle int - стиль
   * @param aScreen {@link VedScreen} - экран
   * @param aVertical boolean - признак расположения патитры (вертикально/горизонтально)
   * @param aIconSize {@link EIconSize} - размер значка
   */
  public VedItemsSimplePaletteBar( Composite aParent, int aSwtStyle, IVedScreen aScreen, boolean aVertical,
      EIconSize aIconSize ) {
    super( aScreen );
    if( aIconSize == null ) {
      iconSize = hdpiService().getIconsSize( VED_EDITOR_PALETTE_ICON_SIZE_CATEGORY );
    }
    else {
      iconSize = aIconSize;
    }
    paletteComp = new TsPanel( aParent, aScreen.tsContext(), aSwtStyle );
    if( !aVertical ) {
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

    Image image = iconManager().loadStdIcon( aEntry.iconId(), iconSize );

    btn.setImage( image );
    btn.setToolTipText( aEntry.nmName() + '\n' + aEntry.description() );

    DragSource source = new DragSource( btn, DND.DROP_MOVE | DND.DROP_COPY );
    source.setData( aEntry );
    Transfer[] types = { TextTransfer.getInstance() };
    source.setTransfer( types );

    source.addDragListener( new DragSourceAdapter() {

      @Override
      public void dragSetData( DragSourceEvent aEvent ) {
        IVedItemCfg itemCfg = ((IVedItemsPaletteEntry)source.getData()).itemCfg();
        aEvent.data = VedItemCfg.KEEPER.ent2str( itemCfg );
      }
    } );

  }

}

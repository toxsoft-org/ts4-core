package org.toxsoft.core.tsgui.ved.editor.palette;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Абстрактный класс, от которого должны наследоваться все реализации палитр.
 * <p>
 * Реализует функциональность по работе со списками вхождений в палитру и категорий.
 *
 * @author vs
 */
public abstract class VedAbstractItemsPalette
    implements IVedItemsPalette, ITsGuiContextable {

  private final IStridablesListEdit<IVedItemsPaletteEntry> entries = new StridablesList<>();

  private final IStridablesList<IVedItemsPaletteCategory> categories = new StridablesList<>();

  private final IVedScreen vedScreen;

  /**
   * Constructor.
   *
   * @param aVedScreen {@link IVedScreen} - the VED screen palette is bound to
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedAbstractItemsPalette( IVedScreen aVedScreen ) {
    TsNullArgumentRtException.checkNull( aVedScreen );
    vedScreen = aVedScreen;
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return vedScreen.tsContext();
  }

  // ------------------------------------------------------------------------------------
  // IVedItemsPalette
  //

  @Override
  public final IStridablesList<IVedItemsPaletteEntry> listEntries() {
    if( listCategories().isEmpty() ) {
      return entries;
    }
    IStridablesListEdit<IVedItemsPaletteEntry> result = new StridablesList<>( entries );
    for( IVedItemsPaletteCategory cat : listCategories() ) {
      result.addAll( cat.listEntries() );
    }
    return result;
  }

  @Override
  public final IStridablesList<IVedItemsPaletteCategory> listCategories() {
    return categories;
  }

  @Override
  public IVedItemsPaletteCategory defineCategory( IStridableParameterized aCategory ) {
    // TODO Auto-generated method stub
    throw new TsUnderDevelopmentRtException();
  }

  @Override
  public final void addEntry( IVedItemsPaletteEntry aEntry, String aCategoryId ) {
    if( aCategoryId != null ) {
      throw new TsUnderDevelopmentRtException();
    }
    TsItemAlreadyExistsRtException.checkTrue( entries.hasElem( aEntry ) );
    entries.add( aEntry );
    doOnEntryAdded( aEntry, aCategoryId );
  }

  // ------------------------------------------------------------------------------------
  // API for subclasses
  //

  @SuppressWarnings( "javadoc" )
  public IVedScreen vedScreen() {
    return vedScreen;
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  @Override
  public abstract Control getControl();

  protected abstract void doOnEntryAdded( IVedItemsPaletteEntry aEntry, String aCategoryId );
}

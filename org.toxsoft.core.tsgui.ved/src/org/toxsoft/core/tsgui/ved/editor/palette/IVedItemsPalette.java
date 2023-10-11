package org.toxsoft.core.tsgui.ved.editor.palette;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The GUI component containing VED palette entries for VED items creation.
 * <p>
 * Palette may contain categories (the groups of entries). Entries may be added to categories or directly to the
 * palette. If no category is defined, entries will be added directly to the palette. The behavior and UI of the entries
 * added to directly to the palette (when categories exists) depends on the palette implementation and is undefined.
 *
 * @author hazard157
 */
public interface IVedItemsPalette {

  /**
   * Returns all entries in all categories.
   *
   * @return {@link IStridablesList}&lt;{@link IVedItemsPaletteEntry}&gt; - the entries list
   */
  IStridablesList<IVedItemsPaletteEntry> listEntries();

  /**
   * Returns all categories in this palette.
   * <p>
   * If no category was created by the user, the list is empty.
   *
   * @return {@link IStridablesList}&lt;{@link IVedItemsPaletteCategory}&gt; - the categories list
   */
  IStridablesList<IVedItemsPaletteCategory> listCategories();

  /**
   * Defines the category.
   * <p>
   * As usual< options {@link IAvMetaConstants#TSID_NAME}, {@link IAvMetaConstants#TSID_DESCRIPTION} and
   * {@link IAvMetaConstants#TSID_ICON_ID} are used for visual representation of the category.
   *
   * @param aCategory {@link IStridableParameterized} - category parameters
   * @return {@link IVedItemsPaletteCategory} - created category
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ctagory with the specified ID already exists
   */
  IVedItemsPaletteCategory defineCategory( IStridableParameterized aCategory );

  /**
   * Adds entry to the specified category or directly to the palette.
   *
   * @param aEntry {@link IVedItemsPaletteEntry} - the entry to add
   * @param aCategoryId String - category ID or <code>null</code> to add directly to the palette
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException entry with the same ID already in palette
   * @throws TsItemNotFoundRtException no such category exists
   */
  void addEntry( IVedItemsPaletteEntry aEntry, String aCategoryId );

  /**
   * Returns the SWT control implementing this component.
   *
   * @return {@link Control} - the SWT control
   */
  Control getControl();

  // ------------------------------------------------------------------------------------
  // Inline methods for convenience

  @SuppressWarnings( "javadoc" )
  default void addEntry( IVedItemsPaletteEntry aEntry ) {
    addEntry( aEntry, null );
  }

}

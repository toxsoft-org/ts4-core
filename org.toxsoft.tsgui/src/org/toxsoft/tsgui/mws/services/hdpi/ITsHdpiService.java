package org.toxsoft.tsgui.mws.services.hdpi;

import static org.toxsoft.tsgui.mws.services.hdpi.ITsHdpiServiceConstants.*;

import org.toxsoft.tsgui.graphics.icons.EIconSize;
import org.toxsoft.tslib.bricks.events.ITsEventer;
import org.toxsoft.tslib.utils.errors.*;

/**
 * Screen DPI dependent GUI settings service.
 * <p>
 * TODO usage and concepts
 *
 * @author hazard157
 */
public interface ITsHdpiService {

  /**
   * Returns the default icons size.
   * <p>
   * Other icon sizes may be spcified as scale of the default size.
   *
   * @return {@link EIconSize} - the default icon size
   */
  EIconSize getDefaultIconSize();

  /**
   * Returns icons size for specified category of application GUI.
   *
   * @param aCategoryId String - the icons category ID
   * @return {@link EIconSize} - the size of category icons
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException unknown category
   */
  EIconSize getIconsSize( String aCategoryId );

  /**
   * Creates new category of icons.
   * <p>
   * Icon scale factor determines icon size relative to the {@link #getDefaultIconSize()}. It is a number of steps to
   * increase/decrase icon size in the {@link EIconSize} constants sequence. Value of 0 means the default size, +1 means
   * {@link EIconSize#nextSize()}, -1 - {@link EIconSize#prevSize()}, +2 - two times {@link EIconSize#nextSize()} an so
   * on.
   *
   * @param aCategoryId String - category ID (must be an IDpath)
   * @param aIconScale int - icon scale factor
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException category with this ID already exists
   * @throws TsIllegalArgumentRtException argument is not an IDpath
   */
  void defineIconCategory( String aCategoryId, int aIconScale );

  /**
   * Returns the service eventer.
   *
   * @return {@link ITsEventer}&lt;{@link ITsHdpiServiceListener}&gt; - the service eventer
   */
  ITsEventer<ITsHdpiServiceListener> eventer();

  // ------------------------------------------------------------------------------------
  // Inline methods for convinience
  //

  @SuppressWarnings( "javadoc" )
  default EIconSize getMenuIconsSize() {
    return getIconsSize( ICON_CATEG_ID_MENU );
  }

  @SuppressWarnings( "javadoc" )
  default EIconSize getToolbarIconsSize() {
    return getIconsSize( ICON_CATEG_ID_TOOLBAR );
  }

  @SuppressWarnings( "javadoc" )
  default EIconSize getJFaceCellIconsSize() {
    return getIconsSize( ICON_CATEG_ID_JFACE_CELL );
  }

  @SuppressWarnings( "javadoc" )
  default EIconSize getUipartTabIconsSize() {
    return getIconsSize( ICON_CATEG_ID_UIPART_TAB );
  }

}

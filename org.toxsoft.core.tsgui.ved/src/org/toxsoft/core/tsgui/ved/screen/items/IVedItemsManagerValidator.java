package org.toxsoft.core.tsgui.ved.screen.items;

import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Validates {@link IVedItemsManager} CRUD actions.
 *
 * @author hazard157
 */
public interface IVedItemsManagerValidator {

  /**
   * Checks if the item can be created.
   * <p>
   * Check for:
   * <ul>
   * <li>index validity;</li>
   * <li>same ID already exists;</li>
   * <li>factory ID is not registered.</li>
   * </ul>
   *
   * @param aIndex int - index of inserted item
   * @param aCfg {@link IVedItemCfg} - the configuration the item to create
   * @return {@link ValidationResult} - the check result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  ValidationResult canCreate( int aIndex, IVedItemCfg aCfg );

  /**
   * Checks if item can be removed.
   *
   * @param aId String - the ID of the item to remove
   * @return {@link ValidationResult} - the check result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  ValidationResult canRemove( String aId );

}

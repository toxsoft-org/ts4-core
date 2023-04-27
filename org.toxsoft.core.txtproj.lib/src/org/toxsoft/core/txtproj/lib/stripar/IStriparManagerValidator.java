package org.toxsoft.core.txtproj.lib.stripar;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * STRIPAR management validator.
 *
 * @author hazard157
 */
public interface IStriparManagerValidator {

  /**
   * Checks if the new instance of the entity may be created.
   *
   * @param aId String - the entity ID (an IDpath)
   * @param aParams {@link IOptionSet} - entity parameters initial values
   * @return {@link ValidationResult} - the check result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  ValidationResult canCreateItem( String aId, IOptionSet aParams );

  /**
   * Checks if the existing entity may be edited.
   *
   * @param aOldId String - the existing entity ID
   * @param aId String - new ID of the edited entity (may be the same as old ID)
   * @param aParams {@link IOptionSet} - new values of the parameters subset
   * @return {@link ValidationResult} - the check result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  ValidationResult canEditItem( String aOldId, String aId, IOptionSet aParams );

  /**
   * Checks if item may be removed.
   * <p>
   * Absence of the STRIPAR with the specified ID is a warning, not an error.
   *
   * @param aId String - the ID of the removed entity
   * @return {@link ValidationResult} - the check result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  ValidationResult canRemoveItem( String aId );

}

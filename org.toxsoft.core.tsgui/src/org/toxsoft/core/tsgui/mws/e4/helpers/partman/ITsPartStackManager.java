package org.toxsoft.core.tsgui.mws.e4.helpers.partman;

import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.eclipse.e4.ui.workbench.modeling.*;
import org.eclipse.e4.ui.workbench.modeling.EPartService.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Simplifies {@link MPartStack} usage for dynamic parts management.
 * <p>
 * <b>Warning</b>: the managed part stack must be marked with tag <code>NoAutoCollapse</code>, otherwise part stack will
 * disappear when last part closes.
 *
 * @author hazard157
 */
public interface ITsPartStackManager {

  /**
   * Returns managed part stack.
   *
   * @return {@link MPartStack} - managed part stack, never is <code>null</code>
   */
  MPartStack getPartStack();

  /**
   * Returns existing managed part (parts created by this manager).
   * <p>
   * The keys in returned map are {@link MPart#getElementId()} defined by {@link UIpartInfo#partId()}.
   *
   * @return {@link IStringMap}&lt;{@link MPart}&gt; - map "part ID" - "the part"
   */
  IStringMap<MPart> listManagedParts();

  /**
   * Creates new managed part in {@link #getPartStack()}.
   * <p>
   * Created part becomes the the visible, as described for {@link PartState#VISIBLE}.
   * <p>
   * Part is created with {@link EPartService#REMOVE_ON_HIDE_TAG} tag so when close part will be disposed.
   *
   * @param aInfo {@link UIpartInfo} - information about part to be created
   * @return {@link MPart} - created part
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException part with same ID already exists
   */
  MPart createPart( UIpartInfo aInfo );

  /**
   * Closes and destroys the specified part.
   * <p>
   * The part ID is {@link MPart#getElementId()} also used as the keys in the map {@link #listManagedParts()}.
   *
   * @param aPartId String - the ID of the part to be closed and destroyed
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no managed part found with the specified ID
   */
  void closePart( String aPartId );

  /**
   * Closes all managed parts.
   */
  void closeAll();

  // ------------------------------------------------------------------------------------
  // Inline methods for convenience
  //

  /**
   * Finds part by ID.
   * <p>
   * Warning: method is searching only managed part from {@link #listManagedParts()}.
   *
   * @param aPartId String - the part ID
   * @return {@link MPart} - found part or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  default MPart findPart( String aPartId ) {
    return listManagedParts().findByKey( aPartId );
  }

}

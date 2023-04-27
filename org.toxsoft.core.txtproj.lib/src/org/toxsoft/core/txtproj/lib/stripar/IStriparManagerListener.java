package org.toxsoft.core.txtproj.lib.stripar;

import org.toxsoft.core.tslib.coll.helpers.*;

/**
 * {@link IStriparManager} event listener.
 *
 * @author hazard157
 */
public interface IStriparManagerListener {

  /**
   * Called when any change in the list {@link IStriparManager#items()} happens.
   *
   * @param aSource {@link IStriparManagerApi} - the event source
   * @param aOp {@link ECrudOp} - change type
   * @param aId String - changed item ID or <code>null</code> for batch change
   */
  void onChanged( IStriparManagerApi<?> aSource, ECrudOp aOp, String aId );

}

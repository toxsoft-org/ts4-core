package org.toxsoft.core.txtproj.lib.stripar;

import org.toxsoft.core.tslib.av.opset.IOptionSet;
import org.toxsoft.core.tslib.av.utils.IParameterized;
import org.toxsoft.core.tslib.bricks.strid.IStridable;

/**
 * Concrete STRIPAR entities creator.
 * <p>
 * Used by the {@link StriparManager} to create entities.
 *
 * @author hazard157
 * @param <E> - STRIPAR type
 */
public interface IStriparCreator<E extends IStridable & IParameterized> {

  /**
   * Creates STRIPAR entity.
   *
   * @param aId String - the entity ID (IDpath)
   * @param aParams {@link IOptionSet} - initial values for {@link IParameterized#params() STRIPAR.params()}
   * @return &lt;E&gt; - created entity
   */
  E create( String aId, IOptionSet aParams );

}

package org.toxsoft.core.txtproj.lib.stripar;

import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.txtproj.lib.*;

/**
 * Project data unit to manages STRIPAR entities.
 * <P>
 * STRIPAR is any STRIdable and PARamaterized entity implementation both {@link IStridable} and {@link IParameterized}
 * interfaces. Obviously any {@link IStridableParameterized} entities also are the STRIPARs.
 *
 * @author hazard157
 * @param <E> - stridable parameterized entity type
 */
public interface IStriparManager<E extends IStridable & IParameterized>
    extends IStriparManagerApi<E>, IProjDataUnit {

  // nop

}

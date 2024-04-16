package org.toxsoft.core.tslib.math.combicond;

import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.core.tslib.math.combicond.impl.*;

/**
 * The registry of the available {@link ISingleCondType}.
 * <p>
 * There is two approaches how to use this registry:
 * <ul>
 * <li>there is a singleton instance {@link DefaultSingleCondTypesRegistry#INSTANCE} of the registry capable to hold any
 * subclasses of {@link ISingleCondType}. User must ensure {@link ISingleCondType#id()} identifiers are unique and
 * convert registry entries to the desired types;</li>
 * <li>create and use own implementation of this interface with needed generic type. Such method requires to pass the
 * instance everywhere, where the SCT registry is needed.</li>
 * </ul>
 *
 * @author hazard157
 * @param <T> - Java types of the condition type definitions
 */
public interface ISingleCondTypesRegistry<T extends ISingleCondType>
    extends IStridablesRegisrty<T> {

  // nop

}

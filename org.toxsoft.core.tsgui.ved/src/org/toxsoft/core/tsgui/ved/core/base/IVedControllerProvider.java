package org.toxsoft.core.tsgui.ved.core.base;

import org.toxsoft.core.tsgui.ved.core.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;

/**
 * Controller factory and definitions provider.
 *
 * @author hazard157
 */
public interface IVedControllerProvider
    extends IVedEntityProviderBase {

  /**
   * Returns possible link definitions of controllers created by this provider.
   * <p>
   * Some links are mandatory for controller to work. Again, for <code>Gauge</code> example "splitBar" link to the
   * <code>SplitBar</code> component is mandatory while "textValue" link to the <code>TextLabel</code> component
   * (displaying the value) is optional. Mandatory links has the {@link IAvMetaConstants#TSID_IS_MANDATORY} option set
   * to <code>true</code> in {@link IVedCompBindDef#params()}.
   * <p>
   * This is a "backend" method intended to be used by some GUI designeds.
   *
   * @return {@link IStridablesList}&lt;{@link IVedCompBindDef}&gt; - links definitions
   */
  IStridablesList<IVedCompBindDef> bindDefs();

  @Override
  @SuppressWarnings( "unchecked" )
  IVedController create( String aId, IVedEnvironment aVedEnv );

}

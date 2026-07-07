package org.toxsoft.core.tsgui.ved.comps.render;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;

/**
 * The visel renderers factories registry.
 * <p>
 * The reference to the registry implementation must be in the {@link ITsGuiContext} context.
 *
 * @author vs
 */
public class VedRendererFactoriesRegistry
    extends StridablesRegisrty<IViselRendererFactory> {

  /**
   * Constructor.
   */
  public VedRendererFactoriesRegistry() {
    super( IViselRendererFactory.class );
  }

  /**
   * Возвращает список фабрик отрисовщиков указанного типа.
   *
   * @param aKindId String - тип отрисовщика
   * @return IStridablesList&lt;IViselRendererFactory> - список фабрик отрисовщиков указанного типа
   */
  public IStridablesList<IViselRendererFactory> listFactories( String aKindId ) {
    IStridablesListEdit<IViselRendererFactory> result = new StridablesList<>();
    for( IViselRendererFactory f : items() ) {
      if( f.kindId().equals( aKindId ) ) {
        result.add( f );
      }
    }
    return result;
  }
}

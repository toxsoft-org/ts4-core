package org.toxsoft.core.tsgui.ved.m5;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;

/**
 * LM for {@link VedItemM5Model}.
 * <p>
 * Just list items, both VISELs and actors.
 *
 * @author hazard157
 */
class VedItemM5LifecycleManager
    extends M5LifecycleManager<IVedItem, IVedScreen> {

  public VedItemM5LifecycleManager( IM5Model<IVedItem> aModel, IVedScreen aMaster ) {
    super( aModel, false, false, false, true, aMaster );
  }

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  @Override
  protected IList<IVedItem> doListEntities() {
    IStridablesList<VedAbstractVisel> llVisels = master().model().visels().list();
    IStridablesList<VedAbstractActor> llActors = master().model().actors().list();
    IListEdit<IVedItem> ll = new ElemArrayList<>( llVisels.size() + llActors.size() );
    ll.addAll( (IList)llVisels );
    ll.addAll( (IList)llActors );
    return ll;
  }

}

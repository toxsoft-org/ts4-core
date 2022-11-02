package org.toxsoft.core.tsgui.ved.zver1.core.impl;

import java.util.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.ved.zver1.core.view.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedScreenManager} implementation.
 *
 * @author hazard157
 */
public class VedScreenManager
    implements IVedScreenManager {

  private final GenericChangeEventer activeScreenChangeEventer;
  private final IListEdit<VedScreen> screensList = new ElemArrayList<>();
  private final VedEnvironment       vedEnv;

  private VedScreen activeScreen = null;

  /**
   * Constructor.
   *
   * @param aVedEnv {@link VedEnvironment} - owner VED environment
   */
  public VedScreenManager( VedEnvironment aVedEnv ) {
    vedEnv = TsNullArgumentRtException.checkNull( aVedEnv );
    activeScreenChangeEventer = new GenericChangeEventer( this );
  }

  // ------------------------------------------------------------------------------------
  // Package API
  //

  /**
   * Called by screen when it is closing from {@link VedScreen#close()}.
   *
   * @param aScreen {@link VedScreen} - the closing screen
   */
  void papiWhenScreenClosed( VedScreen aScreen ) {
    TsInternalErrorRtException.checkFalse( screensList.hasElem( aScreen ) );
    screensList.remove( aScreen );
  }

  // ------------------------------------------------------------------------------------
  // IVedScreenManager
  //

  @Override
  public IVedScreen createScreen( Canvas aCanvas ) {
    TsNullArgumentRtException.checkNull( aCanvas );
    VedScreen vs = new VedScreen( aCanvas, vedEnv );
    screensList.add( vs );
    // TODO maybe more initialization?
    if( screensList.size() == 1 ) {
      activateScreen( vs );
    }
    return vs;
  }

  @Override
  public IVedScreen activeScreen() {
    return activeScreen;
  }

  @Override
  public void activateScreen( IVedScreen aScreen ) {
    if( Objects.equals( activeScreen, aScreen ) ) {
      return;
    }
    if( activeScreen != null ) {
      // TODO inform screen about de-activation
    }
    if( aScreen instanceof VedScreen vedScreen ) {
      activeScreen = vedScreen;

      // TODO inform screen about activation

      activeScreenChangeEventer.fireChangeEvent();
    }
    else {
      throw new TsIllegalArgumentRtException();
    }
  }

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  @Override
  public IList<IVedScreen> listScreens() {
    return (IList)screensList;
  }

  @Override
  public IGenericChangeEventer activeScreenChangeEventer() {
    return activeScreenChangeEventer;
  }

}

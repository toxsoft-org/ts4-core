package org.toxsoft.core.tsgui.mws.services.e4helper;

import static org.toxsoft.core.tsgui.mws.services.e4helper.ITsResources.*;

import java.util.*;

import org.eclipse.core.commands.*;
import org.eclipse.core.commands.common.*;
import org.eclipse.e4.core.commands.*;
import org.eclipse.e4.core.contexts.*;
import org.eclipse.e4.core.services.events.*;
import org.eclipse.e4.ui.model.application.ui.*;
import org.eclipse.e4.ui.model.application.ui.advanced.*;
import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.eclipse.e4.ui.workbench.*;
import org.eclipse.e4.ui.workbench.modeling.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * {@link ITsE4Helper} implementation.
 * <p>
 * TODO list:
 * <ul>
 * <li>Listen to the E4 events and fire {@link ITsE4PerspectiveSwitchListener} events.</li>
 * </ul>
 *
 * @author hazard157
 */
public class TsE4Helper
    implements ITsE4Helper {

  /**
   * {@link ITsE4Helper#perspectiveEventer()} implementation.
   *
   * @author hazard157
   */
  class PerspectiveEventer
      extends AbstractTsEventer<ITsE4PerspectiveSwitchListener> {

    private String  perspectiveId = null;
    private boolean wasChange     = false;

    private void reallyFire( String aPerspectiveId ) {
      for( ITsE4PerspectiveSwitchListener l : listeners() ) {
        try {
          l.onPerspectiveChanged( TsE4Helper.this, aPerspectiveId );
        }
        catch( Exception ex ) {
          LoggerUtils.errorLogger().error( ex );
        }
      }
    }

    @Override
    protected boolean doIsPendingEvents() {
      return wasChange;
    }

    @Override
    protected void doFirePendingEvents() {
      reallyFire( perspectiveId );
    }

    @Override
    protected void doClearPendingEvents() {
      wasChange = false;
      perspectiveId = null;
    }

    void fireEvent( String aPerspectiveId ) {
      if( isFiringPaused() ) {
        perspectiveId = aPerspectiveId;
        wasChange = true;
        return;
      }
      reallyFire( aPerspectiveId );
    }

  }

  private final PerspectiveEventer perspectiveEventer = new PerspectiveEventer();

  private final IEclipseContext windowContext;

  /**
   * Конструктор.
   * <p>
   * Внимание: аргумент должен быть контекстом уровня окна (или ниже, уровня вью).
   *
   * @param aAppContext {@link IEclipseContext} - контекст приложения уровня окна
   * @throws TsNullArgumentRtException аргумент = null
   */
  public TsE4Helper( IEclipseContext aAppContext ) {
    TsNullArgumentRtException.checkNull( aAppContext );
    windowContext = aAppContext;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns windows level context.
   *
   * @return {@link IEclipseContext} - windows level context
   */
  public IEclipseContext eclipseContext() {
    return windowContext;
  }

  // ------------------------------------------------------------------------------------
  // ITsE4Helper
  //

  @Override
  public void updateHandlersCanExecuteState() {
    IEventBroker eventBroker = windowContext.getActive( IEventBroker.class );
    eventBroker.send( UIEvents.REQUEST_ENABLEMENT_UPDATE_TOPIC, UIEvents.ALL_ELEMENT_ID );
  }

  @Override
  public MPart switchToPerspective( String aPerspectiveId, String aActivatePartId ) {
    TsNullArgumentRtException.checkNull( aPerspectiveId );
    EModelService modelService = windowContext.get( EModelService.class );
    MWindow window = windowContext.get( MWindow.class );
    MPerspective persp = (MPerspective)modelService.find( aPerspectiveId, window );
    MPart part = null;
    if( persp != null ) {
      EPartService partService = windowContext.get( EPartService.class );
      partService.switchPerspective( persp );
      // сделать активным вью
      if( aActivatePartId != null ) {
        part = partService.findPart( aActivatePartId );
        if( part != null ) {
          partService.activate( part, true );
        }
      }
      // fire perspective event
      perspectiveEventer.fireEvent( aPerspectiveId );
    }
    updateHandlersCanExecuteState();
    return part;
  }

  @Override
  public String currentPerspId() {
    EModelService modelService = windowContext.get( EModelService.class );
    MPerspective perspective = modelService.getActivePerspective( windowContext.get( MWindow.class ) );
    if( perspective != null ) {
      return perspective.getElementId();
    }
    return null;
  }

  @Override
  public String currentPartId() {
    EPartService partService = windowContext.get( EPartService.class );
    MPart part = partService.getActivePart();
    if( part != null ) {
      return part.getElementId();
    }
    return null;
  }

  @Override
  public void quitApplication() {
    MwsMainWindowStaff mainWindowStaff = windowContext.get( MwsMainWindowStaff.class );
    if( mainWindowStaff.canCloseWindow() ) {
      IWorkbench workbench = windowContext.get( IWorkbench.class );
      // TODO надо решить вопрос - закрывается одно окно, какого хера завершать всё приложение?
      // это работает если в приложении одно главное окно, а если несколько?
      workbench.close();
    }
  }

  @Override
  public void execCmd( String aCmdId ) {
    TsNullArgumentRtException.checkNull( aCmdId );
    ECommandService commandService = windowContext.get( ECommandService.class );
    Command cmd = commandService.getCommand( aCmdId );
    EHandlerService handlerService = windowContext.get( EHandlerService.class );
    ParameterizedCommand pCmd = new ParameterizedCommand( cmd, null );
    if( handlerService.canExecute( pCmd ) ) {
      handlerService.executeHandler( pCmd );
    }
  }

  @Override
  public void execCmd( String aCmdId, IStringMap<String> aArgValues ) {
    TsNullArgumentRtException.checkNulls( aCmdId, aArgValues );
    ECommandService commandService = windowContext.get( ECommandService.class );
    Command cmd = commandService.getCommand( aCmdId );
    Parameterization[] params = null;
    // prepare command params (args)
    if( !aArgValues.isEmpty() ) {
      params = new Parameterization[aArgValues.size()];
      int index = 0;
      for( String argId : aArgValues.keys() ) {
        IParameter par = null;
        try {
          par = cmd.getParameter( argId );
        }
        catch( @SuppressWarnings( "unused" ) NotDefinedException ex ) {
          // exception will be thrown below
        }
        if( par == null ) {
          throw new TsItemNotFoundRtException( FMT_ERR_NO_SUCH_ARG_OF_E4_CMD, aCmdId, argId );
        }
        params[index++] = new Parameterization( par, aArgValues.getByKey( argId ) );
      }
    }
    EHandlerService handlerService = windowContext.get( EHandlerService.class );
    ParameterizedCommand pCmd = new ParameterizedCommand( cmd, params );
    if( handlerService.canExecute( pCmd ) ) {
      handlerService.executeHandler( pCmd );
    }
  }

  /**
   * Finds specified element in e4 model of application.
   *
   * @param <T> - the type of element being searched for
   * @param aRoot {@link MElementContainer} - search root in e4 model of application
   * @param aId String - the ID of element being searched for
   * @param aClass {@link Class}&lt;T&gt; - the type of element being searched for
   * @param aFlags int - search flags {@link EModelService}<code>.XXX</code>
   * @return &lt;T&gt; - found element or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  @Override
  public <T> T findElement( MElementContainer<?> aRoot, String aId, Class<T> aClass, int aFlags ) {
    TsNullArgumentRtException.checkNulls( aRoot, aId, aClass );
    ElementMatcher matcher = new ElementMatcher( aId, aClass, (String)null );
    EModelService modelService = windowContext.get( EModelService.class );
    List<T> elems = modelService.findElements( aRoot, aClass, aFlags, matcher );
    if( elems.isEmpty() ) {
      return null;
    }
    return elems.get( 0 );
  }

  @Override
  public ITsEventer<ITsE4PerspectiveSwitchListener> perspectiveEventer() {
    return perspectiveEventer;
  }

}

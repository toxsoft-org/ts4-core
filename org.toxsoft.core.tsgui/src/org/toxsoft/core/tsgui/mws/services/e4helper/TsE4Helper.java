package org.toxsoft.core.tsgui.mws.services.e4helper;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.toxsoft.core.tsgui.mws.bases.MwsMainWindowStaff;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Реализация {@link ITsE4Helper}.
 *
 * @author hazard157
 */
public class TsE4Helper
    implements ITsE4Helper {

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

}

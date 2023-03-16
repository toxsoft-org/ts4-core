package org.toxsoft.core.tsgui.mws.bases;

import static org.toxsoft.core.tslib.ITsHardConstants.*;

import javax.annotation.*;
import javax.inject.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.eclipse.e4.ui.workbench.modeling.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * Базовый класс для всех вью приложений на платформе e4.
 *
 * @author hazard157
 */
public abstract class MwsAbstractPart
    implements ITsGuiContextable {

  /**
   * Calls windows lifecycle handling methods of the registered quants and this addon.
   */
  class WindowLifeCylceListener
      implements IMainWindowLifeCylceListener {

    @Override
    public void beforeMainWindowOpen( IEclipseContext aWinContext, MWindow aWindow ) {
      appWideQuantManager.initWin( aWinContext );
    }

    @Override
    public boolean canCloseMainWindow( IEclipseContext aWinContext, MWindow aWindow ) {
      return appWideQuantManager.canCloseMainWindow( aWinContext, aWindow );
    }

    @Override
    public void beforeMainWindowClose( IEclipseContext aWinContext, MWindow aWindow ) {
      appWideQuantManager.whenCloseMainWindow( aWinContext, aWindow );
    }

  }

  @Inject
  IEclipseContext partContext;

  @Inject
  MWindow window;

  @Inject
  MPart selfPart;

  @Inject
  MwaWindowStaff winStaff;

  @Inject
  IApplicationWideQuantManager appWideQuantManager;

  IMainWindowLifeCylceListener mainWindowLifeCylceListener = new WindowLifeCylceListener();

  /**
   * Listens to the part service events to call whenXxx() methods of this class.
   */
  private final IPartListener partListener = new IPartListener() {

    @Override
    public void partVisible( MPart aPart ) {
      if( selfPart == aPart ) {
        whenPartVisible();
      }
    }

    @Override
    public void partHidden( MPart aPart ) {
      if( selfPart == aPart ) {
        whenPartHidden();
      }
    }

    @Override
    public void partDeactivated( MPart aPart ) {
      if( selfPart == aPart ) {
        whenPartDeactivated();
      }
    }

    @Override
    public void partBroughtToTop( MPart aPart ) {
      if( selfPart == aPart ) {
        whenPartBroughtToTop();
      }
    }

    @Override
    public void partActivated( MPart aPart ) {
      if( selfPart == aPart ) {
        whenPartActivated();
      }
    }
  };

  /**
   * TS context is initialized in tsContext().
   */
  private ITsGuiContext tsContext = null;

  /**
   * The key of the {@link Boolean} reference in the window context as the sign of the .
   */
  private static final String KEY_IS_INITED_PARTS_FOR_WINDOW = TS_FULL_ID + ".MwsWindowWasInited"; //$NON-NLS-1$

  /**
   * Constructor.
   */
  protected MwsAbstractPart() {
    // nop
  }

  @PostConstruct
  final void init( Composite aParent ) {
    boolean wasWindowInited = isWindowInitFlag();
    if( !wasWindowInited ) {
      setWindowInitFlag();
      winStaff.addMainWindowLifecycleInterceptor( mainWindowLifeCylceListener );
    }
    EPartService partService = getWindowContext().get( EPartService.class );
    partService.addPartListener( partListener );
    if( !wasWindowInited ) {
      setWindowInitFlag();
      winStaff.fireBeforeWindowOpenEvent();
    }
    try {
      doInit( aParent );
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
    }
  }

  @PreDestroy
  final void destroyPart() {
    try {
      EPartService partService = getWindowContext().get( EPartService.class );
      partService.removePartListener( partListener );
      winStaff.removeMainWindowLifecycleInterceptor( mainWindowLifeCylceListener );
      beforeDestroy();
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
    }
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private final boolean isWindowInitFlag() {
    Object val = window.getContext().get( KEY_IS_INITED_PARTS_FOR_WINDOW );
    if( val instanceof Boolean boolVal ) {
      if( boolVal.booleanValue() ) {
        return true;
      }
    }
    return false;
  }

  private final void setWindowInitFlag() {
    window.getContext().set( KEY_IS_INITED_PARTS_FOR_WINDOW, Boolean.TRUE );
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    if( tsContext == null ) {
      tsContext = new TsGuiContext( partContext );
    }
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // API класса
  //

  /**
   * Передает фокус клавиатуры этому вью (если функциональность реализована наследником).
   *
   * @return boolean - аналогично {@link Control#setFocus()}
   */
  public boolean setFocusOnPart() {
    return doSetFocusOnPart();
  }

  // ------------------------------------------------------------------------------------
  // API для наследников
  //

  /**
   * Возвращает контекст приложения уровня окна приложения.
   * <p>
   * Внимание: метод {@link #getWindowContext()} возвращает контекст, связанный с окном, но гораздо чаще нужен контекст
   * уровня вью, который возвращается в {@link #partContext()}. Используйте именно {@link #partContext()}, если не
   * пишете что-то чрезвичайно системно-уровневое.
   *
   * @return {@link IEclipseContext} - контекст приложения уровня окна приложения
   */
  public IEclipseContext getWindowContext() {
    return window.getContext();
  }

  /**
   * Возвращает контекст приложения уровня вью.
   *
   * @return {@link IEclipseContext} - контекст приложения уровня вью
   */
  public IEclipseContext partContext() {
    return partContext;
  }

  /**
   * Возврвщает модель этого вью.
   *
   * @return {@link MPart} - модель этого вью
   */
  public MPart getSelfPart() {
    return selfPart;
  }

  /**
   * Опредеяет, является ли вью видимым.
   * <p>
   * <b>Внимание:</b> нельзя вызывать из {@link #doInit(Composite)}, поскольку к этому моменту вью не полностью
   * инициализировано.
   *
   * @return boolean - признак видимого вью
   */
  public boolean isPartVisible() {
    EPartService partService = window.getContext().get( EPartService.class );
    return partService.isPartVisible( selfPart );
  }

  // ------------------------------------------------------------------------------------
  // Методы для переопределения наследниками
  //

  /**
   * Наследник может определиь, какому виджету передать фокус при явном вызове {@link #setFocusOnPart()}.
   * <p>
   * В базовом классе просто возвращает false, при переопределении вызывать родительский метод не нужно.
   *
   * @return boolean - аналогично {@link Control#setFocus()}
   */
  protected boolean doSetFocusOnPart() {
    return false;
  }

  // ------------------------------------------------------------------------------------
  // Called from IPartListener, if event is releated to this part
  //

  protected void whenPartVisible() {
    // nop
  }

  protected void whenPartHidden() {
    // nop
  }

  protected void whenPartDeactivated() {
    // nop
  }

  protected void whenPartBroughtToTop() {
    // nop
  }

  protected void whenPartActivated() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // Subclass
  //

  /**
   * Subclass must create part content.
   *
   * @param aParent {@link Composite} - the parent for part content SWT widgets
   */
  abstract protected void doInit( Composite aParent );

  /**
   * Called before this part is destroyed.
   */
  protected void beforeDestroy() {
    // nop
  }

}

package org.toxsoft.core.tsgui.mws.bases;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.*;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContextable;
import org.toxsoft.core.tsgui.bricks.ctx.impl.TsGuiContext;
import org.toxsoft.core.tsgui.mws.services.e4helper.ITsE4Helper;
import org.toxsoft.core.tsgui.mws.services.e4helper.TsE4Helper;
import org.toxsoft.core.tsgui.utils.TsGuiUtils;
import org.toxsoft.core.tslib.utils.errors.TsInternalErrorRtException;
import org.toxsoft.core.tslib.utils.logs.impl.LoggerUtils;

/**
 * Базовый класс для всех вью приложений на платформе e4.
 *
 * @author hazard157
 */
public abstract class MwsAbstractPart
    implements ITsGuiContextable {

  @Inject
  IEclipseContext partContext;

  @Inject
  MWindow window;

  @Inject
  MPart selfPart;

  @Inject
  MwsMainWindowStaff mainStaff;

  private final IWindowCloseHandler closeHandler = new IWindowCloseHandler() {

    @Override
    public boolean close( MWindow aWindow ) {
      if( mainStaff.canCloseWindow() ) {
        mainStaff.fireBeforeWindowCloseEvent();
        return true;
      }
      return false;
    }
  };

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
   * Ключ, под которым в контексте окна хранится {@link Boolean} признак инициализации частей для окна {@link #window}.
   */
  private static final String KEY_IS_INITED_PARTS_FOR_WINDOW = "ru.toxsoft.IsInitedPartsForWindow"; //$NON-NLS-1$

  /**
   * TS context is initialized in tsContext().
   */
  private ITsGuiContext tsContext = null;

  /**
   * Пустой конструктор для наследников.
   */
  protected MwsAbstractPart() {
    // nop
  }

  @PostConstruct
  final void init( Composite aParent ) {
    checkAndInitWindow();
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
      beforeDestroy();
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
    }
  }

  // ------------------------------------------------------------------------------------
  // Внутренняя реализация
  //

  /**
   * Метод инициализации, вызывается первым после создания вью в методе {@link #init(Composite)}.
   * <p>
   * Осуществляет одноразовую инициализацию для окна при вызове для первого вью этого окна.
   */
  private final void checkAndInitWindow() {
    TsInternalErrorRtException.checkNull( window );
    boolean wasWindowInited = wasWindowInit();
    if( !wasWindowInited ) {
      initOncePerWindow();
    }
    initOncePerView();
    if( !wasWindowInited ) {
      setWindowInitFlag();
      mainStaff.fireBeforeWindowOpenEvent();
    }
  }

  private void initOncePerWindow() {
    mainStaff.setWindow( window );
    window.getContext().set( IWindowCloseHandler.class, closeHandler );
    window.getContext().set( ITsE4Helper.class, new TsE4Helper( window.getContext() ) );
    TsGuiUtils.storeGuiThreadWinContext( window.getContext() );
  }

  private void initOncePerView() {
    // слушаем изменения в состоянии вью
    EPartService partService = getWindowContext().get( EPartService.class );
    partService.addPartListener( partListener );
  }

  private final boolean wasWindowInit() {
    Object val = window.getContext().get( KEY_IS_INITED_PARTS_FOR_WINDOW );
    if( val instanceof Boolean ) {
      Boolean boolVal = (Boolean)val;
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
  // Вызываемые из IPartListener, когда событие касается этого вью
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
  // Методы для реализации наследниками
  //

  abstract protected void doInit( Composite aParent );

  protected void beforeDestroy() {
    // nop
  }

}

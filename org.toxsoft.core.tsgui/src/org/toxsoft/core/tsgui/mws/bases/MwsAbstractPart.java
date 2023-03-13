package org.toxsoft.core.tsgui.mws.bases;

import javax.annotation.*;
import javax.inject.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.eclipse.e4.ui.workbench.modeling.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

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
  MwaWindowStaff mainStaff;

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

  // /**
  // * Ключ, под которым в контексте окна хранится {@link Boolean} признак инициализации частей для окна {@link
  // #window}.
  // */
  // private static final String KEY_IS_INITED_PARTS_FOR_WINDOW = "ru.toxsoft.IsInitedPartsForWindow"; //$NON-NLS-1$

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
      // initOncePerWindow
      TsGuiUtils.storeGuiThreadWinContext( window.getContext() );
    }
    // initOncePerView
    EPartService partService = getWindowContext().get( EPartService.class );
    partService.addPartListener( partListener );

    if( !wasWindowInited ) {
      setWindowInitFlag();
      mainStaff.fireBeforeWindowOpenEvent();
    }
  }

  // private final boolean wasWindowInit() {
  // Object val = window.getContext().get( KEY_IS_INITED_PARTS_FOR_WINDOW );
  // if( val instanceof Boolean boolVal ) {
  // if( boolVal.booleanValue() ) {
  // return true;
  // }
  // }
  // return false;
  // }
  //
  // private final void setWindowInitFlag() {
  // window.getContext().set( KEY_IS_INITED_PARTS_FOR_WINDOW, Boolean.TRUE );
  // }

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

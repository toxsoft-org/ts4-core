package org.toxsoft.core.tsgui.mws.bases;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.IPartListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContextable;
import org.toxsoft.core.tsgui.bricks.ctx.impl.TsGuiContext;
import org.toxsoft.core.tslib.utils.logs.impl.LoggerUtils;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Inject;

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
  MwsWindowStaff winStaff;

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
   * Constructor.
   */
  protected MwsAbstractPart() {
    // nop
  }

  @PostConstruct
  final void initPart( Composite aParent ) {
    EPartService partService = partContext().get( EPartService.class );
    partService.addPartListener( partListener );
    winStaff.papiOnPartInit( this );
    try {
      tsContext = new TsGuiContext( partContext );
      doInit( aParent );
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
    }
  }

  @PreDestroy
  final void destroyPart() {
    try {
      EPartService partService = partContext().get( EPartService.class );
      partService.removePartListener( partListener );
      winStaff.papiOnPartDestroy( this );
      beforeDestroy();
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
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

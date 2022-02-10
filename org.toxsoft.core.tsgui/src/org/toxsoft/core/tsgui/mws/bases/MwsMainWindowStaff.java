package org.toxsoft.core.tsgui.mws.bases;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.IListEdit;
import org.toxsoft.core.tslib.coll.impl.ElemArrayList;
import org.toxsoft.core.tslib.coll.impl.ElemLinkedBundleList;
import org.toxsoft.core.tslib.utils.errors.TsIllegalStateRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.core.tslib.utils.logs.impl.LoggerUtils;

/**
 * Вспомогательный класс для поддержки взаимодйствия {@link MwsAbstractPart} и {@link MwsAbstractAddon}.
 * <p>
 * Ссылку на экземпляр этого класса {@link MwsAbstractAddon} кладет в контекст окна, и к нему доступ имеет
 * {@link MwsAbstractPart}. Смысл этого заключается в том, чтобы избежать <code>static</code> ссылок, что не работает в
 * RAP e4 приложениях.
 * <p>
 * Методы класса являются потоко-безопсаными.
 *
 * @author hazard157
 */
public class MwsMainWindowStaff {

  /**
   * Список задач жизненного цикла окна.
   * <p>
   * Доступ к списку сделан потоко-безопасным,поскольку список используется для потенциально нескольких окон приложения,
   * каждый из которых имеет свой главный GUI-поток выполнения.
   */
  private final IListEdit<IMainWindowLifeCylceListener> windowInterceptors = new ElemLinkedBundleList<>();

  /**
   * Ссылка на обслуживаемое этим классом окно.
   * <p>
   * Инициализируется первым вью {@link MwsAbstractPart} один раз методом {@link #setWindow(MWindow)}.
   */
  private MWindow window = null;

  /**
   * Конструктор.
   *
   * @param aInterceptor {@link IMainWindowLifeCylceListener} - перехватчик событий главного кона приложения
   * @throws TsNullArgumentRtException аргумент = null
   */
  public MwsMainWindowStaff( IMainWindowLifeCylceListener aInterceptor ) {
    windowInterceptors.add( aInterceptor );
  }

  private boolean isInit() {
    return window != null;
  }

  // ------------------------------------------------------------------------------------
  // Внутренний API
  //

  /**
   * Связывает этот экземпляр с окном.
   *
   * @param aWindow {@link MWindow} - окно
   */
  public void setWindow( MWindow aWindow ) {
    TsNullArgumentRtException.checkNull( aWindow );
    TsIllegalStateRtException.checkNoNull( window );
    window = aWindow;
  }

  // ------------------------------------------------------------------------------------
  // API для пользователей
  //

  /**
   * Добавляет перехватчик событий главного кона приложения.
   * <p>
   * Если такой перехватчик уже добавлен, метод ничего не делает.
   *
   * @param aInterceptor {@link IMainWindowLifeCylceListener} - перехватчик событий главного кона приложения
   * @throws TsNullArgumentRtException аргумент = null
   */
  public void addMainWindowLifecycleInterceptor( IMainWindowLifeCylceListener aInterceptor ) {
    synchronized (windowInterceptors) {
      if( !windowInterceptors.hasElem( aInterceptor ) ) {
        windowInterceptors.add( aInterceptor );
      }
    }
  }

  /**
   * Вызывает слушатели {@link IMainWindowLifeCylceListener#beforeMainWindowOpen(IEclipseContext, MWindow)}.
   */
  public final void fireBeforeWindowOpenEvent() {
    if( !isInit() ) {
      return;
    }
    IList<IMainWindowLifeCylceListener> ll;
    synchronized (windowInterceptors) {
      if( windowInterceptors.isEmpty() ) {
        return;
      }
      ll = new ElemArrayList<>( windowInterceptors );
    }
    for( IMainWindowLifeCylceListener l : ll ) {
      try {
        l.beforeMainWindowOpen( window.getContext(), window );
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex );
      }
    }
  }

  /**
   * Вызывает слушатели {@link IMainWindowLifeCylceListener#beforeMainWindowClose(IEclipseContext, MWindow)}.
   */
  public void fireBeforeWindowCloseEvent() {
    if( !isInit() ) {
      return;
    }
    IList<IMainWindowLifeCylceListener> ll;
    synchronized (windowInterceptors) {
      if( windowInterceptors.isEmpty() ) {
        return;
      }
      ll = new ElemArrayList<>( windowInterceptors );
    }
    for( IMainWindowLifeCylceListener l : ll ) {
      try {
        l.beforeMainWindowClose( window.getContext(), window );
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex );
      }
    }
  }

  /**
   * Определяет, можно ли закрыть окно вызовами
   * {@link IMainWindowLifeCylceListener#canCloseMainWindow(IEclipseContext, MWindow)}.
   *
   * @return boolean - признак разрешения закрытия окна<br>
   *         <b>true</b> - окно будет закрыто;<br>
   *         <b>false</b> - окно останется открытым.
   */
  public boolean canCloseWindow() {
    if( !isInit() ) {
      return true;
    }
    IList<IMainWindowLifeCylceListener> ll;
    boolean canClose = true;
    synchronized (windowInterceptors) {
      ll = new ElemArrayList<>( windowInterceptors );
    }
    for( IMainWindowLifeCylceListener l : ll ) {
      try {
        if( !l.canCloseMainWindow( window.getContext(), window ) ) {
          canClose = false;
          break;
        }
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex );
      }
    }
    if( canClose ) {
      fireBeforeWindowCloseEvent();
    }
    return canClose;
  }

}

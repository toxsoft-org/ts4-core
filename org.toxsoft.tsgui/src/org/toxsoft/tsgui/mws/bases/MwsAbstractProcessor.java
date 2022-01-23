package org.toxsoft.tsgui.mws.bases;

import static org.toxsoft.tsgui.mws.IMwsCoreConstants.*;
import static org.toxsoft.tsgui.mws.bases.ITsResources.*;

import java.util.List;

import javax.annotation.PostConstruct;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.ElementMatcher;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.toxsoft.tsgui.Activator;
import org.toxsoft.tsgui.graphics.icons.EIconSize;
import org.toxsoft.tsgui.mws.IMwsCoreConstants;
import org.toxsoft.tsgui.mws.osgi.IMwsOsgiService;
import org.toxsoft.tslib.utils.errors.TsItemNotFoundRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.tslib.utils.logs.impl.LoggerUtils;

/**
 * Базовый класс для облегчения реализации процессоров E4-модели приложения.
 *
 * @author goga
 */
public abstract class MwsAbstractProcessor {

  private IEclipseContext appContext;
  private MApplication    application;
  private EModelService   modelService;
  private MTrimmedWindow  mainWindow;
  private IMwsOsgiService mwsService;

  /**
   * Пустой конструктор без аргументов - такой же должен быть у наследника.
   */
  protected MwsAbstractProcessor() {
    // nop
  }

  @PostConstruct
  void init( IEclipseContext aAppContext ) {
    TsNullArgumentRtException.checkNull( aAppContext );
    appContext = aAppContext;
    application = aAppContext.get( MApplication.class );
    modelService = aAppContext.get( EModelService.class );
    mainWindow = getElement( application, MWSID_WINDOW_MAIN, MTrimmedWindow.class, EModelService.ANYWHERE );
    mwsService = getOsgiService( IMwsOsgiService.class );
    try {
      doProcess();
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
    }
  }

  // ------------------------------------------------------------------------------------
  // Методы для наследника
  //

  /**
   * Возвращает контекст приложения.
   *
   * @return {@link IEclipseContext} - контекст приложения
   */
  public IEclipseContext appContext() {
    return appContext;
  }

  /**
   * Возвращает приложение (корень E4-модели приложения).
   *
   * @return {@link MApplication} - приложение
   */
  public MApplication application() {
    return application;
  }

  /**
   * Возвращает службу дотспуа к E4-модели приложения.
   *
   * @return {@link EModelService} - служба дотспуа к E4-модели приложения
   */
  public EModelService modelService() {
    return modelService;
  }

  /**
   * Возвращает главное окно модульного АРМ.
   * <p>
   * Возвращается модель окна с идентификатором {@link IMwsCoreConstants#MWSID_WINDOW_MAIN}.
   *
   * @return {@link MTrimmedWindow} - главное окно приложения модельного АРМ
   */
  public MTrimmedWindow mainWindow() {
    return mainWindow;
  }

  /**
   * Возвращает OSGI службу {@link IMwsOsgiService}.
   *
   * @return {@link IMwsOsgiService} - OSGI служба {@link IMwsOsgiService}
   */
  public IMwsOsgiService mwsService() {
    return mwsService;
  }

  /**
   * Находит запрошенный элемент в E4-модели приложения.
   *
   * @param <T> - тип (класс, интерфейс) искомого элемента
   * @param aRoot {@link MElementContainer} - корневой элемент (eptk У4-модели приложения) поиска в поддереве
   * @param aId String - идентификатор искомого элемента
   * @param aClass {@link Class}&lt;T&gt; - тип (класс, интерфейс) искомого элемента
   * @param aFlags int - призаки (флаги) поиска из {@link EModelService}<code>.XXX</code>
   * @return &lt;T&gt; - найденный элемент или <code>null</code>
   */
  public <T> T findElement( MElementContainer<?> aRoot, String aId, Class<T> aClass, int aFlags ) {
    ElementMatcher matcher = new ElementMatcher( aId, aClass, (String)null );
    List<T> elems = modelService.findElements( aRoot, aClass, aFlags, matcher );
    if( elems.isEmpty() ) {
      return null;
    }
    return elems.get( 0 );
  }

  /**
   * Находит запрошенный элемент в E4-модели приложения.
   *
   * @param <T> - тип (класс, интерфейс) искомого элемента
   * @param aRoot {@link MElementContainer} - корневой элемент (eptk У4-модели приложения) поиска в поддереве
   * @param aId String - идентификатор искомого элемента
   * @param aClass {@link Class}&lt;T&gt; - тип (класс, интерфейс) искомого элемента
   * @param aFlags int - призаки (флаги) поиска из {@link EModelService}<code>.XXX</code>
   * @return &lt;T&gt; - найденный элемент или <code>null</code>
   * @throws TsItemNotFoundRtException не найден искомый элемент
   */
  public <T> T getElement( MElementContainer<?> aRoot, String aId, Class<T> aClass, int aFlags ) {
    T elem = findElement( aRoot, aId, aClass, aFlags );
    if( elem == null ) {
      throw new TsItemNotFoundRtException( FMT_ERR_NO_E4_ELEM, aClass.getSimpleName(), aId );
    }
    return elem;
  }

  /**
   * Находит зарегистрированный в OSGi сервис по его типу.
   *
   * @param <S> - тип (класс) сервиса
   * @param aSeviceClass {@link Class}&lt;S&gt; - класс сервиса
   * @return &lt;S&gt; - сервис или <code>null</code>
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   */
  public <S> S findOsgiService( Class<S> aSeviceClass ) {
    TsNullArgumentRtException.checkNull( aSeviceClass );
    BundleContext bundleContext = Activator.getInstance().getBundle().getBundleContext();
    ServiceReference<S> ref = bundleContext.getServiceReference( aSeviceClass );
    if( ref != null ) {
      return bundleContext.getService( ref );
    }
    return null;
  }

  /**
   * Возвращает зарегистрированный в OSGi сервис по его типу.
   *
   * @param <S> - тип (класс) сервиса
   * @param aSeviceClass {@link Class}&lt;S&gt; - класс сервиса
   * @return &lt;S&gt; - сервис или <code>null</code>
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   */
  public <S> S getOsgiService( Class<S> aSeviceClass ) {
    S service = findOsgiService( aSeviceClass );
    if( service == null ) {
      throw new TsItemNotFoundRtException( FMT_ERR_AP_NO_OSGI_SERVICE, aSeviceClass.getSimpleName() );
    }
    return service;
  }

  /**
   * Создает URI значка, который находится в заданном плагине.
   * <p>
   * Расположение значка в плагине должен соответствовать соглашениям tsgui:
   * <ul>
   * <li>файл значка должен иметь раширение ".png" (в нижнем регистре);</li>
   * <li>файл значка должен находится в плагине в подпапке "/icons/isNNxNN/", где "isNNxNN" - идентификатор одной из
   * констант {@link EIconSize#id()};</li>
   * <li>в папке "/icons" должны находится подпапки "/isNNxNN" для каждой константы {@link EIconSize}, и в каждом должен
   * находится файл значка соответствующего размера.</li>
   * </ul>
   * <p>
   * Полученный URI можно использовать для программного указания значка сущностьям E4-модели приложения.
   *
   * @param aPluginId String - идентификатор плагина
   * @param aIconId String - название файла (без расширения) значка
   * @param aIconSize {@link EIconSize} - размер значка
   * @return String - идентификатор значка
   */
  @SuppressWarnings( "nls" )
  public String makePluginIconUri( String aPluginId, String aIconId, EIconSize aIconSize ) {
    return "platform:/plugin/" + aPluginId + "/icons/" + aIconSize.id() + "/" + aIconId + ".png";
  }

  /**
   * Создает URI встроенного в библиотеку (плагин) tsgui значка.
   * <p>
   * Полученный URI можно использовать для программного указания значка сущностьям E4-модели приложения.
   *
   * @param aIconId String - название файла (без расширения) значка
   * @param aIconSize {@link EIconSize} - размер значка
   * @return String - идентификатор значка
   */
  public String makeTsguiIconUri( String aIconId, EIconSize aIconSize ) {
    return makePluginIconUri( Activator.PLUGIN_ID, aIconId, aIconSize );
  }

  // ------------------------------------------------------------------------------------
  // Абстрактные методы
  //

  /**
   * Наследник должен выполнить работу процессора в этом методе.
   */
  protected abstract void doProcess();

}

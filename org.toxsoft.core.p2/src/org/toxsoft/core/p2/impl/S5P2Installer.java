package org.toxsoft.core.p2.impl;

import static java.lang.String.*;
import static org.toxsoft.core.p2.impl.IS5Resources.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.*;
import java.util.Collection;

import org.eclipse.core.runtime.*;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.equinox.p2.core.*;
import org.eclipse.equinox.p2.engine.*;
import org.eclipse.equinox.p2.engine.query.UserVisibleRootQuery;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.operations.*;
import org.eclipse.equinox.p2.query.*;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.*;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.toxsoft.core.log4j.LoggerWrapper;
import org.toxsoft.core.p2.IS5P2Installer;
import org.toxsoft.core.p2.IWorkbenchRestarter;
import org.toxsoft.core.tsgui.dialogs.TsDialogUtils;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.IListEdit;
import org.toxsoft.core.tslib.coll.impl.ElemArrayList;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.StringMap;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.ILogger;

/**
 * Инструментарий p2-обновления rcp-клиентов
 *
 * @author mvk
 */
public class S5P2Installer
    implements IS5P2Installer {

  protected final IWorkbenchRestarter workbenchRestarter;

  /**
   * Журнал работы
   */
  protected static final ILogger logger = LoggerWrapper.getLogger( S5P2Installer.class );

  /**
   * Конструктор
   *
   * @param aWorkbench {@link IWorkbenchRestarter} рабочий стол
   * @throws TsNullArgumentRtException аргумент = null
   */
  public S5P2Installer( IWorkbenchRestarter aWorkbench ) {
    TsNullArgumentRtException.checkNull( aWorkbench );
    workbenchRestarter = aWorkbench;
  }

  // ------------------------------------------------------------------------------------
  // Реализация IS5P2Installer
  //
  @Override
  public void execute( final BundleContext aBundleContext, final Display aDisplay, final Shell aShell,
      final IStringList aRepositories, boolean aSilence ) {
    TsNullArgumentRtException.checkNull( aBundleContext );
    TsNullArgumentRtException.checkNull( aRepositories );
    createProgressMonitorDialog( aShell, MSG_CHECK_UPDATES, aMonitor -> {
      // Создание ссылки на службу провайдера агента обновления
      ServiceReference<?> providerRef = aBundleContext.getServiceReference( IProvisioningAgentProvider.SERVICE_NAME );
      if( providerRef == null ) {
        throw new TsInternalErrorRtException( MSG_ERR_PROVIDER_NOT_FOUND, IProvisioningAgentProvider.SERVICE_NAME );
      }
      try {
        // Получение службы провайдера
        IProvisioningAgentProvider agentProvider = (IProvisioningAgentProvider)aBundleContext.getService( providerRef );
        // null: агент создается для работающей системы
        IProvisioningAgent agent = agentProvider.createAgent( null );
        aMonitor.beginTask( MSG_CHECK_UPDATES, aRepositories.size() );
        // Признак того, что найден хотя бы один репозиторий и была завершена проверка
        boolean checkCompleted = false;
        for( String repo1 : aRepositories ) {
          aMonitor.setTaskName( format( MSG_CHECK_REPOSITORY, repo1 ) );
          if( checkRepository( repo1, agent, aDisplay, aShell, workbenchRestarter, aMonitor,
              aSilence ) == Status.OK_STATUS ) {
            // Найден первый доступный репозиторий
            checkCompleted = true;
            break;
          }
        }
        if( !checkCompleted && !aSilence ) {
          // Нет доступных репозиториев
          StringBuilder sb = new StringBuilder();
          for( int index = 0, n = aRepositories.size(); index < n; index++ ) {
            String repo2 = aRepositories.get( index );
            sb.append( repo2 );
            if( index + 1 < n ) {
              sb.append( '\n' );
            }
          }
          logger.error( MSG_ERR_REPOSITORY_NOT_FOUND, sb.toString() );
          error( aDisplay, aShell, MSG_ERR_REPOSITORY_NOT_FOUND, sb.toString() );
        }
      }
      catch( ProvisionException e ) {
        // Ошибка создания агента обновления
        logger.error( e );
        // GOGA 2020-07-23 --- убираем зависимость от старого S5
        // throw new TsIllegalArgumentRtException( e, MSG_ERR_CREATE_AGENT, cause( e ) );
        String s = e.getLocalizedMessage() != null ? e.getLocalizedMessage() : e.getClass().getSimpleName();
        throw new TsIllegalArgumentRtException( e, MSG_ERR_CREATE_AGENT, s );
        // --- GOGA 2020-07-23
      }
      finally {
        // Освобожение ссылки на службу провайдера
        aBundleContext.ungetService( providerRef );
      }
    } );
  }

  // ------------------------------------------------------------------------------------
  // Методы пакета
  //
  /**
   * Проверка обновлений на репозитории
   *
   * @param aRepository String URL репозитория
   * @param aAgent {@link IProvisioningAgent} агент обновления
   * @param aDisplay {@link Display} объект синхронизации с GUI
   * @param aShell {@link Shell} родительское окно. Может быть null
   * @param aWorkbench {@link IWorkbenchRestarter} рабочий стол программы
   * @param aMonitor {@link IProgressMonitor} монитор выполнения задачи
   * @param aSilence <b>true</b> бесшумный режим работы; <b>false</b> выдавать сообщения об обновлении.
   * @return {@link IStatus} результат проверки обновлений. {@link Status#OK_STATUS} установлено обновление
   * @throws TsNullArgumentRtException любой аргумен = null (кроме aShell)
   */
  static IStatus checkRepository( String aRepository, IProvisioningAgent aAgent, final Display aDisplay,
      final Shell aShell, final IWorkbenchRestarter aWorkbench, IProgressMonitor aMonitor, boolean aSilence ) {
    TsNullArgumentRtException.checkNulls( aRepository, aAgent, aDisplay, aWorkbench, aMonitor );
    // Проверка доступности репозитория
    logger.info( MSG_REPO_PING_REPOSITORY, aRepository );
    aMonitor.subTask( MSG_PING_REPOSITORY );
    if( !pingURL( aRepository, 1000 ) ) {
      logger.warning( MSG_ERR_REPO_NOT_FOUND, aRepository );
      return Status.CANCEL_STATUS;
    }
    // Настройка операции обновления
    final ProvisioningSession session = new ProvisioningSession( aAgent );
    final UpdateOperation operation = new UpdateOperation( session );

    String repository = aRepository;
    // TODO: mvkd workarround (иногда nothing_to_update в windows). - не эффективно
    // источник: https://bugs.eclipse.org/bugs/show_bug.cgi?id=236437#c88
    // if( repository.endsWith( "/" ) == false ) { //$NON-NLS-1$
    // repository = repository + "/"; //$NON-NLS-1$
    // }
    URI uri = null;
    try {
      uri = new URI( repository );
    }
    catch( final URISyntaxException e ) {
      System.err.println( e.getMessage() );
      return Status.CANCEL_STATUS;
    }

    // Определение местоположения репозиториев artifact и metadata
    operation.getProvisioningContext().setArtifactRepositories( uri );
    operation.getProvisioningContext().setMetadataRepositories( uri );

    // SubMonitor sub = SubMonitor.convert( aMonitor, format( MSG_FIND_UPDATES, repository ), 200 );
    // // Запуск операции проверки наличия обновлений
    // IStatus status = operation.resolveModal( sub.newChild( 100 ) );

    logger.info( MSG_REPO_FIND_UPDATES, repository );
    aMonitor.subTask( MSG_FIND_UPDATES );
    // Запуск операции проверки наличия обновлений
    IStatus status = operation.resolveModal( aMonitor );
    if( status.getCode() == UpdateOperation.STATUS_NOTHING_TO_UPDATE ) {
      logger.info( MSG_REPO_UPDATES_NOT_FOUND, repository );
      // Обновления не найдены
      if( !aSilence ) {
        info( aDisplay, aShell, MSG_UPDATES_NOT_FOUND );
      }
      return Status.OK_STATUS;
    }
    // Добавленые модули установки
    IStringMap<S5P2InstallableUnit> addedModules = IStringMap.EMPTY;
    // Удаленные модули установки
    IStringMap<S5P2InstallableUnit> removedModules = IStringMap.EMPTY;
    // План обновления
    IProvisioningPlan plan = operation.getProvisioningPlan();
    if( plan != null ) {
      if( plan.getAdditions() != null ) {
        IInstallableUnit[] planAdditions =
            plan.getAdditions().query( QueryUtil.createIUAnyQuery(), aMonitor ).toArray( IInstallableUnit.class );
        addedModules = unitsToMap( planAdditions );
      }
      if( plan.getRemovals() != null ) {
        IInstallableUnit[] planRemovals =
            plan.getRemovals().query( QueryUtil.createIUAnyQuery(), aMonitor ).toArray( IInstallableUnit.class );
        removedModules = unitsToMap( planRemovals );
      }
    }
    // Создание задачи установки обновлений
    final ProvisioningJob provisioningJob = operation.getProvisioningJob( aMonitor );
    if( provisioningJob == null ) {
      // Запрещено запускать инсталяцию из под среды Eclipse IDE
      if( !aSilence ) {
        error( aDisplay, aShell, MSG_ERR_IDE_NOT_SUPPORT, repository );
      }
      return Status.OK_STATUS;
    }
    // Запуск задачи установки обновлений
    aMonitor.subTask( MSG_INSTALL_UPDATES );
    IStatus retValue = provisioningJob.runModal( aMonitor );
    if( retValue.isOK() ) {
      // Успешное завершение обновления
      String updates = updatesToString( addedModules, removedModules );
      String updateMessage = format( MSG_UPDATES, updates );
      logger.info( updateMessage );
      openRestartWaitDialog( aDisplay, aShell, MSG_INSTALL_COMPLETED, updateMessage, MSG_NEED_RESTART, 10000,
          aWorkbench );
      return Status.OK_STATUS;
    }
    // Неожиданная ошибка обновления
    logger.error( MSG_ERR_UNEXPECTED, retValue, provisioningJob );
    error( aDisplay, aShell, MSG_ERR_UNEXPECTED, retValue, provisioningJob );
    return Status.CANCEL_STATUS;
  }

  // ------------------------------------------------------------------------------------
  // Внутренние методы
  //
  /**
   * Возвращает список установленных модулей
   *
   * @param aAgent {@link IProvisioningAgent} агент обновления
   * @return {@link IInstallableUnit} [] массив установленных модулей
   * @throws TsNullArgumentRtException аргумент = null
   */
  @SuppressWarnings( "unused" )
  private static IInstallableUnit[] getInstalledIUs( IProvisioningAgent aAgent ) {
    TsNullArgumentRtException.checkNull( aAgent );
    IProfileRegistry registry = (IProfileRegistry)aAgent.getService( IProfileRegistry.SERVICE_NAME );
    IProfile profile = registry.getProfile( IProfileRegistry.SELF );
    if( profile == null ) {
      return new IInstallableUnit[0];
    }
    IQuery<IInstallableUnit> query = new UserVisibleRootQuery();
    IQueryResult<IInstallableUnit> queryResult = profile.query( query, null );
    Collection<IInstallableUnit> units = queryResult.toUnmodifiableSet();
    IInstallableUnit[] retValue = new IInstallableUnit[units.size()];
    int index = 0;
    for( IInstallableUnit unit : units ) {
      retValue[index++] = unit;
    }
    return retValue;
  }

  /**
   * Проводит проверку доступности сайта указанного через {@link URL}.
   * <p>
   * Для проверки используется отправка пакетов 'HEAD request' и обработка кода ответа в диапазоне 200-399.
   *
   * @param aUrl String URL для проверки.
   * @param timeout int таймаут (мсек) суммарный для отправки и получения ответа.
   * @return <b>true</b> указанный адрес доступен для обращения; <b>false</b> указанный адрес недоступен для обращения.
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  @SuppressWarnings( "nls" )
  static boolean pingURL( String aUrl, int timeout ) {
    TsNullArgumentRtException.checkNull( aUrl );
    // Otherwise an exception may be thrown on invalid SSL certificates.
    String url = aUrl.replaceFirst( "^https", "http" );
    try {
      HttpURLConnection connection = (HttpURLConnection)new URL( url + "/content.jar" ).openConnection();
      connection.setConnectTimeout( timeout );
      connection.setReadTimeout( timeout );
      connection.setRequestMethod( "HEAD" );
      int responseCode = connection.getResponseCode();
      return (200 <= responseCode && responseCode <= 399);
    }
    catch( @SuppressWarnings( "unused" ) IOException exception ) {
      return false;
    }
  }

  /**
   * Возвращает список модулей установки в виде s5-идентфицируемого списка
   *
   * @param aUnits {@link IInstallableUnit} [] массив модулей установки
   * @return {@link IStringMap}&lt;{@link S5P2InstallableUnit}&gt; список модулей установки
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  private static IStringMap<S5P2InstallableUnit> unitsToMap( IInstallableUnit[] aUnits ) {
    TsNullArgumentRtException.checkNull( aUnits );
    IStringMapEdit<S5P2InstallableUnit> retValue = new StringMap<>();
    for( int index = 0, n = aUnits.length; index < n; index++ ) {
      IInstallableUnit unit = aUnits[index];
      // TODO: системные плагины которым добавляется префикс "tooling". Возможно обработку этого можно сделать иначе
      if( unit.getId().startsWith( "tooling" ) ) { //$NON-NLS-1$
        continue;
      }
      retValue.put( unit.getId(), new S5P2InstallableUnit( unit ) );
    }
    return retValue;
  }

  /**
   * Возвращает тестовое представление произведенных изменений в программе
   *
   * @param aAddedModules {@link IList} список добавленных модулей
   * @param aRemovedModules {@link IList} список удаленных модулей
   * @return String текствовое представление обновления
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  private static String updatesToString( IStringMap<S5P2InstallableUnit> aAddedModules,
      IStringMap<S5P2InstallableUnit> aRemovedModules ) {
    TsNullArgumentRtException.checkNulls( aAddedModules, aRemovedModules );
    // Формирование полного списка модулей
    IListEdit<S5P2InstallableUnit> allUnits = new ElemArrayList<>( aAddedModules );
    for( S5P2InstallableUnit unit : aRemovedModules ) {
      if( !allUnits.hasElem( unit ) ) {
        allUnits.add( unit );
      }
    }
    StringBuilder retValue = new StringBuilder();
    for( int index = 0, n = allUnits.size(); index < n; index++ ) {
      S5P2InstallableUnit unit = allUnits.get( index );
      String id = unit.id();
      S5P2InstallableUnit added = aAddedModules.findByKey( id );
      S5P2InstallableUnit removed = aRemovedModules.findByKey( id );
      if( added != null && removed == null ) {
        retValue.append( format( MSG_ADDED_MODULE, id, added.unit().getVersion() ) );
      }
      if( added == null && removed != null ) {
        retValue.append( format( MSG_REMOVED_MODULE, id, removed.unit().getVersion() ) );
      }
      if( added != null && removed != null ) {
        Version removeVersion = removed.unit().getVersion();
        Version addVersion = added.unit().getVersion();
        if( removeVersion.equals( addVersion ) ) {
          // TODO: Версия не менялась. Интересно что изменилось (время сборки, размер, ... )?
          continue;
        }
        retValue.append( format( "%s (%s => %s)", id, removeVersion, addVersion ) ); //$NON-NLS-1$
      }
      if( index + 1 < n ) {
        retValue.append( '\n' );
      }
    }
    return retValue.toString();
  }

  /**
   * Вывод диалога с сообщением
   *
   * @param aDisplay {@link Display} объект синхронизации с GUI
   * @param aShell {@link Shell} родительское окно
   * @param aMsg String сообщение для вывода
   * @param aArgs Object... параметры для сообщения
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  static void info( Display aDisplay, Shell aShell, String aMsg, Object... aArgs ) {
    TsNullArgumentRtException.checkNulls( aDisplay, aMsg );
    aDisplay.syncExec( () -> TsDialogUtils.info( aShell, aMsg, aArgs ) );
  }

  /**
   * Вывод диалога об ошибке
   *
   * @param aDisplay {@link Display} объект синхронизации с GUI
   * @param aShell {@link Shell} родительское окно
   * @param aMsg String сообщение для вывода
   * @param aArgs Object... параметры для сообщения
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  static void error( Display aDisplay, Shell aShell, String aMsg, Object... aArgs ) {
    TsNullArgumentRtException.checkNulls( aDisplay, aMsg );
    aDisplay.syncExec( () -> TsDialogUtils.error( aShell, aMsg, aArgs ) );
  }

  /**
   * Выполняет работы в отдельном потоке при открытом диалоге ожидания.
   *
   * @param aShell {@link Shell} родительское окно
   * @param aDialogName String - имя диалога ожидания.
   * @param aRunnable IRunnableWithProgress - реализация потока выполнения работы.
   */
  static void createProgressMonitorDialog( final Shell aShell, final String aDialogName,
      final IRunnableWithProgress aRunnable ) {

    final ProgressMonitorDialog dialog = new ProgressMonitorDialog( aShell ) {

      @Override
      protected Control createDialogArea( Composite aParent ) {

        Control c = super.createDialogArea( aParent );
        c.getShell().setText( aDialogName );
        return c;
      }
    };

    try {
      dialog.run( true, true, aRunnable );
    }
    catch( InvocationTargetException | InterruptedException e ) {
      logger.error( e );
    }
  }

  /**
   * Выполняет работы в отдельном потоке при открытом диалоге ожидания.
   *
   * @param aDisplay {@link UISynchronize} объект синхронизации с GUI
   * @param aShell {@link Shell} родительское окно. null: нет родительского окна
   * @param aDialogName String - имя диалога ожидания.
   * @param aTopMessage String сообщение выводимое внутри диалога сверху прогресс-бара
   * @param aBottomMessage String сообщение выводимое внутри диалога снизу прогресс-бара
   * @param aTimeout long long таймаут отображения диалога
   * @param aWorkbech {@link IWorkbenchRestarter} рабочий стол для перезапуска
   * @throws TsNullArgumentRtException любой аргумент = null (кроме aShell)
   */
  static void openRestartWaitDialog( Display aDisplay, final Shell aShell, final String aDialogName,
      final String aTopMessage, final String aBottomMessage, long aTimeout, IWorkbenchRestarter aWorkbech ) {
    TsNullArgumentRtException.checkNulls( aDialogName, aTopMessage, aBottomMessage );

    aDisplay.syncExec( () -> {
      try {
        S5P2WaitDialog dialog =
            new S5P2WaitDialog( aDisplay, aShell, aDialogName, aTopMessage, aBottomMessage, aTimeout );
        dialog.open();
        if( dialog.getReturnCode() != IDialogConstants.CANCEL_ID ) {
          aWorkbech.restart();
        }
      }
      catch( Exception e ) {
        logger.error( e );
      }
    } );
  }
}

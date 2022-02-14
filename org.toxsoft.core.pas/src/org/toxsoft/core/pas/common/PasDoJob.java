package org.toxsoft.core.pas.common;

import static org.toxsoft.core.pas.common.ITsResources.*;

import org.toxsoft.core.tslib.bricks.ICooperativeMultiTaskable;
import org.toxsoft.core.tslib.utils.ICloseable;
import org.toxsoft.core.tslib.utils.errors.TsInternalErrorRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.core.tslib.utils.logs.ILogger;

/**
 * Фоновая задача подсистемы pas
 *
 * @author mvk
 */
public class PasDoJob
    implements Runnable, ICloseable {

  /**
   * Таймаут работы по умолчанию
   */
  private static final long DOJOB_TIMEOUT_DEFAULT = 500;

  /**
   * Имя задачи
   */
  private final String name;

  /**
   * Задача
   */
  private final ICooperativeMultiTaskable pasTask;

  /**
   * Используемый таймаут работы
   */
  private final long doJobTimeout;

  /**
   * Поток фоновой задачи
   */
  private volatile Thread doJobThread;

  /**
   * Признак требования завершить работу фоновую задачу
   */
  private volatile boolean stopQueried = false;

  /**
   * Журнал работы
   */
  private final ILogger logger;

  /**
   * Конструктор
   *
   * @param aName String имя задачи
   * @param aPasTask {@link ICooperativeMultiTaskable} фоновая задача pas
   * @param aLogger {@link ILogger} журнал работы
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public PasDoJob( String aName, ICooperativeMultiTaskable aPasTask, ILogger aLogger ) {
    name = TsNullArgumentRtException.checkNull( aName );
    pasTask = TsNullArgumentRtException.checkNull( aPasTask );
    doJobTimeout = DOJOB_TIMEOUT_DEFAULT;
    logger = TsNullArgumentRtException.checkNull( aLogger );
  }

  /**
   * Конструктор
   *
   * @param aName String имя задачи
   * @param aPasTask {@link ICooperativeMultiTaskable} фоновая задача pas
   * @param aDojobTimeout long таймаут выполнения задачи doJob
   * @param aLogger {@link ILogger} журнал работы
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public PasDoJob( String aName, ICooperativeMultiTaskable aPasTask, long aDojobTimeout, ILogger aLogger ) {
    name = TsNullArgumentRtException.checkNull( aName );
    pasTask = TsNullArgumentRtException.checkNull( aPasTask );
    doJobTimeout = (aDojobTimeout > 0 ? aDojobTimeout : DOJOB_TIMEOUT_DEFAULT);
    logger = TsNullArgumentRtException.checkNull( aLogger );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса Runnable
  //
  @Override
  public void run() {
    if( doJobThread != null || stopQueried ) {
      // Недопустимый usecase
      throw new TsInternalErrorRtException();
    }
    // Запуск фоновой задачи pasTask
    logger.info( MSG_START_DOJOB );
    doJobThread = Thread.currentThread();
    doJobThread.setName( name );
    stopQueried = false;
    try {
      // вступаем в цикл до момента запроса останова методом queryStop()
      while( !stopQueried ) {
        try {
          try {
            pasTask.doJob();
          }
          catch( Exception e ) {
            logger.error( e );
          }
          Thread.sleep( doJobTimeout );
          logger.debug( doJobThread.getName() );
        }
        catch( @SuppressWarnings( "unused" ) InterruptedException e ) {
          // Остановка фоновой задачи клиента СПД (interrupted)
          logger.info( ERR_DOJOB_INTERRUPT );
        }
      }
    }
    catch( Exception e ) {
      logger.error( e );
    }
    finally {
      // Снимаем с потока doJob состояние interrupted возможно установленное при close
      Thread.interrupted();
      doJobThread = null;
    }
    // Завершение фоновой задачи pasTask
    logger.info( MSG_FINISH_DOJOB );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ICloseable
  //
  @Override
  public void close() {
    Thread thread = doJobThread;
    if( thread == null ) {
      return;
    }
    stopQueried = true;
    try {
      // Поток канала будет находится в состоянии 'interrupted' поэтому любые обращения к блокировкам в этом
      // потоке будет поднимать исключение InterruptedException (например завершение работы с s5). Чтобы не создавать
      // отдельный поток "завершения" мы даем наследнику возможность освободить ресурсы до перехода в состояние
      // 'interrupted'
      // doClose();
    }
    catch( Exception e ) {
      logger.error( e );
    }
    // Прерывание блокирующих вызовов потока
    thread.interrupt();
  }
}

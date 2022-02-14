package org.toxsoft.core.pas.common;

import static org.toxsoft.core.pas.common.ITsResources.*;

import java.io.IOException;

import org.toxsoft.core.pas.tj.ITjObject;
import org.toxsoft.core.tslib.coll.derivative.IQueue;
import org.toxsoft.core.tslib.coll.derivative.Queue;
import org.toxsoft.core.tslib.utils.ICloseable;
import org.toxsoft.core.tslib.utils.errors.TsInternalErrorRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.core.tslib.utils.logs.ILogger;

/**
 * Писатель данных в канал
 *
 * @author mvk
 */
final class PasChannelWriter
    implements Runnable, ICloseable {

  /**
   * Формат имени писателя
   */
  private static final String NAME_FORMAT = "PAS writer[%s]"; //$NON-NLS-1$

  /**
   * Имя писателя
   */
  private final String name;

  /**
   * Канал по которому проводится передача данных
   */
  private final PasChannel channel;

  /**
   * Очередь запросов
   */
  private final IQueue<Call> calls = new Queue<>();

  /**
   * Поток задачи
   */
  private volatile Thread writerThread = null;

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
   * @param aChannel {@link PasChannel} канал по которому проводится передача данных
   * @param aLogger {@link ILogger} журнал работы класса канала
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  PasChannelWriter( PasChannel aChannel, ILogger aLogger ) {
    TsNullArgumentRtException.checkNulls( aChannel, aLogger );
    name = String.format( NAME_FORMAT, aChannel.toString() );
    channel = aChannel;
    logger = aLogger;
    Thread thread = new Thread( this );
    thread.start();
    logger.info( "PasChannelWriter(...): %s", aChannel ); //$NON-NLS-1$
  }

  // ------------------------------------------------------------------------------------
  // Открытое API
  //
  /**
   * Запись объект {@link ITjObject} в канале
   *
   * @param aObj {@link ITjObject} - JSON-объект
   * @throws TsNullArgumentRtException аргумент = null
   * @throws IOException ошибка ввода/вывода
   */
  void writeJsonObject( ITjObject aObj )
      throws IOException {
    TsNullArgumentRtException.checkNull( aObj );
    synchronized (calls) {
      logger.info( "PasChannelWriter.writeJsonObject(...): %s", aObj ); //$NON-NLS-1$
      calls.putTail( new Call( aObj ) );
      calls.notifyAll();
    }
  }

  // ------------------------------------------------------------------------------------
  // Runnable
  //
  @Override
  public void run() {
    if( writerThread != null || stopQueried ) {
      // Недопустимый usecase
      throw new TsInternalErrorRtException();
    }
    writerThread = Thread.currentThread();
    writerThread.setName( name );
    // Запуск поставщика событий бекенда
    logger.info( MSG_START_WRITER, writerThread.getName() );
    stopQueried = false;
    try {
      // вступаем в цикл до момента запроса останова методом queryStop()
      while( !stopQueried ) {
        try {
          // Вызов фронтенда. null: нет вызовов
          Call call = null;
          synchronized (calls) {
            if( calls.size() == 0 ) {
              // Вызовов нет, ждем сигнала о добавлении новых вызовов
              calls.wait();
            }
            call = calls.getHeadOrNull();
            // Выход из критической секции
          }
          if( call != null && !stopQueried ) {
            logger.info( "PasChannelWriter.run(...): %s", call.obj ); //$NON-NLS-1$
            channel.writeJsonObject( call.obj );
          }
        }
        catch( @SuppressWarnings( "unused" ) InterruptedException e ) {
          // Остановка поставщика событий бекенда (interrupt)
          logger.info( ERR_INTERRUPT_WRITER, writerThread.getName() );
        }
      }
    }
    catch( Exception e ) {
      logger.error( e );
    }
    finally {
      // Снимаем с потока doJob состояние interrupted возможно установленное при close
      Thread.interrupted();
      // Завершение работы поставщика событий бекенда (finish)
      logger.info( MSG_FINISH_WRITER, writerThread.getName() );
      // Поток завершил работу
      writerThread = null;
    }
  }

  // ------------------------------------------------------------------------------------
  // ICloseable
  //
  @Override
  public void close() {
    Thread thread = writerThread;
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

  /**
   * Вызов фронтенд
   */
  private static final class Call {

    final ITjObject obj;

    /**
     * Конструктор вызова фронтенд
     *
     * @param aObj {@link ITjObject} - JSON-объект
     * @throws TsNullArgumentRtException любой аргумент = null
     */
    Call( ITjObject aObj ) {
      obj = TsNullArgumentRtException.checkNull( aObj );

    }
  }
}

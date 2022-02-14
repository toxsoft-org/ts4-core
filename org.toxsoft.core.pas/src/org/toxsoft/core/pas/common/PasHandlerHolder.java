package org.toxsoft.core.pas.common;

import static org.toxsoft.core.pas.common.ITsResources.*;

import org.toxsoft.core.pas.json.*;
import org.toxsoft.core.tslib.bricks.ctx.ITsContextRo;
import org.toxsoft.core.tslib.coll.primtypes.IStringMapEdit;
import org.toxsoft.core.tslib.coll.primtypes.impl.StringMap;
import org.toxsoft.core.tslib.coll.synch.SynchronizedStringMap;
import org.toxsoft.core.tslib.utils.errors.TsItemAlreadyExistsRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.core.tslib.utils.logs.ILogger;
import org.toxsoft.core.tslib.utils.logs.impl.LoggerUtils;

/**
 * Хранитель обработчиков событий JSON передаваемых по каналам {@link PasChannel}.
 *
 * @author mvk
 * @param <CHANNEL> тип двунаправленного канала обмена между клиентом и сервером
 */
public class PasHandlerHolder<CHANNEL extends PasChannel> {

  /**
   * Контекст
   */
  private final ITsContextRo context;

  /**
   * Карта зарегистрированных обработчиков уведомлений <br>
   * Ключ: имя метода уведомления {@link IJSONNotification#method()};<br>
   * Значение: Обработчик уведомления {@link IJSONNotificationHandler}.
   */
  private final IStringMapEdit<IJSONNotificationHandler<CHANNEL>> notificationHandlers =
      new SynchronizedStringMap<>( new StringMap<>() );

  /**
   * Карта зарегистрированных обработчиков запросов <br>
   * Ключ: имя запроса {@link IJSONRequest#method()};<br>
   * Значение: Обработчик запроса {@link IJSONRequestHandler}.
   */
  private final IStringMapEdit<IJSONRequestHandler<CHANNEL>> requestHandlers =
      new SynchronizedStringMap<>( new StringMap<>() );

  /**
   * Карта зарегистрированных обработчиков результатов выполнения запросов <br>
   * Ключ: имя запроса {@link IJSONRequest#method()};<br>
   * Значение: Обработчик запроса {@link IJSONResultHandler}.
   */
  private final IStringMapEdit<IJSONResultHandler<CHANNEL>> resultHandlers =
      new SynchronizedStringMap<>( new StringMap<>() );

  /**
   * Карта зарегистрированных обработчиков ошибок выполнения запросов <br>
   * Ключ: имя запроса {@link IJSONRequest#method()};<br>
   * Значение: Обработчик запроса {@link IJSONErrorHandler}.
   */
  private final IStringMapEdit<IJSONErrorHandler<CHANNEL>> errorHandlers =
      new SynchronizedStringMap<>( new StringMap<>() );

  /**
   * Обработчик событий каналов
   */
  private IPasChannelHandler<CHANNEL> channelHandler = new IPasChannelHandler<>() {
    // nop
  };

  /**
   * Журнал работы класса
   */
  private final ILogger logger;

  /**
   * Конструктор.
   *
   * @param aContext {@link ITsContextRo} - контекст приложения, использующего (запускающего) мост
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  protected PasHandlerHolder( ITsContextRo aContext ) {
    context = TsNullArgumentRtException.checkNull( aContext );
    logger = LoggerUtils.defaultLogger();
  }

  /**
   * Конструктор.
   *
   * @param aContext {@link ITsContextRo} - контекст приложения, использующего (запускающего) мост
   * @param aLogger {@link ILogger} реализация журнала работы класса
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  protected PasHandlerHolder( ITsContextRo aContext, ILogger aLogger ) {
    context = TsNullArgumentRtException.checkNull( aContext );
    logger = TsNullArgumentRtException.checkNull( aLogger );
  }

  // ------------------------------------------------------------------------------------
  // Открытое API
  //
  /**
   * Возвращает обработчик событий каналов
   *
   * @return {@link IPasChannelHandler} обработчик событий
   */
  public final IPasChannelHandler<CHANNEL> channelHandler() {
    return channelHandler;
  }

  /**
   * Регистрация обработчика уведомления
   *
   * @param aMethodName String имя метода уведомления
   * @param aHandler {@link IJSONNotificationHandler}
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public final void registerNotificationHandler( String aMethodName, IJSONNotificationHandler<CHANNEL> aHandler ) {
    TsNullArgumentRtException.checkNull( aMethodName );
    TsNullArgumentRtException.checkNull( aHandler );
    // Установка обработчика запросов метода
    logger.debug( MSG_SET_NOTIFICATION_HANDLER, aMethodName, notificationHandlers.findByKey( aMethodName ), aHandler );
    notificationHandlers.put( aMethodName, aHandler );
  }

  /**
   * ДеРегистрация обработчика запроса
   * <p>
   * Если обработчик метода не зарегистрирован, то ничего не делает
   *
   * @param aMethodName String имя метода запроса
   * @return {@link IJSONNotificationHandler} дерегистрированный обработчик. null: обработчик не был зарегистирован
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public final IJSONNotificationHandler<CHANNEL> unregisterNotificationHandler( String aMethodName ) {
    TsNullArgumentRtException.checkNull( aMethodName );
    logger.debug( MSG_SET_NOTIFICATION_HANDLER, aMethodName, notificationHandlers.findByKey( aMethodName ), null );
    return notificationHandlers.removeByKey( aMethodName );
  }

  /**
   * Регистрация обработчика запроса
   *
   * @param aMethodName String имя метода запроса
   * @param aHandler {@link IJSONRequestHandler}
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public final void registerRequestHandler( String aMethodName, IJSONRequestHandler<CHANNEL> aHandler ) {
    TsNullArgumentRtException.checkNull( aMethodName );
    TsNullArgumentRtException.checkNull( aHandler );
    // Установка обработчика запросов метода
    logger.debug( MSG_SET_REQUEST_HANDLER, aMethodName, requestHandlers.findByKey( aMethodName ), aHandler );
    requestHandlers.put( aMethodName, aHandler );
  }

  /**
   * ДеРегистрация обработчика запроса
   * <p>
   * Если обработчик метода не зарегистрирован, то ничего не делает
   *
   * @param aMethodName String имя метода запроса
   * @return {@link IJSONRequestHandler} дерегистрированный обработчик. null: обработчик не был зарегистирован
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public final IJSONRequestHandler<CHANNEL> unregisterRequestHandler( String aMethodName ) {
    TsNullArgumentRtException.checkNull( aMethodName );
    logger.debug( MSG_SET_REQUEST_HANDLER, aMethodName, requestHandlers.findByKey( aMethodName ), null );
    return requestHandlers.removeByKey( aMethodName );
  }

  /**
   * Регистрация обработчика результатов запроса
   *
   * @param aMethodName String имя метода запроса
   * @param aHandler {@link IJSONResultHandler}
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsItemAlreadyExistsRtException обработчик уже зарегистрирован
   */
  public final void registerResultHandler( String aMethodName, IJSONResultHandler<CHANNEL> aHandler ) {
    TsNullArgumentRtException.checkNull( aMethodName );
    TsNullArgumentRtException.checkNull( aHandler );
    // Установка обработчика результатов выполнения метода
    logger.debug( MSG_SET_RESULT_HANDLER, aMethodName, resultHandlers.findByKey( aMethodName ), aHandler );
    resultHandlers.put( aMethodName, aHandler );
  }

  /**
   * ДеРегистрация обработчика результатов запроса
   * <p>
   * Если обработчик метода не зарегистрирован, то ничего не делает
   *
   * @param aMethodName String имя метода запроса
   * @return {@link IJSONResultHandler} дерегистрированный обработчик. null: обработчик не был зарегистирован
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public final IJSONResultHandler<CHANNEL> unregisterResultHandler( String aMethodName ) {
    TsNullArgumentRtException.checkNull( aMethodName );
    // Установка обработчика результатов выполнения метода
    logger.debug( MSG_SET_RESULT_HANDLER, aMethodName, resultHandlers.findByKey( aMethodName ), null );
    return resultHandlers.removeByKey( aMethodName );
  }

  /**
   * Регистрация обработчика ошибок обработки запроса
   *
   * @param aMethodName String имя метода запроса
   * @param aHandler {@link IJSONErrorHandler} обработчик ошибок
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsItemAlreadyExistsRtException обработчик уже зарегистрирован
   */
  public final void registerErrorHandler( String aMethodName, IJSONErrorHandler<CHANNEL> aHandler ) {
    TsNullArgumentRtException.checkNull( aMethodName );
    TsNullArgumentRtException.checkNull( aHandler );
    // Установка обработчика ошибок выполнения метода
    logger.debug( MSG_SET_ERROR_HANDLER, aMethodName, errorHandlers.findByKey( aMethodName ), aHandler );
    errorHandlers.put( aMethodName, aHandler );
  }

  /**
   * ДеРегистрация обработчика ошибок обработки запроса
   * <p>
   * Если обработчик метода не зарегистрирован, то ничего не делает
   *
   * @param aMethodName String имя метода запроса
   * @return {@link IJSONErrorHandler} дерегистрированный обработчик. null: обработчик не был зарегистирован
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public final IJSONErrorHandler<CHANNEL> unregisterErrorHandler( String aMethodName ) {
    TsNullArgumentRtException.checkNull( aMethodName );
    // Установка обработчика ошибок выполнения метода
    logger.debug( MSG_SET_ERROR_HANDLER, aMethodName, errorHandlers.findByKey( aMethodName ), null );
    return errorHandlers.removeByKey( aMethodName );
  }

  /**
   * Возвращает зарегистрированный обработчик уведомления
   *
   * @param aMethod String имя метода
   * @return {@link IJSONRequestHandler} обработчик уведомления. null: незарегистрирован
   * @throws TsNullArgumentRtException аргумент = null
   */
  public final IJSONNotificationHandler<CHANNEL> findNotificationHandler( String aMethod ) {
    TsNullArgumentRtException.checkNull( aMethod );
    return notificationHandlers.findByKey( aMethod );
  }

  /**
   * Возвращает зарегистрированный обработчик метода
   *
   * @param aMethod String имя метода
   * @return {@link IJSONRequestHandler} обработчик метода. null: незарегистрирован
   * @throws TsNullArgumentRtException аргумент = null
   */
  public final IJSONRequestHandler<CHANNEL> findRequestHandler( String aMethod ) {
    TsNullArgumentRtException.checkNull( aMethod );
    return requestHandlers.findByKey( aMethod );
  }

  /**
   * Возвращает зарегистрированный обработчик результатов выполнения метода
   *
   * @param aMethod String имя метода
   * @return {@link IJSONResultHandler} обработчик метода. null: незарегистрирован
   * @throws TsNullArgumentRtException аргумент = null
   */
  public final IJSONResultHandler<CHANNEL> findResultHandler( String aMethod ) {
    TsNullArgumentRtException.checkNull( aMethod );
    return resultHandlers.findByKey( aMethod );
  }

  /**
   * Возвращает зарегистрированный обработчик ошибок выполнения метода
   *
   * @param aMethod String имя метода
   * @return {@link IJSONErrorHandler} обработчик метода. null: незарегистрирован
   * @throws TsNullArgumentRtException аргумент = null
   */
  public final IJSONErrorHandler<CHANNEL> findErrorHandler( String aMethod ) {
    TsNullArgumentRtException.checkNull( aMethod );
    return errorHandlers.findByKey( aMethod );
  }

  // ------------------------------------------------------------------------------------
  // API для наследников
  //
  /**
   * Заменяет обработчик событий канала
   *
   * @param aHandler {@link IPasChannelHandler} обработчик событий канала
   * @throws TsNullArgumentRtException аргумент = null
   */
  protected final void setChannelHandler( IPasChannelHandler<CHANNEL> aHandler ) {
    // Установка обработчика событий канала
    logger.debug( MSG_SET_CHANNEL_HANDLER, channelHandler, aHandler );
    channelHandler = TsNullArgumentRtException.checkNull( aHandler );
  }

  /**
   * Возвращает контекст запускающего приложения.
   *
   * @return {@link ITsContextRo} - контекст запускающего приложения
   */
  protected final ITsContextRo context() {
    return context;
  }

  /**
   * Возвращает журнал работы класса
   *
   * @return {@link ILogger} журнал работы
   */
  protected final ILogger logger() {
    return logger;
  }
}

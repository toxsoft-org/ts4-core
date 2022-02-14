package org.toxsoft.core.pas.common;

import static org.toxsoft.core.pas.common.ITsResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.impl.DataDef.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.pas.server.PasServer;
import org.toxsoft.core.tslib.av.EAtomicType;
import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.av.metainfo.IDataDef;

/**
 * Известные параметры, используемые сетью Public Access Server {@link PasServer}.
 *
 * @author mvk
 */
public interface IPasParams {

  // ------------------------------------------------------------------------------------
  // Конфигурационные параметры PAS (Public Access Server)
  //
  /**
   * Таймаут (мсек) в течении которого по каналу должен быть передан хотя бы одно сообщение. В противном случае, канал
   * будет закрыт
   * <p>
   * <= 0: Отключение механизма проверки работоспособности канала
   * <p>
   * {@link IAtomicValue#NULL}: используется таймаут принятый от удаленной стороны
   * <p>
   * Тип: {@link EAtomicType#INTEGER}
   */
  IDataDef OP_PAS_FAILURE_TIMEOUT = create( "failureTimeout", INTEGER, //$NON-NLS-1$
      TSID_NAME, STR_N_PAS_FAILURE_TIMEOUT, //
      TSID_DESCRIPTION, STR_D_PAS_FAILURE_TIMEOUT, //
      TSID_IS_NULL_ALLOWED, AV_TRUE, //
      TSID_DEFAULT_VALUE, avInt( 60000 ) );

  /**
   * Требование отправлять пинг-запросы для проверки работоспособности канала.
   * <p>
   * Частота отправки ping-сообщений = ( 2 * failureTimeout ) / 3. Если failureTimeout <= 0 или
   * {@link IAtomicValue#NULL}, то пинг не передается
   * <p>
   * Тип: {@link EAtomicType#BOOLEAN}
   */
  IDataDef OP_PAS_SEND_PING = create( "sendPing", BOOLEAN, //$NON-NLS-1$
      TSID_NAME, STR_N_PAS_SEND_PING, //
      TSID_DESCRIPTION, STR_D_PAS_SEND_PING, //
      TSID_IS_NULL_ALLOWED, AV_TRUE, //
      TSID_DEFAULT_VALUE, avBool( true ) );

  /**
   * Таймаут (мсек), в течении которого, поток записи должен завершить запись данных в канал. В противном случае, канал
   * будет закрыт.
   * <p>
   * Необходимость таймаута определена в источнике:
   * https://stackoverflow.com/questions/1338885/java-socket-output-stream-writes-do-they-block
   * <p>
   * <= 0: Отключение механизма проверки работоспособности канала
   * <p>
   * Тип: {@link EAtomicType#INTEGER}
   */
  IDataDef OP_PAS_WRITE_TIMEOUT = create( "writeTimeout", INTEGER, //$NON-NLS-1$
      TSID_NAME, STR_N_PAS_WRITE_TIMEOUT, //
      TSID_DESCRIPTION, STR_D_PAS_WRITE_TIMEOUT, //
      TSID_IS_NULL_ALLOWED, AV_TRUE, //
      TSID_DEFAULT_VALUE, avInt( 5000 ) );

}

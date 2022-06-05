package org.toxsoft.core.pas.server;

import static org.toxsoft.core.pas.server.ITsResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.impl.DataDef.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.pas.common.IPasParams;
import org.toxsoft.core.tslib.av.metainfo.IDataDef;

/**
 * Известные параметры, используемые сервером {@link PasServer}.
 *
 * @author hazard157
 */
public interface IPasServerParams
    extends IPasParams {

  // ------------------------------------------------------------------------------------
  // Конфигурационные параметры СПД (сервера публичного доступа)
  //

  /**
   * IP-адрес или текстовое имя сервера публичного доступа (пустая строка: выбрать автоматически).
   */
  IDataDef OP_PAS_SERVER_ADDRESS = create( "PasServerAddress", STRING, //$NON-NLS-1$
      TSID_NAME, STR_N_PAS_SERVER_ADDRESS, //
      TSID_DESCRIPTION, STR_D_PAS_SERVER_ADDRESS, //
      TSID_IS_NULL_ALLOWED, AV_TRUE, //
      TSID_DEFAULT_VALUE, avStr( "127.0.0.1" ) ); //$NON-NLS-1$

  /**
   * Номер порта подключения к серверу публичного доступа. 0: выбрать автоматически).
   */
  IDataDef OP_PAS_SERVER_PORT = create( "PasServerPort", INTEGER, //$NON-NLS-1$
      TSID_NAME, STR_N_PAS_SERVER_PORT, //
      TSID_DESCRIPTION, STR_D_PAS_SERVER_PORT, //
      TSID_IS_NULL_ALLOWED, AV_TRUE, //
      TSID_DEFAULT_VALUE, avInt( 15751 ) );

  /**
   * Размер очереди входящих запросов на ПСД (по умолчанию 16).
   */
  IDataDef OP_PAS_IN_CONN_QUEUE_SIZE = create( "PasInConnectionQueueSize", INTEGER, //$NON-NLS-1$
      TSID_NAME, STR_N_PAS_IN_CONN_QUEUE_SIZE, //
      TSID_DESCRIPTION, STR_D_PAS_IN_CONN_QUEUE_SIZE, //
      TSID_IS_NULL_ALLOWED, AV_TRUE, //
      TSID_DEFAULT_VALUE, avInt( 16 ) );

}

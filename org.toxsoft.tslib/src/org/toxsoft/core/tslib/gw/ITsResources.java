package org.toxsoft.core.tslib.gw;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  /**
   * {@link GwUtils}.
   */
  String MSG_ERR_GWID_STR_EMPTY  = "Gwid не может быть пустой строкой";
  String MSG_ERR_INV_GWID_FORMAT = "Формат Gwid примерно такой: com.example.classId[obj.strid]$cmd(CmdId)$arg(ArgId)";
  String MSG_ERR_INV_GWID_AV     = "Строка Gwid должна быть атомарным значением типа STRING";

}

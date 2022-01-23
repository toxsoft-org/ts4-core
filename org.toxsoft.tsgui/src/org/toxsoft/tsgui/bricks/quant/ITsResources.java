package org.toxsoft.tsgui.bricks.quant;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  String FMT_WARN_QUANT_DUP_INIT_APP = "Quant       %s: повторный вызов initApp() игнорирован";
  String FMT_INFO_QUANT_INIT_APP     = "Quant       %s: initApp()";
  String FMT_WARN_QUANT_DUP_INIT_WIN = "Quant       %s: повторный вызов initWin() игнорирован";
  String FMT_INFO_QUANT_INIT_WIN     = "Quant       %s: initWin()";
  String FMT_WARN_CLOSE_UNOPENED     = "Quant       %s: close() неинициализированного кванта игнорирован";
  String FMT_INFO_QUANT_CLOSE        = "Quant       %s: close()";
  String FMT_ERR_APP_INIT_QUANT      = "Quant       %s: При инициализации (на уровне приложения) возникла ошибка";
  String FMT_ERR_WIN_INIT_QUANT      = "Quant       %s: При инициализации (на уровне окна) возникла ошибка";
  String FMT_ERR_CLOSING_QUANT       = "Quant       %s: При завершении возникла ошибка";

}

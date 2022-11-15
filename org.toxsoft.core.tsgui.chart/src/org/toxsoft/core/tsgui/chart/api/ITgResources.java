package org.toxsoft.core.tsgui.chart.api;

/**
 * Локализуемые ресурсы.
 *
 * @author vs
 */
@SuppressWarnings( "nls" )
interface ITgResources {

  /**
   * {@link EGraphicRenderingKind}
   */
  String E_D_GRK_LINE   = Messages.ITgResources_E_D_GRK_LINE;
  String E_N_GRK_LINE   = Messages.ITgResources_E_N_GRK_LINE;
  String E_D_GRK_LADDER = Messages.ITgResources_E_D_GRK_LADDER;
  String E_N_GRK_LADDER = Messages.ITgResources_E_N_GRK_LADDER;

  /**
   * {@link EDisplayFormat}
   */
  String STR_N_AS_INTEGER  = "как целое";
  String STR_D_AS_INTEGER  = "отображение без дробной части";
  String STR_N_ONE_DIGIT   = "с одним знаком";
  String STR_D_ONE_DIGIT   = "отображение с одним знаком";
  String STR_N_TWO_DIGIT   = "с двумя знаками";
  String STR_D_TWO_DIGIT   = "отображение с двумя знаками";
  String STR_N_THREE_DIGIT = "с тремя знаками";
  String STR_D_THREE_DIGIT = "отображение с тремя знаками";
  String STR_N_AS_FLOAT    = "с плавающей точностью";
  String STR_D_AS_FLOAT    = "отображение с плавающей точностью";

}

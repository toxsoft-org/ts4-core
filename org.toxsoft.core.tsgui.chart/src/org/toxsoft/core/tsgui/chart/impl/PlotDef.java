package org.toxsoft.core.tsgui.chart.impl;

import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;

import ru.toxsoft.tsgui.chart.api.*;

/**
 * Описание графика с указанием шкалы, холста, набора данных и параметров отрисовки.
 * <p>
 *
 * @author vs
 */
public class PlotDef
    // extends NameableDef
    extends Stridable
    implements IPlotDef {

  private final String    yAxisId;
  private final String    dataSetId;
  private final String    canvasId;
  private final IG2Params rendererParams;

  /**
   * Конструктор.
   *
   * @param aNameable IStridable - идентифицирующая и описательная информация
   * @param aYAxisId String - ИД Y шкалы
   * @param aDataSetId String - ИД набора данных
   * @param aCanvasId String - ИД холста
   * @param aRendererParams IG2Params - параметры отрисовки
   */
  public PlotDef( IStridable aNameable, String aYAxisId, String aDataSetId, String aCanvasId,
      IG2Params aRendererParams ) {
    super( aNameable );
    yAxisId = aYAxisId;
    dataSetId = aDataSetId;
    canvasId = aCanvasId;
    rendererParams = aRendererParams;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IPlotDef
  //

  @Override
  public String yAxisId() {
    return yAxisId;
  }

  @Override
  public String dataSetid() {
    return dataSetId;
  }

  @Override
  public String canvasId() {
    return canvasId;
  }

  @Override
  public IG2Params rendererParams() {
    return rendererParams;
  }

}

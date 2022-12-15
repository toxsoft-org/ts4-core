package org.toxsoft.core.jasperreports.gui.main;

import java.util.*;

/**
 * События действий просмоторщика печатной формы
 *
 * @author Max
 */
public class ReportViewerActionEvent
    extends EventObject {

  private static final long serialVersionUID = 3263044756402830729L;

  private String actionId;

  /**
   * Конструктор по источнику и идентификатору дейсвтия
   *
   * @param aSource TsReportViewer - источник события,
   * @param aActionId String - идентификатор дейсвтвия
   */
  public ReportViewerActionEvent( TsReportViewer aSource, String aActionId ) {
    super( aSource );
    actionId = aActionId;
  }

  /**
   * Возвращает идентификатор дейсвтвия.
   *
   * @return String - идентификатор дейсвтвия
   */
  public String getActionId() {
    return actionId;
  }

}

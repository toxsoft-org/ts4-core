package org.toxsoft.core.jasperreports.gui.main;

/**
 * Слушатель событий действий просмоторщика печатной формы
 *
 * @author Max
 */
public interface IReportViewerActionListener {

  /**
   * Вызывается, когда действие выполнено.
   *
   * @param aEvent ReportViewerActionEvent - событие.
   */
  void actionDone( ReportViewerActionEvent aEvent );

  /**
   * Вызывается, когда действие отменено.
   *
   * @param aEvent ReportViewerActionEvent - событие.
   */
  void actionCanceled( ReportViewerActionEvent aEvent );

  /**
   * Вызывается, когда действие вызвано.
   *
   * @param aEvent ReportViewerActionEvent - событие.
   */
  void actionCalled( ReportViewerActionEvent aEvent );

  /**
   * Вызывается, когда во время выполнения действия произошла ошибка.
   *
   * @param aEvent ReportViewerActionEvent - событие.
   */
  void actionError( ReportViewerActionEvent aEvent );

}

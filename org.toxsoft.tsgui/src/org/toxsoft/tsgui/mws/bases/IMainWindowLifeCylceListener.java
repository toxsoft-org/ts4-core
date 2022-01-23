package org.toxsoft.tsgui.mws.bases;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.IWindowCloseHandler;
import org.eclipse.swt.widgets.Shell;

/**
 * Слушатель событий главного окна приложения.
 *
 * @author hazard157
 */
public interface IMainWindowLifeCylceListener {

  /**
   * Вызывается при инициализации первого вью, перед его показом.
   * <p>
   * То есть, вызывается когда окно и {@link Shell} уже созданы, но еще не показаны.
   *
   * @param aWinContext {@link IEclipseContext} - контекст уровня главного окна
   * @param aWindow {@link MWindow} - окно
   */
  void beforeMainWindowOpen( IEclipseContext aWinContext, MWindow aWindow );

  /**
   * Реализация может перехватить и запретить попытку закрытия окна.
   * <p>
   * Вызывается при попытке закрыть окно (командой завершения программы, щелчком на кнопке окна и т.п.). В этом методе,
   * например, можно показать диалог с предложением сохранить редактируемый файл, и если пользовтель пожелает, отменить
   * закрытие окна и продолжить редаетирование. Для этого достаточно врернуть <code>false</code>.
   * <p>
   * Если хотите только отреагирвоать на закрытие окна (например, сохранить настройки), то такой код следует расположить
   * в теле метода {@link #beforeMainWindowClose(IEclipseContext, MWindow)}, который будет вызван агарантировано. А этот
   * мотеод не обязательно бедет вызван, ведь если есть дав перехватчика, и епрвый запретит закрытие окна, второй уже не
   * будет вызван.
   * <p>
   * Внимание: использование этого перехватчика приводит к тому, что нельзя использовать принятый в e4 обработчик
   * закрытия окна {@link IWindowCloseHandler}, иначе перестанет работать этот перехватчик.
   *
   * @param aWinContext {@link IEclipseContext} - контекст уровня главного окна
   * @param aWindow {@link MWindow} - окно
   * @return boolean - признак разрешения закрытия окна<br>
   *         <b>true</b> - окно будет закрыто;<br>
   *         <b>false</b> - окно останется открытым.
   */
  boolean canCloseMainWindow( IEclipseContext aWinContext, MWindow aWindow );

  /**
   * Вызывается перед закрытием главного окна, после того, как решение о завершении приложения уже принято.
   *
   * @param aWinContext {@link IEclipseContext} - контекст уровня главного окна
   * @param aWindow {@link MWindow} - окно
   */
  void beforeMainWindowClose( IEclipseContext aWinContext, MWindow aWindow );

}

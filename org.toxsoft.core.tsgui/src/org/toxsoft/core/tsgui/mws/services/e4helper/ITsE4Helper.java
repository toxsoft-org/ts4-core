package org.toxsoft.core.tsgui.mws.services.e4helper;

import org.eclipse.e4.core.di.annotations.*;
import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Служба, набор методов для облегчения выполнения типичных задач GUI в приложении на базе E4.
 * <p>
 * Важно: сслыка на этот объект должен быть создан в контексте окна! То есть, не в адоне, который выполяется до создания
 * главного окна. Кроме того, если у программы несколько окон, в каждом должен быть свой экземпляр этой службы.
 *
 * @author hazard157
 */
public interface ITsE4Helper {

  /**
   * Для всех обработчков вызывает методы, аннотированные как {@link CanExecute}.
   */
  void updateHandlersCanExecuteState();

  /**
   * Переключается на выбранную перспективу и опционально активирует вью.
   *
   * @param aPerspectiveId String - идентификатор перспективы
   * @param aActivatePartId String - идентификатор вью, может быть null
   * @return {@link MPart} - активированное вью или null
   * @throws TsNullArgumentRtException aPerspectiveId = null
   */
  MPart switchToPerspective( String aPerspectiveId, String aActivatePartId );

  /**
   * Возвращает идентификатор активной перспективы.
   *
   * @return String - идентификатор активной перспективы или <code>null</code> если нет активной перспективы
   */
  String currentPerspId();

  /**
   * Возвращает идентификатор активного вью.
   *
   * @return String - идентификатор активного вью или <code>null</code> если нет активного вью
   */
  String currentPartId();

  /**
   * Завершает работу приложения.
   */
  void quitApplication();

  /**
   * Runs E4 command without parameters.
   *
   * @param aCmdId String - E4 command ID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void execCmd( String aCmdId );

  /**
   * Runs E4 command with parameters.
   *
   * @param aCmdId String - E4 command ID
   * @param aArgValues {@link IStringMap}&lt;String&gt; - copmmand params as map "param ID" - "value"
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException aArgValues contains unknown parameter
   */
  void execCmd( String aCmdId, IStringMap<String> aArgValues );

}

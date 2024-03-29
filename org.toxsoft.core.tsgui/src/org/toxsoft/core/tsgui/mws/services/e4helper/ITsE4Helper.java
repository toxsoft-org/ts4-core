package org.toxsoft.core.tsgui.mws.services.e4helper;

import org.eclipse.e4.core.di.annotations.*;
import org.eclipse.e4.ui.model.application.ui.*;
import org.eclipse.e4.ui.model.application.ui.advanced.*;
import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.eclipse.e4.ui.workbench.modeling.*;
import org.toxsoft.core.tsgui.mws.*;
import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Служба, набор методов для облегчения выполнения типичных задач GUI в приложении на базе E4.
 * <p>
 * Важно: сслыка на этот объект должен быть создан в контексте окна! То есть, не в адоне, который выполяется до создания
 * главного окна. Кроме того, если у программы несколько окон, в каждом должен быть свой экземпляр этой службы.
 * <p>
 * <b>WARNING</b>: FIXME as of 17/06/2022 eventer {@link #perspectiveEventer()} fire events only when perspectives are
 * changed through {@link #switchToPerspective(String, String)} method (including switching with MWS command
 * {@link IMwsCoreConstants#MWSID_CMD_SWITCH_PERSP}). Switching perspectives via E4 builtin means <b>does not fires</b>
 * {@link ITsE4PerspectiveSwitchListener#onPerspectiveChanged(ITsE4Helper, String)} events!
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
   * Finds the perspective by ID.
   *
   * @param aPerspectiveId String - the perspective ID
   * @return {@link MPerspective} - found perspective or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  MPerspective findPerspective( String aPerspectiveId );

  /**
   * Finds the part by ID.
   *
   * @param aPartId String - the part ID
   * @return {@link MPart} - found part or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  MPart findPart( String aPartId );

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

  /**
   * Finds specified element in e4 model of application.
   *
   * @param <T> - the type of element being searched for
   * @param aRoot {@link MElementContainer} - search root in e4 model of application
   * @param aId String - the ID of element being searched for
   * @param aClass {@link Class}&lt;T&gt; - the type of element being searched for
   * @param aFlags int - search flags {@link EModelService}<code>.XXX</code>
   * @return &lt;T&gt; - found element or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  <T> T findElement( MElementContainer<?> aRoot, String aId, Class<T> aClass, int aFlags );

  /**
   * Perspective change eventer.
   *
   * @return {@link ITsE4Helper}&lt;{@link ITsE4PerspectiveSwitchListener}&gt; - the eventer
   */
  ITsEventer<ITsE4PerspectiveSwitchListener> perspectiveEventer();

}

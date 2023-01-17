package org.toxsoft.core.p2;

import java.net.URL;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.BundleContext;
import org.toxsoft.core.tslib.coll.primtypes.IStringList;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Установщик обновлений клиентов через механизм eclipse.equinox.p2
 *
 * @author mvk
 */
public interface IS5P2Installer {

  /**
   * Запуск задачи проверки обновлений программы и их установки
   *
   * @param aBundleContext {@link BundleContext} контекст плагина
   * @param aDisplay {@link Display} объект синхронизации с GUI
   * @param aShell {@link Shell} родительское окно
   * @param aRepositories {@link IStringList} список ({@link URL}) репозиториев
   * @param aSilence boolean <b>true</b> безшумный режим работы. <b>false</b> выдавать сообщения об проводимом
   *          обновлении
   * @throws TsNullArgumentRtException aBundleContext или aDisplay или aRepositories = null
   */
  void execute( final BundleContext aBundleContext, final Display aDisplay, final Shell aShell,
      final IStringList aRepositories, boolean aSilence );

}

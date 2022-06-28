package org.toxsoft.core.tsgui.ved.glib.comps;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.ved.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Панель инструментов редактора.
 * <p>
 *
 * @author vs
 */
public class VedToolsPanel
    extends TsPanel
    implements IVedContextable {

  /**
   * Конструктор.<br>
   * Запоминает ссылку на контекст. Не копирует его.
   *
   * @param aParent Composite - родительская компонента
   * @param aContext ITsGuiContext - контекст панели
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   */
  public VedToolsPanel( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
  }

}

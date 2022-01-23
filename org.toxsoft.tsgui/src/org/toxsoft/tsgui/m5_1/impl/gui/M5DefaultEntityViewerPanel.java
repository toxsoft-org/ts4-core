package org.toxsoft.tsgui.m5_1.impl.gui;

import static org.toxsoft.tsgui.m5_1.IM5Constants.*;

import org.toxsoft.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.tsgui.m5_1.*;
import org.toxsoft.tsgui.m5_1.api.IM5FieldDef;
import org.toxsoft.tsgui.m5_1.api.IM5Model;
import org.toxsoft.tsgui.m5_1.gui.IM5EntityPanel;
import org.toxsoft.tsgui.panels.vecboard.IVecLadderLayout;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Панель {@link IM5EntityPanel} по умолчанию для просмотра сущности.
 * <p>
 * Панель создает редакторы (в режиме просмотра) всех полей без признака {@link IM5Constants#M5FF_HIDDEN} в раскладке
 * {@link IVecLadderLayout}.
 *
 * @author goga
 * @param <T> - класс моделированых сущностей, отображаемых в панели
 */
public class M5DefaultEntityViewerPanel<T>
    extends M5EntityPanelWithValeds<T> {

  /**
   * Конструктор.
   *
   * @param aContext {@link ITsGuiContext} - контекст панели
   * @param aModel {@link IM5Model} - модель
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public M5DefaultEntityViewerPanel( ITsGuiContext aContext, IM5Model<T> aModel ) {
    super( aContext, aModel, true );
  }

  @Override
  protected void doInitEditors() {
    // создает редакторы всех полей без признака M5FF_HIDDEN
    for( IM5FieldDef<T, ?> fDef : model().fieldDefs() ) {
      if( (fDef.hints() & M5FF_HIDDEN) == 0 ) {
        addField( fDef.id() );
      }
    }
  }

}

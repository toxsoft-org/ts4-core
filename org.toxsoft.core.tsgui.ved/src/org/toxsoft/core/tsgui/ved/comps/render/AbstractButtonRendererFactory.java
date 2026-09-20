package org.toxsoft.core.tsgui.ved.comps.render;

import static org.toxsoft.core.tsgui.ved.comps.render.IRendererConstants.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.ved.incub.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;

/**
 * Класс, от которго должны наследоваться фабрики отрисовщиков кнопок.
 * <p>
 *
 * @author vs
 */
public abstract class AbstractButtonRendererFactory
    extends VedAbstractRendererFactory {

  /**
   * Renderer kind id
   */
  protected static final String KIND_ID = "buttonRenderer"; //$NON-NLS-1$

  protected AbstractButtonRendererFactory( String aId, Object... aIdsAndValues ) {
    super( aId, aIdsAndValues );
  }

  @Override
  protected final void addTinTypeInfoes( IStridablesListEdit<ITinFieldInfo> aFields ) {
    addSpecificTinTypeInfoes( aFields );

    // ----------------------------------------------------------------------------
    // Скрытые поля, значения которых устанавливаются извне
    //
    aFields.add( TtiUtils.createHidden( TFI_BUTTON_HOVERED ) );
    aFields.add( TtiUtils.createHidden( TFI_BUTTON_STATE ) );
  }

  // ------------------------------------------------------------------------------------
  // To Override
  //

  /**
   * Наследник должен добавить описания специфичных полей.
   *
   * @param fields IStridablesListEdit&lt;ITinFieldInfo> - редактируемый список описаний полей
   */
  protected abstract void addSpecificTinTypeInfoes( IStridablesListEdit<ITinFieldInfo> fields );

}

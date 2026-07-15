package org.toxsoft.core.tsgui.ved.comps.render;

import static org.toxsoft.core.tsgui.ved.comps.render.IRendererConstants.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.ved.incub.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;

/**
 * Класс, от которго должны наследоваться отрисовщики круговых элементов.
 * <p>
 *
 * @author vs
 */
public abstract class AbstractCircularRendererFactory
    extends VedAbstractRendererFactory {

  protected AbstractCircularRendererFactory( String aId, Object... aIdsAndValues ) {
    super( aId, aIdsAndValues );
  }

  @Override
  protected final void addTinTypeInfoes( IStridablesListEdit<ITinFieldInfo> aFields ) {
    addSpecificTinTypeInfoes( aFields );

    // ----------------------------------------------------------------------------
    // Скрытые поля, значения которых устанавливаются извне
    //
    aFields.add( TtiUtils.createHidden( TFI_OWNER_RADIUS ) );
    aFields.add( TtiUtils.createHidden( TFI_OWNER_ANCHOR_X ) );
    aFields.add( TtiUtils.createHidden( TFI_OWNER_ANCHOR_Y ) );
    aFields.add( TtiUtils.createHidden( TFI_OWNER_START_ANGLE ) );
    aFields.add( TtiUtils.createHidden( TFI_OWNER_DELTA_ANGLE ) );
    aFields.add( TtiUtils.createHidden( TFI_ARROW_ANGLE ) );
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

package org.toxsoft.core.tsgui.valed.controls.graphics;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tslib.bricks.events.change.*;

/**
 * Базовый класс для создания панелей задания параметров градиентов.
 *
 * @author vs
 */
public class AbstractPanelGradientSelector
    extends TsPanel
    implements IGenericChangeEventCapable {

  private final GenericChangeEventer changeEventer;

  AbstractPanelGradientSelector( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    changeEventer = new GenericChangeEventer( this );
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public GenericChangeEventer genericChangeEventer() {
    return changeEventer;
  }

}

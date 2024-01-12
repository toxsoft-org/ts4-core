package org.toxsoft.core.tsgui.ved.comps;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;

public interface IViselButton
    extends IVedVisel {

  EButtonViselState buttonState();

  ITsGuiContext tsContext();
}

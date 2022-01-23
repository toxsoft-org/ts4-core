package org.toxsoft.tsgui.m5_2.gui.panels;

import org.toxsoft.tsgui.m5_2.*;

public interface IM5GeneralCollEditPanel<T>
    extends IM5CollPanelBase<T> {

  CollEditController<T> getEditHelper();

  void setEditHelper( CollEditController<T> aEditHelper );

}

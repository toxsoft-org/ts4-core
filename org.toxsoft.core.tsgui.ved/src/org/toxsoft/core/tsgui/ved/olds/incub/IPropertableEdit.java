package org.toxsoft.core.tsgui.ved.olds.incub;

import org.toxsoft.core.tslib.av.opset.*;

/**
 * {@link IPropertable} extention with editing ability.
 *
 * @author hazard157
 */
public interface IPropertableEdit
    extends IPropertable {

  /**
   * Exposes propertieis for editing.
   *
   * @return {@link INotifierOptionSetEdit} - editable properties
   */
  @Override
  INotifierOptionSetEdit props();

}

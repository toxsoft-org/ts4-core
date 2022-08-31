package org.toxsoft.core.tslib.av.utils;

import org.toxsoft.core.tslib.av.opset.*;

/**
 * A batch editable extension of {@link IParameterized} interface.
 *
 * @author hazard157
 */
public interface IParameterizedBatchEdit
    extends IParameterized {

  /**
   * Returns batch editor of {@link #params()}.
   *
   * @return {@link IOpsBatchEdit} - parameters batch editor
   */
  IOpsBatchEdit paramsBatchEditor();

}

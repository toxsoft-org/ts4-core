package org.toxsoft.tslib.av.utils;

import org.toxsoft.tslib.av.opset.IOptionSet;
import org.toxsoft.tslib.av.opset.IOptionSetEdit;

/**
 * An editable extension of {@link IParameterized} interface.
 *
 * @author hazard157
 */
public interface IParameterizedEdit
    extends IParameterized {

  /**
   * Return editable parameters.
   *
   * @return {@link IOptionSet} - editable parameters
   */
  @Override
  IOptionSetEdit params();

}

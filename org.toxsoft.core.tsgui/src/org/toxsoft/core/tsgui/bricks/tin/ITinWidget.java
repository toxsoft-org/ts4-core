package org.toxsoft.core.tsgui.bricks.tin;

import org.toxsoft.core.tsgui.panels.generic.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;

/**
 * Inspector widget.
 *
 * @author hazard157
 */
public interface ITinWidget
    extends IGenericContentPanel {

  /**
   * Returns the information about objects to be edited.
   *
   * @return {@link ITinTypeInfo} - entity info or <code>null</code> if inspector is note ready for editing
   */
  ITinTypeInfo getTypeInfo();

  /**
   * Prepares inspector to edit values of the specified object types.
   *
   * @param aTypeInfo {@link ITinTypeInfo} - the object type information or <code>null</code>
   */
  void setEntityInfo( ITinTypeInfo aTypeInfo );

  /**
   * Sets the inspected value.
   * <p>
   * Setting <code>null</code> has the same effect as setting {@link ITinValue#NULL}. If type info is not set (that is
   * if {@link #getTypeInfo()} = <code>null</code>) then call of this method is ignored.
   *
   * @param aValue {@link ITinValue} - the value, may be <code>null</code>
   */
  void setValue( ITinValue aValue );

  /**
   * Checks if inspector widget contains readable value.
   * <p>
   * If method returns {@link EValidationResultType#ERROR} then calling {@link #getValue()} will throw an exception.
   *
   * @return {@link ValidationResult} - the check result
   */
  ValidationResult canGetValue();

  /**
   * Returns the inspected value.
   *
   * @return {@link ITinValue} - the value
   * @throws TsValidationFailedRtException failed {@link #canGetValue()}
   */
  ITinValue getValue();

}

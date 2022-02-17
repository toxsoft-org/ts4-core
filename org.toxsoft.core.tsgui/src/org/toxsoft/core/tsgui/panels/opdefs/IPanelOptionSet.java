package org.toxsoft.core.tsgui.panels.opdefs;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Panel to view {@link IOptionSet} values.
 * <p>
 * This is base read-only interface for editable panel {@link IPanelOptionSetEdit}.
 * <p>
 * Panel displays only options known listed in {@link #listOpionDefs()}.
 *
 * @author hazard157
 */
public interface IPanelOptionSet
    extends ILazyControl<Control> {

  /**
   * Returns the definitions of the known options to be displayed (and edited).
   * <p>
   * Edit controls will be displayed in the order of the returned list.
   *
   * @return {@link IStridablesList}&lt;{@link IDataDef}&gt; - list of option definitions
   */
  IStridablesList<IDataDef> listOpionDefs();

  /**
   * Sets the definitions of the known options.
   * <p>
   * Changes the content of the editors in panel.
   *
   * @param aDefs {@link IList}&lt;{@link IDataDef}&gt; - list of option definitions
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setOptionDefs( IList<IDataDef> aDefs );

  /**
   * Returns the values of the options in the panel.
   *
   * @return {@link IOptionSet} - the options values
   */
  IOptionSet getValues();

  /**
   * Sets the values of the options in the panel.
   *
   * @param aValues {@link IOptionSet} - the values of the options
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setValues( IOptionSet aValues );

}

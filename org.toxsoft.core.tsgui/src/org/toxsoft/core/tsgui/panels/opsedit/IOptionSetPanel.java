package org.toxsoft.core.tsgui.panels.opsedit;

import org.toxsoft.core.tsgui.panels.generic.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Option set view/edit panel.
 * <p>
 * Only the options listed in {@link #listOptionDefs()} are displayed. Panel retains unlisted option unchanged. Panel
 * can not change list of options, neither remove nor add an option, only values may be edited.
 *
 * @author hazard157
 */
public interface IOptionSetPanel
    extends IGenericEntityEditPanel<IOptionSet> {

  /**
   * {@inheritDoc}
   * <p>
   * Fires the {@link #genericChangeEventer()} event.
   */
  @Override
  void setEntity( IOptionSet aEntity );

  /**
   * Returns the definitions of the known options to be displayed (and edited).
   * <p>
   * Edit controls will be displayed in the order of the returned list.
   *
   * @return {@link IStridablesList}&lt;{@link IDataDef}&gt; - list of option definitions
   */
  IStridablesList<IDataDef> listOptionDefs();

  /**
   * Sets the definitions of the known options.
   * <p>
   * Changes the content of the editors in panel.
   * <p>
   * Fires the {@link #genericChangeEventer()} event.
   *
   * @param aDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - list of option definitions
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setOptionDefs( IStridablesList<IDataDef> aDefs );

  /**
   * Determines if panel content editing is allowed right now.
   * <p>
   * For viewers {@link #isViewer()} = <code>true</code> always returns <code>false</code>.
   *
   * @return boolean - edit mode flag
   */
  boolean isEditable();

  /**
   * Toggles panel content edit mode.
   * <p>
   * For viewers {@link #isViewer()} = <code>true</code> this method does nothing.
   *
   * @param aEditable boolean - edit mode flag
   */
  void setEditable( boolean aEditable );

  /**
   * Returns option values change eventer.
   * <p>
   * Option value change events are fired each time user changes values in option VALEDs. Event is fired only if VALED
   * contains valid value, that is {@link IValedControl#canGetValue()} return not an error.
   * <p>
   * Note: entity and/or options definion changes does not causes this event to be fired.
   *
   * @return {@link ITsEventer}&lt;{@link IOptionValueChangeListener}&gt; - the eventer
   */
  ITsEventer<IOptionValueChangeListener> optionChangeEventer();

}

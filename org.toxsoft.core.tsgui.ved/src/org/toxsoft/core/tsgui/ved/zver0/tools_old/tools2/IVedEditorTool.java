package org.toxsoft.core.tsgui.ved.zver0.tools_old.tools2;

import org.toxsoft.core.tsgui.ved.zver1.core.impl.*;
import org.toxsoft.core.tsgui.ved.zver1.core.library.*;
import org.toxsoft.core.tsgui.ved.zver1.utils.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.strid.*;

/**
 * Editor tool is a means of special visual editing of a component.
 * <p>
 * The only allowed implementation of this inteface is {@link VedAbstractEditorTool} class.
 *
 * @author hazard157
 */
public interface IVedEditorTool
    extends IStridableParameterized, IIconIdable, IVedContextable {

  /**
   * Returns the provider that created this tool.
   *
   * @return {@link IVedEditorToolProvider} - creator
   */
  IVedEditorToolProvider provider();

}

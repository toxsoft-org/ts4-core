package org.toxsoft.core.tsgui.ved.extra.tools;

import org.toxsoft.core.tsgui.ved.core.impl.*;
import org.toxsoft.core.tsgui.ved.core.library.*;
import org.toxsoft.core.tsgui.ved.utils.*;
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

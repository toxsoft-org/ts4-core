package org.toxsoft.core.tsgui.ved.glib.library;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tsgui.ved.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Shows library manager {@link IVedEnvironment#libraryManager()} contents.
 *
 * @author hazard157
 */
public class VedLibraryManagerViewer
    extends TsPanel
    implements IVedContextable {

  /**
   * Constructor.
   * <p>
   * Constructos stores reference to the context, does not creates copy.
   *
   * @param aParent {@link Composite} - parent component
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedLibraryManagerViewer( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
  }

}

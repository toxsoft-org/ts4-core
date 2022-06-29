package org.toxsoft.core.tsgui.ved.glib.inspector;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.panels.opsedit.*;
import org.toxsoft.core.tsgui.panels.opsedit.impl.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tsgui.ved.utils.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Displayes properties of the currently selected component and allows to change them.
 * <p>
 * TODO how to get selected component?
 *
 * @author hazard157
 */
public class ComponentInpectorPanel
    extends TsPanel
    implements IVedContextable {

  private final IGenericChangeListener selectedComponentChangeListener = new IGenericChangeListener() {

    @Override
    public void onGenericChangeEvent( Object aSource ) {
      IVedComponent c = (IVedComponent)aSource;
      panel.setEntity( c.props() );
    }
  };

  private final IOptionSetPanel panel;

  private IVedComponent currentComponent = null;

  /**
   * Constructor.
   * <p>
   * Constructos stores reference to the context, does not creates copy.
   *
   * @param aParent {@link Composite} - parent component
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ComponentInpectorPanel( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    this.setLayout( new BorderLayout() );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    panel = new OptionSetPanel( ctx, false );
    panel.createControl( aParent );
    panel.getControl().setLayoutData( BorderLayout.CENTER );
    // TODO set selected component change listener
  }

}

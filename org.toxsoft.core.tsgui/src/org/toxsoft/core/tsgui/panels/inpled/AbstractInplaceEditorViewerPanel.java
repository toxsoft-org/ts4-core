package org.toxsoft.core.tsgui.panels.inpled;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IInplaceEditorPanel} viewer implementation, that is {@link #isViewer()} always returns <code>true</code>.
 *
 * @author hazard157
 */
public abstract class AbstractInplaceEditorViewerPanel
    extends AbstractLazyPanel<Control>
    implements IInplaceEditorPanel {

  /**
   * Constructor.
   * <p>
   * Constructor stores reference to the context, does not creates copy.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public AbstractInplaceEditorViewerPanel( ITsGuiContext aContext ) {
    super( aContext );
  }

  // ------------------------------------------------------------------------------------
  // AbstractLazyPanel
  //

  @Override
  protected abstract Control doCreateControl( Composite aParent );

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return NoneGenericChangeEventer.INSTANCE;
  }

  // ------------------------------------------------------------------------------------
  // IInplaceEditorPanel
  //

  @Override
  final public boolean isViewer() {
    return true;
  }

  @Override
  final public boolean isEditing() {
    return false;
  }

  @Override
  public void startEditing() {
    // nop
  }

  @Override
  final public boolean isChanged() {
    return false;
  }

  @Override
  public void applyAndFinishEditing() {
    // nop
  }

  @Override
  public void cancelAndFinishEditing() {
    // nop
  }

  @Override
  public void refresh() {
    // nop
  }

}

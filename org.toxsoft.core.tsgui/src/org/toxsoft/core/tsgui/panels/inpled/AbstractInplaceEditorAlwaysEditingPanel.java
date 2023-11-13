package org.toxsoft.core.tsgui.panels.inpled;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IInplaceEditorPanel} always editing implementation, {@link #isEditing()} always returns <code>true</code>.
 *
 * @author hazard157
 */
public abstract class AbstractInplaceEditorAlwaysEditingPanel
    extends AbstractLazyPanel<Control>
    implements IInplaceEditorPanel {

  private final GenericChangeEventer genericChangeEventer;

  /**
   * Constructor.
   * <p>
   * Constructor stores reference to the context, does not creates copy.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public AbstractInplaceEditorAlwaysEditingPanel( ITsGuiContext aContext ) {
    super( aContext );
    genericChangeEventer = new GenericChangeEventer( this );
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
  public GenericChangeEventer genericChangeEventer() {
    return genericChangeEventer;
  }

  // ------------------------------------------------------------------------------------
  // IInplaceEditorPanel
  //

  @Override
  final public boolean isViewer() {
    return false;
  }

  @Override
  final public boolean isEditing() {
    return true;
  }

  @Override
  public void startEditing() {
    // nop
  }

  @Override
  public abstract boolean isChanged();

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

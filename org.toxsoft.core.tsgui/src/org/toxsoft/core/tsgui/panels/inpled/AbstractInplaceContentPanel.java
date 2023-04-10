package org.toxsoft.core.tsgui.panels.inpled;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.generic.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Base class to implement contanten panel in {@link InplaceEditorPanel}.
 * <p>
 * TODO how to use? wrappers around genreic, M5 and other panels
 *
 * @author hazard157
 */
public abstract class AbstractInplaceContentPanel
    extends AbstractLazyPanel<Control>
    implements IGenericContentPanel, ITsActionHandler {

  private final GenericChangeEventer eventer;

  /**
   * Constructor.
   * <p>
   * Constructor stores reference to the context, does not creates copy.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public AbstractInplaceContentPanel( ITsGuiContext aContext ) {
    super( aContext );
    eventer = new GenericChangeEventer( this );
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  final public GenericChangeEventer genericChangeEventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // AbstractLazyPanel
  //

  @Override
  protected abstract Control doCreateControl( Composite aParent );

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Changes edited content according to current values in the widgets.
   *
   * @throws TsValidationFailedRtException {@link #validate()} failed
   */
  public final void applyChanges() {
    TsValidationFailedRtException.checkError( validate() );
    doApplyChanges();
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * The subclass may declare decalre additional actions.
   * <p>
   * Additional actions are the actions with IDs not listed in {@link InplaceEditorPanel#DEFAULT_EDIT_ACTION_DEFS}.
   * Additional buttons will be created in button bar and {@link #handleAction(String)} will be called on such button
   * press.
   * <p>
   * This method may contain actions with IDs of default actions to redefine texts/icon on the default buttons. Anyway
   * actions with default IDs are handled by {@link InplaceEditorPanel}.
   * <p>
   * In base class returns {@link IStridablesList#EMPTY}. There is no need to call superclass method when overriding.
   *
   * @return {@link IStridablesList}&lt;{@link ITsActionDef}&gt; - additional actions
   */
  public IStridablesList<ITsActionDef> listSupportedActions() {
    return IStridablesList.EMPTY;
  }

  /**
   * Determines if additional action may be executed right now.
   * <p>
   * This method is not called for default actions.
   * <p>
   * In base class returns <code>true</code>. There is no need to call superclass method when overriding.
   *
   * @param aActionId String - the action ID {@link ITsActionDef#id()}
   * @return <code>true</code> if action may be performed right now, <code>false</code> action cann't be executed
   */
  public boolean isActionEnabled( String aActionId ) {
    return true;
  }

  /**
   * Subclass may handle additional actions listed in {@link #listSupportedActions()}.
   * <p>
   * Default actions are handled by {@link InplaceEditorPanel} and this method is not called. {@inheritDoc}
   */
  @Override
  public void handleAction( String aActionId ) {
    // nop
  }

  /**
   * Checks if corrent changes can be applied.
   * <p>
   * If9 method returns {@link EValidationResultType#ERROR} then "OK" and "Apply" buttons will be disabled.
   * <p>
   * In base class returns {@link ValidationResult#SUCCESS}. There is no need to call superclass method when overriding.
   *
   * @return {@link ValidationResult} - the check result
   */
  public ValidationResult validate() {
    return ValidationResult.SUCCESS;
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  @Override
  public abstract boolean isViewer();

  /**
   * Determines if panel is in editin mode.
   *
   * @return boolean - <code>true</code> in editing mode, <code>false</code> in viewer mode
   */
  public abstract boolean isEditing();

  /**
   * Detrmines if there were anu changes in widget values since last {@link #applyChanges()}.
   * <p>
   * In other words determines if there is any difference between values in GUI widgets and values of underlying content
   * properties.
   *
   * @return boolean - <code>true</code> there is unsaved changes
   */
  public abstract boolean isChanged();

  /**
   * Checks if panel can be set in editing mode.
   *
   * @return {@link ValidationResult} - the check result, on eroor editing can not be started
   */
  public abstract ValidationResult canStartEditing();

  /**
   * Changes the panel edit mode.
   *
   * @param aMode boolean - <code>true</code> in editing mode, <code>false</code> in viewer mode
   */
  public abstract void setEditMode( boolean aMode );

  /**
   * Implementation must change edited content according to current values in the widgets.
   * <p>
   * This method is called only if prior call to {@link #validate()} does not returns error.
   */
  public abstract void doApplyChanges();

  /**
   * Sets the widget values in panel from the editet content properties.
   */
  public abstract void revertChanges();

}

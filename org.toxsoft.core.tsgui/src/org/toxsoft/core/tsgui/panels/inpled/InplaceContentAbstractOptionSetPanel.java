package org.toxsoft.core.tsgui.panels.inpled;

import static org.toxsoft.core.tsgui.panels.inpled.ITsResources.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.panels.opsedit.*;
import org.toxsoft.core.tsgui.panels.opsedit.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link AbstractContentPanel} implementation with {@link IOptionSetPanel}.
 * <p>
 * There are two similar content panels with different behavior:
 * <ul>
 * <li>{@link InplaceContentOptionSetEditPanel} - directly edits the specified {@link IOptionSetEdit} instance;</li>
 * <li>{@link InplaceContentAbstractOptionSetPanel} - edits {@link IOptionSet} but user has to retrieve and apply
 * changes in overridden method;</li>
 * </ul>
 *
 * @author hazard157
 */
public abstract class InplaceContentAbstractOptionSetPanel
    extends AbstractContentPanel {

  private final IOptionSetPanel panel;
  private final IOptionSetEdit  initialValues = new OptionSet();

  private boolean changed = false;

  /**
   * Constructor.
   * <p>
   * Constructor stores reference to the context, does not creates copy.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public InplaceContentAbstractOptionSetPanel( ITsGuiContext aContext ) {
    super( aContext );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    panel = new OptionSetPanel( ctx, false, true );
    panel.genericChangeEventer().addListener( aSource -> {
      changed = true;
      genericChangeEventer().fireChangeEvent();
    } );
    panel.setEditable( false );
  }

  @Override
  protected Control doCreateControl( Composite aParent ) {
    return panel.createControl( aParent );
  }

  @Override
  public boolean isViewer() {
    return panel.isViewer();
  }

  @Override
  public boolean isEditing() {
    return panel.isEditable();
  }

  @Override
  public boolean isChanged() {
    return changed;
  }

  @Override
  public ValidationResult canStartEditing() {
    if( panel.listOptionDefs().isEmpty() ) {
      return ValidationResult.error( MSG_ERR_NO_OPSET_TO_EDIT );
    }
    return ValidationResult.SUCCESS;
  }

  @Override
  public void setEditMode( boolean aMode ) {
    if( aMode != isEditing() ) {
      panel.setEditable( aMode );
      changed = false;
    }
  }

  @Override
  public ValidationResult validate() {
    return panel.canGetEntity();
  }

  @Override
  public void doApplyChanges() {
    doDoApplyChanges( panel.getEntity() );
    changed = false;
  }

  @Override
  public void revertChanges() {
    panel.setEntity( initialValues );
    changed = false;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Sets the content to be edited.
   * <p>
   * Sets panel to view mode, resets {@link #isChanged()} flag.
   * <p>
   * Generates the change event.
   *
   * @param aInitialValues {@link IOptionSetEdit} - initial values to be edited
   * @param aOpDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - the editable options definitions
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void setContentToEdit( IOptionSet aInitialValues, IStridablesList<IDataDef> aOpDefs ) {
    TsNullArgumentRtException.checkNulls( aInitialValues, aOpDefs );
    setEditMode( false );
    panel.setOptionDefs( aOpDefs );
    initialValues.setAll( aInitialValues );
    panel.setEntity( initialValues );
    genericChangeEventer().fireChangeEvent();
    changed = false;
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * Subclass must apply changes from panel widgets to the options set somewhere else.
   *
   * @param aChangedOpset {@link IOptionSet} - currently edited values from the panel widgets
   */
  protected abstract void doDoApplyChanges( IOptionSet aChangedOpset );

}

package org.toxsoft.core.tsgui.panels.inpled;

import static org.toxsoft.core.tsgui.panels.inpled.ITsResources.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.panels.opsedit.*;
import org.toxsoft.core.tsgui.panels.opsedit.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
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
public class InplaceContentOptionSetEditPanel
    extends AbstractContentPanel {

  private final IOptionSetPanel panel;

  /**
   * The edited option set, <code>null</code> means nothing to edit.
   */
  private IOptionSetEdit opsToEdit = null;
  private boolean        changed   = false;

  /**
   * Constructor.
   * <p>
   * Constructor stores reference to the context, does not creates copy.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public InplaceContentOptionSetEditPanel( ITsGuiContext aContext ) {
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
    if( opsToEdit != null ) {
      opsToEdit.setAll( panel.getEntity() );
    }
    changed = false;
  }

  @Override
  public void revertChanges() {
    if( opsToEdit != null ) {
      panel.setEntity( opsToEdit );
    }
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
   * @param aOps {@link IOptionSetEdit} - the edited option set, or <code>null</code> to edit nothing
   * @param aOpDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - the editable options definitions
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void setContentToEdit( IOptionSetEdit aOps, IStridablesList<IDataDef> aOpDefs ) {
    TsNullArgumentRtException.checkNull( aOpDefs );
    setEditMode( false );
    if( aOps != null ) {
      panel.setOptionDefs( aOpDefs );
      opsToEdit = aOps;
      panel.setEntity( opsToEdit );
    }
    else {
      panel.setOptionDefs( IStridablesList.EMPTY );
      panel.setEntity( IOptionSet.NULL );
    }
    genericChangeEventer().fireChangeEvent();
    changed = false;
  }

}

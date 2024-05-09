package org.toxsoft.core.tsgui.dialogs.datarec;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.panels.generic.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link AbstractTsDialogPanel} implementation wrapping over {@link IGenericEntityEditPanel}.
 * <p>
 * {@link IGenericEntityEditPanelCreator} is used to specify the panel.
 *
 * @author hazard157
 * @param <T> - data transfer object type passed to/from dialog
 * @param <E> - client specified optional environment type
 */
public class TsDialogGenericEntityEditPanel<T, E>
    extends AbstractTsDialogPanel<T, E> {

  private final IGenericEntityEditPanel<T> panel;

  /**
   * Constructs panel as {@link TsDialog} content.
   *
   * @param aParent {@link Composite} - the parent composite
   * @param aOwnerDialog {@link TsDialog} - the owner dialog
   * @param aPanelCreator {@link IGenericEntityEditPanelCreator}&lt;T&gt; - the panel creator
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TsDialogGenericEntityEditPanel( Composite aParent, TsDialog<T, E> aOwnerDialog,
      IGenericEntityEditPanelCreator<T> aPanelCreator ) {
    super( aParent, aOwnerDialog );
    TsNullArgumentRtException.checkNull( aPanelCreator );
    panel = aPanelCreator.create( tsContext(), false );
    panel.createControl( aParent );
    this.setLayout( new BorderLayout() );
    panel.getControl().setLayoutData( BorderLayout.CENTER );
    panel.genericChangeEventer().addListener( notificationGenericChangeListener );
  }

  @Override
  protected void doSetDataRecord( T aData ) {
    panel.setEntity( aData );
  }

  @Override
  protected ValidationResult doValidate() {
    return panel.canGetEntity();
  }

  @Override
  protected T doGetDataRecord() {
    return panel.getEntity();
  }

}

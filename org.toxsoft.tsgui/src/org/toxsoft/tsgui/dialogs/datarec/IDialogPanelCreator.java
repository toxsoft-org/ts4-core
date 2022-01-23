package org.toxsoft.tsgui.dialogs.datarec;

import org.eclipse.swt.widgets.Composite;

/**
 * Factory to create {@link AbstractTsDialogPanel}.
 * <p>
 * Used to specify in {@link TsDialog} constructor dialog content panel.
 *
 * @author hazard157
 * @param <T> - data transfet object type passed to/from dialog
 * @param <E> - client specified optional environment type
 */
public interface IDialogPanelCreator<T, E> {

  /**
   * Created dialog content panel.
   *
   * @param aParent {@link Composite} - parent composite
   * @param aOwnerDialog {@link TsDialog} - owner dialog
   * @return {@link AbstractTsDialogPanel} - created content panel instance, must not be <code>null</code>
   */
  AbstractTsDialogPanel<T, E> createDialogPanel( Composite aParent, TsDialog<T, E> aOwnerDialog );

}

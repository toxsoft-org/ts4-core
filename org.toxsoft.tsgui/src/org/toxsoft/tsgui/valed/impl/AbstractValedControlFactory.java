package org.toxsoft.tsgui.valed.impl;

import static org.toxsoft.tsgui.valed.api.IValedControlConstants.*;

import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.toxsoft.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.tsgui.valed.api.*;
import org.toxsoft.tslib.utils.errors.*;

/**
 * Basic implementation of {@link IValedControlFactory}.
 *
 * @author hazard157
 */
public abstract class AbstractValedControlFactory
    implements IValedControlFactory {

  private final String factoryName;

  /**
   * Constructor for subclasses.
   *
   * @param aFactoryName Striung - the factory name
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument is a blank string
   */
  protected AbstractValedControlFactory( String aFactoryName ) {
    TsErrorUtils.checkNonBlank( aFactoryName );
    factoryName = aFactoryName;
  }

  // ------------------------------------------------------------------------------------
  // IValueEditorControlFactory
  //

  @Override
  public String factoryName() {
    return factoryName;
  }

  @Override
  public <V> IValedControl<V> createEditor( ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    if( OPDEF_CREATE_UNEDITABLE.getValue( aContext.params() ).asBool() ) {
      return doCreateViewer( aContext );
    }
    return doCreateEditor( aContext );
  }

  // ------------------------------------------------------------------------------------
  // To be overriden
  //

  /**
   * Subclass must create the editor instance.
   *
   * @param <V> - the edited value type
   * @param aContext {@link ITsGuiContext} - editor context, never is <code>null</code>
   * @return {@link IValedControl} - created editor instance, must not be <code>null</code>
   */
  protected abstract <V> IValedControl<V> doCreateEditor( ITsGuiContext aContext );

  /**
   * Subclass may create the viewer instance.
   * <p>
   * Viewer is the {@link IValedControl}, created only to view the value, without editing capability. In many caeses
   * viewer uses other SWT widget than editor. For example, a {@link Spinner} may be used to edit integer value, while
   * unedtable {@link Text} (that is {@link Text#getEditable()} = <code>false</code>) may be used to view value.
   * <p>
   * This method is called from {@link #createEditor(ITsGuiContext)} when flag
   * {@link IValedControlConstants#OPID_CREATE_UNEDITABLE} is set to <code>true</code>.
   * <p>
   * By deafult simply calls {@link #doCreateEditor(ITsGuiContext)}, when overriding, no need to call the parent method.
   *
   * @param <V> - the edited value type
   * @param aContext {@link ITsGuiContext} - editor context, never is <code>null</code>
   * @return {@link IValedControl} - created viewer instance, must not be <code>null</code>
   */
  protected <V> IValedControl<V> doCreateViewer( ITsGuiContext aContext ) {
    return doCreateEditor( aContext );
  }

}

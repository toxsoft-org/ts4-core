package org.toxsoft.core.tsgui.valed.impl;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.utils.errors.*;

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
   * @param aFactoryName String - the factory name
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
  final public String factoryName() {
    return factoryName;
  }

  @Override
  final public <V> IValedControl<V> createEditor( ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );

    // TODO AbstractValedControlFactory.createEditor()
    if( OPDEF_IS_SINGLE_LINE_UI.getValue( aContext.params() ).asBool() ) {
      return doCreateSingleLine( aContext );
    }

    if( OPDEF_CREATE_UNEDITABLE.getValue( aContext.params() ).asBool() ) {
      return doCreateViewer( aContext );
    }
    return doCreateEditor( aContext );
  }

  // ------------------------------------------------------------------------------------
  // To be overridden
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
   * Viewer is the {@link IValedControl}, created only to view the value, without editing capability. In many cases
   * viewer uses other SWT widget than editor. For example, a {@link Spinner} may be used to edit integer value, while
   * uneditable {@link Text} (that is {@link Text#getEditable()} = <code>false</code>) may be used to view value.
   * <p>
   * This method is called from {@link #createEditor(ITsGuiContext)} when flag
   * {@link IValedControlConstants#OPID_CREATE_UNEDITABLE} is set to <code>true</code>.
   * <p>
   * In the base class calls {@link #doCreateEditor(ITsGuiContext)}, when overriding, no need to call the parent method.
   *
   * @param <V> - the edited value type
   * @param aContext {@link ITsGuiContext} - editor context, never is <code>null</code>
   * @return {@link IValedControl} - created viewer instance, must not be <code>null</code>
   */
  protected <V> IValedControl<V> doCreateViewer( ITsGuiContext aContext ) {
    return doCreateEditor( aContext );
  }

  /**
   * Subclass may override default single-line-height version of CALED.
   * <p>
   * This method is called from {@link #createEditor(ITsGuiContext)} when flag
   * {@link IValedControlConstants#OPID_IS_SINGLE_LINE_UI} is set to <code>true</code>. See the comments of the constant
   * {@link IValedControlConstants#OPID_IS_SINGLE_LINE_UI OPID_IS_SINGLE_LINE_UI} for details.
   * <p>
   * In the base class creates instance of the class {@link ValedControlSingleLineWrapper}, when overriding, no need to
   * call the parent method.
   *
   * @param <V> - the edited value type
   * @param aContext {@link ITsGuiContext} - editor context, never is <code>null</code>
   * @return {@link IValedControl} - created single-line-height instance, must not be <code>null</code>
   * @see IValedControlConstants#OPDEF_IS_SINGLE_LINE_UI
   */
  protected <V> IValedControl<V> doCreateSingleLine( ITsGuiContext aContext ) {
    return new ValedControlSingleLineWrapper<>( aContext, this );
  }

  /**
   * Subclass may determine if this editor is suitable to edit atomic values of the specified type.
   * <p>
   * Caller guarantees that if <code>true</code> returned, this editor will be used for VALED with the specified
   * context. So implementation may change context to prepare for correct creation.
   * <p>
   * Returns <code>false</code> in the base class, there is no need to call superclass method when overriding.
   * <p>
   * The argument <code>aKeeperId</code> is value of the option {@link IAvMetaConstants#TSID_KEEPER_ID} from the context
   * <code>aEditorContext</code>. It is meaningful only for {@link EAtomicType#VALOBJ} types.
   *
   * @param aAtomicType {@link EAtomicType} - the edited value type
   * @param aKeeperId String - the keeper ID or <code>null</code>
   * @param aEditorContext {@link ITsGuiContext} - the editor creation context
   * @return boolean - <code>true</code> if this editor will be used for editing
   */
  protected boolean isSuitableAvEditor( EAtomicType aAtomicType, String aKeeperId, ITsGuiContext aEditorContext ) {
    return false;
  }

  /**
   * Subclass may determine if this editor is suitable to edit values of the specified class.
   * <p>
   * Called for any classes except {@link IAtomicValue}. For atomic value
   * {@link #isSuitableAvEditor(EAtomicType, String, ITsGuiContext)} will be called.
   * <p>
   * Caller guarantees that if <code>true</code> returned, this editor will be used for VALED with the specified
   * context. So implementation may change context to prepare for correct creation.
   * <p>
   * Returns <code>false</code> in the base class, there is no need to call superclass method when overriding.
   *
   * @param aValueClass {@link Class} - the edited values class
   * @param aEditorContext {@link ITsGuiContext} - the editor creation context
   * @return boolean - <code>true</code> if this editor will be used for editing
   */
  protected boolean isSuitableRawEditor( Class<?> aValueClass, ITsGuiContext aEditorContext ) {
    return false;
  }

}

package org.toxsoft.tsgui.valed.controls.basic;

import static org.toxsoft.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.tsgui.valed.controls.basic.ITsResources.*;
import static org.toxsoft.tslib.av.EAtomicType.*;
import static org.toxsoft.tslib.av.impl.AvUtils.*;
import static org.toxsoft.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.toxsoft.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.tsgui.valed.api.IValedControl;
import org.toxsoft.tsgui.valed.impl.AbstractValedControl;
import org.toxsoft.tsgui.valed.impl.AbstractValedControlFactory;
import org.toxsoft.tslib.av.IAtomicValue;
import org.toxsoft.tslib.av.impl.DataDef;
import org.toxsoft.tslib.av.metainfo.IDataDef;
import org.toxsoft.tslib.bricks.ctx.ITsContextRo;
import org.toxsoft.tslib.utils.TsLibUtils;
import org.toxsoft.tslib.utils.errors.*;

/**
 * {@link String} editor with {@link Text} widget.
 *
 * @author hazard157
 */
public class ValedStringText
    extends AbstractValedControl<String, Composite> {

  /**
   * ID of the {@link #OPDEF_IS_MULTI_LINE}.
   */
  public static final String OPID_IS_MULTI_LINE = VALED_OPID_PREFIX + ".ValedStringText.IsMultiLine"; //$NON-NLS-1$

  /**
   * The flag detrimes if multi-line text will be edited is widget.
   */
  public static final IDataDef OPDEF_IS_MULTI_LINE = DataDef.create( OPID_IS_MULTI_LINE, BOOLEAN, //
      TSID_NAME, STR_N_IS_MULTI_LINE, //
      TSID_DESCRIPTION, STR_D_IS_MULTI_LINE, //
      TSID_DEFAULT_VALUE, AV_FALSE //
  );

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".StringText"; //$NON-NLS-1$

  /**
   * The factory class.
   *
   * @author hazard157
   */
  static class Factory
      extends AbstractValedControlFactory {

    protected Factory() {
      super( FACTORY_NAME );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected IValedControl<String> doCreateEditor( ITsGuiContext aContext ) {
      AbstractValedControl<String, ?> e = new ValedStringText( aContext );
      e.setParamIfNull( OPDEF_IS_WIDTH_FIXED, AV_FALSE );
      e.setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_FALSE );
      return e;
    }

  }

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  Composite backplane = null;
  Text      text      = null;

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the editor context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException context does not contains mandatory information
   * @throws TsIllegalArgumentRtException <code>enum</code> does not contains any constant
   */
  public ValedStringText( ITsGuiContext aContext ) {
    super( aContext );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void recreateWidgets() {
    backplane.setLayoutDeferred( true );
    try {
      String savedText = TsLibUtils.EMPTY_STRING;
      if( text != null ) {
        savedText = text.getText();
        text.dispose();
        text = null;
      }
      int style = SWT.BORDER;
      if( getIsMultiLine() ) {
        style |= SWT.MULTI | SWT.WRAP | SWT.V_SCROLL;
      }
      text = new Text( backplane, style );
      internalSetValueToWidget( savedText );
      text.setToolTipText( getTooltipText() );
      text.addModifyListener( notificationModifyListener );
      text.addFocusListener( notifyEditFinishedOnFocusLostListener );
      text.setEditable( isEditable() );
    }
    finally {
      backplane.setLayoutDeferred( false );
      backplane.getParent().layout( true );
      backplane.layout( true );
    }
  }

  private void internalSetValueToWidget( String aValue ) {
    setSelfEditing( true );
    try {
      text.setText( aValue );
    }
    finally {
      setSelfEditing( false );
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов AbstractDavControl
  //

  @Override
  public <X extends ITsContextRo> void onContextOpChanged( X aSource, String aId, IAtomicValue aValue ) {
    if( isWidget() ) {
      boolean wasMultiLine = (text.getStyle() & SWT.MULTI) != 0;
      if( wasMultiLine != getIsMultiLine() ) {
        recreateWidgets();
      }
    }
  }

  @Override
  protected void doSetEditable( boolean aEditable ) {
    if( isWidget() ) {
      text.setEditable( isEditable() );
    }
  }

  @Override
  protected Composite doCreateControl( Composite aParent ) {
    backplane = new Composite( aParent, SWT.NONE );
    backplane.setLayout( new FillLayout() );
    recreateWidgets();
    return backplane;
  }

  @Override
  protected String doGetUnvalidatedValue() {
    return text.getText();
  }

  @Override
  protected void doSetUnvalidatedValue( String aValue ) {
    internalSetValueToWidget( aValue );
  }

  @Override
  protected void doClearValue() {
    internalSetValueToWidget( TsLibUtils.EMPTY_STRING );
  }

  // ------------------------------------------------------------------------------------
  // API класса
  //

  /**
   * Returns the value of the option {@link #OPID_IS_MULTI_LINE}.
   *
   * @return boolean - the value of the option {@link #OPID_IS_MULTI_LINE}
   */
  public boolean getIsMultiLine() {
    return OPDEF_IS_MULTI_LINE.getValue( params() ).asBool();
  }

  /**
   * Sets the value of the option {@link #OPID_IS_MULTI_LINE}.
   *
   * @param aValue boolean - the value of the option {@link #OPID_IS_MULTI_LINE}
   */
  public void setIsMultiLine( boolean aValue ) {
    params().setBool( OPID_IS_MULTI_LINE, aValue );
  }

}

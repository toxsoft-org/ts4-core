package org.toxsoft.core.tsgui.valed.controls.graphics;

import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tsgui.valed.controls.graphics.ITsResources.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.fonts.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.helpers.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Simple editor of {@link IFontInfo} as uneditable text field with edit button at right.
 * <p>
 * The editor does returns <code>null</code> even if <code>null</code> was set via
 * {@link #doSetUnvalidatedValue(IFontInfo)}. Editor uses {@link IFontInfo#NULL} do denote that default font must be
 * used.
 *
 * @author hazard157
 */
public class ValedSimpleFontInfo
    extends AbstractValedTextAndButton<IFontInfo> {

  /**
   * The factory class.
   *
   * @author hazard157
   */
  @SuppressWarnings( "unchecked" )
  public static class Factory
      extends AbstractValedControlFactory {

    /**
     * Constructor.
     */
    public Factory() {
      super( FACTORY_NAME );
    }

    @Override
    protected IValedControl<IFontInfo> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedSimpleFontInfo( aContext );
    }

    @Override
    protected IValedControl<IFontInfo> doCreateViewer( ITsGuiContext aContext ) {
      return new ValedSimpleFontInfoViewer( aContext );
    }

    @Override
    protected boolean isSuitableRawEditor( Class<?> aValueClass, ITsGuiContext aEditorContext ) {
      return aValueClass.equals( IFontInfo.class );
    }

  }

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".SimpleFontInfo"; //$NON-NLS-1$

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  private IFontInfo fontInfo = IFontInfo.NULL;

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the valed context
   * @throws TsNullArgumentRtException аргумент = null
   */
  ValedSimpleFontInfo( ITsGuiContext aContext ) {
    super( aContext );
    setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_TRUE );
    setParamIfNull( OPDEF_VERTICAL_SPAN, AV_1 );
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedTextAndButton
  //

  @Override
  final protected void doAfterControlCreated() {
    ITsIconManager iconManager = tsContext().get( ITsIconManager.class );
    EIconSize iconSize = hdpiService().getJFaceCellIconsSize();
    getButtonControl().setImage( iconManager.loadStdIcon( ICONID_FONTS, iconSize ) );
  }

  @Override
  final protected boolean doProcessButtonPress() {
    FontDialog dlg = new FontDialog( getShell() );
    dlg.setText( DLG_T_FONT_SELECT );
    ITsFontManager fontManager = tsContext().get( ITsFontManager.class );
    if( fontInfo != IFontInfo.NULL ) {
      FontData initialFdntData = fontManager.info2data( fontInfo );
      dlg.setFontList( new FontData[] { initialFdntData } );
    }
    FontData fd = dlg.open();
    if( fd == null ) {
      return false;
    }
    fontInfo = fontManager.data2info( fd );
    return true;
  }

  @Override
  protected void doUpdateTextControl() {
    if( fontInfo != IFontInfo.NULL ) {
      getTextControl().setText( fontInfo.toString() );
    }
    else {
      getTextControl().setText( STR_MSG_DEFAULT_FONT );
    }
  }

  @Override
  protected void doDoSetUnvalidatedValue( IFontInfo aValue ) {
    fontInfo = aValue;
  }

  @Override
  protected IFontInfo doGetUnvalidatedValue() {
    return fontInfo;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the current value in editor.
   *
   * @return {@link IFontInfo} - the font information, never is <code>null</code>
   */
  public IFontInfo getFontInfo() {
    return fontInfo;
  }

  /**
   * Sets the value to edit.
   * <p>
   * Setting <code>null</code> is the same as setting {@link IFontInfo#NULL}.
   *
   * @param aFontInfo {@link IFontInfo} - the font information or <code>null</code>
   */
  public void setFontInfo( IFontInfo aFontInfo ) {
    if( aFontInfo != null ) {
      fontInfo = aFontInfo;
    }
    else {
      fontInfo = IFontInfo.NULL;
    }
    doUpdateTextControl();
  }

}

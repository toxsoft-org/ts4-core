package org.toxsoft.core.tsgui.valed.controls.graphics;

import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.core.tsgui.valed.controls.graphics.ITsResources.*;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.fonts.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.valed.controls.helpers.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Simple eidot of {@link IFontInfo} as uneditable text field with edit button at right.
 * <p>
 * Thisd class is generic because may edit both {@link IFontInfo} and {@link IAtomicValue}.
 *
 * @author hazard157
 * @param <V> - the edited value type
 */
public abstract class AbstractValedSimpleFontInfo<V>
    extends AbstractValedTextAndButton<V> {

  private IFontInfo fontInfo = IFontInfo.NULL;

  /**
   * Constructor for subclasses.
   *
   * @param aTsContext {@link ITsGuiContext} - the valed context
   * @throws TsNullArgumentRtException аргумент = null
   */
  protected AbstractValedSimpleFontInfo( ITsGuiContext aTsContext ) {
    super( aTsContext );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void updateTextControl() {
    if( fontInfo != IFontInfo.NULL ) {
      getTextControl().setText( fontInfo.toString() );
    }
    else {
      getTextControl().setText( STR_MSG_DEFAULT_FONT );
    }
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedTextAndButton
  //

  @Override
  final protected void doAfterControlCreated() {
    ITsIconManager iconManager = tsContext().get( ITsIconManager.class );
    EIconSize iconSize = hdpiService().getJFaceCellIconsSize();
    getButtonControl().setImage( iconManager.loadStdIcon( ICONID_FONTS, iconSize ) );
    updateTextControl();
  }

  @Override
  final protected void doProcessButtonPress() {
    FontDialog dlg = new FontDialog( getShell() );
    dlg.setText( DLG_T_FONT_SELECT );
    ITsFontManager fontManager = tsContext().get( ITsFontManager.class );
    if( fontInfo != IFontInfo.NULL ) {
      FontData initialFdntData = fontManager.info2data( fontInfo );
      dlg.setFontList( new FontData[] { initialFdntData } );
    }
    FontData fd = dlg.open();
    if( fd == null ) {
      return;
    }
    fontInfo = fontManager.data2info( fd );
    updateTextControl();
    fireModifyEvent( true );
  }

  // ------------------------------------------------------------------------------------
  // For subclasses
  //

  protected IFontInfo getFontInfo() {
    return fontInfo;
  }

  protected void setFontInfo( IFontInfo aFontInfo ) {
    if( aFontInfo != null ) {
      fontInfo = aFontInfo;
    }
    else {
      fontInfo = IFontInfo.NULL;
    }
    updateTextControl();
  }

}

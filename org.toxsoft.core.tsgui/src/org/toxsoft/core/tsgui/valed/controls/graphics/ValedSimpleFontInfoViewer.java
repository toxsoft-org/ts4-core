package org.toxsoft.core.tsgui.valed.controls.graphics;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.fonts.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

public class ValedSimpleFontInfoViewer
    extends AbstractValedControl<IFontInfo, Text> {

  private IFontInfo fontInfo = IFontInfo.NULL;

  /**
   * Конструкторe.
   *
   * @param aContext {@link ITsGuiContext} - контекст редактора
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  ValedSimpleFontInfoViewer( ITsGuiContext aContext ) {
    super( aContext );
    setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_TRUE );
    setParamIfNull( OPDEF_VERTICAL_SPAN, AV_1 );
  }

  // ------------------------------------------------------------------------------------
  //
  //

  private void refreshView() {
    Text text = getControl();
    if( text == null ) {
      return;
    }
    text.setText( "ABCxyz абвЭЮЯ 12390" );
    Font font = null;
    if( fontInfo != IFontInfo.NULL ) {
      font = fontManager().getFont( fontInfo );
    }
    getControl().setFont( font );
  }

  // ------------------------------------------------------------------------------------
  //
  //

  @Override
  protected Text doCreateControl( Composite aParent ) {
    Text text = new Text( aParent, SWT.BORDER | SWT.READ_ONLY );
    return text;
  }

  @Override
  protected void doSetEditable( boolean aEditable ) {
    // NOP
  }

  @Override
  protected IFontInfo doGetUnvalidatedValue() {
    return fontInfo;
  }

  @Override
  protected void doSetUnvalidatedValue( IFontInfo aValue ) {
    fontInfo = aValue != null ? aValue : IFontInfo.NULL;
    refreshView();
  }

  @Override
  protected void doClearValue() {
    fontInfo = IFontInfo.NULL;
    refreshView();
  }

}

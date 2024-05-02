package org.toxsoft.core.tsgui.bricks.combicond;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IFormulaTextWidget} implementation.
 *
 * @author hazard157
 */
public class FormulaTextWidget
    implements IFormulaTextWidget, ITsGuiContextable {

  private final GenericChangeEventer eventer;
  private final ITsGuiContext        tsContext;

  private final StyledText text;

  private RGB rgbErrorColor = new RGB( 255, 200, 200 );

  /**
   * Constructor.
   *
   * @param aParent {@link Composite} - the SWT paremt
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public FormulaTextWidget( Composite aParent, ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNulls( aParent, aContext );
    tsContext = aContext;
    eventer = new GenericChangeEventer( this );
    text = new StyledText( aParent, SWT.BORDER | SWT.SINGLE );
    text.addModifyListener( e -> eventer.fireChangeEvent() );
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // IFormulaTextWidget
  //

  @Override
  public Control getControl() {
    return text;
  }

  @Override
  public String getFormulaText() {
    return text.getText();
  }

  @Override
  public void setFormulaText( String aFormula ) {
    TsNullArgumentRtException.checkNull( aFormula );
    text.setText( aFormula );
    // clearStypeRanges();
  }

  @Override
  public void setToolTipText( String aTooltip ) {
    text.setToolTipText( aTooltip );
  }

  @Override
  public void setHighlights( int aErrorPos, IList<StyleRange> aHighlights ) {
    // check arguments
    TsNullArgumentRtException.checkNull( aHighlights );
    int fsLen = text.getText().length();
    TsIllegalArgumentRtException.checkTrue( aErrorPos < -1 || aErrorPos > fsLen );
    for( StyleRange r : aHighlights ) {
      TsIllegalArgumentRtException.checkTrue( r.start < 0 );
      TsIllegalArgumentRtException.checkTrue( r.start + r.length > fsLen );
    }
    // reset old styles
    text.setStyleRange( null );
    // set highlights
    for( StyleRange r : aHighlights ) {
      // if( aErrorPos < 0 ) {
      text.setStyleRange( r );
      // continue;
      // }
    }
    // set error range (if any)
    if( aErrorPos >= 0 ) {
      Color errColor = colorManager().getColor( rgbErrorColor );
      StyleRange errorRange = new StyleRange();
      errorRange.start = aErrorPos;
      errorRange.length = fsLen - aErrorPos;
      errorRange.background = errColor;
      text.setStyleRange( errorRange );
    }
  }

  @Override
  public boolean isEditable() {
    return text.getEditable();
  }

  @Override
  public void setEditable( boolean aEditable ) {
    text.setEditable( aEditable );
  }

  @Override
  public RGB getErrorHighlightColor() {
    return rgbErrorColor;
  }

  @Override
  public void setErrorHighlightColor( RGB aColor ) {
    TsNullArgumentRtException.checkNull( aColor );
    rgbErrorColor = aColor;
  }

}

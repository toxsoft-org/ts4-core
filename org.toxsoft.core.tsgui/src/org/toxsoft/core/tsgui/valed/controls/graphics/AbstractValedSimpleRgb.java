package org.toxsoft.core.tsgui.valed.controls.graphics;

import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.core.tsgui.valed.controls.graphics.ITsResources.*;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.valed.controls.helpers.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Simple editor of {@link RGB} as uneditable text field with edit button at right.
 * <p>
 * This class is generic because may edit both {@link RGB} and {@link IAtomicValue}.
 *
 * @author hazard157
 * @author dima
 * @param <V> - the edited value type
 */
public abstract class AbstractValedSimpleRgb<V>
    extends AbstractValedLabelAndButton<V> {

  private static final int IMAGE_WIDTH  = 20;
  private static final int IMAGE_HEIGHT = 20;
  private RGB              value        = null;
  private Image            colorImage   = null;

  /**
   * Constructor.
   *
   * @param aTsContext {@link ITsGuiContext} - the VALED context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  protected AbstractValedSimpleRgb( ITsGuiContext aTsContext ) {
    super( aTsContext );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  /**
   * Returns human-readable name of the RGB color.
   *
   * @param aRgb {@link RGB} - the color, may be <code>null</code>
   * @return String - human-readable color name
   */
  public String rgb2text( RGB aRgb ) {
    if( aRgb == null ) {
      return STR_MSG_DEFAULT_COLOR;
    }
    return colorManager().getHumanReadableName( aRgb );
  }

  @Override
  protected void doUpdateLabelControl() {
    getLabelControl().setText( rgb2text( value ) );
    // refresh color sample rectangle
    if( colorImage != null ) {
      colorImage.dispose();
    }
    if( value != null ) {
      colorImage = new Image( Display.getCurrent(), IMAGE_WIDTH, IMAGE_HEIGHT );
      GC gc = new GC( colorImage );
      gc.setBackground( new Color( value.red, value.green, value.blue ) );
      gc.fillRectangle( 0, 0, colorImage.getImageData().width, colorImage.getImageData().height );
      gc.dispose();
      getLabelControl().setImage( colorImage );
    }
    // TODO set font color to WHITE for dark colors
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedTextAndButton
  //

  @Override
  final protected void doAfterControlCreated() {
    ITsIconManager iconManager = tsContext().get( ITsIconManager.class );
    EIconSize iconSize = hdpiService().getJFaceCellIconsSize();
    getButtonControl().setImage( iconManager.loadStdIcon( ICONID_COLORS, iconSize ) );
  }

  @Override
  final protected boolean doProcessButtonPress() {
    ColorDialog dlg = new ColorDialog( getShell() );
    dlg.setText( DLG_T_COLOR_SELECT );
    dlg.setRGB( value );
    RGB newVal = dlg.open();
    if( newVal == null ) {
      return false;
    }
    value = newVal;
    return true;
  }

  @Override
  protected void onDispose() {
    if( colorImage != null ) {
      colorImage.dispose();
    }
  }

  // ------------------------------------------------------------------------------------
  // For subclasses
  //

  protected RGB getRgb() {
    return value;
  }

  protected void setRgb( RGB aRgb ) {
    value = aRgb;
  }

}

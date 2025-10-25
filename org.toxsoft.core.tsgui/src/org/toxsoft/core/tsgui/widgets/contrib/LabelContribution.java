package org.toxsoft.core.tsgui.widgets.contrib;

import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.eclipse.jface.action.*;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Contributes {@link CLabel} control to the toolbar.
 *
 * @author vs
 */
public class LabelContribution
    extends ControlContribution {

  private final int width;
  private final int swtStyle;

  private String initialText = EMPTY_STRING;
  private CLabel label       = null;

  /**
   * Constructor.
   *
   * @param aId String - the contribution item ID, may be <code>null</code>
   * @param aWidth int - contribution item width in pixels or {@link SWT#DEFAULT} for default width
   * @param aText String - initial text string
   * @param aSwtStyle int - SWT style to be used for {@link CLabel} control creation
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public LabelContribution( String aId, int aWidth, String aText, int aSwtStyle ) {
    super( aId );
    TsNullArgumentRtException.checkNull( aText );
    width = aWidth;
    swtStyle = aSwtStyle;
    initialText = aText;
  }

  /**
   * Constructor.
   * <p>
   * Creates control with no text.
   *
   * @param aId String - the contribution item ID, may be <code>null</code>
   * @param aWidth int - width of the contribution item or {@link SWT#DEFAULT} for default width
   * @param aSwtStyle int - SWT style to be used for {@link CLabel} control creation
   */
  public LabelContribution( String aId, int aWidth, int aSwtStyle ) {
    this( aId, aWidth, EMPTY_STRING, aSwtStyle );
  }

  /**
   * Constructor.
   * <p>
   * Creates control with no text and default contribution item ID..
   *
   * @param aWidth int - width of the contribution item or {@link SWT#DEFAULT} for default width
   * @param aSwtStyle int - SWT style to be used for {@link CLabel} control creation
   */
  public LabelContribution( int aWidth, int aSwtStyle ) {
    this( LabelContribution.class.getSimpleName(), aWidth, EMPTY_STRING, aSwtStyle );
  }

  // ------------------------------------------------------------------------------------
  // ControlContribution
  //

  @Override
  protected Control createControl( Composite aParent ) {
    label = new CLabel( aParent, swtStyle );
    label.setText( initialText );
    label.setBackground( new Color( new RGB( 255, 255, 255 ) ) );
    label.setSize( SWT.DEFAULT, SWT.DEFAULT );
    return label;
  }

  @Override
  protected int computeWidth( Control aControl ) {
    if( width == SWT.DEFAULT ) {
      return super.computeWidth( aControl );
    }
    return width;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Sets the text to b displayed.
   * <p>
   * Method mey be used at any time, even if control {@link #label()} is not created yet.
   *
   * @param aText String - the text
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void setText( String aText ) {
    TsNullArgumentRtException.checkNull( aText );
    if( label != null ) {
      label.setText( aText );
      label.redraw();
    }
    else {
      initialText = aText;
    }
  }

  /**
   * Returns the implementing label control.
   *
   * @return CLabel - the label control or <code>null</code> before control creation
   */
  public CLabel label() {
    return label;
  }

}

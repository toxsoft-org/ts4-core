package org.toxsoft.core.tsgui.widgets.contrib;

import org.eclipse.jface.action.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Contributes {@link FilterTextField} control to the toolbar.
 *
 * @author hazard157
 */
public class TextFilterContribution
    extends ControlContribution
    implements ITsGuiContextable {

  private final ITsGuiContext tsContext;
  private final int           width;

  private FilterTextField ftField;

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aId String - the contribution item ID, may be <code>null</code>
   * @param aWidth int - contribution item width in pixels or {@link SWT#DEFAULT} for default width
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException illegal width
   */
  public TextFilterContribution( ITsGuiContext aContext, String aId, int aWidth ) {
    super( aId );
    TsNullArgumentRtException.checkNull( aContext );
    TsIllegalArgumentRtException.checkTrue( aWidth < SWT.DEFAULT );
    TsIllegalArgumentRtException.checkTrue( aWidth >= 0 && aWidth < 8 );
    tsContext = aContext;
    width = aWidth;
  }

  /**
   * Constructor.
   * <p>
   * Creates control with default contribution item ID.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aWidth int - contribution item width in pixels or {@link SWT#DEFAULT} for default width
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException illegal width
   */
  public TextFilterContribution( ITsGuiContext aContext, int aWidth ) {
    this( aContext, TextFilterContribution.class.getSimpleName(), aWidth );
  }

  // ------------------------------------------------------------------------------------
  // ControlContribution
  //

  @Override
  protected Control createControl( Composite aParent ) {
    ftField = new FilterTextField( aParent, tsContext );
    return ftField;
  }

  @Override
  protected int computeWidth( Control aControl ) {
    if( width == SWT.DEFAULT ) {
      return super.computeWidth( aControl );
    }
    return width;
  }

  // ------------------------------------------------------------------------------------
  // TextFilterContribution
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the implementing control to access filter settings.
   *
   * @return {@link FilterTextField} - the filter text field
   */
  public FilterTextField field() {
    return ftField;
  }

}

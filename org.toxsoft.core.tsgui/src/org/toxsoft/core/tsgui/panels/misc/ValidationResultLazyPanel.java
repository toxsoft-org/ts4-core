package org.toxsoft.core.tsgui.panels.misc;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The same panel as {@link ValidationResultPanel} but with lazy initialization.
 * <p>
 * Uses {@link ValidationResultPanel} as an implementation so supports all options.
 *
 * @author hazard157
 */
public class ValidationResultLazyPanel
    extends AbstractLazyPanel<Control> {

  private ValidationResultPanel panel  = null;
  private ValidationResult      status = ValidationResult.SUCCESS;

  /**
   * Constructor.
   * <p>
   * Constructor stores reference to the context, does not creates copy.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ValidationResultLazyPanel( ITsGuiContext aContext ) {
    super( aContext );
  }

  // ------------------------------------------------------------------------------------
  // AbstractLazyPanel
  //

  @Override
  protected Control doCreateControl( Composite aParent ) {
    panel = new ValidationResultPanel( aParent, tsContext() );
    panel.setShownValidationResult( status );
    return panel;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns currently displayed validation result.
   * <p>
   * Initially returns {@link ValidationResult#SUCCESS}.
   *
   * @return {@link ValidationResult} - the validation result
   */
  public ValidationResult getShownValidationResult() {
    return status;
  }

  /**
   * Sets validation result to be displayed
   *
   * @param aStatus {@link ValidationResult} - the validation result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void setShownValidationResult( ValidationResult aStatus ) {
    TsNullArgumentRtException.checkNull( aStatus );
    status = aStatus;
    panel.setShownValidationResult( status );
  }

}

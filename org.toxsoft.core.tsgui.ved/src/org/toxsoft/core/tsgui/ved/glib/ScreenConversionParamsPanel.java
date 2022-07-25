package org.toxsoft.core.tsgui.ved.glib;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Contains control to manage {@link IVedScreen#getConversion()} parameters.
 * <p>
 * Contains control to change zoom, shift and set location of screen, rotate screen.
 * <p>
 * Listens to the {@link IVedScreen#conversionChangeEventer()} and updates controls.
 *
 * @author hazard157
 */
public class ScreenConversionParamsPanel
    extends TsPanel {

  private final IGenericChangeListener screenConversionChangeListener = aSource -> internalUpdateControlsFromScreen();

  private IVedScreen attachedScreen = null;

  /**
   * Constructor.
   * <p>
   * Constructos stores reference to the context, does not creates copy.
   *
   * @param aParent {@link Composite} - parent component
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ScreenConversionParamsPanel( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    // TODO create widgets
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void internalUpdateControlsFromScreen() {
    // TODO ScreenConversionParamsPanel.internalUpdateControlsFromScreen()
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  public void attachToScreen( IVedScreen aScreen ) {
    if( attachedScreen != null ) {
      attachedScreen.conversionChangeEventer().removeListener( screenConversionChangeListener );
    }
    attachedScreen = aScreen;
    if( attachedScreen != null ) {
      attachedScreen.conversionChangeEventer().addListener( screenConversionChangeListener );
    }
    internalUpdateControlsFromScreen();
  }

}

package org.toxsoft.core.tsgui.ved.valeds;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tslib.av.*;

/**
 * VALED to select a VISEL ID from the {@link IVedScreenModel#visels()} as an atmoic value.
 * <p>
 * Wraps over {@link ValedVedViselIdSelector}.
 *
 * @author hazard157
 */
public class ValedVedViselAvStringIdSelector
    extends AbstractAvWrapperValedControl<String> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".VedViselAvStringIdSelector"; //$NON-NLS-1$

  /**
   * The factory singleton.
   */
  @SuppressWarnings( "unchecked" )
  public static final AbstractValedControlFactory FACTORY = new AbstractValedControlFactory( FACTORY_NAME ) {

    @Override
    protected IValedControl<IAtomicValue> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedVedViselAvStringIdSelector( aContext );
    }

  };

  ValedVedViselAvStringIdSelector( ITsGuiContext aTsContext ) {
    super( aTsContext, EAtomicType.STRING, ValedVedViselIdSelector.FACTORY );
  }

  // ------------------------------------------------------------------------------------
  // AbstractAvWrapperValedControl
  //

  @Override
  protected IAtomicValue tv2av( String aTypedValue ) {
    return avStr( aTypedValue );
  }

  @Override
  protected String av2tv( IAtomicValue aAtomicValue ) {
    return aAtomicValue.asString();
  }

}

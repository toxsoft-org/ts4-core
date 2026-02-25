package org.toxsoft.core.tsgui.valed.controls.metainf;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.utils.valobj.*;

/**
 * VALED to specify keeper ID as {@link IAtomicValue} either registered in {@link TsValobjUtils} or entered manually.
 *
 * @author hazard157
 */
public class ValedAvStringKeeperIdSelector
    extends AbstractAvWrapperValedControl<String> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".AvStringKeeperIdSelector"; //$NON-NLS-1$

  /**
   * The factory singleton.
   */
  @SuppressWarnings( "unchecked" )
  public static final AbstractValedControlFactory FACTORY = new AbstractValedControlFactory( FACTORY_NAME ) {

    @Override
    protected IValedControl<IAtomicValue> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedAvStringKeeperIdSelector( aContext );
    }

  };

  protected ValedAvStringKeeperIdSelector( ITsGuiContext aTsContext ) {
    super( aTsContext, EAtomicType.STRING, ValedKeeperIdSelector.FACTORY );
  }

  @Override
  protected IAtomicValue tv2av( String aTypedValue ) {
    return avStr( aTypedValue );
  }

  @Override
  protected String av2tv( IAtomicValue aAtomicValue ) {
    return aAtomicValue.asString();
  }

}

package org.toxsoft.core.tsgui.rcp.valed;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import java.io.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link EAtomicType#STRING} of type {@link File#getPath()} editor.
 * <p>
 * Wraps over {@link ValedFile}.
 *
 * @author hazard157
 */
public class ValedAvStringFile
    extends AbstractAvWrapperValedControl<File> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".AvStringFile"; //$NON-NLS-1$

  /**
   * The factory class.
   *
   * @author hazard157
   */
  static class Factory
      extends AbstractValedControlFactory {

    protected Factory() {
      super( FACTORY_NAME );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected IValedControl<IAtomicValue> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedAvStringFile( aContext );
    }

  }

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  /**
   * Constructor.
   *
   * @param aTsContext {@link ITsGuiContext} - the editor context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ValedAvStringFile( ITsGuiContext aTsContext ) {
    super( aTsContext, EAtomicType.STRING, ValedFile.FACTORY );
  }

  @Override
  protected IAtomicValue tv2av( File aTypedValue ) {
    return avStr( aTypedValue.getPath() );
  }

  @Override
  protected File av2tv( IAtomicValue aAtomicValue ) {
    return new File( aAtomicValue.asString() );
  }

}

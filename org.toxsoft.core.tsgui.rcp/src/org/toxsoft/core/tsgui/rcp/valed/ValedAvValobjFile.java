package org.toxsoft.core.tsgui.rcp.valed;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;

import java.io.File;

import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.valed.api.IValedControl;
import org.toxsoft.core.tsgui.valed.controls.av.AbstractAvValobjWrapperValedControl;
import org.toxsoft.core.tsgui.valed.controls.av.ValedAvValobjLocalDateMpv;
import org.toxsoft.core.tsgui.valed.impl.AbstractValedControlFactory;
import org.toxsoft.core.tslib.av.EAtomicType;
import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link EAtomicType#VALOBJ} of type {@link File} editor.
 * <p>
 * Wraps over {@link ValedFile}.
 *
 * @author goga
 */
public class ValedAvValobjFile
    extends AbstractAvValobjWrapperValedControl<File> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".AvValobjFile"; //$NON-NLS-1$

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
      return new ValedAvValobjLocalDateMpv( aContext );
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
  public ValedAvValobjFile( ITsGuiContext aTsContext ) {
    super( aTsContext, ValedFile.FACTORY );
  }

}

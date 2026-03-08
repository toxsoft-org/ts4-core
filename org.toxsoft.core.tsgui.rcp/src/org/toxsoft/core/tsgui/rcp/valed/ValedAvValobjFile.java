package org.toxsoft.core.tsgui.rcp.valed;

import java.io.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.gui.*;

/**
 * {@link EAtomicType#VALOBJ} of type {@link File} editor.
 * <p>
 * Wraps over {@link ValedFile}.
 *
 * @author hazard157
 */
public class ValedAvValobjFile
    extends AbstractAvValobjWrapperValedControl<File> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = ITsLibInnerSharedConstants.TSLIB_VALED_AV_VALOBJ_FILE;

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
      return new ValedAvValobjFile( aContext );
    }

    @Override
    protected boolean isSuitableAvEditor( EAtomicType aAtomicType, String aKeeperId, ITsGuiContext aEditorContext ) {
      if( aAtomicType == EAtomicType.VALOBJ && aKeeperId != null ) {
        return aKeeperId.equals( FileKeeper.KEEPER_ID );
      }
      return false;
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

package org.toxsoft.core.tsgui.bricks.combicond.valed;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.math.combicond.*;
import org.toxsoft.core.tslib.math.combicond.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link EAtomicType#VALOBJ} of type {@link ISingleCondParams} editor.
 * <p>
 * Wraps over {@link ValedSingleCondParams}.
 *
 * @author hazard157
 */
public class ValedAvSingleCondParams
    extends AbstractAvValobjWrapperValedControl<ISingleCondParams> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".AvSingleCondParams"; //$NON-NLS-1$

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new AbstractValedControlFactory( FACTORY_NAME ) {

    @SuppressWarnings( "unchecked" )
    @Override
    protected IValedControl<IAtomicValue> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedAvSingleCondParams( aContext );
    }

    @Override
    protected boolean isSuitableAvEditor( EAtomicType aAtomicType, String aKeeperId, ITsGuiContext aEditorContext ) {
      if( aAtomicType == EAtomicType.VALOBJ && aKeeperId != null ) {
        return aKeeperId.equals( SingleCondParams.KEEPER_ID );
      }
      return false;
    }
  };

  /**
   * Constructor.
   *
   * @param aTsContext {@link ITsGuiContext} - the VALED context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ValedAvSingleCondParams( ITsGuiContext aTsContext ) {
    super( aTsContext, ValedSingleCondParams.FACTORY );
  }

}

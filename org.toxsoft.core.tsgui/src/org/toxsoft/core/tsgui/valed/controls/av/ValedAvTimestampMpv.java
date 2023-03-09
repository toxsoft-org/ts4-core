package org.toxsoft.core.tsgui.valed.controls.av;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.time.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link EAtomicType#TIMESTAMP} editor using {@link ValedTimestampMpv}.
 *
 * @author hazard157
 */
public class ValedAvTimestampMpv
    extends AbstractAvWrapperValedControl<Long> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".AvTimestampMpv"; //$NON-NLS-1$

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
      return new ValedAvTimestampMpv( aContext );
    }

    @Override
    protected boolean isSuitableAvEditor( EAtomicType aAtomicType, String aKeeperId, ITsGuiContext aEditorContext ) {
      return aAtomicType == EAtomicType.TIMESTAMP;
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
  public ValedAvTimestampMpv( ITsGuiContext aTsContext ) {
    super( aTsContext, EAtomicType.TIMESTAMP, ValedTimestampMpv.FACTORY );
  }

  @Override
  protected IAtomicValue tv2av( Long aValue ) {
    return AvUtils.avTimestamp( aValue.longValue() );
  }

  @Override
  protected Long av2tv( IAtomicValue aAtomicValue ) {
    return Long.valueOf( aAtomicValue.asLong() );
  }

}

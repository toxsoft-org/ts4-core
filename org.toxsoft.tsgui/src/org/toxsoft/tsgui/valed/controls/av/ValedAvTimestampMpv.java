package org.toxsoft.tsgui.valed.controls.av;

import static org.toxsoft.tsgui.valed.api.IValedControlConstants.*;

import org.toxsoft.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.tsgui.valed.api.IValedControl;
import org.toxsoft.tsgui.valed.controls.time.ValedTimestampMpv;
import org.toxsoft.tsgui.valed.impl.AbstractValedControlFactory;
import org.toxsoft.tslib.av.EAtomicType;
import org.toxsoft.tslib.av.IAtomicValue;
import org.toxsoft.tslib.av.impl.AvUtils;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

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
      return new ValedAvIntegerSpinner( aContext );
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

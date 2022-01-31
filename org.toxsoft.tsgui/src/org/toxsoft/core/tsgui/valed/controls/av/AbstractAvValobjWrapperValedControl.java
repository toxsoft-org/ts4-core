package org.toxsoft.core.tsgui.valed.controls.av;

import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.valed.api.IValedControlFactory;
import org.toxsoft.core.tslib.av.EAtomicType;
import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.av.impl.AvUtils;
import org.toxsoft.core.tslib.utils.valobj.TsValobjUtils;

/**
 * The wrapper over valed of any &lt;T&gt; type to be edited as {@link EAtomicType#VALOBJ}.
 * <p>
 * Warning: &lt;T&gt; must be registered as value-object in {@link TsValobjUtils}.
 *
 * @author hazard157
 * @param <T> - value type {@link EAtomicType#VALOBJ}
 */
public class AbstractAvValobjWrapperValedControl<T>
    extends AbstractAvWrapperValedControl<T> {

  protected AbstractAvValobjWrapperValedControl( ITsGuiContext aTsContext, IValedControlFactory aUnderlyingFactory ) {
    super( aTsContext, EAtomicType.VALOBJ, aUnderlyingFactory );
  }

  @SuppressWarnings( "unchecked" )
  @Override
  protected T av2tv( IAtomicValue aValue ) {
    return (T)AvUtils.avValobj( aValue );
  }

  @Override
  protected IAtomicValue tv2av( T aValue ) {
    return AvUtils.avValobj( aValue );
  }

}

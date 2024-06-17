package org.toxsoft.core.tsgui.valed.controls.av;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.utils.gui.ITsLibInnerSharedConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.enums.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.valobj.*;

/**
 * {@link EAtomicType#VALOBJ} Java <code>enum</code> editor using {@link ValedEnumCombo}.
 *
 * @author hazard157
 * @param <V> - enum class
 */
public class ValedAvValobjEnumCombo<V extends Enum<V>>
    extends AbstractAvWrapperValedControl<V> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = TSLIB_VALED_AV_VALOBJ_ENUM_COMBO;

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
      return new ValedAvValobjEnumCombo<>( aContext );
    }

    @Override
    protected boolean isSuitableAvEditor( EAtomicType aAtomicType, String aKeeperId, ITsGuiContext aEditorContext ) {
      if( aAtomicType == EAtomicType.VALOBJ && aKeeperId != null ) {
        IEntityKeeper<?> keeper = TsValobjUtils.findKeeperById( aKeeperId );
        if( keeper != null ) {
          Class<?> rawClass = keeper.entityClass();
          if( rawClass != null && rawClass.isEnum() ) {
            IValedEnumConstants.REFDEF_ENUM_CLASS.setRef( aEditorContext, rawClass );
            return true;
          }
        }
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
  public ValedAvValobjEnumCombo( ITsGuiContext aTsContext ) {
    super( aTsContext, EAtomicType.VALOBJ, ValedEnumCombo.FACTORY );

  }

  @Override
  protected IAtomicValue tv2av( V aTypedValue ) {
    return avValobj( aTypedValue );
  }

  @Override
  protected V av2tv( IAtomicValue aAtomicValue ) {
    return aAtomicValue.asValobj();
  }

}

package org.toxsoft.core.tsgui.valed.controls.graphics;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Аналог {@link ValedSimpleRgb} работающий со значениями {@link IAtomicValue} типа {@link EAtomicType#VALOBJ}.
 * <p>
 * Редактор возвращает {@link IAtomicValue#NULL} если цвет не задан, рассматривая это как "цвет по умолчанию".
 *
 * @author hazard157
 */
public class ValedAvValobjSimpleRgb
    extends AbstractValedSimpleRgb<IAtomicValue> {

  /**
   * The factory class.
   *
   * @author hazard157
   */
  public static class Factory
      extends AbstractValedControlFactory {

    /**
     * Constructor.
     */
    public Factory() {
      super( FACTORY_NAME );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected IValedControl<IAtomicValue> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedAvValobjSimpleRgb( aContext );
    }

    @Override
    protected boolean isSuitableAvEditor( EAtomicType aAtomicType, String aKeeperId, ITsGuiContext aEditorContext ) {
      if( aAtomicType == EAtomicType.VALOBJ && aKeeperId != null ) {
        return aKeeperId.equals( RGBKeeper.KEEPER_ID );
      }
      return false;
    }

  }

  /**
   * The registered factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".AvValobjSimpleRgb"; //$NON-NLS-1$

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the valed context
   * @throws TsNullArgumentRtException аргумент = null
   */
  ValedAvValobjSimpleRgb( ITsGuiContext aContext ) {
    super( aContext );
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedSimpleRgb
  //

  @Override
  protected IAtomicValue doGetUnvalidatedValue() {
    RGB rgb = getRgb();
    if( rgb == null ) {
      return IAtomicValue.NULL;
    }
    return avValobj( rgb );
  }

  @Override
  protected void doDoSetUnvalidatedValue( IAtomicValue aValue ) {
    if( aValue == null || aValue == IAtomicValue.NULL ) {
      setRgb( null );
    }
    else {
      setRgb( aValue.asValobj() );
    }
  }

}

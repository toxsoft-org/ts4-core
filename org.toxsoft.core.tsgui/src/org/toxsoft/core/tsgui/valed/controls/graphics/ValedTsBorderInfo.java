package org.toxsoft.core.tsgui.valed.controls.graphics;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tsgui.valed.controls.graphics.ITsResources.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tsgui.panels.opsedit.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.helpers.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.opset.*;

/**
 * Редактор атрибутов для рисования прямоугольной границы.
 * <p>
 *
 * @author vs
 */
public class ValedTsBorderInfo
    extends AbstractValedLabelAndButton<TsBorderInfo> {

  /**
   * Название фабрики, с которым она зарегистрирована в {@link ValedControlFactoriesRegistry}.
   * <p>
   * Напомним, что автоматическая регистрация с именем класса фабрики тоже работает.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".TsBorderInfo"; //$NON-NLS-1$

  /**
   * The factory class.
   *
   * @author vs
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
    protected IValedControl<TsBorderInfo> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedTsBorderInfo( aContext );
    }

  }

  /**
   * Синглтон Фабрики.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  TsBorderInfo value = TsBorderInfo.NONE;

  /**
   * Конструктор.
   *
   * @param aTsContext ITsGuiContext - соответствующий контекст
   */
  protected ValedTsBorderInfo( ITsGuiContext aTsContext ) {
    super( aTsContext );
    setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_TRUE );
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedLabelAndButton
  //

  @Override
  protected void doProcessButtonPress() {
    TsDialogInfo dlgInfo = new TsDialogInfo( tsContext(), DLG_T_BORDER_INFO, STR_MSG_BORDER_INFO );
    IOptionSet opSet = DialogOptionsEdit.editOpset( dlgInfo, TsBorderInfo.ALL_DEFS, value.options() );
    if( opSet != null ) {
      value = TsBorderInfo.ofOptions( opSet );
      fireModifyEvent( true );
    }
  }

  @Override
  protected TsBorderInfo doGetUnvalidatedValue() {
    return value;
  }

  @Override
  protected void doSetUnvalidatedValue( TsBorderInfo aValue ) {
    value = aValue;
  }

}

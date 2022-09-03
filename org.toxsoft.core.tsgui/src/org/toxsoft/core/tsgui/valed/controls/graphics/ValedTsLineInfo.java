package org.toxsoft.core.tsgui.valed.controls.graphics;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.helpers.*;
import org.toxsoft.core.tsgui.valed.impl.*;

/**
 * Редактор атрибутов для рисования линии.
 * <p>
 *
 * @author vs
 */
public class ValedTsLineInfo
    extends AbstractValedLabelAndButton<TsLineInfo> {

  /**
   * Название фабрики, с которым она зарегистрирована в {@link ValedControlFactoriesRegistry}.
   * <p>
   * Напомним, что автоматическая регистрация с именем класса фабрики тоже работает.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".TsLineInfo"; //$NON-NLS-1$

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
    protected IValedControl<TsLineInfo> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedTsLineInfo( aContext );
    }

  }

  /**
   * Синглтон Фабрики.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  TsLineInfo lineInfo = TsLineInfo.DEFAULT;

  protected ValedTsLineInfo( ITsGuiContext aTsContext ) {
    super( aTsContext );
  }

  // ------------------------------------------------------------------------------------
  //
  //

  @Override
  protected void doProcessButtonPress() {
    TsLineInfo li = PanelTsLineInfoEditor.editLineInfo( lineInfo, tsContext() );
    if( li != null ) {
      lineInfo = li;
    }
  }

  @Override
  protected TsLineInfo doGetUnvalidatedValue() {
    return lineInfo;
  }

  @Override
  protected void doSetUnvalidatedValue( TsLineInfo aValue ) {
    lineInfo = aValue;
  }

}

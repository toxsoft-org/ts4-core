package org.toxsoft.tsgui.m5_1.impl.gui;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.toxsoft.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.tsgui.bricks.dialogs.TsDialogUtils;
import org.toxsoft.tsgui.m5_1.api.*;
import org.toxsoft.tsgui.m5_1.gui.IM5EntityEditPanel;
import org.toxsoft.tsgui.m5_1.gui.IM5EntityPanel;
import org.toxsoft.tslib.bricks.validator.ValidationResult;
import org.toxsoft.tslib.bricks.validator.impl.TsValidationFailedRtException;
import org.toxsoft.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.tslib.utils.logs.impl.LoggerUtils;

/**
 * Базовая реализация панели {@link IM5EntityPanel}.
 *
 * @author goga
 * @param <T> - класс моделированых сущностей, отображаемых в панели
 */
public abstract class M5AbstractEntityPanel<T>
    extends M5PanelBase<T>
    implements IM5EntityEditPanel<T> {

  /**
   * Запомненные значения полей, которые были заданы в {@link #setValues(IM5Bunch)}.
   */
  private IM5Bunch<T> initialValues = null;

  /**
   * Менеджер ЖЦ, может быть <code>null</code>, используется только панелями редактирования.
   */
  private IM5LifecycleManager<T> lifecycleManager = null;

  /**
   * Конструктор.
   *
   * @param aContext {@link ITsGuiContext} - контекст панели
   * @param aModel {@link IM5Model} - модель
   * @param aViewer boolean - признак просмотрщика (панели только для просмотра)
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public M5AbstractEntityPanel( ITsGuiContext aContext, IM5Model<T> aModel, boolean aViewer ) {
    super( aContext, aModel, aViewer );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IM5EntityPanel
  //

  @Override
  public void setValues( IM5Bunch<T> aBunch ) {
    if( aBunch != null ) {
      TsIllegalArgumentRtException.checkFalse( model().equals( aBunch.model() ) );
    }
    initialValues = aBunch;
    try {
      doSetValues( aBunch );
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
      TsDialogUtils.error( getShell(), ex );
    }
    doEditableStateChanged();
  }

  @Override
  public void setEntity( T aEntity ) {
    if( aEntity != null ) {
      TsIllegalArgumentRtException.checkFalse( model().isModelledObject( aEntity ) );
    }
    if( aEntity != null ) {
      setValues( model().valuesOf( aEntity ) );
    }
    else {
      setValues( getValues() );
    }
  }

  @Override
  public IM5Bunch<T> initialValues() {
    return initialValues;
  }

  @Override
  public ValidationResult canGetValues() {
    return doCanGetValues();
  }

  @Override
  public IM5Bunch<T> getValues() {
    // TODO если не было изменений, взять bunch из кеша
    TsValidationFailedRtException.checkError( canGetValues() );
    return doGetValues();
  }

  @Override
  final public IM5LifecycleManager<T> lifecycleManager() {
    return lifecycleManager;
  }

  @Override
  public void setLifecycleManager( IM5LifecycleManager<T> aLifecycleManager ) {
    lifecycleManager = aLifecycleManager;
  }

  // ------------------------------------------------------------------------------------
  // Для перелпределения
  //

  // TODO comments

  @Override
  protected abstract Control doCreateControl( Composite aParent );

  protected abstract void doSetValues( IM5Bunch<T> aBunch );

  protected abstract ValidationResult doCanGetValues();

  protected abstract IM5Bunch<T> doGetValues();

}

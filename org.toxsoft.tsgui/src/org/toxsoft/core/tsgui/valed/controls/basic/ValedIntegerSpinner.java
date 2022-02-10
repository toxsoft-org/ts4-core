package org.toxsoft.core.tsgui.valed.controls.basic;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tsgui.valed.controls.basic.ITsResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link Integer} editor with {@link Spinner} widget.
 *
 * @author goga
 */
public class ValedIntegerSpinner
    extends AbstractValedControl<Integer, Composite> {

  /**
   * ID of the {@link #OPDEF_STEP}.
   */
  public static final String OPID_STEP = VALED_OPID_PREFIX + ".IntegerSpinner.Step"; //$NON-NLS-1$

  /**
   * ID of the {@link #OPDEF_PAGE_STEP}.
   */
  public static final String OPID_PAGE_STEP = VALED_OPID_PREFIX + ".IntegerSpinner.PageStep"; //$NON-NLS-1$

  /**
   * Value change step by the arrow keys.
   */
  public static final IDataDef OPDEF_STEP = DataDef.create( OPID_STEP, INTEGER, //
      TSID_NAME, STR_N_INT_SPINNER_STEP, //
      TSID_DESCRIPTION, STR_D_INT_SPINNER_STEP, //
      TSID_DEFAULT_VALUE, AV_1 //
  );

  /**
   * Value change step by the PageUp/PageDown keys.
   */
  public static final IDataDef OPDEF_PAGE_STEP = DataDef.create( OPID_PAGE_STEP, INTEGER, //
      TSID_NAME, STR_N_INT_SPINNER_PAGE_STEP, //
      TSID_DESCRIPTION, STR_D_INT_SPINNER_PAGE_STEP, //
      TSID_DEFAULT_VALUE, avInt( 10 ) //
  );

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".IntegerSpinner"; //$NON-NLS-1$

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
    protected IValedControl<Integer> doCreateEditor( ITsGuiContext aContext ) {
      AbstractValedControl<Integer, ?> e = new ValedIntegerSpinner( aContext );
      return e;
    }

  }

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  private Composite backplane = null;

  /**
   * В режиме {@link #isEditable()} = <code>false</code> целое отображается текстом.
   */
  private Text text = null;

  /**
   * В режиме {@link #isEditable()} = <code>true</code> целое отображается счётчиком.
   */
  Spinner spinner = null;

  Integer value = null;

  /**
   * Конструктор.
   *
   * @param aContext {@link ITsGuiContext} - контекст редактора
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public ValedIntegerSpinner( ITsGuiContext aContext ) {
    super( aContext );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void recreateWidgets() {
    boolean isTextOnly = text != null;
    boolean isNoWidgets = text == null && spinner == null;
    if( !isNoWidgets ) {
      if( isTextOnly != isEditable() ) {
        return; // не изменился isReadonly(), не надо пересоздавать виджеты
      }
    }
    backplane.setLayoutDeferred( true );
    try {
      if( text != null ) {
        text.dispose();
        text = null;
      }
      if( spinner != null ) {
        value = Integer.valueOf( spinner.getSelection() );
        spinner.dispose();
        spinner = null;
      }
      if( isEditable() ) {
        spinner = new Spinner( backplane, SWT.BORDER );
        updateSpinnerLimits();
        spinner.addModifyListener( aE -> {
          value = Integer.valueOf( spinner.getSelection() );
          fireModifyEvent( true );
        } );
        spinner.addFocusListener( notifyEditFinishedOnFocusLostListener );
      }
      else {
        text = new Text( backplane, SWT.BORDER );
        text.setEditable( false );
      }
      displayValue();
    }
    finally {
      backplane.setLayoutDeferred( false );
      backplane.getParent().layout( true );
      backplane.layout( true );
    }
  }

  private void updateSpinnerLimits() {
    TsInternalErrorRtException.checkNull( spinner );
    int minValue = getMinValue();
    int maxValue = getMaxValue();
    int step = getStep();
    int pageStep = getPageStep();
    if( maxValue <= minValue ) {
      if( minValue == Integer.MAX_VALUE ) {
        --minValue;
      }
      maxValue = minValue + 1;
      step = 1;
    }
    if( step < 1 ) {
      step = 1;
    }
    if( pageStep <= step ) {
      pageStep = 10 * step;
    }
    spinner.setMinimum( minValue );
    spinner.setMaximum( maxValue );
    spinner.setIncrement( step );
    spinner.setPageIncrement( pageStep );
  }

  private void displayValue() {
    if( text != null ) {
      if( value != null ) {
        text.setText( value.toString() );
      }
      else {
        text.setText( TsLibUtils.EMPTY_STRING );
      }
    }
    if( spinner != null ) {
      if( value != null ) {
        spinner.setSelection( value.intValue() );
      }
      else {
        int v = 0;
        if( v < getMinValue() ) {
          v = getMinValue();
        }
        if( v > getMaxValue() ) {
          v = getMaxValue();
        }
        spinner.setSelection( v );
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedControl
  //

  @Override
  public <X extends ITsContextRo> void onContextOpChanged( X aSource, String aId, IAtomicValue aValue ) {
    if( isWidget() ) {
      updateSpinnerLimits();
    }
  }

  @Override
  protected void doSetEditable( boolean aEditable ) {
    if( isWidget() ) {
      boolean wasEditable = text == null;
      if( wasEditable != isEditable() ) {
        recreateWidgets();
      }
    }
  }

  @Override
  protected Composite doCreateControl( Composite aParent ) {
    backplane = new Composite( aParent, SWT.NONE );
    backplane.setLayout( new FillLayout() );
    recreateWidgets();
    return backplane;
  }

  @Override
  protected Integer doGetUnvalidatedValue() {
    return value;
  }

  @Override
  protected void doSetUnvalidatedValue( Integer aValue ) {
    value = aValue;
    displayValue();
  }

  @Override
  protected void doClearValue() {
    value = null;
    displayValue();
  }

  // ------------------------------------------------------------------------------------
  // API класса
  //

  // TODO TRANSLATE

  /**
   * Возвращает значение параметра {@link #OPDEF_STEP}.
   *
   * @return int - значение параметра {@link #OPDEF_STEP}
   */
  public int getStep() {
    return params().getInt( OPDEF_STEP );
  }

  /**
   * Задает значение параметра {@link #OPDEF_STEP}.
   *
   * @param aValue int - значение параметра {@link #OPDEF_STEP}
   */
  public void setStep( int aValue ) {
    params().setInt( OPDEF_STEP, aValue );
  }

  /**
   * Возвращает значение параметра {@link #OPDEF_PAGE_STEP}.
   *
   * @return int - значение параметра {@link #OPDEF_PAGE_STEP}
   */
  public int getPageStep() {
    return params().getInt( OPDEF_PAGE_STEP );
  }

  /**
   * Задает значение параметра {@link #OPDEF_PAGE_STEP}.
   *
   * @param aValue int - значение параметра {@link #OPDEF_PAGE_STEP}
   */
  public void setPageStep( int aValue ) {
    params().setInt( OPDEF_PAGE_STEP, aValue );
  }

  /**
   * Возвращает значение параметра {@link IAvMetaConstants#TSID_MIN_INCLUSIVE}, или
   * {@link IAvMetaConstants#TSID_MIN_EXCLUSIVE} + 1.
   *
   * @return int - значение параметра
   */
  public int getMinValue() {
    int minValue = Integer.MIN_VALUE;
    if( params().hasValue( TSID_MIN_EXCLUSIVE ) ) {
      minValue = params().getInt( TSID_MIN_INCLUSIVE );
    }
    else {
      if( params().hasValue( TSID_MIN_EXCLUSIVE ) ) {
        minValue = params().getInt( TSID_MIN_EXCLUSIVE ) + 1;
      }
    }
    return minValue;
  }

  /**
   * Возвращает значение параметра {@link IAvMetaConstants#TSID_MAX_INCLUSIVE}, или
   * {@link IAvMetaConstants#TSID_MAX_EXCLUSIVE} - 1.
   *
   * @return int - значение параметра
   */
  public int getMaxValue() {
    int maxValue = Integer.MAX_VALUE;
    if( params().hasValue( TSID_MAX_INCLUSIVE ) ) {
      maxValue = params().getInt( TSID_MAX_INCLUSIVE );
    }
    else {
      if( params().hasValue( TSID_MAX_EXCLUSIVE ) ) {
        maxValue = params().getInt( TSID_MAX_EXCLUSIVE ) - 1;
      }
    }
    return maxValue;
  }

  /**
   * Задает параметры спиннера.
   *
   * @param aStep int - значение параметра {@link #OPDEF_STEP}
   * @param aPageStep int - значение параметра {@link #OPDEF_PAGE_STEP}
   * @param aMinValue int - значение параметра {@link IAvMetaConstants#TSID_MIN_INCLUSIVE}
   * @param aMaxValue int - значение параметра {@link IAvMetaConstants#TSID_MAX_INCLUSIVE}
   * @throws TsIllegalArgumentRtException aPageStep < aStep
   * @throws TsIllegalArgumentRtException aMaxValue < aMinValue
   */
  public void setLimits( int aStep, int aPageStep, int aMinValue, int aMaxValue ) {
    TsIllegalArgumentRtException.checkTrue( aPageStep < aStep );
    TsIllegalArgumentRtException.checkTrue( aMaxValue < aMinValue );
    IOptionSetEdit ops = new OptionSet();
    ops.setInt( OPDEF_STEP, aStep );
    ops.setInt( OPDEF_PAGE_STEP, aPageStep );
    ops.setInt( TSID_MIN_INCLUSIVE, aMinValue );
    ops.setInt( TSID_MAX_INCLUSIVE, aMaxValue );
    params().extendSet( ops );
  }

}

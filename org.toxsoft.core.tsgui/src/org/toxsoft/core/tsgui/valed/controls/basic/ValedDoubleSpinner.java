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
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Вещественный счётчик ({@link Spinner}).
 * <p>
 * В режиме {@link #isEditable()} = <code>false</code> виджет переключается в нередактируемый {@link Text}.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
public class ValedDoubleSpinner
    extends AbstractValedControl<Double, Composite> {

  /**
   * Максимальное количество знаков после запятой.
   * <p>
   * Это максимальное значение параметра {@link #OPDEF_FLOATING_DIGITS}.
   */
  public static final int MAX_FLOATING_DIGITS = 5;

  /**
   * Максимальный множитель преобразования double атомарно значения в int значение спинера.
   */
  private static final int MAX_FACTOR = 10 * 10 * 10 * 10 * 10; // 10 в степени MAX_FLOATING_DIGITS

  /**
   * Максимальное значение, которое может быть отображено данным контролем.
   */
  public static double MAX_AVC_VALUE = Integer.MAX_VALUE / MAX_FACTOR / 2;

  /**
   * Минимальное значение, которое может быть отображено данным контролем.
   */
  public static double MIN_AVC_VALUE = Integer.MIN_VALUE / MAX_FACTOR / 2;

  /**
   * ID of the {@link #OPDEF_FLOATING_DIGITS}.
   */
  public static final String OPID_FLOATING_DIGITS = VALED_OPID_PREFIX + ".DoubleSpinner.FloatingDigits"; //$NON-NLS-1$

  /**
   * ID of the {@link #OPDEF_STEP}.
   */
  public static final String OPID_STEP = VALED_OPID_PREFIX + ".DoubleSpinner.Step"; //$NON-NLS-1$

  /**
   * ID of the {@link #OPDEF_PAGE_STEP}.
   */
  public static final String OPID_PAGE_STEP = VALED_OPID_PREFIX + ".DoubleSpinner.PageStep"; //$NON-NLS-1$

  /**
   * Number of decimal digits after comma.
   */
  public static final IDataDef OPDEF_FLOATING_DIGITS = DataDef.create( OPID_FLOATING_DIGITS, INTEGER, //
      TSID_NAME, STR_N_DOUBLE_SPINNER_FLOATING_DIGITS, //
      TSID_DESCRIPTION, STR_D_DOUBLE_SPINNER_FLOATING_DIGITS, //
      TSID_DEFAULT_VALUE, avInt( 2 ) //
  );

  /**
   * Value change step by the arrow keys.
   */
  public static final IDataDef OPDEF_STEP = DataDef.create( OPID_STEP, FLOATING, //
      TSID_NAME, STR_N_DOUBLE_SPINNER_STEP, //
      TSID_DESCRIPTION, STR_D_DOUBLE_SPINNER_STEP, //
      TSID_DEFAULT_VALUE, AV_1 //
  );

  /**
   * Value change step by the PageUp/PageDown keys.
   */
  public static final IDataDef OPDEF_PAGE_STEP = DataDef.create( OPID_PAGE_STEP, FLOATING, //
      TSID_NAME, STR_N_DOUBLE_SPINNER_PAGE_STEP, //
      TSID_DESCRIPTION, STR_D_DOUBLE_SPINNER_PAGE_STEP, //
      TSID_DEFAULT_VALUE, avInt( 10 ) //
  );

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".DoubleSpinner"; //$NON-NLS-1$

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
    protected IValedControl<Double> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedDoubleSpinner( aContext );
    }

    @Override
    protected boolean isSuitableRawEditor( Class<?> aValueClass, ITsGuiContext aEditorContext ) {
      return aValueClass.equals( Double.class );
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
  private Spinner spinner = null;

  private Double value = Double.valueOf( 0.0 );

  /**
   * Конструктор.
   *
   * @param aContext {@link ITsGuiContext} - контекст редактора
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public ValedDoubleSpinner( ITsGuiContext aContext ) {
    super( aContext );
    setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_TRUE );
    setParamIfNull( OPDEF_VERTICAL_SPAN, AV_1 );
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  private double getFactor() {
    double factor = 1.0;
    for( int i = getFloatingDigits(); i > 0; i-- ) {
      factor *= 10;
    }
    return factor;
  }

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
        try {
          value = readDoubleFromSpinner();
        }
        catch( @SuppressWarnings( "unused" ) Exception ex ) {
          // значит, нет вещественного числа в счётчике, пусть останется последнее валидное значение
        }
        spinner.removeModifyListener( notificationModifyListener );
        spinner.dispose();
        spinner = null;
      }
      if( isEditable() ) {
        spinner = new Spinner( backplane, SWT.BORDER );
        updateSpinnerLimits();
        spinner.addModifyListener( notificationModifyListener );
        spinner.addSelectionListener( notificationSelectionListener );
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
    double factor = getFactor();
    // max value
    int hi = (int)(factor * MAX_AVC_VALUE);
    double m = getMaxValue();
    int mi = (int)(factor * m);
    hi = hi < mi ? hi : mi;
    // min value
    int lo = (int)(factor * MIN_AVC_VALUE);
    m = getMinValue();
    mi = (int)(factor * m);
    lo = lo > mi ? hi : mi;
    if( lo > hi ) {
      lo = hi - (int)(10 * factor);
    }
    spinner.setMaximum( hi );
    spinner.setMinimum( lo );
    int step = (int)(factor * getStep());
    int pageStep = (int)(factor * getPageStep());
    int delta = hi - lo;
    if( step > delta / 100 ) {
      step = delta / 100;
    }
    if( pageStep > delta / 10 ) {
      pageStep = delta / 10;
    }
    if( pageStep < step ) {
      pageStep = 2 * step;
    }
    spinner.setIncrement( step );
    spinner.setPageIncrement( pageStep );
    spinner.setDigits( getFloatingDigits() );
  }

  private void displayValue() {
    if( text != null ) {
      if( value != null ) {
        int digits = spinner.getDigits();
        String fmtStr = String.format( "%%0.%df", Integer.valueOf( digits ) );
        String s = String.format( fmtStr, value );
        text.setText( s );
      }
      else {
        text.setText( TsLibUtils.EMPTY_STRING );
      }
    }
    if( spinner != null ) {
      if( value != null ) {
        spinner.setSelection( (int)(value.doubleValue() * getFactor()) );
      }
      else {
        if( getMinValue() < 0.0 && getMaxValue() > 0.0 ) {
          spinner.setSelection( 0 );
        }
        else {
          spinner.setSelection( spinner.getMinimum() );
        }
      }
    }
  }

  /**
   * Считывает значение из счётчика.
   *
   * @return {@link Double} - считанное значение
   */
  private Double readDoubleFromSpinner() {
    TsInternalErrorRtException.checkNull( spinner );
    String s = spinner.getText();
    if( s.isEmpty() ) {
      return null;
    }
    int selection = spinner.getSelection();
    int digits = spinner.getDigits();
    return Double.valueOf( selection / Math.pow( 10, digits ) );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов AbstractDavControl
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
  public ValidationResult canGetValue() {
    try {
      if( readDoubleFromSpinner() != null ) {
        return ValidationResult.SUCCESS;
      }
    }
    catch( @SuppressWarnings( "unused" ) Exception ex ) {
      // invalide value in widget
    }
    return ValidationResult.error( FMT_ERR_INV_FLOATING_TEXT );
  }

  @Override
  protected Double doGetUnvalidatedValue() {
    value = readDoubleFromSpinner();
    return value;
  }

  @Override
  protected void doSetUnvalidatedValue( Double aValue ) {
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

  /**
   * Возвращает значение параметра {@link #OPDEF_FLOATING_DIGITS}.
   * <p>
   * Возвращается значение в пределах 0 .. {@link #MAX_FLOATING_DIGITS}.
   *
   * @return int - значение параметра {@link #OPDEF_FLOATING_DIGITS}
   */
  public int getFloatingDigits() {
    int v = params().getInt( OPDEF_FLOATING_DIGITS );
    if( v < 0 ) {
      return 0;
    }
    if( v > MAX_FLOATING_DIGITS ) {
      return MAX_FLOATING_DIGITS;
    }
    return v;
  }

  /**
   * Задает значение параметра {@link #OPDEF_FLOATING_DIGITS}.
   * <p>
   * Перед сохранением значение загоняется в пределы 0 .. {@link #MAX_FLOATING_DIGITS}.
   *
   * @param aValue int - значение параметра {@link #OPDEF_FLOATING_DIGITS}
   */
  public void setFloatingDigits( int aValue ) {
    int v = aValue;
    if( v < 0 ) {
      v = 0;
    }
    if( v > MAX_FLOATING_DIGITS ) {
      v = MAX_FLOATING_DIGITS;
    }
    params().setInt( OPDEF_FLOATING_DIGITS, v );
  }

  /**
   * Возвращает значение параметра {@link #OPDEF_STEP}.
   *
   * @return double - значение параметра {@link #OPDEF_STEP}
   */
  public double getStep() {
    return params().getFloat( OPDEF_STEP );
  }

  /**
   * Задает значение параметра {@link #OPDEF_STEP}.
   *
   * @param aValue double - значение параметра {@link #OPDEF_STEP}
   */
  public void setStep( double aValue ) {
    params().setDouble( OPDEF_STEP, aValue );
  }

  /**
   * Возвращает значение параметра {@link #OPDEF_PAGE_STEP}.
   *
   * @return double - значение параметра {@link #OPDEF_PAGE_STEP}
   */
  public double getPageStep() {
    return params().getFloat( OPDEF_PAGE_STEP );
  }

  /**
   * Задает значение параметра {@link #OPDEF_PAGE_STEP}.
   *
   * @param aValue double - значение параметра {@link #OPDEF_PAGE_STEP}
   */
  public void setPageStep( double aValue ) {
    params().setDouble( OPDEF_PAGE_STEP, aValue );
  }

  /**
   * Возвращает значение параметра {@link IAvMetaConstants#TSID_MIN_INCLUSIVE}, или
   * {@link IAvMetaConstants#TSID_MIN_EXCLUSIVE}.
   * <p>
   * Возвращаемое значение находится в пределах {@link #MIN_AVC_VALUE} .. {@link #MAX_AVC_VALUE} - 10.0.
   *
   * @return double - значение параметра
   */
  public double getMinValue() {
    double minValue = MIN_AVC_VALUE;
    if( tsContext().isSelfOption( TSID_MIN_INCLUSIVE ) ) {
      minValue = params().getFloat( TSID_MIN_INCLUSIVE );
    }
    else {
      if( tsContext().isSelfOption( TSID_MIN_EXCLUSIVE ) ) {
        minValue = params().getFloat( TSID_MIN_EXCLUSIVE ) + Double.MIN_NORMAL;
      }
    }
    if( minValue < MIN_AVC_VALUE ) {
      return MIN_AVC_VALUE;
    }
    if( minValue > MAX_AVC_VALUE - 10.0 ) {
      return MAX_AVC_VALUE - 10.0;
    }
    return minValue;
  }

  /**
   * Возвращает значение параметра {@link IAvMetaConstants#TSID_MAX_INCLUSIVE}, или
   * {@link IAvMetaConstants#TSID_MAX_EXCLUSIVE}.
   * <p>
   * Возвращаемое значение находится в пределах {@link #MIN_AVC_VALUE} + 10.0 .. {@link #MAX_AVC_VALUE}.
   *
   * @return double - значение параметра
   */
  public double getMaxValue() {
    double maxValue = MAX_AVC_VALUE;

    // TODO --- temporary code to catch AvTypeCastRtException
    // if( tsContext().isSelfOption( TSID_MAX_INCLUSIVE ) ) {
    if( params().hasKey( TSID_MAX_INCLUSIVE ) ) {
      // ---

      maxValue = params().getFloat( TSID_MAX_INCLUSIVE );
    }
    else {
      if( tsContext().isSelfOption( TSID_MAX_EXCLUSIVE ) ) {
        maxValue = params().getFloat( TSID_MAX_EXCLUSIVE ) - Double.MIN_NORMAL;
      }
    }
    if( maxValue < MIN_AVC_VALUE + 10.0 ) {
      return MIN_AVC_VALUE + 10.0;
    }
    if( maxValue > MAX_AVC_VALUE ) {
      return MAX_AVC_VALUE;
    }
    return maxValue;
  }

  /**
   * Задает параметры спиннера.
   *
   * @param aStep double - значение параметра {@link #OPDEF_STEP}
   * @param aPageStep double - значение параметра {@link #OPDEF_PAGE_STEP}
   * @param aMinValue double - значение параметра {@link IAvMetaConstants#TSID_MIN_INCLUSIVE}
   * @param aMaxValue double - значение параметра {@link IAvMetaConstants#TSID_MAX_INCLUSIVE}
   * @throws TsIllegalArgumentRtException aPageStep < aStep
   * @throws TsIllegalArgumentRtException aMaxValue < aMinValue
   */
  public void setLimits( double aStep, double aPageStep, double aMinValue, double aMaxValue ) {
    TsIllegalArgumentRtException.checkTrue( aPageStep < aStep );
    TsIllegalArgumentRtException.checkTrue( aMaxValue < aMinValue );
    IOptionSetEdit ops = new OptionSet();
    ops.setDouble( OPDEF_STEP, aStep );
    ops.setDouble( OPDEF_PAGE_STEP, aPageStep );
    ops.setDouble( TSID_MIN_INCLUSIVE, aMinValue );
    ops.setDouble( TSID_MAX_INCLUSIVE, aMaxValue );
    params().extendSet( ops );
  }

}

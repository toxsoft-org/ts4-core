package org.toxsoft.core.tsgui.widgets.mpv.impl;

import static org.toxsoft.core.tsgui.widgets.mpv.impl.ITsResources.*;

import org.eclipse.swt.widgets.Text;
import org.toxsoft.core.tsgui.widgets.mpv.IMultiPartValue;
import org.toxsoft.core.tsgui.widgets.mpv.IPart;
import org.toxsoft.core.tslib.bricks.events.change.GenericChangeEventer;
import org.toxsoft.core.tslib.bricks.strio.IStrioReader;
import org.toxsoft.core.tslib.bricks.strio.chario.impl.CharInputStreamString;
import org.toxsoft.core.tslib.bricks.strio.impl.StrioReader;
import org.toxsoft.core.tslib.bricks.validator.ValidationResult;
import org.toxsoft.core.tslib.bricks.validator.impl.TsValidationFailedRtException;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.IListEdit;
import org.toxsoft.core.tslib.coll.impl.ElemArrayList;
import org.toxsoft.core.tslib.coll.primtypes.IIntList;
import org.toxsoft.core.tslib.coll.primtypes.IIntListEdit;
import org.toxsoft.core.tslib.coll.primtypes.impl.IntArrayList;
import org.toxsoft.core.tslib.math.IntRange;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link IMultiPartValue} base implementation.
 *
 * @author hazard157
 */
public class MultiPartValueBase
    implements IMultiPartValue {

  private final GenericChangeEventer eventer;
  private final IListEdit<Part>      parts = new ElemArrayList<>();

  /**
   * Constructor.
   */
  public MultiPartValueBase() {
    eventer = new GenericChangeEventer( this );
  }

  /**
   * Constructor.
   *
   * @param aParts {@link IList}&lt;{@link Part}&gt; - the parts
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public MultiPartValueBase( IList<Part> aParts ) {
    eventer = new GenericChangeEventer( this );
    parts.setAll( aParts );
  }

  /**
   * Constructor.
   *
   * @param aParts {@link Part}[] - the parts
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public MultiPartValueBase( Part... aParts ) {
    eventer = new GenericChangeEventer( this );
    parts.setAll( aParts );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  @SuppressWarnings( "boxing" )
  ValidationResult canSetPartValue( int aPartIndex, int aValue ) {
    IPart p = parts.get( aPartIndex );
    int minValue = doGetPartMinValue( aPartIndex );
    int maxValue = doGetPartMaxValue( aPartIndex );
    if( aValue < minValue || aValue > maxValue ) {
      return ValidationResult.error( FMT_ERR_VALUE_OUT_OF_RANGE, aValue, p.name(), minValue, maxValue );
    }
    return ValidationResult.SUCCESS;
  }

  boolean internalValuesEqualsTo( IIntList aPartValues ) {
    if( parts.size() != aPartValues.size() ) {
      return false;
    }
    for( int i = 0, count = parts.size(); i < count; i++ ) {
      IPart p = parts.get( i );
      if( p.value() != aPartValues.getValue( i ) ) {
        return false;
      }
    }
    return true;
  }

  // ------------------------------------------------------------------------------------
  // API for subclasses
  //

  final protected Part getPart( int aIndex ) {
    return parts.get( aIndex );
  }

  final protected void addPart( Part aPart ) {
    parts.add( aPart );
  }

  final protected int pval( int aIndex ) {
    return getPart( aIndex ).value();
  }

  final protected void sval( int aIndex, int aValue ) {
    getPart( aIndex ).setValue( aValue );
  }

  // ------------------------------------------------------------------------------------
  // IMultiPartValue
  //

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  @Override
  public IList<IPart> parts() {
    return (IList)parts;
  }

  @Override
  public int getCharLength() {
    int len = 0;
    for( IPart p : parts ) {
      len += p.charsCount();
    }
    return len;
  }

  @Override
  public String getValueString() {
    StringBuilder sb = new StringBuilder();
    for( IPart p : parts ) {
      sb.append( p.partString() );
    }
    return sb.toString();
  }

  @Override
  public ValidationResult canSetValueString( String aValueString ) {
    IStrioReader sr = new StrioReader( new CharInputStreamString( aValueString ) );
    ValidationResult vr = ValidationResult.SUCCESS;
    // проверка формата
    for( Part p : parts ) {
      vr = ValidationResult.firstNonOk( vr, p.canReadValue( sr ) );
      if( vr.isError() ) {
        return vr;
      }
    }
    // проверка значений в частях
    sr = new StrioReader( new CharInputStreamString( aValueString ) );
    IIntListEdit partValues = new IntArrayList();
    for( int i = 0; i < parts.size(); i++ ) {
      Part p = parts.get( i );
      int v = p.readValue( sr );
      partValues.add( v );
      vr = ValidationResult.firstNonOk( vr, canSetPartValue( i, v ) );
      if( vr.isError() ) {
        return vr;
      }
    }
    vr = ValidationResult.firstNonOk( vr, doValidateValuesSet( partValues ) );
    return vr;
  }

  @Override
  public void setValueString( String aValueString ) {
    TsValidationFailedRtException.checkError( canSetValueString( aValueString ) );
    IIntList oldValues = getPartsValues();
    IStrioReader sr = new StrioReader( new CharInputStreamString( aValueString ) );
    for( int i = 0; i < parts.size(); i++ ) {
      Part p = parts.get( i );
      int v = p.readValue( sr );
      p.setValue( v );
    }
    // check if values really changed
    if( !internalValuesEqualsTo( oldValues ) ) {
      eventer.fireChangeEvent();
    }
  }

  @Override
  public void setPartValue( int aPartIndex, int aValue ) {
    TsValidationFailedRtException.checkError( canSetPartValue( aPartIndex, aValue ) );
    Part p = parts.get( aPartIndex );
    p.setValue( aValue );
  }

  @Override
  public void changePartValue( int aPartIndex, int aDelta, boolean aProcessOverflow ) {
    if( aDelta == 0 ) {
      return;
    }
    Part p = parts.get( aPartIndex );
    int newValue = p.value() + aDelta;
    int minValue = doGetPartMinValue( aPartIndex );
    int maxValue = doGetPartMaxValue( aPartIndex );
    int overflow = 0;
    if( newValue > maxValue ) {
      overflow = newValue - maxValue;
      newValue = maxValue;
    }
    if( newValue < minValue ) {
      overflow = newValue - minValue;
      newValue = minValue;
    }
    IIntList oldVals = getPartsValues();
    if( overflow == 0 || !aProcessOverflow ) {
      p.setValue( newValue );
    }
    else {
      doProcessOverflow( aPartIndex, newValue, overflow );
    }
    IIntList newVals = getPartsValues();
    if( !newVals.equals( oldVals ) ) {
      eventer.fireChangeEvent();
    }
  }

  /**
   * Returns the index of the part under caret position.
   * <p>
   * Caret position is defined as in {@link Text#getCaretPosition()}.
   *
   * @param aCaretPos int - caret position
   * @return int - part index or -1 if caret position is out of {@link IMultiPartValue} text
   */
  @Override
  public int indexOfPartByCaretPos( int aCaretPos ) {
    if( aCaretPos < 0 || aCaretPos > getCharLength() ) {
      return -1;
    }
    int rightPos = 0;
    // перебираем с второго по предпоследную часть. Все части кроме поледней НЕ включают в себя самую правую
    // позицию - он уже относится к следующей, только последняя часть включает в себя позицию ЗА последним символом
    for( int i = 1, count = parts.size(); i < count; i++ ) {
      rightPos = rightPos + parts.get( i - 1 ).charsCount();
      if( aCaretPos < rightPos ) {
        return i - 1;
      }
    }
    return parts.size() - 1;
  }

  @Override
  public IIntList getPartsValues() {
    IIntListEdit ll = new IntArrayList( parts.size() );
    for( IPart p : parts ) {
      ll.add( p.value() );
    }
    return ll;
  }

  @Override
  public GenericChangeEventer eventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * Subclass may redefine maximal allowed value for specific part.
   * <p>
   * Returned value must remain in range {@link IPart#range()}.
   * <p>
   * In base class simply returns higher limit {@link IntRange#maxValue()} of {@link IPart#range()}. Whether to call
   * superclass method from subclass depends on processing algorithm.
   * <p>
   * Method is useful to temporary change parts limits depending on other parts values. Eg for DAY_OF_MONTH field
   * maximal value may be 30 (instead of 31) depending on MONTH field, and 28 or 29 for February also considering YEAR
   * field. Other common use-case is setting time in specified interval like 13:20-15:45, where
   * {@link #doGetPartMaxValue(int)} and {@link #doGetPartMinValue(int)} fields are heavily used.
   *
   * @param aPartIndex int - the part index
   * @return int - maximal allowed valuu of part
   */
  protected int doGetPartMaxValue( int aPartIndex ) {
    return parts.get( aPartIndex ).range().maxValue();
  }

  /**
   * Subclass may redefine minimal allowed value for specific part.
   * <p>
   * Returned value must remain in range {@link IPart#range()}.
   * <p>
   * In base class simply returns lower limit {@link IntRange#minValue()} of {@link IPart#range()}. Whether to call
   * superclass method from subclass depends on processing algorithm.
   *
   * @param aPartIndex int - the part index
   * @return int - minimal allowed valuu of part
   */
  protected int doGetPartMinValue( int aPartIndex ) {
    return parts.get( aPartIndex ).range().minValue();
  }

  /**
   * Subclass may process part value change and overflow/underflow in {@link #changePartValue(int, int, boolean)}.
   * <p>
   * This method is called when part overflow/underflow really happens, that is <code>aOverflow</code> never is 0 and
   * when overflow processing is requested.
   * <p>
   * In the base class simply sets the parts value. Whether to call superclass method from subclass depends on
   * processing algorithm.
   * <p>
   * Subclass must not generate any event, event will be generated automaticaly.
   *
   * @param aPartIndex int - the index of the part
   * @param aNewValue int - calculated value to be set in part
   * @param aOverflow int - >0 is overflow amount (<0 underflow), never is 0
   */
  void doProcessOverflow( int aPartIndex, int aNewValue, int aOverflow ) {
    Part p = parts.get( aPartIndex );
    p.setValue( aNewValue );
  }

  /**
   * Subclass may perform additional check if values may be set to this MPV.
   * <p>
   * This method is called when trying to change value via {@link #setValueString(String)}.
   * <p>
   * Argument list size matches the {@link #parts()} list size.
   *
   * @param aPartValues {@link IIntList} - the probable parts values
   * @return {@link ValidationResult} - the validation result
   */
  protected ValidationResult doValidateValuesSet( IIntList aPartValues ) {
    return ValidationResult.SUCCESS;
  }

}

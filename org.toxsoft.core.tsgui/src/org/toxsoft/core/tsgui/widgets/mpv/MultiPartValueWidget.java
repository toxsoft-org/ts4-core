package org.toxsoft.core.tsgui.widgets.mpv;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.singlesrc.ISingleSourcing_MouseWheelListener;
import org.toxsoft.core.singlesrc.TsSinglesourcingUtils;
import org.toxsoft.core.tsgui.widgets.mpv.impl.VertTwoButtonsPane;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.validator.ValidationResult;
import org.toxsoft.core.tslib.coll.primtypes.IIntListEdit;
import org.toxsoft.core.tslib.coll.primtypes.impl.IntArrayList;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IMultiPartValue} editor, with "up" / "down" buttons on the right.
 * <p>
 * Warning: due to SWT {@link Text} widget restrictions this widget correctly works with mouse wheel events only when
 * fonts with equal-width digit glyphs. Font may contain variable width glyphs, the only requirement is the ASCII digits
 * 0-9 to have the same width.
 *
 * @author hazard157
 */
public class MultiPartValueWidget
    extends Composite
    implements IGenericChangeEventCapable {

  /**
   * This composite layout.
   *
   * @author hazard157
   */
  protected class SelfLayout
      extends Layout {

    public SelfLayout() {
      // nop
    }

    @Override
    protected Point computeSize( Composite aComposite, int aWHint, int aHHint, boolean aFlushCache ) {
      if( twoButtonsPane == null ) {
        return text.computeSize( SWT.DEFAULT, SWT.DEFAULT, true );
      }
      Point textSize = text.computeSize( SWT.DEFAULT, SWT.DEFAULT, true );
      Point rightSize = twoButtonsPane.computeSize( SWT.DEFAULT, textSize.y, true );
      // widget height is determined by the text field height
      // widget width is determined by the text field width and button pane width
      // ширина контроля - ширина текста + предпочтительная ширина панели кнопок
      return new Point( textSize.x + rightSize.x, textSize.y );
    }

    @Override
    protected void layout( Composite aComposite, boolean aFlushCache ) {
      Point textSize = text.computeSize( SWT.DEFAULT, SWT.DEFAULT, true );
      text.setBounds( new Rectangle( 0, 0, textSize.x, textSize.y ) );
      if( twoButtonsPane != null ) {
        Point rightSize = twoButtonsPane.computeSize( SWT.DEFAULT, textSize.y, true );
        twoButtonsPane.setBounds( new Rectangle( textSize.x, 0, rightSize.x, textSize.y ) );
      }
    }

  }

  private static final int INITIAL_ARROW_STEP = 1;
  private static final int INITIAL_PAGE_STEP  = 10;

  /**
   * Handling keyboard keys UP/DOWN/PAGE_UP/PAGE_DOWN.
   */
  private final Listener keyDownListener = new Listener() {

    @Override
    public void handleEvent( Event aEvent ) {
      if( !isEditable() ) {
        return;
      }
      int pos = text.getCaretPosition();
      switch( aEvent.keyCode ) {
        case SWT.ARROW_UP: {
          handleChangeRequest( +arrowStep, pos );
          break;
        }
        case SWT.ARROW_DOWN: {
          handleChangeRequest( -arrowStep, pos );
          break;
        }
        case SWT.PAGE_UP: {
          handleChangeRequest( +pageStep, pos );
          break;
        }
        case SWT.PAGE_DOWN: {
          handleChangeRequest( -pageStep, pos );
          break;
        }
        default:
          break;
      }
    }
  };

  /**
   * Mouse wheel scroll event handler.<br>
   * The same handler is registered for {@link #text} and {@link #twoButtonsPane} controls.
   */
  private final ISingleSourcing_MouseWheelListener mouseWheelListener = new ISingleSourcing_MouseWheelListener() {

    @Override
    public void mouseScrolled( MouseEvent aEvent ) {
      if( !isEditable() ) {
        return;
      }
      int pos;
      // we need to distinguish when mouse is over text field or buttons panew
      if( aEvent.widget == twoButtonsPane ) { // if on the buttons, text caret position determines part to change
        pos = text.getCaretPosition();
      }
      else { // if on the text field, mouse cursor position determines part to change
        pos = getCaretPositionAtCoor( aEvent.x );
      }
      if( pos < 0 ) {
        return;
      }
      int delta = -arrowStep;
      if( (aEvent.stateMask & SWT.CTRL) != 0 ) {
        delta = -pageStep;
      }
      if( aEvent.count > 0 ) { // scroll UP
        delta = -delta;
      }
      handleChangeRequest( delta, pos );
    }
  };

  /**
   * Mouse click listener for buttons pane control.
   */
  private final MouseListener twoButtonsMouseListener = new MouseAdapter() {

    @Override
    public void mouseDown( MouseEvent aEvent ) {
      if( !isEditable() ) {
        return;
      }
      int delta = arrowStep;
      if( (aEvent.stateMask & SWT.CTRL) != 0 ) {
        delta = pageStep;
      }
      int pos = text.getCaretPosition();
      switch( twoButtonsPane.whereIsPoint( aEvent.x, aEvent.y ) ) {
        case OUTSIDE:
          break;
        case UP:
          handleChangeRequest( +delta, pos );
          break;
        case DOWN:
          handleChangeRequest( -delta, pos );
          break;
        default:
          throw new TsNotAllEnumsUsedRtException();
      }
    }
  };

  /**
   * Handling text changes by any means (copy/paste, user keyboard input, etc).
   */
  private final Listener verifyListener = aEvent -> {
    if( !this.isVerifyIgnored && isEditable() ) {
      handleVerify( aEvent );
    }
  };

  private final IGenericChangeListener valueChangeListener = aSource -> updateOnMpvValueChange();

  private final GenericChangeEventer genericChangeEventer;
  private final IMultiPartValue      mpv;

  protected final Text     text;           // text field
  final VertTwoButtonsPane twoButtonsPane; // buttons on the right side

  /**
   * Flags that widget text is changing from inside this widget source code. <br>
   * The flag is <code>true</code> when text is changing by the code of this Java file so {@link #handleVerify(Event)}
   * will not be called.
   */
  boolean isVerifyIgnored = true;

  boolean editable = true;

  boolean processOverflow = true;

  private int arrowStep = INITIAL_ARROW_STEP;
  private int pageStep  = INITIAL_PAGE_STEP;

  // TODO TRANSLATE

  /**
   * Конструктор для наследников.
   * <p>
   * Кроме используемых родительским {@link Composite} битами стиля aStyle, использует следующие биты:
   * <ul>
   * <li>{@link SWT#READ_ONLY} - не позволяет редактировать значение;</li>
   * </ul>
   *
   * @param aParent {@link Composite} - родительская панель
   * @param aStyle int - стиль контроля, биты <code>SWT.XXX</code>, собранные по ИЛИ
   * @param aMpv {@link IMultiPartValue} - the edited value
   */
  public MultiPartValueWidget( Composite aParent, int aStyle, IMultiPartValue aMpv ) {
    super( aParent, aStyle );
    genericChangeEventer = new GenericChangeEventer( this );
    mpv = TsNullArgumentRtException.checkNull( aMpv );
    this.setLayout( new SelfLayout() );
    // text
    text = new Text( this, SWT.BORDER | SWT.SINGLE );
    // buttonPane
    if( !isReadOnly() ) {
      twoButtonsPane = new VertTwoButtonsPane( this );
      text.addListener( SWT.KeyDown, keyDownListener );
      text.addListener( SWT.Verify, verifyListener );
      twoButtonsPane.addMouseListener( twoButtonsMouseListener );
      TsSinglesourcingUtils.Control_addMouseWheelListener( text, mouseWheelListener );
      TsSinglesourcingUtils.Control_addMouseWheelListener( twoButtonsPane, mouseWheelListener );
    }
    else {
      twoButtonsPane = null;
    }
    editable = isReadOnly();
    text.setEditable( editable );
    text.setText( mpv.getValueString() );
    internalCalibrateCoorsToCaret();
    mpv.eventer().addListener( valueChangeListener );
    isVerifyIgnored = false;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  /**
   * Determines if widget was created in read-only mode.
   *
   * @return boolean - <code>true</code> if control was created with {@link SWT#READ_ONLY} bit
   */
  private boolean isReadOnly() {
    return (getStyle() & SWT.READ_ONLY) != 0;
  }

  /**
   * Contains X coordinates in pixels of left edge of each character in {@link #text}.
   * <p>
   * After calibration contains {@link Text#getText()} characters plus 1 values. The last value is <b>right</b> edge of
   * last character in {@link #text}.
   * <p>
   * If {@link Text#getText()} is an empty string, this list is empty.
   */
  private IIntListEdit charStartCoors = new IntArrayList();

  /**
   * Fills {@link #charStartCoors} list.
   * <p>
   * Note: calibration does not affect text in {@link #text} but resets selection and places caret at the end of text.
   */
  protected void internalCalibrateCoorsToCaret() {
    charStartCoors.clear();
    int count = text.getCharCount();
    for( int i = 0; i <= count; i++ ) {
      text.setSelection( i, i );
      int xCoor = TsSinglesourcingUtils.Text_getCaretPosition( text );
      charStartCoors.add( xCoor );
    }
  }

  /**
   * Determines position of caret as if mouse will be clicked at specified postion <b>inside</b> the text.
   * <p>
   * If argument is out of text (before first of after last char) returns -1. For empty {@link #text} returns -1.
   * <p>
   * Warning: {@link #charStartCoors} array <b>must</b> be calbrated before call to this method.
   *
   * @param aCoorX int - X coordinate in {@link #text} widget in pixels
   * @return int - caret position (as defined by {@link Text#getCaretPosition()}) or -1
   */
  int getCaretPositionAtCoor( int aCoorX ) {
    if( charStartCoors.isEmpty() || (aCoorX < charStartCoors.getValue( 0 )) ) {
      return -1;
    }
    for( int i = 1; i < charStartCoors.size(); i++ ) {
      if( aCoorX < charStartCoors.getValue( i ) ) {
        return i - 1;
      }
    }
    return -1;
  }

  /**
   * Finds non-last part index only if caret is after digits part.
   * <p>
   * Never returns last part. Caret is afetr digits means that it is before {@link IPart#charAfter()} or at the end of
   * the part. End of the part is also start of the next part, but in this method end of the part heas priority over
   * start of next part.
   *
   * @param aCaretPos int - caret position as in {@link Text#getCaretPosition()}
   * @return int part index or -1
   */
  private int indexOfPartIfCaretPosIsAtEnd( int aCaretPos ) {
    if( aCaretPos <= 0 || aCaretPos >= mpv.getCharLength() ) {
      return -1;
    }
    int afterPartPos = 0;
    for( int i = 0, count = mpv.parts().size() - 1; i < count; i++ ) {
      IPart p = mpv.parts().get( i );
      afterPartPos += p.charsCount();
      if( aCaretPos == afterPartPos ) {
        return i;
      }
      if( p.charAfter() != null && aCaretPos == (afterPartPos - 1) ) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Проверяет корректность всех редактирований текста в виджете {@link #text}.
   *
   * @param aEvent {@link Event} - событие редактирования
   */
  void handleVerify( Event aEvent ) {
    // указываем SWT, чтобы он ничего не делал в результате это правки - мы сами все сделаем
    aEvent.doit = false;
    // если текст поступил за пределами представления MPV, то игнорируем его
    int start = aEvent.start;
    if( start >= mpv.getCharLength() ) {
      return;
    }
    // измененноый текст, который поступил в событии
    String newText = aEvent.text;
    int length = newText.length();
    // теперь сфоримруем отредактированный текст, и посмотрим, правильный ли он
    StringBuilder sb = new StringBuilder( text.getText() );
    int end = aEvent.start + length;
    sb.replace( aEvent.start, end, newText );
    ValidationResult vr = mpv.canSetValueString( sb.toString() );
    // если правильный, то изменим значение
    if( !vr.isError() ) {
      mpv.setValueString( sb.toString() );
      int newPos = end;
      /**
       * при успешном вводе последей цифры (возможно, вместе с afterChar) в цифровую часть - перескочить в первую
       * позицию следующей части (а для последней части - остаться на месте).
       */
      int partIndex = indexOfPartIfCaretPosIsAtEnd( newPos );
      if( partIndex >= 0 ) {
        // посчитаем позицию начала части, куда надо перепигнуть
        int nextPartStartPos = 0;
        for( int i = 0; i <= partIndex; i++ ) {
          nextPartStartPos += mpv.parts().get( i ).charsCount();
        }
        // пропустим начальный символ, если от есть
        if( mpv.parts().get( partIndex + 1 ).charBefore() != null ) {
          ++nextPartStartPos;
        }
        newPos = nextPartStartPos;
      }
      text.setSelection( newPos, newPos );
    }
  }

  /**
   * Handles value change request at specified caret position.
   * <p>
   * Is called when user presses UP/DOWN/PG_UP/PG_DN keys mouse wheel scroll and mouse click up right-side buttons.
   *
   * @param aDelta int - requested change amount
   * @param aCaretPosition int - caret position (as defined by {@link Text#getCaretPosition()})
   */
  void handleChangeRequest( int aDelta, int aCaretPosition ) {
    int partIndex = mpv().indexOfPartByCaretPos( aCaretPosition );
    if( partIndex < 0 ) {
      return;
    }
    mpv().changePartValue( partIndex, aDelta, processOverflow );
  }

  /**
   * Updates text in widget from {@link IMultiPartValue#getValueString()}.
   * <p>
   * Is called from {@link IMultiPartValue#eventer()} listener.
   */
  void updateOnMpvValueChange() {
    int pos = text.getCaretPosition();
    isVerifyIgnored = true;
    text.setText( mpv.getValueString() );
    text.setSelection( pos, pos );
    isVerifyIgnored = false;
    genericChangeEventer.fireChangeEvent();
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventer
  //

  @Override
  public GenericChangeEventer genericChangeEventer() {
    return genericChangeEventer;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the edited value.
   *
   * @return {@link IMultiPartValue} - the edited value
   */
  public IMultiPartValue mpv() {
    return mpv;
  }

  /**
   * Returns the arrow step - change amount on arrow up/down, mouse wheel and mouse click events.
   *
   * @return int - the arrow step
   */
  public int getArrowStep() {
    return arrowStep;
  }

  /**
   * Returns the page step - change amount on page up/down, Ctrl + mouse wheel and Ctrl + mouse click events.
   *
   * @return int the page step
   */
  public int getPageStep() {
    return pageStep;
  }

  /**
   * Sets the value change step values.
   *
   * @param aArrowStep int - change amount on arrow up/down, mouse wheel and mouse click events
   * @param aPageStep int - change amount on page up/down, Ctrl + mouse wheel and Ctrl + mouse click events
   * @throws TsIllegalArgumentRtException aArrowStep < 1
   * @throws TsIllegalArgumentRtException aPageStep < aArrowStep
   */
  public void setSteps( int aArrowStep, int aPageStep ) {
    TsIllegalArgumentRtException.checkTrue( aArrowStep < 1 );
    TsIllegalArgumentRtException.checkTrue( aPageStep < aArrowStep );
    arrowStep = aArrowStep;
    pageStep = aPageStep;
  }

  /**
   * Determines if value editing is allowed.
   * <p>
   * If control was created in uneditable mode (with {@link SWT#READ_ONLY} bit) this method returns only
   * <code>false</code>.
   *
   * @return boolean - permission to edit value
   */
  public boolean isEditable() {
    return !isReadOnly() && editable;
  }

  /**
   * Changes permission to edit value.
   * <p>
   * Note that only widget becames uneditable and value can not be changed by the GUI user. Programatical editing of
   * {@link #mpv()} is always allowed.
   * <p>
   * If control was created in uneditable mode (with {@link SWT#READ_ONLY} bit) this method has no effect.
   *
   * @param aEditable boolean - permission to edit value
   */
  public void setEditable( boolean aEditable ) {
    if( !isReadOnly() ) {
      editable = aEditable;
      text.setEditable( aEditable );
      // TODO refresh
    }
  }

  /**
   * Determines if overflow/underflow will be processed.
   * <p>
   * TODO what is overflow processing
   * <p>
   * Initially overflow processing is off.
   *
   * @return boolean - value will be passed to {@link IMultiPartValue#changePartValue(int, int, boolean)}
   */
  public boolean isOverflowProcessing() {
    return processOverflow;
  }

  /**
   * Sets overflow/underflow processing on/off.
   *
   * @param aOn boolean overflow processing
   */
  public void setOverflowProcessing( boolean aOn ) {
    processOverflow = aOn;
  }

}

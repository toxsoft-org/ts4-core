package org.toxsoft.core.tsgui.ved.screen.impl;

import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;

import org.eclipse.swt.*;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.ved.comps.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * Обработчик взаимодействия с пользователем для поля ввода.
 * <p>
 *
 * @author vs
 */
public class InputFieldHandler
    implements ITsUserInputListener {

  private static Clipboard clipboard;

  private final VedAbstractVisel visel;

  private final VedScreen vedScreen;

  private boolean editing = false;

  private int caretPos = 0;

  /**
   * Конструктор.
   *
   * @param aVedScreen VedScreen - экран редактора
   * @param aVisel VedAbstractVisel - визуальный элемент, отображающий текст
   */
  public InputFieldHandler( VedScreen aVedScreen, VedAbstractVisel aVisel ) {
    vedScreen = aVedScreen;
    visel = aVisel;
    clipboard = new Clipboard( aVedScreen.getDisplay() );
  }

  // ------------------------------------------------------------------------------------
  // ITsUserInputListener
  //

  @Override
  public boolean onMouseDown( Object aSource, ETsMouseButton aButton, int aState, ITsPoint aCoors, Control aWidget ) {
    if( aButton == ETsMouseButton.LEFT && aState == 0 ) {
      if( isViselPointed( aCoors ) ) {
        if( editing ) {
          visel.props().setValobj( ViselLabel.PROPID_SELECTION, ITsPoint.ZERO );
          ID2Point p = vedScreen.view().coorsConverter().swt2Visel( aCoors, visel );
          caretPos = ((ViselLabel)visel).findCaretPos( (int)p.x() );
          visel.props().setInt( PROPID_CARET_POS, caretPos );
        }
        else {
          editing = true;
        }
        return true;
      }
    }
    if( visel.props().getValobj( ViselLabel.PROPID_SELECTION ) != ITsPoint.ZERO ) {
      visel.props().setValobj( ViselLabel.PROPID_SELECTION, ITsPoint.ZERO );
    }
    editing = false;
    return false;
  }

  @Override
  public boolean onMouseDoubleClick( Object aSource, ETsMouseButton aButton, int aState, ITsPoint aCoors,
      Control aWidget ) {
    if( aButton == ETsMouseButton.LEFT && aState == 0 ) {
      if( isViselPointed( aCoors ) ) {
        String text = visel.props().getStr( PROPID_TEXT );
        visel.props().setValobj( ViselLabel.PROPID_SELECTION, new TsPoint( 0, text.length() ) );
        caretPos = text.length();
        visel.props().setInt( PROPID_CARET_POS, caretPos );
        return true;
      }
    }
    return false;
  }

  static class InputFieldDragInfo {

    private final TsPointEdit selection = new TsPointEdit( 0, 0 );

    InputFieldDragInfo( int aStartSelectionIndex ) {
      selection.setX( aStartSelectionIndex );
    }

    ITsPoint selection( int aSelectionIndex ) {
      if( selection.x() == aSelectionIndex ) {
        return ITsPoint.ZERO;
      }
      selection.setY( aSelectionIndex );
      return selection;
    }
  }

  boolean dragging = false;

  @Override
  public boolean onMouseDragStart( Object aSource, DragOperationInfo aDragInfo ) {
    if( aDragInfo.button() == ETsMouseButton.LEFT ) { // && aDragInfo.startingState() == 0 ) {
      if( isViselPointed( aDragInfo.startingPoint() ) ) {
        ID2Point p = vedScreen.view().coorsConverter().swt2Visel( aDragInfo.startingPoint(), visel );
        caretPos = ((ViselLabel)visel).findCaretPos( (int)p.x() );
        InputFieldDragInfo di = new InputFieldDragInfo( caretPos );
        aDragInfo.setCargo( di );
        dragging = true;
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean onMouseDragMove( Object aSource, DragOperationInfo aDragInfo, int aState, ITsPoint aCoors ) {
    if( dragging ) {
      ID2Point p = vedScreen.view().coorsConverter().swt2Visel( aCoors, visel );
      caretPos = ((ViselLabel)visel).findCaretPos( (int)p.x() );
      ITsPoint sel = ((InputFieldDragInfo)aDragInfo.cargo()).selection( caretPos );
      visel.props().setInt( PROP_CARET_POS, caretPos );
      visel.props().setValobj( ViselLabel.PROPID_SELECTION, sel );
    }
    return false;
  }

  @Override
  public boolean onMouseDragFinish( Object aSource, DragOperationInfo aDragInfo, int aState, ITsPoint aCoors ) {
    dragging = false;
    return false;
  }

  @Override
  public boolean onMouseDragCancel( Object aSource, DragOperationInfo aDragInfo ) {
    dragging = false;
    return false;
  }

  @Override
  public boolean onKeyDown( Object aSource, int aCode, char aChar, int aState ) {
    if( visel != null ) {
      if( editing ) {
        String text = visel.props().getStr( PROPID_TEXT );
        switch( aCode ) {
          case SWT.ARROW_LEFT:
            processLeftArrow( aState );
            return true;
          case SWT.ARROW_RIGHT:
            processRightArrow( aState );
            return true;
          case SWT.DEL:
            ITsPoint sel = visel.props().getValobj( ViselLabel.PROPID_SELECTION );
            if( sel == ITsPoint.ZERO ) {
              if( caretPos < text.length() ) {
                String str1 = text.substring( 0, caretPos );
                String str2 = text.substring( caretPos + 1 );
                visel.props().setStr( PROPID_TEXT, str1 + str2 );
              }
            }
            else {
              deleteSelectedTextPart( text, sel );
            }
            return true;
          default:
            break;
        }

        if( (aState & SWT.MODIFIER_MASK) == SWT.CTRL ) {
          if( aCode == 99 ) { // key code for symbol "C"
            doCopy();
            return true;
          }
          if( aCode == 118 ) { // key code for symbol "V"
            doPaste();
            return true;
          }
        }

        StringBuilder sb = new StringBuilder( text );
        if( aChar == SWT.BS ) {
          if( caretPos > 0 && text.length() > 0 ) {
            String str1 = text.substring( 0, caretPos - 1 );
            String str2 = TsLibUtils.EMPTY_STRING;
            if( caretPos < text.length() - 1 ) {
              str2 = text.substring( caretPos );
            }
            caretPos--;
            visel.props().setInt( PROPID_CARET_POS, caretPos );
            visel.props().setStr( PROPID_TEXT, str1 + str2 );
          }
          return true;
        }

        // System.out.println( "char = " + (int)aChar );
        if( aChar > 31 ) {
          sb.insert( caretPos, aChar );
          caretPos++;
          visel.props().setInt( PROPID_CARET_POS, caretPos );
          visel.props().setStr( PROPID_TEXT, sb.toString() );
          return true;
        }
      }
    }
    return false;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает положение текстового курсора.
   *
   * @return int - положение текстового курсора как индекс символа
   */
  public int caretPos() {
    return caretPos;
  }

  /**
   * Возвращает признак того находится ли поле ввода в режиме редактирования.
   *
   * @return <b>true</b> - поле редактируется (нужно рисовать курсор)<br>
   *         false<b></b> - поле неактивно (курсор рисовать не нужно)
   */
  public boolean isEditing() {
    return editing;
  }

  /**
   * Устанавливает режим редактирования для поля ввода.
   *
   * @param aEditing boolean - режим редактирования для поля ввода
   */
  public void setEditing( boolean aEditing ) {
    editing = aEditing;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private boolean isViselPointed( ITsPoint aCoors ) {
    if( visel != null ) {
      ID2Point p = vedScreen.view().coorsConverter().swt2Visel( aCoors, visel );
      if( visel.isYours( p ) ) {
        return true;
      }
    }
    return false;
  }

  private ITsPoint selection() {
    if( visel != null ) {
      return visel.props().getValobj( ViselLabel.PROPID_SELECTION );
    }
    return ITsPoint.ZERO;
  }

  private void processLeftArrow( int aState ) {
    if( aState == 0 ) {
      if( selection() != ITsPoint.ZERO ) {
        visel.props().setValobj( ViselLabel.PROPID_SELECTION, ITsPoint.ZERO );
      }
      caretPos--;
      if( caretPos < 0 ) {
        caretPos = 0;
      }
      visel.props().setInt( PROPID_CARET_POS, caretPos );
    }
    if( aState == SWT.SHIFT ) {
      int oldPos = caretPos;
      caretPos--;
      if( caretPos < 0 ) {
        caretPos = 0;
      }
      visel.props().setInt( PROPID_CARET_POS, caretPos );
      processArrowSelection( oldPos, caretPos );
    }

  }

  private void processRightArrow( int aState ) {
    if( aState == 0 ) {
      if( selection() != ITsPoint.ZERO ) {
        visel.props().setValobj( ViselLabel.PROPID_SELECTION, ITsPoint.ZERO );
      }
      caretPos++;
      String text = visel.props().getStr( PROP_TEXT );
      if( caretPos > text.length() ) {
        caretPos = text.length();
      }
      visel.props().setInt( PROPID_CARET_POS, caretPos );
    }
    if( aState == SWT.SHIFT ) {
      int oldPos = caretPos;
      caretPos++;
      String text = visel.props().getStr( PROP_TEXT );
      if( caretPos > text.length() ) {
        caretPos = text.length();
      }
      visel.props().setInt( PROPID_CARET_POS, caretPos );
      processArrowSelection( oldPos, caretPos );
    }

  }

  private void processArrowSelection( int aOldPos, int aNewPos ) {
    ITsPoint sel = selection();
    int idx1 = 0;
    if( sel.x() != aOldPos ) {
      idx1 = sel.x();
    }
    if( sel.y() != aOldPos ) {
      idx1 = sel.y();
    }
    if( sel == ITsPoint.ZERO ) {
      idx1 = aOldPos;
    }
    if( idx1 == aNewPos ) {
      visel.props().setValobj( ViselLabel.PROPID_SELECTION, ITsPoint.ZERO );
      return;
    }
    visel.props().setValobj( ViselLabel.PROPID_SELECTION, new TsPoint( idx1, aNewPos ) );
  }

  private void doCopy() {
    // write data to Clipboard
    ITsPoint sel = visel.props().getValobj( ViselLabel.PROPID_SELECTION );
    if( sel != ITsPoint.ZERO ) {
      String text = visel.props().getStr( PROPID_TEXT );
      String subStr = text.substring( Math.min( sel.x(), sel.y() ), Math.max( sel.x(), sel.y() ) );

      TextTransfer textTransfer = TextTransfer.getInstance();
      clipboard.setContents( new Object[] { subStr }, new Transfer[] { textTransfer } );
    }
  }

  private void doPaste() {
    TextTransfer transfer = TextTransfer.getInstance();
    String data = (String)clipboard.getContents( transfer );
    if( data != null ) {
      ITsPoint sel = visel.props().getValobj( ViselLabel.PROPID_SELECTION );
      String text = visel.props().getStr( PROP_TEXT );
      deleteSelectedTextPart( text, sel );
      String str1 = text.substring( 0, Math.min( sel.x(), sel.y() ) );
      String str2 = text.substring( Math.max( sel.x(), sel.y() ) );
      visel.props().setStr( PROP_TEXT, str1 + data + str2 );
      caretPos = (str1 + data).length();
      visel.props().setInt( PROP_CARET_POS, caretPos );
    }
  }

  private void deleteSelectedTextPart( String aText, ITsPoint aSelection ) {
    String str1 = aText.substring( 0, Math.min( aSelection.x(), aSelection.y() ) );
    String str2 = aText.substring( Math.max( aSelection.x(), aSelection.y() ) );
    visel.props().setStr( PROPID_TEXT, str1 + str2 );
    visel.props().setValobj( ViselLabel.PROPID_SELECTION, ITsPoint.ZERO );
    caretPos = str1.length();
    visel.props().setInt( PROPID_CARET_POS, str1.length() );
  }
}

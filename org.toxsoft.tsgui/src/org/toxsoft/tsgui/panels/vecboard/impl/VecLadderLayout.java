package org.toxsoft.tsgui.panels.vecboard.impl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.toxsoft.tsgui.graphics.EVerAlignment;
import org.toxsoft.tsgui.panels.vecboard.*;
import org.toxsoft.tsgui.utils.layout.BorderLayout;

/**
 * Реализация раскладки {@link IVecLadderLayout}.
 *
 * @author goga
 */
public class VecLadderLayout
    extends AbstractVecLayout<IVecLadderLayoutData>
    implements IVecLadderLayout {

  private final boolean isLabelsShown;

  /**
   * Создает раскладку с указанными параметрами.
   *
   * @param aIsLabelsShown boolean - признак показа подписей к полям ввода
   */
  public VecLadderLayout( boolean aIsLabelsShown ) {
    isLabelsShown = aIsLabelsShown;
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  private void fillNoLabels( Composite aParent ) {
    aParent.setLayout( new GridLayout( 1, false ) );
    for( int i = 0, n = items().size(); i < n; i++ ) {
      Item<IVecLadderLayoutData> item = items().get( i );
      if( item.cb() == null ) { // нет контроля - идем дальше
        continue;
      }
      Control c = item.cb().createControl( aParent );
      IVecLadderLayoutData ld = item.layoutData();
      c.setToolTipText( ld.tooltip() );
      int verSpan = ld.verticalSpan();
      boolean isLastItem = (i == n - 1);
      if( isLastItem && verSpan > 1 ) { // последний многострочный элемент делаем вертикально растяжимым
        c.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true, 1, verSpan ) );
      }
      else {
        c.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false, 1, verSpan ) );
      }
    }
  }

  static GridData gd( IVecLadderLayoutData aLayoutData, boolean aIsSingleInRow, int aSpanHeightInPixels ) {
    int horAlign = aLayoutData.horAlignment().swtStyle();
    int verAlign = aLayoutData.verAlignment().swtStyle();
    int horSpan = aIsSingleInRow ? 2 : 1;
    boolean isVerGrab = aLayoutData.verticalSpan() > 1 && aLayoutData.verAlignment() == EVerAlignment.FILL;
    GridData gd = new GridData( horAlign, verAlign, true, isVerGrab, horSpan, aLayoutData.verticalSpan() );
    if( aLayoutData.verticalSpan() > 1 ) {
      gd.heightHint = aLayoutData.verticalSpan() * aSpanHeightInPixels;
    }
    return gd;
  }

  private void fillWithLabels( Composite aParent ) {
    aParent.setLayout( new GridLayout( 2, false ) );
    // вычислим высоту одного SPAN в пикселях
    FontData fontData = aParent.getFont().getFontData()[0];
    int spanHeight = 2 * fontData.getHeight();
    /**
     * В дальнейшем настройка высоты строк сетки размеки происходит следующим образом:<br>
     * если предолагаемая высота 1 SPAN, то используем высоту по умолчанию;<br>
     * если 2 или более SPAN-ов, то задаем высоту с помощью GridData.heightHint<br>
     */
    // пройдем по всем контролям
    for( int i = 0, n = items().size(); i < n; i++ ) {
      Item<IVecLadderLayoutData> item = items().get( i );
      if( item.cb() == null ) { // нет контроля - идем дальше
        continue;
      }
      IVecLadderLayoutData ld = item.layoutData();
      // boolean isLastControl = (i == (n - 1));
      if( !ld.isLabelShown() ) { // нет подписи - расположим во всю ширину строки
        Control c = item.cb().createControl( aParent );
        c.setLayoutData( gd( ld, true, spanHeight ) );
        c.setToolTipText( ld.tooltip() );
        continue;
      }
      // отображение с подписью
      if( ld.isFullWidthControl() ) { // подпись сверху - контроль ведь во всю ширину
        Composite container = new Composite( aParent, SWT.NONE );
        container.setLayout( new BorderLayout() );
        Label l = new Label( container, SWT.LEFT );
        l.setText( ld.labelText() );
        l.setToolTipText( ld.tooltip() );
        l.setLayoutData( BorderLayout.NORTH );
        Control c = item.cb().createControl( container );
        c.setToolTipText( ld.tooltip() );
        c.setLayoutData( BorderLayout.CENTER );
        container.setLayoutData( gd( ld, true, spanHeight ) );
      }
      else { // подпись слева - контроль справа
        Label l = new Label( aParent, SWT.NONE );
        l.setText( ld.labelText() );
        l.setToolTipText( ld.tooltip() );
        // GOGA 06.07.2015
        // int labelVerAlign = SWT.CENTER;
        // if( ld.verticalSpan() > 1 ) {
        // labelVerAlign = SWT.TOP;
        // }
        // boolean isVerGrab = ld.verticalSpan() > 1;
        GridData labelGd = new GridData( SWT.FILL, SWT.CENTER, false, false, 1, ld.verticalSpan() );
        l.setLayoutData( labelGd );
        Control c = item.cb().createControl( aParent );
        c.setToolTipText( ld.tooltip() );
        c.setLayoutData( gd( ld, false, spanHeight ) );
      }
    }
  }

  // static GridData gd( IVecLadderLayoutData aLayoutData, boolean aIsLastControl, boolean aIsSingleInRow ) {
  // int horAlign = aLayoutData.horAlignment().swtStyle();
  // int verAlign = aLayoutData.verAlignment().swtStyle();
  // boolean isVerGrab = aIsLastControl;
  // int horSpan = 1;
  // if( aIsSingleInRow ) {
  // horSpan = 2;
  // }
  // return new GridData( horAlign, verAlign, true, isVerGrab, horSpan, aLayoutData.verticalSpan() );
  // }
  //
  // private void fillWithLabels( Composite aParent ) {
  // aParent.setLayout( new GridLayout( 2, false ) );
  // for( int i = 0, n = items().size(); i < n; i++ ) {
  // Item<IVecLadderLayoutData> item = items().get( i );
  // if( item.cb() == null ) { // нет контроля - идем дальше
  // continue;
  // }
  // IVecLadderLayoutData ld = item.layoutData();
  // boolean isLastControl = (i == (n - 1));
  // if( !ld.isLabelShown() ) { // нет подписи - расположим во всю ширину строки
  // Control c = item.cb().createControl( aParent );
  // c.setLayoutData( gd( ld, isLastControl, true ) );
  // continue;
  // }
  // // отображение с подписью
  // if( ld.isFullWidthControl() ) { // подпись сверху - контроль ведь во всю ширину
  // Composite container = new Composite( aParent, SWT.NONE );
  // container.setLayout( new BorderLayout() );
  // Label l = new Label( container, SWT.LEFT );
  // l.setText( ld.labelText() );
  // l.setLayoutData( BorderLayout.NORTH );
  // Control c = item.cb().createControl( container );
  // c.setLayoutData( BorderLayout.CENTER );
  // container.setLayoutData( gd( ld, isLastControl, true ) );
  // }
  // else { // подпись слева - контроль справа
  // Label l = new Label( aParent, SWT.NONE );
  // l.setText( ld.labelText() );
  // l.setLayoutData( new GridData( SWT.FILL, SWT.TOP, false, false, 1, 1 ) );
  // Control c = item.cb().createControl( aParent );
  // c.setLayoutData( gd( ld, isLastControl, false ) );
  // }
  // }
  // }

  // ------------------------------------------------------------------------------------
  // Реализация методов AbstractVecLayout
  //

  @Override
  protected void fillComposite( Composite aParent ) {
    if( isLabelsShown ) {
      fillWithLabels( aParent );
    }
    else {
      fillNoLabels( aParent );
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ILayout
  //

  @Override
  public EVecLayoutKind layoutKind() {
    return EVecLayoutKind.LADDER;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ILadderLayout
  //

  @Override
  public boolean isLabelsShown() {
    return isLabelsShown;
  }

}

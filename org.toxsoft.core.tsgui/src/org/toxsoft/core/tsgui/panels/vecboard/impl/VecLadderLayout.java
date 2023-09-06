package org.toxsoft.core.tsgui.panels.vecboard.impl;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.panels.vecboard.*;
import org.toxsoft.core.tsgui.utils.layout.BorderLayout;

/**
 * {@link IVecLadderLayout} implementation.
 *
 * @author hazard157
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
  // Implementation
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
    // calculate height of one SPAN ion pixels
    FontData fontData = aParent.getFont().getFontData()[0];
    int spanHeight = 2 * fontData.getHeight();
    // iterate over all items on the layout
    for( int i = 0, n = items().size(); i < n; i++ ) {
      Item<IVecLadderLayoutData> item = items().get( i );
      if( item.cb() == null ) { // bypass non-control item
        continue;
      }
      IVecLadderLayoutData ld = item.layoutData();
      // boolean isLastControl = (i == (n - 1));
      // no label, VALED has full width (2 columns)
      if( !ld.isLabelShown() ) {
        Control c = item.cb().createControl( aParent );
        c.setLayoutData( gd( ld, true, spanHeight ) );
        c.setToolTipText( ld.tooltip() );
        continue;
      }
      // here we have to setup VALED with label
      if( ld.isFullWidthControl() ) { // label -> north side, VALED -> south, full width (2 columns)
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
      else { // label -> left, VALED -> right side
        Label l = new Label( aParent, SWT.NONE );
        l.setText( ld.labelText() );
        l.setToolTipText( ld.tooltip() );
        GridData labelGd = new GridData( SWT.FILL, SWT.CENTER, false, false, 1, ld.verticalSpan() );
        l.setLayoutData( labelGd );
        Control c = item.cb().createControl( aParent );
        c.setToolTipText( ld.tooltip() );
        c.setLayoutData( gd( ld, false, spanHeight ) );
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // AbstractVecLayout
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
  // ILayout
  //

  @Override
  public EVecLayoutKind layoutKind() {
    return EVecLayoutKind.LADDER;
  }

  // ------------------------------------------------------------------------------------
  // ILadderLayout
  //

  @Override
  public boolean isLabelsShown() {
    return isLabelsShown;
  }

}

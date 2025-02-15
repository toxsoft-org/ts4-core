package org.toxsoft.core.tsgui.valed.controls.graphics;

import static org.toxsoft.core.tsgui.valed.controls.graphics.ITsResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Панель редактирования параметров "линейной" заливки.
 *
 * @author vs
 */
public class PanelLinearGradientSelector
    extends AbstractPanelGradientSelector {

  ResultPanel resultPanel;

  class ResultPanel
      extends TsPanel {

    PaintListener paintListener = aE -> {

      Rectangle r = clientRect();

      int x = 0;
      int y = 0;
      int colorIdx = 0;
      Color[] c = { colorManager().getColor( ETsColor.GRAY ), colorManager().getColor( ETsColor.DARK_GRAY ) };
      while( x <= r.width ) {
        while( y <= r.height ) {
          aE.gc.setBackground( c[colorIdx] );
          aE.gc.fillRectangle( r.x + x, r.y + y, 16, 16 );
          colorIdx = (colorIdx + 1) % 2;
          y += 16;
        }
        y = 0;
        x += 16;
        colorIdx = (colorIdx + (r.height / 16) % 2) % 2;
      }

      if( pattern != null ) {
        Pattern p = pattern.pattern( aE.gc, r.width, r.height );
        if( p != null ) {
          aE.gc.setBackgroundPattern( p );
          aE.gc.fillRectangle( r );
          p.dispose();
        }
      }
    };

    public ResultPanel( Composite aParent, ITsGuiContext aContext ) {
      super( aParent, aContext, SWT.DOUBLE_BUFFERED );
      addPaintListener( paintListener );
    }

    Rectangle clientRect() {
      Point p = getSize();
      return new Rectangle( 8, 8, p.x - 8, p.y - 8 );
    }

  }

  PanelGradientFractions fractionsPanel;

  RgbaSelector rgbaSelector;

  Spinner angleSpin;
  Slider  angleSlide;

  int angle = 0;

  IGenericChangeListener changeListener = aSource -> {
    fractionsPanel.setRGBA( rgbaSelector.rgba() );
    IList<Pair<Double, RGBA>> fractions = fractionsPanel.fractions();
    LinearGradientInfo gi = new LinearGradientInfo( fractions, angle );
    pattern = gi.createGradient( tsContext() );
    resultPanel.redraw();
    genericChangeEventer().fireChangeEvent();
  };

  IGradient pattern = null;

  PanelLinearGradientSelector( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );

    setLayout( new GridLayout( 1, false ) );

    resultPanel = new ResultPanel( this, aContext );
    resultPanel.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );

    fractionsPanel = new PanelGradientFractions( this, aContext );
    fractionsPanel.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );
    fractionsPanel.genericChangeEventer().addListener( aSource -> {
      rgbaSelector.setRgba( fractionsPanel.selectedRgba() );
      IList<Pair<Double, RGBA>> fractions = fractionsPanel.fractions();
      LinearGradientInfo gi = new LinearGradientInfo( fractions, angle );
      pattern = gi.createGradient( aContext );
    } );

    createAngleControlsGroup( this );

    rgbaSelector = new RgbaSelector( this, SWT.NONE, aContext.eclipseContext() );
    rgbaSelector.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );

    rgbaSelector.setRgba( new RGBA( 255, 255, 255, 255 ) );
    rgbaSelector.genericChangeEventer().addListener( changeListener );

    updateGradient( aContext );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает параметры заливки.<br>
   *
   * @return IGradientInfo - параметры заливки
   */
  public IGradientInfo gradientInfo() {
    return new LinearGradientInfo( fractionsPanel.fractions(), angle );
  }

  /**
   * Задает параметры радиального градиента.
   *
   * @param aInfo IGradientInfo - параметры радиального градиента
   */
  public void setGradientInfo( IGradientInfo aInfo ) {
    TsIllegalArgumentRtException.checkFalse( aInfo.gradientType() == EGradientType.LINEAR );
    LinearGradientInfo info = (LinearGradientInfo)aInfo;
    fractionsPanel.setFractions( info.fractions() );
    angle = (int)info.angle();
    angleSpin.setSelection( angle );
    angleSlide.setSelection( angle );
    updateGradient( tsContext() );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  Composite createAngleControlsGroup( Composite aParent ) {
    Composite comp = new Composite( aParent, SWT.NONE );
    comp.setLayout( new GridLayout( 3, false ) );

    angleSlide = new Slider( comp, SWT.HORIZONTAL );
    angleSlide.setValues( 0, 0, 91, 1, 1, 10 );
    angleSlide.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
    angleSlide.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        angle = angleSlide.getSelection();
        angleSpin.setSelection( angle );
        updateGradient( tsContext() );
      }
    } );

    CLabel l;
    l = new CLabel( comp, SWT.NONE );
    l.setText( STR_L_GRADIENT_ANGLE );
    angleSpin = new Spinner( comp, SWT.BORDER );
    angleSpin.setMinimum( 0 );
    angleSpin.setMaximum( 90 );
    angleSpin.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        angle = angleSpin.getSelection();
        angleSlide.setSelection( angle );
        updateGradient( tsContext() );
      }
    } );

    return comp;
  }

  void updateGradient( ITsGuiContext aContext ) {
    rgbaSelector.setRgba( fractionsPanel.selectedRgba() );
    IList<Pair<Double, RGBA>> fractions = fractionsPanel.fractions();
    LinearGradientInfo gi = new LinearGradientInfo( fractions, angle );
    pattern = gi.createGradient( aContext );
    resultPanel.redraw();
  }

}

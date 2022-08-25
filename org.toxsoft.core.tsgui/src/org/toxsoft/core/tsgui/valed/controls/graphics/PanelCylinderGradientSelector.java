package org.toxsoft.core.tsgui.valed.controls.graphics;

import org.eclipse.swt.*;
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

/**
 * Панель редактирования параметров "цилиндрической" заливки.
 *
 * @author vs
 */
public class PanelCylinderGradientSelector
    extends TsPanel {

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
        aE.gc.setBackgroundPattern( p );
        aE.gc.fillRectangle( getClientArea() );
        p.dispose();
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

  IGenericChangeListener changeListener = aSource -> {
    fractionsPanel.setRGBA( rgbaSelector.rgba() );
    IList<Pair<Double, RGBA>> fractions = fractionsPanel.fractions();
    CylinderGradientInfo gi = new CylinderGradientInfo( fractions );
    pattern = gi.createGradient( gi, tsContext() );
    resultPanel.redraw();
  };

  private final ITsGuiContext tsContext;

  IGradient pattern = null;

  PanelCylinderGradientSelector( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    tsContext = aContext;

    setLayout( new GridLayout( 1, false ) );

    resultPanel = new ResultPanel( this, aContext );
    resultPanel.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );

    fractionsPanel = new PanelGradientFractions( this, aContext );
    fractionsPanel.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );
    fractionsPanel.genericChangeEventer().addListener( aSource -> {
      rgbaSelector.setRgba( fractionsPanel.selectedRgba() );
      IList<Pair<Double, RGBA>> fractions = fractionsPanel.fractions();
      CylinderGradientInfo gi = new CylinderGradientInfo( fractions );
      pattern = gi.createGradient( gi, aContext );
    } );

    rgbaSelector = new RgbaSelector( this, SWT.NONE, aContext.eclipseContext() );
    rgbaSelector.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );

    rgbaSelector.setRgba( new RGBA( 255, 255, 255, 255 ) );
    rgbaSelector.genericChangeEventer().addListener( changeListener );
  }

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает параметры заливки.<br>
   *
   * @return IGradientInfo - параметры заливки
   */
  public IGradientInfo patternInfo() {
    return new CylinderGradientInfo( fractionsPanel.fractions() );
  }

}

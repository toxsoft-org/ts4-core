package org.toxsoft.core.tsgui.ved.comps;

import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.gc.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * Базовый класс для отрисовщиков визелей типа "Кнопка".
 *
 * @author vs
 */
public abstract class AbstractButtonRenderer
    implements IButtonRenderer, ITsGuiContextable {

  protected final IViselButton button;

  protected Rectangle swtRect = new Rectangle( 0, 0, 1, 1 );

  protected Font font = null;

  protected RGBA bkRgba = new RGBA( 255, 255, 255, 255 );

  protected RGBA fgRgba = new RGBA( 0, 0, 0, 255 );

  protected RGBA hvRgba = new RGBA( 0, 0, 0, 255 );

  protected RGBA selRgba = new RGBA( 0, 0, 0, 255 );

  protected TsLineInfo lineInfo = TsLineInfo.ofWidth( 1 );

  protected boolean hovered = false;

  protected AbstractButtonRenderer( IViselButton aButton ) {
    button = aButton;
  }

  // ------------------------------------------------------------------------------------
  // ITsContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return button.tsContext();
  }

  // ------------------------------------------------------------------------------------
  // IButtonRenderer
  //

  @Override
  public void drawButton( ITsGraphicsContext aPaintContext ) {
    paintBackground( aPaintContext );
    drawText( aPaintContext );
  }

  @Override
  public final void update() {
    if( button.props().hasKey( PROPID_FONT ) ) {
      font = fontManager().getFont( button.props().getValobj( PROPID_FONT ) );
    }
    if( button.props().hasKey( PROPID_BK_COLOR ) ) {
      bkRgba = button.props().getValobj( PROPID_BK_COLOR );
    }
    if( button.props().hasKey( PROPID_FG_COLOR ) ) {
      fgRgba = button.props().getValobj( PROPID_FG_COLOR );
    }
    if( button.props().hasKey( PROPID_HOVERED_BK_COLOR ) ) {
      hvRgba = button.props().getValobj( PROPID_HOVERED_BK_COLOR );
    }
    if( button.props().hasKey( PROPID_SELECTED_BK_COLOR ) ) {
      selRgba = button.props().getValobj( PROPID_SELECTED_BK_COLOR );
    }
    hovered = button.props().getBool( PROPID_HOVERED );
    updateSwtRect();
    doUpdate();
  }

  // ------------------------------------------------------------------------------------
  // To implements
  //

  protected abstract void doUpdate();

  protected abstract void paintBackground( ITsGraphicsContext aPaintContext );

  // ------------------------------------------------------------------------------------
  // To override
  //

  void drawText( ITsGraphicsContext aPaintContext ) {
    if( !buttonText().isBlank() ) {
      Point p = textLocation( aPaintContext );

      if( buttonState() == EButtonViselState.PRESSED ) {
        p.x += 2;
        p.y += 2;
      }

      aPaintContext.gc().setFont( font );
      aPaintContext.gc().setBackgroundPattern( null );
      aPaintContext.gc().setForeground( colorManager().getColor( fgRgba ) );
      if( !buttonText().isBlank() ) {
        aPaintContext.gc().drawText( buttonText(), p.x, p.y, true );
      }
    }

  }

  // ------------------------------------------------------------------------------------
  // For use
  //

  protected EButtonViselState buttonState() {
    return button.buttonState();
  }

  protected boolean isHovered() {
    return button.props().getBool( PROPID_HOVERED );
  }

  protected String buttonText() {
    if( button.props().hasKey( PROPID_TEXT ) ) {
      return button.props().getStr( PROPID_TEXT );
    }
    return TsLibUtils.EMPTY_STRING;
  }

  protected ID2Rectangle bounds() {
    return button.bounds();
  }

  protected Point textLocation( ITsGraphicsContext aPaintContext ) {
    ID2Rectangle r = bounds();
    aPaintContext.gc().setFont( font );
    Point p = aPaintContext.gc().textExtent( buttonText() );
    int x = (int)((r.width() - p.x) / 2.);
    int y = (int)((r.height() - p.y) / 2.);
    p.x = x;
    p.y = y;
    return p;
  }

  protected void updateSwtRect() {
    ID2Rectangle r = bounds();

    swtRect.x = (int)Math.round( r.x1() );
    swtRect.y = (int)Math.round( r.y1() );
    swtRect.width = (int)Math.round( r.width() );
    swtRect.height = (int)Math.round( r.height() );
  }

}

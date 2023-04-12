package org.toxsoft.core.tsgui.graphics.txtsplit;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Base implementation of {@link ICanvasTextSplitter}.
 * <p>
 * The instance does not disposes any resources specified in constructor!
 *
 * @author hazard157
 */
public non-sealed abstract class AbstractCanvasTextSplitter
    implements ICanvasTextSplitter {

  private final GC   gc;
  private final Font font;

  /**
   * Constructor.
   * <p>
   * The instance does not disposes any resources specified in constructor!
   *
   * @param aGc {@link GC} - drawing canvas
   * @param aFont {@link Font} - text drawing font
   */
  public AbstractCanvasTextSplitter( GC aGc, Font aFont ) {
    TsNullArgumentRtException.checkNulls( aGc, aFont );
    gc = aGc;
    font = aFont;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the canvas specified in the constructor.
   *
   * @return {@link Canvas} - the drawing canvas
   */
  final public GC gc() {
    return gc;
  }

  /**
   * Returns the font specified in the constructor.
   *
   * @return {@link Font} - the font
   */
  final public Font font() {
    return font;
  }

  // ------------------------------------------------------------------------------------
  // ICanvasTextSplitter
  //

  @Override
  final public IList<TextLine> splitText( String aText, int aWidth, int aLinesNum ) {
    TsNullArgumentRtException.checkNull( aText );
    TsIllegalArgumentRtException.checkTrue( aWidth < 1 );
    TsIllegalArgumentRtException.checkTrue( aLinesNum < 1 );
    IListEdit<TextLine> lines = new ElemArrayList<>();
    doSplitText( aText, aWidth, aLinesNum, lines );
    return lines;
  }

  // ------------------------------------------------------------------------------------
  // to implement
  //

  /**
   * Implementation must split text according to it's strategy.
   * <p>
   * Arguments are checked to be valid as specified for {@link #splitText(String, int, int)}.
   *
   * @param aText String - the text to split, never is <code>null</code>
   * @param aWidth int - with of rectangular area to fit, always >= 1
   * @param aLinesNum int - max number of text lines, always >= 1
   * @param aLines {@link IStringListEdit} - empty list to fill in the lines of text
   */
  protected abstract void doSplitText( String aText, int aWidth, int aLinesNum, IListEdit<TextLine> aLines );

}

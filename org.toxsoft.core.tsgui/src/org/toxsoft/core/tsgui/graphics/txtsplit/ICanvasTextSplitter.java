package org.toxsoft.core.tsgui.graphics.txtsplit;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Split text to display as multi-line on canvas.
 * <p>
 * Interface have different implementations with the different splitting strategy.
 * <p>
 * Usage: TODO describe {@link ICanvasTextSplitter} usage
 *
 * @author hazard157
 */
public sealed interface ICanvasTextSplitter
    permits AbstractCanvasTextSplitter {

  /**
   * Splits text to fit specified width and in the specified rows.
   *
   * @param aText String - the text to split
   * @param aWidth int - with of rectangular area to fit
   * @param aLinesNum int - max number of text lines
   * @return {@link IList}&lt;{@link TextLine}&gt; - the split and probably modified text
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException aWidth < 1
   * @throws TsIllegalArgumentRtException aLinesNum < 1
   */
  IList<TextLine> splitText( String aText, int aWidth, int aLinesNum );

}

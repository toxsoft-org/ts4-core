package org.toxsoft.core.tsgui.graphics.txtsplit;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.coll.*;

/**
 * Simple implementation of {@link ICanvasTextSplitter} - splits text by new lines.
 * <p>
 * Neither checks the if text fits the area, nor checks the number of lines.
 *
 * @author hazard157
 */
public class SimpleCanvasTextSplitter
    extends AbstractCanvasTextSplitter {

  /**
   * Constructor.
   * <p>
   * The instance does not disposes any resources specified in constructor!
   *
   * @param aGc {@link GC} - drawing canvas
   * @param aFont {@link Font} - text drawing font
   */
  public SimpleCanvasTextSplitter( GC aGc, Font aFont ) {
    super( aGc, aFont );
  }

  // ------------------------------------------------------------------------------------
  // AbstractCanvasTextSplitter
  //

  @Override
  protected void doSplitText( String aText, int aWidth, int aLinesNum, IListEdit<TextLine> aLines ) {
    for( String s : aText.split( "" + CHAR_EOL ) ) { //$NON-NLS-1$
      Point p = gc().textExtent( s );
      aLines.add( new TextLine( new TsPoint( p.x, p.y ), s ) );
    }
  }

}

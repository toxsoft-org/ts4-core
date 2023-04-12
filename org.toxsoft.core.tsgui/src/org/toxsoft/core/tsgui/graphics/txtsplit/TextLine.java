package org.toxsoft.core.tsgui.graphics.txtsplit;

import org.toxsoft.core.tslib.bricks.geometry.*;

/**
 * @author hazard157
 * @param bounds {@link ITsPoint} - size of the text extent in pixels
 * @param text {@link String} - the single-line text
 */
public record TextLine ( ITsPoint bounds, String text ) {

}

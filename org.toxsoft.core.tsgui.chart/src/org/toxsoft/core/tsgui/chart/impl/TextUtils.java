package org.toxsoft.core.tsgui.chart.impl;

import static java.awt.RenderingHints.*;

import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.image.*;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.chart.legaсy.*;
import org.toxsoft.core.tsgui.graphics.fonts.*;

public class TextUtils {

  public static Image createRotatedTextImage( String aText, IFontInfo aFontInfo, double aAngle,
      org.eclipse.swt.graphics.Color aColor ) {

    GlyphVector textGlyph = createTextGlyph( aFontInfo, aText );
    Shape textShape = textGlyph.getOutline( (float)(0f - textGlyph.getVisualBounds().getX()), //
        (float)-textGlyph.getVisualBounds().getY() );

    textShape = rotateShape( textShape, Math.toRadians( aAngle ) );

    int imgWidth = textShape.getBounds().width;
    int imgHeight = textShape.getBounds().height;

    final BufferedImage awtImage = new BufferedImage( imgWidth, imgHeight, BufferedImage.TYPE_INT_ARGB );

    final java.awt.Graphics2D g2d = awtImage.createGraphics();
    g2d.setRenderingHint( KEY_ANTIALIASING, VALUE_ANTIALIAS_ON );
    g2d.setRenderingHint( KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_ON );
    g2d.setRenderingHint( KEY_RENDERING, VALUE_RENDER_QUALITY );

    Color textColor = new Color( aColor.getRed(), aColor.getGreen(), aColor.getBlue() );
    g2d.setColor( textColor );
    g2d.setBackground( textColor );
    g2d.fill( textShape );

    ImageData imgData = TsImageUtils.convertToSWT( awtImage );
    g2d.dispose();
    return new Image( Display.getDefault(), imgData );
  }

  public static Shape rotateShape( Shape aShape, double aAngle ) {
    AffineTransform at = new AffineTransform();
    at.rotate( aAngle );
    Area area = new Area( aShape );
    area = area.createTransformedArea( at );
    Rectangle2D rect = area.getBounds2D();
    at = new AffineTransform();
    at.translate( -rect.getX(), -rect.getY() );
    return area.createTransformedArea( at );
  }

  public static GlyphVector createTextGlyph( IFontInfo aFontInfo, String aText ) {
    int fontStyle = 0;
    if( aFontInfo.isBold() ) {
      fontStyle |= Font.BOLD;
    }
    if( aFontInfo.isItalic() ) {
      fontStyle |= Font.ITALIC;
    }
    Font font = new Font( aFontInfo.fontName(), fontStyle, aFontInfo.fontSize() );

    // GraphicsConfiguration gfxConf;
    // gfxConf = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
    // BufferedImage awtImage = gfxConf.createCompatibleImage( 16, 16, java.awt.Transparency.TRANSLUCENT );
    BufferedImage awtImage = new BufferedImage( 16, 16, BufferedImage.TYPE_INT_ARGB );
    java.awt.Graphics2D g2d = awtImage.createGraphics();
    g2d.setRenderingHint( KEY_ANTIALIASING, VALUE_ANTIALIAS_ON );
    g2d.setRenderingHint( KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_ON );
    g2d.setRenderingHint( KEY_RENDERING, VALUE_RENDER_QUALITY );

    GlyphVector v = font.createGlyphVector( g2d.getFontRenderContext(), aText );
    g2d.dispose();
    return v;
  }

}

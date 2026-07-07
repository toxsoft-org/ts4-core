package org.toxsoft.core.tsgui.ved.comps.AI.SVG;

/** Текст: &lt;text x y&gt;content&lt;/text&gt; */
public class SvgText
    extends SvgShape {

  private double x, y;
  private String content;
  private String fontFamily;
  private String fontSize;

  public SvgText() {
    super( "text" );
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public String getContent() {
    return content;
  }

  public String getFontFamily() {
    return fontFamily;
  }

  public String getFontSize() {
    return fontSize;
  }

  public void setX( double x ) {
    this.x = x;
  }

  public void setY( double y ) {
    this.y = y;
  }

  public void setContent( String content ) {
    this.content = content;
  }

  public void setFontFamily( String ff ) {
    this.fontFamily = ff;
  }

  public void setFontSize( String fs ) {
    this.fontSize = fs;
  }

  @Override
  public String toString() {
    return super.toString() + String.format( " x=%.1f y=%.1f text=\"%s\"", x, y, content );
  }
}

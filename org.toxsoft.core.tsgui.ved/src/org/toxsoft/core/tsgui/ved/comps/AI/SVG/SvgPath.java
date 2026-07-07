package org.toxsoft.core.tsgui.ved.comps.AI.SVG;

/** Произвольный путь: &lt;path d&gt; */
public class SvgPath
    extends SvgShape {

  private String d;

  public SvgPath() {
    super( "path" );
  }

  public String getD() {
    return d;
  }

  public void setD( String d ) {
    this.d = d;
  }

  @Override
  public String toString() {
    String preview = d != null && d.length() > 40 ? d.substring( 0, 40 ) + "…" : d;
    return super.toString() + " d=" + preview;
  }
}

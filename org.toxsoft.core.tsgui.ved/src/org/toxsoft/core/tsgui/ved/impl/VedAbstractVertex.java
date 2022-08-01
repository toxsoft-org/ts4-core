package org.toxsoft.core.tsgui.ved.impl;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;

/**
 * Базовый класс для создания вершин "активной" границы.
 * <p>
 *
 * @author vs
 */
public abstract class VedAbstractVertex
    extends Stridable
    implements IVedVertex {

  private final IOptionSetEdit params = new OptionSet();

  private Color fgColor = null;

  private Color bgColor = null;

  protected VedAbstractVertex( String aId, String aName, String aDescr ) {
    super( aId, aName, aDescr );
  }

  @Override
  public final IOptionSet params() {
    return params;
  }

  @Override
  public Color foregroundColor() {
    return fgColor;
  }

  @Override
  public Color backgroundColor() {
    return bgColor;
  }

  @Override
  public void setForeground( Color aColor ) {
    fgColor = aColor;
  }

  @Override
  public void setBackground( Color aColor ) {
    bgColor = aColor;
  }

}

package org.toxsoft.core.tsgui.chart.legaсy;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;

/**
 * Хранитель объектов типа {@link Margins}.
 *
 * @author vs
 */
public class MarginsKeeper
    extends AbstractEntityKeeper<Margins> {

  /**
   * ИД "хранителя"
   */
  public static final String KEEPER_ID = "Margins"; //$NON-NLS-1$

  /**
   * Экземпляр-синглтон хранителя.
   */
  public static final IEntityKeeper<Margins> KEEPER = new MarginsKeeper();

  protected MarginsKeeper() {
    super( Margins.class, EEncloseMode.ENCLOSES_BASE_CLASS, Margins.NONE );
  }

  @Override
  protected void doWrite( IStrioWriter aSw, Margins aEntity ) {
    aSw.writeInt( aEntity.left() );
    aSw.writeSeparatorChar();
    aSw.writeInt( aEntity.top() );
    aSw.writeSeparatorChar();
    aSw.writeInt( aEntity.right() );
    aSw.writeSeparatorChar();
    aSw.writeInt( aEntity.bottom() );
  }

  @Override
  protected Margins doRead( IStrioReader aSr ) {
    int left = aSr.readInt();
    aSr.ensureSeparatorChar();
    int top = aSr.readInt();
    aSr.ensureSeparatorChar();
    int right = aSr.readInt();
    aSr.ensureSeparatorChar();
    int bottom = aSr.readInt();
    return new Margins( left, top, right, bottom );
  }

}

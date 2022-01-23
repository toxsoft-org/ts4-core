package org.toxsoft.tslib.bricks.geometry.impl;

import org.toxsoft.tslib.bricks.geometry.ITsPoint;
import org.toxsoft.tslib.bricks.keeper.AbstractEntityKeeper;
import org.toxsoft.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.tslib.bricks.strio.IStrioReader;
import org.toxsoft.tslib.bricks.strio.IStrioWriter;
import org.toxsoft.tslib.utils.valobj.TsValobjUtils;

/**
 * Хранитель объектов типа {@link ITsPoint}.
 *
 * @author hazard157
 */
public class TsPointKeeper
    extends AbstractEntityKeeper<ITsPoint> {

  /**
   * Идентификатор регистрации хранителя {@link #KEEPER} в реестре {@link TsValobjUtils}.
   */
  public static final String KEEPER_ID = "TsPoint"; //$NON-NLS-1$

  /**
   * Экземпляр-синглтон хранителя.
   */
  public static final IEntityKeeper<ITsPoint> KEEPER = new TsPointKeeper();

  protected TsPointKeeper() {
    super( ITsPoint.class, EEncloseMode.ENCLOSES_BASE_CLASS, ITsPoint.ZERO );
  }

  @Override
  protected void doWrite( IStrioWriter aSw, ITsPoint aEntity ) {
    aSw.writeInt( aEntity.x() );
    aSw.writeSeparatorChar();
    aSw.writeInt( aEntity.y() );
  }

  @Override
  protected ITsPoint doRead( IStrioReader aSr ) {
    int x = aSr.readInt();
    aSr.ensureSeparatorChar();
    int y = aSr.readInt();
    return new TsPoint( x, y );
  }

}

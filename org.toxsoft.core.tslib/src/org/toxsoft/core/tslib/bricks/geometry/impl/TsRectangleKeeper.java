package org.toxsoft.core.tslib.bricks.geometry.impl;

import org.toxsoft.core.tslib.bricks.geometry.ITsPoint;
import org.toxsoft.core.tslib.bricks.geometry.ITsRectangle;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper;
import org.toxsoft.core.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.core.tslib.bricks.strio.IStrioReader;
import org.toxsoft.core.tslib.bricks.strio.IStrioWriter;
import org.toxsoft.core.tslib.utils.valobj.TsValobjUtils;

/**
 * Хранитель объектов типа {@link ITsRectangle}.
 * <p>
 * Поддерживает запись/чтение константы {@link ITsRectangle#NONE}.
 *
 * @author hazard157
 */
public class TsRectangleKeeper
    extends AbstractEntityKeeper<ITsRectangle> {

  /**
   * Идентификатор регистрации хранителя {@link #KEEPER} в реестре {@link TsValobjUtils}.
   */
  public static final String KEEPER_ID = "TsRectangle"; //$NON-NLS-1$

  /**
   * Экземпляр-синглтон хранителя.
   */
  public static final IEntityKeeper<ITsRectangle> KEEPER = new TsRectangleKeeper();

  protected TsRectangleKeeper() {
    super( ITsRectangle.class, EEncloseMode.ENCLOSES_BASE_CLASS, ITsRectangle.NONE );
  }

  @Override
  protected void doWrite( IStrioWriter aSw, ITsRectangle aEntity ) {
    TsPointKeeper.KEEPER.write( aSw, aEntity.a() );
    aSw.writeSeparatorChar();
    TsPointKeeper.KEEPER.write( aSw, aEntity.b() );
  }

  @Override
  protected ITsRectangle doRead( IStrioReader aSr ) {
    ITsPoint a = TsPointKeeper.KEEPER.read( aSr );
    aSr.ensureSeparatorChar();
    ITsPoint b = TsPointKeeper.KEEPER.read( aSr );
    return new TsRectangle( a, b );
  }

}

package org.toxsoft.tsgui.utils.swt;

import org.eclipse.swt.graphics.Point;
import org.toxsoft.tslib.bricks.keeper.AbstractEntityKeeper;
import org.toxsoft.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.tslib.bricks.strio.IStrioReader;
import org.toxsoft.tslib.bricks.strio.IStrioWriter;
import org.toxsoft.tslib.utils.valobj.TsValobjUtils;

/**
 * Хранитель объекта типа {@link Point} в текстовом представлении.
 *
 * @author mvk
 */
public class PointKeeper
    extends AbstractEntityKeeper<Point> {

  /**
   * Идентификатор регистрации хранителя {@link #KEEPER} в реестре {@link TsValobjUtils}.
   */
  public static final String KEEPER_ID = "Point"; //$NON-NLS-1$

  /**
   * Экземпляр-синглтон хранителя.
   */
  public static final IEntityKeeper<Point> KEEPER = new PointKeeper();

  private PointKeeper() {
    super( Point.class, EEncloseMode.ENCLOSES_BASE_CLASS, null );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса AbstractEntityKeeper
  //
  @Override
  public Class<Point> entityClass() {
    return Point.class;
  }

  @Override
  protected void doWrite( IStrioWriter aSw, Point aEntity ) {
    aSw.writeInt( aEntity.x );
    aSw.writeSeparatorChar();
    aSw.writeInt( aEntity.y );
  }

  @Override
  protected Point doRead( IStrioReader aSr ) {
    int x = aSr.readInt();
    aSr.ensureSeparatorChar();
    int y = aSr.readInt();
    return new Point( x, y );
  }
}

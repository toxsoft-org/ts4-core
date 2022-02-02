package org.toxsoft.core.tslib.gw.gwid;

import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper;
import org.toxsoft.core.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.core.tslib.bricks.strio.IStrioReader;
import org.toxsoft.core.tslib.bricks.strio.IStrioWriter;
import org.toxsoft.core.tslib.coll.IListEdit;

/**
 * Хранитель объектов типа {@link IGwidList}.
 * <p>
 * Внимание: возвращаемые методами чтения значение типа {@link IGwidList} можно безопасно приводить к {@link GwidList}.
 *
 * @author hazard157
 */
public class GwidListKeeper
    extends AbstractEntityKeeper<IGwidList> {

  /**
   * Value-object keeper identifier.
   */
  public static final String KEEPER_ID = "GwidList"; //$NON-NLS-1$

  /**
   * Экземпляр-синглтон.
   */
  public static final IEntityKeeper<IGwidList> KEEPER = new GwidListKeeper( false );

  /**
   * Экземпляр-синглтон.
   */
  public static final IEntityKeeper<IGwidList> KEEPER_INDENTED = new GwidListKeeper( true );

  private final boolean indented;

  private GwidListKeeper( boolean aIndented ) {
    super( IGwidList.class, EEncloseMode.ENCLOSES_KEEPER_IMPLEMENTATION, IGwidList.EMPTY );
    indented = aIndented;
  }

  @Override
  public Class<IGwidList> entityClass() {
    return IGwidList.class;
  }

  @Override
  protected void doWrite( IStrioWriter aSw, IGwidList aEntity ) {
    Gwid.KEEPER.writeColl( aSw, aEntity, indented );
  }

  @Override
  protected IGwidList doRead( IStrioReader aSr ) {
    IListEdit<Gwid> ll = Gwid.KEEPER.readColl( aSr );
    return GwidList.createDirect( ll );
  }

}

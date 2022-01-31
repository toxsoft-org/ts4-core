package org.toxsoft.core.tslib.gw.skid;

import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper;
import org.toxsoft.core.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.core.tslib.bricks.strio.IStrioReader;
import org.toxsoft.core.tslib.bricks.strio.IStrioWriter;
import org.toxsoft.core.tslib.coll.IListEdit;

/**
 * Хранитель объектов типа {@link ISkidList}.
 * <p>
 * Внимание: возвращаемые методами чтения значение типа {@link ISkidList} можно безопасно приводить к {@link SkidList}.
 *
 * @author hazard157
 */
public class SkidListKeeper
    extends AbstractEntityKeeper<ISkidList> {

  /**
   * Value-object keeper identifier.
   */
  public static final String KEEPER_ID = "SkidList"; //$NON-NLS-1$

  /**
   * Экземпляр-синглтон.
   */
  public static final IEntityKeeper<ISkidList> KEEPER = new SkidListKeeper( false );

  /**
   * Экземпляр-синглтон.
   */
  public static final IEntityKeeper<ISkidList> KEEPER_INDENTED = new SkidListKeeper( true );

  /**
   * Экземпляр-синглтон.
   */
  public static final IEntityKeeper<ISkidList> KEEPER_READ_NEW_INSTANCES = new SkidListKeeper( false, 0 );

  private final boolean indented;

  private SkidListKeeper( boolean aIndented ) {
    super( ISkidList.class, EEncloseMode.ENCLOSES_KEEPER_IMPLEMENTATION, ISkidList.EMPTY );
    indented = aIndented;
  }

  private SkidListKeeper( boolean aIndented, @SuppressWarnings( "unused" ) int aFoo ) {
    super( ISkidList.class, EEncloseMode.ENCLOSES_KEEPER_IMPLEMENTATION, null );
    indented = aIndented;
  }

  @Override
  public Class<ISkidList> entityClass() {
    return ISkidList.class;
  }

  @Override
  protected void doWrite( IStrioWriter aDw, ISkidList aEntity ) {
    Skid.KEEPER.writeColl( aDw, aEntity, indented );
  }

  @Override
  protected ISkidList doRead( IStrioReader aDr ) {
    IListEdit<Skid> ll = Skid.KEEPER.readColl( aDr );
    return SkidList.createDirect( ll );
  }

}

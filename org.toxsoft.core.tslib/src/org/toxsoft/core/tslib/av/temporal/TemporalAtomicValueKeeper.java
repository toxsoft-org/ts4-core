package org.toxsoft.core.tslib.av.temporal;

import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.av.impl.AtomicValueKeeper;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper;
import org.toxsoft.core.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.core.tslib.bricks.strio.IStrioReader;
import org.toxsoft.core.tslib.bricks.strio.IStrioWriter;
import org.toxsoft.core.tslib.utils.valobj.TsValobjUtils;

/**
 * Хранитель объектов типа {@link ITemporalAtomicValue}.
 *
 * @author hazard157
 */
public class TemporalAtomicValueKeeper
    extends AbstractEntityKeeper<ITemporalAtomicValue> {

  /**
   * Идентификатор регистрации хранителя {@link #KEEPER} в реестре {@link TsValobjUtils}.
   */
  public static final String KEEPER_ID = "TemporalAtomicValue"; //$NON-NLS-1$

  /**
   * Экземпляр-синглтон хранителя.
   */
  public static final IEntityKeeper<ITemporalAtomicValue> KEEPER = new TemporalAtomicValueKeeper();

  private TemporalAtomicValueKeeper() {
    super( ITemporalAtomicValue.class, EEncloseMode.ENCLOSES_BASE_CLASS, ITemporalAtomicValue.NULL );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса AbstractEntityKeeper
  //

  @Override
  protected void doWrite( IStrioWriter aSw, ITemporalAtomicValue aEntity ) {
    aSw.writeTimestamp( aEntity.timestamp() );
    aSw.writeSeparatorChar();
    AtomicValueKeeper.KEEPER.write( aSw, aEntity.value() );
  }

  @Override
  protected ITemporalAtomicValue doRead( IStrioReader aSr ) {
    long timestamp = aSr.readTimestamp();
    aSr.ensureSeparatorChar();
    IAtomicValue value = AtomicValueKeeper.KEEPER.read( aSr );
    return new TemporalAtomicValue( timestamp, value );
  }

}

package org.toxsoft.core.tslib.bricks.keeper.std;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import java.time.LocalDate;

import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper;
import org.toxsoft.core.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.core.tslib.bricks.strio.IStrioReader;
import org.toxsoft.core.tslib.bricks.strio.IStrioWriter;
import org.toxsoft.core.tslib.utils.valobj.TsValobjUtils;

/**
 * Хранитель объектов типа {@link LocalDate}.
 * <p>
 * Хранит {@link LocalDate} в совместимом с {@link IStrioWriter#writeDate(long)} формате, в виде "YYYY-MM-DD".
 *
 * @author hazard157
 */
public class LocalDateKeeper
    extends AbstractEntityKeeper<LocalDate> {

  /**
   * Идентификатор регистрации хранителя {@link #KEEPER} в реестре {@link TsValobjUtils}.
   */
  public static final String KEEPER_ID = "Date"; //$NON-NLS-1$

  /**
   * Синглтон хранителя.
   */
  public static final IEntityKeeper<LocalDate> KEEPER = new LocalDateKeeper();

  private LocalDateKeeper() {
    super( LocalDate.class, EEncloseMode.NOT_IN_PARENTHESES, null );
  }

  @Override
  protected void doWrite( IStrioWriter aSw, LocalDate aEntity ) {
    // OPTIMIZE в случае проблем с производительностью, заменить на методы типа StridWriter.outXxx()
    Integer year = Integer.valueOf( aEntity.getYear() );
    Integer month = Integer.valueOf( aEntity.getMonthValue() );
    Integer dayOfMonth = Integer.valueOf( aEntity.getDayOfMonth() );
    aSw.p( "%04d-%02d-%02d", year, month, dayOfMonth ); //$NON-NLS-1$
  }

  @Override
  protected LocalDate doRead( IStrioReader aSr ) {
    int year = aSr.readInt();
    aSr.ensureChar( CHAR_TIMESTAMP_YMD_SEPARATOR );
    int month = aSr.readInt();
    aSr.ensureChar( CHAR_TIMESTAMP_YMD_SEPARATOR );
    int dom = aSr.readInt();
    return LocalDate.of( year, month, dom );
  }

}

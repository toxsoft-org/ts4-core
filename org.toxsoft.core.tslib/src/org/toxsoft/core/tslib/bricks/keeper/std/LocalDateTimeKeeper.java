package org.toxsoft.core.tslib.bricks.keeper.std;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import java.time.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.valobj.*;

/**
 * Хранитель объектов типа {@link LocalDateTime}.
 * <p>
 * Хранит в совместимом с {@link IStrioWriter} формате, в виде "YYYY-MM-DD_HH:MM:SS.UUU" со следующими особенностями:
 * <ul>
 * <li>не сохраняет наносекунды, только миллисекунды;</li>
 * <li>при сохранении, если значение миллисекунд равно 0, то они не записываютс, и пишется как "YYYY-MM-DD_HH:MM:SS", то
 * есть, в формате {@link IStrioWriter#writeDateTime(long)}. Если миллиекунды не 0, то записывается в формате
 * {@link IStrioWriter#writeTimestamp(long)}, в виде "YYYY-MM-DD_HH:MM:SS.UUU".</li>
 * </ul>
 *
 * @author hazard157
 */
public class LocalDateTimeKeeper
    extends AbstractEntityKeeper<LocalDateTime> {

  /**
   * Идентификатор регистрации хранителя {@link #KEEPER} в реестре {@link TsValobjUtils}.
   */
  public static final String KEEPER_ID = "DateTime"; //$NON-NLS-1$

  /**
   * Синглтон хранителя.
   */
  public static final IEntityKeeper<LocalDateTime> KEEPER = new LocalDateTimeKeeper();

  private LocalDateTimeKeeper() {
    super( LocalDateTime.class, EEncloseMode.NOT_IN_PARENTHESES, null );
  }

  @Override
  protected void doWrite( IStrioWriter aSw, LocalDateTime aEntity ) {
    // OPTIMIZE в случае проблем с производительностью, заменить на методы типа StridWriter.outXxx()
    Integer year = Integer.valueOf( aEntity.getYear() );
    Integer month = Integer.valueOf( aEntity.getMonthValue() );
    Integer dayOfMonth = Integer.valueOf( aEntity.getDayOfMonth() );
    Integer hh = Integer.valueOf( aEntity.getHour() );
    Integer mm = Integer.valueOf( aEntity.getMinute() );
    Integer ss = Integer.valueOf( aEntity.getSecond() );
    Integer mmm = Integer.valueOf( aEntity.getNano() / 1_000_000 );
    if( mmm.intValue() != 0 ) {
      aSw.p( "%04d-%02d-%02d_%02d:%02d:%02d.%03d", year, month, dayOfMonth, hh, mm, ss, mmm ); //$NON-NLS-1$
    }
    else {
      aSw.p( "%04d-%02d-%02d_%02d:%02d:%02d", year, month, dayOfMonth, hh, mm, ss ); //$NON-NLS-1$

    }
  }

  @Override
  protected LocalDateTime doRead( IStrioReader aSr ) {
    int year = aSr.readInt();
    aSr.ensureChar( CHAR_TIMESTAMP_YMD_SEPARATOR );
    int month = aSr.readInt();
    aSr.ensureChar( CHAR_TIMESTAMP_YMD_SEPARATOR );
    int dom = aSr.readInt();
    aSr.ensureChar( CHAR_TIMESTAMP_DATETIME_SEPARATOR );
    int hh = aSr.readInt();
    aSr.ensureChar( CHAR_TIMESTAMP_HMS_SEPARATOR );
    int mm = aSr.readInt();
    aSr.ensureChar( CHAR_TIMESTAMP_HMS_SEPARATOR );
    int ss = aSr.readInt();
    int uuu = 0;
    if( aSr.peekChar( EStrioSkipMode.SKIP_NONE ) == CHAR_TIMESTAMP_MILLISEC_SEPARATOR ) {
      aSr.ensureChar( CHAR_TIMESTAMP_MILLISEC_SEPARATOR );
      uuu = aSr.readInt();
    }
    return LocalDateTime.of( year, month, dom, hh, mm, ss, uuu * 1000 );
  }

}

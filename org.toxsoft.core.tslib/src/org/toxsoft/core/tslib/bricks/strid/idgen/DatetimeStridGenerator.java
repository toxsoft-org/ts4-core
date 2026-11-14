package org.toxsoft.core.tslib.bricks.strid.idgen;

import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.tslib.bricks.strid.idgen.ITsResources.*;

import java.time.*;

import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.math.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IStridGenerator} creating IDs based on {@link LocalDateTime#now()}.
 * <p>
 * Creates STRIDs like "<b>prefix_YYYYMMDD_HHMMSS_NNNNNNNNN</b>".
 * <p>
 * Important note: this generator is <b>not</b> intended for fast ID generation in sequence. Method {@link #nextId()}
 * uses call to {@link LocalDateTime#now()}, ensure that {@link LocalDateTime#now()} not returns the same value.
 * <p>
 * Class is <b>not</b> thread-safe.
 *
 * @author hazard157
 */
public final class DatetimeStridGenerator
    implements IStridGenerator {

  /**
   * Default prefix of generated STRIDs.
   */
  public static final String DEFAULT_PREFIX = "id"; //$NON-NLS-1$

  /**
   * State option: prefix string.
   */
  public static final IDataDef OPDEF_PREFIX = DataDef.create( "Prefix", STRING, //$NON-NLS-1$
      TSID_DEFAULT_VALUE, avStr( DEFAULT_PREFIX ) );

  /**
   * Default initial state of the generator.
   */
  public static final IOptionSet DEFAULT_INITIAL_STATE = OptionSetUtils.createOpSet( //
      OPDEF_PREFIX.id(), OPDEF_PREFIX.defaultValue() //
  );

  private static final IntRange YEARS_RANGE = new IntRange( 0, 9999 );

  /**
   * Initial state of the generator.
   */
  private final IOptionSet initialState;

  /**
   * Current state of the generator.
   */
  private final IOptionSetEdit state = new OptionSet();

  // ------------------------------------------------------------------------------------
  // Constructors
  //

  /**
   * Creates generator with default state {@link #DEFAULT_INITIAL_STATE}.
   */
  public DatetimeStridGenerator() {
    initialState = DEFAULT_INITIAL_STATE;
    setState( initialState );
  }

  /**
   * Creates generator with specified state
   *
   * @param aInitialState {@link IOptionSet} - initial state
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException failed {@link #validateState(IOptionSet)}
   */
  public DatetimeStridGenerator( IOptionSet aInitialState ) {
    initialState = new OptionSet( aInitialState );
    setState( initialState );
  }

  /**
   * Constructor.
   *
   * @param aPrefix String - prefix (an IDname)
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException failed {@link #validateState(IOptionSet)}
   */
  public DatetimeStridGenerator( String aPrefix ) {
    initialState = createState( aPrefix );
    setState( initialState );
  }

  /**
   * Creates state fur further usage in constructor or for state change.
   * <p>
   * Created state may not be valid, use {@link #validateState(IOptionSet)} to check for validity.
   *
   * @param aPrefix String - prefix (an IDname)
   * @return {@link IOptionSet} - created state, may not be valid
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static IOptionSet createState( String aPrefix ) {
    TsNullArgumentRtException.checkNull( aPrefix );
    IOptionSetEdit ops = new OptionSet();
    ops.setStr( OPDEF_PREFIX, aPrefix );
    return ops;
  }

  // ------------------------------------------------------------------------------------
  // IStridGenerator
  //

  @Override
  public String nextId() {
    LocalDateTime t = LocalDateTime.now();
    String prefix = OPDEF_PREFIX.getValue( state ).asString();
    return makeId( prefix, t );
  }

  @Override
  public IOptionSet getInitialState() {
    IOptionSetEdit ops = new OptionSet();
    ops.addAll( initialState );
    return ops;
  }

  @Override
  public IOptionSet getState() {
    return new OptionSet( state );
  }

  @Override
  public void setState( IOptionSet aState ) {
    TsValidationFailedRtException.checkWarn( validateState( aState ) );
    state.setAll( aState );
  }

  @Override
  public ValidationResult validateState( IOptionSet aState ) {
    String prefix = OPDEF_PREFIX.getValue( aState ).asString();
    if( !StridUtils.isValidIdName( prefix ) ) {
      return ValidationResult.error( FMT_ERR_SG_NON_IDNAME_PREFIX, prefix );
    }
    return ValidationResult.SUCCESS;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Creates an ID corresponding to the specified time moment.
   * <p>
   * This method is used in {@link #nextId()} to generate and ID based on {@link LocalDateTime#now()}.
   *
   * @param aPrefix String - the prefix
   * @param aTime {@link LocalDateTime} - the time mooment
   * @return String - the ID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException prefix is not an IDpath
   */
  @SuppressWarnings( "boxing" )
  public static String makeId( String aPrefix, LocalDateTime aTime ) {
    StridUtils.checkValidIdPath( aPrefix );
    TsNullArgumentRtException.checkNull( aTime );
    YEARS_RANGE.checkInRange( aTime.getYear() );
    return String.format( "%s_%4d%02d%02d_%02d%02d%02d_%09d", aPrefix, //$NON-NLS-1$
        aTime.getYear(), aTime.getMonthValue(), aTime.getDayOfMonth(), //
        aTime.getHour(), aTime.getMinute(), aTime.getSecond(), aTime.getNano() );
  }

}

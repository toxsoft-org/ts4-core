package org.toxsoft.core.tslib.bricks.strid.idgen;

import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.tslib.bricks.strid.idgen.ITsResources.*;

import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IStridGenerator} implementation of IDpathes with postfixed timestamp long number.
 * <p>
 * Creates STRIDs like "<b>prefix_TTTTTT[_NNN]</b>" where TTTTTT is {@link System#currentTimeMillis()} value and NNN is
 * optional counter if call of {@link #nextId()} happtns inside the last call millisecond..
 * <p>
 * Class is <b>not</b> thread-safe.
 *
 * @author hazard157
 */
public final class TimastampMsecStridGenerator
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

  /**
   * Initial state of the generator.
   */
  private final IOptionSet initialState;

  /**
   * Current state of the generator.
   */
  private final IOptionSetEdit state = new OptionSet();

  /**
   * Format string for first ID in the millisecond.
   */
  private final String fmtStr = "%s_%d"; //$NON-NLS-1$

  /**
   * Format string for second and next IDs in the millisecond.
   */
  private final String fmtStr1 = "%s_%d_%03d"; //$NON-NLS-1$

  /**
   * Timestamp of last {@link #nextId()} call.
   */
  private long lastIdGenTimestamp = 0;

  /**
   * Counter inside the millisecond of last {@link #nextId()} call,
   */
  private int inMsecCounter = 0;

  // ------------------------------------------------------------------------------------
  // Constructors
  //

  /**
   * Creates generator with default state {@link #DEFAULT_INITIAL_STATE}.
   */
  public TimastampMsecStridGenerator() {
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
  public TimastampMsecStridGenerator( IOptionSet aInitialState ) {
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
  public TimastampMsecStridGenerator( String aPrefix ) {
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
    long time = System.currentTimeMillis();
    String fs;
    if( time == lastIdGenTimestamp ) {
      /**
       * Generating IDs faster than 1 per 0,5 PICOsecond is not supported :)))<br>
       * In other words, more than 2_147_483_647 IDs in MILLIsecond)
       */
      TsInternalErrorRtException.checkTrue( inMsecCounter == Integer.MAX_VALUE );
      inMsecCounter++;
      fs = fmtStr1;
    }
    else {
      inMsecCounter = 0;
      fs = fmtStr;
    }
    lastIdGenTimestamp = time;
    String prefix = OPDEF_PREFIX.getValue( state ).asString();
    Long argTimestamp = Long.valueOf( lastIdGenTimestamp );
    Integer argCounter = Integer.valueOf( inMsecCounter );
    return String.format( fs, prefix, argTimestamp, argCounter );
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

  @SuppressWarnings( "boxing" )
  @Override
  public void setState( IOptionSet aState ) {
    TsValidationFailedRtException.checkWarn( validateState( aState ) );
    state.setAll( aState );
  }

  @SuppressWarnings( "boxing" )
  @Override
  public ValidationResult validateState( IOptionSet aState ) {
    String prefix = OPDEF_PREFIX.getValue( aState ).asString();
    if( !StridUtils.isValidIdName( prefix ) ) {
      return ValidationResult.error( FMT_ERR_SG_NON_IDNAME_PREFIX, prefix );
    }
    return ValidationResult.SUCCESS;
  }

}

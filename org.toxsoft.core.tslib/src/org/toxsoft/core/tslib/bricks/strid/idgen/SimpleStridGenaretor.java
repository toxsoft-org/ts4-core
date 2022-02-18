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
import org.toxsoft.core.tslib.math.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Simple IDname STRID generator.
 * <p>
 * Creates STRIDs like "<b>Prefix</b>NNN" where NNN is increasing non-negative <code>long</code> number. Prefix itself
 * must be a valid IDname.
 * <p>
 * Class is <b>not</b> thread-safe.
 *
 * @author hazard157
 */
public final class SimpleStridGenaretor
    implements IStridGenerator {

  /**
   * Default prefix of generated STRIDs.
   */
  public static final String DEFAULT_PREFIX = "id_"; //$NON-NLS-1$

  /**
   * Initial default value of the counter.
   * <p>
   * Please note that 1 (already increased number) will be used for the first STRID creation.
   */
  public static final long DEFAULT_INITIAL_COUNTER = 0;

  /**
   * State option: prefix string.
   */
  public static final IDataDef OPDEF_PREFIX = DataDef.create( "Prefix", STRING, //$NON-NLS-1$
      TSID_DEFAULT_VALUE, avStr( DEFAULT_PREFIX ) );

  /**
   * State option: counter value.
   */
  public static final IDataDef OPDEF_COUNTER = DataDef.create( "Counter", INTEGER, //$NON-NLS-1$
      TSID_DEFAULT_VALUE, avInt( DEFAULT_INITIAL_COUNTER ) );

  /**
   * State option: with od zero padded numbering.<br>
   * Usage: padding with leading zeroes (format string "%0Nd") is used for counter formating to the ID text. The number
   * N is the pad width. Pad width may be in range 2..20, the numbers outside this reange means that padding is not
   * used.<br>
   * Default value: 0 (no padding)
   */
  public static final IDataDef OPDEF_PAD_WIDTH = DataDef.create( "PadWidth", INTEGER, //$NON-NLS-1$
      TSID_DEFAULT_VALUE, AV_0 );

  /**
   * Default initial state of the generator.
   */
  public static final IOptionSet DEFAULT_INITIAL_STATE = OptionSetUtils.createOpSet( //
      OPDEF_PREFIX.id(), OPDEF_PREFIX.defaultValue(), //
      OPDEF_COUNTER.id(), OPDEF_COUNTER.defaultValue(), //
      OPDEF_PAD_WIDTH.id(), OPDEF_PAD_WIDTH.defaultValue() //
  );

  private static final IntRange PAD_WIDTH_RANGE = new IntRange( 2, 20 );

  /**
   * Initial state of the generator.
   */
  private final IOptionSet initialState;

  /**
   * Current state of the generator.
   */
  private final IOptionSetEdit state = new OptionSet();

  /**
   * Format string for STRID generation, is initialized in {@link #setState(IOptionSet)}.
   */
  private String fmtStr = null;

  // ------------------------------------------------------------------------------------
  // Constructors
  //

  /**
   * Creates generator with default state {@link #DEFAULT_INITIAL_STATE}.
   */
  public SimpleStridGenaretor() {
    initialState = DEFAULT_INITIAL_STATE;
    setState( initialState );
  }

  /**
   * Creates generator with specified state
   *
   * @param aInitialState {@link IOptionSet} - initial state
   * @throws TsNullArgumentRtException aContextPrefix = 0
   * @throws TsValidationFailedRtException failed {@link #validateState(IOptionSet)}
   */
  public SimpleStridGenaretor( IOptionSet aInitialState ) {
    initialState = new OptionSet( aInitialState );
    setState( initialState );
  }

  /**
   * Creates state fur further usage in constructor or for state change.
   * <p>
   * Created state may not be valid, use {@link #validateState(IOptionSet)} to check for validity.
   *
   * @param aPrefix String - prefix (an IDname)
   * @param aSeed long - counter initial value
   * @param aPadWidth int - zero padded number text width, see {@link #OPDEF_PAD_WIDTH} for explaination
   * @return {@link IOptionSet} - created state, may not be valid
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static IOptionSet createState( String aPrefix, long aSeed, int aPadWidth ) {
    IOptionSetEdit ops = new OptionSet();
    ops.setStr( OPDEF_PREFIX, aPrefix );
    ops.setLong( OPDEF_COUNTER, aSeed );
    ops.setInt( OPDEF_PAD_WIDTH, aPadWidth );
    return ops;
  }

  // ------------------------------------------------------------------------------------
  // IStridGenerator
  //

  @SuppressWarnings( "boxing" )
  @Override
  public String nextId() {
    long counter = OPDEF_COUNTER.getValue( state ).asLong();
    TsIllegalStateRtException.checkTrue( counter == Long.MAX_VALUE );
    ++counter;
    OPDEF_COUNTER.setValue( state, avInt( counter ) );
    return String.format( fmtStr, counter );
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
    String prefix = OPDEF_PREFIX.getValue( state ).asString();
    int padWidth = OPDEF_PAD_WIDTH.getValue( state ).asInt();
    if( PAD_WIDTH_RANGE.isInRange( padWidth ) ) {
      fmtStr = String.format( "%s%%0%dd", prefix, padWidth ); //$NON-NLS-1$
    }
    else {
      fmtStr = String.format( "%s%%d", prefix ); //$NON-NLS-1$
    }
  }

  @SuppressWarnings( "boxing" )
  @Override
  public ValidationResult validateState( IOptionSet aState ) {
    long counter = OPDEF_COUNTER.getValue( aState ).asLong();
    if( counter < 0 ) {
      return ValidationResult.error( FMT_ERR_SG_NEGATIVE_COUNTER, counter );
    }
    if( counter == Long.MAX_VALUE ) {
      return ValidationResult.error( FMT_ERR_SG_COUNTER_MAX, counter );
    }
    String prefix = OPDEF_PREFIX.getValue( aState ).asString();
    if( !StridUtils.isValidIdName( prefix ) ) {
      return ValidationResult.error( FMT_ERR_SG_NON_IDNAME_PREFIX, prefix );
    }
    return ValidationResult.SUCCESS;
  }

}

package org.toxsoft.core.tslib.bricks.strid.idgen;

import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.tslib.bricks.strid.idgen.ITsResources.*;

import java.util.*;

import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Strid geberaten based on UUID generation by {@link UUID#randomUUID()}.
 * <p>
 * Createds STRIDs like "uuid_f2d8afc1_6dd8_4dc6_ad26_dc701284e43c". As a prefix "<i>uuid_</i>" any valid IDpath may be
 * used.
 * <p>
 * State of the generator includes only prefix string.
 *
 * @author goga
 */
public class UuidStridGenerator
    implements IStridGenerator {

  /**
   * Default prefix of generated STRIDs.
   */
  private static final String DEFAULT_PREFIX = "uuid_"; //$NON-NLS-1$

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

  // ------------------------------------------------------------------------------------
  // Constructors
  //

  /**
   * Creates generator with default state {@link #DEFAULT_INITIAL_STATE}.
   */
  public UuidStridGenerator() {
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
  public UuidStridGenerator( IOptionSet aInitialState ) {
    initialState = new OptionSet( aInitialState );
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
    IOptionSetEdit ops = new OptionSet();
    ops.setStr( OPDEF_PREFIX, aPrefix );
    return ops;
  }

  // ------------------------------------------------------------------------------------
  // IStridGenerator
  //

  @Override
  public String nextId() {
    UUID uuid = UUID.randomUUID();
    String prefix = OPDEF_PREFIX.getValue( state ).asString();
    return prefix + uuid.toString().replace( '-', '_' );
  }

  @Override
  public IOptionSet getInitialState() {
    return new OptionSet( initialState );
  }

  @Override
  public IOptionSet getState() {
    return state;
  }

  @Override
  public void setState( IOptionSet aState ) {
    TsValidationFailedRtException.checkWarn( validateState( aState ) );
    state.setAll( aState );
  }

  @Override
  public ValidationResult validateState( IOptionSet aState ) {
    TsNullArgumentRtException.checkNull( aState );
    String prefix = OPDEF_PREFIX.getValue( aState ).asString();
    if( !StridUtils.isValidIdPath( prefix ) ) {
      return ValidationResult.error( FMT_ERR_UU_NON_IDPATH_PREFIX, prefix );
    }
    return ValidationResult.SUCCESS;
  }

}

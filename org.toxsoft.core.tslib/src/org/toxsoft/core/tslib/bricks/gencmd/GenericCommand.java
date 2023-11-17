package org.toxsoft.core.tslib.bricks.gencmd;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Generic command is and ID and argument options set.
 *
 * @author hazard157
 */
public final class GenericCommand {

  /**
   * "None" command singleton.
   */
  public static final GenericCommand NONE = new GenericCommand( IStridable.NONE_ID, IOptionSet.NULL );

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "GenericCommand"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<GenericCommand> KEEPER =
      new AbstractEntityKeeper<>( GenericCommand.class, EEncloseMode.ENCLOSES_BASE_CLASS, NONE ) {

        @Override
        protected void doWrite( IStrioWriter aSw, GenericCommand aEntity ) {
          aSw.writeAsIs( aEntity.cmdId );
          aSw.writeSeparatorChar();
          OptionSetKeeper.KEEPER.write( aSw, aEntity.args );
        }

        @Override
        protected GenericCommand doRead( IStrioReader aSr ) {
          String cmdId = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          IOptionSet args = OptionSetKeeper.KEEPER.read( aSr );
          return new GenericCommand( cmdId, args );
        }

      };

  private final String     cmdId;
  private final IOptionSet args;

  private GenericCommand( String aCmdId, IOptionSet aArgs ) {
    cmdId = aCmdId;
    args = aArgs;
  }

  /**
   * Static constructor.
   *
   * @param aCmdId String - the command ID
   * @param aArgs {@link IOptionSet} - the command arguments
   * @return {@link GenericCommand} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not IDpath
   */
  public static GenericCommand of( String aCmdId, IOptionSet aArgs ) {
    StridUtils.checkValidIdPath( aCmdId );
    TsNullArgumentRtException.checkNull( aArgs );
    if( aArgs.isEmpty() ) {
      return new GenericCommand( aCmdId, IOptionSet.NULL );
    }
    return new GenericCommand( aCmdId, new OptionSet( aArgs ) );
  }

  /**
   * Static constructor of the command with no arguments.
   *
   * @param aCmdId String - the command ID
   * @return {@link GenericCommand} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not IDpath
   */
  public static GenericCommand of( String aCmdId ) {
    StridUtils.checkValidIdPath( aCmdId );
    return new GenericCommand( aCmdId, IOptionSet.NULL );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the command ID.
   *
   * @return String - the command ID, always an IDpath
   */
  public String cmdId() {
    return cmdId;
  }

  /**
   * Returns the command arguments.
   *
   * @return {@link IOptionSet} - the command arguments
   */
  public IOptionSet args() {
    return args;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return cmdId + '-' + args.toString();
  }

  @Override
  public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    if( aThat instanceof GenericCommand that ) {
      return cmdId.equals( that.cmdId ) && args.equals( that.args );
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + cmdId.hashCode();
    result = TsLibUtils.PRIME * result + args.hashCode();
    return result;
  }

}

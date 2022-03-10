package org.toxsoft.core.tslib.bricks.events.msg;

import java.io.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The generic message class may be used for message queueing, storing, serializing, etc.
 * <p>
 * Genegic message had an ID {@link #messageId()} and arguments {@link #args()}.
 * <p>
 * This is immutable class.
 *
 * @author hazard157
 */
public sealed class GenericMessage
    implements Serializable permits GtMessage {

  private static final long serialVersionUID = -599836453604892829L;

  /**
   * No message singleton.
   */
  public static final GenericMessage NONE = new GenericMessage( IStridable.NONE_ID );

  /**
   * Keepr singleton.
   */
  public static final IEntityKeeper<GenericMessage> KEEPER =
      new AbstractEntityKeeper<>( GenericMessage.class, EEncloseMode.ENCLOSES_BASE_CLASS, NONE ) {

        @Override
        protected void doWrite( IStrioWriter aSw, GenericMessage aEntity ) {
          aSw.writeAsIs( aEntity.messageId() );
          aSw.writeSeparatorChar();
          OptionSetKeeper.KEEPER.write( aSw, aEntity.args() );
        }

        @Override
        protected GenericMessage doRead( IStrioReader aSr ) {
          String messageId = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          IOptionSet args = OptionSetKeeper.KEEPER.read( aSr );
          return new GenericMessage( messageId, args, 0 );
        }
      };

  private final String     messageId;
  private final IOptionSet args;

  /**
   * Constructor.
   *
   * @param aMessageId String - the message ID
   * @param aArgs {@link IOptionSet} - arguments, will be copied to internal set
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the ID is not an IDpath
   */
  public GenericMessage( String aMessageId, IOptionSet aArgs ) {
    messageId = aMessageId;
    IOptionSetEdit tmp = new OptionSet();
    tmp.addAll( aArgs );
    args = tmp;
  }

  /**
   * Constructor.
   *
   * @param aMessageId String - the message ID
   * @param aIdsAndValues Object[] - identifier / value pairs as for {@link OptionSetUtils#createOpSet(Object...)}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the ID is not an IDpath
   * @throws TsIllegalArgumentRtException number of elements in array is uneven
   * @throws ClassCastException argument types convention is violated
   */
  public GenericMessage( String aMessageId, Object... aIdsAndValues ) {
    messageId = aMessageId;
    args = OptionSetUtils.createOpSet( aIdsAndValues );
  }

  /**
   * Constructor of message with an empty arguments.
   *
   * @param aMessageId String - the message ID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the ID is not an IDpath
   */
  public GenericMessage( String aMessageId ) {
    messageId = aMessageId;
    args = IOptionSet.NULL;
  }

  /**
   * Internal constructor for {@link #KEEPER}.
   *
   * @param aMessageId String - the message ID
   * @param aArgs {@link IOptionSet} - arguments, will be copied to internal set
   * @param aFoo int - unused argument for unique signature
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the ID is not an IDpath
   */
  protected GenericMessage( String aMessageId, IOptionSet aArgs, int aFoo ) {
    messageId = aMessageId;
    args = aArgs;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the message ID.
   *
   * @return String - the message ID, an IDpath
   */
  public String messageId() {
    return messageId;
  }

  /**
   * Returns the message arguments.
   *
   * @return {@link IOptionSet} - the message arguments
   */
  public IOptionSet args() {
    return args;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return messageId + '-' + args.toString();
  }

  @Override
  public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    if( aThat instanceof GenericMessage that ) {
      return this.messageId.equals( that.messageId ) && this.args.equals( that.args );
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + messageId.hashCode();
    result = TsLibUtils.PRIME * result + args.hashCode();
    return result;
  }

}

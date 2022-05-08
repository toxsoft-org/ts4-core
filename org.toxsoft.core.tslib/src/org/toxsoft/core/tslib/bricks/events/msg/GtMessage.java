package org.toxsoft.core.tslib.bricks.events.msg;

import static org.toxsoft.core.tslib.ITsHardConstants.*;

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
 * {@link GenericMessage} extension with the topic.
 * <p>
 * {@link GtMessage} in addition to the {@link GenericMessage} fields has a topic ID {@link #topicId()}.
 * <p>
 * This is immutable class.
 *
 * @author hazard157
 */
public final class GtMessage
    extends GenericMessage {

  private static final long serialVersionUID = -599836453604892829L;

  /**
   * Topic ID for broadcast messages, delivered to any topic listeners.
   */
  public static final String TOPIC_ID_BROADCAST = TS_ID + ".Topic.Broadcast"; //$NON-NLS-1$

  /**
   * No message singleton.
   */
  @SuppressWarnings( "hiding" )
  public static final GtMessage NONE = new GtMessage( IStridable.NONE_ID, IStridable.NONE_ID );

  /**
   * Keeper singleton.
   */
  @SuppressWarnings( "hiding" )
  public static final IEntityKeeper<GtMessage> KEEPER =
      new AbstractEntityKeeper<>( GtMessage.class, EEncloseMode.ENCLOSES_BASE_CLASS, NONE ) {

        @Override
        protected void doWrite( IStrioWriter aSw, GtMessage aEntity ) {
          aSw.writeAsIs( aEntity.topicId() );
          aSw.writeSeparatorChar();
          aSw.writeAsIs( aEntity.messageId() );
          aSw.writeSeparatorChar();
          OptionSetKeeper.KEEPER.write( aSw, aEntity.args() );
        }

        @Override
        protected GtMessage doRead( IStrioReader aSr ) {
          String topicId = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          String messageId = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          IOptionSet args = OptionSetKeeper.KEEPER.read( aSr );
          return new GtMessage( topicId, messageId, args, 0 );
        }
      };

  private final String topicId;

  /**
   * Constructor.
   *
   * @param aTopicId String - the topic ID (IDpath)
   * @param aMessageId String - the message ID
   * @param aArgs {@link IOptionSet} - arguments, will be copied to internal set
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any ID is not an IDpath
   */
  public GtMessage( String aTopicId, String aMessageId, IOptionSet aArgs ) {
    super( aMessageId, aArgs );
    topicId = StridUtils.checkValidIdPath( aTopicId );
  }

  /**
   * Constructor.
   *
   * @param aTopicId String - the topic ID (IDpath)
   * @param aMessageId String - the message ID
   * @param aIdsAndValues Object[] - identifier / value pairs as for {@link OptionSetUtils#createOpSet(Object...)}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any ID is not an IDpath
   * @throws TsIllegalArgumentRtException number of elements in array is uneven
   * @throws ClassCastException argument types convention is violated
   */
  public GtMessage( String aTopicId, String aMessageId, Object... aIdsAndValues ) {
    super( aMessageId, aIdsAndValues );
    topicId = StridUtils.checkValidIdPath( aTopicId );
  }

  /**
   * Constructor of message with an empty arguments.
   *
   * @param aTopicId String - the topic ID (IDpath)
   * @param aMessageId String - the message ID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException and ID is not an IDpath
   */
  public GtMessage( String aTopicId, String aMessageId ) {
    super( aMessageId );
    topicId = StridUtils.checkValidIdPath( aTopicId );
  }

  /**
   * Internal constructor for {@link #KEEPER}.
   *
   * @param aTopicId String - the topic ID (IDpath)
   * @param aMessageId String - the message ID
   * @param aArgs {@link IOptionSet} - arguments, will be copied to internal set
   * @param aFoo int - unused argument for unique signature
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the ID is not an IDpath
   */
  GtMessage( String aTopicId, String aMessageId, IOptionSet aArgs, int aFoo ) {
    super( aMessageId, aArgs, aFoo );
    topicId = aTopicId;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the topic ID.
   *
   * @return String - the topic ID, an IDpath
   */
  public String topicId() {
    return topicId;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return topicId + " - " + super.toString(); //$NON-NLS-1$
  }

  @Override
  public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    if( aThat instanceof GtMessage that ) {
      return this.topicId.equals( that.topicId ) && super.equals( that );
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + super.hashCode();
    result = TsLibUtils.PRIME * result + topicId.hashCode();
    return result;
  }

}

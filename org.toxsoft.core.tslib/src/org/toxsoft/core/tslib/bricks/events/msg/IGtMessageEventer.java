package org.toxsoft.core.tslib.bricks.events.msg;

import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.txtmatch.*;

/**
 * Interface of service generating {@link GtMessage} event.
 *
 * @author hazard157
 */
public interface IGtMessageEventer
    extends ITsEventer<IGtMessageListener> {

  /**
   * Add the listener to all topics.
   * <p>
   * Prevous registreation of <code>aListener</code> will be cancelled.
   *
   * @param aListener {@link IGtMessageListener} - the listener
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  @Override
  void addListener( IGtMessageListener aListener );

  /**
   * Add listener to the specified topics.
   * <p>
   * Listened topics will be determined by the by the {@link TextMatcher#match(String)} invocation on topic ID.
   * <p>
   * Prevous registreation of <code>aListener</code> will be cancelled.
   *
   * @param aListener {@link IGtMessageListener} - the listener
   * @param aTopicIdMatcher {@link ITsFilter}&lt;String&gt; - tpoic IDs selector
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setListener( IGtMessageListener aListener, ITsFilter<String> aTopicIdMatcher );

  // ------------------------------------------------------------------------------------
  // Convinience inline methods
  //

  /**
   * Wraps {@link #setListener(IGtMessageListener, ITsFilter)} for single topic ID listening.
   * <p>
   * Prevous registreation of <code>aListener</code> will be cancelled.
   *
   * @param aListener {@link IGtMessageListener} - the listener
   * @param aTopicId String - the topic ID to listen
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException topic ID is not an IDpath
   */
  default void setListener( IGtMessageListener aListener, String aTopicId ) {
    StridUtils.checkValidIdPath( aTopicId );
    TextMatcher tm = new TextMatcher( ETextMatchMode.EXACT, aTopicId );
    setListener( aListener, tm );
  }

}

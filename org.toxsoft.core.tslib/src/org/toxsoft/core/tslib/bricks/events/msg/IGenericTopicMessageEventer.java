package org.toxsoft.core.tslib.bricks.events.msg;

import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Interface of service generating {@link GenericTopicMessage} event.
 *
 * @author hazard157
 */
public interface IGenericTopicMessageEventer
    extends ITsEventer<IGenericTopicMessageListener> {

  /**
   * Add the listener to all topics.
   *
   * @param aListener &lt;L&gt; - the listener
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  @Override
  void addListener( IGenericTopicMessageListener aListener );

  /**
   * Add listener to the specified topics.
   *
   * @param aTopicIds {@link IStringList} - the topic IDs
   * @param aListener {@link IGenericMessageListener} - the listener to add
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException at least one topic ID is not an IDpath
   */
  void addListener( IStringList aTopicIds, IGenericTopicMessageListener aListener );

}

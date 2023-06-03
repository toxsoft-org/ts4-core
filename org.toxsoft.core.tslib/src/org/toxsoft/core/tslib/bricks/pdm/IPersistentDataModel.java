package org.toxsoft.core.tslib.bricks.pdm;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The concept of editable data model for MVC pattern with external data storage supprt.
 * <p>
 * TODO description, what is content, memento, why persistable, etc.
 * <p>
 * TODO usage of the DM
 *
 * @author hazard157
 */
public interface IPersistentDataModel
    extends IGenericChangeEventCapable, ITsClearableEx, ITsValidatable {

  /**
   * Creates new instance of the empty (cleared) content of this data model.
   *
   * @return {@link IPdmMemento} - new instance of the empty content
   */
  IPdmContent createClearPdmContent();

  /**
   * Returns the persistable copy of the data model content.
   *
   * @return {@link IPdmContent} - the persistable copy of the DM content
   */
  IPdmContent getPdmContent();

  /**
   * Sets the content o9f this data model.
   *
   * @param aContent {@link IPdmContent} - content to be set
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the is not a content of this data model
   */
  void setPdmContent( IPdmContent aContent );

  /**
   * Determines if memento is supported.
   * <p>
   * If memento is not supported call to {@link #getMemento()} and {@link #setMemento(IPdmMemento)} will throw an
   * exception.
   *
   * @return boolean - <code>true</code> memento can be used by caller
   */
  boolean isMemetoSupported();

  /**
   * Returns the new instance of the memento - copy of the current content of this data model.
   *
   * @return {@link IPdmMemento} - new instance of the memento
   * @throws TsUnsupportedFeatureRtException memento is not supported
   */
  IPdmMemento getMemento();

  /**
   * Sets the data model content from the memento.
   * <p>
   * The only way to get memento of the data model is the method {@link #getMemento()}.
   *
   * @param aMemento {@link IPdmMemento} - the memento
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument is not a memento of this data model
   * @throws TsUnsupportedFeatureRtException memento is not supported
   */
  void setMemento( IPdmMemento aMemento );

}

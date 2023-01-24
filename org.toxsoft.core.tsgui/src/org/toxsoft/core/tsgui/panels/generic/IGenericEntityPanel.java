package org.toxsoft.core.tsgui.panels.generic;

/**
 * Generci panel to display propertis of one entity.
 *
 * @author hazard157
 * @param <T> - the entity type
 */
public interface IGenericEntityPanel<T>
    extends IGenericContentPanel {

  /**
   * Sets entity to be displayed in panel.
   *
   * @param aEntity &lt;T&gt; - the displayed entity, may be <code>null</code>
   */
  void setEntity( T aEntity );

}

package org.toxsoft.core.tsgui.panels.opsedit.group;

/**
 * Listener to the section values change.
 *
 * @author hazard157
 */
public interface ISectionValuesChangeListener {

  /**
   * The singleton of the dummy listener.
   */
  ISectionValuesChangeListener NONE = ( aSection, aOptionId ) -> {
    // nop
  };

  /**
   * Called when value of the section is changed.
   *
   * @param aSection {@link ISectionDef} - the section
   * @param aOptionId String - ID of the option in {@link ISectionDef#values()}
   */
  void onSectionValueChange( ISectionDef aSection, String aOptionId );

}

package org.toxsoft.core.txtproj.lib.sfv;

import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;

/**
 * Section read from text file by {@link StrioUtils#readSections(IStrioReader)}.
 * <p>
 * {@link Comparable} case <b>in</b>-sensitive compares {@link #sectionId()}.
 *
 * @author hazard157
 */
public interface ISfvSection
    extends Comparable<ISfvSection> {

  /**
   * Returns the section ID.
   *
   * @return String - the section ID is an IDpath
   */
  String sectionId();

  /**
   * Returns the section content.
   * <p>
   * Section content starts by opening bracket and finshed with ending bracket.
   *
   * @return String - the section content may be really huge string
   */
  String sectionContent();

}

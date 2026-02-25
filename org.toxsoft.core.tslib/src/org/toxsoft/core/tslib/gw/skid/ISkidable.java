package org.toxsoft.core.tslib.gw.skid;

/**
 * Mix-in interface of entities identified by the SKID.
 *
 * @author hazard157
 */
public interface ISkidable {

  /**
   * Returns an entity SKID.
   *
   * @return {@link Skid} - the skid
   */
  Skid skid();

}

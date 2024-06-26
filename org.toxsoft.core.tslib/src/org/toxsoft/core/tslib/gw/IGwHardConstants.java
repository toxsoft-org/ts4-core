package org.toxsoft.core.tslib.gw;

/**
 * The Green World constants.
 * <p>
 * TODO we need more detailed explanation of the colored worlds:
 * <ul>
 * <li>Red World - real world, the reality around us;</li>
 * <li>Green World - the Red World model, contains programming language independent concepts;</li>
 * <li>Blue World - an implementation of Green World in the Java programming language.</li>
 * </ul>
 *
 * @author hazard157
 */
public interface IGwHardConstants {

  /**
   * The root class identifier.
   */
  String GW_ROOT_CLASS_ID = "SkObject"; //$NON-NLS-1$

  /**
   * Keyword: class.
   */
  String GW_KEYWORD_CLASS = "class"; //$NON-NLS-1$

  /**
   * Keyword: attribute.
   */
  String GW_KEYWORD_ATTR = "attr"; //$NON-NLS-1$

  /**
   * Keyword: rivet.
   */
  String GW_KEYWORD_RIVET = "rivet"; //$NON-NLS-1$

  /**
   * Keyword: CLOB.
   */
  String GW_KEYWORD_CLOB = "clob"; //$NON-NLS-1$

  /**
   * Keyword: RT-data.
   */
  String GW_KEYWORD_RTDATA = "rtdata"; //$NON-NLS-1$

  /**
   * Keyword: link.
   */
  String GW_KEYWORD_LINK = "link"; //$NON-NLS-1$

  /**
   * Keyword: command.
   */
  String GW_KEYWORD_CMD = "cmd"; //$NON-NLS-1$

  /**
   * Keyword: command argument.
   */
  String GW_KEYWORD_CMD_ARG = "arg"; //$NON-NLS-1$

  /**
   * Keyword: event.
   */
  String GW_KEYWORD_EVENT = "event"; //$NON-NLS-1$

  /**
   * Keyword: event parameter.
   */
  String GW_KEYWORD_EVENT_PARAM = "prm"; //$NON-NLS-1$

}

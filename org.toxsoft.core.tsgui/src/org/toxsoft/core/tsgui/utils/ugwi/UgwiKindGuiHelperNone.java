package org.toxsoft.core.tsgui.utils.ugwi;

import org.toxsoft.core.tslib.gw.ugwi.kind.*;

/**
 * GUI helper for UGWI kind {@link UgwiKindNone#KIND_ID}.
 *
 * @author hazard157
 */
public class UgwiKindGuiHelperNone
    extends UgwiKindGuiHelper {

  /**
   * The registered kind ID.
   */
  public static final String KIND_ID = "none"; //$NON-NLS-1$

  /**
   * The singleton instance.
   */
  public static final IUgwiKindGuiHelper INSTANCE = new UgwiKindGuiHelperNone();

  /**
   * Constructor.
   */
  public UgwiKindGuiHelperNone() {
    super( UgwiKindNone.KIND_ID );
  }

  // ------------------------------------------------------------------------------------
  // AbstractUgwiKindGuiHelper
  //

}

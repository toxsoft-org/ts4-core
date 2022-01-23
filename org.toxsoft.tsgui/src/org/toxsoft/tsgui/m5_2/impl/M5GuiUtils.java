package org.toxsoft.tsgui.m5_2.impl;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.toxsoft.tsgui.dialogs.datarec.ITsDialogInfo;
import org.toxsoft.tsgui.m5_2.*;
import org.toxsoft.tslib.utils.errors.TsUnderDevelopmentRtException;

/**
 * M5 GUI helper methods.
 *
 * @author hazard157
 */
public class M5GuiUtils {

  public static <T> T askCreate( IEclipseContext aAppContext, IM5Model<T> aModel, IM5Bunch<T> aValues,
      ITsDialogInfo aDialogInfo, IM5LifecycleManager<T> aLifecycleManager ) {

    // TODO реализовать M5GuiUtils.M5GuiUtils()
    throw new TsUnderDevelopmentRtException( "M5GuiUtils.M5GuiUtils()" );

  }

  /**
   * No subclassing.
   */
  private M5GuiUtils() {
    // nop
  }

}

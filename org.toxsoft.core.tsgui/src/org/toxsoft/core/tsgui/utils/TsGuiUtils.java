package org.toxsoft.core.tsgui.utils;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.fonts.impl.FontInfo;
import org.toxsoft.core.tsgui.graphics.image.EThumbSize;
import org.toxsoft.core.tsgui.utils.layout.EBorderLayoutPlacement;
import org.toxsoft.core.tsgui.utils.rectfit.ERectFitMode;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tsgui.widgets.mpv.EMpvTimeLen;
import org.toxsoft.core.tslib.utils.errors.TsIllegalStateRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.core.tslib.utils.valobj.TsValobjUtils;

/**
 * General purpose utility constants and methods.
 *
 * @author hazard157
 */
public class TsGuiUtils {

  /**
   * Holds windows level {@link IEclipseContext} assosiated to the GUI-thread.
   */
  private static final ThreadLocal<IEclipseContext> guiThreadWinContext = new ThreadLocal<>();

  /**
   * Performs tsgui core library initializtion.
   */
  public static void initializeTsGuiCore() {
    TsValobjUtils.registerKeeper( FontInfo.KEEPER_ID, FontInfo.KEEPER );
    TsValobjUtils.registerKeeper( PointKeeper.KEEPER_ID, PointKeeper.KEEPER );
    TsValobjUtils.registerKeeper( RGBKeeper.KEEPER_ID, RGBKeeper.KEEPER );
    TsValobjUtils.registerKeeper( RGBAKeeper.KEEPER_ID, RGBAKeeper.KEEPER );
    TsValobjUtils.registerKeeper( EBorderLayoutPlacement.KEEPER_ID, EBorderLayoutPlacement.KEEPER );
    TsValobjUtils.registerKeeper( EThumbSize.KEEPER_ID, EThumbSize.KEEPER );
    TsValobjUtils.registerKeeper( EBorderType.KEEPER_ID, EBorderType.KEEPER );
    TsValobjUtils.registerKeeper( EHorAlignment.KEEPER_ID, EHorAlignment.KEEPER );
    TsValobjUtils.registerKeeper( EVerAlignment.KEEPER_ID, EVerAlignment.KEEPER );
    TsValobjUtils.registerKeeper( EMpvTimeLen.KEEPER_ID, EMpvTimeLen.KEEPER );
    TsValobjUtils.registerKeeper( ERectFitMode.KEEPER_ID, ERectFitMode.KEEPER );
    TsValobjUtils.registerKeeper( ETsFulcrum.KEEPER_ID, ETsFulcrum.KEEPER );
    TsValobjUtils.registerKeeper( ETsOrientation.KEEPER_ID, ETsOrientation.KEEPER );
  }

  /**
   * Stores windows level {@link IEclipseContext} assosiated to the GUI-thread.
   * <p>
   * This method must be called once per GUI-thread as soon as possibe after thread and cpntext is created.
   *
   * @param aWinContext {@link IEclipseContext} - windows level context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static final void storeGuiThreadWinContext( IEclipseContext aWinContext ) {
    TsNullArgumentRtException.checkNull( aWinContext );
    guiThreadWinContext.set( aWinContext );
  }

  /**
   * Returns the context previously stored by {@link #storeGuiThreadWinContext(IEclipseContext)}.
   * <p>
   * Warning: this method must be called from GUI-thread.
   *
   * @return {@link IEclipseContext} - context assosiated with current GUI-thread
   * @throws TsIllegalStateRtException non-GUI thread call or context was not stored yet
   */
  public static final IEclipseContext getGuiThreadWinContext() {
    IEclipseContext ctx = guiThreadWinContext.get();
    TsIllegalStateRtException.checkNull( ctx );
    return ctx;
  }

  /**
   * Prohibition of descendants creation.
   */
  private TsGuiUtils() {
    // nop
  }

}
package org.toxsoft.core.tsgui.utils;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.fonts.impl.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.utils.margins.*;
import org.toxsoft.core.tsgui.utils.rectfit.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tsgui.valed.controls.basic.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tsgui.widgets.mpv.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.valobj.*;

/**
 * General purpose utility constants and methods.
 *
 * @author hazard157
 */
public class TsGuiUtils {

  /**
   * Holds windows level {@link IEclipseContext} associated to the GUI-thread.
   */
  // private static final ThreadLocal<IEclipseContext> guiThreadWinContext = new ThreadLocal<>();

  /**
   * Performs TsGUI core library initialization.
   */
  public static void initializeTsGuiCore() {
    TsValobjUtils.registerKeeper( FontInfo.KEEPER_ID, FontInfo.KEEPER );
    TsValobjUtils.registerKeeper( PointKeeper.KEEPER_ID, PointKeeper.KEEPER );
    TsValobjUtils.registerKeeper( RGBKeeper.KEEPER_ID, RGBKeeper.KEEPER );
    TsValobjUtils.registerKeeper( RGBAKeeper.KEEPER_ID, RGBAKeeper.KEEPER );
    TsValobjUtils.registerKeeper( EBorderLayoutPlacement.KEEPER_ID, EBorderLayoutPlacement.KEEPER );
    TsValobjUtils.registerKeeper( EThumbSize.KEEPER_ID, EThumbSize.KEEPER );
    TsValobjUtils.registerKeeper( EIconSize.KEEPER_ID, EIconSize.KEEPER );
    TsValobjUtils.registerKeeper( EBorderType.KEEPER_ID, EBorderType.KEEPER );
    TsValobjUtils.registerKeeper( EHorAlignment.KEEPER_ID, EHorAlignment.KEEPER );
    TsValobjUtils.registerKeeper( EVerAlignment.KEEPER_ID, EVerAlignment.KEEPER );
    TsValobjUtils.registerKeeper( EMpvTimeLen.KEEPER_ID, EMpvTimeLen.KEEPER );
    TsValobjUtils.registerKeeper( ERectFitMode.KEEPER_ID, ERectFitMode.KEEPER );
    TsValobjUtils.registerKeeper( ETsFulcrum.KEEPER_ID, ETsFulcrum.KEEPER );
    TsValobjUtils.registerKeeper( ETsOrientation.KEEPER_ID, ETsOrientation.KEEPER );
    TsValobjUtils.registerKeeper( ETsColor.KEEPER_ID, ETsColor.KEEPER );
    TsValobjUtils.registerKeeper( ETsLineCapStyle.KEEPER_ID, ETsLineCapStyle.KEEPER );
    TsValobjUtils.registerKeeper( ETsLineJoinStyle.KEEPER_ID, ETsLineJoinStyle.KEEPER );
    TsValobjUtils.registerKeeper( ETsLineType.KEEPER_ID, ETsLineType.KEEPER );
    TsValobjUtils.registerKeeper( TsLineInfo.KEEPER_ID, TsLineInfo.KEEPER );
    TsValobjUtils.registerKeeper( TsBorderInfo.KEEPER_ID, TsBorderInfo.KEEPER );
    TsValobjUtils.registerKeeper( ETsOutlineKind.KEEPER_ID, ETsOutlineKind.KEEPER );
    TsValobjUtils.registerKeeper( EGradientType.KEEPER_ID, EGradientType.KEEPER );
    TsValobjUtils.registerKeeper( TsMargins.KEEPER_ID, TsMargins.KEEPER );
    TsValobjUtils.registerKeeper( TsGridMargins.KEEPER_ID, TsGridMargins.KEEPER );
    TsValobjUtils.registerKeeper( TsImageDescriptor.KEEPER_ID, TsImageDescriptor.KEEPER );
    TsValobjUtils.registerKeeper( TsFillInfo.KEEPER_ID, TsFillInfo.KEEPER );
    TsValobjUtils.registerKeeper( ETsBorderKind.KEEPER_ID, ETsBorderKind.KEEPER );

    // following this is some kind of hacking!
    DataDef dd = (DataDef)IAvMetaConstants.DDEF_DESCRIPTION;
    dd.params().setInt( IValedControlConstants.OPDEF_VERTICAL_SPAN, 3 );
    dd.params().setStr( IValedControlConstants.OPDEF_EDITOR_FACTORY_NAME, ValedAvStringText.FACTORY_NAME );
    dd.params().setBool( ValedStringText.OPDEF_IS_MULTI_LINE, true );

  }

  /**
   * Prepares data definitions for the specified options values.
   * <p>
   * The simplest possible data definitions are created for unknown option, for known option IDs the default data
   * definitions are used.
   *
   * @param aValues {@link IOptionSet} - the option values
   * @return {@link IStridablesList}&lt;{@link IDataDef}&gt; - data definitions
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static IStridablesList<IDataDef> prepareDefaultDefs( IOptionSet aValues ) {
    IStridablesListEdit<IDataDef> ll = new StridablesList<>();
    for( String id : aValues.keys() ) {
      IAtomicValue av = aValues.getValue( id );
      IDataDef def = switch( id ) {
        case TSID_NAME -> DDEF_NAME;
        case TSID_DESCRIPTION -> DDEF_DESCRIPTION;
        default -> DataDef.create( id, av.atomicType(), //
            TSID_NAME, id, //
            OPDEF_EDITOR_FACTORY_NAME, ValedControlUtils.getDefaultFactoryName( av.atomicType() ) //
          );
      };
      ll.add( def );
    }
    return ll;
  }

  // /**
  // * Stores windows level {@link IEclipseContext} associated to the GUI-thread.
  // * <p>
  // * This method must be called once per GUI-thread as soon as possibe after thread and cpntext is created.
  // *
  // * @param aWinContext {@link IEclipseContext} - windows level context
  // * @throws TsNullArgumentRtException any argument = <code>null</code>
  // */
  // public static final void storeGuiThreadWinContext( IEclipseContext aWinContext ) {
  // TsNullArgumentRtException.checkNull( aWinContext );
  // guiThreadWinContext.set( aWinContext );
  // }
  //
  // /**
  // * Returns the context previously stored by {@link #storeGuiThreadWinContext(IEclipseContext)}.
  // * <p>
  // * Warning: this method must be called from GUI-thread.
  // *
  // * @return {@link IEclipseContext} - context associated with current GUI-thread
  // * @throws TsIllegalStateRtException non-GUI thread call or context was not stored yet
  // */
  // public static final IEclipseContext getGuiThreadWinContext() {
  // IEclipseContext ctx = guiThreadWinContext.get();
  // TsIllegalStateRtException.checkNull( ctx );
  // return ctx;
  // }
  //
  // /**
  // * Finds the context previously stored by {@link #storeGuiThreadWinContext(IEclipseContext)}.
  // * <p>
  // * Method returns <code>null</code> either if called not from mainGUI thread or if called before
  // * {@link #storeGuiThreadWinContext(IEclipseContext)}.
  // *
  // * @return {@link IEclipseContext} - context associated with current GUI-thread or <code>null</code>
  // */
  // public static final IEclipseContext findGuiThreadWinContext() {
  // return guiThreadWinContext.get();
  // }

  /**
   * Prohibition of descendants creation.
   */
  private TsGuiUtils() {
    // nop
  }

}

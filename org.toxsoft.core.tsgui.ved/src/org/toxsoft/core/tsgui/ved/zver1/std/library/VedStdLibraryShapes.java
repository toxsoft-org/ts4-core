package org.toxsoft.core.tsgui.ved.zver1.std.library;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.zver1.std.library.ITsResources.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.ved.zver1.core.impl.*;
import org.toxsoft.core.tsgui.ved.zver1.std.comps.*;
import org.toxsoft.core.tslib.av.opset.impl.*;

/**
 * Basic library - primitive shapes and pointer tool.
 *
 * @author hazard157
 */
public class VedStdLibraryShapes
    extends VedLibrary {

  /**
   * The library ID.
   */
  public static final String LIBRARY_ID = VED_ID + ".library.Shapes"; //$NON-NLS-1$

  /**
   * Constructor.
   */
  public VedStdLibraryShapes() {
    super( LIBRARY_ID, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_N_LIB_SHAPES, //
        TSID_DESCRIPTION, STR_D_LIB_SHAPES, //
        TSID_ICON_ID, ICONID_VED_LOGO //
    ) );
    // HERE add components
    componentProviders().add( VedStdCompRectangle.PROVIDER );
    componentProviders().add( VedStdCompBorder.PROVIDER );
    componentProviders().add( VedStdCompTextLabel.PROVIDER );
  }

}

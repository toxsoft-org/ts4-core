package org.toxsoft.core.tsgui.m5.std.models.av;

import static org.toxsoft.core.tsgui.ITsGuiConstants.*;

import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;

/**
 * M5-model of {@link IDataDef}.
 *
 * @author hazard157
 */
public class DataDefM5Model
    extends M5Model<IDataDef> {

  /**
   * The model ID.
   */
  public static final String MODEL_ID = TSGUI_M5_ID + ".DataDef"; //$NON-NLS-1$

  /**
   * Constructor.
   */
  public DataDefM5Model() {
    super( MODEL_ID, IDataDef.class );
    // TODO Auto-generated constructor stub
  }

}

package org.toxsoft.core.tsgui.m5.std.models.av;

import static org.toxsoft.core.tslib.ITsHardConstants.*;

import org.toxsoft.core.tsgui.m5.std.models.enums.*;
import org.toxsoft.core.tslib.av.*;

/**
 * M5-model of {@link EAtomicType}.
 *
 * @author hazard157
 */
public class AtomicTypeM5Model
    extends M5StridableEnumModelBase<EAtomicType> {

  /**
   * The model ID.
   */
  public static final String MODEL_ID = TS_ID + "EAtomicType"; //$NON-NLS-1$

  /**
   * Constructor.
   */
  public AtomicTypeM5Model() {
    super( MODEL_ID, EAtomicType.class );
  }

}

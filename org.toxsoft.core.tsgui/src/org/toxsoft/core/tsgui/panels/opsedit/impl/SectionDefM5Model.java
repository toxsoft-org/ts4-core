package org.toxsoft.core.tsgui.panels.opsedit.impl;

import static org.toxsoft.core.tsgui.panels.opsedit.impl.ITsResources.*;
import static org.toxsoft.core.tslib.ITsHardConstants.*;

import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.m5.std.fields.*;
import org.toxsoft.core.tsgui.panels.opsedit.group.*;
import org.toxsoft.core.tslib.av.metainfo.*;

/**
 * {@link ISectionDef} M5-model.
 *
 * @author hazard157
 */
public class SectionDefM5Model
    extends M5Model<ISectionDef> {

  /**
   * The model ID.
   */
  public static final String MODEL_ID = TS_ID + ".tsgui.m5.SectionDef"; //$NON-NLS-1$

  /**
   * Attribute {@link ISectionDef#id()}.
   */
  public static final IM5AttributeFieldDef<ISectionDef> ID = new M5StdFieldDefId<>();

  /**
   * Attribute {@link ISectionDef#nmName()}.
   */
  public static final IM5AttributeFieldDef<ISectionDef> NAME = new M5StdFieldDefName<>();

  /**
   * Attribute {@link ISectionDef#description()}.
   */
  public static final IM5AttributeFieldDef<ISectionDef> DESCRIPTION = new M5StdFieldDefDescription<>();

  /**
   * Attribute: parameter {@link ISectionDef#params()} of ID {@link IAvMetaConstants#TSID_ICON_ID}.
   */
  public static final IM5AttributeFieldDef<ISectionDef> ICON = new M5StdFieldDefParamIconId<>();

  /**
   * Constructor.
   */
  public SectionDefM5Model() {
    super( MODEL_ID, ISectionDef.class );
    setNameAndDescription( STR_N_M5M_SECTION_DEF, STR_D_M5M_SECTION_DEF );
    addFieldDefs( ICON, ID, NAME, DESCRIPTION );
  }

  @Override
  protected IM5LifecycleManager<ISectionDef> doCreateLifecycleManager( Object aMaster ) {
    return new SectionDefM5LifecycleManager( this, aMaster );
  }

}

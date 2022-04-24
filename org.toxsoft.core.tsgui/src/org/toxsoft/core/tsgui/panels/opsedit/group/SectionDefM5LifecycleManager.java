package org.toxsoft.core.tsgui.panels.opsedit.group;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.panels.opsedit.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Lifecycle manager for model {@link SectionDefM5Model}.
 * <p>
 * This LM simpledoes not allows and changes, just returns list of sections specitied as master object.
 *
 * @author hazard157
 */
public class SectionDefM5LifecycleManager
    extends M5LifecycleManager<ISectionDef, IList<ISectionDef>> {

  /**
   * Constructor.
   *
   * @param aModel {@link IM5Model} - the model
   * @param aMaster Object - wrapped list of sections, must be of type {@link IList}&lt;{@link ISectionDef}&gt;
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SectionDefM5LifecycleManager( IM5Model<ISectionDef> aModel, Object aMaster ) {
    super( aModel, false, false, false, false, TsErrorUtils.checkListOfTypes( aMaster, ISectionDef.class ) );
  }

  @Override
  protected IList<ISectionDef> doListEntities() {
    return master();
  }

}

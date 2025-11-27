package org.toxsoft.core.tsgui.m5.gui.panels.impl;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;

import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.IM5EntityPanel;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link IM5EntityPanel} default implementation to show datail about specified entity.
 * <p>
 * This panel is the viewer so {@link #isViewer()} = <code>true</code>.
 * <p>
 * This panel contains editors for fields with flag {@link IM5Constants#M5FF_DETAIL}.
 *
 * @author hazard157
 * @param <T> - modeled entity type
 */
public class M5DefaultEntityDetailsPanel<T>
    extends M5EntityPanelWithValeds<T> {

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aModel {@link IM5Model} - entity model
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public M5DefaultEntityDetailsPanel( ITsGuiContext aContext, IM5Model<T> aModel ) {
    super( aContext, aModel, true );
  }

  @Override
  protected void doInitEditors() {
    // cretae editors for M5FF_DETAIL fields
    for( IM5FieldDef<T, ?> fDef : model().fieldDefs() ) {
      if( fDef.hasFlag( M5FF_DETAIL ) ) {
        addField( fDef.id() );
      }
    }
  }

}

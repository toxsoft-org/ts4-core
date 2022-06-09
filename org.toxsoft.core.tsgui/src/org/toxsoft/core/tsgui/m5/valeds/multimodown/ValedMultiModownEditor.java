package org.toxsoft.core.tsgui.m5.valeds.multimodown;

import static org.toxsoft.core.tsgui.m5.valeds.IM5ValedConstants.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.valeds.*;
import org.toxsoft.core.tsgui.valed.impl.*;

/**
 * This multi modownfield editor contains just a factory invoking editor with requested widget type.
 * <p>
 * Followig widget types are supported:
 * <ul>
 * <li>{@link IM5ValedConstants#M5VWTID_TABLE};</li>
 * </ul>
 * For uncpecified or unknown widget types {@link IM5ValedConstants#M5VWTID_TABLE} will be used.
 *
 * @author goga
 */
@SuppressWarnings( { "unchecked", "rawtypes" } )
public class ValedMultiModownEditor {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".MultiModownEditor"; //$NON-NLS-1$

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new AbstractValedControlFactory( FACTORY_NAME ) {

    @Override
    protected AbstractValedControl doCreateEditor( ITsGuiContext aContext ) {
      switch( M5_VALED_OPDEF_WIDGET_TYPE_ID.getValue( aContext.params() ).asString() ) {
        case M5VWTID_TABLE:
        default:
          return new ValedMultiModownTableEditor<>( aContext );
      }
    }

    @Override
    protected AbstractValedControl doCreateViewer( ITsGuiContext aContext ) {
      OPDEF_CREATE_UNEDITABLE.setValue( aContext.params(), AV_TRUE );
      switch( M5_VALED_OPDEF_WIDGET_TYPE_ID.getValue( aContext.params() ).asString() ) {
        case M5VWTID_TABLE:
        default:
          return new ValedMultiModownTableViewer<>( aContext );
        case M5VWTID_TEXT:
          return new ValedMultiModownTextViewer<>( aContext );
      }
    }

  };

}

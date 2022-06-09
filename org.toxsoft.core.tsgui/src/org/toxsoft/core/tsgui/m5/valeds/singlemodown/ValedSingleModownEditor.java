package org.toxsoft.core.tsgui.m5.valeds.singlemodown;

import static org.toxsoft.core.tsgui.m5.valeds.IM5ValedConstants.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.valeds.*;
import org.toxsoft.core.tsgui.valed.impl.*;

/**
 * This single modown field editor contains just a factory invoking editor with requested widget type.
 * <p>
 * Followig widget types are supported:
 * <ul>
 * <li>{@link IM5ValedConstants#M5VWTID_TEXT};</li>
 * <li>{@link IM5ValedConstants#M5VWTID_INPLACE}.</li>
 * </ul>
 * For uncpecified or unknown widget types {@link IM5ValedConstants#M5VWTID_TEXT} will be used.
 *
 * @author hazard157
 */
@SuppressWarnings( { "unchecked", "rawtypes" } )
public class ValedSingleModownEditor {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".SingleModownEditor"; //$NON-NLS-1$

  /**
   * The factory singleton.
   */
  public static AbstractValedControlFactory FACTORY = new AbstractValedControlFactory( FACTORY_NAME ) {

    @Override
    protected AbstractValedControl doCreateEditor( ITsGuiContext aContext ) {
      switch( M5_VALED_OPDEF_WIDGET_TYPE_ID.getValue( aContext.params() ).asString() ) {
        case M5VWTID_TEXT:
        default:
          return new ValedSingleModownTextEditor<>( aContext );
        case M5VWTID_INPLACE:
          return new ValedSingleModownInplaceEditor<>( aContext );
      }
    }

    @Override
    protected AbstractValedControl doCreateViewer( ITsGuiContext aContext ) {
      OPDEF_CREATE_UNEDITABLE.setValue( aContext.params(), AV_TRUE );
      switch( M5_VALED_OPDEF_WIDGET_TYPE_ID.getValue( aContext.params() ).asString() ) {
        case M5VWTID_TEXT:
        default:
          return new ValedSingleModownTextViewer<>( aContext );
        case M5VWTID_INPLACE:
          return new ValedSingleModownInplaceEditor<>( aContext );
      }
    }
  };

}

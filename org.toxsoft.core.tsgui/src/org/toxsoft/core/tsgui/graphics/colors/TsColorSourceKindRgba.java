package org.toxsoft.core.tsgui.graphics.colors;

import static org.toxsoft.core.tsgui.graphics.colors.ITsResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.graphics.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;

/**
 * {@link ITsColorSourceKind} implementation - color from RGBA.
 * <p>
 * The color resource is specified as color components values and alpha channel value.
 *
 * @author vs
 */
public class TsColorSourceKindRgba
    extends AbstractTsColorSourceKind {

  /**
   * The kind ID.
   */
  public static final String KIND_ID = "rgba"; //$NON-NLS-1$

  /**
   * Option: color components.
   */
  public static final IDataDef OPDEF_RGBA = DataDef.create( "rgba", VALOBJ, //$NON-NLS-1$
      TSID_NAME, STR_RGBA_COLOR, //
      TSID_DESCRIPTION, STR_RGBA_COLOR_D, //
      IValedControlConstants.OPDEF_EDITOR_FACTORY_NAME, ValedAvValobjSimpleRgba.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avValobj( new RGBA( 0, 0, 0, 255 ) ) //
  );

  /**
   * The singleton instance.
   */
  public static final ITsColorSourceKind INSTANCE = new TsColorSourceKindRgba();

  private TsColorSourceKindRgba() {
    super( KIND_ID, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_SRCKIND_RGBA, //
        TSID_DESCRIPTION, STR_SRCKIND_RGBA_D //
    ) );
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsColorSourceKind
  //

  @Override
  public IOptionSet doEdit( IOptionSet aParams, ITsGuiContext aContext ) {
    RGBA rgba = aParams.getValobj( OPDEF_RGBA );
    rgba = PanelRgbaSelector.editRgba( rgba, aContext );
    if( rgba != null ) {
      IOptionSetEdit ops = new OptionSet();
      ops.setValobj( OPDEF_RGBA, rgba );
      return ops;
    }
    return null;
  }

  @Override
  public String doGetHumanReadableString( IOptionSet aParams ) {
    RGBA rgba = params().getValobj( OPDEF_RGBA );
    return rgba.toString();
  }

  @Override
  protected Color doCreate( TsColorDescriptor aDescriptor ) {
    RGBA rgba = params().getValobj( OPDEF_RGBA );
    return new Color( rgba );
  }

  @Override
  public String uniqueColorNameString( IOptionSet aParams ) {
    RGBA rgba = params().getValobj( OPDEF_RGBA );
    return rgba.toString();
  }

}

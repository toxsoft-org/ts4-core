package org.toxsoft.core.tsgui.graphics.colors;

import static org.toxsoft.core.tsgui.graphics.colors.ITsResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
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
public class TsColorSourceKindTsColor
    extends AbstractTsColorSourceKind {

  /**
   * The kind ID.
   */
  public static final String KIND_ID = "tsColor"; //$NON-NLS-1$

  /**
   * Option: color components.
   */
  public static final IDataDef OPDEF_TSCOLOR = DataDef.create( "tsColor", VALOBJ, //$NON-NLS-1$
      TSID_NAME, STR_TSCOLOR, //
      TSID_DESCRIPTION, STR_TSCOLOR_D, //
      TSID_KEEPER_ID, ETsColor.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( ETsColor.BLACK ) //
  );

  /**
   * The singleton instance.
   */
  public static final ITsColorSourceKind INSTANCE = new TsColorSourceKindTsColor();

  private TsColorSourceKindTsColor() {
    super( KIND_ID, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_SRCKIND_TSCOLOR, //
        TSID_DESCRIPTION, STR_SRCKIND_TSCOLOR_D //
    ) );
    opDefs().add( OPDEF_TSCOLOR );
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsColorSourceKind
  //

  @Override
  public String doGetHumanReadableString( IOptionSet aParams ) {
    ETsColor color = params().getValobj( OPDEF_TSCOLOR );
    return color.nmName();
  }

  @Override
  protected Color doCreate( TsColorDescriptor aDescriptor ) {
    ETsColor color = aDescriptor.params().getValobj( OPDEF_TSCOLOR );
    return new Color( color.rgba() );
  }

  @Override
  public String uniqueColorNameString( IOptionSet aParams ) {
    ETsColor color = aParams.getValobj( OPDEF_TSCOLOR );
    return color.nmName();
  }

}

package org.toxsoft.core.tsgui.graphics.colors;

import static org.toxsoft.core.tsgui.graphics.colors.ITsResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
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
public class TsColorSourceKindSystemColor
    extends AbstractTsColorSourceKind {

  /**
   * The kind ID.
   */
  public static final String KIND_ID = "swtSysColor"; //$NON-NLS-1$

  /**
   * Option: color components.
   */
  public static final IDataDef OPDEF_SYSCOLOR = DataDef.create( "sysColor", VALOBJ, //$NON-NLS-1$
      TSID_NAME, STR_SYSCOLOR, //
      TSID_DESCRIPTION, STR_SYSCOLOR_D, //
      TSID_KEEPER_ID, ESwtSysColor.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( ESwtSysColor.SYSCOL_WIDGET_BACKGROUND ) //
  );

  /**
   * The singleton instance.
   */
  public static final ITsColorSourceKind INSTANCE = new TsColorSourceKindSystemColor();

  private TsColorSourceKindSystemColor() {
    super( KIND_ID, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_SRCKIND_SYSCOLOR, //
        TSID_DESCRIPTION, STR_SRCKIND_SYSCOLOR_D //
    ) );
    opDefs().add( OPDEF_SYSCOLOR );
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsColorSourceKind
  //

  @Override
  public String doGetHumanReadableString( IOptionSet aParams ) {
    ESwtSysColor color = params().getValobj( OPDEF_SYSCOLOR );
    return color.nmName();
  }

  @Override
  protected Color doCreate( TsColorDescriptor aDescriptor, Display aDisplay ) {
    ESwtSysColor swtColor = aDescriptor.params().getValobj( OPDEF_SYSCOLOR );
    return aDisplay.getSystemColor( swtColor.getSwtColorId() );
  }

  @Override
  public String uniqueColorNameString( IOptionSet aParams ) {
    ESwtSysColor color = aParams.getValobj( OPDEF_SYSCOLOR );
    return color.nmName();
  }

}

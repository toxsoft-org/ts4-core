package org.toxsoft.core.tsgui.graphics.image;

import static org.toxsoft.core.tsgui.graphics.image.ITsResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.jface.resource.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITsImageSourceKind} implementation - image of the specified icon ID of the specified size.
 * <p>
 * An icon is understood as in {@link ITsIconManager}.
 *
 * @author hazard157
 */
public class TsImageSourceKindTsIcon
    extends AbstractTsImageSourceKind {

  /**
   * Option: the icon ID.
   */
  public static final IDataDef OPDEF_ICON_ID = DataDef.create( "iconId", STRING, //$NON-NLS-1$
      TSID_NAME, STR_TSICON_ICON_ID, //
      TSID_DESCRIPTION, STR_TSICON_ICON_ID_D, //
      TSID_IS_MANDATORY, AV_TRUE, //
      TSID_IS_NULL_ALLOWED, AV_FALSE, //
      TSID_DEFAULT_VALUE, IAtomicValue.NULL //
  );

  /**
   * Option: the icon ID.
   */
  public static final IDataDef OPDEF_ICON_SIZE = DataDef.create( "size", VALOBJ, //$NON-NLS-1$
      TSID_NAME, STR_TSICON_ICON_SIZE, //
      TSID_DESCRIPTION, STR_TSICON_ICON_SIZE_D, //
      TSID_KEEPER_ID, EIconSize.KEEPER_ID, //
      TSID_IS_MANDATORY, AV_TRUE, //
      TSID_IS_NULL_ALLOWED, AV_FALSE, //
      TSID_DEFAULT_VALUE, avValobj( EIconSize.IS_48X48 ) //
  );

  /**
   * The kind ID.
   */
  public static final String KIND_ID = "tsicon"; //$NON-NLS-1$

  /**
   * The singleton instance.
   */
  public static final ITsImageSourceKind INSTANCE = new TsImageSourceKindTsIcon();

  private TsImageSourceKindTsIcon() {
    super( KIND_ID, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_SRCKIND_TSICON, //
        TSID_DESCRIPTION, STR_SRCKIND_TSICON_D //
    ) );
    opDefs().add( OPDEF_ICON_ID );
    opDefs().add( OPDEF_ICON_SIZE );
  }

  /**
   * Creates the image descriptor of this kind.
   *
   * @param aIconId String - the icon ID
   * @param aIconSize {@link EIconSize} - the icon size
   * @return {@link TsImageDescriptor} - created descriptor
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException an ID is a blank string
   */
  public static TsImageDescriptor createImageDescriptor( String aIconId, EIconSize aIconSize ) {
    TsErrorUtils.checkNonBlank( aIconId );
    TsNullArgumentRtException.checkNull( aIconSize );
    return TsImageDescriptor.create( KIND_ID, //
        OPDEF_ICON_ID, aIconId, //
        OPDEF_ICON_SIZE, avValobj( aIconSize ) //
    );
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsImageSourceKind
  //

  @Override
  protected TsImage doCreate( TsImageDescriptor aDescriptor, ITsGuiContext aContext ) {
    String iconId = OPDEF_ICON_ID.getValue( aDescriptor.params() ).asString();
    EIconSize iconSize = OPDEF_ICON_SIZE.getValue( aDescriptor.params() ).asValobj();
    ITsIconManager iconManager = aContext.get( ITsIconManager.class );
    String symbolicName = iconManager.makeSymbolicName( iconId, iconSize );
    if( !iconManager.isRegistered( symbolicName ) ) {
      ITsImageManager imageManager = aContext.get( ITsImageManager.class );
      return imageManager.createUnknownImage( iconSize.size() );
    }
    ImageDescriptor imgDescr = iconManager.loadFreeDescriptor( symbolicName );
    Display display = aContext.get( Display.class );
    Image image = imgDescr.createImage( display );
    return TsImage.create( image );
  }

}

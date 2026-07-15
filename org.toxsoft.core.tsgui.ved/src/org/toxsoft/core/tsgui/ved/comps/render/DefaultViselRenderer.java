package org.toxsoft.core.tsgui.ved.comps.render;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.graphics.gc.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;

public class DefaultViselRenderer
    extends AbstractViselRenderer {

  /**
   * The renderer factory ID.
   */
  public static final String FACTORY_ID = VED_ID + ".defaultRendererFactory"; //$NON-NLS-1$

  /**
   * Renderer kind id
   */
  public static final String KIND_ID = "defaultRenderer"; //$NON-NLS-1$

  /**
   * The VISEL factory singleton.
   */
  public static final VedAbstractRendererFactory FACTORY = new VedAbstractRendererFactory( FACTORY_ID, //
      TSID_NAME, "Default", //
      TSID_DESCRIPTION, "Default renderer - red rectangle", //
      TSID_ICON_ID, ICONID_VISEL_RECTANGLE ) {

    @Override
    public String kindId() {
      return KIND_ID;
    }

    @Override
    public ViselRendererCfg createConfig( String aId, String aViselId ) {
      return new ViselRendererCfg( "default", KIND_ID, FACTORY_ID, IOptionSet.NULL, aViselId );
    }

    @Override
    protected void addTinTypeInfoes( IStridablesListEdit<ITinFieldInfo> fields ) {
      // nop
    }

    @Override
    protected AbstractViselRenderer doCreate( ViselRendererCfg aCfg, IVedVisel aVisel, VedScreen aVedScreen ) {
      return new DefaultViselRenderer( "default", aVisel, aVedScreen.tsContext() ); //$NON-NLS-1$
    }

  };

  double width  = 1.;
  double height = 1.;

  TsFillInfo fillInfo = new TsFillInfo( new RGBA( 255, 0, 0, 255 ) );

  /**
   * Constructor.
   *
   * @param aId String - idnetifier
   * @param aVisel {@link IVedVisel} - the corresponding VISEL
   * @param aTsContext {@link ITsGuiContext} - corresponding context
   */
  public DefaultViselRenderer( String aId, IVedVisel aVisel, ITsGuiContext aTsContext ) {
    super( aId, IStridablesList.EMPTY, aVisel, aTsContext );
  }

  // ------------------------------------------------------------------------------------
  // IViselRenderer
  //

  @Override
  public String kindId() {
    return KIND_ID;
  }

  @Override
  protected ITinTypeInfo doCreateTypeInfo() {
    return new PropertableEntitiesTinTypeInfo<>( IStridablesList.EMPTY, AbstractViselRenderer.class );
  }

  @Override
  public IStridablesList<ITinFieldInfo> tinFieldInfoes() {
    return IStridablesList.EMPTY;
  }

  @Override
  protected void doUpdateCachesAfterPropsChange( IOptionSet aChangedValues ) {
    if( aChangedValues.keys().hasElem( PROPID_WIDTH ) ) {
      width = aChangedValues.getDouble( PROPID_WIDTH );
    }
    if( aChangedValues.keys().hasElem( PROPID_HEIGHT ) ) {
      height = aChangedValues.getDouble( PROPID_HEIGHT );
    }
  }

  @Override
  public void doPaint( ITsGraphicsContext aPaintContext ) {
    aPaintContext.setFillInfo( fillInfo );
    aPaintContext.fillRect( 0, 0, (int)width, (int)height );
  }

}

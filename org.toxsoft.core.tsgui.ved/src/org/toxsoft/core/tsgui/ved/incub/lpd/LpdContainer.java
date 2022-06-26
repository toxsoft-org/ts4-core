package org.toxsoft.core.tsgui.ved.incub.lpd;

import org.toxsoft.core.tsgui.ved.incub.secdata.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.chario.impl.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;

/**
 * {@link ILpdContainer} implementation.
 *
 * @author hazard157
 */
public class LpdContainer
    implements ILpdContainer {

  private static final String SECTID_PANEL_CFG      = "LivePanelConfig";     //$NON-NLS-1$
  private static final String SECTID_COMPONENT_CFGS = "LivePanelComponents"; //$NON-NLS-1$

  private final IOptionSetEdit               panelCfg      = new OptionSet();
  private final IListEdit<ILpdComponentInfo> componentCfgs = new ElemLinkedBundleList<>();

  /**
   * Constructor.
   */
  public LpdContainer() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // IKeepableEntity
  //

  @Override
  public void write( IStrioWriter aSw ) {
    // panelCfg
    StrioUtils.writeKeywordHeader( aSw, SECTID_PANEL_CFG );
    OptionSetKeeper.KEEPER_INDENTED.write( aSw, panelCfg );
    aSw.writeEol();
    // componentCfgs
    StrioUtils.writeKeywordHeader( aSw, SECTID_COMPONENT_CFGS );
    LpdComponentInfo.KEEPER.writeColl( aSw, componentCfgs, true );
    aSw.writeEol();
  }

  @Override
  public void read( IStrioReader aSr ) {
    // read raw sections from storage
    RawSectionedDataContainer rawContainer = new RawSectionedDataContainer();
    rawContainer.read( aSr );
    // panelCfg
    String ins = rawContainer.getContent( SECTID_PANEL_CFG, null );
    if( ins != null ) {
      IStrioReader sr = new StrioReader( new CharInputStreamString( ins ) );
      panelCfg.setAll( OptionSetKeeper.KEEPER.read( sr ) );
    }
    // componentCfgs
    ins = rawContainer.getContent( SECTID_COMPONENT_CFGS, null );
    if( ins != null ) {
      IStrioReader sr = new StrioReader( new CharInputStreamString( ins ) );
      componentCfgs.setAll( LpdComponentInfo.KEEPER.readColl( sr ) );
    }
    // gc
    rawContainer.clear();
  }

  // ------------------------------------------------------------------------------------
  // ILpdContainer
  //

  @Override
  public IOptionSetEdit panelCfg() {
    return panelCfg;
  }

  @Override
  public IListEdit<ILpdComponentInfo> componentConfigs() {
    return componentCfgs;
  }

}

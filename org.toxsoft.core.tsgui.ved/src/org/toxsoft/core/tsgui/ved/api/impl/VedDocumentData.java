package org.toxsoft.core.tsgui.ved.api.impl;

import org.toxsoft.core.tsgui.ved.api.cfgdata.*;
import org.toxsoft.core.tsgui.ved.incub.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;

/**
 * {@link IVedDocumentData} mutable implementation.
 *
 * @author hazard157
 */
public class VedDocumentData
    implements IVedDocumentData {

  private static final String KW_DOC_PROPS      = "DocProps";              //$NON-NLS-1$
  private static final String KW_COMP_CFGS      = "ComponentConfigs";      //$NON-NLS-1$
  private static final String KW_TAILOR_CFGS    = "TailorConfigs";         //$NON-NLS-1$
  private static final String KW_ACTOR_CFGS     = "ActorConfigs";          //$NON-NLS-1$
  private static final String KW_BINDING_CFGS   = "BindingConfigsMap";     //$NON-NLS-1$
  private static final String KW_EXT_SECTS_DATA = "ExtentionSectionsData"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<IVedDocumentData> KEEPER =
      new AbstractEntityKeeper<>( IVedDocumentData.class, EEncloseMode.ENCLOSES_BASE_CLASS, new VedDocumentData() ) {

        @Override
        protected void doWrite( IStrioWriter aSw, IVedDocumentData aEntity ) {
          StrioUtils.writeKeywordHeader( aSw, KW_DOC_PROPS, true );
          OptionSetKeeper.KEEPER_INDENTED.write( aSw, aEntity.documentPropValues() );
          aSw.writeEol();

          // TODO Auto-generated method stub

        }

        @Override
        protected IVedDocumentData doRead( IStrioReader aSr ) {
          // TODO Auto-generated method stub
          return null;
        }
      };

  private final IOptionSetEdit                                      documentPropValues;
  private final IStridablesListEdit<IVedEntityConfig>               componentConfigs;
  private final IStridablesListEdit<IVedEntityConfig>               tailorConfigs;
  private final IStringMapEdit<IStridablesListEdit<IVedBindingCfg>> tailorBindingConfigs;
  private final IStridablesListEdit<IVedEntityConfig>               actorConfigs;
  private final KtorSectionsContainer                               secitonsData;

  /**
   * Constructor.
   */
  public VedDocumentData() {
    documentPropValues = new OptionSet();
    componentConfigs = new StridablesList<>();
    tailorConfigs = new StridablesList<>();
    actorConfigs = new StridablesList<>();
    tailorBindingConfigs = new StringMap<>();
    secitonsData = new KtorSectionsContainer();
  }

  // ------------------------------------------------------------------------------------
  // class API
  //

  /**
   * Returns the bindings of each tailor from the list {@link #tailorConfigs()}.
   *
   * @return {@link IStringMapEdit}&lt;{@link IStridablesListEdit}&lt;{@link IVedBindingCfg}&gt;&gt; - editable configs
   */
  public IStringMapEdit<IStridablesListEdit<IVedBindingCfg>> tailorBindingConfigsEdit() {
    return tailorBindingConfigs;
  }

  // ------------------------------------------------------------------------------------
  // IVedDocumentData
  //

  @Override
  public IOptionSetEdit documentPropValues() {
    return documentPropValues;
  }

  @Override
  public IStridablesListEdit<IVedEntityConfig> componentConfigs() {
    return componentConfigs;
  }

  @Override
  public IStridablesListEdit<IVedEntityConfig> tailorConfigs() {
    return tailorConfigs;
  }

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  @Override
  public IStringMap<IStridablesList<IVedBindingCfg>> tailorBindingConfigs() {
    return (IStringMap)tailorBindingConfigs;
  }

  @Override
  public IStridablesListEdit<IVedEntityConfig> actorConfigs() {
    return actorConfigs;
  }

  @Override
  public KtorSectionsContainer secitonsData() {
    return secitonsData;
  }

}

package org.toxsoft.core.tsgui.ved.api.impl;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

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
import org.toxsoft.core.tslib.coll.*;
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
          aSw.incNewLine();
          // doc props
          StrioUtils.writeKeywordHeader( aSw, KW_DOC_PROPS, true );
          OptionSetKeeper.KEEPER_INDENTED.write( aSw, aEntity.documentPropValues() );
          aSw.writeEol();
          // components
          StrioUtils.writeKeywordHeader( aSw, KW_COMP_CFGS, true );
          VedEntityConfig.KEEPER.writeColl( aSw, aEntity.componentConfigs(), true );
          aSw.writeEol();
          // tailors
          StrioUtils.writeKeywordHeader( aSw, KW_TAILOR_CFGS, true );
          VedEntityConfig.KEEPER.writeColl( aSw, aEntity.tailorConfigs(), true );
          aSw.writeEol();
          // actors
          StrioUtils.writeKeywordHeader( aSw, KW_ACTOR_CFGS, true );
          VedEntityConfig.KEEPER.writeColl( aSw, aEntity.actorConfigs(), true );
          aSw.writeEol();
          // bindings
          StrioUtils.writeKeywordHeader( aSw, KW_BINDING_CFGS, true );
          aSw.writeChar( CHAR_ARRAY_BEGIN );
          if( !aEntity.tailorBindingConfigs().isEmpty() ) {
            aSw.incNewLine();
            for( String compId : aEntity.tailorBindingConfigs().keys() ) {
              aSw.writeAsIs( compId );
              aSw.writeChars( CHAR_SPACE, CHAR_EQUAL, CHAR_SPACE );
              VedBindingCfg.KEEPER.writeColl( aSw, aEntity.tailorBindingConfigs().getByKey( compId ), true );
              aSw.writeEol();
            }
            aSw.decNewLine();
          }
          aSw.writeChar( CHAR_ARRAY_END );
          aSw.writeEol();
          // sections
          StrioUtils.writeKeywordHeader( aSw, KW_EXT_SECTS_DATA, true );
          aSw.writeChar( CHAR_SET_BEGIN );
          aSw.incNewLine();
          ((KtorSectionsContainer)aEntity.secitonsData()).write( aSw );
          aSw.decNewLine();
          aSw.writeChar( CHAR_SET_END );
          aSw.decNewLine();
        }

        @Override
        protected IVedDocumentData doRead( IStrioReader aSr ) {
          VedDocumentData dd = new VedDocumentData();
          // doc props
          StrioUtils.ensureKeywordHeader( aSr, KW_DOC_PROPS );
          dd.documentPropValues.setAll( OptionSetKeeper.KEEPER.read( aSr ) );
          // components
          StrioUtils.ensureKeywordHeader( aSr, KW_COMP_CFGS );
          VedEntityConfig.KEEPER.readColl( aSr, dd.componentConfigs );
          // tailors
          StrioUtils.ensureKeywordHeader( aSr, KW_TAILOR_CFGS );
          VedEntityConfig.KEEPER.readColl( aSr, dd.tailorConfigs );
          // actors
          StrioUtils.ensureKeywordHeader( aSr, KW_ACTOR_CFGS );
          VedEntityConfig.KEEPER.readColl( aSr, dd.actorConfigs );
          // bindings
          if( !aSr.readArrayBegin() ) {
            do {
              String compId = aSr.readIdPath();
              aSr.ensureChar( CHAR_EQUAL );
              IList<IVedBindingCfg> cfgsList = VedBindingCfg.KEEPER.readColl( aSr );
              dd.tailorBindingConfigs.put( compId, new StridablesList<>( cfgsList ) );

            } while( aSr.readArrayNext() );
          }
          // sections
          StrioUtils.ensureKeywordHeader( aSr, KW_EXT_SECTS_DATA );
          aSr.ensureChar( CHAR_SET_BEGIN );
          dd.secitonsData.read( aSr );
          aSr.ensureChar( CHAR_SET_END );
          return dd;
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

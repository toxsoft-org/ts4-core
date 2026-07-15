package org.toxsoft.core.tsgui.ved.comps.render;

import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.gui.*;

/**
 * Configuration data of the renderer.
 *
 * @author vs
 */
public class ViselRendererCfg
    implements IStridable, IIconIdable, IParameterizedEdit {

  private static final String KW_PARAMS = "params"; //$NON-NLS-1$

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "ViselRendererCfg"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<ViselRendererCfg> KEEPER =
      new AbstractEntityKeeper<>( ViselRendererCfg.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, ViselRendererCfg aEntity ) {
          aSw.incNewLine();
          // item ID and factory ID
          aSw.writeAsIs( aEntity.id() );
          aSw.writeSeparatorChar();
          aSw.writeAsIs( aEntity.kindId() );
          aSw.writeSeparatorChar();
          aSw.writeAsIs( aEntity.factoryId() );
          aSw.writeSeparatorChar();
          aSw.writeAsIs( aEntity.viselId() );
          aSw.writeSeparatorChar();
          aSw.writeEol();
          // properties values
          // OptionSetKeeper.KEEPER_INDENTED.write( aSw, aEntity.propValues() );
          aSw.setIndented( true );
          OptionSetKeeper.KEEPER.write( aSw, aEntity.propValues() );
          aSw.writeSeparatorChar();
          aSw.writeEol();
          // parameters values
          StrioUtils.writeKeywordHeader( aSw, KW_PARAMS, true );
          OptionSetKeeper.KEEPER.write( aSw, aEntity.params() );
          aSw.writeSeparatorChar();
          aSw.writeEol();
          aSw.decNewLine();
        }

        @Override
        protected ViselRendererCfg doRead( IStrioReader aSr ) {
          // item ID and factory ID
          String id = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          String kindId = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          String factoryId = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          String viselId = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          // properties values
          IOptionSet propValues = OptionSetKeeper.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          // parameters values
          StrioUtils.ensureKeywordHeader( aSr, KW_PARAMS );
          IOptionSet params = OptionSetKeeper.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          // create an item to read extra data into it
          ViselRendererCfg itemCfg = new ViselRendererCfg( id, kindId, factoryId, params, viselId );
          itemCfg.propValues().setAll( propValues );
          return itemCfg;
        }
      };

  private final IOptionSetEdit params     = new OptionSet();
  private final IOptionSetEdit propValues = new OptionSet();

  private final String id;
  private final String kindId;
  private final String factoryId;
  private final String viselId;

  /**
   * Constructor.
   *
   * @param aId String - rend erer identifier
   * @param aKindId {@link String} - the renderer kind ID
   * @param aFactoryId {@link String} - the renderer factory ID
   * @param aProps {@link IOptionSet} - initial values of {@link #propValues()}
   * @param aViselId String - VISEL Id
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public ViselRendererCfg( String aId, String aKindId, String aFactoryId, IOptionSet aProps, String aViselId ) {
    id = StridUtils.checkValidIdPath( aId );
    propValues.setAll( aProps );
    // propValues.setStr( PROPID_NAME, aViselId );
    kindId = StridUtils.checkValidIdPath( aKindId );
    factoryId = StridUtils.checkValidIdPath( aFactoryId );
    viselId = StridUtils.checkValidIdPath( aViselId );
  }

  // ------------------------------------------------------------------------------------
  // IStridable
  //

  @Override
  public String id() {
    return id;
  }

  @Override
  public String nmName() {
    if( params().hasKey( PROPID_NAME ) ) {
      String n = params().getStr( PROPID_NAME );
      if( !n.isBlank() ) {
        return n;
      }
    }
    return propValues.getStr( PROP_NAME );
  }

  @Override
  public String description() {
    return propValues.getStr( PROP_DESCRIPTION );
  }

  // ------------------------------------------------------------------------------------
  // IParameterizedEdit
  //

  @Override
  public IOptionSetEdit params() {
    return params;
  }

  // ------------------------------------------------------------------------------------
  // IIconIdable
  //

  @Override
  public String iconId() {
    return params().getStr( TSID_ICON_ID, null );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns renderers kind, for example: "button", "RoundAxis" etc.
   *
   * @return String - kind ID of the renderer
   */
  public String kindId() {
    return kindId;
  }

  /**
   * Returns the ID of the factory used to create the renderer from the configuration data {@link ViselRendererCfg}.
   * <p>
   * It is assumed that somewhere exists the registry containing factories.
   *
   * @return {@link String} - the VISEL factory ID
   */
  public String factoryId() {
    return factoryId;
  }

  /**
   * Returns the ID of the owner visel.
   *
   * @return String - the ID of the owner visel
   */
  public String viselId() {
    return viselId;
  }

  /**
   * Returns the values of the properties used to build the renderer instance.
   *
   * @return {@link IOptionSet} - property values for renderer instance creation
   */
  public IOptionSetEdit propValues() {
    return propValues;
  }

}

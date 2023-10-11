package org.toxsoft.core.tsgui.ved.screen.cfg;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.storage.*;

/**
 * {@link IVedItemCfg} implementation.
 *
 * @author hazard157
 */
public final class VedItemCfg
    extends StridableParameterized
    implements IVedItemCfg {

  private static final String KW_PARAMS = "params"; //$NON-NLS-1$
  private static final String KW_EXTRA  = "extra";  //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  @SuppressWarnings( "hiding" )
  public static final IEntityKeeper<IVedItemCfg> KEEPER =
      new AbstractEntityKeeper<>( IVedItemCfg.class, EEncloseMode.ENCLOSES_KEEPER_IMPLEMENTATION, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, IVedItemCfg aEntity ) {
          // item ID and factory ID
          aSw.writeAsIs( aEntity.id() );
          aSw.writeSeparatorChar();
          aSw.writeAsIs( aEntity.factoryId() );
          aSw.writeSeparatorChar();
          aSw.writeEol();
          // properties values
          OptionSetKeeper.KEEPER_INDENTED.write( aSw, aEntity.propValues() );
          aSw.writeSeparatorChar();
          aSw.writeEol();
          // parameters values
          StrioUtils.writeKeywordHeader( aSw, KW_PARAMS, true );
          OptionSetKeeper.KEEPER.write( aSw, aEntity.params() );
          aSw.writeSeparatorChar();
          aSw.writeEol();
          // extra data
          StrioUtils.writeKeywordHeader( aSw, KW_EXTRA, true );
          ((VedItemCfg)aEntity).extraData().write( aSw );
          aSw.writeEol();
        }

        @Override
        protected IVedItemCfg doRead( IStrioReader aSr ) {
          // item ID and factory ID
          String id = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          String factoryId = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          // properties values
          IOptionSet propValues = OptionSetKeeper.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          // parameters values
          StrioUtils.ensureKeywordHeader( aSr, KW_PARAMS );
          IOptionSet params = OptionSetKeeper.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          // create an item to read extra data into it
          VedItemCfg itemCfg = new VedItemCfg( id, factoryId, params );
          itemCfg.propValues().setAll( propValues );
          // extra data
          StrioUtils.ensureKeywordHeader( aSr, KW_EXTRA );
          itemCfg.extraData().read( aSr );
          return itemCfg;
        }
      };

  private final String                     factoryId;
  private final IOptionSetEdit             propValues = new OptionSet();
  private final KeepablesStorageAsKeepable extraData  = new KeepablesStorageAsKeepable();

  /**
   * Constructor.
   *
   * @param aId String - VISEL identifier
   * @param aFactoryId {@link String} - the VISEL factory ID
   * @param aParams {@link IOptionSet} - initial values of {@link #params()}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedItemCfg( String aId, String aFactoryId, IOptionSet aParams ) {
    super( aId, aParams );
    factoryId = StridUtils.checkValidIdPath( aFactoryId );
  }

  /**
   * Copy constructor.
   *
   * @param aSource {@link IVedItemCfg} - the source
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedItemCfg( IVedItemCfg aSource ) {
    super( TsNullArgumentRtException.checkNull( aSource ).id(), aSource.params() );
    factoryId = aSource.factoryId();
    propValues.setAll( aSource.propValues() );
    extraData.copyFrom( aSource.extraData() );
  }

  /**
   * Copy constructor with specifying an ID.
   *
   * @param aItemId String - the ID of the created item
   * @param aSource {@link IVedItemCfg} - the source
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public VedItemCfg( String aItemId, IVedItemCfg aSource ) {
    super( aItemId, aSource.params() );
    factoryId = aSource.factoryId();
    propValues.setAll( aSource.propValues() );
    extraData.copyFrom( aSource.extraData() );
  }

  // ------------------------------------------------------------------------------------
  // IViselCfg
  //

  @Override
  public String factoryId() {
    return factoryId;
  }

  @Override
  public IOptionSetEdit propValues() {
    return propValues;
  }

  @Override
  public KeepablesStorageAsKeepable extraData() {
    return extraData;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return String.format( "%s (factoryId=%s)", id(), factoryId.toString() ); //$NON-NLS-1$
  }

}

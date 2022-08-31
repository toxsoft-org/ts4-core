package org.toxsoft.core.txtproj.lib.impl;

import static org.toxsoft.core.txtproj.lib.impl.ITsResources.*;

import org.toxsoft.core.tslib.av.opset.INotifierOptionSetEdit;
import org.toxsoft.core.tslib.av.opset.IOptionSetEdit;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.events.change.GenericChangeEventer;
import org.toxsoft.core.tslib.bricks.events.change.IGenericChangeEventer;
import org.toxsoft.core.tslib.bricks.strid.impl.StridUtils;
import org.toxsoft.core.tslib.bricks.strio.IStrioReader;
import org.toxsoft.core.tslib.bricks.strio.IStrioWriter;
import org.toxsoft.core.tslib.bricks.strio.impl.StrioUtils;
import org.toxsoft.core.tslib.bricks.validator.ITsValidator;
import org.toxsoft.core.tslib.bricks.validator.ValidationResult;
import org.toxsoft.core.tslib.bricks.validator.impl.TsValidationFailedRtException;
import org.toxsoft.core.tslib.coll.helpers.ECrudOp;
import org.toxsoft.core.tslib.coll.notifier.basis.ITsCollectionChangeListener;
import org.toxsoft.core.tslib.coll.primtypes.IStringMap;
import org.toxsoft.core.tslib.coll.primtypes.IStringMapEdit;
import org.toxsoft.core.tslib.coll.primtypes.impl.StringMap;
import org.toxsoft.core.tslib.utils.errors.TsItemAlreadyExistsRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.core.tslib.utils.logs.impl.LoggerUtils;
import org.toxsoft.core.txtproj.lib.*;
import org.toxsoft.core.txtproj.lib.tdfile.*;

/**
 * Реализация {@link ITsProject}.
 *
 * @author hazard157
 */
public class TsProject
    implements ITsProject {

  private static final String KW_PARAMS = "ProjectParameters"; //$NON-NLS-1$

  private final ITsCollectionChangeListener paramsChangeListener = new ITsCollectionChangeListener() {

    @Override
    public void onCollectionChanged( Object aSource, ECrudOp aOp, Object aItem ) {
      genericChangeEventer.fireChangeEvent();
    }
  };

  private final ITsValidator<TsProjectFileFormatInfo> defaultValidator = new ITsValidator<>() {

    @Override
    public ValidationResult validate( TsProjectFileFormatInfo aValue ) {
      TsNullArgumentRtException.checkNull( aValue );
      if( initialFormatInfo.equals( aValue ) ) {
        return ValidationResult.SUCCESS;
      }
      return ValidationResult.error( FMT_ERR_INV_PROJ_FILE_FORMAT, initialFormatInfo.toString(), aValue.toString() );
    }
  };

  private final TsProjectContentChangeProducerHelper ccpHelper;
  final GenericChangeEventer                         genericChangeEventer;
  private final INotifierOptionSetEdit               params    = new NotifierOptionSetEditWrapper( new OptionSet() );
  private final IStringMapEdit<IProjDataUnit>        projUnits = new StringMap<>();
  private final ITdFile                              tdFile    = new TdFile();

  private ITsValidator<TsProjectFileFormatInfo> ffValidator = ITsValidator.PASS;

  final TsProjectFileFormatInfo   initialFormatInfo;
  private TsProjectFileFormatInfo formatInfo;

  /**
   * Просто конструктор.
   *
   * @param aFormatInfo {@link TsProjectFileFormatInfo} - информация о формате файла проекта
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public TsProject( TsProjectFileFormatInfo aFormatInfo ) {
    initialFormatInfo = TsNullArgumentRtException.checkNull( aFormatInfo );
    formatInfo = initialFormatInfo;
    params.addCollectionChangeListener( paramsChangeListener );
    genericChangeEventer = new GenericChangeEventer( this );
    ccpHelper = new TsProjectContentChangeProducerHelper( this );
    for( IProjDataUnit u : projUnits.values() ) {
      u.genericChangeEventer().addListener( genericChangeEventer );
    }
  }

  // ------------------------------------------------------------------------------------
  // IKeepableEntity
  //

  private TdfSection sectFromFile( String aKeyword ) {
    TdfSection s = tdFile.sections().findByKey( aKeyword );
    if( s == null ) {
      s = new TdfSection( aKeyword );
      tdFile.add( s );
    }
    // TODO генерировать сообщение onContentChange???
    return s;
  }

  @Override
  public void write( IStrioWriter aSw ) {
    TsNullArgumentRtException.checkNull( aSw );
    ccpHelper.fireBeforeSave();
    TsProjectFileFormatInfoKeeper.KEEPER.write( aSw, formatInfo );
    aSw.writeEol();
    StrioUtils.writeKeywordHeader( aSw, KW_PARAMS, true );
    OptionSetKeeper.KEEPER_INDENTED.write( aSw, params );
    aSw.writeEol();
    for( String kw : projUnits.keys() ) {
      IProjDataUnit unit = projUnits.getByKey( kw );
      TdfSection s = sectFromFile( kw );
      s.writeEntity( unit );
      aSw.writeEol();
    }
    tdFile.write( aSw );
    ccpHelper.fireAfterSave();
  }

  @Override
  public void read( IStrioReader aSr ) {
    TsNullArgumentRtException.checkNull( aSr );
    genericChangeEventer.pauseFiring();
    try {
      TsProjectFileFormatInfo ffInfo = TsProjectFileFormatInfoKeeper.KEEPER.read( aSr );
      TsValidationFailedRtException.checkError( ffValidator.validate( ffInfo ) );
      formatInfo = ffInfo;
      StrioUtils.ensureKeywordHeader( aSr, KW_PARAMS );
      params.setAll( OptionSetKeeper.KEEPER.read( aSr ) );
      tdFile.read( aSr );
      for( String kw : projUnits.keys() ) {
        IProjDataUnit unit = projUnits.getByKey( kw );
        TdfSection section = tdFile.sections().findByKey( kw );
        if( section != null ) {
          section.readEntity( unit );
        }
      }
    }
    finally {
      genericChangeEventer.resumeFiring( true );
      ccpHelper.fireChangeEvent( false );
    }
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает все текстовые данные.
   * <p>
   * Внимание: этот метод только для средств разработчика, не следует его использовать в обычных программах, это
   * небезопасно!
   *
   * @return {@link ITdFile} - все данные, в том числе неопубликованные
   */
  public ITdFile tdFile() {
    return tdFile;
  }

  // ------------------------------------------------------------------------------------
  // ITsClearableCollection
  //

  @Override
  public void clear() {
    genericChangeEventer.pauseFiring();
    for( IProjDataUnit u : projUnits.values() ) {
      try {
        u.clear();
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex );
      }
    }
    genericChangeEventer.resumeFiring( true );
    ccpHelper.fireChangeEvent( true );
  }

  // ------------------------------------------------------------------------------------
  // ITsProject
  //

  @Override
  public TsProjectFileFormatInfo initialFormatInfo() {
    return initialFormatInfo;
  }

  @Override
  public TsProjectFileFormatInfo formatInfo() {
    return formatInfo;
  }

  @Override
  public IOptionSetEdit params() {
    return params;
  }

  @Override
  public IStringMap<IProjDataUnit> units() {
    return projUnits;
  }

  @Override
  public ValidationResult registerUnit( String aId, IProjDataUnit aUnit, boolean aReadContent ) {
    StridUtils.checkValidIdPath( aId );
    TsNullArgumentRtException.checkNull( aUnit );
    TsItemAlreadyExistsRtException.checkTrue( projUnits.hasKey( aId ) );
    projUnits.put( aId, aUnit );
    aUnit.genericChangeEventer().addListener( genericChangeEventer );
    if( aReadContent ) {
      // занесем в компоненту содержимое ранее прочитанного раздела (если такой раздел был)
      TdfSection sect = tdFile.sections().findByKey( aId );
      if( sect != null ) {
        try {
          genericChangeEventer.pauseFiring();
          sect.readEntity( aUnit );
        }
        catch( Exception ex ) {
          LoggerUtils.errorLogger().error( ex );
          return ValidationResult.error( ex );
        }
        finally {
          /**
           * Игнорируем сгенерированные компонентой извещения, ведь изменилось содержимое компоненты, а не файла
           * проекта.
           */
          genericChangeEventer.resumeFiring( false );
        }
      }
    }
    // TODO генерировать сообщение onContentChange???
    return ValidationResult.SUCCESS;
  }

  @Override
  public void unregisterUnit( String aId ) {
    IProjDataUnit pdu = projUnits.removeByKey( aId );
    if( pdu != null ) {
      pdu.genericChangeEventer().removeListener( genericChangeEventer );
      // TODO генерировать сообщение onContentChange???
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsProjectContentChangeProducer
  //

  @Override
  public void addProjectContentChangeListener( ITsProjectContentChangeListener aListener ) {
    ccpHelper.addProjectContentChangeListener( aListener );
  }

  @Override
  public void removeProjectContentChangeListener( ITsProjectContentChangeListener aListener ) {
    ccpHelper.removeProjectContentChangeListener( aListener );
  }

  @Override
  public ITsValidator<TsProjectFileFormatInfo> fileFormatValidator() {
    return ffValidator;
  }

  @Override
  public void setFileFormatValidator( ITsValidator<TsProjectFileFormatInfo> aValidator ) {
    if( aValidator != null ) {
      ffValidator = aValidator;
    }
    else {
      ffValidator = defaultValidator;
    }
  }

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return genericChangeEventer;
  }

}
